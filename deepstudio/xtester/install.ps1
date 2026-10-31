# X-Tester MCP installer
#
# Delivery model:
#   This script is designed to be run directly from a URL via `irm <url> | iex`
#   (or `iwr <url> | iex`). It therefore MUST stay fully self-contained: do NOT
#   add dot-sourcing of sibling files, module imports from the repo, relative
#   `$PSScriptRoot` references, or any other dependency that does not exist when
#   the script is piped into PowerShell from the network. Everything the
#   installer needs (Python detection, venv setup, client registration, etc.)
#   has to be inlined in this single file.
#
# Privacy & compliance:
#   Because this runs on end-user machines, be mindful of privacy compliance.
#   Do NOT collect, log, transmit, or persist any personal data, telemetry,
#   credentials, tokens, or machine identifiers. Keep output limited to local
#   install progress, and only contact the explicitly configured package feeds.
# =============================================================================

param(
  [ValidateSet("private-feed", "local")]
  [string]$Source = "private-feed",

  [ValidateSet("copilot", "claude-desktop", "claude-code", "vscode", "all")]
  [string[]]$Client = @("copilot"),

  [string]$Version,

  [string]$ServerName = "xtester",

  [string]$VenvPath = (Join-Path $env:LOCALAPPDATA "XTester\mcp"),

  [string]$LocalPath,

  [string]$FeedUrl = "https://microsoft.pkgs.visualstudio.com/OS/_packaging/DeepStudio/pypi/simple/",

  [string]$ExtraIndexUrl = "https://pypi.org/simple",

  [switch]$Force,

  [switch]$SkipClientInstall,

  [switch]$Help
)

$ErrorActionPreference = "Stop"

$Script:InstallerVersion = "1.13.6"
$Script:RuntimeMutationStarted = $false

if ($Help) {
  @"
XTester installer

Parameters:
  -Source <private-feed|local>   Install source. Default: private-feed
  -Version <version>             Optional XTester package version
  -Client <name[]>               copilot, claude-desktop, claude-code, vscode, or all
  -VenvPath <path>               Managed MCP virtual environment
  -LocalPath <path>              Package root for -Source local
  -Force                         Recreate the managed environment
  -SkipClientInstall             Do not install missing client CLIs
  -Help                          Show this help without changing local state
"@
  return
}

try {
  [Console]::OutputEncoding = [System.Text.Encoding]::UTF8
  $OutputEncoding = [System.Text.Encoding]::UTF8
} catch { }

function Info([string]$Message) { Write-Host "  $Message" -ForegroundColor Cyan }
function Success([string]$Message) { Write-Host "  OK  $Message" -ForegroundColor Green }
function Warn([string]$Message) { Write-Host "  WARN $Message" -ForegroundColor Yellow }
function Fail([string]$Message) { Write-Host "  ERR $Message" -ForegroundColor Red }

function Read-OptionalPrompt([string]$Prompt, [string]$SkipMessage) {
  if ($null -eq $Host.UI.RawUI -or [Console]::IsInputRedirected) {
    Info $SkipMessage
    return $null
  }
  try {
    return Read-Host $Prompt
  }
  catch {
    Info $SkipMessage
    return $null
  }
}

function Invoke-Step([string]$Description, [scriptblock]$Script) {
  Info $Description
  & $Script
  Success $Description
}

function Invoke-External([string]$FilePath, [string[]]$Arguments) {
  & $FilePath @Arguments
  if ($LASTEXITCODE -ne 0) {
    throw "$FilePath $($Arguments -join ' ') failed with exit code $LASTEXITCODE"
  }
}

function Get-XTesterReadinessStatePath {
  if ([string]::IsNullOrWhiteSpace($env:LOCALAPPDATA)) {
    throw "LOCALAPPDATA is required to locate XTester readiness state."
  }
  return Join-Path $env:LOCALAPPDATA "XTester\state\readiness-v1.json"
}

function Invalidate-XTesterReadiness {
  $statePath = Get-XTesterReadinessStatePath
  if (Test-Path -LiteralPath $statePath) {
    Remove-Item -LiteralPath $statePath -Force -ErrorAction Stop
    Info "Invalidated previous readiness state: $statePath"
  }
}

# winget renders its download spinner with `\r` redraws. When PowerShell wraps
# stdout (any `2>&1 | ...` pipeline counts as a non-TTY consumer) every frame
# is flushed with a newline, producing a vertical column of `-`,`\\`,`|`,`/`
# (and sometimes block glyphs) in the console. Suppress those frames so the
# operator only sees real status lines.
function Test-IsWingetSpinnerLine([string]$Line) {
  if ($null -eq $Line) { return $true }
  $trimmed = $Line.Trim()
  if ([string]::IsNullOrEmpty($trimmed)) { return $false }
  # Pure spinner / progress glyphs (single char per line is the worst case).
  if ($trimmed -match '^[\-\\|/\u2580-\u259F\u2588-\u259B\u2591\u2592\u2593\u2588]+$') { return $true }
  # Spinner glyphs followed by a percentage like ` -  42 %`.
  if ($trimmed -match '^[\-\\|/\u2580-\u259F\u2588-\u259B\u2591\u2592\u2593\u2588\s]+\d+\s*%\s*$') { return $true }
  # Spinner glyphs followed by a `12.3 MB / 45.6 MB` style download counter.
  if ($trimmed -match '^[\-\\|/\u2580-\u259F\u2588-\u259B\u2591\u2592\u2593\u2588\s]+\d[\d.,]*\s*(B|KB|MB|GB)\s*/\s*\d') { return $true }
  return $false
}

function Invoke-WingetQuiet([string]$WingetPath, [string[]]$Arguments) {
  & $WingetPath @Arguments 2>&1 | ForEach-Object {
    $line = [string]$_
    if ([string]::IsNullOrWhiteSpace($line)) { return }
    if (Test-IsWingetSpinnerLine $line) { return }
    Write-Host "    $line"
  }
}

function Invoke-WithCleanPythonPath([scriptblock]$Script) {
  $previousPythonPath = $env:PYTHONPATH
  # pip's text spinner relies on \r to overwrite a single line, but when stdout
  # is wrapped by PowerShell (especially in non-TTY hosts like the VS Code
  # PowerShell terminal) each frame is flushed with a newline, producing a
  # vertical column of '-' '\\' '|' '/' glyphs. Force pip into its no-spinner
  # output mode for the duration of the install.
  $previousPipProgressBar = $env:PIP_PROGRESS_BAR
  $previousPipNoInput = $env:PIP_NO_INPUT
  $previousPipVersionCheck = $env:PIP_DISABLE_PIP_VERSION_CHECK
  $previousPipKeyringProvider = $env:PIP_KEYRING_PROVIDER
  try {
    Remove-Item Env:\PYTHONPATH -ErrorAction SilentlyContinue
    $env:PIP_PROGRESS_BAR = "off"
    $env:PIP_NO_INPUT = "1"
    $env:PIP_DISABLE_PIP_VERSION_CHECK = "1"
    # PIP_NO_INPUT=1 makes pip skip its interactive keyring path, which the
    # Azure Artifacts credential provider (artifacts-keyring) depends on, so
    # private-feed requests would fail to authenticate. Forcing the "import"
    # keyring provider makes pip load keyring in-process (no prompt, MSAL
    # silent in practice), restoring feed auth for both version discovery and
    # the actual install while staying fully non-interactive.
    $env:PIP_KEYRING_PROVIDER = "import"
    & $Script
  }
  finally {
    if ($null -ne $previousPythonPath) {
      $env:PYTHONPATH = $previousPythonPath
    }
    if ($null -eq $previousPipProgressBar) { Remove-Item Env:\PIP_PROGRESS_BAR -ErrorAction SilentlyContinue }
    else { $env:PIP_PROGRESS_BAR = $previousPipProgressBar }
    if ($null -eq $previousPipNoInput) { Remove-Item Env:\PIP_NO_INPUT -ErrorAction SilentlyContinue }
    else { $env:PIP_NO_INPUT = $previousPipNoInput }
    if ($null -eq $previousPipVersionCheck) { Remove-Item Env:\PIP_DISABLE_PIP_VERSION_CHECK -ErrorAction SilentlyContinue }
    else { $env:PIP_DISABLE_PIP_VERSION_CHECK = $previousPipVersionCheck }
    if ($null -eq $previousPipKeyringProvider) { Remove-Item Env:\PIP_KEYRING_PROVIDER -ErrorAction SilentlyContinue }
    else { $env:PIP_KEYRING_PROVIDER = $previousPipKeyringProvider }
  }
}

function Stop-XTesterMcpProcesses {
  Invoke-Step "Stopping running X-Tester MCP processes" {
    try {
      $processes = @(Get-CimInstance Win32_Process -ErrorAction Stop | Where-Object {
        $_.Name -ieq "xtester-mcp.exe" -or ($_.CommandLine -and $_.CommandLine -match '(^|[\\/\s"''])(xtester-mcp)(\.exe)?([\s"'']|$)')
      })
    }
    catch {
      Warn "Could not inspect running processes: $($_.Exception.Message)"
      return
    }

    if ($processes.Count -eq 0) {
      Info "No running xtester-mcp processes found."
      return
    }

    foreach ($entry in $processes) {
      $label = "$($entry.Name) pid=$($entry.ProcessId)"
      Info "Stopping $label"
      try {
        $process = Get-Process -Id $entry.ProcessId -ErrorAction Stop
        if ($process.CloseMainWindow()) {
          if ($process.WaitForExit(2000)) { continue }
        }
        Stop-Process -Id $entry.ProcessId -Force -ErrorAction Stop
      }
      catch {
        Warn "Could not stop ${label}: $($_.Exception.Message)"
      }
    }
  }
}

function Require-Command([string]$Name, [string]$InstallHint) {
  $cmd = Get-Command $Name -ErrorAction SilentlyContinue
  if ($cmd) { return $cmd.Source }
  Fail "$Name was not found on PATH."
  if ($InstallHint) { Info $InstallHint }
  exit 1
}

function ConvertTo-Hashtable($Value) {
  if ($null -eq $Value) { return $null }
  if ($Value -is [System.Collections.IDictionary]) { return $Value }
  if ($Value -is [System.Collections.IEnumerable] -and $Value -isnot [string]) {
    $items = @()
    foreach ($item in $Value) { $items += (ConvertTo-Hashtable $item) }
    return ,$items
  }
  if ($Value.GetType().Name -eq "PSCustomObject") {
    $table = [ordered]@{}
    foreach ($property in $Value.PSObject.Properties) {
      $table[$property.Name] = ConvertTo-Hashtable $property.Value
    }
    return $table
  }
  return $Value
}

function Remove-JsonComments([string]$Text) {
  # VS Code writes mcp.json as JSONC: it permits `//` / `/* */` comments and
  # trailing commas, neither of which Windows PowerShell 5.1's ConvertFrom-Json
  # accepts. Strip them defensively while respecting string literals so an
  # otherwise-valid VS Code file still parses. PowerShell 7+ tolerates comments
  # natively, so this is a no-op-equivalent safety net there.
  $sb = [System.Text.StringBuilder]::new()
  $inString = $false
  $escaped = $false
  $i = 0
  $len = $Text.Length
  while ($i -lt $len) {
    $ch = $Text[$i]
    $next = if ($i + 1 -lt $len) { $Text[$i + 1] } else { [char]0 }
    if ($inString) {
      [void]$sb.Append($ch)
      if ($escaped) { $escaped = $false }
      elseif ($ch -eq '\') { $escaped = $true }
      elseif ($ch -eq '"') { $inString = $false }
      $i++
      continue
    }
    if ($ch -eq '"') { $inString = $true; [void]$sb.Append($ch); $i++; continue }
    if ($ch -eq '/' -and $next -eq '/') {
      while ($i -lt $len -and $Text[$i] -ne "`n") { $i++ }
      continue
    }
    if ($ch -eq '/' -and $next -eq '*') {
      $i += 2
      while ($i + 1 -lt $len -and -not ($Text[$i] -eq '*' -and $Text[$i + 1] -eq '/')) { $i++ }
      $i += 2
      continue
    }
    [void]$sb.Append($ch)
    $i++
  }
  # Match string tokens first so JSONC cleanup cannot change option values.
  return ([regex]::Replace($sb.ToString(), '"(?:\\.|[^"\\])*"|,(?=\s*[}\]])', {
    param($match)
    if ($match.Value -eq ',') { return '' }
    return $match.Value
  }))
}

function Read-JsonObject([string]$Path, [hashtable]$DefaultValue) {
  if (-not (Test-Path -LiteralPath $Path)) { return $DefaultValue }
  $raw = Get-Content -Raw -LiteralPath $Path
  if ([string]::IsNullOrWhiteSpace($raw)) {
    throw "Existing config is empty, not an absent registration: $Path"
  }
  try {
    $parsed = ConvertTo-Hashtable ($raw | ConvertFrom-Json)
  }
  catch {
    # Retry once after stripping JSONC comments / trailing commas, which is the
    # common reason a hand- or VS Code-authored config fails strict parsing.
    try {
      $parsed = ConvertTo-Hashtable ((Remove-JsonComments $raw) | ConvertFrom-Json)
    }
    catch {
      throw "The existing config file is not valid JSON and could not be parsed: $Path`n" +
            "  Parser error: $($_.Exception.Message)`n" +
            "  Fix or delete that file (e.g. reset it to '{}') and re-run the installer."
    }
  }
  if ($parsed -isnot [System.Collections.IDictionary]) {
    throw "Existing config must contain a JSON object: $Path"
  }
  return $parsed
}

function Write-JsonObject([string]$Path, $Value) {
  $dir = Split-Path -Parent $Path
  if ($dir) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
  $Value | ConvertTo-Json -Depth 20 | Set-Content -LiteralPath $Path -Encoding UTF8
}

function Resolve-LocalPackagePath {
  if (-not $LocalPath) {
    throw "-Source local requires -LocalPath pointing at the XTester package directory (e.g. -LocalPath C:\path\to\xtester)."
  }
  if (-not (Test-Path -LiteralPath $LocalPath)) {
    throw "Local package path does not exist: $LocalPath"
  }
  return (Resolve-Path -LiteralPath $LocalPath).Path
}

# Python detection / installation helpers (inlined and self-contained).
  function Write-PyInfo([string]$Message) { Write-Host "  [python] $Message" -ForegroundColor Cyan }
  function Write-PyWarn([string]$Message) { Write-Host "  [python] WARN $Message" -ForegroundColor Yellow }
  function Write-PyFail([string]$Message) { Write-Host "  [python] ERR  $Message" -ForegroundColor Red }
  function Write-PyOk  ([string]$Message) { Write-Host "  [python] OK   $Message" -ForegroundColor Green }

  function Get-HostArchitecture {
    try { return [System.Runtime.InteropServices.RuntimeInformation]::OSArchitecture.ToString() }
    catch { return $env:PROCESSOR_ARCHITECTURE }
  }

  function Test-IsWindowsArm64Host {
    $isWindowsHost = $false
    try { $isWindowsHost = $IsWindows } catch { }
    if (-not $isWindowsHost -and $env:OS -eq "Windows_NT") { $isWindowsHost = $true }
    if (-not $isWindowsHost) { return $false }
    return (Get-HostArchitecture) -match "^(Arm64|AArch64)$"
  }

  function Get-PythonMachine([string]$FilePath, [string[]]$Arguments) {
    try {
      $output = & $FilePath @Arguments -c "import platform; print(platform.machine())" 2>&1
      if ($LASTEXITCODE -ne 0) { return $null }
      return ($output | Out-String).Trim()
    } catch { return $null }
  }

  function Test-IsUnsupportedWindowsArm64Python([string]$FilePath, [string[]]$Arguments) {
    if (-not (Test-IsWindowsArm64Host)) { return $false }
    $machine = Get-PythonMachine $FilePath $Arguments
    return $machine -match "^(ARM64|AARCH64)$"
  }

  function Test-IsMsys2OrMinGWPython([string[]]$PathCandidates) {
    # MSYS2 / MinGW Python (ucrt64 / mingw64 / clang64, etc.) is a Windows .exe
    # but builds virtualenvs with a POSIX layout (bin/python, NO
    # Scripts\python.exe), which breaks every downstream step that expects the
    # native Windows venv layout (the venv ends up "missing python.exe"). Detect
    # it by its unmistakable install-root markers and skip it, the same way the
    # Microsoft Store stub is skipped.
    foreach ($p in $PathCandidates) {
      if ($p -and $p -match '(?i)[\\/](msys2?|msys32|msys64|mingw32|mingw64|ucrt64|ucrt32|clang64|clang32|clangarm64|cygwin|cygwin64)[\\/]') {
        return $true
      }
    }
    return $false
  }

  function Test-PythonExecutable([string]$FilePath, [string[]]$Arguments) {
    if (-not $FilePath) { return $null }
    $label = if ($Arguments -and $Arguments.Count -gt 0) { "$FilePath $($Arguments -join ' ')" } else { "$FilePath" }
    try { $output = & $FilePath @Arguments --version 2>&1 }
    catch { Write-PyWarn "Skipping ${label}: launcher threw $($_.Exception.Message)"; return $null }
    if ($LASTEXITCODE -ne 0) { Write-PyWarn "Skipping ${label}: '--version' exited with code $LASTEXITCODE"; return $null }
    $text = ($output | Out-String).Trim()
    if ([string]::IsNullOrWhiteSpace($text)) { return $null }
    if ($text -notmatch "Python\s+(\d+)\.(\d+)\.(\d+)") { Write-PyWarn "Skipping ${label}: unrecognised version output '$text'"; return $null }
    $major = [int]$Matches[1]; $minor = [int]$Matches[2]
    if ($major -lt 3 -or ($major -eq 3 -and $minor -lt 11)) { Write-PyWarn "Skipping ${label}: $text is older than Python 3.11"; return $null }
    if (Test-IsUnsupportedWindowsArm64Python $FilePath $Arguments) {
      Write-PyWarn "${label}: native ARM64 Python detected. Modern wheels for cryptography/keyring/artifacts-keyring exist on win_arm64; if pip later falls back to a source build, re-run with x64 Python 3.12."
    }
    if (Test-IsMsys2OrMinGWPython @($FilePath)) {
      Write-PyWarn "Skipping ${label}: MSYS2/MinGW Python builds POSIX-layout venvs (bin/python, no Scripts\python.exe) that the installer cannot use. Install a python.org or 'winget install -e --id Python.Python.3.12' interpreter instead."
      return $null
    }
    try {
      $sysExe = & $FilePath @Arguments -c "import sys; print(sys.executable)" 2>&1
      if ($LASTEXITCODE -eq 0) {
        $sysExeText = ($sysExe | Out-String).Trim()
        if ($sysExeText -match "\\WindowsApps\\" -or $sysExeText -match "\\Packages\\PythonSoftwareFoundation\.Python\.") {
          Write-PyWarn "Skipping ${label}: Microsoft Store Python at '$sysExeText' redirects venv writes"
          return $null
        }
        if (Test-IsMsys2OrMinGWPython @($sysExeText)) {
          Write-PyWarn "Skipping ${label}: MSYS2/MinGW Python at '$sysExeText' builds POSIX-layout venvs the installer cannot use. Use a python.org or winget Python instead."
          return $null
        }
      }
    } catch { }
    return [pscustomobject]@{ FilePath = $FilePath; Arguments = $Arguments; Version = $text }
  }

  function Get-RegistryPythonInstalls {
    $results = @()
    $roots = @("HKCU:\Software\Python", "HKLM:\Software\Python", "HKLM:\Software\WOW6432Node\Python")
    foreach ($root in $roots) {
      if (-not (Test-Path -LiteralPath $root)) { continue }
      $companies = Get-ChildItem -LiteralPath $root -ErrorAction SilentlyContinue
      foreach ($company in $companies) {
        if ($company.PSChildName -ne "PythonCore") { continue }
        $tags = Get-ChildItem -LiteralPath $company.PSPath -ErrorAction SilentlyContinue
        foreach ($tag in $tags) {
          $installPathKey = Join-Path $tag.PSPath "InstallPath"
          if (-not (Test-Path -LiteralPath $installPathKey)) { continue }
          try {
            $props = Get-ItemProperty -LiteralPath $installPathKey -ErrorAction Stop
            $dir = $props."(default)"
            if (-not $dir) { $dir = $props."ExecutablePath" | Split-Path -Parent -ErrorAction SilentlyContinue }
            if ($dir -and (Test-Path -LiteralPath $dir)) {
              $exe = Join-Path $dir "python.exe"
              if (Test-Path -LiteralPath $exe) { $results += $exe }
            }
          } catch { }
        }
      }
    }
    return ($results | Select-Object -Unique)
  }

  function Find-PythonCandidate {
    $candidates = @()
    $isRealPython = {
      param($cmd)
      if (-not $cmd) { return $false }
      if ($cmd.Source -and $cmd.Source -match "WindowsApps\\python(3)?\.exe$") { return $false }
      return $true
    }
    foreach ($name in @("python", "python3")) {
      $cmd = Get-Command $name -ErrorAction SilentlyContinue
      if ($cmd -and -not (& $isRealPython $cmd)) {
        Write-PyWarn "Skipping ${name}: '$($cmd.Source)' is the Microsoft Store WindowsApps stub"
      } elseif (& $isRealPython $cmd) {
        $candidates += ,@($cmd.Source, @())
      }
    }
    $py = Get-Command "py" -ErrorAction SilentlyContinue
    if ($py) { foreach ($flag in @("-3.12", "-3.11", "-3.13", "-3.14", "-3")) { $candidates += ,@($py.Source, @($flag)) } }
    foreach ($exe in (Get-RegistryPythonInstalls)) { $candidates += ,@($exe, @()) }
    $installRoots = @(
      (Join-Path $env:LOCALAPPDATA "Programs\Python"),
      (Join-Path $env:ProgramFiles "Python"),
      $env:ProgramFiles,
      ${env:ProgramFiles(x86)}
    )
    foreach ($root in $installRoots) {
      if (-not $root -or -not (Test-Path -LiteralPath $root)) { continue }
      $found = Get-ChildItem -LiteralPath $root -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -match "^Python3(1[1-9]|[2-9][0-9])(x64|-x64)?$" } |
        Sort-Object Name -Descending
      foreach ($dir in $found) {
        $exe = Join-Path $dir.FullName "python.exe"
        if (Test-Path -LiteralPath $exe) { $candidates += ,@($exe, @()) }
      }
    }
    $wingetPackages = Join-Path $env:LOCALAPPDATA "Microsoft\WinGet\Packages"
    if (Test-Path -LiteralPath $wingetPackages) {
      $pythonPkgs = Get-ChildItem -LiteralPath $wingetPackages -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -like "Python.Python.3.*" } | Sort-Object Name -Descending
      foreach ($pkg in $pythonPkgs) {
        $exes = Get-ChildItem -LiteralPath $pkg.FullName -Recurse -Filter python.exe -ErrorAction SilentlyContinue -Depth 4
        foreach ($exe in $exes) { $candidates += ,@($exe.FullName, @()) }
      }
    }
    $wingetLinks = Join-Path $env:LOCALAPPDATA "Microsoft\WinGet\Links"
    if (Test-Path -LiteralPath $wingetLinks) {
      foreach ($name in @("python.exe", "python3.exe", "python3.12.exe", "python3.11.exe")) {
        $exe = Join-Path $wingetLinks $name
        if (Test-Path -LiteralPath $exe) { $candidates += ,@($exe, @()) }
      }
    }
    # Scoop user-scope installs (no admin, common workaround on machines where
    # winget is blocked by org policy).
    $scoopApps = Join-Path $env:USERPROFILE "scoop\apps"
    if (Test-Path -LiteralPath $scoopApps) {
      $scoopPythonDirs = Get-ChildItem -LiteralPath $scoopApps -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -match "^python(3(1[1-9]|[2-9][0-9])?)?$" } | Sort-Object Name -Descending
      foreach ($dir in $scoopPythonDirs) {
        $exe = Join-Path $dir.FullName "current\python.exe"
        if (Test-Path -LiteralPath $exe) { $candidates += ,@($exe, @()) }
      }
    }
    foreach ($exe in @("C:\tools\python", "C:\tools\python3")) {
      $exePath = Join-Path $exe "python.exe"
      if (Test-Path -LiteralPath $exePath) { $candidates += ,@($exePath, @()) }
    }
    $chocoVersioned = Get-ChildItem -LiteralPath "C:\" -Directory -ErrorAction SilentlyContinue |
      Where-Object { $_.Name -match "^Python3(1[1-9]|[2-9][0-9])$" } | Sort-Object Name -Descending
    foreach ($dir in $chocoVersioned) {
      $exe = Join-Path $dir.FullName "python.exe"
      if (Test-Path -LiteralPath $exe) { $candidates += ,@($exe, @()) }
    }
    if ($candidates.Count -eq 0) {
      Write-PyWarn "No Python candidates were discovered."
      Write-PyInfo "Scanned: PATH (python, python3), py.exe launcher, HKCU/HKLM\Software\Python\PythonCore registry, %LOCALAPPDATA%\Programs\Python, %ProgramFiles%\Python, %ProgramFiles%, %ProgramFiles(x86)%, %LOCALAPPDATA%\Microsoft\WinGet\Packages\Python.Python.3.*, %LOCALAPPDATA%\Microsoft\WinGet\Links, %USERPROFILE%\scoop\apps\python*, C:\tools\python*, C:\Python3*."
    }
    foreach ($pair in $candidates) {
      $result = Test-PythonExecutable $pair[0] $pair[1]
      if ($result) { return $result }
    }
    return $null
  }

  function Install-PythonViaWinget {
    param([switch]$ForceX64SideBySide)
    $winget = Get-Command "winget" -ErrorAction SilentlyContinue
    if (-not $winget) { Write-PyWarn "winget is not available; cannot auto-install Python."; return $false }
    $stepLabel = if ($ForceX64SideBySide) { "Installing x64 Python 3.12 alongside existing ARM64 (Python.Python.3.12 --architecture x64 --force)" } else { "Installing Python 3.12 via winget (Python.Python.3.12)" }
    Write-PyInfo $stepLabel
    $wingetArgs = @("install", "-e", "--id", "Python.Python.3.12", "--scope", "user")
    if (Test-IsWindowsArm64Host) { $wingetArgs += @("--architecture", "x64") }
    if ($ForceX64SideBySide) {
      $sideBySideRoot = Join-Path $env:LOCALAPPDATA "Programs\Python\Python312x64"
      $wingetArgs += @("--force", "--location", $sideBySideRoot)
    }
    $wingetArgs += @("--accept-source-agreements", "--accept-package-agreements", "--silent", "--disable-interactivity")
    $script:WingetBlockedByPolicy = $false
    & $winget.Source @wingetArgs 2>&1 | ForEach-Object {
      $line = [string]$_
      if ([string]::IsNullOrWhiteSpace($line)) { return }
      if ($line -match '^[\s\u2580-\u259F\u2588-\u259B\-\\|/]+\s*\d+\s*%\s*$') { return }
      if ($line -match '^[\s\u2580-\u259F\u2588-\u259B\-\\|/]+\s*\d[\d.,]*\s*(KB|MB|GB)\s*/\s*\d') { return }
      if ($line -match '^[\s\-\\|/]+$') { return }
      if ($line -match 'Organization policies are preventing installation' -or $line -match 'exit code:\s*1625') {
        $script:WingetBlockedByPolicy = $true
      }
      Write-Host "    $line"
    }
    if ($LASTEXITCODE -ne 0) {
      Write-PyWarn "winget exited with code $LASTEXITCODE (this can mean 'already installed' or 'no upgrade needed' and is often safe to ignore)."
    }
    # Clean up partial Python install roots winget left behind on policy / 1603 abort.
    $localProgramsPythonRoot = Join-Path $env:LOCALAPPDATA "Programs\Python"
    if (Test-Path -LiteralPath $localProgramsPythonRoot) {
      Get-ChildItem -LiteralPath $localProgramsPythonRoot -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -match "^Python3(1[1-9]|[2-9][0-9])(x64|-x64)?$" } |
        ForEach-Object {
          $rootPython = Join-Path $_.FullName "python.exe"
          if (-not (Test-Path -LiteralPath $rootPython)) {
            Write-PyWarn "Cleaning up partial Python install (no python.exe at root): $($_.FullName)"
            try { Remove-Item -LiteralPath $_.FullName -Recurse -Force -ErrorAction Stop }
            catch { Write-PyWarn "Could not remove $($_.FullName): $($_.Exception.Message)" }
          }
        }
    }
    $machinePath = [Environment]::GetEnvironmentVariable("Path", "Machine")
    $userPath = [Environment]::GetEnvironmentVariable("Path", "User")
    $extra = @()
    $wingetLinks = Join-Path $env:LOCALAPPDATA "Microsoft\WinGet\Links"
    if (Test-Path -LiteralPath $wingetLinks) { $extra += $wingetLinks }
    $localProgramsPython = Join-Path $env:LOCALAPPDATA "Programs\Python"
    if (Test-Path -LiteralPath $localProgramsPython) {
      Get-ChildItem -LiteralPath $localProgramsPython -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -match "^Python3(1[1-9]|[2-9][0-9])(x64|-x64)?$" } |
        ForEach-Object { $extra += $_.FullName; $extra += (Join-Path $_.FullName "Scripts") }
    }
    $scoopShims = Join-Path $env:USERPROFILE "scoop\shims"
    if (Test-Path -LiteralPath $scoopShims) { $extra += $scoopShims }
    $env:Path = (@($machinePath, $userPath) + $extra | Where-Object { $_ }) -join ";"
    return $true
  }

  function Install-Scoop {
    $scoopShim = Join-Path $env:USERPROFILE "scoop\shims\scoop.ps1"
    if (Test-Path -LiteralPath $scoopShim) { return $true }
    Write-PyInfo "scoop is not installed. Bootstrapping scoop (user-scope, no admin required)..."
    try {
      $current = Get-ExecutionPolicy -Scope CurrentUser -ErrorAction SilentlyContinue
      if ($current -eq "Restricted" -or $current -eq "AllSigned" -or $current -eq "Undefined") {
        Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned -Force -ErrorAction Stop
      }
      $installer = "$env:TEMP\install-scoop.ps1"
      Invoke-WebRequest -UseBasicParsing -Uri "https://get.scoop.sh" -OutFile $installer -ErrorAction Stop
      & powershell -NoProfile -ExecutionPolicy Bypass -File $installer -RunAsAdmin:$false 2>&1 | ForEach-Object {
        $line = [string]$_
        if ([string]::IsNullOrWhiteSpace($line)) { return }
        Write-Host "    $line"
      }
      Remove-Item -LiteralPath $installer -Force -ErrorAction SilentlyContinue
    } catch {
      Write-PyWarn "Failed to bootstrap scoop: $($_.Exception.Message)"
      return $false
    }
    $scoopShims = Join-Path $env:USERPROFILE "scoop\shims"
    if (Test-Path -LiteralPath $scoopShims) {
      $env:Path = ($env:Path.TrimEnd(';') + ";" + $scoopShims)
    }
    return (Test-Path -LiteralPath $scoopShim)
  }

  function Install-PythonViaScoop {
    $scoop = Get-Command "scoop" -ErrorAction SilentlyContinue
    if (-not $scoop) {
      if (-not (Install-Scoop)) {
        Write-PyInfo "scoop is not available and could not be bootstrapped automatically. Install scoop from https://scoop.sh and re-run, or install Python manually."
        return $false
      }
      $scoop = Get-Command "scoop" -ErrorAction SilentlyContinue
      if (-not $scoop) {
        $scoopPs1 = Join-Path $env:USERPROFILE "scoop\shims\scoop.ps1"
        if (Test-Path -LiteralPath $scoopPs1) {
          $scoop = [pscustomobject]@{ Source = $scoopPs1 }
        } else { return $false }
      }
    }
    Write-PyInfo "Falling back to scoop: scoop install python (user-scope, no admin required)"
    & $scoop.Source install python 2>&1 | ForEach-Object {
      $line = [string]$_
      if ([string]::IsNullOrWhiteSpace($line)) { return }
      Write-Host "    $line"
    }
    if ($LASTEXITCODE -ne 0) { Write-PyWarn "scoop install python exited with code $LASTEXITCODE." }
    $scoopShims = Join-Path $env:USERPROFILE "scoop\shims"
    if (Test-Path -LiteralPath $scoopShims) {
      $env:Path = ($env:Path.TrimEnd(';') + ";" + $scoopShims)
    }
    return $true
  }

  function Resolve-Python {
    $found = Find-PythonCandidate
    if ($found) { return $found }
    Write-PyWarn "No working Python 3.11+ interpreter was found on PATH."
    Write-PyInfo "Attempting automatic install via winget..."
    $installed = Install-PythonViaWinget
    if ($script:WingetBlockedByPolicy) {
      Write-PyWarn "winget reported the install was blocked by organization policy (1625)."
      if (Install-PythonViaScoop) {
        $found = Find-PythonCandidate
        if ($found) { return $found }
      }
    }
    if ($installed) {
      $found = Find-PythonCandidate
      if ($found) { return $found }
      if (Test-IsWindowsArm64Host) {
        Write-PyWarn "Existing Python.Python.3.12 appears to be ARM64; forcing a side-by-side x64 install."
        $forced = Install-PythonViaWinget -ForceX64SideBySide
        if ($forced) {
          $found = Find-PythonCandidate
          if ($found) { return $found }
        }
      }
      Write-PyWarn "Python was installed but is not yet visible to this session."
      Write-PyInfo "Open a new PowerShell window and re-run this installer."
    }
    if (-not $script:WingetBlockedByPolicy) {
      if (Install-PythonViaScoop) {
        $found = Find-PythonCandidate
        if ($found) { return $found }
      }
    }
    Write-PyFail "No working Python 3.11+ interpreter was found."
    Write-PyInfo "Tried: python, python3, py -3.12, py -3.11, py -3.13, py -3.14, py -3, HKCU/HKLM\Software\Python\PythonCore registry, %LOCALAPPDATA%\Programs\Python, %ProgramFiles%\Python, %ProgramFiles%, %ProgramFiles(x86)%, %LOCALAPPDATA%\Microsoft\WinGet\Packages\Python.Python.3.*, %LOCALAPPDATA%\Microsoft\WinGet\Links, %USERPROFILE%\scoop\apps\python*, C:\tools\python*, C:\Python3*."
    Write-PyInfo "The Microsoft Store stub at WindowsApps\python.exe is intentionally ignored."
    Write-PyInfo "Install Python manually with one of:"
    Write-PyInfo "  scoop install python   (user-scope, works when winget is blocked by org policy)"
    Write-PyInfo "  winget install -e --id Python.Python.3.12"
    Write-PyInfo "  choco install python --version=3.12"
    Write-PyInfo "  https://www.python.org/downloads/"
    Write-PyInfo "Then open a new terminal and re-run this installer."
    throw "No working Python 3.11+ interpreter was found."
  }

function Resolve-ManagedVenvPython {
  # Returns the venv's python.exe, transparently following a Microsoft Store /
  # UWP write-redirect when the direct path is absent. Returns $null when the
  # venv has no usable interpreter. On a redirect hit, repoints $Script:VenvPath
  # at the redirected location so later steps (pip install, xtester-mcp.exe
  # resolution) use the real on-disk venv.
  $direct = Join-Path $VenvPath "Scripts\python.exe"
  if (Test-Path -LiteralPath $direct) { return $direct }

  # Microsoft Store / UWP Python redirects writes under %LOCALAPPDATA% into
  # %LOCALAPPDATA%\Packages\PythonSoftwareFoundation.Python.*\LocalCache\Local\<rest>.
  $localAppData = $env:LOCALAPPDATA
  if ($localAppData -and $VenvPath.StartsWith($localAppData, [System.StringComparison]::OrdinalIgnoreCase)) {
    $relative = $VenvPath.Substring($localAppData.Length).TrimStart('\','/')
    $packagesRoot = Join-Path $localAppData "Packages"
    if (Test-Path -LiteralPath $packagesRoot) {
      $candidates = Get-ChildItem -LiteralPath $packagesRoot -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -like "PythonSoftwareFoundation.Python.*" }
      foreach ($pkg in $candidates) {
        $candidateVenv = Join-Path $pkg.FullName (Join-Path "LocalCache\Local" $relative)
        $candidatePython = Join-Path $candidateVenv "Scripts\python.exe"
        if (Test-Path -LiteralPath $candidatePython) {
          Warn "Microsoft Store Python redirected the venv into $($pkg.Name)."
          Warn "Using redirected venv: $candidateVenv"
          Info "To avoid this, install Python from https://www.python.org/downloads/ or via 'winget install -e --id Python.Python.3.12'."
          $Script:VenvPath = $candidateVenv
          return $candidatePython
        }
      }
    }
  }
  return $null
}

function New-ManagedVenv($PythonInfo) {
  if ($Force -and (Test-Path -LiteralPath $VenvPath)) {
    Start-XTesterRuntimeMutation
    Invoke-Step "Removing existing managed venv: $VenvPath" {
      Remove-Item -LiteralPath $VenvPath -Recurse -Force
    }
  }

  # A venv directory left behind by a previously aborted run can exist without a
  # usable interpreter (e.g. `python -m venv` failed midway, or a later step
  # threw after the directory was partially created). Because creation is skipped
  # when the directory already exists, that stale shell would otherwise make
  # every future run fail with "Managed venv is missing python.exe". Detect an
  # incomplete venv up front and rebuild it instead of failing.
  if ((Test-Path -LiteralPath $VenvPath) -and -not (Resolve-ManagedVenvPython)) {
    Start-XTesterRuntimeMutation
    Warn "Existing managed venv at $VenvPath is incomplete (no usable python.exe); recreating it."
    Invoke-Step "Removing incomplete managed venv: $VenvPath" {
      Remove-Item -LiteralPath $VenvPath -Recurse -Force -ErrorAction SilentlyContinue
    }
  }

  if (-not (Test-Path -LiteralPath $VenvPath)) {
    Start-XTesterRuntimeMutation
    Invoke-Step "Creating managed X-Tester venv: $VenvPath" {
      $venvArgs = @()
      if ($PythonInfo.Arguments) { $venvArgs += $PythonInfo.Arguments }
      $venvArgs += @("-m", "venv", $VenvPath)
      Invoke-External $PythonInfo.FilePath $venvArgs
    }
  }

  $venvPython = Resolve-ManagedVenvPython
  if (-not $venvPython) {
    throw "Managed venv is missing python.exe after creation: $(Join-Path $VenvPath 'Scripts\python.exe').`n" +
          "  The base interpreter ($($PythonInfo.Version)) may have failed to provision the venv.`n" +
          "  Re-run with -Force, or install python.org Python 3.12 (not the Microsoft Store build) and retry."
  }
  return $venvPython
}

function Get-LatestXTesterVersionFromFeed([string]$VenvPython) {
  # Ask the feed directly for the newest published version instead of trusting
  # pip's `-U` against a possibly stale cached/locally-known index. We then pin
  # the install to that exact version so the result is deterministic.
  #
  # `pip index versions` prints the latest as the first token, e.g.
  #   xtester (0.0.23)
  #   Available versions: 0.0.23, 0.0.22, ...
  # Feed auth works here because the surrounding Invoke-WithCleanPythonPath sets
  # PIP_KEYRING_PROVIDER=import (see that function for why).
  #
  # The Azure Artifacts credential provider writes benign informational lines
  # (e.g. "[Information] [CredentialProvider]... Acquired bearer token using
  # 'MSAL Silent'") to *stderr*. Under the script-wide $ErrorActionPreference =
  # "Stop", Windows PowerShell 5.1 turns native-command stderr captured via
  # `2>&1` into a terminating NativeCommandError, aborting the installer even
  # though the call succeeded. Relax the preference locally so those stderr
  # lines are captured as plain text and we can rely on $LASTEXITCODE instead.
  $raw = & {
    $ErrorActionPreference = "Continue"
    & $VenvPython @(
      "-m", "pip", "index", "versions", "xtester",
      "--no-cache-dir",
      "--index-url", $FeedUrl,
      "--extra-index-url", $ExtraIndexUrl
    ) 2>&1
  }
  if ($LASTEXITCODE -ne 0) { return $null }
  $text = ($raw | Out-String)
  $match = [regex]::Match($text, 'xtester\s*\(([^)]+)\)')
  if ($match.Success) { return $match.Groups[1].Value.Trim() }
  $match = [regex]::Match($text, 'LATEST:\s*([^\s]+)')
  if ($match.Success) { return $match.Groups[1].Value.Trim() }
  return $null
}

function Get-XTesterLauncherRepairVersion([string]$VenvPython) {
  # This probe must also work with older installed XTester versions, so it
  # uses Python's metadata rather than importing a new package helper. Single
  # quotes in Python survive Windows PowerShell 5.1 native argument passing.
  $probe = @'
import os
import re
import sys
import sysconfig
from importlib.metadata import distribution
from pathlib import Path

dist = distribution('xtester')
version = dist.version
if not re.fullmatch(r'[0-9][A-Za-z0-9.!+_-]*', version):
    raise SystemExit('Invalid installed XTester version metadata')
names = {entry.name for entry in dist.entry_points if entry.group == 'console_scripts'}
if not {'xtester', 'xtester-mcp'} <= names:
    raise SystemExit('XTester metadata is missing required console_scripts')
if any(not re.fullmatch(r'[A-Za-z0-9][A-Za-z0-9._-]*', name) or name.endswith('.') for name in names):
    raise SystemExit('Invalid XTester console_scripts name')
scripts = Path(sysconfig.get_path('scripts')).resolve()
suffix = '.exe' if os.name == 'nt' else ''
missing = []
for name in sorted(names):
    launcher = scripts / (name + suffix)
    if launcher.resolve().parent != scripts or not launcher.is_file() or not os.access(launcher, os.X_OK):
        missing.append(name)
if missing:
    print('Missing XTester launchers: ' + ', '.join(missing), file=sys.stderr)
    print(version)
'@
  $repairVersion = & $VenvPython -I -c $probe
  if ($LASTEXITCODE -ne 0) {
    throw "Could not validate managed XTester launcher metadata (exit code $LASTEXITCODE). Client registration stopped."
  }
  return $repairVersion
}

function Ensure-XTesterLaunchers([string]$VenvPython, [string]$WheelPath = "") {
  $repairVersion = Get-XTesterLauncherRepairVersion $VenvPython
  if ([string]::IsNullOrWhiteSpace($repairVersion)) { return }

  Warn "Repairing missing XTester launchers at installed version $repairVersion without changing dependencies."
  Start-XTesterRuntimeMutation
  $repairArgs = @(
    "-m", "pip", "install", "--progress-bar", "off", "--force-reinstall", "--no-deps",
    "xtester==$repairVersion"
  )
  if ($WheelPath) {
    $repairArgs += $WheelPath
  } else {
    $repairArgs += @("--index-url", $FeedUrl, "--extra-index-url", $ExtraIndexUrl)
  }
  # Do not include feed URLs in an exception: they may contain credentials.
  & $VenvPython @repairArgs
  if ($LASTEXITCODE -ne 0) {
    throw "XTester launcher repair failed (exit code $LASTEXITCODE). Client registration stopped."
  }
  if (Get-XTesterLauncherRepairVersion $VenvPython) {
    throw "XTester launchers are still missing after one repair attempt. Client registration stopped."
  }
}

function Start-XTesterRuntimeMutation {
  if (-not $script:RuntimeMutationStarted) {
    Stop-XTesterMcpProcesses
    $script:RuntimeMutationStarted = $true
  }
}

function Get-XTesterInstalledVersion([string]$VenvPython) {
  $probe = @'
from importlib.metadata import PackageNotFoundError, version
try:
    print(version('xtester'))
except PackageNotFoundError:
    pass
'@
  $installed = & $VenvPython -I -c $probe
  if ($LASTEXITCODE -ne 0) { throw "Could not read managed XTester version metadata." }
  return $installed
}

function Install-XTesterIntoVenv([string]$VenvPython) {
  Invoke-WithCleanPythonPath {
    $targetVersion = $Version
    if ($Source -ne "local" -and [string]::IsNullOrWhiteSpace($targetVersion)) {
      Info "Resolving latest X-Tester version from private feed"
      $targetVersion = Get-LatestXTesterVersionFromFeed $VenvPython
      if ([string]::IsNullOrWhiteSpace($targetVersion)) {
        Warn "Could not resolve latest version from feed; falling back to plain 'latest' install. Installed version is not confirmed current."
      } else {
        Success "Latest X-Tester on feed: $targetVersion"
      }
    }
    if ($Source -ne "local" -and -not $Force -and $targetVersion) {
      $installedVersion = Get-XTesterInstalledVersion $VenvPython
      if ($installedVersion -eq $targetVersion) {
        Ensure-XTesterLaunchers $VenvPython
        if (-not (Test-XTesterRemoteDependencies $VenvPython)) {
          Warn "Repairing missing Remote dependencies at installed XTester version $installedVersion."
          Start-XTesterRuntimeMutation
          Invoke-External $VenvPython @(
            "-m", "pip", "install", "--progress-bar", "off",
            "--index-url", $FeedUrl, "--extra-index-url", $ExtraIndexUrl,
            "xtester[remote]==$installedVersion"
          )
          if (-not (Test-XTesterRemoteDependencies $VenvPython)) {
            throw "Remote dependencies are still unavailable after one repair attempt. Client registration stopped."
          }
        }
        Success "Managed XTester $installedVersion is current; skipped package and bulk dependency upgrades."
        return
      }
    }
    Start-XTesterRuntimeMutation
    Invoke-Step "Upgrading pip and installing private-feed auth helpers" {
      Invoke-External $VenvPython @("-m", "pip", "install", "--progress-bar", "off", "-U", "pip")
      Invoke-External $VenvPython @("-m", "pip", "install", "--progress-bar", "off", "-U", "--prefer-binary", "keyring", "artifacts-keyring")
    }

    if ($Source -eq "local") {
      $path = Resolve-LocalPackagePath
      # Build a wheel first so the local path exercises the same package-data
      # behavior as the private-feed installation.
      $distDir = Join-Path $path "dist"
      $buildDir = Join-Path $path "build"
      Invoke-Step "Building X-Tester wheel from local source: $path" {
        if (Test-Path -LiteralPath $distDir)  { Remove-Item -Recurse -Force $distDir }
        if (Test-Path -LiteralPath $buildDir) { Remove-Item -Recurse -Force $buildDir }
        Invoke-External $VenvPython @("-m", "pip", "install", "--progress-bar", "off", "-U", "build")
        Push-Location $path
        try {
          Invoke-External $VenvPython @("-m", "build", "--wheel", "--outdir", $distDir)
        } finally {
          Pop-Location
        }
      }
      $wheel = Get-ChildItem -LiteralPath $distDir -Filter "xtester-*.whl" -ErrorAction SilentlyContinue | Sort-Object LastWriteTime -Descending | Select-Object -First 1
      if (-not $wheel) { throw "No xtester wheel produced under $distDir" }
      Invoke-Step "Installing X-Tester wheel: $($wheel.Name)" {
        Invoke-External $VenvPython @("-m", "pip", "install", "--progress-bar", "off", "--force-reinstall", "--no-deps", $wheel.FullName)
        Invoke-External $VenvPython @("-m", "pip", "install", "--progress-bar", "off", "-U", "$($wheel.FullName)[remote]")
      }
      Ensure-XTesterLaunchers $VenvPython $wheel.FullName
      return
    }

    if ([string]::IsNullOrWhiteSpace($targetVersion)) {
      $packageSpec = "xtester[remote]"
      $installLabel = "latest X-Tester from private feed"
    } else {
      $packageSpec = "xtester[remote]==$targetVersion"
      $installLabel = "X-Tester $packageSpec from private feed"
    }

    Invoke-Step "Installing $installLabel" {
      # --no-cache-dir forces pip to re-fetch the simple index instead of
      # reusing a stale cached index page. Without it, a freshly published
      # version on the private feed can be ignored (pip's -U only upgrades
      # within the versions it already knows about), leaving the venv pinned
      # to an older release even though a newer one is available.
      $primaryExtraIndexUrl = $ExtraIndexUrl
      $fallbackExtraIndexUrl = "https://packagefeedproxy.microsoft.io/pypi/simple/"
      $pipArgs = @(
        "-m", "pip", "install", "--progress-bar", "off", "-U", "--prefer-binary", "--no-cache-dir",
        "--index-url", $FeedUrl,
        "--extra-index-url", $primaryExtraIndexUrl,
        $packageSpec
      )

      try {
        Invoke-External $VenvPython $pipArgs
      }
      catch {
        $errorText = "$($_.Exception.Message)"
        $normalize = {
          param([string]$url)
          if ([string]::IsNullOrWhiteSpace($url)) { return "" }
          return $url.Trim().TrimEnd('/').ToLowerInvariant()
        }
        $isNetworkFetchFailure = (
          $errorText -match '(?i)ssl|tls|handshake|certificate|connection|timed out|read timed out|proxy|files\.pythonhosted\.org|pypi\.org'
        )
        $shouldRetryWithProxy = (
          $Source -eq "private-feed" -and
          (& $normalize $primaryExtraIndexUrl) -ne (& $normalize $fallbackExtraIndexUrl) -and
          $isNetworkFetchFailure
        )

        if (-not $shouldRetryWithProxy) {
          throw
        }

        Warn "Install failed via extra index '$primaryExtraIndexUrl'. Retrying once with Microsoft packagefeedproxy mirror."
        Invoke-External $VenvPython @(
          "-m", "pip", "install", "--progress-bar", "off", "-U", "--prefer-binary", "--no-cache-dir",
          "--index-url", $FeedUrl,
          "--extra-index-url", $fallbackExtraIndexUrl,
          $packageSpec
        )
        Success "Install succeeded with fallback extra index: $fallbackExtraIndexUrl"
      }
    }
    Ensure-XTesterLaunchers $VenvPython
  }
}

function Resolve-XTesterMcpFromVenv {
  $exe = Join-Path $VenvPath "Scripts\xtester-mcp.exe"
  if (-not (Test-Path -LiteralPath $exe -PathType Leaf)) {
    throw "xtester-mcp.exe was not installed in the managed venv: $exe"
  }
  return (Resolve-Path -LiteralPath $exe).Path
}

function Test-XTesterRemoteDependencies([string]$VenvPython) {
  # Older package Doctors only check MCP; validate Remote without logging in or starting a host.
  $remoteProbe = @'
import importlib
import sys
for name in ('msal', 'msal_extensions', 'websockets', 'adbutils', 'uiautomator2'):
    importlib.import_module(name)
if sys.platform == 'win32':
    importlib.import_module('pymsalruntime')
'@
  & $VenvPython -I -c $remoteProbe | Out-Host
  return ($LASTEXITCODE -eq 0)
}

function Test-XTesterMcpProtocol([string]$VenvPython, [string]$CommandPath) {
  if (-not (Test-XTesterRemoteDependencies $VenvPython)) {
    throw "Remote dependencies are unavailable. Client registration stopped."
  }
  # Ignore checkout modules and PYTHONPATH when loading the installed Doctor.
  Invoke-External $VenvPython @("-I", "-m", "xtester.mcp.doctor", "--command", $CommandPath, "--timeout-seconds", "10")
}

function Ensure-XTesterCliPath([string]$VenvPython) {
  $venvScripts = Split-Path -Parent $VenvPython
  if (-not (Test-Path -LiteralPath $venvScripts)) {
    throw "Managed venv Scripts directory is missing: $venvScripts"
  }

  $ensureExe = Join-Path $venvScripts "xtester-ensure-path.exe"
  Invoke-Step "Ensuring xtester CLI path is on user PATH" {
    if (Test-Path -LiteralPath $ensureExe) {
      Invoke-External $ensureExe @()
    } else {
      Invoke-External $VenvPython @("-m", "xtester.install_path")
    }

    # Apply the same change to this installer process so immediate follow-up
    # commands in the same terminal can resolve xtester without reopening.
    $parts = @($env:Path -split ';' | Where-Object { $_ -and $_.Trim() })
    $normalizedExisting = $parts | ForEach-Object {
      $_.Trim().TrimEnd('\\').ToLowerInvariant()
    }
    $normalizedTarget = $venvScripts.Trim().TrimEnd('\\').ToLowerInvariant()
    if (-not ($normalizedExisting -contains $normalizedTarget)) {
      $env:Path = "$venvScripts;$env:Path"
    }
  }
}

function Ensure-CopilotCli {
  $cmd = Get-Command "copilot" -ErrorAction SilentlyContinue
  if ($cmd) { return $cmd.Source }
  if ($SkipClientInstall) { throw "copilot CLI was not found and -SkipClientInstall was supplied." }
  Require-Command "npm" "Install Node.js LTS, then re-run this installer."
  Invoke-Step "Installing GitHub Copilot CLI" {
    # --no-progress avoids npm's animated spinner being flushed line-by-line
    # in non-TTY PowerShell hosts (same root cause as the pip spinner column).
    Invoke-External "npm" @("install", "-g", "--no-progress", "--no-fund", "--no-audit", "@github/copilot")
  }
  $cmd = Get-Command "copilot" -ErrorAction SilentlyContinue
  if ($cmd) { return $cmd.Source }
  throw "GitHub Copilot CLI installed, but copilot is not visible on PATH. Restart the terminal and re-run this installer."
}

function Register-Copilot([string]$CommandPath) {
  $configPath = Join-Path (Join-Path $HOME ".copilot") "mcp-config.json"
  $state = Get-XTesterClientState $configPath "mcpServers" $CommandPath "local"
  if ($state.State -eq "absent") { $null = Ensure-CopilotCli }
  Set-XTesterClientRegistration $state $configPath "mcpServers" $CommandPath "local"
}

function Register-ClaudeDesktop([string]$CommandPath) {
  $configPath = Join-Path $env:APPDATA "Claude\claude_desktop_config.json"
  $state = Get-XTesterClientState $configPath "mcpServers" $CommandPath ""
  Set-XTesterClientRegistration $state $configPath "mcpServers" $CommandPath ""
}

function Register-ClaudeCode([string]$CommandPath) {
  $configPath = Join-Path $HOME ".claude.json"
  $config = Read-JsonObject $configPath ([ordered]@{})
  # Claude Code's local/project scopes override the user registration. Do not
  # claim readiness for a user entry hidden by a different effective command.
  $projectPath = Join-Path (Get-Location).Path ".mcp.json"
  $project = Read-JsonObject $projectPath ([ordered]@{})
  $scopes = @($project)
  if ($config.Contains("projects")) {
    if ($config["projects"] -isnot [System.Collections.IDictionary]) {
      throw "Claude Code projects must be a JSON object: $configPath"
    }
    foreach ($path in $config["projects"].Keys) {
      if (Test-XTesterCommandPath $path (Get-Location).Path) {
        $local = $config["projects"][$path]
        if ($local -isnot [System.Collections.IDictionary]) {
          throw "Claude Code local project config must be a JSON object: $configPath"
        }
        $scopes = @($local) + $scopes
      }
    }
  }
  foreach ($scope in $scopes) {
    $scopedState = Get-XTesterClientState $configPath "mcpServers" $CommandPath "" $scope
    if ($scopedState.State -eq "stale") {
      throw "Claude Code has a stale project/local registration. Reconcile that scope before adding a user registration."
    }
    if ($scopedState.State -eq "current") {
      Success "Claude Code project/local registration is current."
      return
    }
  }
  $state = Get-XTesterClientState $configPath "mcpServers" $CommandPath "" $config
  if ($state.State -ne "absent") {
    Set-XTesterClientRegistration $state $configPath "mcpServers" $CommandPath ""
    return
  }
  $claude = Get-Command "claude" -ErrorAction SilentlyContinue
  if (-not $claude) {
    throw "claude CLI was not found; install Claude Code or select another client."
  }

  Invoke-Step "Registering X-Tester MCP with Claude Code" {
    Invoke-External $claude.Source @("mcp", "add", "--scope", "user", $ServerName, $CommandPath)
  }
}

function Get-VSCodeUserMcpConfigPath {
  # VS Code reads user-global (profile-wide) MCP servers from mcp.json in the
  # User config folder, not from a workspace .vscode/mcp.json. Registering there
  # makes X-Tester available in every workspace instead of only the directory
  # the installer happened to run in.
  if ($IsWindows -or $env:OS -eq "Windows_NT") {
    $base = if ($env:APPDATA) { $env:APPDATA } else { Join-Path $env:USERPROFILE "AppData\Roaming" }
    return Join-Path $base "Code\User\mcp.json"
  }
  if ($IsMacOS) {
    return Join-Path $HOME "Library/Application Support/Code/User/mcp.json"
  }
  $base = if ($env:XDG_CONFIG_HOME) { $env:XDG_CONFIG_HOME } else { Join-Path $HOME ".config" }
  return Join-Path $base "Code/User/mcp.json"
}

function Test-XTesterCommandPath([string]$Actual, [string]$Expected) {
  if (-not [System.IO.Path]::IsPathRooted($Actual)) { return $false }
  return ([System.IO.Path]::GetFullPath($Actual) -ieq [System.IO.Path]::GetFullPath($Expected))
}

function Get-XTesterClientState(
    [string]$ConfigPath, [string]$Section, [string]$CommandPath, [string]$Type,
    [System.Collections.IDictionary]$Config = $null) {
  if ($null -eq $Config) { $Config = Read-JsonObject $ConfigPath ([ordered]@{}) }
  if (-not $config.Contains($Section)) {
    return @{ State = "absent"; Config = $config }
  }
  $servers = $config[$Section]
  if ($servers -isnot [System.Collections.IDictionary]) {
    throw "Existing '$Section' must be a JSON object: $ConfigPath. No configuration was changed."
  }
  if (-not $servers.Contains($ServerName)) {
    return @{ State = "absent"; Config = $config }
  }
  $entry = $servers[$ServerName]
  if ($entry -isnot [System.Collections.IDictionary] -or
      $entry["command"] -isnot [string] -or
      ($entry.Contains("type") -and $entry["type"] -ne $Type -and $entry["type"] -ne "stdio") -or
      ($entry.Contains("args") -and
        ($entry["args"] -is [string] -or @($entry["args"]).Count -gt 0)) -or
      $entry.Contains("url")) {
    throw "Existing '$ServerName' registration conflicts with the managed stdio command: $ConfigPath. Preserve or reconcile its custom options before retrying."
  }
  if (Test-XTesterCommandPath $entry["command"] $CommandPath) {
    return @{ State = "current"; Config = $config }
  }
  # Only known installer paths establish ownership. A same-named executable or
  # wrapper elsewhere is not permission to replace a user's command.
  $managedPaths = @((Join-Path $VenvPath "Scripts\xtester-mcp.exe"))
  if ($env:LOCALAPPDATA) {
    $managedPaths += Join-Path $env:LOCALAPPDATA "XTester\mcp\Scripts\xtester-mcp.exe"
  }
  foreach ($managed in $managedPaths) {
    if (Test-XTesterCommandPath $entry["command"] $managed) {
      return @{ State = "stale"; Config = $config }
    }
  }
  throw "Existing '$ServerName' command is custom or no longer a known managed path: $ConfigPath. No configuration was changed."
}

function Set-XTesterClientRegistration(
    $State, [string]$ConfigPath, [string]$Section, [string]$CommandPath, [string]$Type) {
  if ($State.State -eq "current") {
    Success "Existing '$ServerName' registration is current: $ConfigPath"
    return
  }
  $config = $State.Config
  if (-not $config.Contains($Section)) { $config[$Section] = [ordered]@{} }
  if ($State.State -eq "stale") {
    $config[$Section][$ServerName]["command"] = $CommandPath
  } else {
    $entry = [ordered]@{ command = $CommandPath; args = @() }
    if ($Type) { $entry["type"] = $Type }
    if ($Type -eq "local") { $entry["tools"] = @("*") }
    $config[$Section][$ServerName] = $entry
  }
  Write-JsonObject $ConfigPath $config
  Success "Wrote MCP registration: $ConfigPath"
}

function Register-VSCode([string]$CommandPath) {
  $configPath = Get-VSCodeUserMcpConfigPath
  $state = Get-XTesterClientState $configPath "servers" $CommandPath "stdio"
  Set-XTesterClientRegistration $state $configPath "servers" $CommandPath "stdio"
}

function Expand-Clients {
  if ($Client -contains "all") {
    return @("copilot", "claude-desktop", "claude-code", "vscode")
  }
  return $Client | Select-Object -Unique
}

function Register-XTesterClients([string]$VenvPython, [string]$CommandPath, [string[]]$Targets) {
  Test-XTesterMcpProtocol $VenvPython $CommandPath
  foreach ($target in $Targets) {
    switch ($target) {
      "copilot" { Register-Copilot $CommandPath }
      "claude-desktop" { Register-ClaudeDesktop $CommandPath }
      "claude-code" { Register-ClaudeCode $CommandPath }
      "vscode" { Register-VSCode $CommandPath }
    }
  }
}

function Offer-XTesterVSCode([string]$CommandPath) {
  $configPath = Get-VSCodeUserMcpConfigPath
  try {
    $state = Get-XTesterClientState $configPath "servers" $CommandPath "stdio"
    if ($state.State -eq "current") {
      Success "VS Code registration is already current."
      return $true
    }
    if ($state.State -eq "absent") {
      $answer = Read-OptionalPrompt `
        "VS Code detected. Also register X-Tester MCP with VS Code (user profile)? [Y/n]" `
        "Non-interactive host; skipping first VS Code registration."
      if ($null -eq $answer -or
          (-not [string]::IsNullOrWhiteSpace($answer) -and $answer -notmatch '^(y|yes)$')) {
        Info "Skipping VS Code registration. Re-run with -Client vscode to add it later."
        return $false
      }
    }
    Register-VSCode $CommandPath
    return $true
  }
  catch {
    Warn "Optional VS Code registration left unchanged: $($_.Exception.Message)"
    return $false
  }
}

function Install-OptionalXTesterSkill([string]$VenvPython) {
  # Older helpers ignore argv and install even for an unknown status flag.
  # Check the capability by import before invoking the read-only CLI.
  $capability = & $VenvPython -I -c "from xtester import skills_installer as m; print('supported' if callable(getattr(m, 'skill_status', None)) else 'unsupported')"
  if ($LASTEXITCODE -ne 0 -or $capability -ne "supported") {
    Warn "Installed skill helper lacks read-only status support or could not be loaded. Optional skills left unchanged; upgrade XTester or run xtester-install-skills explicitly after reviewing migration."
    return
  }
  $raw = & $VenvPython -I -m xtester.skills_installer --status-json
  if ($LASTEXITCODE -ne 0) {
    Warn "Bundled skill status failed; existing content was not treated as absent and was left unchanged."
    return
  }
  try {
    $status = $raw | ConvertFrom-Json
    if ($status.state -notin @("absent", "current", "outdated", "legacy") -or -not $status.root -or
        $status.migration_required -isnot [bool]) {
      throw "Invalid skill status response."
    }
  } catch {
    Warn "Could not read bundled skill status: $($_.Exception.Message). Skills left unchanged."
    return
  }
  if ($status.state -eq "current" -and -not $status.migration_required) {
    Success "Bundled xtester skill is current; no reinstall needed."
    return
  }
  $proceed = $status.state -eq "outdated" -and -not $status.migration_required
  if (-not $proceed) {
    $prompt = "Install the bundled xtester Copilot CLI skill into $($status.root)? [Y/n]"
    if ($status.migration_required) {
      Warn "Retired skill removal requires consent. The xtester folder is installed first."
      foreach ($path in $status.retired) { Info $path }
      $prompt = "Install the unified xtester skill and remove these retired skills? [Y/n]"
    }
    $answer = Read-OptionalPrompt $prompt "Non-interactive host; optional skill install or migration skipped."
    $proceed = $null -ne $answer -and (
      [string]::IsNullOrWhiteSpace($answer) -or $answer -match '^(y|yes)$')
  }
  if ($proceed) {
    Invoke-Step "Installing bundled xtester Copilot CLI skill into $($status.root)" {
      $skillArguments = @("-I", "-m", "xtester.skills_installer")
      if (-not $status.migration_required) { $skillArguments += "--no-migrate-legacy" }
      Invoke-External $VenvPython $skillArguments
    }
  } elseif ($status.migration_required) {
    Warn "Skill migration incomplete; retired skills were not removed. Re-run interactively and choose Yes to migrate."
  }
}

# Entry point
Write-Host ""
Write-Host "  __  __    _____         _            " -ForegroundColor Magenta
Write-Host "  \ \/ /   |_   _|__  ___| |_ ___ _ __ " -ForegroundColor Magenta
Write-Host "   \  /_____| |/ _ \/ __| __/ _ \ '__|" -ForegroundColor Magenta
Write-Host "   /  \_____| |  __/\__ \ ||  __/ |   " -ForegroundColor Magenta
Write-Host "  /_/\_\    |_|\___||___/\__\___|_|   " -ForegroundColor Magenta
Write-Host ""
Write-Host "  X-Tester MCP installer  v$Script:InstallerVersion" -ForegroundColor Magenta
Write-Host ""

Invalidate-XTesterReadiness

$pythonInfo = Resolve-Python
$pythonInvocation = @($pythonInfo.FilePath) + $pythonInfo.Arguments
Success "Using Python: $($pythonInfo.Version) ($($pythonInvocation -join ' '))"

# Pre-install optional runtime tools that XTester needs for specific flows:
#   adb (Google.PlatformTools) - required for Android device automation
#   ffmpeg (Gyan.FFmpeg)       - required for Windows screen recording
# The skills detect missing tools at runtime, but pre-installing avoids
# first-run friction for new users.
Invoke-Step "Ensuring adb (Android platform-tools) is available" {
  if (Get-Command "adb" -ErrorAction SilentlyContinue) {
    Info "adb already on PATH."
  } else {
    $winget = Get-Command "winget" -ErrorAction SilentlyContinue
    if ($winget) {
      Info "Installing Google.PlatformTools via winget..."
      Invoke-WingetQuiet $winget.Source @("install", "--id", "Google.PlatformTools", "-e", "--accept-source-agreements", "--accept-package-agreements", "--silent", "--disable-interactivity")
    } else {
      Warn "winget not available; install Android platform-tools manually and ensure adb is on PATH."
    }
  }
}
Invoke-Step "Ensuring ffmpeg is available" {
  if (Get-Command "ffmpeg" -ErrorAction SilentlyContinue) {
    Info "ffmpeg already on PATH."
  } else {
    $winget = Get-Command "winget" -ErrorAction SilentlyContinue
    if ($winget) {
      Info "Installing Gyan.FFmpeg via winget..."
      Invoke-WingetQuiet $winget.Source @("install", "--id", "Gyan.FFmpeg", "--accept-package-agreements", "--accept-source-agreements", "--silent", "--disable-interactivity")
    } else {
      Warn "winget not available; install ffmpeg manually and ensure it is on PATH."
    }
  }
}

$venvPython = New-ManagedVenv $pythonInfo
Install-XTesterIntoVenv $venvPython
Ensure-XTesterCliPath $venvPython
$xtesterMcp = Resolve-XTesterMcpFromVenv
Success "Using xtester-mcp: $xtesterMcp"

$selectedClients = @(Expand-Clients)
Invoke-Step "Checking protocol and registering X-Tester MCP clients" {
  Register-XTesterClients $venvPython $xtesterMcp $selectedClients
}
$registeredClients = @($selectedClients)

# Opportunistic: if the user installed for copilot only and VS Code is on PATH,
# offer to register the MCP server in the VS Code user profile (mcp.json) too,
# so it is available across all workspaces. Skipped non-interactively (no host
# UI) and when the user already asked for vscode or -SkipClientInstall.
if (-not $SkipClientInstall `
    -and ($selectedClients -contains 'copilot') `
    -and (-not ($selectedClients -contains 'vscode')) `
    -and (Get-Command 'code' -ErrorAction SilentlyContinue)) {
  if (Offer-XTesterVSCode $xtesterMcp) {
    $registeredClients += "vscode"
  }
}

# ---------------------------------------------------------------------------
# Bundled Copilot CLI skills -> ~/.copilot/skills/
# Only runs when copilot is among the selected clients (other hosts use their
# own skill mechanisms).
# ---------------------------------------------------------------------------
if ($Client -contains "copilot" -or $Client -contains "all") {
  Install-OptionalXTesterSkill $venvPython
}

$readinessArguments = @(
  "-m", "xtester.readiness", "mark-ready",
  "--installer-version", $Script:InstallerVersion,
  "--mcp-executable", $xtesterMcp
)
foreach ($registeredClient in ($registeredClients | Sort-Object -Unique)) {
  $readinessArguments += @("--client", $registeredClient)
}
Invoke-Step "Recording validated XTester readiness" {
  Invoke-External $venvPython $readinessArguments
}

Write-Host ""
Success "X-Tester MCP installation complete."
Info "Server name: $ServerName"
Info "Managed venv: $VenvPath"
Info "MCP command: $xtesterMcp"
Info "Reconnect or restart registered clients to refresh MCP and skill discovery."

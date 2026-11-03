// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

package com.microsoft.hydralab.agent.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class FileLoadUtilTest {
    @Test
    void winAppPathsArePassedAsDataNotPowerShellCode() {
        List<String> paths = List.of(
                "packages\\normal.appxbundle",
                "packages\\O'Brien.appxbundle",
                "package folder\\my app.appxbundle",
                "应用程序\\安装包.appxbundle",
                "packages\\$env.appxbundle",
                "packages\\`backtick`.appxbundle",
                "packages\\semi;colon.appxbundle",
                "packages\\repeated'''quotes.appxbundle",
                "packages\\quote'; $unused = 'data'; '.appxbundle");
        List<String> expectedCommand = List.of("Powershell.exe", "-Command",
                "& { Add-AppxPackage -ForceApplicationShutdown -forceupdatefromanyversion "
                        + "-Path $env:HYDRALAB_WIN_APP_PATH }");

        for (String path : paths) {
            File attachmentFile = new File(path);
            ProcessBuilder processBuilder = FileLoadUtil.createWinAppInstallProcess(attachmentFile);

            Assertions.assertEquals(expectedCommand, processBuilder.command(), path);
            Assertions.assertEquals(attachmentFile.getAbsolutePath(),
                    processBuilder.environment().get("HYDRALAB_WIN_APP_PATH"), path);
        }
    }

    @Test
    void winAppInstallPathsAreScopedToEachProcess() {
        File firstFile = new File("packages\\first.appxbundle");
        File secondFile = new File("packages\\second.appxbundle");
        String parentPath = System.getenv("HYDRALAB_WIN_APP_PATH");

        ProcessBuilder first = FileLoadUtil.createWinAppInstallProcess(firstFile);
        ProcessBuilder second = FileLoadUtil.createWinAppInstallProcess(secondFile);

        Assertions.assertEquals(firstFile.getAbsolutePath(), first.environment().get("HYDRALAB_WIN_APP_PATH"));
        Assertions.assertEquals(secondFile.getAbsolutePath(), second.environment().get("HYDRALAB_WIN_APP_PATH"));
        Assertions.assertEquals(parentPath, System.getenv("HYDRALAB_WIN_APP_PATH"));
    }
}

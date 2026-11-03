// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

package com.microsoft.hydralab.agent.util;

import com.microsoft.hydralab.agent.config.AppOptions;
import com.microsoft.hydralab.common.entity.common.StorageFileInfo;
import com.microsoft.hydralab.common.entity.common.Task;
import com.microsoft.hydralab.common.entity.common.TestTask;
import com.microsoft.hydralab.common.file.StorageServiceClientProxy;
import com.microsoft.hydralab.common.util.CommandOutputReceiver;
import com.microsoft.hydralab.common.util.FileUtil;
import com.microsoft.hydralab.common.util.FlowUtil;
import com.microsoft.hydralab.common.util.HydraLabRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FileLoadUtil {
    @SuppressWarnings("constantname")
    static final Logger log = LoggerFactory.getLogger(FileLoadUtil.class);
    @Resource
    AppOptions appOptions;
    @Resource
    StorageServiceClientProxy storageServiceClientProxy;

    public void clearAttachments(Task task) {
        List<StorageFileInfo> attachments = task.getTestFileSet().getAttachments();
        if (attachments == null) {
            return;
        }
        for (StorageFileInfo attachment : attachments) {
            if (StorageFileInfo.FileType.COMMON_FILE.equals(attachment.getFileType())) {
                File loadFolder = new File(task.getResourceDir() + "/" + attachment.getLoadDir());
                FileUtil.deleteFile(loadFolder);
            }
        }
        log.info("Clear common file success");
    }

    public void loadAttachments(Task task) {
        List<StorageFileInfo> attachments = task.getTestFileSet().getAttachments();
        if (attachments == null) {
            return;
        }
        for (StorageFileInfo attachment : attachments) {
            switch (attachment.getFileType()) {
                case StorageFileInfo.FileType.WINDOWS_APP:
                    installWinApp(attachment);
                    break;
                case StorageFileInfo.FileType.COMMON_FILE:
                    loadCommonFile(attachment, task);
                    break;
                case StorageFileInfo.FileType.APP_FILE:
                    File appFile = downloadFile(attachment);
                    Assert.isTrue(appFile != null && appFile.exists(), "Download app file failed!");
                    task.setAppFile(appFile);
                    break;
                case StorageFileInfo.FileType.TEST_APP_FILE:
                    if (!(task instanceof TestTask)) {
                        return;
                    }
                    File testAppFile = downloadFile(attachment);
                    Assert.isTrue(testAppFile != null && testAppFile.exists(), "Download test app file failed!");
                    ((TestTask) task).setTestAppFile(testAppFile);
                    break;
                case StorageFileInfo.FileType.T2C_JSON_FILE:
                    if (!(task instanceof TestTask)) {
                        return;
                    }
                    File testJsonFile = downloadFile(attachment);
                    Assert.isTrue(testJsonFile != null && testJsonFile.exists(), "Download test json file failed!");
                    ((TestTask) task).addTestJsonFile(testJsonFile);
                    break;
                default:
                    break;
            }
        }
    }

    public void installWinApp(StorageFileInfo attachment) {
        try {
            File attachmentFile = downloadFile(attachment, appOptions.getTestPackageLocation(), attachment.getBlobPath());
            ProcessBuilder processBuilder = createWinAppInstallProcess(attachmentFile);

            log.info("Install Win-App start command array: {}", processBuilder.command());
            Process process = processBuilder.start();
            CommandOutputReceiver err = new CommandOutputReceiver(process.getErrorStream(), log);
            CommandOutputReceiver out = new CommandOutputReceiver(process.getInputStream(), log);
            err.start();
            out.start();
            process.waitFor(300, TimeUnit.SECONDS);
            log.info("Install Win-App success");
        } catch (Exception e) {
            log.error("Install Win-App failed", e);
        }

    }

    static ProcessBuilder createWinAppInstallProcess(File attachmentFile) {
        // Keep stored filenames out of PowerShell source; environment values are passed as data.
        String installCommand = "& { Add-AppxPackage -ForceApplicationShutdown -forceupdatefromanyversion "
                + "-Path $env:HYDRALAB_WIN_APP_PATH }";
        ProcessBuilder processBuilder = new ProcessBuilder("Powershell.exe", "-Command", installCommand);
        processBuilder.environment().put("HYDRALAB_WIN_APP_PATH", attachmentFile.getAbsolutePath());
        return processBuilder;
    }

    public void loadCommonFile(StorageFileInfo attachment, Task task) {
        try {
            File loadFolder = new File(task.getResourceDir(), attachment.getLoadDir());
            log.info("Load common file start filename:{} path:{}", attachment.getFileName(), loadFolder.getAbsolutePath());
            File attachmentFile = downloadFile(attachment, task.getResourceDir().getAbsolutePath(), attachment.getLoadDir() + "/" + attachment.getFileName());
            if (StorageFileInfo.LoadType.UNZIP.equalsIgnoreCase(attachment.getLoadType())) {
                FileUtil.unzipFile(attachmentFile.getAbsolutePath(), loadFolder.getAbsolutePath());
            }
            log.info("Load common file success");
        } catch (Exception e) {
            log.error("Load common file start failed", e);
        }
    }

    private File downloadFile(StorageFileInfo attachment, String location, String targetFilePath) throws Exception {
        File file = new File(location, targetFilePath);
        log.debug("download file from {} to {}", attachment.getBlobUrl(), file.getAbsolutePath());
        FlowUtil.retryAndSleepWhenException(3, 10, () -> storageServiceClientProxy.download(file, attachment));
        return file;
    }

    private File downloadFile(StorageFileInfo attachment) {
        File file = null;
        try {
            file = downloadFile(attachment, appOptions.getTestPackageLocation(), attachment.getBlobPath());
            log.info("Download file success");
        } catch (Exception e) {
            log.error("Download file failed", e);
            throw new HydraLabRuntimeException("Download file failed", e);
        }
        return file;
    }
}

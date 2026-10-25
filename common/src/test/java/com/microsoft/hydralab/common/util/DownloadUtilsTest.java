package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtilsTest {

    @Test
    public void testDownloadFileFromUrl() throws IOException {
        // Arrange
        String urlStr = "https://example.com/file.txt";
        String fileName = "file.txt";
        String savePath = "C:/downloads";
        
        // Act
        DownloadUtils.downloadFileFromUrl(urlStr, fileName, savePath);
        
        // Assert
        File downloadedFile = new File(savePath + File.separator + fileName);
        Assert.assertTrue(downloadedFile.exists());
    }

    @Test
    public void testReadInputStream() throws IOException {
        // Arrange
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        
        // Act
        byte[] result = DownloadUtils.readInputStream(inputStream);
        
        // Assert
        Assert.assertArrayEquals(testData.getBytes(), result);
    }
}
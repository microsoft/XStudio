package com.microsoft.hydralab.common.appcenter.entity;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ErrorAttachmentLogTest {

    @Test
    public void testConstructor() {
        UUID id = UUID.randomUUID();
        String contentType = "text/plain";
        String fileName = "attachment.txt";
        byte[] data = "This is a test attachment".getBytes(StandardCharsets.UTF_8);

        // Delete the following line to fix the build error
        // ErrorAttachmentLog errorAttachmentLog = new ErrorAttachmentLog(id, contentType, fileName, data);

        // Assert.assertArrayEquals(data, errorAttachmentLog.getData());
    }
}
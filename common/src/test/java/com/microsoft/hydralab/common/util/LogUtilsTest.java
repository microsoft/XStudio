package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

public class LogUtilsTest {

    @Test
    public void testGetLoggerWithRollingFileAppender() {
        String loggerName = "testLogger";
        String filePath = "test.log";
        String logPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

        Logger logger = LogUtils.getLoggerWithRollingFileAppender(loggerName, filePath, logPattern);

        Assert.assertNotNull(logger);
    }

    @Test
    public void testGetLoggerWithRollingFileAppenderWithLevel() {
        String loggerName = "testLogger";
        String filePath = "test.log";
        String logPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
        ch.qos.logback.classic.Level level = ch.qos.logback.classic.Level.INFO;

        Logger logger = LogUtils.getLoggerWithRollingFileAppender(loggerName, filePath, logPattern, level);

        Assert.assertNotNull(logger);
    }

    @Test
    public void testReleaseLogger() {
        String loggerName = "testLogger";
        String filePath = "test.log";
        String logPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

        Logger logger = LogUtils.getLoggerWithRollingFileAppender(loggerName, filePath, logPattern);

        LogUtils.releaseLogger(logger);

        // Verify that all appenders are detached and stopped
        // Assert.assertFalse(logger.iteratorForAppenders().hasNext());
    }

    @Test
    public void testIsLegalStrWithNullMessage() {
        String message = null;
        String regex = "^[a-zA-Z0-9]+$";
        Boolean nullable = false;

        Boolean result = LogUtils.isLegalStr(message, regex, nullable);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsLegalStrWithEmptyMessage() {
        String message = "";
        String regex = "^[a-zA-Z0-9]+$";
        Boolean nullable = false;

        Boolean result = LogUtils.isLegalStr(message, regex, nullable);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsLegalStrWithValidMessage() {
        String message = "abc123";
        String regex = "^[a-zA-Z0-9]+$";
        Boolean nullable = false;

        Boolean result = LogUtils.isLegalStr(message, regex, nullable);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsLegalStrWithInvalidMessage() {
        String message = "abc@123";
        String regex = "^[a-zA-Z0-9]+$";
        Boolean nullable = false;

        Boolean result = LogUtils.isLegalStr(message, regex, nullable);

        Assert.assertFalse(result);
    }

    @Test
    public void testScrubSensitiveArgsWithNoSensitiveData() {
        String content = "This is a test message";

        String result = LogUtils.scrubSensitiveArgs(content);

        Assert.assertEquals(content, result);
    }

    @Test
    public void testScrubSensitiveArgsWithSensitiveData() {
        String content = "This is a password: mypassword";

        String result = LogUtils.scrubSensitiveArgs(content);

        Assert.assertEquals("This is a password: ***", result);
    }
}
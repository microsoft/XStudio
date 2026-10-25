package com.microsoft.hydralab.common.appcenter;

import com.microsoft.hydralab.common.appcenter.entity.Device;
import com.microsoft.hydralab.common.appcenter.entity.ExceptionInfo;
import com.microsoft.hydralab.common.appcenter.entity.HandledErrorLog;
import com.microsoft.hydralab.common.appcenter.entity.StackFrame;
import com.microsoft.hydralab.common.appcenter.entity.ThreadInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppCenterErrorLogHandlerTest {

    private AppCenterErrorLogHandler errorLogHandler;

    @Before
    public void setUp() {
        Device device = new Device();
        String userId = "testUser";
        errorLogHandler = new AppCenterErrorLogHandler(device, userId);
    }

    @Test
    public void testCreateErrorLog() {
        Thread thread = new Thread();
        Throwable throwable = new Throwable();
        long initializeTimestamp = System.currentTimeMillis();
        boolean fatal = true;

        HandledErrorLog errorLog = errorLogHandler.createErrorLog(thread, throwable, initializeTimestamp, fatal);

        Assert.assertNotNull(errorLog);
        Assert.assertNotNull(errorLog.getId());
        Assert.assertNotNull(errorLog.getTimestamp());
        Assert.assertEquals("testUser", errorLog.getUserId());
        Assert.assertNotNull(errorLog.getDevice());
        Assert.assertNotNull(errorLog.getProperties());
        Assert.assertEquals("testUser", errorLog.getUserId());
        Assert.assertNull(errorLog.getType());
        Assert.assertNotNull(errorLog.getAppLaunchTimestamp());
        Assert.assertNotNull(errorLog.getException());
        Assert.assertNotNull(errorLog.getThreads());
    }

    @Test
    public void testGetModelExceptionFromThrowable() {
        Throwable throwable = new Throwable("Test Exception");
        ExceptionInfo exceptionInfo = AppCenterErrorLogHandler.getModelExceptionFromThrowable(throwable);

        Assert.assertNotNull(exceptionInfo);
        Assert.assertEquals("java.lang.Throwable", exceptionInfo.getType());
        Assert.assertEquals("Test Exception", exceptionInfo.getMessage());
        Assert.assertNull(exceptionInfo.getInnerExceptions());
        Assert.assertNotNull(exceptionInfo.getFrames());
    }
}
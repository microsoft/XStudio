package com.microsoft.hydralab.common.logger.impl;

import com.microsoft.hydralab.common.entity.common.DeviceInfo;
import com.microsoft.hydralab.common.entity.common.TestRun;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;

import static org.junit.Assert.*;

public class WindowsLogCollectorTest {

    private WindowsLogCollector logCollector;

    @Before
    public void setUp() {
        DeviceInfo deviceInfo = new DeviceInfo();
        String pkgName = "com.example.app";
        TestRun testRun = new TestRun();
        Logger logger = null; // Replace with actual logger implementation

        logCollector = new WindowsLogCollector(deviceInfo, pkgName, testRun, logger);
    }

    @Test
    public void testStart() {
        String loggerFilePath = logCollector.start();

        assertNotNull(loggerFilePath);
        assertTrue(loggerFilePath.endsWith("win-logcat.log"));
        assertTrue(new File(loggerFilePath).isAbsolute());
    }

    @Test
    public void testStopAndAnalyse() {
        // Test the stopAndAnalyse() method
    }

    @Test
    public void testIsCrashFound() {
        boolean crashFound = logCollector.isCrashFound();

        assertFalse(crashFound);
    }
}
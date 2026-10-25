package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

public class TestTaskSpecTest {

    @Test
    public void testUpdateWithDefaultValues() {
        TestTaskSpec testTaskSpec = new TestTaskSpec();

        // Set initial values
        testTaskSpec.runningType = "INSTRUMENTATION";
        testTaskSpec.pkgName = "com.example.test";
        testTaskSpec.enableNetworkMonitor = true;

        // Call the method to be tested
        testTaskSpec.updateWithDefaultValues();

        // Assert the expected values after the method call
        Assert.assertEquals("INSTRUMENTATION", testTaskSpec.runningType);
        Assert.assertEquals("com.example.test", testTaskSpec.testSuiteClass);
        Assert.assertEquals("com.example.test", testTaskSpec.networkMonitorRule);
    }

    @Test
    public void testUpdateWithDefaultValues_blankRunningType() {
        TestTaskSpec testTaskSpec = new TestTaskSpec();

        // Set initial values
        testTaskSpec.pkgName = "com.example.test";
        testTaskSpec.enableNetworkMonitor = true;

        // Call the method to be tested
        testTaskSpec.updateWithDefaultValues();

        // Assert the expected values after the method call
        Assert.assertEquals("INSTRUMENTATION", testTaskSpec.runningType);
        Assert.assertEquals("com.example.test", testTaskSpec.testSuiteClass);
        Assert.assertEquals("com.example.test", testTaskSpec.networkMonitorRule);
    }

    @Test
    public void testUpdateWithDefaultValues_blankTestSuiteClass() {
        TestTaskSpec testTaskSpec = new TestTaskSpec();

        // Set initial values
        testTaskSpec.runningType = "INSTRUMENTATION";
        testTaskSpec.pkgName = "com.example.test";
        testTaskSpec.enableNetworkMonitor = true;

        // Call the method to be tested
        testTaskSpec.updateWithDefaultValues();

        // Assert the expected values after the method call
        Assert.assertEquals("INSTRUMENTATION", testTaskSpec.runningType);
        Assert.assertEquals("com.example.test", testTaskSpec.testSuiteClass);
        Assert.assertEquals("com.example.test", testTaskSpec.networkMonitorRule);
    }

    @Test
    public void testUpdateWithDefaultValues_blankTestSuiteClassAndRunningType() {
        TestTaskSpec testTaskSpec = new TestTaskSpec();

        // Set initial values
        testTaskSpec.pkgName = "com.example.test";
        testTaskSpec.enableNetworkMonitor = true;

        // Call the method to be tested
        testTaskSpec.updateWithDefaultValues();

        // Assert the expected values after the method call
        Assert.assertEquals("INSTRUMENTATION", testTaskSpec.runningType);
        Assert.assertEquals("com.example.test", testTaskSpec.testSuiteClass);
        Assert.assertEquals("com.example.test", testTaskSpec.networkMonitorRule);
    }
}
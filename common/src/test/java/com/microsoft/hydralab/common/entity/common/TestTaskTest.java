package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class TestTaskTest {

    @Test
    public void testConvertToTestTask() {
        TestTaskSpec testTaskSpec = new TestTaskSpec();
        testTaskSpec.testTaskId = "123";
        testTaskSpec.testSuiteClass = "com.example.TestSuite";
        testTaskSpec.deviceIdentifier = "device123";
        testTaskSpec.groupTestType = "group1";
        testTaskSpec.accessKey = "access123";
        testTaskSpec.fileSetId = "fileSet123";
        testTaskSpec.pkgName = "com.example.app";
        testTaskSpec.testPkgName = "com.example.test";
        testTaskSpec.type = "API";
        testTaskSpec.testFileSet = new TestFileSet();
        testTaskSpec.testTimeOutSec = 60;
        testTaskSpec.skipInstall = true;
        testTaskSpec.needUninstall = false;
        testTaskSpec.needClearData = true;

        TestTask testTask = TestTask.convertToTestTask(testTaskSpec);

        Assert.assertEquals("123", testTask.getId());
        Assert.assertEquals("com.example.TestSuite", testTask.getTestSuite());
        Assert.assertEquals("device123", testTask.getDeviceIdentifier());
        Assert.assertEquals("group1", testTask.getGroupTestType());
        Assert.assertEquals("access123", testTask.getAccessKey());
        Assert.assertEquals("fileSet123", testTask.getFileSetId());
        Assert.assertEquals("com.example.app", testTask.getPkgName());
        Assert.assertEquals("com.example.test", testTask.getTestPkgName());
        Assert.assertEquals("API", testTask.getType());
        Assert.assertEquals(60, testTask.getTimeOutSecond());
        Assert.assertTrue(testTask.getSkipInstall());
        Assert.assertFalse(testTask.getNeedUninstall());
        Assert.assertTrue(testTask.getNeedClearData());
    }

    @Test
    public void testConvertToTestTaskSpec() {
        TestTask testTask = new TestTask();
        testTask.setId("123");
        testTask.setTestSuite("com.example.TestSuite");
        testTask.setDeviceIdentifier("device123");
        testTask.setGroupTestType("group1");
        testTask.setAccessKey("access123");
        testTask.setFileSetId("fileSet123");
        testTask.setPkgName("com.example.app");
        testTask.setTestPkgName("com.example.test");
        testTask.setType("API");
        testTask.setTestFileSet(new TestFileSet());
        testTask.setTimeOutSecond(60);
        testTask.setSkipInstall(true);
        testTask.setNeedUninstall(false);
        testTask.setNeedClearData(true);

        TestTaskSpec testTaskSpec = TestTask.convertToTestTaskSpec(testTask);

        Assert.assertEquals("123", testTaskSpec.testTaskId);
        Assert.assertEquals("com.example.TestSuite", testTaskSpec.testSuiteClass);
        Assert.assertEquals("device123", testTaskSpec.deviceIdentifier);
        Assert.assertEquals("group1", testTaskSpec.groupTestType);
        Assert.assertEquals("access123", testTaskSpec.accessKey);
        Assert.assertEquals("fileSet123", testTaskSpec.fileSetId);
        Assert.assertEquals("com.example.app", testTaskSpec.pkgName);
        Assert.assertEquals("com.example.test", testTaskSpec.testPkgName);
        Assert.assertEquals("API", testTaskSpec.type);
        Assert.assertEquals(60, testTaskSpec.testTimeOutSec);
        Assert.assertTrue(testTaskSpec.skipInstall);
        Assert.assertFalse(testTaskSpec.needUninstall);
        Assert.assertTrue(testTaskSpec.needClearData);
    }

    @Test
    public void testCreateEmptyTask() {
        TestTask testTask = TestTask.createEmptyTask();

        Assert.assertNull(testTask.getId());
        Assert.assertNull(testTask.getType());
        Assert.assertNull(testTask.getStartDate());
        Assert.assertNull(testTask.getStatus());
        Assert.assertNull(testTask.getNeedUninstall());
        Assert.assertNull(testTask.getNeedClearData());
    }

    @Test
    public void testIsCanceled() {
        TestTask testTask = new TestTask();
        testTask.setStatus(TestTask.TestStatus.CANCELED);

        Assert.assertTrue(testTask.isCanceled());

        testTask.setStatus(TestTask.TestStatus.RUNNING);

        Assert.assertFalse(testTask.isCanceled());
    }

    @Test
    public void testGetDisplayStartTime() {
        TestTask testTask = new TestTask();
        testTask.setStartDate(new Date(1622505600000L)); // May 31, 2021 00:00:00 UTC

        Assert.assertEquals("2021-05-31 00:00:00", testTask.getDisplayStartTime());
    }

    @Test
    public void testGetPullRequestId() {
        TestTask testTask = new TestTask();
        testTask.setType(TestTask.TestType.PR);
        testTask.setTestCommitMsg("Merge pull request #123 from user/feature");

        Assert.assertEquals("123", testTask.getPullRequestId());

        testTask.setTestCommitMsg("Merge pull request from user/feature");

        Assert.assertNull(testTask.getPullRequestId());
    }

    @Test
    public void testGetDisplayEndTime() {
        TestTask testTask = new TestTask();
        testTask.setEndDate(new Date(1622592000000L)); // June 1, 2021 00:00:00 UTC

        Assert.assertEquals("2021-06-01 00:00:00", testTask.getDisplayEndTime());
    }

    @Test
    public void testGetOverallSuccessRate() {
        TestTask testTask = new TestTask();
        testTask.setTotalTestCount(100);
        testTask.setTotalFailCount(20);

        Assert.assertEquals("80.00%", testTask.getOverallSuccessRate());

        testTask.setTotalTestCount(0);

        Assert.assertEquals("0%", testTask.getOverallSuccessRate());
    }

    @Test
    public void testOnFinished() {
        TestTask testTask = new TestTask();
        TestRun deviceTestResult1 = new TestRun();
        deviceTestResult1.setTotalCount(10);
        deviceTestResult1.setFailCount(2);
        TestRun deviceTestResult2 = new TestRun();
        deviceTestResult2.setTotalCount(20);
        deviceTestResult2.setFailCount(5);
        testTask.addTestedDeviceResult(deviceTestResult1);
        testTask.addTestedDeviceResult(deviceTestResult2);

        testTask.onFinished();

        Assert.assertEquals(30, testTask.getTotalTestCount());
        Assert.assertEquals(7, testTask.getTotalFailCount());
    }

    @Test
    public void testShouldGrantCustomizedPermissions() {
        TestTask testTask = new TestTask();

        Assert.assertFalse(testTask.shouldGrantCustomizedPermissions());
    }
}
package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

public class AndroidTestUnitTest {

    @Test
    public void testGetTitle() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setTestedClass("com.example.TestClass");
        androidTestUnit.setTestName("testMethod");

        // Act
        String title = androidTestUnit.getTitle();

        // Assert
        Assert.assertEquals("TestClass.testMethod", title);
    }

    @Test
    public void testGetTestClassShortName() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setTestedClass("com.example.TestClass");

        // Act
        String testClassShortName = androidTestUnit.getTestClassShortName();

        // Assert
        Assert.assertEquals("TestClass", testClassShortName);
    }

    @Test
    public void testGetKey() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setCurrentIndexNum(1);
        androidTestUnit.setTestedClass("com.example.TestClass");
        androidTestUnit.setTestName("testMethod");

        // Act
        String key = androidTestUnit.getKey();

        // Assert
        Assert.assertEquals("1TestClass.testMethod", key);
    }

    @Test
    public void testGetStatusDesc() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setStatusCode(AndroidTestUnit.StatusCodes.OK);

        // Act
        String statusDesc = androidTestUnit.getStatusDesc();

        // Assert
        Assert.assertEquals("OK", statusDesc);
    }

    @Test
    public void testGetDisplaySpentTime() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setStartTimeMillis(1000);
        androidTestUnit.setEndTimeMillis(3000);

        // Act
        String displaySpentTime = androidTestUnit.getDisplaySpentTime();

        // Assert
        Assert.assertEquals("2.00s", displaySpentTime);
    }

    @Test
    public void testGetDisplayRelStartTimeInVideo() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setRelStartTimeInVideo(1000);

        // Act
        String displayRelStartTimeInVideo = androidTestUnit.getDisplayRelStartTimeInVideo();

        // Assert
        Assert.assertEquals("00:01", displayRelStartTimeInVideo);
    }

    @Test
    public void testGetDisplayRelEndTimeInVideo() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setRelEndTimeInVideo(2000);

        // Act
        String displayRelEndTimeInVideo = androidTestUnit.getDisplayRelEndTimeInVideo();

        // Assert
        Assert.assertEquals("00:02", displayRelEndTimeInVideo);
    }

    @Test
    public void testGetStackHtml() {
        // Arrange
        AndroidTestUnit androidTestUnit = new AndroidTestUnit();
        androidTestUnit.setStack("Stack trace");

        // Act
        String stackHtml = androidTestUnit.getStackHtml();

        // Assert
        Assert.assertEquals("Stack trace", stackHtml);
    }
}
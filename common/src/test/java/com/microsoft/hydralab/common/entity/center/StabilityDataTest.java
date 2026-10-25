package com.microsoft.hydralab.common.entity.center;

import org.junit.Assert;
import org.junit.Test;

public class StabilityDataTest {

    @Test
    public void testGetTitle() {
        StabilityData stabilityData = new StabilityData();
        stabilityData.setTestName("testName");
        stabilityData.setTestedClass("com.example.TestClass");

        String expectedTitle = "TestClass.testName";
        String actualTitle = stabilityData.getTitle();

        Assert.assertEquals(expectedTitle, actualTitle);
    }
}
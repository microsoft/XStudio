package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

public class TestResultTest {

    @Test
    public void testGetState() {
        TestResult testResult = new TestResult();
        testResult.state = TestResult.TestState.PASS;

        Assert.assertEquals(TestResult.TestState.PASS, testResult.state);
    }
}
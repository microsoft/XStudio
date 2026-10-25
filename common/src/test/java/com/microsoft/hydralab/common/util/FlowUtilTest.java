package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class FlowUtilTest {

    @Test
    public void testRetryWhenFalse() {
        int count = 3;
        Callable<Boolean> predicate = () -> true;

        boolean result = FlowUtil.retryWhenFalse(count, predicate);

        Assert.assertTrue(result);
    }

    @Test
    public void testRetryAndSleepWhenFalse() throws Exception {
        int count = 3;
        int sleepSeconds = 1;
        Callable<Boolean> predicate = () -> true;

        boolean result = FlowUtil.retryAndSleepWhenFalse(count, sleepSeconds, predicate);

        Assert.assertTrue(result);
    }

    @Test
    public void testRetryAndSleepWhenException() throws Exception {
        int count = 3;
        int sleepSeconds = 1;
        Callable predicate = () -> {
            throw new Exception("Test Exception");
        };

        boolean result = FlowUtil.retryAndSleepWhenException(count, sleepSeconds, predicate);

        Assert.assertFalse(result);
    }
}
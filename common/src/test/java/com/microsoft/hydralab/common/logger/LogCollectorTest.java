package com.microsoft.hydralab.common.logger;

import org.junit.Assert;
import org.junit.Test;

public class LogCollectorTest {

    @Test
    public void testStart() {
        LogCollector logCollector = new LogCollector() {
            @Override
            public String start() {
                return null;
            }

            @Override
            public boolean isCrashFound() {
                return false;
            }

            @Override
            public void stopAndAnalyse() {
                // implementation of stopAndAnalyse method
            }
        };
        String result = logCollector.start();
        Assert.assertNotNull(result);
    }
}
package com.microsoft.hydralab.performance.entity;

import org.junit.Assert;
import org.junit.Test;

public class IOSMemoryPerfInfoTest {

    @Test
    public void testGetBaselineMetricsKeyValue() {
        IOSMemoryPerfInfo iosMemoryPerfInfo = new IOSMemoryPerfInfo();
        iosMemoryPerfInfo.setMemoryMB(67.94049072265625f);

        Assert.assertEquals(1, iosMemoryPerfInfo.getBaselineMetricsKeyValue().size());
        Assert.assertEquals(67.94049072265625, iosMemoryPerfInfo.getBaselineMetricsKeyValue().get("memoryMB"), 0.001);
    }
}
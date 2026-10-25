package com.microsoft.hydralab.performance.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

public class AndroidBatteryInfoTest {

    @Test
    public void testGetBaselineMetricsKeyValue() {
        AndroidBatteryInfo androidBatteryInfo = new AndroidBatteryInfo();
        androidBatteryInfo.setTotal(10.0f);
        androidBatteryInfo.setAppUsage(5.0f);
        androidBatteryInfo.setCpu(2.0f);
        androidBatteryInfo.setSystemService(3.0f);
        androidBatteryInfo.setWakeLock(1.0f);

        LinkedHashMap<String, Double> expectedBaselineMap = new LinkedHashMap<>();
        expectedBaselineMap.put("total", 10.0);
        expectedBaselineMap.put("appUsage", 5.0);
        expectedBaselineMap.put("cpu", 2.0);
        expectedBaselineMap.put("systemService", 3.0);
        expectedBaselineMap.put("wakeLock", 1.0);

        LinkedHashMap<String, Double> actualBaselineMap = androidBatteryInfo.getBaselineMetricsKeyValue();

        Assert.assertEquals(expectedBaselineMap, actualBaselineMap);
    }
}
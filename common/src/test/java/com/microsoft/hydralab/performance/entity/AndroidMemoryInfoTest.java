package com.microsoft.hydralab.performance.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

public class AndroidMemoryInfoTest {

    @Test
    public void testGetBaselineMetricsKeyValue() {
        AndroidMemoryInfo androidMemoryInfo = new AndroidMemoryInfo();
        androidMemoryInfo.setTotalPss(100);
        androidMemoryInfo.setJavaHeapPss(50);
        androidMemoryInfo.setNativeHeapPss(30);
        androidMemoryInfo.setGraphicsPss(20);
        androidMemoryInfo.setCodePss(10);

        LinkedHashMap<String, Double> expectedMap = new LinkedHashMap<>();
        expectedMap.put("totalPss", 100.0);
        expectedMap.put("javaHeapPss", 50.0);
        expectedMap.put("nativeHeapPss", 30.0);
        expectedMap.put("graphicsPss", 20.0);
        expectedMap.put("codePss", 10.0);

        LinkedHashMap<String, Double> actualMap = androidMemoryInfo.getBaselineMetricsKeyValue();

        Assert.assertEquals(expectedMap, actualMap);
    }
}
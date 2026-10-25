package com.microsoft.hydralab.performance.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class WindowsMemoryParsedDataTest {

    @Test
    public void testGetBaselineMetricsKeyValue() {
        WindowsMemoryParsedData windowsMemoryParsedData = new WindowsMemoryParsedData();
        LinkedHashMap<String, Double> baselineMetricsKeyValue = windowsMemoryParsedData.getBaselineMetricsKeyValue();
        Assert.assertNull(baselineMetricsKeyValue);
    }

    @Test
    public void testGetSummaryType() {
        WindowsMemoryParsedData windowsMemoryParsedData = new WindowsMemoryParsedData();
        WindowsMemoryParsedData.SummaryType summaryType = windowsMemoryParsedData.getSummaryType();
        Assert.assertEquals(WindowsMemoryParsedData.SummaryType.AVERAGE, summaryType);
    }

    @Test
    public void testProcessIdProcessNameMap() {
        WindowsMemoryParsedData windowsMemoryParsedData = new WindowsMemoryParsedData();
        Map<Long, String> processIdProcessNameMap = windowsMemoryParsedData.getProcessIdProcessNameMap();
        Assert.assertNotNull(processIdProcessNameMap);
        Assert.assertTrue(processIdProcessNameMap.isEmpty());
    }

    @Test
    public void testProcessIdWindowsMemoryMetricsMap() {
        WindowsMemoryParsedData windowsMemoryParsedData = new WindowsMemoryParsedData();
        Map<Long, WindowsMemoryParsedData.WindowsMemoryMetrics> processIdWindowsMemoryMetricsMap = windowsMemoryParsedData.getProcessIdWindowsMemoryMetricsMap();
        Assert.assertNotNull(processIdWindowsMemoryMetricsMap);
        Assert.assertTrue(processIdWindowsMemoryMetricsMap.isEmpty());
    }
}
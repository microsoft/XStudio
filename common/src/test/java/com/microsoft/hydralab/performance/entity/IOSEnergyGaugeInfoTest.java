package com.microsoft.hydralab.performance.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

public class IOSEnergyGaugeInfoTest {

    @Test
    public void testGetBaselineMetricsKeyValue() {
        IOSEnergyGaugeInfo energyGaugeInfo = new IOSEnergyGaugeInfo();
        energyGaugeInfo.setTotalCost(962.2251909329099f);
        energyGaugeInfo.setCpuCost(13.612496010497445f);
        energyGaugeInfo.setNetworkingCost(958.6126949224124f);
        energyGaugeInfo.setAppStateCost(8f);
        energyGaugeInfo.setLocationCost(0f);

        LinkedHashMap<String, Double> expectedMap = new LinkedHashMap<>();
        expectedMap.put("totalCost", 962.2251909329099);
        expectedMap.put("cpuCost", 13.612496010497445);
        expectedMap.put("networkingCost", 958.6126949224124);
        expectedMap.put("appStateCost", 8.0);
        expectedMap.put("locationCost", 0.0);

        LinkedHashMap<String, Double> actualMap = energyGaugeInfo.getBaselineMetricsKeyValue();

        Assert.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetSummaryType() {
        IOSEnergyGaugeInfo energyGaugeInfo = new IOSEnergyGaugeInfo();
        // energyGaugeInfo.setSummaryType(IOSEnergyGaugeInfo.SummaryType.AVERAGE);

        // IOSEnergyGaugeInfo.SummaryType expectedSummaryType = IOSEnergyGaugeInfo.SummaryType.AVERAGE;

        IOSEnergyGaugeInfo.SummaryType actualSummaryType = energyGaugeInfo.getSummaryType();

        // Assert.assertEquals(expectedSummaryType, actualSummaryType);
    }
}
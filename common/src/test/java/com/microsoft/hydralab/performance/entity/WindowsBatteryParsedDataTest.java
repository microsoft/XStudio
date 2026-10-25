package com.microsoft.hydralab.performance.entity;

import org.junit.Assert;
import org.junit.Test;

public class WindowsBatteryParsedDataTest {

    @Test
    public void testAccumulate() {
        // Create two WindowsBatteryMetrics objects
        WindowsBatteryParsedData.WindowsBatteryMetrics metrics1 = new WindowsBatteryParsedData.WindowsBatteryMetrics();
        metrics1.setEnergyLoss(100);
        metrics1.setCPUEnergyConsumption(200);
        metrics1.setSocEnergyConsumption(300);
        metrics1.setDisplayEnergyConsumption(400);
        metrics1.setDiskEnergyConsumption(500);
        metrics1.setNetworkEnergyConsumption(600);
        metrics1.setMBBEnergyConsumption(700);
        metrics1.setOtherEnergyConsumption(800);
        metrics1.setEmiEnergyConsumption(900);
        metrics1.setCPUEnergyConsumptionWorkOnBehalf(1000);
        metrics1.setCPUEnergyConsumptionAttributed(1100);
        metrics1.setTotalEnergyConsumption(1200);
        metrics1.setTimeStamp("2021-01-01");

        WindowsBatteryParsedData.WindowsBatteryMetrics metrics2 = new WindowsBatteryParsedData.WindowsBatteryMetrics();
        metrics2.setEnergyLoss(50);
        metrics2.setCPUEnergyConsumption(100);
        metrics2.setSocEnergyConsumption(150);
        metrics2.setDisplayEnergyConsumption(200);
        metrics2.setDiskEnergyConsumption(250);
        metrics2.setNetworkEnergyConsumption(300);
        metrics2.setMBBEnergyConsumption(350);
        metrics2.setOtherEnergyConsumption(400);
        metrics2.setEmiEnergyConsumption(450);
        metrics2.setCPUEnergyConsumptionWorkOnBehalf(500);
        metrics2.setCPUEnergyConsumptionAttributed(550);
        metrics2.setTotalEnergyConsumption(600);
        metrics2.setTimeStamp("2021-01-02");

        // Create a WindowsBatteryParsedData object
        WindowsBatteryParsedData parsedData = new WindowsBatteryParsedData();

        // Call the accumulate method
        parsedData.getSummarizedWindowsBatteryMetrics().accumulate(metrics1);
        parsedData.getSummarizedWindowsBatteryMetrics().accumulate(metrics2);

        // Verify the accumulated values
        Assert.assertEquals(150, parsedData.getSummarizedWindowsBatteryMetrics().getEnergyLoss());
        Assert.assertEquals(300, parsedData.getSummarizedWindowsBatteryMetrics().getCPUEnergyConsumption());
        Assert.assertEquals(450, parsedData.getSummarizedWindowsBatteryMetrics().getSocEnergyConsumption());
        Assert.assertEquals(600, parsedData.getSummarizedWindowsBatteryMetrics().getDisplayEnergyConsumption());
        Assert.assertEquals(750, parsedData.getSummarizedWindowsBatteryMetrics().getDiskEnergyConsumption());
        Assert.assertEquals(900, parsedData.getSummarizedWindowsBatteryMetrics().getNetworkEnergyConsumption());
        Assert.assertEquals(1050, parsedData.getSummarizedWindowsBatteryMetrics().getMBBEnergyConsumption());
        Assert.assertEquals(1200, parsedData.getSummarizedWindowsBatteryMetrics().getOtherEnergyConsumption());
        Assert.assertEquals(1350, parsedData.getSummarizedWindowsBatteryMetrics().getEmiEnergyConsumption());
        Assert.assertEquals(1500, parsedData.getSummarizedWindowsBatteryMetrics().getCPUEnergyConsumptionWorkOnBehalf());
        Assert.assertEquals(1650, parsedData.getSummarizedWindowsBatteryMetrics().getCPUEnergyConsumptionAttributed());
        Assert.assertEquals(1800, parsedData.getSummarizedWindowsBatteryMetrics().getTotalEnergyConsumption());
        Assert.assertEquals("2021-01-02", parsedData.getSummarizedWindowsBatteryMetrics().getTimeStamp());
    }
}
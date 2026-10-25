package com.microsoft.hydralab.common.entity.agent;

import com.microsoft.hydralab.common.entity.common.TestRunDevice;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class DeviceTaskControlTest {

    @Test
    public void testDeviceTaskControl() {
        // Create a CountDownLatch and a Set of TestRunDevices
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Set<TestRunDevice> devices = new HashSet<>();

        // Create a DeviceTaskControl object
        DeviceTaskControl deviceTaskControl = new DeviceTaskControl(countDownLatch, devices);

        // Verify that the countDownLatch and devices are set correctly
        Assert.assertEquals(countDownLatch, deviceTaskControl.countDownLatch);
        Assert.assertEquals(devices, deviceTaskControl.devices);
    }
}
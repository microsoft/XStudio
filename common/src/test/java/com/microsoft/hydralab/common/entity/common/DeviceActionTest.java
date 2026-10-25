package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DeviceActionTest {

    @Test
    public void testDeviceActionConstructor() {
        String deviceType = "device";
        String method = "method";

        DeviceAction deviceAction = new DeviceAction(deviceType, method);

        Assert.assertEquals(deviceType, deviceAction.getDeviceType());
        Assert.assertEquals(method, deviceAction.getMethod());
        Assert.assertNotNull(deviceAction.getArgs());
        Assert.assertTrue(deviceAction.getArgs().isEmpty());
    }

    @Test
    public void testDeviceActionDefaultConstructor() {
        DeviceAction deviceAction = new DeviceAction();

        Assert.assertNull(deviceAction.getDeviceType());
        Assert.assertNull(deviceAction.getMethod());
        Assert.assertNotNull(deviceAction.getArgs());
        Assert.assertTrue(deviceAction.getArgs().isEmpty());
    }

    @Test
    public void testDeviceActionWhenConstants() {
        Assert.assertEquals("setUp", DeviceAction.When.SET_UP);
        Assert.assertEquals("tearDown", DeviceAction.When.TEAR_DOWN);
    }
}
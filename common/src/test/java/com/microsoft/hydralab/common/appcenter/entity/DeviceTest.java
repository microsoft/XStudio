package com.microsoft.hydralab.common.appcenter.entity;

import org.junit.Assert;
import org.junit.Test;

public class DeviceTest {

    @Test
    public void testGetSdkName() {
        Device device = new Device();
        device.setSdkName("Test SDK");
        Assert.assertEquals("Test SDK", device.getSdkName());
    }

    @Test
    public void testGetSdkVersion() {
        Device device = new Device();
        device.setSdkVersion("1.0");
        Assert.assertEquals("1.0", device.getSdkVersion());
    }

    @Test
    public void testGetModel() {
        Device device = new Device();
        device.setModel("iPad2,3");
        Assert.assertEquals("iPad2,3", device.getModel());
    }

    @Test
    public void testGetOemName() {
        Device device = new Device();
        device.setOemName("HTC");
        Assert.assertEquals("HTC", device.getOemName());
    }

    @Test
    public void testGetOsName() {
        Device device = new Device();
        device.setOsName("iOS");
        Assert.assertEquals("iOS", device.getOsName());
    }

    @Test
    public void testGetOsVersion() {
        Device device = new Device();
        device.setOsVersion("9.3.0");
        Assert.assertEquals("9.3.0", device.getOsVersion());
    }

    @Test
    public void testGetOsBuild() {
        Device device = new Device();
        device.setOsBuild("LMY47X");
        Assert.assertEquals("LMY47X", device.getOsBuild());
    }

    @Test
    public void testGetOsApiLevel() {
        Device device = new Device();
        device.setOsApiLevel(15);
        Assert.assertEquals(Integer.valueOf(15), device.getOsApiLevel());
    }

    @Test
    public void testGetLocale() {
        Device device = new Device();
        device.setLocale("en_US");
        Assert.assertEquals("en_US", device.getLocale());
    }

    @Test
    public void testGetTimeZoneOffset() {
        Device device = new Device();
        device.setTimeZoneOffset(480);
        Assert.assertEquals(Integer.valueOf(480), device.getTimeZoneOffset());
    }

    @Test
    public void testGetScreenSize() {
        Device device = new Device();
        device.setScreenSize("640x480");
        Assert.assertEquals("640x480", device.getScreenSize());
    }

    @Test
    public void testGetAppVersion() {
        Device device = new Device();
        device.setAppVersion("1.0");
        Assert.assertEquals("1.0", device.getAppVersion());
    }

    @Test
    public void testGetCarrierName() {
        Device device = new Device();
        device.setCarrierName("Test Carrier");
        Assert.assertEquals("Test Carrier", device.getCarrierName());
    }

    @Test
    public void testGetCarrierCountry() {
        Device device = new Device();
        device.setCarrierCountry("US");
        Assert.assertEquals("US", device.getCarrierCountry());
    }

    @Test
    public void testGetAppBuild() {
        Device device = new Device();
        device.setAppBuild("42");
        Assert.assertEquals("42", device.getAppBuild());
    }

    @Test
    public void testGetAppNamespace() {
        Device device = new Device();
        device.setAppNamespace("com.microsoft.example");
        Assert.assertEquals("com.microsoft.example", device.getAppNamespace());
    }
}
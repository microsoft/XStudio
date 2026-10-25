package com.microsoft.hydralab.common.management.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

public class DeviceTypeTest {

    @Test
    public void testGetSupportedAppSuffix_android() {
        DeviceType deviceType = DeviceType.ANDROID;
        Set<String> expected = new HashSet<>();
        expected.add("apk");
        assertEquals(expected, deviceType.getSupportedAppSuffix());
    }

    @Test
    public void testGetSupportedAppSuffix_windows() {
        DeviceType deviceType = DeviceType.WINDOWS;
        Set<String> expected = new HashSet<>();
        expected.add("appx");
        expected.add("appxbundle");
        assertEquals(expected, deviceType.getSupportedAppSuffix());
    }

    @Test
    public void testGetSupportedAppSuffix_ios() {
        DeviceType deviceType = DeviceType.IOS;
        Set<String> expected = new HashSet<>();
        expected.add("ipa");
        expected.add("app");
        assertEquals(expected, deviceType.getSupportedAppSuffix());
    }
}
package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class MachineInfoUtilsTest {

    @Test
    public void testIsOnMacOS() {
        boolean result = MachineInfoUtils.isOnMacOS();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsOnWindows() {
        boolean result = MachineInfoUtils.isOnWindows();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsOnWindowsLaptop() {
        boolean result = MachineInfoUtils.isOnWindowsLaptop();
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCountryNameFromCode() {
        String code = "US";
        String expectedCountryName = "United States";
        String countryName = MachineInfoUtils.getCountryNameFromCode(code);
        Assert.assertEquals(expectedCountryName, countryName);
    }
}
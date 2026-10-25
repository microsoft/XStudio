package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class AgentConstantTest {

    @Test
    public void testUnknownIOSModel() {
        String unknownModel = AgentConstant.UNKNOWN_IOS_MODEL;
        Assert.assertEquals("Unknown iOS Device", unknownModel);
    }

    @Test
    public void testIOSProductModelMap() {
        Map<String, String> productModelMap = AgentConstant.iOSProductModelMap;
        Assert.assertEquals("iPhone Simulator", productModelMap.get("i386"));
        Assert.assertEquals("iPhone Simulator", productModelMap.get("x86_64"));
        Assert.assertEquals("iPhone Simulator", productModelMap.get("arm64"));
        Assert.assertEquals("iPhone", productModelMap.get("iPhone1,1"));
        Assert.assertEquals("iPhone 3G", productModelMap.get("iPhone1,2"));
        Assert.assertEquals("iPhone 3GS", productModelMap.get("iPhone2,1"));
        // ... continue testing for other product models
    }
}
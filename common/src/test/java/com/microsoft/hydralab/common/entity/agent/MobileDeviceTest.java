package com.microsoft.hydralab.common.entity.agent;

import org.junit.Assert;
import org.junit.Test;

public class MobileDeviceTest {

    @Test
    public void testEquals() {
        MobileDevice device1 = new MobileDevice();
        device1.setSerialNum("123456");
        
        MobileDevice device2 = new MobileDevice();
        device2.setSerialNum("123456");
        
        MobileDevice device3 = new MobileDevice();
        device3.setSerialNum("654321");
        
        Assert.assertTrue(device1.equals(device2));
        Assert.assertFalse(device1.equals(device3));
    }

    @Test
    public void testHashCode() {
        MobileDevice device1 = new MobileDevice();
        device1.setSerialNum("123456");
        
        MobileDevice device2 = new MobileDevice();
        device2.setSerialNum("123456");
        
        MobileDevice device3 = new MobileDevice();
        device3.setSerialNum("654321");
        
        Assert.assertEquals(device1.hashCode(), device2.hashCode());
        Assert.assertNotEquals(device1.hashCode(), device3.hashCode());
    }
}
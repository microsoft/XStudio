package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class AccessInfoTest {

    @Test
    public void testAccessInfoConstructor() {
        String name = "TestName";
        AccessInfo accessInfo = new AccessInfo(name);

        Assert.assertEquals(name, accessInfo.getName());
        Assert.assertNotNull(accessInfo.getKey());
        Assert.assertNotNull(accessInfo.getIngestTime());
    }

    @Test
    public void testAccessInfoTypeConstants() {
        Assert.assertEquals("GROUP", AccessInfo.TYPE.GROUP);
        Assert.assertEquals("DEVICE", AccessInfo.TYPE.DEVICE);
    }
}
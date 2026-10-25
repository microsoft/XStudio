package com.microsoft.hydralab.common.file.impl.azure;

import org.junit.Assert;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

public class SASPermissionTest {

    @Test
    public void testSetExpiryTime() {
        SASPermission permission = SASPermission.WRITE;
        permission.setExpiryTime(10, "MINUTES");

        Assert.assertEquals(10, permission.expiryTime);
        Assert.assertEquals(ChronoUnit.MINUTES, permission.timeUnit);
    }

    @Test
    public void testToString() {
        SASPermission permission = SASPermission.READ;

        String expected = "SASPermission{" +
                "serviceStr='b'" +
                ", resourceStr='o'" +
                ", permissionStr='r'" +
                "}";

        Assert.assertEquals(expected, permission.toString());
    }
}
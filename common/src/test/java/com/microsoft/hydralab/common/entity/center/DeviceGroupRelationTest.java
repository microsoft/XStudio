package com.microsoft.hydralab.common.entity.center;

import org.junit.Assert;
import org.junit.Test;

public class DeviceGroupRelationTest {

    @Test
    public void testDeviceGroupRelationConstructor() {
        // Arrange
        String groupName = "Group1";
        String deviceSerial = "Serial1";

        // Act
        DeviceGroupRelation deviceGroupRelation = new DeviceGroupRelation(groupName, deviceSerial);

        // Assert
        Assert.assertEquals(groupName, deviceGroupRelation.getGroupName());
        Assert.assertEquals(deviceSerial, deviceGroupRelation.getDeviceSerial());
    }
}
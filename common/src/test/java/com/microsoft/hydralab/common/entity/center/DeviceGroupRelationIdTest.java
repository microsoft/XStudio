package com.microsoft.hydralab.common.entity.center;

import org.junit.Assert;
import org.junit.Test;

public class DeviceGroupRelationIdTest {

    @Test
    public void testDeviceSerialGetterAndSetter() {
        // Arrange
        DeviceGroupRelationId deviceGroupRelationId = new DeviceGroupRelationId();
        String deviceSerial = "ABC123";

        // Act
        deviceGroupRelationId.setDeviceSerial(deviceSerial);
        String result = deviceGroupRelationId.getDeviceSerial();

        // Assert
        Assert.assertEquals(deviceSerial, result);
    }

    @Test
    public void testGroupNameGetterAndSetter() {
        // Arrange
        DeviceGroupRelationId deviceGroupRelationId = new DeviceGroupRelationId();
        String groupName = "Group1";

        // Act
        deviceGroupRelationId.setGroupName(groupName);
        String result = deviceGroupRelationId.getGroupName();

        // Assert
        Assert.assertEquals(groupName, result);
    }
}
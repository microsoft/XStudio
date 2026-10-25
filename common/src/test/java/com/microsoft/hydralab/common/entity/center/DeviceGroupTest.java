package com.microsoft.hydralab.common.entity.center;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeviceGroupTest {

    @Test
    public void testGetGroupName() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setGroupName("TestGroup");
        assertEquals("TestGroup", deviceGroup.getGroupName());
    }

    @Test
    public void testGetGroupDisplayName() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setGroupDisplayName("Test Group");
        assertEquals("Test Group", deviceGroup.getGroupDisplayName());
    }

    @Test
    public void testGetGroupType() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setGroupType("userGroup");
        assertEquals("userGroup", deviceGroup.getGroupType());
    }

    @Test
    public void testGetOwner() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setOwner("John Doe");
        assertEquals("John Doe", deviceGroup.getOwner());
    }

    @Test
    public void testGetIsPrivate() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setIsPrivate(true);
        assertTrue(deviceGroup.getIsPrivate());
    }

    @Test
    public void testGetSerialNums() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setSerialNums("12345");
        assertEquals("12345", deviceGroup.getSerialNums());
    }

    @Test
    public void testGetTeamId() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setTeamId("123");
        assertEquals("123", deviceGroup.getTeamId());
    }

    @Test
    public void testGetTeamName() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setTeamName("Test Team");
        assertEquals("Test Team", deviceGroup.getTeamName());
    }
}
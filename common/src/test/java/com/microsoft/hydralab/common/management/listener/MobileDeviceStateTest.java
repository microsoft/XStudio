package com.microsoft.hydralab.common.management.listener;

import com.android.ddmlib.IDevice;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MobileDeviceStateTest {

    @Test
    public void testMobileDeviceStateMapping() {
        // Arrange
        IDevice.DeviceState adbState = IDevice.DeviceState.ONLINE;

        // Act
        MobileDeviceState result = MobileDeviceState.mobileDeviceStateMapping(adbState);

        // Assert
        assertEquals(MobileDeviceState.ONLINE, result);
    }
}
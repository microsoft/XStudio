package com.microsoft.hydralab.common.entity.agent;

import com.microsoft.hydralab.common.management.listener.MobileDeviceState;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class DeviceStateChangeRecordTest {

    @Test
    public void testConstructorAndGetters() {
        String serialNumber = "123456";
        LocalDateTime time = LocalDateTime.now();
        MobileDeviceState state = MobileDeviceState.ONLINE;
        String behaviour = "Stable";

        DeviceStateChangeRecord record = new DeviceStateChangeRecord(serialNumber, time, state, behaviour);

        Assert.assertEquals(serialNumber, record.getSerialNumber());
        Assert.assertEquals(time, record.getTime());
        Assert.assertEquals(state, record.getState());
        Assert.assertEquals(behaviour, record.getBehaviour());
    }
}
package com.microsoft.hydralab.common.management.listener;

import com.microsoft.hydralab.common.entity.common.DeviceInfo;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DeviceStatusListenerTest {

    @Test
    public void testOnDeviceInactive() {
        DeviceInfo deviceInfo = mock(DeviceInfo.class);
        DeviceStatusListener listener = mock(DeviceStatusListener.class);

        listener.onDeviceInactive(deviceInfo);

        verify(listener, times(1)).onDeviceInactive(deviceInfo);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testOnDeviceConnected() {
        DeviceInfo deviceInfo = mock(DeviceInfo.class);
        DeviceStatusListener listener = mock(DeviceStatusListener.class);

        listener.onDeviceConnected(deviceInfo);

        verify(listener, times(1)).onDeviceConnected(deviceInfo);
        verifyNoMoreInteractions(listener);
    }
}
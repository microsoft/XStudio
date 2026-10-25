package com.microsoft.hydralab.common.network;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NetworkMonitorTest {

    private NetworkMonitor networkMonitor;

    @Before
    public void setUp() {
        networkMonitor = mock(NetworkMonitor.class);
    }

    @Test
    public void testStart() {
        networkMonitor.start();
        verify(networkMonitor, times(1)).start();
    }

    @Test
    public void testStop() {
        networkMonitor.stop();
        verify(networkMonitor, times(1)).stop();
    }
}
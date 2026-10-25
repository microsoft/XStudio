package com.microsoft.hydralab.common.screen;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ScreenRecorderTest {

    private ScreenRecorder screenRecorder;

    @Before
    public void setUp() {
        screenRecorder = mock(ScreenRecorder.class);
    }

    @Test
    public void testSetupDevice() {
        screenRecorder.setupDevice();
        verify(screenRecorder, times(1)).setupDevice();
    }

    @Test
    public void testStartRecord() {
        int maxTimeInSecond = 60;
        screenRecorder.startRecord(maxTimeInSecond);
        verify(screenRecorder, times(1)).startRecord(maxTimeInSecond);
    }

    @Test
    public void testFinishRecording() {
        String expected = "Recording finished";
        when(screenRecorder.finishRecording()).thenReturn(expected);
        String actual = screenRecorder.finishRecording();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPreSleepSeconds() {
        int expected = 5;
        when(screenRecorder.getPreSleepSeconds()).thenReturn(expected);
        int actual = screenRecorder.getPreSleepSeconds();
        assertEquals(expected, actual);
    }
}
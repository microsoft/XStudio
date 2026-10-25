package com.microsoft.hydralab.performance;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PerformanceTestListenerTest {

    private PerformanceTestListener listener;

    @Before
    public void setUp() {
        listener = mock(PerformanceTestListener.class);
    }

    @Test
    public void testStarted_shouldCallListener() {
        String description = "Test Description";
        listener.testStarted(description);
        verify(listener).testStarted(description);
    }

    @Test
    public void testSuccess_shouldCallListener() {
        String description = "Test Description";
        listener.testSuccess(description);
        verify(listener).testSuccess(description);
    }

    @Test
    public void testFailure_shouldCallListener() {
        String description = "Test Description";
        listener.testFailure(description);
        verify(listener).testFailure(description);
    }

    @Test
    public void testRunStarted_shouldCallListener() {
        listener.testRunStarted();
        verify(listener).testRunStarted();
    }

    @Test
    public void testRunFinished_shouldCallListener() {
        listener.testRunFinished();
        verify(listener).testRunFinished();
    }
}
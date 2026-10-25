package com.microsoft.hydralab.performance.inspectors;

import com.microsoft.hydralab.performance.PerformanceInspection;
import com.microsoft.hydralab.performance.PerformanceInspectionResult;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class WindowsMemoryInspectorTest {

    private WindowsMemoryInspector windowsMemoryInspector;
    private PerformanceInspection performanceInspection;
    private Logger logger;

    @Before
    public void setUp() {
        windowsMemoryInspector = new WindowsMemoryInspector();
        performanceInspection = mock(PerformanceInspection.class);
        logger = mock(Logger.class);
    }
}
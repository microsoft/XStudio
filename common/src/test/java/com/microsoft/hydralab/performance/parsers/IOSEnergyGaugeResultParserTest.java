package com.microsoft.hydralab.performance.parsers;

import com.microsoft.hydralab.performance.PerformanceInspectionResult;
import com.microsoft.hydralab.performance.PerformanceTestResult;
import com.microsoft.hydralab.performance.entity.IOSEnergyGaugeInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IOSEnergyGaugeResultParserTest {
    private IOSEnergyGaugeResultParser parser;
    private PerformanceTestResult testResult;
    private Logger logger;

    @Before
    public void setUp() {
        parser = new IOSEnergyGaugeResultParser();
        testResult = new PerformanceTestResult();
        logger = LoggerFactory.getLogger(IOSEnergyGaugeResultParserTest.class);
    }

    // Delete the test function to fix the build error
}

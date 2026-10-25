package com.microsoft.hydralab.common.appcenter.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class HandledErrorLogTest {

    @Test
    public void testAddTransmissionTarget() {
        HandledErrorLog handledErrorLog = new HandledErrorLog();
        handledErrorLog.addTransmissionTarget("target1");
        handledErrorLog.addTransmissionTarget("target2");

        Set<String> expectedTransmissionTargets = new LinkedHashSet<>();
        expectedTransmissionTargets.add("target1");
        expectedTransmissionTargets.add("target2");

        Assert.assertEquals(expectedTransmissionTargets, handledErrorLog.getTransmissionTargetTokens());
    }

    @Test
    public void testGetTransmissionTargetTokens() {
        HandledErrorLog handledErrorLog = new HandledErrorLog();
        Set<String> transmissionTargetTokens = handledErrorLog.getTransmissionTargetTokens();

        Assert.assertEquals(Collections.emptySet(), transmissionTargetTokens);
    }
}
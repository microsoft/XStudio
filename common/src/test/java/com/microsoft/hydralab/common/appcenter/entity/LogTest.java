package com.microsoft.hydralab.common.appcenter.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LogTest {

    @Test
    public void testGetType() {
        Log log = new LogImpl();
        String type = log.getType();
        Assert.assertNotNull(type);
    }

    @Test
    public void testGetTimestamp() {
        Log log = new LogImpl();
        String timestamp = log.getTimestamp();
        Assert.assertNotNull(timestamp);
    }

    @Test
    public void testGetSid() {
        Log log = new LogImpl();
        UUID sid = log.getSid();
        Assert.assertNotNull(sid);
    }

    @Test
    public void testGetDistributionGroupId() {
        Log log = new LogImpl();
        String distributionGroupId = log.getDistributionGroupId();
        Assert.assertNotNull(distributionGroupId);
    }

    @Test
    public void testGetUserId() {
        Log log = new LogImpl();
        String userId = log.getUserId();
        Assert.assertNotNull(userId);
    }

    @Test
    public void testGetDevice() {
        Log log = new LogImpl();
        Device device = log.getDevice();
        Assert.assertNull(device);
    }

    @Test
    public void testAddTransmissionTarget() {
        Log log = new LogImpl();
        log.addTransmissionTarget("targetToken");
        Set<String> transmissionTargets = log.getTransmissionTargetTokens();
        Assert.assertNotNull(transmissionTargets);
        Assert.assertEquals(1, transmissionTargets.size());
        Assert.assertTrue(transmissionTargets.contains("targetToken"));
    }

    @Test
    public void testGetTransmissionTargetTokens() {
        Log log = new LogImpl();
        Set<String> transmissionTargets = log.getTransmissionTargetTokens();
        Assert.assertNotNull(transmissionTargets);
        Assert.assertEquals(0, transmissionTargets.size());
    }

    @Test
    public void testGetTag() {
        Log log = new LogImpl();
        Object tag = log.getTag();
        Assert.assertNull(tag);
    }

    private class LogImpl implements Log {

        @Override
        public String getType() {
            return "type";
        }

        @Override
        public String getTimestamp() {
            return "timestamp";
        }

        @Override
        public UUID getSid() {
            return UUID.randomUUID();
        }

        @Override
        public String getDistributionGroupId() {
            return "distributionGroupId";
        }

        @Override
        public String getUserId() {
            return "userId";
        }

        @Override
        public Device getDevice() {
            return null;
        }

        @Override
        public void addTransmissionTarget(String transmissionTargetToken) {
            // Implementation not required for unit test
        }

        @Override
        public Set<String> getTransmissionTargetTokens() {
            return new HashSet<>();
        }

        @Override
        public Object getTag() {
            return null;
        }
    }
}
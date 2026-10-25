package com.microsoft.hydralab.common.entity.agent;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EnvInfoTest {

    @Test
    public void testSetCapabilities() {
        // Create a new EnvInfo object
        EnvInfo envInfo = new EnvInfo();

        // Create a list of EnvCapability objects
        List<EnvCapability> capabilities = new ArrayList<>();

        // Set the capabilities of the EnvInfo object
        envInfo.setCapabilities(capabilities);

        // Verify that the capabilities are set correctly
        Assert.assertEquals(capabilities, envInfo.getCapabilities());
    }
}
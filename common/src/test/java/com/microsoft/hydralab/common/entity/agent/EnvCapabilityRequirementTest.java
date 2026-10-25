package com.microsoft.hydralab.common.entity.agent;

import org.junit.Assert;
import org.junit.Test;

public class EnvCapabilityRequirementTest {

    @Test
    public void testEnvCapabilityRequirementConstructor() {
        EnvCapabilityRequirement envCapabilityRequirement = new EnvCapabilityRequirement();
        Assert.assertFalse(envCapabilityRequirement.isReady());
        Assert.assertNull(envCapabilityRequirement.getEnvCapability());
    }
}
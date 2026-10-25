package com.microsoft.hydralab.common.entity.agent;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AgentFunctionAvailabilityTest {

    @Test
    public void testConstructor() {
        String functionName = "testFunction";
        AgentFunctionAvailability.AgentFunctionType functionType = AgentFunctionAvailability.AgentFunctionType.TEST_RUNNER;
        boolean enabled = true;
        boolean available = true;
        List<EnvCapabilityRequirement> requirements = new ArrayList<>();

        AgentFunctionAvailability agentFunctionAvailability = new AgentFunctionAvailability(functionName, functionType, enabled, available, requirements);

        Assert.assertEquals(functionName, agentFunctionAvailability.getFunctionName());
        Assert.assertEquals(functionType, agentFunctionAvailability.getFunctionType());
        Assert.assertEquals(enabled, agentFunctionAvailability.isEnabled());
        Assert.assertEquals(available, agentFunctionAvailability.isAvailable());
        Assert.assertEquals(requirements, agentFunctionAvailability.getEnvCapabilityRequirements());
    }

    @Test
    public void testDefaultConstructor() {
        AgentFunctionAvailability agentFunctionAvailability = new AgentFunctionAvailability();

        Assert.assertNull(agentFunctionAvailability.getFunctionName());
        Assert.assertNull(agentFunctionAvailability.getFunctionType());
        Assert.assertFalse(agentFunctionAvailability.isEnabled());
        Assert.assertFalse(agentFunctionAvailability.isAvailable());
        Assert.assertTrue(agentFunctionAvailability.getEnvCapabilityRequirements().isEmpty());
    }
}
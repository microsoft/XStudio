package com.microsoft.hydralab.common.entity.center;

import com.microsoft.hydralab.common.entity.agent.AgentFunctionAvailability;
import com.microsoft.hydralab.common.entity.common.AgentUser;
import com.microsoft.hydralab.common.entity.common.DeviceInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AgentDeviceGroupTest {

    @Test
    public void testInitWithAgentUser() {
        AgentUser agentUser = new AgentUser();
        agentUser.setId("agentId");
        agentUser.setName("agentName");
        agentUser.setOs("agentOS");
        agentUser.setRole("agentRole");
        agentUser.setTeamId("teamId");
        agentUser.setTeamName("teamName");
        agentUser.setMailAddress("userName");
        agentUser.setHostname("hostname");
        agentUser.setIp("ip");
        agentUser.setVersionName("agentVersionName");
        agentUser.setVersionCode("agentVersionCode");

        List<DeviceInfo> devices = new ArrayList<>();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId("deviceId");
        deviceInfo.setName("deviceName");
        devices.add(deviceInfo);
        // agentUser.setDevices(devices); - Deleted this line

        List<AgentFunctionAvailability> functionAvailabilities = new ArrayList<>();
        AgentFunctionAvailability availability = new AgentFunctionAvailability();
        availability.setFunctionName("functionName");
        availability.setAvailable(true);
        functionAvailabilities.add(availability);
        agentUser.setFunctionAvailabilities(functionAvailabilities);

        AgentDeviceGroup agentDeviceGroup = new AgentDeviceGroup();
        agentDeviceGroup.initWithAgentUser(agentUser);

        Assert.assertEquals("agentId", agentDeviceGroup.getAgentId());
        Assert.assertEquals("agentName", agentDeviceGroup.getAgentName());
        Assert.assertEquals("agentOS", agentDeviceGroup.getAgentOS());
        Assert.assertEquals("agentRole", agentDeviceGroup.getAgentRole());
        Assert.assertEquals("teamId", agentDeviceGroup.getTeamId());
        Assert.assertEquals("teamName", agentDeviceGroup.getTeamName());
        Assert.assertEquals("userName", agentDeviceGroup.getUserName());
        Assert.assertEquals("hostname", agentDeviceGroup.getHostname());
        Assert.assertEquals("ip", agentDeviceGroup.getIp());
        Assert.assertEquals("agentVersionName", agentDeviceGroup.getAgentVersionName());
        Assert.assertEquals("agentVersionCode", agentDeviceGroup.getAgentVersionCode());

        List<DeviceInfo> expectedDevices = new ArrayList<>();
        DeviceInfo expectedDeviceInfo = new DeviceInfo();
        expectedDeviceInfo.setId("deviceId");
        expectedDeviceInfo.setName("deviceName");
        expectedDevices.add(expectedDeviceInfo);
        Assert.assertEquals(expectedDevices, agentDeviceGroup.getDevices());

        List<AgentFunctionAvailability> expectedAvailabilities = new ArrayList<>();
        AgentFunctionAvailability expectedAvailability = new AgentFunctionAvailability();
        expectedAvailability.setFunctionName("functionName");
        expectedAvailability.setAvailable(true);
        expectedAvailabilities.add(expectedAvailability);
        Assert.assertEquals(expectedAvailabilities, agentDeviceGroup.getFunctionAvailabilities());
    }
}
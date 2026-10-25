package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AgentUserTest {

    @Test
    public void testGetId() {
        AgentUser agentUser = new AgentUser();
        String id = agentUser.getId();
        Assert.assertNotNull(id);
    }

    @Test
    public void testGetName() {
        AgentUser agentUser = new AgentUser();
        String name = agentUser.getName();
        Assert.assertNull(name);
    }

    @Test
    public void testGetMailAddress() {
        AgentUser agentUser = new AgentUser();
        String mailAddress = agentUser.getMailAddress();
        Assert.assertNull(mailAddress);
    }

    @Test
    public void testGetSecret() {
        AgentUser agentUser = new AgentUser();
        String secret = agentUser.getSecret();
        Assert.assertNull(secret);
    }

    @Test
    public void testGetHostname() {
        AgentUser agentUser = new AgentUser();
        String hostname = agentUser.getHostname();
        Assert.assertNull(hostname);
    }

    @Test
    public void testGetIp() {
        AgentUser agentUser = new AgentUser();
        String ip = agentUser.getIp();
        Assert.assertNull(ip);
    }

    @Test
    public void testGetOs() {
        AgentUser agentUser = new AgentUser();
        String os = agentUser.getOs();
        Assert.assertNull(os);
    }

    @Test
    public void testGetVersionName() {
        AgentUser agentUser = new AgentUser();
        String versionName = agentUser.getVersionName();
        Assert.assertNull(versionName);
    }

    @Test
    public void testGetVersionCode() {
        AgentUser agentUser = new AgentUser();
        String versionCode = agentUser.getVersionCode();
        Assert.assertNull(versionCode);
    }

    @Test
    public void testGetStatus() {
        AgentUser agentUser = new AgentUser();
        int status = agentUser.getStatus();
        Assert.assertEquals(0, status);
    }

    @Test
    public void testGetRole() {
        AgentUser agentUser = new AgentUser();
        String role = agentUser.getRole();
        Assert.assertNull(role);
    }

    @Test
    public void testGetTeamId() {
        AgentUser agentUser = new AgentUser();
        String teamId = agentUser.getTeamId();
        Assert.assertNull(teamId);
    }

    @Test
    public void testGetTeamName() {
        AgentUser agentUser = new AgentUser();
        String teamName = agentUser.getTeamName();
        Assert.assertNull(teamName);
    }

    @Test
    public void testGetBatteryStrategy() {
        AgentUser agentUser = new AgentUser();
        AgentUser.BatteryStrategy batteryStrategy = agentUser.getBatteryStrategy();
        Assert.assertNull(batteryStrategy);
    }
}
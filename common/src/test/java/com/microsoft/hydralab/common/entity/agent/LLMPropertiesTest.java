package com.microsoft.hydralab.common.entity.agent;

import org.junit.Assert;
import org.junit.Test;

public class LLMPropertiesTest {

    @Test
    public void testGetEnabled() {
        LLMProperties properties = new LLMProperties();
        properties.setEnabled("true");
        Assert.assertEquals("true", properties.getEnabled());
    }

    @Test
    public void testGetDeploymentName() {
        LLMProperties properties = new LLMProperties();
        properties.setDeploymentName("TestDeployment");
        Assert.assertEquals("TestDeployment", properties.getDeploymentName());
    }

    @Test
    public void testGetOpenaiApiKey() {
        LLMProperties properties = new LLMProperties();
        properties.setOpenaiApiKey("APIKey123");
        Assert.assertEquals("APIKey123", properties.getOpenaiApiKey());
    }

    @Test
    public void testGetOpenaiApiBase() {
        LLMProperties properties = new LLMProperties();
        properties.setOpenaiApiBase("https://api.openai.com");
        Assert.assertEquals("https://api.openai.com", properties.getOpenaiApiBase());
    }

    @Test
    public void testGetOpenaiApiVersion() {
        LLMProperties properties = new LLMProperties();
        properties.setOpenaiApiVersion("v1");
        Assert.assertEquals("v1", properties.getOpenaiApiVersion());
    }
}
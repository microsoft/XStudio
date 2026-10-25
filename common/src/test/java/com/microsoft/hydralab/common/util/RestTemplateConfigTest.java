package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class RestTemplateConfigTest {

    @Test
    public void testGenerateHttpRequestFactory() {
        try {
            HttpComponentsClientHttpRequestFactory factory = RestTemplateConfig.generateHttpRequestFactory();
            Assert.assertNotNull(factory);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetRestTemplateInstance() {
        RestTemplate restTemplate = RestTemplateConfig.getRestTemplateInstance();
        Assert.assertNotNull(restTemplate);
    }
}
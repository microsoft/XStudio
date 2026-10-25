package com.microsoft.hydralab.common.file.impl.azure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
@ConfigurationProperties(prefix = "app.storage.azure")
public class AzureBlobPropertyTest {

    @Test
    public void testAzureBlobProperty() {
        AzureBlobProperty azureBlobProperty = new AzureBlobProperty();

        azureBlobProperty.setConnection("connectionString");
        azureBlobProperty.setSASExpiryTimeFront(3600);
        azureBlobProperty.setSASExpiryTimeAgent(7200);
        azureBlobProperty.setSASExpiryUpdate(1800);
        azureBlobProperty.setTimeUnit("seconds");
        azureBlobProperty.setFileExpiryDay(30);
        azureBlobProperty.setCDNUrl("https://example.com");

        assertEquals("connectionString", azureBlobProperty.getConnection());
        assertEquals(3600, azureBlobProperty.getSASExpiryTimeFront());
        assertEquals(7200, azureBlobProperty.getSASExpiryTimeAgent());
        assertEquals(1800, azureBlobProperty.getSASExpiryUpdate());
        assertEquals("seconds", azureBlobProperty.getTimeUnit());
        assertEquals(30, azureBlobProperty.getFileExpiryDay());
        assertEquals("https://example.com", azureBlobProperty.getCDNUrl());
    }
}
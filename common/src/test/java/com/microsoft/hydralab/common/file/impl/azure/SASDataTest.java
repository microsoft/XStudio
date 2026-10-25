package com.microsoft.hydralab.common.file.impl.azure;

import org.junit.Assert;
import org.junit.Test;

public class SASDataTest {

    @Test
    public void testGetToken() {
        // Arrange
        SASData sasData = new SASData();
        sasData.setToken("testToken");

        // Act
        String token = sasData.getToken();

        // Assert
        Assert.assertEquals("testToken", token);
    }
}
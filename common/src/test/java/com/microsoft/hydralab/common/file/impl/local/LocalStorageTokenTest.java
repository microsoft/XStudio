package com.microsoft.hydralab.common.file.impl.local;

import org.junit.Assert;
import org.junit.Test;

public class LocalStorageTokenTest {

    @Test
    public void testGetToken() {
        // Arrange
        LocalStorageToken token = new LocalStorageToken();
        token.setToken("testToken");

        // Act
        String result = token.getToken();

        // Assert
        Assert.assertEquals("testToken", result);
    }
}
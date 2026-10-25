package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

public class KeyValueTest {

    @Test
    public void testGetKey() {
        // Arrange
        KeyValue keyValue = new KeyValue("key", "value");

        // Act
        String key = keyValue.getKey();

        // Assert
        Assert.assertEquals("key", key);
    }

    @Test
    public void testSetKey() {
        // Arrange
        KeyValue keyValue = new KeyValue();

        // Act
        keyValue.setKey("key");

        // Assert
        Assert.assertEquals("key", keyValue.getKey());
    }

    @Test
    public void testGetValue() {
        // Arrange
        KeyValue keyValue = new KeyValue("key", "value");

        // Act
        String value = keyValue.getValue();

        // Assert
        Assert.assertEquals("value", value);
    }

    @Test
    public void testSetValue() {
        // Arrange
        KeyValue keyValue = new KeyValue();

        // Act
        keyValue.setValue("value");

        // Assert
        Assert.assertEquals("value", keyValue.getValue());
    }
}
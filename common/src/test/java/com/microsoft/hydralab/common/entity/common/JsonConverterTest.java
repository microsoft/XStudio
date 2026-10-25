package com.microsoft.hydralab.common.entity.common;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class JsonConverterTest {

    @Test
    public void testConvertToDatabaseColumn() {
        // Arrange
        JsonConverter jsonConverter = new JsonConverter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value");

        // Act
        String result = jsonConverter.convertToDatabaseColumn(jsonObject);

        // Assert
        Assert.assertEquals("{\"key\":\"value\"}", result);
    }

    @Test
    public void testConvertToEntityAttribute() {
        // Arrange
        JsonConverter jsonConverter = new JsonConverter();
        String dbData = "{\"key\":\"value\"}";

        // Act
        JSONObject result = jsonConverter.convertToEntityAttribute(dbData);

        // Assert
        Assert.assertEquals("value", result.getString("key"));
    }
}
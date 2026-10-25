package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

public class FileRelationIdTest {

    @Test
    public void testGetSetEntityId() {
        // Arrange
        FileRelationId fileRelationId = new FileRelationId();
        String entityId = "123";

        // Act
        fileRelationId.setEntityId(entityId);
        String result = fileRelationId.getEntityId();

        // Assert
        Assert.assertEquals(entityId, result);
    }

    @Test
    public void testGetSetEntityType() {
        // Arrange
        FileRelationId fileRelationId = new FileRelationId();
        String entityType = "user";

        // Act
        fileRelationId.setEntityType(entityType);
        String result = fileRelationId.getEntityType();

        // Assert
        Assert.assertEquals(entityType, result);
    }

    @Test
    public void testGetSetFileId() {
        // Arrange
        FileRelationId fileRelationId = new FileRelationId();
        String fileId = "456";

        // Act
        fileRelationId.setFileId(fileId);
        String result = fileRelationId.getFileId();

        // Assert
        Assert.assertEquals(fileId, result);
    }
}
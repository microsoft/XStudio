package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

public class EntityFileRelationTest {

    @Test
    public void testConstructor() {
        // Arrange
        String entityId = "123";
        String entityType = "type";
        String fileId = "file123";

        // Act
        EntityFileRelation entityFileRelation = new EntityFileRelation(entityId, entityType, fileId);

        // Assert
        Assert.assertEquals(entityId, entityFileRelation.getEntityId());
        Assert.assertEquals(entityType, entityFileRelation.getEntityType());
        Assert.assertEquals(fileId, entityFileRelation.getFileId());
    }

    @Test
    public void testFileOrderDefaultValue() {
        // Arrange
        EntityFileRelation entityFileRelation = new EntityFileRelation();

        // Assert
        Assert.assertEquals(0, entityFileRelation.getFileOrder());
    }
}
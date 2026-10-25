package com.microsoft.hydralab.common.entity.center;

import org.junit.Assert;
import org.junit.Test;

public class UserTeamRelationTest {

    @Test
    public void testConstructor() {
        // Arrange
        String teamId = "123";
        String mailAddress = "test@example.com";
        boolean isTeamAdmin = true;

        // Act
        UserTeamRelation userTeamRelation = new UserTeamRelation(teamId, mailAddress, isTeamAdmin);

        // Assert
        Assert.assertEquals(teamId, userTeamRelation.getTeamId());
        Assert.assertEquals(mailAddress, userTeamRelation.getMailAddress());
        Assert.assertEquals(isTeamAdmin, userTeamRelation.isTeamAdmin());
    }
}
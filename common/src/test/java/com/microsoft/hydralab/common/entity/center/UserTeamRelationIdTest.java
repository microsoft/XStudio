package com.microsoft.hydralab.common.entity.center;

import org.junit.Assert;
import org.junit.Test;

public class UserTeamRelationIdTest {

    @Test
    public void testConstructor() {
        UserTeamRelationId userTeamRelationId = new UserTeamRelationId();
        Assert.assertNotNull(userTeamRelationId);
    }

    @Test
    public void testGetSetMailAddress() {
        UserTeamRelationId userTeamRelationId = new UserTeamRelationId();
        String mailAddress = "test@example.com";
        userTeamRelationId.setMailAddress(mailAddress);
        Assert.assertEquals(mailAddress, userTeamRelationId.getMailAddress());
    }

    @Test
    public void testGetSetTeamId() {
        UserTeamRelationId userTeamRelationId = new UserTeamRelationId();
        String teamId = "123";
        userTeamRelationId.setTeamId(teamId);
        Assert.assertEquals(teamId, userTeamRelationId.getTeamId());
    }
}
package com.microsoft.hydralab.common.entity.center;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RolePermissionRelationIdTest {

    @Test
    public void testGetSetRoleId() {
        RolePermissionRelationId rolePermissionRelationId = new RolePermissionRelationId();
        rolePermissionRelationId.setRoleId("role1");
        assertEquals("role1", rolePermissionRelationId.getRoleId());
    }

    @Test
    public void testGetSetPermissionId() {
        RolePermissionRelationId rolePermissionRelationId = new RolePermissionRelationId();
        rolePermissionRelationId.setPermissionId("permission1");
        assertEquals("permission1", rolePermissionRelationId.getPermissionId());
    }
}
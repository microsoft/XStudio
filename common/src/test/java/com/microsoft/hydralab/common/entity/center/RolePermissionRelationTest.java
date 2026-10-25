package com.microsoft.hydralab.common.entity.center;

import org.junit.Test;
import static org.junit.Assert.*;

public class RolePermissionRelationTest {

    @Test
    public void testConstructor() {
        String roleId = "role1";
        String permissionId = "permission1";
        RolePermissionRelation rolePermissionRelation = new RolePermissionRelation(roleId, permissionId);

        assertEquals(roleId, rolePermissionRelation.getRoleId());
        assertEquals(permissionId, rolePermissionRelation.getPermissionId());
    }
}
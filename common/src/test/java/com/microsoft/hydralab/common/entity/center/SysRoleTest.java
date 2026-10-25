package com.microsoft.hydralab.common.entity.center;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SysRoleTest {

    @Test
    public void testGetAuthority() {
        // Arrange
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("admin");

        // Act
        String authority = sysRole.getAuthority();

        // Assert
        assertEquals("admin", authority);
    }
}
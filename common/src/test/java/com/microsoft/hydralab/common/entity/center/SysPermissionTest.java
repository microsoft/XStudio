package com.microsoft.hydralab.common.entity.center;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SysPermissionTest {

    @Test
    public void testGetAuthority() {
        // Create a SysPermission object
        SysPermission sysPermission = new SysPermission();
        sysPermission.setPermissionContent("testPermission");

        // Call the getAuthority() method
        String authority = sysPermission.getAuthority();

        // Assert that the returned authority is equal to the permission content
        assertEquals("testPermission", authority);
    }
}
package com.microsoft.hydralab.common.entity.center;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SysUserTest {

    @Test
    public void testGetAuthorities() {
        SysUser sysUser = new SysUser();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_ADMIN";
            }
        });
        sysUser.setAuthorities(authorities);

        Collection<? extends GrantedAuthority> result = sysUser.getAuthorities();

        Assert.assertEquals(authorities, result);
    }

    @Test
    public void testGetCredentials() {
        SysUser sysUser = new SysUser();

        Object result = sysUser.getCredentials();

        Assert.assertNull(result);
    }

    @Test
    public void testGetDetails() {
        SysUser sysUser = new SysUser();

        Object result = sysUser.getDetails();

        Assert.assertNull(result);
    }

    @Test
    public void testGetPrincipal() {
        SysUser sysUser = new SysUser();
        sysUser.setMailAddress("test@example.com");

        Object result = sysUser.getPrincipal();

        Assert.assertEquals("test@example.com", result);
    }

    @Test
    public void testIsAuthenticated() {
        SysUser sysUser = new SysUser();

        boolean result = sysUser.isAuthenticated();

        Assert.assertTrue(result);
    }

    @Test
    public void testSetAuthenticated() {
        SysUser sysUser = new SysUser();

        sysUser.setAuthenticated(true);

        // No assertion as the method does not have any logic
    }

    @Test
    public void testGetName() {
        SysUser sysUser = new SysUser();
        sysUser.setUserName("John Doe");

        String result = sysUser.getName();

        Assert.assertEquals("John Doe", result);
    }
}
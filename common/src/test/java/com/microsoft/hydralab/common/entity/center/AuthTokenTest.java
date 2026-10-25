package com.microsoft.hydralab.common.entity.center;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenTest {

    @Test
    public void testGetId() {
        AuthToken authToken = new AuthToken();
        authToken.setId(1L);
        assertEquals(1L, authToken.getId());
    }

    @Test
    public void testGetToken() {
        AuthToken authToken = new AuthToken();
        authToken.setToken("abc123");
        assertEquals("abc123", authToken.getToken());
    }

    @Test
    public void testGetCreator() {
        AuthToken authToken = new AuthToken();
        authToken.setCreator("John Doe");
        assertEquals("John Doe", authToken.getCreator());
    }
}
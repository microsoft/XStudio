package com.microsoft.hydralab.common.file.impl.local;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocalStoragePermissionTest {

    @Test
    public void testEnumValues() {
        LocalStoragePermission[] permissions = LocalStoragePermission.values();
        assertEquals(2, permissions.length);
        assertEquals(LocalStoragePermission.WRITE, permissions[0]);
        assertEquals(LocalStoragePermission.READ, permissions[1]);
    }
}
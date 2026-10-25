package com.microsoft.hydralab.common.entity.center;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SysTeamTest {

    @Test
    public void testGetTeamId() {
        SysTeam sysTeam = new SysTeam();
        assertNotNull(sysTeam.getTeamId());
    }

    @Test
    public void testGetTeamName() {
        SysTeam sysTeam = new SysTeam();
        sysTeam.setTeamName("Test Team");
        assertEquals("Test Team", sysTeam.getTeamName());
    }
}
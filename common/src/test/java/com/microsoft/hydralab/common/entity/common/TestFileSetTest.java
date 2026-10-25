package com.microsoft.hydralab.common.entity.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFileSetTest {

    @Test
    public void testConstructor() {
        TestFileSet testFileSet = new TestFileSet();
        assertNotNull(testFileSet.getId());
        assertNotNull(testFileSet.getIngestTime());
    }

    @Test
    public void testGettersAndSetters() {
        TestFileSet testFileSet = new TestFileSet();

        testFileSet.setBuildType("buildType");
        assertEquals("buildType", testFileSet.getBuildType());

        testFileSet.setRunningType("runningType");
        assertEquals("runningType", testFileSet.getRunningType());

        testFileSet.setAppName("appName");
        assertEquals("appName", testFileSet.getAppName());

        testFileSet.setPackageName("packageName");
        assertEquals("packageName", testFileSet.getPackageName());

        testFileSet.setVersion("version");
        assertEquals("version", testFileSet.getVersion());

        testFileSet.setCommitId("commitId");
        assertEquals("commitId", testFileSet.getCommitId());

        testFileSet.setCommitMessage("commitMessage");
        assertEquals("commitMessage", testFileSet.getCommitMessage());

        testFileSet.setCommitCount("commitCount");
        assertEquals("commitCount", testFileSet.getCommitCount());

        testFileSet.setTeamId("teamId");
        assertEquals("teamId", testFileSet.getTeamId());

        testFileSet.setTeamName("teamName");
        assertEquals("teamName", testFileSet.getTeamName());
    }
}
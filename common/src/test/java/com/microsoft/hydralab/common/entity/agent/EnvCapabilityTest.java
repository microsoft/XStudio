package com.microsoft.hydralab.common.entity.agent;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class EnvCapabilityTest {

    @Test
    public void testSetVersion() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.java;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        String version = "11.0.1";

        // Act
        envCapability.setVersion(version);

        // Assert
        Assert.assertEquals(version, envCapability.getVersion());
        Assert.assertEquals(11, envCapability.getMajorVersion());
        Assert.assertEquals(0, envCapability.getMinorVersion());
    }

    @Test
    public void testIsReady_WhenKeywordNotReady() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.ffmpeg;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);

        // Act
        boolean isReady = envCapability.isReady();

        // Assert
        Assert.assertFalse(isReady);
    }

    @Test
    public void testIsReady_WhenMajorVersionLessThanMinimumViableMajorVersion() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.python;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        envCapability.setVersion("2.7.0");

        // Act
        boolean isReady = envCapability.isReady();

        // Assert
        Assert.assertFalse(isReady);
    }

    @Test
    public void testIsReady_WhenMajorVersionEqualToMinimumViableMajorVersionAndMinorVersionLessThanMinimumViableMinorVersion() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.python;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        envCapability.setVersion("3.7.0");

        // Act
        boolean isReady = envCapability.isReady();

        // Assert
        Assert.assertFalse(isReady);
    }

    @Test
    public void testIsReady_WhenMajorVersionEqualToMinimumViableMajorVersionAndMinorVersionEqualToMinimumViableMinorVersion() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.python;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        envCapability.setVersion("3.8.0");

        // Act
        boolean isReady = envCapability.isReady();

        // Assert
        Assert.assertTrue(isReady);
    }

    @Test
    public void testMeet_WhenEnvCapabilityRequirementIsNull() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.java;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        EnvCapability envCapabilityRequirement = null;

        // Act
        boolean meetRequirement = envCapability.meet(envCapabilityRequirement);

        // Assert
        Assert.assertFalse(meetRequirement);
    }

    @Test
    public void testMeet_WhenKeywordNotMatch() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword1 = EnvCapability.CapabilityKeyword.java;
        File file1 = new File("path/to/file1");
        EnvCapability envCapability1 = new EnvCapability(keyword1, file1);
        EnvCapability.CapabilityKeyword keyword2 = EnvCapability.CapabilityKeyword.python;
        File file2 = new File("path/to/file2");
        EnvCapability envCapability2 = new EnvCapability(keyword2, file2);

        // Act
        boolean meetRequirement = envCapability1.meet(envCapability2);

        // Assert
        Assert.assertFalse(meetRequirement);
    }

    @Test
    public void testMeet_WhenNotReady() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.java;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        envCapability.setVersion("10.0.0");
        EnvCapability envCapabilityRequirement = new EnvCapability(keyword, 11, 0);

        // Act
        boolean meetRequirement = envCapability.meet(envCapabilityRequirement);

        // Assert
        Assert.assertFalse(meetRequirement);
    }

    @Test
    public void testMeet_WhenMajorVersionLessThanRequirementMajorVersion() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.java;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        envCapability.setVersion("11.0.0");
        EnvCapability envCapabilityRequirement = new EnvCapability(keyword, 12, 0);

        // Act
        boolean meetRequirement = envCapability.meet(envCapabilityRequirement);

        // Assert
        Assert.assertFalse(meetRequirement);
    }

    @Test
    public void testMeet_WhenMajorVersionEqualToRequirementMajorVersionAndMinorVersionLessThanRequirementMinorVersion() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.java;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        envCapability.setVersion("11.0.0");
        EnvCapability envCapabilityRequirement = new EnvCapability(keyword, 11, 1);

        // Act
        boolean meetRequirement = envCapability.meet(envCapabilityRequirement);

        // Assert
        Assert.assertFalse(meetRequirement);
    }

    @Test
    public void testMeet_WhenMajorVersionEqualToRequirementMajorVersionAndMinorVersionEqualToRequirementMinorVersion() {
        // Arrange
        EnvCapability.CapabilityKeyword keyword = EnvCapability.CapabilityKeyword.java;
        File file = new File("path/to/file");
        EnvCapability envCapability = new EnvCapability(keyword, file);
        envCapability.setVersion("11.0.0");
        EnvCapability envCapabilityRequirement = new EnvCapability(keyword, 11, 0);

        // Act
        boolean meetRequirement = envCapability.meet(envCapabilityRequirement);

        // Assert
        Assert.assertTrue(meetRequirement);
    }
}
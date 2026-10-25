package com.microsoft.hydralab.common.file;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StoragePropertiesTest {

    @Test
    public void testGetScreenshotContainerName() {
        StorageProperties storageProperties = new StorageProperties() {
            // Implementing the abstract class here
        };
        String expected = "images";
        String actual = storageProperties.getScreenshotContainerName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAppFileContainerName() {
        StorageProperties storageProperties = new StorageProperties() {
            // Implementing the abstract class here
        };
        String expected = "pkgstore";
        String actual = storageProperties.getAppFileContainerName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTestResultContainerName() {
        StorageProperties storageProperties = new StorageProperties() {
            // Implementing the abstract class here
        };
        String expected = "testresults";
        String actual = storageProperties.getTestResultContainerName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgentPackageContainerName() {
        StorageProperties storageProperties = new StorageProperties() {
            // Implementing the abstract class here
        };
        String expected = "pkgstore";
        String actual = storageProperties.getAgentPackageContainerName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTestJsonContainerName() {
        StorageProperties storageProperties = new StorageProperties() {
            // Implementing the abstract class here
        };
        String expected = "testjson";
        String actual = storageProperties.getTestJsonContainerName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTestSuiteContainerName() {
        StorageProperties storageProperties = new StorageProperties() {
            // Implementing the abstract class here
        };
        String expected = "testsuitestore";
        String actual = storageProperties.getTestSuiteContainerName();
        assertEquals(expected, actual);
    }
}
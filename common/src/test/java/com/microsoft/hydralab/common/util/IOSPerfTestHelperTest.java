package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class IOSPerfTestHelperTest {

    @Test
    public void testAdd() throws IOException {
        IOSPerfTestHelper helper = IOSPerfTestHelper.getInstance();
        String key = "testKey";
        File resultFile = new File("result.txt");
        Process process = new ProcessBuilder().command("command").start();

        helper.add(key, resultFile, process);

        Assert.assertTrue(helper.isRunning(key));
    }

    @Test
    public void testIsRunning() throws IOException {
        IOSPerfTestHelper helper = IOSPerfTestHelper.getInstance();
        String key = "testKey";
        File resultFile = new File("result.txt");
        Process process = new ProcessBuilder().command("command").start();

        helper.add(key, resultFile, process);

        Assert.assertTrue(helper.isRunning(key));
    }

    @Test
    public void testGetResultFile() throws IOException {
        IOSPerfTestHelper helper = IOSPerfTestHelper.getInstance();
        String key = "testKey";
        File resultFile = new File("result.txt");
        Process process = new ProcessBuilder().command("command").start();

        helper.add(key, resultFile, process);

        File retrievedFile = helper.getResultFile(key);

        Assert.assertEquals(resultFile, retrievedFile);
    }

    @Test
    public void testGetStartTime() throws IOException {
        IOSPerfTestHelper helper = IOSPerfTestHelper.getInstance();
        String key = "testKey";
        File resultFile = new File("result.txt");
        Process process = new ProcessBuilder().command("command").start();

        helper.add(key, resultFile, process);

        long startTime = helper.getStartTime(key);

        Assert.assertNotEquals(0, startTime);
    }

    @Test
    public void testStop() throws IOException {
        IOSPerfTestHelper helper = IOSPerfTestHelper.getInstance();
        String key = "testKey";
        File resultFile = new File("result.txt");
        Process process = new ProcessBuilder().command("command").start();

        helper.add(key, resultFile, process);

        helper.stop(key);

        Assert.assertFalse(helper.isRunning(key));
    }
}
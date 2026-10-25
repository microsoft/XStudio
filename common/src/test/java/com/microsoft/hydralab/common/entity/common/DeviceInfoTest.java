package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DeviceInfoTest {

    @Test
    public void testSetStatus() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setStatus("ONLINE");
        Assert.assertEquals("ONLINE", deviceInfo.getStatus());
    }

    @Test
    public void testGetCurrentCommandStr() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.addCurrentCommand("command1");
        deviceInfo.addCurrentCommand("command2");
        String expected = Thread.currentThread().getName() + ":\ncommand1\n" +
                Thread.currentThread().getName() + ":\ncommand2\n";
        Assert.assertEquals(expected, deviceInfo.getCurrentCommandStr());
    }

    @Test
    public void testAddCurrentCommand() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.addCurrentCommand("command1");
        Assert.assertEquals("command1", deviceInfo.getCurrentCommand().get(Thread.currentThread()));
    }

    @Test
    public void testFinishCommand() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.addCurrentCommand("command1");
        deviceInfo.finishCommand();
        Assert.assertNull(deviceInfo.getCurrentCommand().get(Thread.currentThread()));
        Assert.assertNull(deviceInfo.getCurrentProcess().get(Thread.currentThread()));
    }

    @Test
    public void testAddCurrentProcess() {
        DeviceInfo deviceInfo = new DeviceInfo();
        Process process = null;
        try {
            process = new ProcessBuilder().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deviceInfo.addCurrentProcess(process);
        Assert.assertEquals(process, deviceInfo.getCurrentProcess().get(Thread.currentThread()));
    }

    @Test
    public void testAddCurrentTask() {
        DeviceInfo deviceInfo = new DeviceInfo();
        TestTask testTask = new TestTask();
        deviceInfo.addCurrentTask(testTask);
        Assert.assertEquals(DeviceInfo.TESTING, deviceInfo.getStatus());
        Assert.assertEquals(testTask.getId(), deviceInfo.getRunningTaskId());
        Assert.assertEquals(testTask.getPkgName(), deviceInfo.getRunningTaskPackageName());
    }

    @Test
    public void testFinishTask() {
        DeviceInfo deviceInfo = new DeviceInfo();
        TestTask testTask = new TestTask();
        deviceInfo.addCurrentTask(testTask);
        deviceInfo.finishTask();
        Assert.assertNull(deviceInfo.getCurrentTask().get(Thread.currentThread()));
        Assert.assertEquals(DeviceInfo.ONLINE, deviceInfo.getStatus());
        Assert.assertNull(deviceInfo.getRunningTaskId());
        Assert.assertNull(deviceInfo.getRunningTaskPackageName());
    }

    @Test
    public void testReset() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setStatus(DeviceInfo.TESTING);
        deviceInfo.setRunningTaskId("task1");
        deviceInfo.setRunningTaskPackageName("com.example");
        deviceInfo.reset();
        Assert.assertEquals(DeviceInfo.ONLINE, deviceInfo.getStatus());
        Assert.assertNull(deviceInfo.getRunningTaskId());
        Assert.assertNull(deviceInfo.getRunningTaskPackageName());
    }

    @Test
    public void testIsAlive() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setStatus(DeviceInfo.TESTING);
        Assert.assertTrue(deviceInfo.isAlive());
        deviceInfo.setStatus(DeviceInfo.ONLINE);
        Assert.assertTrue(deviceInfo.isAlive());
        deviceInfo.setStatus(DeviceInfo.UNSTABLE);
        Assert.assertTrue(deviceInfo.isAlive());
        deviceInfo.setStatus(DeviceInfo.OFFLINE);
        Assert.assertFalse(deviceInfo.isAlive());
    }

    @Test
    public void testIsTesting() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setStatus(DeviceInfo.TESTING);
        Assert.assertTrue(deviceInfo.isTesting());
        deviceInfo.setStatus(DeviceInfo.ONLINE);
        Assert.assertFalse(deviceInfo.isTesting());
    }

    @Test
    public void testIsOnline() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setStatus(DeviceInfo.ONLINE);
        Assert.assertTrue(deviceInfo.isOnline());
        deviceInfo.setStatus(DeviceInfo.TESTING);
        Assert.assertFalse(deviceInfo.isOnline());
    }

    @Test
    public void testIsOffline() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setStatus(DeviceInfo.OFFLINE);
        Assert.assertTrue(deviceInfo.isOffline());
        deviceInfo.setStatus(DeviceInfo.TESTING);
        Assert.assertFalse(deviceInfo.isOffline());
    }

    @Test
    public void testIsUnstable() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setStatus(DeviceInfo.UNSTABLE);
        Assert.assertTrue(deviceInfo.isUnstable());
        deviceInfo.setStatus(DeviceInfo.TESTING);
        Assert.assertFalse(deviceInfo.isUnstable());
    }

    @Test
    public void testKillAll() {
        DeviceInfo deviceInfo = new DeviceInfo();
        Process process = null;
        try {
            process = new ProcessBuilder().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deviceInfo.addCurrentProcess(process);
        deviceInfo.addCurrentCommand("command1");
        deviceInfo.addCurrentTask(new TestTask());
        deviceInfo.killAll();
        Assert.assertNull(deviceInfo.getCurrentProcess().get(Thread.currentThread()));
        Assert.assertNull(deviceInfo.getCurrentCommand().get(Thread.currentThread()));
        Assert.assertNull(deviceInfo.getCurrentTask().get(Thread.currentThread()));
    }
}
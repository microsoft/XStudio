package com.microsoft.hydralab.performance.entity;

import com.microsoft.hydralab.performance.hprof.ObjectInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AndroidHprofMemoryInfoTest {

    @Test
    public void testGetBitmapInfoList() {
        // Create a sample list of ObjectInfo objects
        List<ObjectInfo> bitmapInfoList = new ArrayList<>();
        bitmapInfoList.add(new ObjectInfo());
        bitmapInfoList.add(new ObjectInfo());

        // Create an instance of AndroidHprofMemoryInfo
        AndroidHprofMemoryInfo memoryInfo = new AndroidHprofMemoryInfo();
        memoryInfo.setBitmapInfoList(bitmapInfoList);

        // Verify that the getBitmapInfoList() method returns the same list
        Assert.assertEquals(bitmapInfoList, memoryInfo.getBitmapInfoList());
    }

    @Test
    public void testGetTopObjectList() {
        // Create a sample list of ObjectInfo objects
        List<ObjectInfo> topObjectList = new ArrayList<>();
        topObjectList.add(new ObjectInfo());
        topObjectList.add(new ObjectInfo());

        // Create an instance of AndroidHprofMemoryInfo
        AndroidHprofMemoryInfo memoryInfo = new AndroidHprofMemoryInfo();
        memoryInfo.setTopObjectList(topObjectList);

        // Verify that the getTopObjectList() method returns the same list
        Assert.assertEquals(topObjectList, memoryInfo.getTopObjectList());
    }

    @Test
    public void testGetAppPackageName() {
        // Create a sample app package name
        String appPackageName = "com.example.app";

        // Create an instance of AndroidHprofMemoryInfo
        AndroidHprofMemoryInfo memoryInfo = new AndroidHprofMemoryInfo();
        memoryInfo.setAppPackageName(appPackageName);

        // Verify that the getAppPackageName() method returns the same package name
        Assert.assertEquals(appPackageName, memoryInfo.getAppPackageName());
    }

    @Test
    public void testGetTimeStamp() {
        // Create a sample timestamp
        long timeStamp = System.currentTimeMillis();

        // Create an instance of AndroidHprofMemoryInfo
        AndroidHprofMemoryInfo memoryInfo = new AndroidHprofMemoryInfo();
        memoryInfo.setTimeStamp(timeStamp);

        // Verify that the getTimeStamp() method returns the same timestamp
        Assert.assertEquals(timeStamp, memoryInfo.getTimeStamp());
    }

    @Test
    public void testGetDescription() {
        // Create a sample description
        String description = "Sample description";

        // Create an instance of AndroidHprofMemoryInfo
        AndroidHprofMemoryInfo memoryInfo = new AndroidHprofMemoryInfo();
        memoryInfo.setDescription(description);

        // Verify that the getDescription() method returns the same description
        Assert.assertEquals(description, memoryInfo.getDescription());
    }
}
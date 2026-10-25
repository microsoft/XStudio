package com.microsoft.hydralab.performance.hprof;

import org.junit.Assert;
import org.junit.Test;

public class BitmapInfoTest {

    @Test
    public void testComputePerPixelSize() {
        BitmapInfo bitmapInfo = new BitmapInfo();
        bitmapInfo.width = 10;
        bitmapInfo.height = 5;
        bitmapInfo.nativeSize = 100;

        bitmapInfo.computePerPixelSize();

        Assert.assertEquals(2.0f, bitmapInfo.perPixelSize, 0.001);
    }

    @Test
    public void testGetSizeInfo() {
        BitmapInfo bitmapInfo = new BitmapInfo();
        bitmapInfo.width = 10;
        bitmapInfo.height = 5;
        bitmapInfo.nativeSize = 100;

        String expectedSizeInfo = "ObjectSize: 100, BitmapSize: 10x5";
        String actualSizeInfo = bitmapInfo.getSizeInfo();

        Assert.assertEquals(expectedSizeInfo, actualSizeInfo);
    }
}
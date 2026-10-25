package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ThreadUtilsTest {

    @Test
    public void testDoInParallel() {
        List<Integer> dataList = new ArrayList<>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
        dataList.add(4);
        dataList.add(5);

        ThreadUtils.doInParallel(dataList, 2, new ThreadUtils.ParallelTask<Integer>() {
            @Override
            public void processOne(Integer item, int innerIndex) throws Exception {
                System.out.println("Processing item: " + item + " at index: " + innerIndex);
            }

            @Override
            public void onError(Integer item, Exception e) {
                System.out.println("Error processing item: " + item);
                e.printStackTrace();
            }
        });

        // Add assertions here to verify the expected behavior of the method
        // For example, check if all items were processed correctly
        // Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSafeSleep() {
        long startTime = System.currentTimeMillis();
        ThreadUtils.safeSleep(1000);
        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;
        Assert.assertTrue(elapsedTime >= 1000);
    }
}
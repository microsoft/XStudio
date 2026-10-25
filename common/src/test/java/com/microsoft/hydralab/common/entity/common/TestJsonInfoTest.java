package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJsonInfoTest {

    @Test
    public void testGetDisplayIngestTime() {
        // Create a TestJsonInfo object
        TestJsonInfo testJsonInfo = new TestJsonInfo();

        // Get the current date and format it using the same format as the formatDate field
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expectedDisplayIngestTime = formatDate.format(new Date());

        // Call the getDisplayIngestTime method
        String actualDisplayIngestTime = testJsonInfo.getDisplayIngestTime();

        // Assert that the actual display ingest time is equal to the expected display ingest time
        Assert.assertEquals(expectedDisplayIngestTime, actualDisplayIngestTime);
    }
}
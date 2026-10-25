package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtilsTest {

    @Test
    public void testGetTimestampForFilename() {
        // Arrange
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss",
                Locale.getDefault());
        TimeZone gmt = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(gmt);
        dateFormat.setLenient(true);
        String expectedTimestamp = dateFormat.format(new Date());

        // Act
        String actualTimestamp = TimeUtils.getTimestampForFilename();

        // Assert
        Assert.assertEquals(expectedTimestamp, actualTimestamp);
    }
}
package com.microsoft.hydralab.common.util;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CommandOutputReceiverTest {

    private Logger logger;
    private InputStream inputStream;

    @Before
    public void setup() {
        logger = mock(Logger.class);
        inputStream = new ByteArrayInputStream(("Line 1\nLine 2\nLine 3\n").getBytes());
    }

    @Test
    public void testRun_withLogger_shouldLogLines() throws Exception {
        // Arrange
        String line1 = "Line 1";
        String line2 = "Line 2";
        String line3 = "Line 3";
        String expectedLog = line1 + "\n" + line2 + "\n" + line3 + "\n";

        CommandOutputReceiver commandOutputReceiver = new CommandOutputReceiver(inputStream, logger);

        // Act
        commandOutputReceiver.run();

        // Assert
        verify(logger, times(3)).info(anyString());
        verify(logger).info(line1);
        verify(logger).info(line2);
        verify(logger).info(line3);
        verify(logger, never()).error(anyString());
        verify(logger, never()).warn(anyString());
        verify(logger, never()).debug(anyString());
        verify(logger, never()).trace(anyString());
    }

    @Test
    public void testRun_withoutLogger_shouldPrintLines() throws Exception {
        // Arrange
        String line1 = "Line 1";
        String line2 = "Line 2";
        String line3 = "Line 3";
        String expectedOutput = line1 + "\n" + line2 + "\n" + line3 + "\n";

        CommandOutputReceiver commandOutputReceiver = new CommandOutputReceiver(inputStream, null);

        // Act
        commandOutputReceiver.run();

        // Assert
        assertEquals(expectedOutput, System.out.toString());
    }
}
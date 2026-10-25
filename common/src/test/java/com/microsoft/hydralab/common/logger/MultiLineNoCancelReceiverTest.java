package com.microsoft.hydralab.common.logger;

import com.android.ddmlib.MultiLineReceiver;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class MultiLineNoCancelReceiverTest {

    @Test
    public void testIsCancelled() {
        MultiLineNoCancelReceiver receiver = new MultiLineNoCancelReceiver() {
            @Override
            public void processNewLines(String[] lines) {
                // do nothing
            }
        };

        assertFalse(receiver.isCancelled());
    }
}
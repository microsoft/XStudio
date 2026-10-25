package com.microsoft.hydralab.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HydraLabRuntimeExceptionTest {

    @Test
    public void testGetCode() {
        HydraLabRuntimeException exception = new HydraLabRuntimeException(404, "Not Found");
        int code = exception.getCode();
        assertEquals(404, code);
    }
}
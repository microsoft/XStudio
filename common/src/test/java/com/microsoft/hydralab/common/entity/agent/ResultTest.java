package com.microsoft.hydralab.common.entity.agent;

import org.junit.Assert;
import org.junit.Test;

public class ResultTest {

    @Test
    public void testOk() {
        Result<Object> result = Result.ok();
        Assert.assertEquals(200, result.getCode());
        Assert.assertEquals("OK!", result.getMessage());
        Assert.assertNull(result.getContent());
    }

    @Test
    public void testOkWithContent() {
        String content = "Test Content";
        Result<String> result = Result.ok(content);
        Assert.assertEquals(200, result.getCode());
        Assert.assertEquals("OK!", result.getMessage());
        Assert.assertEquals(content, result.getContent());
    }

    @Test
    public void testErrorWithCode() {
        int code = 500;
        Result<Object> result = Result.error(code);
        Assert.assertEquals(code, result.getCode());
        Assert.assertEquals("error", result.getMessage());
        Assert.assertNull(result.getContent());
    }

    @Test
    public void testErrorWithCodeAndMessage() {
        int code = 500;
        String message = "Internal Server Error";
        Result<Object> result = Result.error(code, message);
        Assert.assertEquals(code, result.getCode());
        Assert.assertEquals(message, result.getMessage());
        Assert.assertNull(result.getContent());
    }

    @Test
    public void testErrorWithCodeAndThrowable() {
        int code = 500;
        Throwable throwable = new NullPointerException("Null Pointer Exception");
        Result<Object> result = Result.error(code, throwable);
        Assert.assertEquals(code, result.getCode());
        Assert.assertEquals(throwable.getClass().getName() + ": " + throwable.getMessage() + "\n" + throwable.getStackTrace(), result.getMessage());
        Assert.assertNull(result.getContent());
    }
}
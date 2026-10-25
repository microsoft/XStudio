package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageTest {

    @Test
    public void testOk() {
        String path = "/test";
        Object body = new Object();

        Message message = Message.ok(path, body);

        Assert.assertNotNull(message);
        Assert.assertEquals(Message.HttpMethod.GET.toString(), message.getMethod());
        Assert.assertNotNull(message.getSessionId());
        Assert.assertEquals(path, message.getPath());
        Assert.assertEquals(200, message.getCode());
        Assert.assertEquals(body, message.getBody());
    }

    @Test
    public void testSetBody() {
        Object body = new Object();

        Message message = new Message();
        message.setBody(body);

        Assert.assertEquals(body, message.getBody());
        Assert.assertEquals(body.getClass().getName(), message.getBodyType());
    }

    @Test
    public void testError() {
        Message source = new Message();
        source.setSessionId(UUID.randomUUID().toString());
        source.setPath("/test");

        int code = 400;
        String msg = "Error message";

        Message message = Message.error(source, code, msg);

        Assert.assertNotNull(message);
        Assert.assertEquals(source.getSessionId(), message.getSessionId());
        Assert.assertEquals(source.getPath(), message.getPath());
        Assert.assertEquals(code, message.getCode());
        Assert.assertEquals(msg, message.getMessage());
    }

    @Test
    public void testResponse() {
        Message source = new Message();
        source.setSessionId(UUID.randomUUID().toString());
        source.setPath("/test");
        Object body = new Object();

        Message message = Message.response(source, body);

        Assert.assertNotNull(message);
        Assert.assertEquals(source.getSessionId(), message.getSessionId());
        Assert.assertEquals(source.getPath(), message.getPath());
        Assert.assertEquals(body, message.getBody());
    }

    @Test
    public void testAuth() {
        Message message = Message.auth();

        Assert.assertNotNull(message);
        Assert.assertNotNull(message.getSessionId());
        Assert.assertEquals(Message.HttpMethod.GET.toString(), message.getMethod());
        Assert.assertEquals("/auth", message.getPath());
    }
}
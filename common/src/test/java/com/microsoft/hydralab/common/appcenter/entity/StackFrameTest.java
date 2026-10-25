package com.microsoft.hydralab.common.appcenter.entity;

import org.junit.Assert;
import org.junit.Test;

public class StackFrameTest {

    @Test
    public void testGetClassName() {
        StackFrame stackFrame = new StackFrame();
        stackFrame.setClassName("com.example.TestClass");
        Assert.assertEquals("com.example.TestClass", stackFrame.getClassName());
    }

    @Test
    public void testGetMethodName() {
        StackFrame stackFrame = new StackFrame();
        stackFrame.setMethodName("testMethod");
        Assert.assertEquals("testMethod", stackFrame.getMethodName());
    }

    @Test
    public void testGetLineNumber() {
        StackFrame stackFrame = new StackFrame();
        stackFrame.setLineNumber(10);
        Assert.assertEquals(Integer.valueOf(10), stackFrame.getLineNumber());
    }

    @Test
    public void testGetFileName() {
        StackFrame stackFrame = new StackFrame();
        stackFrame.setFileName("TestFile.java");
        Assert.assertEquals("TestFile.java", stackFrame.getFileName());
    }
}
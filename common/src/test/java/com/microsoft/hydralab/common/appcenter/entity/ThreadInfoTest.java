package com.microsoft.hydralab.common.appcenter.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadInfoTest {

    @Test
    public void testGetId() {
        ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setId(12345);
        assertEquals(12345, threadInfo.getId());
    }

    @Test
    public void testGetName() {
        ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setName("Thread 1");
        assertEquals("Thread 1", threadInfo.getName());
    }

    @Test
    public void testGetFrames() {
        ThreadInfo threadInfo = new ThreadInfo();
        StackFrame stackFrame1 = new StackFrame();
        StackFrame stackFrame2 = new StackFrame();
        threadInfo.getFrames().add(stackFrame1);
        threadInfo.getFrames().add(stackFrame2);
        assertEquals(2, threadInfo.getFrames().size());
        assertTrue(threadInfo.getFrames().contains(stackFrame1));
        assertTrue(threadInfo.getFrames().contains(stackFrame2));
    }
}
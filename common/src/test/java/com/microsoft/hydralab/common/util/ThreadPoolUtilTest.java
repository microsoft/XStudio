package com.microsoft.hydralab.common.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ThreadPoolUtilTest {

    @Test
    public void testNewThreadPoolExecutor() {
        Executor executor = ThreadPoolUtil.newThreadPoolExecutor(10, 60L, "TestThread");
        assertNotNull(executor);
    }

    @Test
    public void testHydraThreadFactory() {
        ThreadPoolUtil.HydraThreadFactory threadFactory = new ThreadPoolUtil.HydraThreadFactory(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // Handle uncaught exception
            }
        }, "TestThread");

        Thread thread = threadFactory.newThread(new Runnable() {
            @Override
            public void run() {
                // Thread logic
            }
        });

        assertNotNull(thread);
        assertEquals("TestThread1", thread.getName());
        assertEquals(false, thread.isDaemon());
    }

    @Test
    public void testTimerExecutor() {
        ScheduledExecutorService timerExecutor = ThreadPoolUtil.TIMER_EXECUTOR;
        assertNotNull(timerExecutor);
    }

    @Test
    public void testPerformanceTestTimerExecutor() {
        ScheduledExecutorService performanceTestTimerExecutor = ThreadPoolUtil.PERFORMANCE_TEST_TIMER_EXECUTOR;
        assertNotNull(performanceTestTimerExecutor);
    }

    @Test
    public void testScreenshotExecutor() {
        Executor screenshotExecutor = ThreadPoolUtil.SCREENSHOT_EXECUTOR;
        assertNotNull(screenshotExecutor);
    }

    @Test
    public void testTestExecutor() {
        Executor testExecutor = ThreadPoolUtil.TEST_EXECUTOR;
        assertNotNull(testExecutor);
    }
}
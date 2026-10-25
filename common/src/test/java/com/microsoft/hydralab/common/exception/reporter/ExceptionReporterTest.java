package com.microsoft.hydralab.common.exception.reporter;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class ExceptionReporterTest {

    @Test
    public void testReportException() {
        ExceptionReporter exceptionReporter = mock(ExceptionReporter.class);
        Exception exception = new Exception();
        boolean fatal = true;

        exceptionReporter.reportException(exception, fatal);

        verify(exceptionReporter).reportException(exception, fatal);
    }

    @Test
    public void testReportExceptionWithThread() {
        ExceptionReporter exceptionReporter = mock(ExceptionReporter.class);
        Exception exception = new Exception();
        Thread thread = new Thread();
        boolean fatal = true;

        exceptionReporter.reportException(exception, thread, fatal);

        verify(exceptionReporter).reportException(exception, thread, fatal);
    }
}
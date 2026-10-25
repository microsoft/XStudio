package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;

public class ShellUtilsTest {

    private Logger classLogger = null; // Replace with actual logger instance

    @Test
    public void testExecLocalCommand() {
        String command = "echo Hello World";

        Process process = ShellUtils.execLocalCommand(command, classLogger);

        Assert.assertNotNull(process);
    }

    @Test
    public void testExecLocalCommandWithRedirect() {
        String command = "echo Hello World";
        File redirectTo = new File("output.txt");

        Process process = ShellUtils.execLocalCommandWithRedirect(command, redirectTo, true, classLogger);

        Assert.assertNotNull(process);
        Assert.assertTrue(redirectTo.exists());
    }

    @Test
    public void testExecLocalCommandWithResult() {
        String command = "echo Hello World";

        String result = ShellUtils.execLocalCommandWithResult(command, classLogger);

        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testKillProcessByCommandStr() {
        String commandStr = "java -jar myapp.jar";

        ShellUtils.killProcessByCommandStr(commandStr, classLogger);

        // Assert that the process is killed
    }
}
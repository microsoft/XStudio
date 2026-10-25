package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class AgentUpdateTaskTest {

    @Test
    public void testUpdateMsgConstructor() {
        Boolean isProceed = true;
        String message = "Update message";
        String errorDesc = "Error description";

        AgentUpdateTask.UpdateMsg updateMsg = new AgentUpdateTask.UpdateMsg(isProceed, message, errorDesc);

        Assert.assertEquals(isProceed, updateMsg.isProceed);
        Assert.assertEquals(message, updateMsg.message);
        Assert.assertEquals(errorDesc, updateMsg.errorDesc);
        Assert.assertNotNull(updateMsg.recordTime);
        Assert.assertTrue(updateMsg.recordTime instanceof Date);
    }
}
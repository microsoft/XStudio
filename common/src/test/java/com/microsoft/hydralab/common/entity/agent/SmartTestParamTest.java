package com.microsoft.hydralab.common.entity.agent;

import com.microsoft.hydralab.common.entity.common.DeviceInfo;
import com.microsoft.hydralab.common.util.Const;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class SmartTestParamTest {

    @Test
    public void testConstructor() {
        // Create test data
        String apkPath = "test.apk";
        DeviceInfo deviceInfo = new DeviceInfo();
        String sourceModelId = "sourceModel";
        String targetModelId = "targetModel";
        int testSteps = 10;
        String folderPath = "testFolder/";
        String stringFolderPath = "stringFolder/";
        File outputFolder = new File("output/");
        LLMProperties llmProperties = new LLMProperties();

        // Create expected modelInfo JSON
        String expectedModelInfo = "{\"bertPath\":\"testFolder/bert_model\",\"topicPath\":\"testFolder/topic_model\",\"sourceModel\":\"sourceModel\",\"targetModel\":\"targetModel\"}";

        // Create expected llmInfo JSON
        String expectedLlmInfo = "{\"llmEnable\":false,\"llmDeployment\":\"\",\"llmApiKey\":\"\",\"llmApiBase\":\"\",\"llmApiVersion\":\"\"}";

        // Create instance of SmartTestParam
        SmartTestParam smartTestParam = new SmartTestParam(apkPath, deviceInfo, sourceModelId, targetModelId, testSteps, folderPath, stringFolderPath, outputFolder, llmProperties);

        // Assert the values are set correctly
        Assert.assertEquals(apkPath, smartTestParam.getApkPath());
        Assert.assertEquals(deviceInfo, smartTestParam.getDeviceInfo());
        Assert.assertEquals(expectedModelInfo, smartTestParam.getModelInfo());
        Assert.assertEquals(String.valueOf(testSteps), smartTestParam.getTestSteps());
        Assert.assertEquals(stringFolderPath, smartTestParam.getStringTextFolder());
        Assert.assertEquals(outputFolder, smartTestParam.getOutputFolder());
        Assert.assertEquals(expectedLlmInfo, smartTestParam.getLlmInfo());
    }
}
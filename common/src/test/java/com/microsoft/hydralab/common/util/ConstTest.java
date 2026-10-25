package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ConstTest {

    @Test
    public void testPath() {
        Assert.assertEquals("/auth", Const.Path.AUTH);
        Assert.assertEquals("/agentInit", Const.Path.AGENT_INIT);
        Assert.assertEquals("/heartbeat", Const.Path.HEARTBEAT);
        Assert.assertEquals("/api/device/list", Const.Path.DEVICE_LIST);
        Assert.assertEquals("/api/device/update", Const.Path.DEVICE_UPDATE);
        Assert.assertEquals("/api/device/status", Const.Path.DEVICE_STATUS);
        Assert.assertEquals("/api/device/access", Const.Path.ACCESS_INFO);
        Assert.assertEquals("/api/test/task/run", Const.Path.TEST_TASK_RUN);
        Assert.assertEquals("/api/test/task/update", Const.Path.TEST_TASK_UPDATE);
        Assert.assertEquals("/api/test/task/cancel", Const.Path.TEST_TASK_CANCEL);
        Assert.assertEquals("/api/test/task/retry", Const.Path.TEST_TASK_RETRY);
        Assert.assertEquals("/api/agent/update", Const.Path.AGENT_UPDATE);
        Assert.assertEquals("/api/agent/restart", Const.Path.AGENT_RESTART);
        Assert.assertEquals("static/dist/images/default_user.png", Const.Path.DEFAULT_PHOTO);
    }

    @Test
    public void testDeviceGroup() {
        Assert.assertEquals("CI", Const.DeviceGroup.CI);
        Assert.assertEquals("REGULAR", Const.DeviceGroup.REGULAR);
        Assert.assertEquals("G.", Const.DeviceGroup.GROUP_NAME_PREFIX);
        Assert.assertEquals("SYS", Const.DeviceGroup.SYS_GROUP);
        Assert.assertEquals("USER", Const.DeviceGroup.USER_GROUP);
        Assert.assertEquals("SINGLE", Const.DeviceGroup.SINGLE_TYPE);
        Assert.assertEquals("REST", Const.DeviceGroup.REST_TYPE);
        Assert.assertEquals("ALL", Const.DeviceGroup.ALL_TYPE);
    }

    @Test
    public void testAgentConfig() {
        Assert.assertEquals(5, Const.AgentConfig.RETRY_TIME);
        Assert.assertEquals(15, Const.AgentConfig.PHOTO_UPDATE_SEC);
        Assert.assertEquals("testTaskId", Const.AgentConfig.TASK_ID_PARAM);
        Assert.assertEquals("serialNum", Const.AgentConfig.SERIAL_PARAM);
        Assert.assertEquals("status", Const.AgentConfig.STATUS_PARAM);
        Assert.assertEquals("isPrivate", Const.AgentConfig.SCOPE_PARAM);
        Assert.assertEquals("restartAgent.sh", Const.AgentConfig.RESTART_FILE_MAC);
        Assert.assertEquals("restartAgent.bat", Const.AgentConfig.RESTART_FILE_WIN);
    }

    @Test
    public void testTaskResult() {
        Assert.assertEquals("DEVICE_OFFLINE", Const.TaskResult.ERROR_DEVICE_OFFLINE);
        Assert.assertEquals("SUCCESS", Const.TaskResult.SUCCESS);
    }

    @Test
    public void testParam() {
        Assert.assertEquals("devices", Const.Param.TEST_DEVICE_SN);
        Assert.assertEquals("groups", Const.Param.GROUP);
        Assert.assertEquals("agents", Const.Param.AGENT);
        Assert.assertEquals("testTaskId", Const.Param.TEST_TASK_ID);
    }

    @Test
    public void testSmartTestConfig() {
        Assert.assertEquals("success", Const.SmartTestConfig.SUCCESS_TAG);
        Assert.assertEquals("exception", Const.SmartTestConfig.TASK_EXP_TAG);
        Assert.assertEquals("appException", Const.SmartTestConfig.APP_EXP_TAG);
        Assert.assertEquals("coverage", Const.SmartTestConfig.COVERAGE_TAG);
        Assert.assertEquals("visited", Const.SmartTestConfig.VISIT_TAG);
        Assert.assertEquals("path_to_screen_bert_model", Const.SmartTestConfig.BERT_PATH_TAG);
        Assert.assertEquals("path_to_screen_topic_classifier_model", Const.SmartTestConfig.TOPIC_PATH_TAG);
        Assert.assertEquals("source_model_id", Const.SmartTestConfig.SOURCE_MODEL_TAG);
        Assert.assertEquals("target_model_id", Const.SmartTestConfig.TARGET_MODEL_TAG);
        Assert.assertEquals("SmartTest.zip", Const.SmartTestConfig.ZIP_FILE_NAME);
        Assert.assertEquals("SmartTest", Const.SmartTestConfig.ZIP_FOLDER_NAME);
        Assert.assertEquals("smartTestResult", Const.SmartTestConfig.RESULT_FOLDER_NAME);
        Assert.assertEquals("directed_acyclic_graph.gexf", Const.SmartTestConfig.GRAPH_FILE_NAME);
        Assert.assertEquals("main.py", Const.SmartTestConfig.PY_FILE_NAME);
        Assert.assertEquals("screenBert.pt", Const.SmartTestConfig.BERT_MODEL_NAME);
        Assert.assertEquals("topic.pt", Const.SmartTestConfig.TOPIC_MODEL_NAME);
        Assert.assertEquals("requirements.txt", Const.SmartTestConfig.REQUIRE_FILE_NAME);
        Assert.assertEquals("SmartTestString", Const.SmartTestConfig.STRING_FOLDER_NAME);
        Assert.assertEquals("strings,username,password", Const.SmartTestConfig.STRING_FILE_NAMES);
        Assert.assertEquals("enable_llm", Const.SmartTestConfig.LLM_ENABLE);
        Assert.assertEquals("deployment_name", Const.SmartTestConfig.LLM_DEPLOYMENT);
        Assert.assertEquals("openai_api_key", Const.SmartTestConfig.LLM_API_KEY);
        Assert.assertEquals("openai_api_base", Const.SmartTestConfig.LLM_API_BASE);
        Assert.assertEquals("openai_api_version", Const.SmartTestConfig.LLM_API_VERSION);
    }

    @Test
    public void testScreenRecoderConfig() {
        Assert.assertEquals("merged_test.mp4", Const.ScreenRecoderConfig.DEFAULT_FILE_NAME);
        Assert.assertEquals("PC_test.mp4", Const.ScreenRecoderConfig.PC_FILE_NAME);
        Assert.assertEquals("PHONE_test.mp4", Const.ScreenRecoderConfig.PHONE_FILE_NAME);
    }

    @Test
    public void testNetworkMonitorConfig() {
        Assert.assertEquals("/Documents/dump.log", Const.NetworkMonitorConfig.AndroidDumpPath);
        Assert.assertEquals("/network_dump.log", Const.NetworkMonitorConfig.DumpPath);
        Assert.assertEquals("/network_result.log", Const.NetworkMonitorConfig.ResultPath);
    }

    @Test
    public void testDeviceStability() {
        Assert.assertEquals("went ONLINE", Const.DeviceStability.BEHAVIOUR_GO_ONLINE);
        Assert.assertEquals("went OFFLINE", Const.DeviceStability.BEHAVIOUR_GO_OFFLINE);
        Assert.assertEquals("connected", Const.DeviceStability.BEHAVIOUR_CONNECT);
        Assert.assertEquals("disconnected", Const.DeviceStability.BEHAVIOUR_DISCONNECT);
    }

    @Test
    public void testFrontEndPath() {
        Assert.assertEquals("/portal", Const.FrontEndPath.PREFIX_PATH);
        Assert.assertEquals("/portal/index.html", Const.FrontEndPath.INDEX_PATH);
        Assert.assertEquals("#", Const.FrontEndPath.ANCHOR);
        Assert.assertEquals("redirectUrl", Const.FrontEndPath.REDIRECT_PARAM);
        Assert.assertEquals("/v3/api-docs", Const.FrontEndPath.SWAGGER_DOC_PATH);
    }

    @Test
    public void testRegexString() {
        Assert.assertEquals("[0-9a-fA-F]{8}(-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}", Const.RegexString.UUID);
        Assert.assertEquals("\\w*", Const.RegexString.COMMON_STR);
        Assert.assertEquals("(/[A-Za-z0-9_.-]*)*", Const.RegexString.URL);
        Assert.assertEquals("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", Const.RegexString.MAIL_ADDRESS);
        Assert.assertEquals("^(\\/[^\\t\\f\\n\\r\\v]+)+\\/?$", Const.RegexString.LINUX_ABSOLUTE_PATH);
        Assert.assertEquals("^([a-zA-Z]:)(\\\\[^/\\\\:*?\"<>|]+\\\\?)*$", Const.RegexString.WINDOWS_ABSOLUTE_PATH);
        Assert.assertEquals("^([^\\/\\\\:*?\"<>|]+\\/)+[^\\/\\\\:*?\"<>|;]+$", Const.RegexString.STORAGE_FILE_REL_PATH);
        Assert.assertEquals("\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", Const.RegexString.PACKAGE_NAME);
        Assert.assertEquals("^[0-9]*$", Const.RegexString.INTEGER);
    }

    @Test
    public void testPermissionType() {
        Assert.assertEquals("API", Const.PermissionType.API);
        Assert.assertEquals("METHOD", Const.PermissionType.METHOD);
    }

    @Test
    public void testDefaultRole() {
        Assert.assertEquals("SUPER_ADMIN", Const.DefaultRole.SUPER_ADMIN);
        Assert.assertEquals("ADMIN", Const.DefaultRole.ADMIN);
        Assert.assertEquals("TEAM_ADMIN", Const.DefaultRole.TEAM_ADMIN);
        Assert.assertEquals("USER", Const.DefaultRole.USER);
    }

    @Test
    public void testDefaultTeam() {
        Assert.assertEquals("Default", Const.DefaultTeam.DEFAULT_TEAM_NAME);
    }

    @Test
    public void testAuthComponent() {
        Assert.assertEquals("DEFAULT_TEAM", Const.AuthComponent.DEFAULT_TEAM);
        Assert.assertEquals("TEAM", Const.AuthComponent.TEAM);
        Assert.assertEquals("ROLE", Const.AuthComponent.ROLE);
        Assert.assertEquals("AUTHORITY", Const.AuthComponent.AUTHORITY);
    }

    @Test
    public void testPreInstallFailurePolicy() {
        Assert.assertEquals("SHUTDOWN", Const.PreInstallFailurePolicy.SHUTDOWN);
        Assert.assertEquals("IGNORE", Const.PreInstallFailurePolicy.IGNORE);
    }

    @Test
    public void testFilePermission() {
        Assert.assertEquals("WRITE", Const.FilePermission.WRITE);
        Assert.assertEquals("READ", Const.FilePermission.READ);
    }

    @Test
    public void testStorageType() {
        Assert.assertEquals("LOCAL", Const.StorageType.LOCAL);
        Assert.assertEquals("AZURE", Const.StorageType.AZURE);
    }

    @Test
    public void testStoragePropertyBean() {
        Assert.assertEquals("localStorageProperty", Const.StoragePropertyBean.LOCAL);
        Assert.assertEquals("azureBlobProperty", Const.StoragePropertyBean.AZURE);
    }

    @Test
    public void testXCTestConfig() {
        Assert.assertEquals("Xctest", Const.XCTestConfig.XCTEST_ZIP_FOLDER_NAME);
        Assert.assertEquals("result.xcresult", Const.XCTestConfig.XCTEST_RESULT_FILE_NAME);
    }

    @Test
    public void testLocalStorageURL() {
        Assert.assertEquals("/api/storage/local/upload", Const.LocalStorageURL.CENTER_LOCAL_STORAGE_UPLOAD);
        Assert.assertEquals("/api/storage/local/download", Const.LocalStorageURL.CENTER_LOCAL_STORAGE_DOWNLOAD);
        Assert.assertEquals("storage/local/", Const.LocalStorageURL.CENTER_LOCAL_STORAGE_ROOT);
    }

    @Test
    public void testLocalStorageConst() {
        Assert.assertEquals(Arrays.asList("/api/storage/local/upload", "/api/storage/local/download"), Const.LocalStorageConst.PATH_PREFIX_LIST);
    }

    @Test
    public void testTestDeviceTag() {
        Assert.assertEquals("PRIMARY_PHONE", Const.TestDeviceTag.PRIMARY_PHONE);
        Assert.assertEquals("SECONDARY_PHONE", Const.TestDeviceTag.SECONDARY_PHONE);
        Assert.assertEquals("TERTIARY_PHONE", Const.TestDeviceTag.TERTIARY_PHONE);
        Assert.assertEquals("PRIMARY_PC", Const.TestDeviceTag.PRIMARY_PC);
    }

    @Test
    public void testOperatedDevice() {
        Assert.assertEquals("Agent", Const.OperatedDevice.AGENT);
        Assert.assertEquals("Windows", Const.OperatedDevice.WINDOWS);
        Assert.assertEquals("Android", Const.OperatedDevice.ANDROID);
        Assert.assertEquals("iOS", Const.OperatedDevice.IOS);
    }
}
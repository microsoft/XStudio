package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

public class GlobalConstantTest {

    @Test
    public void testAgentLiveStatusEnum() {
        GlobalConstant.AgentLiveStatus onlineStatus = GlobalConstant.AgentLiveStatus.ONLINE;
        GlobalConstant.AgentLiveStatus offlineStatus = GlobalConstant.AgentLiveStatus.OFFLINE;

        Assert.assertEquals("ONLINE", onlineStatus.getStatus());
        Assert.assertEquals("OFFLINE", offlineStatus.getStatus());
    }

    @Test
    public void testPrometheusMetricConstants() {
        Assert.assertEquals("agent_disk_usage_ratio", GlobalConstant.PROMETHEUS_METRIC_DISK_USAGE_RATIO);
        Assert.assertEquals("agent_ws_reconnect_retry_times", GlobalConstant.PROMETHEUS_METRIC_WEBSOCKET_RECONNECT_RETRY_TIMES);
        Assert.assertEquals("agent_running_test_num", GlobalConstant.PROMETHEUS_METRIC_RUNNING_TEST_NUM);
        Assert.assertEquals("agent_device_state_change_times", GlobalConstant.PROMETHEUS_METRIC_DEVICE_STATE_CHANGE_TIMES);
        Assert.assertEquals("agent_device_unstable_signal", GlobalConstant.PROMETHEUS_METRIC_TEST_DEVICE_UNSTABLE_SIGNAL);
        Assert.assertEquals("agent_device_offline_signal", GlobalConstant.PROMETHEUS_METRIC_TEST_DEVICE_OFFLINE_SIGNAL);
        Assert.assertEquals("agent_device_running_test_signal", GlobalConstant.PROMETHEUS_METRIC_TEST_DEVICE_RUNNING_TEST_SIGNAL);
        Assert.assertEquals("agent_device_alive_signal", GlobalConstant.PROMETHEUS_METRIC_TEST_DEVICE_ALIVE_SIGNAL);
        Assert.assertEquals("agent_device_adb_cmd_timeout_signal", GlobalConstant.PROMETHEUS_METRIC_TEST_DEVICE_ADB_TIMEOUT_SIGNAL);
        Assert.assertEquals("agent_ws_disconnect_signal", GlobalConstant.PROMETHEUS_METRIC_WEBSOCKET_DISCONNECT_SIGNAL);
        Assert.assertEquals("agent_online_agent_num", GlobalConstant.PROMETHEUS_METRIC_ONLINE_AGENT_NUM);
        Assert.assertEquals("agent_online_device_num", GlobalConstant.PROMETHEUS_METRIC_ONLINE_DEVICE_NUM);
    }
}
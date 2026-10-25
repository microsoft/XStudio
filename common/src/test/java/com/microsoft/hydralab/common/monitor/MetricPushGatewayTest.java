package com.microsoft.hydralab.common.monitor;

import io.prometheus.client.CollectorRegistry;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MetricPushGatewayTest {

    private MetricPushGateway metricPushGateway;
    private CollectorRegistry collectorRegistry;
    private String job;
    private Map<String, String> groupingKey;

    @Before
    public void setUp() {
        metricPushGateway = new MetricPushGateway("http://localhost:9091");
        collectorRegistry = mock(CollectorRegistry.class);
        job = "testJob";
        groupingKey = new HashMap<>();
        groupingKey.put("key1", "value1");
        groupingKey.put("key2", "value2");
    }

    @Test
    public void testPushAdd() throws IOException {
        MetricPushGateway spyMetricPushGateway = spy(metricPushGateway);
        doNothing().when(spyMetricPushGateway).pushAdd(collectorRegistry, job, groupingKey);

        spyMetricPushGateway.pushAdd(collectorRegistry, job, groupingKey);

        verify(spyMetricPushGateway, times(1)).pushAdd(collectorRegistry, job, groupingKey);
    }

    @Test(expected = IOException.class)
    public void testPushAddWithIOException() throws IOException {
        MetricPushGateway spyMetricPushGateway = spy(metricPushGateway);
        doThrow(new IOException()).when(spyMetricPushGateway).pushAdd(collectorRegistry, job, groupingKey);

        spyMetricPushGateway.pushAdd(collectorRegistry, job, groupingKey);
    }
}
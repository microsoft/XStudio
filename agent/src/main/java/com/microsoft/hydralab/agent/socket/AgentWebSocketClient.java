// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

package com.microsoft.hydralab.agent.socket;

import com.microsoft.hydralab.agent.service.AgentWebSocketClientService;
import com.microsoft.hydralab.common.entity.common.Message;
import com.microsoft.hydralab.common.util.Const;
import com.microsoft.hydralab.common.util.FlowUtil;
import com.microsoft.hydralab.common.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.websocket.CloseReason;
import java.net.URI;
import java.nio.ByteBuffer;

@Slf4j
public class AgentWebSocketClient extends WebSocketClient {
    private final AgentWebSocketClientService agentWebSocketClientService;

    private boolean connectionActive = false;
    private int reconnectTime = 0;
    private int violatedReconnectTime = 0;

    public AgentWebSocketClient(URI serverUri, AgentWebSocketClientService agentWebSocketClientService) {
        super(serverUri);
        this.agentWebSocketClientService = agentWebSocketClientService;
        agentWebSocketClientService.setSendMessageCallback(message -> {
            byte[] data = SerializeUtil.messageToByteArr(message);
            log.info("send, path: {}, message data len: {}", message.getPath(), data.length);
            try {
                FlowUtil.retryAndSleepWhenException(3, 10, () -> {
                    AgentWebSocketClient.this.send(data);
                    return true;
                });
            } catch (Exception e) {
                log.error("send message to center error, message path is {}", message.getPath(), e);
            }
        });
    }

    @Override
    public void onOpen(ServerHandshake handShakeData) {
        connectionActive = true;
        log.info("onOpen message {}, {}", handShakeData.getHttpStatus(), handShakeData.getHttpStatusMessage());
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        connectionActive = true;
        Message message = SerializeUtil.byteArrToMessage(bytes.array());
        agentWebSocketClientService.onMessage(message);
        if (Const.Path.DEVICE_LIST.equals(message.getPath())) {
            reconnectTime = 0;
            violatedReconnectTime = 0;
        }
    }

    @Override
    public void onMessage(String message) {
        connectionActive = true;
        log.info("onMessage Receive String message {}", message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.error("onClose {}, {}, {}, {}", code, reason, remote, reconnectTime);
        reconnectTime++;
        connectionActive = false;
        // if the connection is closed by server with 1003, exit the agent
        if (code == CloseReason.CloseCodes.CANNOT_ACCEPT.getCode()) {
            System.exit(code);
        }

        // if the connection is closed by server with 1008, wait and retry
        // Allow up to 10 reconnect attempts before exiting
        if (code == CloseReason.CloseCodes.VIOLATED_POLICY.getCode()) {
            violatedReconnectTime++;
            if(violatedReconnectTime > 10) {
                log.error("onClose, code: {}, reason: {}, remote: {}, reconnectTime: {}, {}", code, reason, remote, reconnectTime, violatedReconnectTime);
                System.exit(code);
            } else {
                // wait for 10 seconds and then retry
                try {
                    log.info("onClose, code: {}, reason: {}, remote: {}, reconnectTime: {}, {} sleep 10 seconds", code, reason, remote, reconnectTime, violatedReconnectTime);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    log.error("onClose, sleep error", e);
                }
            }
        }
    }

    @Override
    public void onError(Exception ex) {
        log.error("onError", ex);
        reconnectTime++;
        connectionActive = false;
    }

    public boolean isConnectionActive() {
        return connectionActive;
    }

    public int getReconnectTime() {
        return reconnectTime;
    }
}

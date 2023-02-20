package com.huytd.websocket.entity;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Data
public class SpringWSSession {
    private final WebSocketSession client;
    private final String id;
    private boolean ssClosed = false;
    private long lastTime;

    private static final Gson gson = new Gson();

    public SpringWSSession(WebSocketSession client) {
        this.client = client;
        this.id = client.getId();
        this.lastTime = System.currentTimeMillis();
    }

    public void close() {
        this.disconnect();
    }

    public synchronized void disconnect() {
        if (this.ssClosed) {
            return;
        }
        this.ssClosed = true;
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                log.error("Ex: {}", e.getMessage());
            }
        }
    }

    public void send(Object message) {
        log.info("send: {}", message);
        String text = gson.toJson(message);
        try {
            client.sendMessage(new TextMessage(text));
        } catch (IOException e) {
            log.error("err: {}", e.getMessage());
        }
        this.lastTime = System.currentTimeMillis();

    }
}

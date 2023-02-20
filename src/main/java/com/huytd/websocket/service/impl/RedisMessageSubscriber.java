package com.huytd.websocket.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huytd.websocket.handler.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    private final Gson gson;

    public RedisMessageSubscriber(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String messageString = new String(message.getBody(), StandardCharsets.UTF_8);

        JsonObject jsonObject = gson.fromJson(messageString, JsonObject.class);

        String userId = jsonObject.get("userId").getAsString();
        JsonElement data = jsonObject.get("data");
        SessionManager.sendMessageByWebSocket(userId, data);

        log.info("Message received : "+ message);
    }
}

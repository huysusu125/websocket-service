package com.huytd.websocket.service.impl;

import com.huytd.websocket.service.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisMessageImpl implements MessagePublisher {
    @Value(value = "${redis-websocket.websocket-topic}")
    private String topic;
    private final  RedisTemplate<String,Object> redisTemplate;

    @Override
    public void publish(String message) {
        redisTemplate.convertAndSend(topic, message);
    }
}

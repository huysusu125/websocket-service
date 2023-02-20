package com.huytd.websocket.controller;

import com.huytd.websocket.service.MessagePublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final MessagePublisher messagePublisher;

    public TestController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @PostMapping
    public ResponseEntity<?> testWebSocket(@RequestBody String message) {
        messagePublisher.publish(message);
        return ResponseEntity.ok(message);
    }
}

package com.huytd.websocket.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huytd.websocket.constain.ParamURI;
import com.huytd.websocket.constain.WebsocketStatusCodeEnum;
import com.huytd.websocket.entity.SpringWSSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("connection closed with status: {}", status.getCode());
        SessionManager.closeSession(session.getId());
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        log.info("connection established");
        Map<String, Object> map = session.getAttributes();
        SpringWSSession client = new SpringWSSession(session);
        String userId = (String) map.get(ParamURI.token);
        if (!StringUtils.hasText(userId)) {
            closeSocket(session, client, userId);
            return;
        }
        //TODO: get token jwt and validation token jwt and get user info from token jwt
        SessionManager.addSession(userId, client);
    }

    private void closeSocket(WebSocketSession session, SpringWSSession client, String userId) throws IOException {
        session.close(new CloseStatus(WebsocketStatusCodeEnum.CLOSE_AND_NOT_RECONNECT.getCode(), "Info invalid"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage msg) throws Exception {
        SessionManager.setLastAction(session.getId());
        JsonObject jo = new JsonObject();
        jo.addProperty("ping", "ok");
        session.sendMessage(new TextMessage(jo.toString()));
        log.info("session: {}, message: {}", session.getId(), msg);
    }
}

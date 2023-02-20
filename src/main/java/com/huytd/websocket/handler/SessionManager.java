package com.huytd.websocket.handler;

import com.huytd.websocket.entity.SpringWSSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SessionManager {
    private static final ConcurrentHashMap<String, List<SpringWSSession>> userSessionMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, SpringWSSession> SESSIONS = new ConcurrentHashMap<>();
    private static final ReentrantLock LOCK = new ReentrantLock(true);


    public static void sendMessageByWebSocket(String userId, Object message) {
        if (userSessionMap.isEmpty()) {
            return;
        }
        List<SpringWSSession> list = userSessionMap.get(userId);
        if (list == null || list.isEmpty()) {
            return;
        }
        list.forEach(springWSSession -> springWSSession.send(message));
    }
    public static void addSession(String userId, SpringWSSession session) {
        SESSIONS.put(session.getId(), session);
        LOCK.lock();
        try {
            addSessionToMapConn(userId, session);
        } finally {
            LOCK.unlock();
        }

    }

    private static void addSessionToMapConn(String userId, SpringWSSession session) {
        List<SpringWSSession> sessions = SessionManager.userSessionMap.get(userId);
        if (sessions == null || sessions.isEmpty()) {
            sessions = new ArrayList<>();
            sessions.add(session);
            SessionManager.userSessionMap.put(userId, sessions);
            return;
        }
        sessions.add(session);
    }

    public static void setLastAction(String id) {
        if (!StringUtils.hasText(id)) {
            return;
        }
        SpringWSSession session = SESSIONS.get(id);
        if (session != null) {
            session.setLastTime(System.currentTimeMillis());
        }
    }

    public static void closeSession(String id) {
        if (!StringUtils.hasText(id)) {
            return;
        }
        SpringWSSession session = SESSIONS.get(id);
        if (session != null) {
            session.close();
        }
    }



}

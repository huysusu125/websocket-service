package com.huytd.websocket.handler;

import com.huytd.websocket.constain.ParamURI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClientSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes)  {
        URI uri = request.getURI();
        log.info("- - - - Connection websocket");
        Map<String, String> params = getParams(uri);
        String token = params.get(ParamURI.token);
        attributes.put(ParamURI.token, token);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    public static Map<String, String> getParams(URI uri) {
        Map<String, String> result = new HashMap<>();
        if (uri != null) {
            String query = uri.getQuery();
            if (query != null) {
                String[] infos = query.split("&");
                if (infos.length > 0) {
                    for (String s : infos) {
                        if (StringUtils.hasText(s) && s.contains("=")) {
                            String[] info = s.split("=");
                            if (info.length >= 2) {
                                String key = info[0];
                                String value = info[1];
                                result.put(key, value);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}

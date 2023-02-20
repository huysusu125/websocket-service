package com.huytd.websocket.constain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum WebsocketStatusCodeEnum {

    CLOSE_AND_NOT_RECONNECT(4009),

    ;

    @Getter
    private final Integer code;

    public static WebsocketStatusCodeEnum valueOf(Integer value) {
        return Arrays.stream(values())
                .filter(type -> type.getCode().equals(value))
                .findFirst()
                .orElse(null);
    }
}

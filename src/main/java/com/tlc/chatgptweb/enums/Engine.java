package com.tlc.chatgptweb.enums;

import lombok.Getter;

@Getter
public enum Engine {

    GPT_3_5_TURBO("gpt-3.5-turbo", 4096),
    GPT_3_5_TURBO_1160("gpt-3.5-turbo-1106", 16385);

    private final String model;
    private final int maxToken;

    Engine(String model, int maxToken) {
        this.model = model;
        this.maxToken = maxToken;
    }

}

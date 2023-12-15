package com.tlc.chatgptweb.cache;

import lombok.Getter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Getter
public enum CacheType {

    OPEN_AI_MESSAGE_CACHE("openai-message", Duration.of(10, TimeUnit.MINUTES.toChronoUnit()), 100);

    private final String name;
    private final Duration expired;
    private final long maximumSize;

    CacheType(String name, Duration expired, long maximumSize) {
        this.name = name;
        this.expired = expired;
        this.maximumSize = maximumSize;
    }

}

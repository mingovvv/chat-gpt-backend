package com.tlc.chatgptweb.config.azure;

import lombok.Getter;

@Getter
public enum KeyVault {

    OPENAI_API_ACCESS_KEY("openai-api-key");

    private final String keyName;

    KeyVault(String keyName) {
        this.keyName = keyName;
    }

}

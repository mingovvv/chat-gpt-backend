package com.tlc.chatgptweb.dto;

import com.tlc.chatgptweb.enums.Engine;
import lombok.Builder;

import java.util.List;

@Builder
public record OpenaiRequest(String model, List<Message> messages, boolean stream) {

    public static OpenaiRequest from(List<Message> messages, boolean isStream) {
        return OpenaiRequest.builder()
                .model(Engine.GPT_3_5_TURBO_1160.getModel())
                .messages(messages)
                .stream(isStream)
                .build();
    }

}

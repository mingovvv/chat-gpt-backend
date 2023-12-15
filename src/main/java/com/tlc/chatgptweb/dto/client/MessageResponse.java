package com.tlc.chatgptweb.dto.client;

import com.tlc.chatgptweb.dto.OpenaiResponse;
import lombok.Builder;

@Builder
public record MessageResponse(String id, String message) {

    public static MessageResponse from(OpenaiResponse response) {
        return MessageResponse.builder()
                .message(response.choices().get(0).message().content())
                .build();
    }

}

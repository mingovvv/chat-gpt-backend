package com.tlc.chatgptweb.dto.client;

import com.tlc.chatgptweb.dto.OpenaiStreamResponse;
import lombok.Builder;

@Builder
public record MessageStreamResponse(String id, String message, boolean isFinished) {

    public static MessageStreamResponse from(OpenaiStreamResponse openaiStreamResponse) {
        return MessageStreamResponse.builder()
                .message(openaiStreamResponse.choices().get(0).delta().content())
                .isFinished("stop".equals(openaiStreamResponse.choices().get(0).finishReason()))
                .build();
    }

}

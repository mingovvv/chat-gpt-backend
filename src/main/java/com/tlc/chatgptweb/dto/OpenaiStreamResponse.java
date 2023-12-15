package com.tlc.chatgptweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.tlc.chatgptweb.enums.Role;

import java.util.List;

@JsonRootName(value = "data")
public record OpenaiStreamResponse(
        String id,
        String object,
        Long created,
        String model,
        List<Choices> choices,
        @JsonProperty("system_fingerprint") String systemFingerprint) {

    public record Choices(
            int index,
            Delta delta,
            @JsonProperty("finish_reason") String finishReason) {

        public record Delta(Role role, String content) {}
    }

}

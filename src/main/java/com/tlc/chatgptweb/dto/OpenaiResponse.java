package com.tlc.chatgptweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenaiResponse(
        String id,
        String object,
        Long created,
        String model,
        List<Choices> choices,
        Usage usage,
        @JsonProperty("system_fingerprint") String systemFingerprint) {

    public record Choices(
            int index,
            Message message,
            @JsonProperty("finish_reason") String finishReason) {}

    public record Usage(
            @JsonProperty("prompt_tokens") int promptTokens,
            @JsonProperty("completion_tokens") int completionTokens,
            @JsonProperty("total_tokens") int totalTokens) {}

}

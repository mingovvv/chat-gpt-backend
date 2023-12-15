package com.tlc.chatgptweb.dto.client;

import lombok.Builder;

@Builder
public record MessageRequest(String id, String message) {
}

package com.tlc.chatgptweb.dto;

import com.tlc.chatgptweb.dto.client.MessageRequest;
import com.tlc.chatgptweb.enums.Role;
import lombok.Builder;

import java.util.List;
import java.util.stream.Stream;

@Builder
public record Message(String role, String content) {

    public static List<Message> toList(String message) {
        return Stream.of(of(Role.USER, message)).toList();
    }

    public static Message of(Role role, MessageRequest messageRequest) {
        return Message.builder()
                .role(role.getName())
                .content(messageRequest.message())
                .build();
    }

    public static Message of(Role role, String message) {
        return Message.builder()
                .role(role.getName())
                .content(message)
                .build();
    }

}

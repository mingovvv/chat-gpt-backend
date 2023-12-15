package com.tlc.chatgptweb.enums;

import com.tlc.chatgptweb.dto.Message;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * CHAT-GPT에게 미리 선입력 시켜둘 대화 정의
 */
@Getter
public enum Dialog {

    GREETINGS(Role.SYSTEM, "해당 애플리케이션은 투라인코드에서 제작되었습니다.");

    private final Role role;
    private final String message;

    Dialog(Role role, String message) {
        this.role = role;
        this.message = message;
    }

    private static final List<Message> initMessages;

    static {
        initMessages = Arrays.stream(values()).map(d -> Message.of(d.role, d.message)).toList();
    }

    public static List<Message> getInitializeDialog() {
        return initMessages;
    }

}

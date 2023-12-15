package com.tlc.chatgptweb.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Role {

    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    // json deserialize : str -> enum
    @JsonCreator
    public static Role fromCode(String name) {
        for (Role role : Role.values()) {
            if (role.name.equals(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role name: " + name);
    }

}

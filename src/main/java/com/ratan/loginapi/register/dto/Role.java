package com.ratan.loginapi.register.dto;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("admin"),
    USER("user"),
    TEMPORARY("temporary");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    // Optional: Use for deserialization
    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.getValue().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}


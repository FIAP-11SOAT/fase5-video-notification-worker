package com.example.notification.shared.exceptions;

import lombok.Getter;

@Getter
public enum ErrorType {

    INVALID_EVENT_TYPE(1, "invalid event type", "event type does not exists"),
    ERROR_SENDING_EMAIL(2, "email error", "error sendind email"),
    UTILITY_CLASS_ERROR(3, "utility class error", "This is a utility class and cannot be instantiated");

    private final int code;
    private final String name;
    public final String message;

    ErrorType(int code, String name, String message){
        this.code = code;
        this.name = name;
        this.message = message;
    }
}

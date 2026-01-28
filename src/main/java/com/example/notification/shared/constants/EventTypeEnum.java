package com.example.notification.shared.constants;

import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.ExceptionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum EventTypeEnum {

    UPLOADED("uploaded"),
    PROCESSED("processed"),
    ERROR_PROCESSING("error");

    private final String value;

    EventTypeEnum(String value) {
        this.value = value.toLowerCase(Locale.ROOT);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EventTypeEnum fromValue(String value) {
        for (EventTypeEnum type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw ExceptionUtils.badRequest(ErrorType.INVALID_EVENT_TYPE, null);
    }
}

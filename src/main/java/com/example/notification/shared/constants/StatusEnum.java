package com.example.notification.shared.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

public enum StatusEnum {

    UPLOADED("uploaded", "Frameify: Seu vídeo foi carregado 🤩"),
    PROCESSED("processed", "Frameify: Seus frames estão disponíveis ! 🚀"),
    ERROR_PROCESSING("error-processing", "Frameify: Houve um problema ao processar seu vídeo 🤦🏽‍♂️");

    private final String templateName;

    @Getter
    private final String subject;

    StatusEnum(String templateName, String subject) {
        this.templateName = templateName.toLowerCase(Locale.ROOT);
        this.subject = subject;
    }

    @JsonValue
    public String getTemplateName() {
        return templateName;
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.templateName.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid StatusEnum: " + value));
    }
}

package com.example.notification.shared.constants;

import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.ExceptionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Locale;

public enum StatusEnum {

    UPLOADED("uploaded", "Frameify: Seu vídeo foi carregado \uD83E\uDD29"),
    PROCESSED("processed", "Frameify: Seus frames estão disponíveis ! \uD83D\uDE80"),
    ERROR_PROCESSING("error-processing", "Frameify: Houve um problema ao processar seu vídeo \uD83E\uDD26\uD83C\uDFFD");

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

}
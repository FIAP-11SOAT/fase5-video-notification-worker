package com.example.notification.core.model;

import com.example.notification.shared.constants.StatusEnum;

import java.time.LocalDateTime;

public record NotificationRequest(
        String videoKey,
        String name,
        String userId,
        StatusEnum status,
        LocalDateTime dateReceived
) {
}

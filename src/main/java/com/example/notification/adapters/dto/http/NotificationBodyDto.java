package com.example.notification.adapters.dto.http;

import com.example.notification.shared.constants.StatusEnum;

public record NotificationBodyDto(
        PayloadDto payload
) {

    public record PayloadDto(
            String videoKey,
            String userId,
            StatusEnum status
    ) {
    }
}

package com.example.notification.adapters.dto.queue;

import com.example.notification.shared.constants.StatusEnum;

public record MessageQueueDto(
        PayloadDto payload
) {

    public record PayloadDto(
            String videoKey,
            String name,
            String userId,
            StatusEnum status
    ) {
    }
}
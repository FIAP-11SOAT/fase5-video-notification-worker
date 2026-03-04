package com.example.notification.adapters.dto.queue;

import com.example.notification.shared.constants.StatusEnum;

public record MessageQueueDto(
        PayloadDto payload
) {

    public record PayloadDto(
            String videoKey,
            String videoName,
            String userId,
            StatusEnum status
    ) {
    }
}
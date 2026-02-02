package com.example.notification.adapters.dto.queue;

import com.example.notification.shared.constants.StatusEnum;
import com.example.notification.shared.dto.ItemDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
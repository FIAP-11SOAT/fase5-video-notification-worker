package com.example.notification.adapters.inbound.queue.sqs;

import com.example.notification.adapters.converter.queue.MessageQueueConverter;
import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.adapters.inbound.queue.QueueMessageListenerPort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"prod", "dev", "test"})
public class SqsQueueMessageListenerAdapter implements QueueMessageListenerPort {

    private final NotificationServicePort service;
    private final MessageQueueConverter converter;
    private final ObjectMapper objectMapper;

    public SqsQueueMessageListenerAdapter(NotificationServicePort service, MessageQueueConverter converter, ObjectMapper objectMapper) {
        this.service = service;
        this.converter = converter;
        this.objectMapper = objectMapper;
    }

    @Override
    @SqsListener(value = "fase5-video-notification-queue")
    public void onMessage(String message) {
        try {
            MessageQueueDto notification = objectMapper.readValue(message, MessageQueueDto.class);
            NotificationRequest request = converter.convertToNotifyRequest(notification);
            service.notify(request);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem da fila: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar mensagem", e);
        }
    }
}

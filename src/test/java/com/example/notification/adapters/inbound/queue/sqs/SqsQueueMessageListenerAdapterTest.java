package com.example.notification.adapters.inbound.queue.sqs;

import com.example.notification.adapters.converter.queue.MessageQueueConverter;
import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.shared.constants.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SqsQueueMessageListenerAdapterTest {

    @Mock
    private NotificationServicePort service;

    @Mock
    private MessageQueueConverter converter;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SqsQueueMessageListenerAdapter listener;

    @Test
    void shouldProcessMessageSuccessfully() throws Exception {
        // given
        String jsonMessage = """
            {
              "payload": {
                "videoKey": "video/123",
                "videoName": "Meu vídeo",
                "userId": "user-1",
                "status": "PROCESSED"
              }
            }
        """;

        MessageQueueDto.PayloadDto payload =
                new MessageQueueDto.PayloadDto(
                        "video/123",
                        "Meu vídeo",
                        "user-1",
                        StatusEnum.PROCESSED
                );

        MessageQueueDto dto = new MessageQueueDto(payload);

        NotificationRequest request =
                new NotificationRequest(
                        "video/123",
                        "Meu vídeo",
                        "user-1",
                        StatusEnum.PROCESSED,
                        LocalDateTime.now()
                );

        when(objectMapper.readValue(jsonMessage, MessageQueueDto.class))
                .thenReturn(dto);

        when(converter.convertToNotifyRequest(dto))
                .thenReturn(request);

        // when
        listener.onMessage(jsonMessage);

        // then
        verify(objectMapper).readValue(jsonMessage, MessageQueueDto.class);
        verify(converter).convertToNotifyRequest(dto);
        verify(service).notify(request);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenJsonIsInvalid() throws Exception {
        // given
        String invalidJson = "{ invalid json }";

        when(objectMapper.readValue(invalidJson, MessageQueueDto.class))
                .thenThrow(new RuntimeException("JSON error"));

        // when
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> listener.onMessage(invalidJson)
        );

        // then
        verify(objectMapper).readValue(invalidJson, MessageQueueDto.class);
        verifyNoInteractions(converter);
        verifyNoInteractions(service);

        assertNotNull(exception.getCause());
        assertEquals("JSON error", exception.getCause().getMessage());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenServiceFails() throws Exception {
        // given
        String jsonMessage = """
            {
              "payload": {
                "videoKey": "video/123",
                "videoName": "Meu vídeo",
                "userId": "user-1",
                "status": "PROCESSED"
              }
            }
        """;

        MessageQueueDto dto = new MessageQueueDto(
                new MessageQueueDto.PayloadDto(
                        "video/123",
                        "Meu vídeo",
                        "user-1",
                        StatusEnum.PROCESSED
                )
        );

        NotificationRequest request =
                new NotificationRequest(
                        "video/123",
                        "Meu vídeo",
                        "user-1",
                        StatusEnum.PROCESSED,
                        LocalDateTime.now()
                );

        when(objectMapper.readValue(jsonMessage, MessageQueueDto.class))
                .thenReturn(dto);

        when(converter.convertToNotifyRequest(dto))
                .thenReturn(request);

        doThrow(new RuntimeException("Service error"))
                .when(service).notify(request);

        // when
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> listener.onMessage(jsonMessage)
        );

        // then
        verify(service).notify(request);
        assertNotNull(exception.getCause());
        assertEquals("Service error", exception.getCause().getMessage());
    }
}

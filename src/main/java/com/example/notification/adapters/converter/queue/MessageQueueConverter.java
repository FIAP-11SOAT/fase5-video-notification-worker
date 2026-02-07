package com.example.notification.adapters.converter.queue;

import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.core.model.NotificationRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageQueueConverter {

    public NotificationRequest convertToNotifyRequest(MessageQueueDto message){
        return new NotificationRequest(
                message.payload().videoKey(),
                message.payload().videoName(),
                message.payload().userId(),
                message.payload().status(),
                LocalDateTime.now()
        );
    }
}

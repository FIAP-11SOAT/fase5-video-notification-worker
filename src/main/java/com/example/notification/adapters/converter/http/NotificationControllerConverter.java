package com.example.notification.adapters.converter.http;

import com.example.notification.adapters.dto.http.NotificationBodyDto;
import com.example.notification.core.model.NotificationRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationControllerConverter {

    public NotificationRequest convertToNotifyRequest(NotificationBodyDto body){
        return new NotificationRequest(
                body.payload().videoKey(),
                body.payload().userId(),
                body.payload().status(),
                LocalDateTime.now()
        );
    }
}

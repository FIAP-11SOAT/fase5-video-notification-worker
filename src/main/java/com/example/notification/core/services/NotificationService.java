package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.dto.UserResponse;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.adapters.outbound.user.UserAdapter;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.core.services.templates.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class NotificationService implements NotificationServicePort {

    private final UserAdapter userService;
    private final EmailServicePort emailService;
    private final TemplateRendererService templateRendererService;

    public NotificationService(
            UserAdapter userService, EmailServicePort emailService,
            TemplateRendererService templateRendererService
    ) {
        this.userService = userService;
        this.emailService = emailService;
        this.templateRendererService = templateRendererService;
    }

    @Override
    public void notify(NotificationRequest request) {
        try {
            String templateName = request.status().getTemplateName();
            String subject = request.status().getSubject();
            UserResponse user = userService.getUserById(request.userId());
            // UserResponse user = new UserResponse("fulano", "orlo5@ethereal.email");
            String htmlBody = templateRendererService.render(
                    templateName,
                    getVariables(user, request)
            );
            String to = user.email();
            EmailDto emailDto = new EmailDto(to, subject, htmlBody);
            emailService.sendEmail(emailDto);
        } catch (Exception e) {
            log.error("[NotificationService]: Error handling request", e);
            throw e;
        }
    }

    private Map<String, Object> getVariables(UserResponse user, NotificationRequest request) {
        return Map.of(
                "customerName", user.name(),
                "videoName", request.name(),
                "videoKey", request.videoKey()
        );
    }
}

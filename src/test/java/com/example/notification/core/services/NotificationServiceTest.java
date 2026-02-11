package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.dto.UserResponse;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.adapters.outbound.user.UserAdapter;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.services.templates.TemplateRendererService;
import com.example.notification.shared.constants.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private UserAdapter userService;

    @Mock
    private EmailServicePort emailService;

    @Mock
    private TemplateRendererService templateRendererService;

    @InjectMocks
    private NotificationService notificationService;

    @Captor
    private ArgumentCaptor<EmailDto> emailDtoCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> variablesCaptor;

    @Test
    void shouldNotifyUserSuccessfully() {
        // given
        StatusEnum status = StatusEnum.PROCESSED;
        NotificationRequest request = new NotificationRequest(
                "video-key-123",
                "Meu vídeo",
                "user-123",
                status,
                LocalDateTime.now()
        );

        UserResponse user = new UserResponse(
                "Amanda",
                "amanda@email.com"
        );

        String renderedHtml = "<html>Email</html>";

        when(userService.getUserById("user-123"))
                .thenReturn(user);

        when(templateRendererService.render(
                eq(status.getTemplateName()),
                anyMap()
        )).thenReturn(renderedHtml);

        // when
        notificationService.notify(request);

        // then
        verify(userService).getUserById("user-123");

        verify(templateRendererService).render(
                eq(status.getTemplateName()),
                variablesCaptor.capture()
        );

        Map<String, Object> variables = variablesCaptor.getValue();
        assertEquals("Amanda", variables.get("customerName"));
        assertEquals("Meu vídeo", variables.get("videoName"));
        assertEquals("video-key-123", variables.get("videoKey"));

        verify(emailService).sendEmail(emailDtoCaptor.capture());

        EmailDto emailDto = emailDtoCaptor.getValue();
        assertEquals("amanda@email.com", emailDto.to());
        assertEquals(status.getSubject(), emailDto.subject());
        assertEquals(renderedHtml, emailDto.body());
    }

    @Test
    void shouldPropagateExceptionWhenAnyDependencyFails() {
        // given
        StatusEnum status = StatusEnum.PROCESSED;
        NotificationRequest request = new NotificationRequest(
                "video-key-123",
                "Meu vídeo",
                "user-123",
                status,
                LocalDateTime.now()
        );

        RuntimeException exception = new RuntimeException("User service down");

        when(userService.getUserById(anyString()))
                .thenThrow(exception);

        // when / then
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> notificationService.notify(request)
        );

        assertEquals(exception, thrown);

        verify(userService).getUserById("user-123");
        verifyNoInteractions(templateRendererService);
        verifyNoInteractions(emailService);
    }
}

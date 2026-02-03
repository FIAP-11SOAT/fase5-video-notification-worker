package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.NotificationAPIException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmtpEmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private SmtpEmailService smtpEmailService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setup() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void shouldSendEmailSuccessfully() {
        // given
        EmailDto emailDto = new EmailDto(
                "destinatario@test.com",
                "Assunto de teste",
                "<p>Corpo do email</p>"
        );

        // when
        smtpEmailService.sendEmail(emailDto);

        // then
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void shouldThrowInternalErrorWhenSendingFails() {
        // given
        EmailDto emailDto = new EmailDto(
                "destinatario@test.com",
                "Assunto erro",
                "<p>Falha</p>"
        );

        RuntimeException cause = new RuntimeException("SMTP offline");

        doThrow(cause)
                .when(mailSender)
                .send(any(MimeMessage.class));


        // when
        NotificationAPIException exception = assertThrows(
                NotificationAPIException.class,
                () -> smtpEmailService.sendEmail(emailDto)
        );

        // then
        assertEquals(
                ErrorType.ERROR_SENDING_EMAIL.getMessage(),
                exception.getErrorCodeMessage()
        );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getHttpStatus()
        );

        assertEquals(cause, exception.getCause());
    }
}

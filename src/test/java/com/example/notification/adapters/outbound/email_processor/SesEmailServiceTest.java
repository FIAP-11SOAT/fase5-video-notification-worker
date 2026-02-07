package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.shared.constants.ApplicationConstants;
import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.NotificationAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SesEmailServiceTest {

    @Mock
    private SesClient sesClient;

    @InjectMocks
    private SesEmailService sesEmailService;

    @Captor
    private ArgumentCaptor<SendEmailRequest> sendEmailRequestCaptor;

    @Test
    void shouldSendEmailSuccessfully() {
        // given
        EmailDto emailDto = new EmailDto(
                "user@test.com",
                "Test subject",
                "<h1>Hello</h1>"
        );

        // when
        sesEmailService.sendEmail(emailDto);

        // then
        verify(sesClient).sendEmail(sendEmailRequestCaptor.capture());

        SendEmailRequest request = sendEmailRequestCaptor.getValue();

        assertEquals(ApplicationConstants.NO_REPLY_EMAIL, request.source());
        assertEquals(emailDto.to(), request.destination().toAddresses().get(0));
        assertEquals(emailDto.subject(), request.message().subject().data());
        assertEquals("UTF-8", request.message().subject().charset());
        assertEquals(emailDto.body(), request.message().body().html().data());
        assertEquals("UTF-8", request.message().body().html().charset());
    }

    @Test
    void shouldThrowNotificationApiExceptionWhenSesFails() {
        // given
        EmailDto emailDto = new EmailDto(
                "user@test.com",
                "Test subject",
                "<h1>Hello</h1>"
        );

        SesException sesException = (SesException) SesException.builder()
                .message("SES error")
                .awsErrorDetails(
                        AwsErrorDetails.builder()
                                .errorMessage("SES is down")
                                .build()
                )
                .build();

        doThrow(sesException)
                .when(sesClient)
                .sendEmail(any(SendEmailRequest.class));

        // when
        NotificationAPIException exception = assertThrows(
                NotificationAPIException.class,
                () -> sesEmailService.sendEmail(emailDto)
        );

        // then
        verify(sesClient).sendEmail(any(SendEmailRequest.class));

        assertEquals(ErrorType.ERROR_SENDING_EMAIL.getMessage(), exception.getErrorCodeMessage());
        assertEquals(500, exception.getHttpStatus());
        assertEquals(sesException, exception.getCause());
    }
}


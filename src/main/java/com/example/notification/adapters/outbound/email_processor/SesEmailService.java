package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.shared.constants.ApplicationConstants;
import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
@Service
@Profile({"prod"})
public class SesEmailService implements EmailServicePort {

    private final SesClient sesClient;

    public SesEmailService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    @Override
    public void sendEmail(EmailDto emailDto) {
        try {
            Destination destination = Destination.builder()
                    .toAddresses(emailDto.to())
                    .build();

            Content subject = Content.builder()
                    .data(emailDto.subject())
                    .charset("UTF-8")
                    .build();

            Content htmlBody = Content.builder()
                    .data(emailDto.body())
                    .charset("UTF-8")
                    .build();

            Body body = Body.builder()
                    .html(htmlBody)
                    .build();

            Message message = Message.builder()
                    .subject(subject)
                    .body(body)
                    .build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .source(ApplicationConstants.NO_REPLY_EMAIL) // no-reply@frameify.dev
                    .destination(destination)
                    .message(message)
                    .build();

            sesClient.sendEmail(request);

            log.info("[SesEmailService]: Email sent to {}", emailDto.to());

        } catch (SesException e) {
            log.error("[SesEmailService]: Error sending email {}", e.awsErrorDetails().errorMessage(), e);
            throw ExceptionUtils.internalError(ErrorType.ERROR_SENDING_EMAIL, e);
        }
    }
}


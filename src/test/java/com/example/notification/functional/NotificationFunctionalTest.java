package com.example.notification.functional;

import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.adapters.inbound.queue.sqs.SqsQueueMessageListenerAdapter;
import com.example.notification.adapters.outbound.email_processor.FakeEmailService;
import com.example.notification.core.services.templates.TemplateRendererService;
import com.example.notification.shared.constants.StatusEnum;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
class NotificationFunctionalTest {

    @Autowired
    private SqsQueueMessageListenerAdapter listener;

    @Autowired
    private FakeEmailService emailService;

    @MockBean
    private TemplateRendererService templateRendererService;

    /*
    @Test
    void shouldConsumeMessageAndCallUserApiViaHttp() {
        // given
        stubFor(get(urlEqualTo("/auth/user-by-id/user123"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {
                          "username": "Fulano HTTP",
                          "email": "fulano.http@email.com",
                          "cpf": "11111111111"
                        }
                    """)));

        when(templateRendererService.render(anyString(), anyMap()))
                .thenReturn("<html>Email</html>");

        MessageQueueDto message = new MessageQueueDto(
                new MessageQueueDto.PayloadDto(
                        "video-123",
                        "Meu vídeo",
                        "user123",
                        StatusEnum.PROCESSED
                )
        );

        // when
        listener.onMessage(message);

        // then — HTTP foi chamado
        WireMock.verify(
                getRequestedFor(urlEqualTo("/auth/user-by-id/user123"))
        );

        // then — FakeEmailService foi usado
        assertThat(emailService.getSentEmails())
                .containsExactly("fulano.http@email.com");
    }

     */

}


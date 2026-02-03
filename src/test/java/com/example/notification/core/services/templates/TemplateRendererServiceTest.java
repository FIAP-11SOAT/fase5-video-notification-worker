package com.example.notification.core.services.templates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateRendererServiceTest {

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private TemplateRendererService templateRendererService;

    @Test
    void shouldRenderTemplateSuccessfully() {
        // given
        String templateName = "email-template";
        Map<String, Object> variables = Map.of(
                "customerName", "Amanda",
                "videoKey", "video-123"
        );

        String renderedHtml = "<html>Email renderizado</html>";

        when(templateEngine.process(
                eq(templateName),
                any(Context.class)
        )).thenReturn(renderedHtml);

        // when
        String result = templateRendererService.render(templateName, variables);

        // then
        assertEquals(renderedHtml, result);

        verify(templateEngine).process(
                eq(templateName),
                any(Context.class)
        );
    }

    @Test
    void shouldPropagateExceptionWhenTemplateEngineFails() {
        // given
        String templateName = "email-template";
        Map<String, Object> variables = Map.of("key", "value");

        RuntimeException exception = new RuntimeException("Template not found");

        when(templateEngine.process(
                eq(templateName),
                any(Context.class)
        )).thenThrow(exception);

        // when / then
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> templateRendererService.render(templateName, variables)
        );

        assertEquals(exception, thrown);
    }
}

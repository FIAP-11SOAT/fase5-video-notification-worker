package com.example.notification.core.services.templates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
public class TemplateRendererService {

    private final TemplateEngine templateEngine;

    public TemplateRendererService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            return templateEngine.process(templateName, context);
        } catch (Exception e){
            log.error("[TemplateRendererService]: Error rendering template", e);
            throw e;
        }
    }
}

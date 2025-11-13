package com.yd.price.workflow.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI priceWorkflowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Price Workflow API")
                        .description("High-performance pricing and adjustments API")
                        .version("v1")
                        .contact(new Contact()
                                .name("Payment team")
                                .email("payment_team@example.com"))
                        .license(new License().name("Internal use only")))
                .externalDocs(new ExternalDocumentation()
                        .description("Internal docs")
                        .url("https://example.com/docs"));
    }
}

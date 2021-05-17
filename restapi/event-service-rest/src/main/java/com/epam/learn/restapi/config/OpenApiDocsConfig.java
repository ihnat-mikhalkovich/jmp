package com.epam.learn.restapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocsConfig {

    @Value("${app-info.version}")
    private String version;

    @Value("${app-info.title}")
    private String title;

    @Value("${app-info.description}")
    private String description;

    @Bean
    public OpenAPI eventServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(title)
                        .description(description)
                        .version(version));
    }

}

package com.project.EventAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI eventApiOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("EventAPI")
            .description("API de gerenciamento de eventos")
            .version("v1"));
  }
}

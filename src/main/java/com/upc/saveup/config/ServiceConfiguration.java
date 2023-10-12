package com.upc.saveup.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("serviceConfiguration")
public class ServiceConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return (new OpenAPI())
                .components(new Components())
                .info(new Info()
                        .title("SaveUp API")
                        .description("SaveUp API Documentation"));
    }
}

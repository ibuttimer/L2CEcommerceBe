package com.ibuttimer.springecom.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("eCommerce API")
                        .description("API of the eCommerce application from Udemy course, 'Full Stack: Angular and Java Spring Boot' " +
                                     "Created by Chád Darby, Harinath Kuntamukkala. See " +
                                     "https://www.udemy.com/course/full-stack-angular-spring-boot-tutorial/")
                        .version(appVersion)
                        .contact(new Contact().name("Ian Buttimer"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}

package com.ibuttimer.springecom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.data.rest.base-path}")
    String apiBasePath;
    @Value("${allowed.origins}")
    String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // set the global CORS config for controllers
        // Note: CORS for REST repositories needs to be set in DataRestConfig.configureRepositoryRestConfiguration()
        addCorsMappings(registry, apiBasePath, allowedOrigins);
    }

    public static void addCorsMappings(CorsRegistry registry, String apiBasePath, String[] allowedOrigins) {
        // generate Ant-style pattern for everything below the base path
        String pathPattern = String.format("%s%s", apiBasePath, apiBasePath.endsWith("/") ? "**" : "/**");
        registry.addMapping(pathPattern).allowedOrigins(allowedOrigins);
    }
}

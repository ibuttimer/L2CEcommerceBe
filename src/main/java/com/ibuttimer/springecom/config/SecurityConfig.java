package com.ibuttimer.springecom.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.Arrays;

import static com.ibuttimer.springecom.config.Config.API_TYPES;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Value("${spring.data.rest.base-path}")
    String apiBasePath;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // disable cross site request forgery since not using cookies for session tracking
        // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#servlet-csrf-configure-disable
        http.csrf(AbstractHttpConfigurer::disable);

        /* https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeHttpRequests()
            https://docs.spring.io/spring-security/reference/migration/servlet/authorization.html
            https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html */
        // protect endpoints at /api/<type>/..
        http.authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/**").permitAll()
                                .requestMatchers(
                                        Arrays.stream(API_TYPES)
                                                .map(type -> apiBasePath + type + "/**")
                                                .toArray(String[]::new)
                                ).authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()));

        // Add CORS filters
        http.cors(withDefaults());

        // Add content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy());

        // Force a non-empty response body for 401's to make the response friendly
        Okta.configureResourceServer401ResponseBody(http);

        return http.build();
    }
}

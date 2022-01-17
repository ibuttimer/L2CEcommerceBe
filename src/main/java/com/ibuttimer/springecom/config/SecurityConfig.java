package com.ibuttimer.springecom.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.ibuttimer.springecom.config.Config.ORDER_URL;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.data.rest.base-path}")
    String apiBasePath;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

        // generate Ant-style pattern for everything below the base path
        String pathPattern = String.format(
                "%s%s%s",
                apiBasePath,
                apiBasePath.endsWith("/") ? ORDER_URL.substring(1) : ORDER_URL,
                ORDER_URL.endsWith("/") ? "**" : "/**"
        );

        // protect orders endpoint
        http.authorizeRequests()
                .antMatchers(pathPattern)
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();

        // add cors filters
        http.cors();

        // set 401 response body
        Okta.configureResourceServer401ResponseBody(http);

        // disable CSRF since not using cookies for session tracking
        http.csrf().disable();
    }
}

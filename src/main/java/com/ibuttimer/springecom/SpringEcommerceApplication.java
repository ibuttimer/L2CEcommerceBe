package com.ibuttimer.springecom;

import com.ibuttimer.springecom.config.Config;
import com.ibuttimer.springecom.config.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Arrays;

@SpringBootApplication
public class SpringEcommerceApplication {

	@Value("${spring.data.rest.base-path}")
	String apiBasePath;
	@Value("${allowed.origins}")
	String[] allowedOrigins;

	private static final Logger logger = LoggerFactory.getLogger(SpringEcommerceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringEcommerceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ApplicationArguments args,
						   Environment env,
						   @Value("${spring.data.rest.base-path}") String apiBasePath) {
		return runner -> {
			logger.info(
					String.format("%nApplication starting with command-line arguments: %s.%n" +
							"To kill this application, press Ctrl + C.", Arrays.toString(args.getSourceArgs())));

			Config.getInstance().setApiBasePath(apiBasePath);
		};
	}
}

package com.ibuttimer.springecom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest
class SpringEcommerceApplicationTests {

	@MockBean
	private JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
	}

}

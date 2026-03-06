package com.nexora.subscription;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestPostgresContainer.class)
class NexoraSubscriptionServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}

package com.glop.catalogue;

import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class CatalogueApplicationTests {

	@Test
	void contextLoads() {
	}

}

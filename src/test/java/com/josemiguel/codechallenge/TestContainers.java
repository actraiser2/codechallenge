package com.josemiguel.codechallenge;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:14.7://localhost:5432/codechallenge", 
		"spring.batch.jdbc.initialize-schema=never"})
public class TestContainers {

	@Test
	public void testSomethingWithTestContainers() {
		Assertions.assertTrue(true, () -> "true == true");
	}
}

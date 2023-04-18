package com.josemiguel.codechallenge;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.apache.camel.util.function.ThrowingFunction;
import org.checkerframework.checker.units.qual.t;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionDTO;
import com.josemiguel.codechallenge.infrastructure.errors.dto.ErrorDTO;

/*@SpringBootTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:14.7://localhost:5432/codechallenge", 
		"spring.batch.jdbc.initialize-schema=never"})*/
public class TestContainers {

	@Test
	public void testSomethingWithTestContainers() throws IOException {
		
		var t1 = TransactionDTO.builder().amount(10d).
				date(ZonedDateTime.now()).
				fee(new BigDecimal(3)).reference("1").build();
		
		var t2 = TransactionDTO.builder().amount(20d).
				date(ZonedDateTime.now()).
				fee(new BigDecimal(3)).reference("1").build();
		
		var t3 = TransactionDTO.builder().amount(30d).
				date(ZonedDateTime.now()).
				fee(new BigDecimal(3)).reference("2").build();
		
		
		
		Assertions.assertFalse(t1 == t2);
	
	}
}

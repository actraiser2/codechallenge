package com.josemiguel.codechallenge;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.infrastructure.utils.Constants;

//@SpringBootTest
public class FunctionalsTests {

	@Test
	public void testReduce() throws IOException {
		var list = List.of(
				new CreateAccountCommand("Cuenta Sabadell", 
						"ES1234563333", "Jose Miguel Besada juez", 50),
				new CreateAccountCommand("Cuenta Abierta", 
						"ES3345454453333", "Jesus besada", 49),
				new CreateAccountCommand("Cuenta Naranja", 
						"ES1234563333", "Andres Romrelaes", 48),
				new CreateAccountCommand("Cuenta Naranja", 
						"ES1234563333", "Andres Romrelaes", 39));
		var arrayOrdered = list.stream().
			sorted(Comparator.comparing(CreateAccountCommand::holderName).
					thenComparing(CreateAccountCommand::accountName)).
			collect(Collectors.groupingBy(CreateAccountCommand::iban));
		
		var avg = list.stream().mapToInt(CreateAccountCommand::age).average().orElse(0);

		var sum = list.stream().mapToInt(CreateAccountCommand::age).sum();

		
		System.out.println("Avg1:" + avg);
		
		System.out.println("Avg2:" + sum  / (double)list.size());

		System.out.println("Sum:" + sum);
		
		var summarizing = list.stream().collect(Collectors.summarizingDouble(CreateAccountCommand::age));
		
		var counting = list.stream().collect(Collectors.groupingBy(CreateAccountCommand::iban, 
				Collectors.counting()));
		
		
		
		System.out.println("counting:" + counting);
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			String a = null;
			a.chars();
		}, () -> "Tiene que saltar una excepcion");
		
		Assertions.assertAll(
				() -> Assertions.assertNotNull(list),
				() -> Assertions.assertTrue(sum > avg)
		);
		
	}
	
	@Test
	public void testDelegate() throws IOException {
		BiFunction<String, Integer, Integer> myFunction = Constants::calculateLength;
		
		var result = myFunction.apply("Josemi", 5);
		
		
		
		URL url = new URL("https://mytriplea.com");
		var inputStream = url.openStream();
		
		long[] cont = {0};
		try(var scanner = new Scanner(inputStream)){
			cont[0] = scanner.findAll(".*cat.*").count();
		};
		
		Double result2 = Optional.ofNullable(result).map(r -> Math.pow(r,  2)).
				map(Double::valueOf).
				orElseGet(() -> 0d);
		
		System.out.println("calculateLength:" + result2);
		
		System.out.println("Total Elements from the scanner:" + cont[0]);
		
	}
}

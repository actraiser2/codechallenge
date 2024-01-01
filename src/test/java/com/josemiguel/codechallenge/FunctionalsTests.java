package com.josemiguel.codechallenge;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;

//@SpringBootTest
public class FunctionalsTests
 {

	@Test
	@Disabled
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
	public void testPrimeNumber() throws IOException {
		var stopwatch = new StopWatch();
		stopwatch.start();
		
		var joined = LongStream.iterate(2, i -> {
			return ++i;
		}).
		//parallel().
		filter(FunctionalsTests::isPrime).
		limit(100).
		mapToObj(p -> p + ":" + factorial(p)).
		collect(Collectors.joining(","));
		
		stopwatch.stop();
		
		var maxValue = LongStream.iterate(5, seed -> (long)Math.pow(seed, 2)).
			limit(1000).
			sorted();

		
		System.out.println("The process lasted:" + stopwatch.getTotalTimeSeconds());
		
		System.out.println("MaxValue:" + maxValue.sum());
	}
	
	@Test
	public void testFindMaxValue() {
		var list = List.of(
				new CreateAccountCommand("Cuenta Sabadell", 
						"ES1234563333", "Jose Miguel Besada juez", 50),
				new CreateAccountCommand("Cuenta Abierta", 
						"ES3345454453333", "Jesus besada", 49),
				new CreateAccountCommand("Cuenta Naranja", 
						"ES1234563333", "Andres Romrelaes", 48),
				new CreateAccountCommand("Cuenta Naranja", 
						"ES1234563333", "Andres Romrelaes", 39));
		

		
		BinaryOperator<CreateAccountCommand> maxF = (c1, c2) -> {
			if (c1.age() >= c2.age()) return c1;
			else return c2;
		};
		
		Optional<CreateAccountCommand> maxCreateAccountCommand = list.stream().parallel().
				map(c -> {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return c;
				}).
				reduce(maxF );
		
		System.out.println("MaxCommand:" + maxCreateAccountCommand.orElseGet(() -> null
				));
	}
	
	public static boolean isPrime(Long number) {
		boolean isPrime = LongStream.rangeClosed(2, (long)Math.sqrt(number)).
				noneMatch(divisor -> number % divisor == 0);
		
		return isPrime;
	}
	
	public static double factorial(Long number) {
		var result = LongStream.rangeClosed(2, number).
			reduce(1, (i, acc) -> i*acc);
		
		return result;
	}
}

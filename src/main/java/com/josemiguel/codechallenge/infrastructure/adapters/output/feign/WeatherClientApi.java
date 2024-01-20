package com.josemiguel.codechallenge.infrastructure.adapters.output.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="weather", url = "${weather.url}")
public interface WeatherClientApi {

	@GetMapping("/forecast")
	public Map<String, Object> forecast(@RequestParam double longitude, 
			@RequestParam double latitude, @RequestParam String current,
			@RequestParam String hourly);
}

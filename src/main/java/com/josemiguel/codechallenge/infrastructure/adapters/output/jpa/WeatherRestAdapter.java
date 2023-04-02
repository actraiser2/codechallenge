package com.josemiguel.codechallenge.infrastructure.adapters.output.jpa;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.josemiguel.codechallenge.application.ports.input.WeatherProxy;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.Weather;
import com.josemiguel.codechallenge.infrastructure.config.properties.WeatherConfigProperties;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.WeatherException;

import lombok.AllArgsConstructor;
import static io.restassured.RestAssured.*;

@Component
@AllArgsConstructor
public class WeatherRestAdapter implements WeatherProxy {

	private WeatherConfigProperties weatherConfigProps;
	
	@Override
	public Weather getWeather(String city) throws WeatherException {
		// TODO Auto-generated method stub
		try {
			return given().
				param("q", city).
				param("appid", weatherConfigProps.getApiKey()).
				param("units", "metric").
				log().all().
			when().
				get(weatherConfigProps.getUrl() + "/data/2.5/weather").
			then().
				log().all().
			extract().
				as(Weather.class);
		}
		catch(Exception ex) {
			throw new WeatherException(ex.getMessage());
		}
		
	}

	@Override
	public Weather getWeather(Double lat, Double lon) throws WeatherException {
		// TODO Auto-generated method stub
		var restTemplate = new RestTemplate();
		
		var response = restTemplate.exchange(weatherConfigProps.getUrl() + 
				"/data/2.5/weather?appid={appid}&lat={lat}&lon={lon}&units=metric",
				HttpMethod.GET, null, Weather.class, 
				weatherConfigProps.getApiKey(), lat, lon);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		else {
			throw new WeatherException(response.toString());
		}
	}
	
	

}

package com.josemiguel.codechallenge.infrastructure.adapters.output.jpa;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

}

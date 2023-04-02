package com.josemiguel.codechallenge.application.ports.input;

import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.Weather;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.WeatherException;

public interface WeatherProxy {

	public Weather getWeather(String city) throws WeatherException;
}

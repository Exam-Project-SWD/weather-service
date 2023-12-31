package org.example.weatherservice.model;

import java.util.List;

public record BadWeatherReport(int customerId, List<WeatherInformation.Weather> weather) {
}

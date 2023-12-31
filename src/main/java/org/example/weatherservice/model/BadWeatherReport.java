package org.example.weatherservice.model;

public record BadWeatherReport(int customerId, WeatherInformation.Weather weather) {
}

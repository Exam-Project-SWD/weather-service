package org.example.weatherservice.model;

import org.example.weatherservice.enums.BadWeatherType;

public record WeatherCause(BadWeatherType type, Weather.Condition details) implements Cause {
}

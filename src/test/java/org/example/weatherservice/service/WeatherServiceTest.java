package org.example.weatherservice.service;

import org.example.weatherservice.enums.BadWeatherType;
import org.example.weatherservice.model.Cause;
import org.example.weatherservice.model.Weather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// There's an annotation called @RestClientTest that can be used to test REST clients.
// But it seems to be made for RestTemplateBuilder and RestClient.Builder, not RestClient itself.
// Long story short, I couldn't get it to work, so I just used @SpringBootTest instead and mock the dependency.
@SpringBootTest(classes = WeatherService.class)
class WeatherServiceTest {
    @Autowired
    private WeatherService weatherService;

    @MockBean
    private RestClient restClient;

    @Test
    void givenBadWeather_whenCheckingForBadWeather_thenReturnTrue() {
        // Arrange
        Weather.Condition badWeather = new Weather.Condition(202, "Thunderstorm", " thunderstorm with heavy rain ", "11d");

        // Act
        Optional<Cause> weatherCause = weatherService.getWeatherCause(badWeather);

        // Assert
        assertTrue(weatherCause.isPresent());
        assertEquals(BadWeatherType.THUNDERSTORM, weatherCause.get().type());
    }

    @Test
    void givenGoodWeather_whenCheckingForBadWeather_thenReturnFalse() {
        // Arrange
        Weather.Condition goodWeather = new Weather.Condition(800, "Clear", "clear sky", "01d");

        // Act
        Optional<Cause> weatherCause = weatherService.getWeatherCause(goodWeather);

        // Assert
        assertTrue(weatherCause.isEmpty());
    }

    @Test
    void givenNotBadEnoughWeather_whenCheckingForBadWeather_thenReturnFalse() {
        // Arrange
        Weather.Condition notBadEnoughWeather = new Weather.Condition(301, "Drizzle", "drizzle", "09d");

        // Act
        Optional<Cause> weatherCause = weatherService.getWeatherCause(notBadEnoughWeather);

        // Assert
        assertTrue(weatherCause.isEmpty());
    }

    @Test
    void givenBadWeather_whenGetBadWeather_thenReturnBadWeatherElements() {
        // Arrange
        List<Weather.Condition> weatherList = List.of(
                new Weather.Condition(202, "Thunderstorm", " thunderstorm with heavy rain ", "11d"),
                new Weather.Condition(800, "Clear", "clear sky", "01d"),
                new Weather.Condition(301, "Drizzle", "drizzle", "09d")
        );

        // Act
        List<Cause> badWeather = weatherService.getBadWeather(new Weather(null, weatherList, null, 0, null, null, null, null));

        // Assert
        assertEquals(1, badWeather.size());
    }


    @Test
    void givenMultipleBadWeather_whenGetBadWeather_thenReturnAll() {
        // Arrange
        List<Weather.Condition> weatherList = List.of(
                new Weather.Condition(202, "Thunderstorm", " thunderstorm with heavy rain ", "11d"),
                new Weather.Condition(800, "Clear", "clear sky", "01d"),
                new Weather.Condition(502, "Rain", "heavy intensity rain", "10d")
        );

        // Act
        List<Cause> badWeather = weatherService.getBadWeather(new Weather(null, weatherList, null, 0, null, null, null, null));

        // Assert
        assertEquals(2, badWeather.size());
    }

    @Test
    void givenGoodWeather_whenGetBadWeather_thenReturnEmptyList() {
        // Arrange
        List<Weather.Condition> weatherList = List.of(
                new Weather.Condition(800, "Clear", "clear sky", "01d"),
                new Weather.Condition(301, "Drizzle", "drizzle", "09d")
        );

        // Act
        List<Cause> badWeather = weatherService.getBadWeather(new Weather(null, weatherList, null, 0, null, null, null, null));

        // Assert
        assertTrue(badWeather.isEmpty());
    }
}
package org.example.weatherservice.service;

import org.example.weatherservice.model.WeatherInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        WeatherInformation.Weather badWeather = new WeatherInformation.Weather(202, "Thunderstorm", " thunderstorm with heavy rain ", "11d");

        // Act
        boolean isBadWeather = weatherService.isBadWeather(badWeather);

        // Assert
        assertTrue(isBadWeather);
    }

    @Test
    void givenGoodWeather_whenCheckingForBadWeather_thenReturnFalse() {
        // Arrange
        WeatherInformation.Weather goodWeather = new WeatherInformation.Weather(800, "Clear", "clear sky", "01d");

        // Act
        boolean isBadWeather = weatherService.isBadWeather(goodWeather);

        // Assert
        assertFalse(isBadWeather);
    }

    @Test
    void givenNotBadEnoughWeather_whenCheckingForBadWeather_thenReturnFalse() {
        // Arrange
        WeatherInformation.Weather notBadEnoughWeather = new WeatherInformation.Weather(301, "Drizzle", "drizzle", "09d");

        // Act
        boolean isBadWeather = weatherService.isBadWeather(notBadEnoughWeather);

        // Assert
        assertFalse(isBadWeather);
    }

    @Test
    void givenBadWeather_whenGetBadWeather_thenReturnBadWeatherElements() {
        // Arrange
        List<WeatherInformation.Weather> weatherList = List.of(
                new WeatherInformation.Weather(202, "Thunderstorm", " thunderstorm with heavy rain ", "11d"),
                new WeatherInformation.Weather(800, "Clear", "clear sky", "01d"),
                new WeatherInformation.Weather(301, "Drizzle", "drizzle", "09d")
        );

        // Act
        List<WeatherInformation.Weather> badWeather = weatherService.getBadWeather(new WeatherInformation(null, weatherList, null, 0, null, null, null, null));

        // Assert
        assertEquals(1, badWeather.size());
    }

    @Test
    void givenGoodWeather_whenGetBadWeather_thenReturnEmptyList() {
        // Arrange
        List<WeatherInformation.Weather> weatherList = List.of(
                new WeatherInformation.Weather(800, "Clear", "clear sky", "01d"),
                new WeatherInformation.Weather(301, "Drizzle", "drizzle", "09d")
        );

        // Act
        List<WeatherInformation.Weather> badWeather = weatherService.getBadWeather(new WeatherInformation(null, weatherList, null, 0, null, null, null, null));

        // Assert
        assertTrue(badWeather.isEmpty());
    }
}
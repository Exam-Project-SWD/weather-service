package org.example.weatherservice.service;

import org.example.weatherservice.config.Beans;
import org.example.weatherservice.model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {WeatherService.class, Beans.class})
class WeatherServiceIT {
    /**
     * Integration test with the external API.
     */
    @Autowired
    private WeatherService weatherService;

    @Test
    void givenZipAndCountry_whenGetLocation_thenReturnLocationInformation() {
        // Arrange
        String zipCode = "2800";
        String countryCode = "dk";

        // Act
        Location location = weatherService.getLocation(zipCode, countryCode);

        // Assert
        assertNotNull(location);
        assertEquals("Lyngby-Taarbæk Municipality", location.name());
        assertEquals(55.7704, location.lat());
        assertEquals(12.5038, location.lon());
    }
}
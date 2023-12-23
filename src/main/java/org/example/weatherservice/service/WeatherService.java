package org.example.weatherservice.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherservice.model.Location;
import org.example.weatherservice.model.WeatherInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class WeatherService {
    private final RestClient restClient;

    // Slightly annoying having to add the common API key to every request, but this is the simplest solution.
    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    public Location getLocation(String zipCode, String countryCode) {
        return restClient.get()
                .uri("geo/1.0/zip?zip={zip},{countryCode}&appid={apiKey}", zipCode, countryCode, apiKey)
                .retrieve()
                .body(Location.class);
    }

    public WeatherInformation getWeather(double lat, double lon) {
        return restClient.get()
                .uri("data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}&units=metric&mode=json", lat, lon, apiKey)
                .retrieve()
                .body(WeatherInformation.class);
    }
}

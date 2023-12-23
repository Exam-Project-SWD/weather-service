package org.example.weatherservice.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherservice.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class WeatherService {
    private final RestClient restClient;
    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    public Location getLocation(String zipCode, String countryCode) {
        return restClient.get()
                .uri("geo/1.0/zip?zip={zip},{countryCode}&appid={apiKey}", zipCode, countryCode, apiKey)
                .retrieve()
                .body(Location.class);
    }
}

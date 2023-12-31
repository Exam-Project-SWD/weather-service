package org.example.weatherservice.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherservice.model.Location;
import org.example.weatherservice.model.WeatherInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WeatherService {
    private final RestClient restClient;

    // Slightly annoying having to add the common API key to every request, but this is the simplest solution.
    // Adding default URI parameters to the Bean gets overwritten by the URI parameters in the request.
    // We could maybe use an interceptor to add the API key to every request.
    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    // TODO: Error handling

    // We can also use Nominatim to get the location from an address.
    // If the weather data is precise down to the actual coordinates, this would be nicer than just zip code.
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

    public List<WeatherInformation.Weather> getBadWeather(WeatherInformation weatherInformation) {
        // This checks the weather objects.
        // We could also check weather conditions in the main object like visibility or wind.
        // The return value and structure might need to change in that case.
        return weatherInformation.weather().stream().filter(this::isBadWeather).toList();
    }

    public boolean isBadWeather(WeatherInformation.Weather weather) {
        int id = weather.id();

        // check bands of weather codes and check severity
        if (id < 300) {
            return true;
        } else if (id < 400) {
            return id >= 312;
        } else if (id < 600) {
            return id >= 502;
        } else if (id < 700) {
            return id >= 601;
        } else return id < 800;
    }
}

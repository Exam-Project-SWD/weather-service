package org.example.weatherservice.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherservice.enums.BadWeatherType;
import org.example.weatherservice.model.Cause;
import org.example.weatherservice.model.Location;
import org.example.weatherservice.model.Weather;
import org.example.weatherservice.model.WeatherCause;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

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
    // If using zip code, we should cache results to avoid unnecessary requests. Location of cities doesn't change often.
    public Location getLocation(String zipCode, String countryCode) {
        return restClient.get()
                .uri("geo/1.0/zip?zip={zip},{countryCode}&appid={apiKey}", zipCode, countryCode, apiKey)
                .retrieve()
                .body(Location.class);
    }

    public Weather getWeather(double lat, double lon) {
        return restClient.get()
                .uri("data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}&units=metric&mode=json", lat, lon, apiKey)
                .retrieve()
                .body(Weather.class);
    }

    public List<Cause> getBadWeather(Weather weather) {
        // This checks the weather objects.
        // We could also check weather conditions in the main object like visibility or wind.
        // The return value and structure might need to change in that case.
        return weather.weather().stream().map(this::isBadWeather).filter(Objects::nonNull).toList();
    }

    // Possibly rename
    // Not super elegant that it returns null if the weather is not bad.
    // But I needed a way to include the type of bad weather if it is bad.
    // So it couldn't just return a boolean.
    // Maybe we could use an Optional instead.
    public Cause isBadWeather(Weather.Condition weather) {
        int id = weather.id();

        // check bands of weather codes and check severity
        // TODO: Redo logic back to broad bands and return Optional
        if (id < 300) {
            return new WeatherCause(BadWeatherType.THUNDERSTORM, weather);
        } else if (id < 400 && id >= 312) {
            return new WeatherCause(BadWeatherType.RAIN, weather);
        } else if (id < 600 && id >= 502) {
            return new WeatherCause(BadWeatherType.RAIN, weather);
        } else if (id < 700 && id >= 601) {
            return new WeatherCause(BadWeatherType.SNOW, weather);
        } else if (id < 800) {
            return new WeatherCause(BadWeatherType.ATMOSPHERE, weather);
        } else return null;
    }
}

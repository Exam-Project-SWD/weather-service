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
import java.util.Optional;

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
        return weather.weather().stream()
                .map(this::getWeatherCause)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        // Can also be done without the second map if we get or else null in the first, then filter on Objects::nonNull.
        // Feels like that slightly defeats the purpose of Optional though.
    }

    // Returns empty Optional if weather is not bad.
    // I needed a way to include the type of bad weather if it is bad.
    // So it couldn't just return a boolean.
    public Optional<Cause> getWeatherCause(Weather.Condition weather) {
        int id = weather.id();

        // check bands of weather codes and check severity.
        // we use extra conditions inside the broader if to return early.
        if (id < 300) {
            return Optional.of(new WeatherCause(BadWeatherType.THUNDERSTORM, weather));
        } else if (id < 400) {
            return id >= 312 ? Optional.of(new WeatherCause(BadWeatherType.RAIN, weather)) : Optional.empty();
        } else if (id < 600) {
            return id >= 502 ? Optional.of(new WeatherCause(BadWeatherType.RAIN, weather)) : Optional.empty();
        } else if (id < 700) {
            return id >= 601 ? Optional.of(new WeatherCause(BadWeatherType.SNOW, weather)) : Optional.empty();
        } else if (id < 800) {
            return Optional.of(new WeatherCause(BadWeatherType.ATMOSPHERE, weather));
        } else return Optional.empty();
    }
}

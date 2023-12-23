package org.example.weatherservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class Beans {
    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.openweathermap.org/")
                .defaultUriVariables(
                        Map.of(
                                "appid", apiKey
                        )
                )
                .build();
    }
}

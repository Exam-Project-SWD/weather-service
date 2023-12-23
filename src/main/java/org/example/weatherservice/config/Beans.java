package org.example.weatherservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Beans {
    @Bean
    public RestClient restClient() {
        return RestClient.create("https://api.openweathermap.org");
    }
}

package org.example.weatherservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weatherservice.model.*;
import org.example.weatherservice.service.KafkaService;
import org.example.weatherservice.service.WeatherService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener {
    private final WeatherService weatherService;
    private final KafkaService kafkaService;

    @KafkaListener(topics = "NEW_ORDER_PLACED", containerFactory = "orderContainerFactory")
    public void processNewOrder(Order order) {
        log.info("Received message: {}", order);

        // TODO: This is temporary for demo purposes.
        // Uses "2800" as default zip code if no address is provided.
        // Also zip code should be a String.
        String zipCode = order.address() != null ? String.valueOf(order.address().postalCode()) : "2800";
        // Default country code is "dk" if no address is provided.
        // This is not a data field we currently provide, but probably should.
        String countryCode = order.address() != null && order.address().countryCode() != null ? order.address().countryCode() : "dk";

        Location location = weatherService.getLocation(zipCode, countryCode);
        Weather weather = weatherService.getWeather(location.lat(), location.lon());
        log.info("Weather information: {}", weather);

        List<Cause> badWeather = weatherService.getBadWeather(weather);
        log.info("Bad weather: {}", badWeather);
        if (!badWeather.isEmpty()) {
            kafkaService.sendBadWeatherReport(new BadWeatherReport(order.customerId(), badWeather));
        }
    }

}

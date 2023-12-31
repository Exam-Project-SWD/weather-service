package org.example.weatherservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weatherservice.model.Order;
import org.example.weatherservice.service.WeatherService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener {
    private final WeatherService weatherService;

    @KafkaListener(topics = "NEW_ORDER_PLACED", containerFactory = "orderContainerFactory", groupId = "weather-service")
    public void processNewOrder(Order order) {
        log.info("Received message: {}", order);
    }

}

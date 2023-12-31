package org.example.weatherservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weatherservice.model.BadWeatherReport;
import org.example.weatherservice.model.Order;
import org.example.weatherservice.model.WeatherInformation;
import org.example.weatherservice.service.KafkaService;
import org.example.weatherservice.service.WeatherService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener {
    private final WeatherService weatherService;
    private final KafkaService kafkaService;

    @KafkaListener(topics = "NEW_ORDER_PLACED", containerFactory = "orderContainerFactory", groupId = "weather-service")
    public void processNewOrder(Order order) {
        log.info("Received message: {}", order);
        kafkaService.sendBadWeatherReport(new BadWeatherReport(order.customerId(), new WeatherInformation.Weather(500, "Rain", "light rain", "10n")));
    }

}

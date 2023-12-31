package org.example.weatherservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weatherservice.enums.Topic;
import org.example.weatherservice.model.BadWeatherReport;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    private final KafkaTemplate<String, BadWeatherReport> kafkaTemplate;

    public void sendBadWeatherReport(BadWeatherReport badWeatherReport) {
        kafkaTemplate.send(Topic.BAD_WEATHER.getTopicName(), badWeatherReport);
        log.info("Sent bad weather report: {}", badWeatherReport);
    }
}

package org.example.weatherservice.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherservice.model.BadWeatherReport;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, BadWeatherReport> kafkaTemplate;
}

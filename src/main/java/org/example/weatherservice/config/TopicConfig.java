package org.example.weatherservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.weatherservice.enums.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
    @Bean
    public NewTopic orderTopic() {
        return new NewTopic(Topic.NEW_ORDER_PLACED.getTopicName(), 1, (short) 1);
    }

    @Bean
    public NewTopic badWeatherTopic() {
        return new NewTopic(Topic.BAD_WEATHER.getTopicName(), 1, (short) 1);
    }
}

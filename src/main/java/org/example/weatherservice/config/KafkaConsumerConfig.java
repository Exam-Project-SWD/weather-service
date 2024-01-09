package org.example.weatherservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.weatherservice.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    // We currently don't use type mapping because the other services don't.
    // Instead, we provide a custom deserializer for the value type.
    // Supposedly, we might need the ContainerFactory for this which is annoying.
    // Maybe there's a nicer way.

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> consumerConfig() {
        return Map.ofEntries(
                // Add bootstrap servers from config, so it can be changed without changing code
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
                Map.entry(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
                Map.entry(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class),
                Map.entry(ConsumerConfig.GROUP_ID_CONFIG, "weather-service"),
                Map.entry(ConsumerConfig.CLIENT_ID_CONFIG, "weather-service")
        );
    }

    @Bean
    public ConsumerFactory<String, Order> consumerFactory() {
        DefaultKafkaConsumerFactory<String, Order> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfig());
        consumerFactory.setValueDeserializer(new JsonDeserializer<>(Order.class, false));
        return consumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> orderContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Order> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(10_000L, 5L)));
        return containerFactory;
    }
}

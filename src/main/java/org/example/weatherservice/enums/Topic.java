package org.example.weatherservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Topic {
    NEW_ORDER_PLACED("NEW_ORDER_PLACED"),
    BAD_WEATHER("BAD_WEATHER");

    // Value for the topic name is in case we want to change the casing of the topics in the future, but keep enums in uppercase by convention
    private final String topicName;
}

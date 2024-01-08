package org.example.weatherservice.model;

import org.example.weatherservice.enums.BadWeatherType;

public interface Cause {
    BadWeatherType type();

    Object details();
}

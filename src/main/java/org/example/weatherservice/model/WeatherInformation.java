package org.example.weatherservice.model;

import java.util.List;

public record WeatherInformation(Coordinates coord, List<Weather> weather, Main main, int visibility, Wind wind,
                                 Clouds clouds, Rain rain, Snow snow) {
    public record Coordinates(double lon, double lat) {
    }

    public record Weather(int id, String main, String description, String icon) {
    }

    public record Main(double temp, double feels_like, double temp_min, double temp_max, int pressure, int humidity) {
    }

    public record Wind(double speed, int deg, double gust) {
    }

    public record Clouds(int all) {
    }

    public record Rain(double _1h, double _3h) {
    }

    public record Snow(double _1h, double _3h) {
    }
}

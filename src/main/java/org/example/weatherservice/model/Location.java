package org.example.weatherservice.model;

public record Location(String zip, String name, double lat, double lon, String country) {
}

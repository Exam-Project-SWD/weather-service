package org.example.weatherservice.model;

public record Order(int customerId, int restaurantId, boolean withDelivery, Address address) {
}

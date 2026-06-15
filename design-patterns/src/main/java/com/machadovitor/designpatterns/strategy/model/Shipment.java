package com.machadovitor.designpatterns.strategy.model;

import java.math.BigDecimal;

// what a strategy gets to look at when pricing a shipment
public record Shipment(Money orderTotal, BigDecimal weightKg, int distanceKm) {

    public Shipment {
        if (weightKg.signum() < 0) {
            throw new IllegalArgumentException("weightKg must not be negative: " + weightKg);
        }
        if (distanceKm < 0) {
            throw new IllegalArgumentException("distanceKm must not be negative: " + distanceKm);
        }
    }

    public static Shipment of(String orderTotal, String weightKg, int distanceKm) {
        return new Shipment(Money.of(orderTotal), new BigDecimal(weightKg), distanceKm);
    }
}

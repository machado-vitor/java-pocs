package com.machadovitor.designpatterns.strategy.checkout;

import com.machadovitor.designpatterns.strategy.model.Money;
import com.machadovitor.designpatterns.strategy.model.Shipment;
import com.machadovitor.designpatterns.strategy.shipping.ShippingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutTest {

    private static final Shipment SHIPMENT = Shipment.of("90.00", "3.5", 120); // $90, 3.5 kg, 120 km

    @Test
    void flatRateIgnoresWeightAndDistance() {
        Checkout checkout = new Checkout(ShippingStrategy.flatRate("9.99"));

        assertEquals(Money.of("9.99"), checkout.shippingFor(SHIPMENT));
        assertEquals(Money.of("99.99"), checkout.totalFor(SHIPMENT));
    }

    @Test
    void weightBasedChargesBasePlusPerKg() {
        Checkout checkout = new Checkout(ShippingStrategy.weightBased("2.00", "1.50"));

        // 2.00 + 1.50 * 3.5 = 7.25
        assertEquals(Money.of("7.25"), checkout.shippingFor(SHIPMENT));
    }

    @Test
    void distanceBasedChargesBasePlusPerKm() {
        Checkout checkout = new Checkout(ShippingStrategy.distanceBased("1.00", "0.05"));

        // 1.00 + 0.05 * 120 = 7.00
        assertEquals(Money.of("7.00"), checkout.shippingFor(SHIPMENT));
    }

    @Test
    void freeOverThresholdFallsBackBelowThreshold() {
        Checkout checkout = new Checkout(ShippingStrategy.freeOver("100.00", ShippingStrategy.flatRate("9.99")));

        // order is $90, below $100, so the wrapped flat rate applies
        assertEquals(Money.of("9.99"), checkout.shippingFor(SHIPMENT));
    }

    @Test
    void freeOverThresholdIsFreeAtOrAboveThreshold() {
        Checkout checkout = new Checkout(ShippingStrategy.freeOver("90.00", ShippingStrategy.flatRate("9.99")));

        // order is exactly $90, so shipping is free
        assertTrue(checkout.shippingFor(SHIPMENT).isZero());
        assertEquals(SHIPMENT.orderTotal(), checkout.totalFor(SHIPMENT));
    }

    @Test
    void swappingStrategyAtRuntimeChangesTheQuote() {
        Checkout checkout = new Checkout(ShippingStrategy.flatRate("9.99"));
        assertEquals(Money.of("9.99"), checkout.shippingFor(SHIPMENT));

        checkout.useStrategy(ShippingStrategy.weightBased("2.00", "1.50"));
        assertEquals(Money.of("7.25"), checkout.shippingFor(SHIPMENT));
    }

    @Test
    void acceptsALambdaAsAStrategy() {
        //a lambda is a valid strategy too
        Checkout checkout = new Checkout(s -> s.orderTotal().times(new java.math.BigDecimal("0.05")));

        assertEquals(Money.of("4.50"), checkout.shippingFor(SHIPMENT)); // 5% of $90
    }
}

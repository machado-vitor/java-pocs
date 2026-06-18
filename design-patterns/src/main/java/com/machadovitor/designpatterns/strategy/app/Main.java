package com.machadovitor.designpatterns.strategy.app;

import com.machadovitor.designpatterns.strategy.checkout.Checkout;
import com.machadovitor.designpatterns.strategy.model.Shipment;
import com.machadovitor.designpatterns.strategy.shipping.ShippingStrategy;

import java.util.List;

public class Main {

    static void main() {
        // one shipment, priced under each strategy in turn
        Shipment shipment = Shipment.of("90.00", "3.5", 120); // $90, 3.5 kg, 120 km

        List<ShippingStrategy> strategies = List.of(
                ShippingStrategy.flatRate("9.99"),
                ShippingStrategy.weightBased("2.00", "1.50"),
                ShippingStrategy.distanceBased("1.00", "0.05"),
                ShippingStrategy.freeOver("100.00", ShippingStrategy.flatRate("9.99")),
                ShippingStrategy.freeOver("50.00", ShippingStrategy.flatRate("9.99")),
                // a plain lambda works too — here a flat 5% of the order
                s -> s.orderTotal().times(new java.math.BigDecimal("0.05")));

        Checkout checkout = new Checkout(strategies.getFirst());
        for (ShippingStrategy strategy : strategies) {
            checkout.useStrategy(strategy);
            System.out.printf(
                    "%-40s shipping %s, total %s%n",
                    strategy.name(), checkout.shippingFor(shipment), checkout.totalFor(shipment));
        }
    }
}

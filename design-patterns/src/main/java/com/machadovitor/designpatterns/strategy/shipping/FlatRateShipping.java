package com.machadovitor.designpatterns.strategy.shipping;

import com.machadovitor.designpatterns.strategy.model.Money;
import com.machadovitor.designpatterns.strategy.model.Shipment;

//   always the same fee, whatever the shipment looks like
public record FlatRateShipping(Money fee) implements ShippingStrategy {

    public FlatRateShipping {
        if (fee.amount().signum() < 0) {
            throw new IllegalArgumentException("fee must not be negative: " + fee);
        }
    }

    @Override
    public Money quote(Shipment shipment) {
        return fee;
    }

    @Override
    public String name() {
        return "flat " + fee;
    }
}

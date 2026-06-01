package com.machadovitor.tax.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void normalisesToTwoDecimalPlaces() {
        assertEquals(Money.of("10.00"), Money.of("10"));
        assertEquals(new BigDecimal("10.00"), Money.of("10").amount());
    }

    @Test
    void roundsHalfUp() {
        assertEquals(Money.of("0.13"), new Money(new BigDecimal("0.125")));
    }

    @Test
    void addsAmounts() {
        assertEquals(Money.of("3.00"), Money.of("1.50").plus(Money.of("1.50")));
    }

    @Test
    void multipliesByFactor() {
        assertEquals(Money.of("30.00"), Money.of("10.00").times(BigDecimal.valueOf(3)));
    }

    @Test
    void zeroIsZero() {
        assertTrue(Money.zero().isZero());
    }
}

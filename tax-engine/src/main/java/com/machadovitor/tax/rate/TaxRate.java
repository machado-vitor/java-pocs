package com.machadovitor.tax.rate;

import com.machadovitor.tax.model.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public sealed interface TaxRate permits TaxRate.Percentage, TaxRate.Exempt, TaxRate.Progressive { // Sealed to exactly Percentage, Exempt and Progressive
// the closed set means these are TaxRate's only implementations.
    // at compile time: javac knows the complete set, so it can:
    // - reject any unauthorized implement
    // at runtime: the permitted list is baked into the .class file as a PermittedSubclasses attribute.
    //The JVM re-checks it at class-load time: a class trying to implement a
    // sealed type without being on the list fails to load (IncompatibleClassChangeError). So the seal holds even against bytecode that never went through your compiler.

    // Tax owed on the given base amount, computed per rate kind.
    Money taxOn(Money base);

    record Percentage(BigDecimal percent) implements TaxRate {

        public Percentage {
            Objects.requireNonNull(percent, "percent must not be null");
            if (percent.signum() < 0) {
                throw new IllegalArgumentException("tax percent must not be negative: " + percent);
            }
        }

        @Override
        public Money taxOn(Money base) {
            return base.times(percent.movePointLeft(2));
        }

        @Override
        public String toString() {
            return percent.stripTrailingZeros().toPlainString() + "%";
        }
    }

    record Exempt() implements TaxRate {

        @Override
        public Money taxOn(Money base) {
            return Money.zero();
        }

        @Override
        public String toString() {
            return "exempt";
        }
    }

    record Progressive(List<Bracket> brackets) implements TaxRate {

        public Progressive {
            Objects.requireNonNull(brackets, "brackets must not be null");
            if (brackets.isEmpty()) {
                throw new IllegalArgumentException("brackets must not be empty");
            }
            brackets = List.copyOf(brackets);
            BigDecimal previous = null;
            for (Bracket bracket : brackets) {
                if (previous != null && bracket.lowerThreshold().compareTo(previous) <= 0) {
                    throw new IllegalArgumentException(
                            "brackets must be sorted by ascending threshold: " + brackets);
                }
                previous = bracket.lowerThreshold();
            }
        }

        @Override
        public Money taxOn(Money base) {
            BigDecimal amount = base.amount();
            BigDecimal tax = BigDecimal.ZERO;
            for (int i = 0; i < brackets.size(); i++) {
                Bracket bracket = brackets.get(i);
                if (amount.compareTo(bracket.lowerThreshold()) <= 0) {
                    break;
                }
                BigDecimal upper = (i + 1 < brackets.size())
                        ? brackets.get(i + 1).lowerThreshold().min(amount)
                        : amount;
                BigDecimal taxable = upper.subtract(bracket.lowerThreshold());
                tax = tax.add(taxable.multiply(bracket.percent().movePointLeft(2)));
            }
            return new Money(tax);
        }

        @Override
        public String toString() {
            return "progressive" + brackets;
        }

        public record Bracket(BigDecimal lowerThreshold, BigDecimal percent) {

            public Bracket {
                Objects.requireNonNull(lowerThreshold, "lowerThreshold must not be null");
                Objects.requireNonNull(percent, "percent must not be null");
                if (lowerThreshold.signum() < 0) {
                    throw new IllegalArgumentException("lowerThreshold must not be negative: " + lowerThreshold);
                }
                if (percent.signum() < 0) {
                    throw new IllegalArgumentException("percent must not be negative: " + percent);
                }
            }

            @Override
            public String toString() {
                return percent.stripTrailingZeros().toPlainString() + "%@"
                        + lowerThreshold.stripTrailingZeros().toPlainString();
            }
        }
    }

    static TaxRate percent(String percent) {
        return new Percentage(new BigDecimal(percent));
    }

    static TaxRate exempt() {
        return new Exempt();
    }

    static TaxRate progressive(Progressive.Bracket... brackets) {
        return new Progressive(List.of(brackets));
    }

    static Progressive.Bracket bracket(String fromAmount, String percent) {
        return new Progressive.Bracket(new BigDecimal(fromAmount), new BigDecimal(percent));
    }
}

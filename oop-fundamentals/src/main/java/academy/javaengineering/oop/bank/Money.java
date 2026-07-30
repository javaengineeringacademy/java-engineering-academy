/*
 * Copyright 2026 Java Engineering Academy contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package academy.javaengineering.oop.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Immutable money value object with currency-aware arithmetic.
 *
 * @param amount non-negative amount normalized to two decimal places
 * @param currency ISO currency for the amount
 */
public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {

    /**
     * Creates a validated money value.
     */
    public Money {
        amount = Objects.requireNonNull(amount, "amount").setScale(2, RoundingMode.HALF_EVEN);
        currency = Objects.requireNonNull(currency, "currency");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
    }

    /**
     * Creates money from decimal text and an ISO currency code.
     *
     * @param amount decimal amount text
     * @param currencyCode ISO currency code
     * @return validated money value
     */
    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(Text.require(amount, "amount")), Currency.getInstance(currencyCode));
    }

    /**
     * Creates zero money for the supplied currency.
     *
     * @param currency ISO currency
     * @return zero money
     */
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    /**
     * Adds money with the same currency.
     *
     * @param other money to add
     * @return summed money
     */
    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    /**
     * Subtracts money with the same currency.
     *
     * @param other money to subtract
     * @return remaining money
     */
    public Money minus(Money other) {
        requireSameCurrency(other);
        var result = amount.subtract(other.amount);
        if (result.signum() < 0) {
            throw new IllegalArgumentException("resulting amount must not be negative");
        }
        return new Money(result, currency);
    }

    /**
     * Multiplies the amount by a non-negative factor.
     *
     * @param factor multiplication factor
     * @return multiplied money
     */
    public Money multipliedBy(BigDecimal factor) {
        Objects.requireNonNull(factor, "factor");
        if (factor.signum() < 0) {
            throw new IllegalArgumentException("factor must not be negative");
        }
        return new Money(amount.multiply(factor), currency);
    }

    /**
     * Returns the smaller money value.
     *
     * @param other money to compare
     * @return the smaller value
     */
    public Money min(Money other) {
        return compareTo(other) <= 0 ? this : other;
    }

    /**
     * Returns true when this amount is zero.
     *
     * @return whether the amount is zero
     */
    public boolean isZero() {
        return amount.signum() == 0;
    }

    /**
     * Returns true when this money is greater than the other amount.
     *
     * @param other money to compare
     * @return whether this value is greater
     */
    public boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    /**
     * Returns true when this money is greater than or equal to the other amount.
     *
     * @param other money to compare
     * @return whether this value is greater than or equal
     */
    public boolean isGreaterThanOrEqualTo(Money other) {
        return compareTo(other) >= 0;
    }

    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount);
    }

    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " + amount.toPlainString();
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other");
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("currency mismatch");
        }
    }
}


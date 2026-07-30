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
import java.util.Objects;

/**
 * Account type for personal deposits that does not allow overdraft withdrawals.
 */
public final class SavingsAccount extends Account {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private final BigDecimal annualInterestRate;

    /**
     * Creates a savings account.
     *
     * @param accountNumber account number
     * @param owner account owner
     * @param openingBalance initial balance
     * @param annualInterestRate annual interest rate, such as 0.06 for six percent
     */
    public SavingsAccount(
            String accountNumber,
            Customer owner,
            Money openingBalance,
            BigDecimal annualInterestRate
    ) {
        super(accountNumber, owner, openingBalance);
        this.annualInterestRate = validateRate(annualInterestRate);
    }

    @Override
    public void withdraw(Money amount) {
        requirePositive(amount, "amount");
        if (!balance().isGreaterThanOrEqualTo(amount)) {
            throw new IllegalArgumentException("savings account cannot be overdrawn");
        }
        decreaseBalance(amount);
        record(TransactionType.WITHDRAWAL, amount, "Savings withdrawal");
    }

    @Override
    public String accountType() {
        return "SAVINGS";
    }

    /**
     * Applies one month of interest using the current balance.
     *
     * @return interest credited
     */
    public Money applyMonthlyInterest() {
        var monthlyRate = annualInterestRate.divide(MONTHS_IN_YEAR, 10, RoundingMode.HALF_EVEN);
        var interest = balance().multipliedBy(monthlyRate);
        if (!interest.isZero()) {
            increaseBalance(interest);
            record(TransactionType.INTEREST, interest, "Monthly savings interest");
        }
        return interest;
    }

    /**
     * Returns the configured annual interest rate.
     *
     * @return annual interest rate
     */
    public BigDecimal annualInterestRate() {
        return annualInterestRate;
    }

    private static BigDecimal validateRate(BigDecimal rate) {
        Objects.requireNonNull(rate, "annualInterestRate");
        if (rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("annualInterestRate must be between 0 and 1");
        }
        return rate;
    }
}


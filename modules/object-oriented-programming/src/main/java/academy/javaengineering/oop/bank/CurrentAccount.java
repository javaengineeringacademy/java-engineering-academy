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

import java.util.Objects;

/**
 * Account type for business operations with an approved overdraft facility.
 */
public final class CurrentAccount extends Account {

    private final Money overdraftLimit;
    private Money overdraftUsed;

    /**
     * Creates a current account.
     *
     * @param accountNumber account number
     * @param owner account owner
     * @param openingBalance initial balance
     * @param overdraftLimit approved overdraft limit
     */
    public CurrentAccount(String accountNumber, Customer owner, Money openingBalance, Money overdraftLimit) {
        super(accountNumber, owner, openingBalance);
        this.overdraftLimit = Objects.requireNonNull(overdraftLimit, "overdraftLimit");
        this.overdraftUsed = Money.zero(overdraftLimit.currency());
    }

    @Override
    public void deposit(Money amount) {
        requirePositive(amount, "amount");
        if (overdraftUsed.isZero()) {
            super.deposit(amount);
            return;
        }

        var overdraftRepayment = amount.min(overdraftUsed);
        overdraftUsed = overdraftUsed.minus(overdraftRepayment);
        var remainingDeposit = amount.minus(overdraftRepayment);
        if (!remainingDeposit.isZero()) {
            increaseBalance(remainingDeposit);
        }
        record(TransactionType.DEPOSIT, amount, "Current account deposit");
    }

    @Override
    public void withdraw(Money amount) {
        requirePositive(amount, "amount");
        if (balance().isGreaterThanOrEqualTo(amount)) {
            decreaseBalance(amount);
            record(TransactionType.WITHDRAWAL, amount, "Current account withdrawal");
            return;
        }

        var shortfall = amount.minus(balance());
        var remainingOverdraft = overdraftLimit.minus(overdraftUsed);
        if (shortfall.isGreaterThan(remainingOverdraft)) {
            throw new IllegalArgumentException("withdrawal exceeds approved overdraft");
        }

        setBalance(Money.zero(balance().currency()));
        overdraftUsed = overdraftUsed.plus(shortfall);
        record(TransactionType.WITHDRAWAL, amount, "Current account overdraft withdrawal");
    }

    @Override
    public String accountType() {
        return "CURRENT";
    }

    @Override
    public Money availableBalance() {
        return balance().plus(overdraftLimit.minus(overdraftUsed));
    }

    /**
     * Returns the approved overdraft limit.
     *
     * @return overdraft limit
     */
    public Money overdraftLimit() {
        return overdraftLimit;
    }

    /**
     * Returns the currently used overdraft amount.
     *
     * @return used overdraft
     */
    public Money overdraftUsed() {
        return overdraftUsed;
    }
}


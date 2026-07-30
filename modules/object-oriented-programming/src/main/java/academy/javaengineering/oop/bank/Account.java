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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base account abstraction shared by concrete bank account types.
 */
public abstract class Account {

    private final String accountNumber;
    private final Customer owner;
    private final List<Transaction> transactionHistory;
    private Money balance;

    /**
     * Creates an account with an opening balance.
     *
     * @param accountNumber stable account number
     * @param owner account owner
     * @param openingBalance opening balance
     */
    protected Account(String accountNumber, Customer owner, Money openingBalance) {
        this.accountNumber = Text.require(accountNumber, "accountNumber");
        this.owner = Objects.requireNonNull(owner, "owner");
        this.balance = Objects.requireNonNull(openingBalance, "openingBalance");
        this.transactionHistory = new ArrayList<>();
        if (!openingBalance.isZero()) {
            record(TransactionType.DEPOSIT, openingBalance, "Opening balance");
        }
    }

    /**
     * Adds money to the account.
     *
     * @param amount deposit amount
     */
    public void deposit(Money amount) {
        requirePositive(amount, "amount");
        increaseBalance(amount);
        record(TransactionType.DEPOSIT, amount, "Cash deposit");
    }

    /**
     * Withdraws money according to the concrete account rules.
     *
     * @param amount withdrawal amount
     */
    public abstract void withdraw(Money amount);

    /**
     * Returns the business account type.
     *
     * @return account type
     */
    public abstract String accountType();

    /**
     * Returns the money available for card authorization or ATM withdrawal.
     *
     * @return available balance
     */
    public Money availableBalance() {
        return balance;
    }

    /**
     * Returns the current ledger balance.
     *
     * @return account balance
     */
    public final Money balance() {
        return balance;
    }

    /**
     * Returns the account number.
     *
     * @return account number
     */
    public final String accountNumber() {
        return accountNumber;
    }

    /**
     * Returns the account owner.
     *
     * @return account owner
     */
    public final Customer owner() {
        return owner;
    }

    /**
     * Returns an immutable snapshot of transactions.
     *
     * @return transaction history snapshot
     */
    public final List<Transaction> transactions() {
        return List.copyOf(transactionHistory);
    }

    protected final void increaseBalance(Money amount) {
        requirePositive(amount, "amount");
        balance = balance.plus(amount);
    }

    protected final void decreaseBalance(Money amount) {
        requirePositive(amount, "amount");
        balance = balance.minus(amount);
    }

    protected final void setBalance(Money newBalance) {
        balance = Objects.requireNonNull(newBalance, "newBalance");
    }

    protected final void record(TransactionType type, Money amount, String description) {
        transactionHistory.add(Transaction.create(type, amount, description));
    }

    protected static void requirePositive(Money amount, String fieldName) {
        Objects.requireNonNull(amount, fieldName);
        if (amount.isZero()) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero");
        }
    }
}


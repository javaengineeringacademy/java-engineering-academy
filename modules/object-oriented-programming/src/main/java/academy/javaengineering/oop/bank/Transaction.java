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

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable account transaction.
 *
 * @param transactionId stable transaction id
 * @param occurredAt event time
 * @param type transaction type
 * @param amount transaction amount
 * @param description human-readable description
 */
public record Transaction(
        UUID transactionId,
        Instant occurredAt,
        TransactionType type,
        Money amount,
        String description
) {

    /**
     * Creates a validated transaction.
     */
    public Transaction {
        transactionId = Objects.requireNonNull(transactionId, "transactionId");
        occurredAt = Objects.requireNonNull(occurredAt, "occurredAt");
        type = Objects.requireNonNull(type, "type");
        amount = Objects.requireNonNull(amount, "amount");
        description = Text.require(description, "description");
    }

    /**
     * Creates a transaction using generated infrastructure fields.
     *
     * @param type transaction type
     * @param amount transaction amount
     * @param description transaction description
     * @return transaction with generated id and current timestamp
     */
    public static Transaction create(TransactionType type, Money amount, String description) {
        return new Transaction(UUID.randomUUID(), Instant.now(), type, amount, description);
    }
}


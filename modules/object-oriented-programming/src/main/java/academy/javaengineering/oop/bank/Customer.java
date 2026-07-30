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

/**
 * Immutable customer identity used by accounts, cards, and loans.
 *
 * @param customerId stable customer identifier
 * @param fullName customer legal name
 * @param email contact email address
 */
public record Customer(String customerId, String fullName, String email) {

    /**
     * Creates a validated customer.
     */
    public Customer {
        customerId = Text.require(customerId, "customerId");
        fullName = Text.require(fullName, "fullName");
        email = Text.require(email, "email").toLowerCase();
        if (!email.contains("@")) {
            throw new IllegalArgumentException("email must contain @");
        }
    }
}


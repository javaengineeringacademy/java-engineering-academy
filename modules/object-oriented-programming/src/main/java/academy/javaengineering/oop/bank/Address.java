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
 * Immutable branch address.
 *
 * @param line1 street address
 * @param city city name
 * @param country country name
 */
public record Address(String line1, String city, String country) {

    /**
     * Creates a validated address.
     */
    public Address {
        line1 = Text.require(line1, "line1");
        city = Text.require(city, "city");
        country = Text.require(country, "country");
    }
}


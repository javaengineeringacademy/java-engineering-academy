package academy.javaengineering.testing.assertj.practices;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Exercise 1: Basic AssertJ Assertions
 *
 * Tasks:
 * 1. Assert integer values
 * 2. Assert string properties
 * 3. Assert boolean conditions
 * 4. Assert null/not-null
 */
class Exercise1BasicAssertions {

    @Test
    void shouldAssertIntegers() {
        // TODO: Assert 2 + 2 equals 4
        // TODO: Assert 10 is greater than 5
        // TODO: Assert number is between 1 and 100
    }

    @Test
    void shouldAssertStrings() {
        String greeting = "Hello, World!";
        // TODO: Assert contains "World"
        // TODO: Assert starts with "Hello"
        // TODO: Assert has length 13
    }

    @Test
    void shouldAssertCollections() {
        List<String> colors = List.of("red", "green", "blue");
        // TODO: Assert has size 3
        // TODO: Assert contains "red"
        // TODO: Assert does not contain "yellow"
    }
}

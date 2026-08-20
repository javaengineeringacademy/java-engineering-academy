package academy.javaengineering.testing.junit5.advanced.practices;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Dynamic Tests
 *
 * Tasks:
 * 1. Create a @TestFactory that generates multiplication table tests
 * 2. Generate tests for a list of string transformations
 * 3. Create dynamic tests from a data source
 */
class Exercise2DynamicTests {

    int multiply(int a, int b) { return a * b; }
    String capitalize(String s) { return s == null ? null : s.substring(0, 1).toUpperCase() + s.substring(1); }

    @TestFactory
    Collection<DynamicTest> multiplicationTable() {
        // TODO: Generate dynamic tests for multiplication
        // Each test should verify multiply(a, b) == expected
        List<int[]> data = List.of(
            new int[]{2, 3, 6},
            new int[]{4, 5, 20},
            new int[]{0, 10, 0}
        );
        return List.of();
    }

    @TestFactory
    Collection<DynamicTest> capitalizeTests() {
        // TODO: Generate dynamic tests for capitalize method
        return List.of();
    }
}

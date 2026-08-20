package academy.javaengineering.testing.coverage.practices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Achieving Coverage Targets
 *
 * Tasks:
 * 1. Write tests for all code paths
 * 2. Achieve 80%+ line coverage
 * 3. Achieve 70%+ branch coverage
 */
class Exercise1CoverageTargets {

    static class NumberClassifier {
        String classify(int number) {
            if (number < 0) return "NEGATIVE";
            if (number == 0) return "ZERO";
            if (number % 2 == 0) return "EVEN";
            return "ODD";
        }
    }

    // TODO: Write tests to achieve high coverage
    @Test
    void shouldClassifyNumbers() {
        NumberClassifier classifier = new NumberClassifier();
        // Add comprehensive tests
    }
}

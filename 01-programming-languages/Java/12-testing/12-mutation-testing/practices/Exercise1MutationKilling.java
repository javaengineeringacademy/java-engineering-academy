package academy.javaengineering.testing.mutation.practices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Killing Mutants
 *
 * Tasks:
 * 1. Write tests that kill boundary mutations
 * 2. Write tests that kill return value mutations
 * 3. Write tests that kill negation mutations
 */
class Exercise1MutationKilling {

    static class GradeCalculator {
        String getGrade(int score) {
            if (score >= 90) return "A";
            if (score >= 80) return "B";
            if (score >= 70) return "C";
            if (score >= 60) return "D";
            return "F";
        }
    }

    // TODO: Write tests that kill ALL mutants
    @Test
    void shouldReturnCorrectGrade() {
        GradeCalculator calc = new GradeCalculator();
        // Add comprehensive tests
    }
}

package academy.javaengineering.testing.mutation.practices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Improving Mutation Score
 *
 * Tasks:
 * 1. Identify weak tests
 * 2. Add missing assertions
 * 3. Test boundary conditions
 */
class Exercise2MutationScore {

    static class StringProcessor {
        String process(String input) {
            if (input == null) return "";
            if (input.isEmpty()) return "EMPTY";
            if (input.length() > 10) return input.toUpperCase();
            return input.toLowerCase();
        }
    }

    // These tests have gaps - improve them
    @Test
    void shouldProcessString() {
        StringProcessor processor = new StringProcessor();
        assertNotNull(processor.process("test"));
    }
}

package academy.javaengineering.testing.coverage.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeCoverageExamples {

    static class StringFormatter {
        String format(String input, boolean uppercase) {
            if (input == null) return "";
            if (input.isEmpty()) return "EMPTY";
            return uppercase ? input.toUpperCase() : input.toLowerCase();
        }
    }

    @Test
    void shouldFormatString() {
        StringFormatter formatter = new StringFormatter();
        assertEquals("", formatter.format(null, false));
        assertEquals("EMPTY", formatter.format("", false));
        assertEquals("HELLO", formatter.format("hello", true));
        assertEquals("hello", formatter.format("HELLO", false));
    }
}

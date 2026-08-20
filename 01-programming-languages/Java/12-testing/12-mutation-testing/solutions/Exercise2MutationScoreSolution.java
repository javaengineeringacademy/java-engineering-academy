package academy.javaengineering.testing.mutation.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Exercise2MutationScoreSolution {

    static class StringProcessor {
        String process(String input) {
            if (input == null) return "";
            if (input.isEmpty()) return "EMPTY";
            if (input.length() > 10) return input.toUpperCase();
            return input.toLowerCase();
        }
    }

    @Test
    void shouldProcessString() {
        StringProcessor processor = new StringProcessor();
        assertEquals("", processor.process(null));
        assertEquals("EMPTY", processor.process(""));
        assertEquals("hello world test", processor.process("Hello World Test"));
        assertEquals("short", processor.process("short"));
    }
}

package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    void testTextRequireNonNull() {
        assertThrows(NullPointerException.class, () -> Text.require(null, "field"));
    }

    @Test
    void testTextRequireNonBlank() {
        assertThrows(IllegalArgumentException.class, () -> Text.require("  ", "field"));
        assertThrows(IllegalArgumentException.class, () -> Text.require("", "field"));
    }

    @Test
    void testTextRequireValid() {
        String result = Text.require("  valid  ", "field");
        assertEquals("valid", result); // trimmed
    }

    @Test
    void testTextRequireMaxLength() {
        assertThrows(IllegalArgumentException.class, () -> Text.require("too long", "field", 5));
    }

    @Test
    void testTextRequireValidMaxLength() {
        String result = Text.require("valid", "field", 10);
        assertEquals("valid", result);
    }
}
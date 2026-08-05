import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Enum Tests")
class EnumTest {

    @Test
    @DisplayName("Enum has correct number of values")
    void valueCount() {
        assertEquals(7, Day.values().length);
        assertEquals(4, Season.values().length);
    }

    @Test
    @DisplayName("valueOf returns correct enum")
    void valueOf() {
        assertEquals(Day.MONDAY, Day.valueOf("MONDAY"));
        assertEquals(Season.SPRING, Season.valueOf("SPRING"));
    }

    @Test
    @DisplayName("Abbreviation getter works")
    void abbreviation() {
        assertEquals("Mon", Day.MONDAY.getAbbreviation());
        assertEquals("Sun", Day.SUNDAY.getAbbreviation());
    }

    @Test
    @DisplayName("isWeekend correctly identifies weekends")
    void isWeekend() {
        assertTrue(Day.SATURDAY.isWeekend());
        assertTrue(Day.SUNDAY.isWeekend());
        assertFalse(Day.MONDAY.isWeekend());
        assertFalse(Day.FRIDAY.isWeekend());
    }

    @Test
    @DisplayName("next() cycles through days")
    void next() {
        assertEquals(Day.TUESDAY, Day.MONDAY.next());
        assertEquals(Day.MONDAY, Day.SUNDAY.next());
    }

    @Test
    @DisplayName("Season isWarm works")
    void isWarm() {
        assertTrue(Season.SPRING.isWarm());
        assertTrue(Season.SUMMER.isWarm());
        assertFalse(Season.FALL.isWarm());
        assertFalse(Season.WINTER.isWarm());
    }

    @Test
    @DisplayName("Season description getter works")
    void description() {
        assertEquals("Hot", Season.SUMMER.getDescription());
        assertEquals("Cold", Season.WINTER.getDescription());
    }

    @Test
    @DisplayName("ordinal() returns position")
    void ordinal() {
        assertEquals(0, Day.MONDAY.ordinal());
        assertEquals(3, Season.WINTER.ordinal());
    }
}
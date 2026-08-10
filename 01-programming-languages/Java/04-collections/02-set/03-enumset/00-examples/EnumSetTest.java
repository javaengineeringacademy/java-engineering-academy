package set.enumset.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class EnumSetTest {

    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
    enum Color { RED, GREEN, BLUE, YELLOW }

    @Test
    void testAddAndSize() {
        EnumSet<Day> set = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY);
        assertEquals(3, set.size());
        set.add(Day.THURSDAY);
        assertEquals(4, set.size());
    }

    @Test
    void testRemove() {
        EnumSet<Day> set = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY);
        set.remove(Day.TUESDAY);
        assertEquals(2, set.size());
        assertFalse(set.contains(Day.TUESDAY));
    }

    @Test
    void testContains() {
        EnumSet<Day> set = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY);
        assertTrue(set.contains(Day.MONDAY));
        assertFalse(set.contains(Day.SUNDAY));
    }

    @Test
    void testIteration() {
        EnumSet<Day> set = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY);
        int count = 0;
        for (Day d : set) count++;
        assertEquals(3, count);
    }

    @Test
    void testEdgeCases() {
        EnumSet<Day> set = EnumSet.noneOf(Day.class);
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void testRange() {
        EnumSet<Day> set = EnumSet.range(Day.MONDAY, Day.FRIDAY);
        assertEquals(5, set.size());
        assertTrue(set.contains(Day.MONDAY));
        assertTrue(set.contains(Day.FRIDAY));
        assertFalse(set.contains(Day.SATURDAY));
    }

    @Test
    void testAllOf() {
        EnumSet<Day> set = EnumSet.allOf(Day.class);
        assertEquals(7, set.size());
    }

    @Test
    void testComplementOf() {
        EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
        EnumSet<Day> notWeekend = EnumSet.complementOf(weekend);
        assertEquals(5, notWeekend.size());
        assertFalse(notWeekend.contains(Day.SATURDAY));
        assertTrue(notWeekend.contains(Day.MONDAY));
    }

    @Test
    void testSetOperations() {
        EnumSet<Day> set1 = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY);
        EnumSet<Day> set2 = EnumSet.of(Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY);
        Set<Day> union = EnumSet.copyOf(set1);
        union.addAll(set2);
        assertEquals(5, union.size());
        Set<Day> intersection = EnumSet.copyOf(set1);
        intersection.retainAll(set2);
        assertEquals(1, intersection.size());
        assertTrue(intersection.contains(Day.WEDNESDAY));
    }
}

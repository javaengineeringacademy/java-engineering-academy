package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IteratorDemoTest {

    private List<String> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
    }

    @Nested
    @DisplayName("Basic Iterator Tests")
    class BasicIteratorTests {

        @Test
        @DisplayName("Should iterate forward through all elements")
        void testForwardIteration() {
            StringBuilder result = new StringBuilder();
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                result.append(it.next()).append(" ");
            }
            assertEquals("A B C D E ", result.toString());
        }

        @Test
        @DisplayName("Should remove elements during iteration")
        void testRemoveDuringIteration() {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().equals("C")) {
                    it.remove();
                }
            }
            assertEquals(4, list.size());
            assertFalse(list.contains("C"));
            assertEquals(List.of("A", "B", "D", "E"), list);
        }

        @Test
        @DisplayName("Should work with enhanced for loop")
        void testEnhancedForLoop() {
            List<String> collected = new ArrayList<>();
            for (String s : list) {
                collected.add(s);
            }
            assertEquals(list, collected);
        }
    }

    @Nested
    @DisplayName("ListIterator Tests")
    class ListIteratorTests {

        @Test
        @DisplayName("Should iterate forward with ListIterator")
        void testForwardListIterator() {
            ListIterator<String> it = list.listIterator();
            List<String> result = new ArrayList<>();
            while (it.hasNext()) {
                result.add(it.next());
            }
            assertEquals(list, result);
        }

        @Test
        @DisplayName("Should iterate backward with ListIterator")
        void testBackwardListIterator() {
            ListIterator<String> it = list.listIterator(list.size());
            List<String> result = new ArrayList<>();
            while (it.hasPrevious()) {
                result.add(it.previous());
            }
            assertEquals(List.of("E", "D", "C", "B", "A"), result);
        }

        @Test
        @DisplayName("Should replace elements with set()")
        void testReplaceWithSet() {
            ListIterator<String> it = list.listIterator();
            while (it.hasNext()) {
                it.set(it.next().toLowerCase());
            }
            assertEquals(List.of("a", "b", "c", "d", "e"), list);
        }

        @Test
        @DisplayName("Should add elements with add()")
        void testAddWithAdd() {
            ListIterator<String> it = list.listIterator();
            while (it.hasNext()) {
                String val = it.next();
                if (val.equals("C")) {
                    it.add("X");
                }
            }
            assertEquals(List.of("A", "B", "C", "X", "D", "E"), list);
        }
    }

    @Nested
    @DisplayName("Fail-Fast Behavior Tests")
    class FailFastTests {

        @Test
        @DisplayName("Should throw ConcurrentModificationException on direct remove")
        void testConcurrentModificationException() {
            assertThrows(ConcurrentModificationException.class, () -> {
                for (String s : list) {
                    if (s.equals("C")) {
                        list.remove(s);
                    }
                }
            });
        }

        @Test
        @DisplayName("Should not throw exception when using iterator remove")
        void testSafeRemovalWithIterator() {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().equals("C")) {
                    it.remove();
                }
            }
            assertFalse(list.contains("C"));
            assertEquals(4, list.size());
        }
    }
}

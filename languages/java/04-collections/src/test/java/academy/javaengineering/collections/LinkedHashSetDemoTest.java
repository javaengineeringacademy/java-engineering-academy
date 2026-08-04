package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkedHashSetDemoTest {

    private LinkedHashSet<String> linkedHashSet;

    @BeforeEach
    void setUp() {
        linkedHashSet = new LinkedHashSet<>();
    }

    @Nested
    @DisplayName("Insertion-Order Tests")
    class InsertionOrderTests {

        @Test
        @DisplayName("Should maintain insertion order")
        void testInsertionOrder() {
            linkedHashSet.add("Charlie");
            linkedHashSet.add("Alice");
            linkedHashSet.add("Bob");

            List<String> iterationOrder = new ArrayList<>();
            for (String s : linkedHashSet) {
                iterationOrder.add(s);
            }
            assertEquals(List.of("Charlie", "Alice", "Bob"), iterationOrder);
        }

        @Test
        @DisplayName("Should maintain order after re-insertion")
        void testReinsertionOrder() {
            linkedHashSet.add("A");
            linkedHashSet.add("B");
            linkedHashSet.add("C");

            // Remove and re-add moves it to end
            linkedHashSet.remove("A");
            linkedHashSet.add("A");

            List<String> result = new ArrayList<>(linkedHashSet);
            assertEquals(List.of("B", "C", "A"), result);
        }
    }

    @Nested
    @DisplayName("Basic Operations Tests")
    class BasicOperationsTests {

        @Test
        @DisplayName("Should add elements without duplicates")
        void testAddNoDuplicates() {
            assertTrue(linkedHashSet.add("A"));
            assertTrue(linkedHashSet.add("B"));
            assertFalse(linkedHashSet.add("A")); // Duplicate
            assertEquals(2, linkedHashSet.size());
        }

        @Test
        @DisplayName("Should remove element")
        void testRemove() {
            linkedHashSet.add("A");
            linkedHashSet.add("B");
            assertTrue(linkedHashSet.remove("A"));
            assertFalse(linkedHashSet.contains("A"));
            assertEquals(1, linkedHashSet.size());
        }

        @Test
        @DisplayName("Should check contains")
        void testContains() {
            linkedHashSet.add("X");
            assertTrue(linkedHashSet.contains("X"));
            assertFalse(linkedHashSet.contains("Y"));
        }

        @Test
        @DisplayName("Should clear all elements")
        void testClear() {
            linkedHashSet.add("A");
            linkedHashSet.add("B");
            linkedHashSet.clear();
            assertTrue(linkedHashSet.isEmpty());
            assertEquals(0, linkedHashSet.size());
        }
    }

    @Nested
    @DisplayName("Iteration Tests")
    class IterationTests {

        @Test
        @DisplayName("Should iterate in insertion order")
        void testIterationOrder() {
            linkedHashSet.add("First");
            linkedHashSet.add("Second");
            linkedHashSet.add("Third");

            Iterator<String> it = linkedHashSet.iterator();
            assertEquals("First", it.next());
            assertEquals("Second", it.next());
            assertEquals("Third", it.next());
        }

        @Test
        @DisplayName("Should safely remove during iteration")
        void testSafeRemovalDuringIteration() {
            linkedHashSet.add("A");
            linkedHashSet.add("B");
            linkedHashSet.add("C");

            Iterator<String> it = linkedHashSet.iterator();
            while (it.hasNext()) {
                if (it.next().equals("B")) {
                    it.remove();
                }
            }

            assertEquals(2, linkedHashSet.size());
            assertFalse(linkedHashSet.contains("B"));
        }
    }
}

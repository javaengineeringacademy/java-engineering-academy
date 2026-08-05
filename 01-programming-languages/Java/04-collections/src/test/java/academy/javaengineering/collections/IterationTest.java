package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IterationTest {

    private List<String> names;

    @BeforeEach
    void setUp() {
        names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana", "Eve"));
    }

    @Nested
    @DisplayName("Traditional For Loop Tests")
    class ForLoopTests {

        @Test
        @DisplayName("Should iterate all elements by index")
        void testForLoopIteration() {
            List<String> result = new ArrayList<>();
            for (int i = 0; i < names.size(); i++) {
                result.add(names.get(i));
            }
            assertEquals(names, result);
        }

        @Test
        @DisplayName("Should iterate in reverse order")
        void testReverseForLoop() {
            List<String> result = new ArrayList<>();
            for (int i = names.size() - 1; i >= 0; i--) {
                result.add(names.get(i));
            }
            assertEquals(List.of("Eve", "Diana", "Charlie", "Bob", "Alice"), result);
        }

        @Test
        @DisplayName("Should iterate with step")
        void testStepForLoop() {
            List<String> result = new ArrayList<>();
            for (int i = 0; i < names.size(); i += 2) {
                result.add(names.get(i));
            }
            assertEquals(List.of("Alice", "Charlie", "Eve"), result);
        }
    }

    @Nested
    @DisplayName("Enhanced For-Each Loop Tests")
    class ForEachLoopTests {

        @Test
        @DisplayName("Should iterate all elements")
        void testForEachIteration() {
            List<String> result = new ArrayList<>();
            for (String name : names) {
                result.add(name);
            }
            assertEquals(names, result);
        }

        @Test
        @DisplayName("Should work with Set")
        void testForEachWithSet() {
            Set<Integer> numbers = new LinkedHashSet<>(Set.of(10, 20, 30));
            int sum = 0;
            for (int num : numbers) {
                sum += num;
            }
            assertEquals(60, sum);
        }
    }

    @Nested
    @DisplayName("Lambda forEach Tests")
    class LambdaForEachTests {

        @Test
        @DisplayName("Should iterate with lambda")
        void testLambdaForEach() {
            List<String> result = new ArrayList<>();
            names.forEach(name -> result.add(name));
            assertEquals(names, result);
        }

        @Test
        @DisplayName("Should transform with lambda")
        void testLambdaTransform() {
            List<String> upper = new ArrayList<>();
            names.forEach(name -> upper.add(name.toUpperCase()));
            assertEquals(List.of("ALICE", "BOB", "CHARLIE", "DIANA", "EVE"), upper);
        }
    }

    @Nested
    @DisplayName("Method Reference Tests")
    class MethodRefTests {

        @Test
        @DisplayName("Should iterate with method reference")
        void testMethodReference() {
            List<String> result = new ArrayList<>();
            names.forEach(result::add);
            assertEquals(names, result);
        }

        @Test
        @DisplayName("Should uppercase with method reference")
        void testMethodReferenceUpperCase() {
            List<String> upper = new ArrayList<>();
            names.forEach(upper::add);
            names.forEach(n -> upper.set(upper.indexOf(n), n.toUpperCase()));
            assertEquals(List.of("ALICE", "BOB", "CHARLIE", "DIANA", "EVE"), upper);
        }
    }

    @Nested
    @DisplayName("Iterator Pattern Tests")
    class IteratorTests {

        @Test
        @DisplayName("Should iterate with Iterator")
        void testIteratorForward() {
            List<String> result = new ArrayList<>();
            Iterator<String> it = names.iterator();
            while (it.hasNext()) {
                result.add(it.next());
            }
            assertEquals(names, result);
        }

        @Test
        @DisplayName("Should remove elements with Iterator")
        void testIteratorRemove() {
            Iterator<String> it = names.iterator();
            while (it.hasNext()) {
                if (it.next().length() <= 3) {
                    it.remove();
                }
            }
            assertEquals(List.of("Alice", "Charlie", "Diana"), names);
        }

        @Test
        @DisplayName("Should iterate backward with ListIterator")
        void testListIteratorBackward() {
            List<String> result = new ArrayList<>();
            ListIterator<String> it = names.listIterator(names.size());
            while (it.hasPrevious()) {
                result.add(it.previous());
            }
            assertEquals(List.of("Eve", "Diana", "Charlie", "Bob", "Alice"), result);
        }

        @Test
        @DisplayName("Should replace elements with ListIterator set()")
        void testListIteratorSet() {
            ListIterator<String> it = names.listIterator();
            while (it.hasNext()) {
                it.set(it.next().toLowerCase());
            }
            assertEquals(List.of("alice", "bob", "charlie", "diana", "eve"), names);
        }
    }

    @Nested
    @DisplayName("Stream forEach Tests")
    class StreamTests {

        @Test
        @DisplayName("Should iterate with stream forEach")
        void testStreamForEach() {
            List<String> result = new ArrayList<>();
            names.stream().forEach(result::add);
            assertEquals(names, result);
        }

        @Test
        @DisplayName("Should filter and collect with stream")
        void testStreamFilterCollect() {
            List<String> result = names.stream()
                    .filter(name -> name.length() > 3)
                    .collect(Collectors.toList());
            assertEquals(List.of("Alice", "Charlie", "Diana"), result);
        }

        @Test
        @DisplayName("Should map and collect with stream")
        void testStreamMapCollect() {
            List<String> result = names.stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            assertEquals(List.of("ALICE", "BOB", "CHARLIE", "DIANA", "EVE"), result);
        }
    }

    @Nested
    @DisplayName("Recursion Tests")
    class RecursionTests {

        @Test
        @DisplayName("Should compute factorial recursively")
        void testFactorial() {
            assertEquals(1, factorial(0));
            assertEquals(1, factorial(1));
            assertEquals(120, factorial(5));
            assertEquals(3628800, factorial(10));
        }

        @Test
        @DisplayName("Should compute fibonacci recursively")
        void testFibonacci() {
            assertEquals(0, fibonacci(0));
            assertEquals(1, fibonacci(1));
            assertEquals(5, fibonacci(5));
            assertEquals(55, fibonacci(10));
        }

        private long factorial(int n) {
            if (n <= 1) return 1;
            return n * factorial(n - 1);
        }

        private long fibonacci(int n) {
            if (n <= 1) return n;
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    @Nested
    @DisplayName("Early Break Tests")
    class EarlyBreakTests {

        @Test
        @DisplayName("Should break from for loop")
        void testForLoopBreak() {
            String found = null;
            for (String name : names) {
                if (name.equals("Charlie")) {
                    found = name;
                    break;
                }
            }
            assertEquals("Charlie", found);
        }

        @Test
        @DisplayName("Should break from Iterator loop")
        void testIteratorBreak() {
            String found = null;
            Iterator<String> it = names.iterator();
            while (it.hasNext()) {
                if (it.next().equals("Diana")) {
                    found = "Diana";
                    break;
                }
            }
            assertEquals("Diana", found);
        }

        @Test
        @DisplayName("Should find with Stream findFirst")
        void testStreamFindFirst() {
            String found = names.stream()
                    .filter(name -> name.startsWith("D"))
                    .findFirst()
                    .orElse(null);
            assertEquals("Diana", found);
        }

        @Test
        @DisplayName("Should check match with Stream anyMatch")
        void testStreamAnyMatch() {
            boolean hasEve = names.stream().anyMatch(name -> name.equals("Eve"));
            boolean hasFrank = names.stream().anyMatch(name -> name.equals("Frank"));
            assertTrue(hasEve);
            assertFalse(hasFrank);
        }
    }
}

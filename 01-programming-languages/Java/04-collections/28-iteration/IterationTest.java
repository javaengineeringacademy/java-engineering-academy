import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class IterationTest {

    @Test
    void testForLoopIteration() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            result.add(names.get(i));
        }
        assertEquals(names, result);
    }

    @Test
    void testReverseForLoop() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = new ArrayList<>();
        for (int i = names.size() - 1; i >= 0; i--) {
            result.add(names.get(i));
        }
        assertEquals(List.of("Charlie", "Bob", "Alice"), result);
    }

    @Test
    void testForEachIteration() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = new ArrayList<>();
        for (String name : names) {
            result.add(name);
        }
        assertEquals(names, result);
    }

    @Test
    void testLambdaForEach() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = new ArrayList<>();
        names.forEach(name -> result.add(name));
        assertEquals(names, result);
    }

    @Test
    void testIteratorForward() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = new ArrayList<>();
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            result.add(it.next());
        }
        assertEquals(names, result);
    }

    @Test
    void testIteratorRemove() {
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana"));
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            if (it.next().length() <= 3) {
                it.remove();
            }
        }
        assertEquals(List.of("Alice", "Charlie", "Diana"), names);
    }

    @Test
    void testStreamForEach() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = new ArrayList<>();
        names.stream().forEach(result::add);
        assertEquals(names, result);
    }

    @Test
    void testStreamFilterCollect() {
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");
        List<String> result = names.stream()
                .filter(name -> name.length() > 3)
                .collect(java.util.stream.Collectors.toList());
        assertEquals(List.of("Alice", "Charlie", "Diana"), result);
    }

    @Test
    void testFactorial() {
        assertEquals(1, factorial(0));
        assertEquals(1, factorial(1));
        assertEquals(120, factorial(5));
    }

    @Test
    void testFibonacci() {
        assertEquals(0, fibonacci(0));
        assertEquals(1, fibonacci(1));
        assertEquals(5, fibonacci(5));
    }

    private long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    @Test
    void testForLoopBreak() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        String found = null;
        for (String name : names) {
            if (name.equals("Bob")) {
                found = name;
                break;
            }
        }
        assertEquals("Bob", found);
    }

    @Test
    void testStreamFindFirst() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        String found = names.stream()
                .filter(name -> name.startsWith("B"))
                .findFirst()
                .orElse(null);
        assertEquals("Bob", found);
    }
}

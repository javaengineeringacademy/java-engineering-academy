package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for try-with-resources examples.
 */
class TryWithResourcesTest {

    @Test
    void testAutoCloseableResourceCloses() {
        List<String> events = new ArrayList<>();

        try (var resource = new TestResource(events, "TestResource")) {
            events.add("working");
        }

        assertEquals(List.of("TestResource opened", "working", "TestResource closed"), events);
    }

    @Test
    void testMultipleResourcesCloseInReverseOrder() {
        List<String> events = new ArrayList<>();

        try (var resource1 = new TestResource(events, "First");
             var resource2 = new TestResource(events, "Second")) {
            events.add("working");
        }

        assertEquals(List.of("First opened", "Second opened", "working",
                "Second closed", "First closed"), events);
    }

    @Test
    void testResourceExceptionPropagation() {
        assertThrows(RuntimeException.class, () -> {
            try (var resource = new TestResource(new ArrayList<>(), "Test")) {
                throw new RuntimeException("Error in try block");
            }
        });
    }

    @Test
    void testSuppressedExceptions() {
        List<Throwable> suppressedExceptions = new ArrayList<>();

        try {
            try (var resource = new ExceptionOnCloseResource()) {
                throw new RuntimeException("Main exception");
            }
        } catch (RuntimeException e) {
            assertEquals("Main exception", e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                suppressedExceptions.add(suppressed);
            }
        }

        assertFalse(suppressedExceptions.isEmpty(), "Should have suppressed exceptions");
    }

    @Test
    void testCustomAutoCloseable() {
        boolean[] closed = {false};

        try (var resource = new AutoCloseable() {
            @Override
            public void close() {
                closed[0] = true;
            }
        }) {
            // Use resource
        }

        assertTrue(closed[0], "Resource should be closed");
    }

    /**
     * Test resource implementation.
     */
    private static class TestResource implements AutoCloseable {

        private final List<String> events;
        private final String name;

        TestResource(List<String> events, String name) {
            this.events = events;
            this.name = name;
            events.add(name + " opened");
        }

        @Override
        public void close() {
            events.add(name + " closed");
        }
    }

    /**
     * Test resource that throws exception on close.
     */
    private static class ExceptionOnCloseResource implements AutoCloseable {

        ExceptionOnCloseResource() {
            // Constructor
        }

        @Override
        public void close() {
            throw new RuntimeException("Close exception");
        }
    }
}

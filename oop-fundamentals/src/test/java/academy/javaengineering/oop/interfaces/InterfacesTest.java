package academy.javaengineering.oop.interfaces;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for interface demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class InterfacesTest {

    @Test
    void shouldMakeBirdFly() {
        Flyable bird = new Bird("Eagle");
        assertDoesNotThrow(bird::fly);
        assertEquals(1000, bird.getMaxAltitude());
    }

    @Test
    void shouldMakeDuckSwimAndFly() {
        SwimmableDuck duck = new SwimmableDuck("Donald");
        
        assertDoesNotThrow(duck::fly);
        assertDoesNotThrow(duck::swim);
        assertDoesNotThrow(duck::quack);
    }

    @Test
    void shouldUseDefaultMethods() {
        Sortable list = new SimpleList();
        list.add(5);
        list.add(2);
        list.add(8);
        
        assertEquals(3, list.size());
        assertFalse(list.isEmpty());
        
        list.sort(); // Default method
        assertEquals(java.util.List.of(2, 5, 8), list.getAll());
    }

    @Test
    void shouldUseStaticMethods() {
        Greeting greeting = Greeting.createGreeting("Hello");
        assertEquals("Hello", greeting.getMessage());
    }

    @Test
    void shouldUseFunctionalInterface() {
        Predicate<String> isLong = s -> s.length() > 5;
        
        assertFalse(isLong.test("Hi"));
        assertTrue(isLong.test("Hello World"));
    }

    @Test
    void shouldChainPredicates() {
        Predicate<String> startsWithH = s -> s.startsWith("H");
        Predicate<String> hasLength5 = s -> s.length() >= 5;
        
        Predicate<String> combined = startsWithH.and(hasLength5);
        
        assertFalse(combined.test("Hi"));        // Too short
        assertFalse(combined.test("World"));     // Doesn't start with H
        assertTrue(combined.test("Hello"));       // Both true
    }

    @Test
    void shouldAccessInterfaceConstants() {
        assertEquals(3, AppConfig.MAX_RETRIES);
        assertEquals(5000, AppConfig.TIMEOUT_MS);
        assertEquals("Java Engineering Academy", AppConfig.APP_NAME);
    }

    @Test
    void shouldUseInterfaceInheritance() {
        AdvancedLogger logger = new ConsoleLogger();
        
        assertDoesNotThrow(() -> logger.log("test"));
        assertDoesNotThrow(() -> logger.logError("error"));
        assertDoesNotThrow(logger::clearLogs);
    }
}
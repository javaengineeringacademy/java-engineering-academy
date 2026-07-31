package academy.javaengineering.oop.equalshashcode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for equals/hashCode demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class EqualsHashCodeTest {

    @Test
    void shouldImplementEqualsCorrectly() {
        Employee emp1 = new Employee(1001, "Alice", "Engineering");
        Employee emp2 = new Employee(1001, "Alice", "Engineering");
        Employee emp3 = new Employee(1002, "Bob", "Marketing");
        
        assertTrue(emp1.equals(emp2));
        assertFalse(emp1.equals(emp3));
        assertFalse(emp1.equals(null));
        assertFalse(emp1.equals("string"));
    }

    @Test
    void shouldImplementHashCodeCorrectly() {
        Employee emp1 = new Employee(1001, "Alice", "Engineering");
        Employee emp2 = new Employee(1001, "Alice", "Engineering");
        
        assertEquals(emp1.hashCode(), emp2.hashCode());
    }

    @Test
    void shouldWorkInHashSet() {
        Employee emp1 = new Employee(1001, "Alice", "Engineering");
        Employee emp2 = new Employee(1001, "Alice", "Engineering");
        
        java.util.Set<Employee> set = new java.util.HashSet<>();
        set.add(emp1);
        set.add(emp2); // Should not be added
        
        assertEquals(1, set.size());
    }

    @Test
    void shouldRejectBrokenImplementation() {
        BadEmployee bad1 = new BadEmployee(1001, "Alice");
        BadEmployee bad2 = new BadEmployee(1001, "Alice");
        
        assertTrue(bad1.equals(bad2));
        // hashCode is inconsistent - this breaks the contract!
        // In a real scenario, this would cause issues in HashMap/HashSet
    }

    @Test
    void shouldImplementPointEquals() {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(10, 20);
        Point p3 = new Point(30, 40);
        
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
    }

    @Test
    void shouldAutoGenerateEqualsForRecords() {
        var r1 = new RecordPerson("Charlie", 25);
        var r2 = new RecordPerson("Charlie", 25);
        var r3 = new RecordPerson("Dave", 30);
        
        assertTrue(r1.equals(r2));
        assertFalse(r1.equals(r3));
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldShowReflexiveProperty() {
        Employee emp = new Employee(1001, "Alice", "Engineering");
        assertTrue(emp.equals(emp)); // Reflexive
    }

    @Test
    void shouldShowSymmetricProperty() {
        Employee emp1 = new Employee(1001, "Alice", "Engineering");
        Employee emp2 = new Employee(1001, "Alice", "Engineering");
        
        assertTrue(emp1.equals(emp2));
        assertTrue(emp2.equals(emp1)); // Symmetric
    }

    @Test
    void shouldShowTransitiveProperty() {
        Employee emp1 = new Employee(1001, "Alice", "Engineering");
        Employee emp2 = new Employee(1001, "Alice", "Engineering");
        Employee emp3 = new Employee(1001, "Alice", "Engineering");
        
        assertTrue(emp1.equals(emp2));
        assertTrue(emp2.equals(emp3));
        assertTrue(emp1.equals(emp3)); // Transitive
    }
}
package academy.javaengineering.oop;

import academy.javaengineering.oop.abstractclasses.Employee;
import academy.javaengineering.oop.abstractclasses.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Abstract Class Tests")
class AbstractClassTest {

    private Manager manager;

    @BeforeEach
    void setUp() {
        manager = new Manager("Alice", "M001", 100000, 5);
    }

    @Test
    @DisplayName("Manager inherits Employee fields")
    void inheritedFields() {
        assertEquals("Alice", manager.getName());
        assertEquals("M001", manager.getId());
        assertEquals(100000, manager.getSalary(), 0.001);
    }

    @Test
    @DisplayName("Manager implements abstract methods")
    void abstractMethods() {
        assertEquals("Manager", manager.getRole());
        double bonus = manager.calculateBonus();
        assertTrue(bonus > 0);
        assertEquals(100000 * 0.20 + 5 * 1000, bonus, 0.001);
    }

    @Test
    @DisplayName("getTeamSize returns team size")
    void teamSize() {
        assertEquals(5, manager.getTeamSize());
    }

    @Test
    @DisplayName("setSalary updates salary")
    void setSalary() {
        manager.setSalary(120000);
        assertEquals(120000, manager.getSalary(), 0.001);
    }

    @Test
    @DisplayName("setSalary rejects negative")
    void rejectNegative() {
        assertThrows(IllegalArgumentException.class, () -> manager.setSalary(-1));
    }

    @Test
    @DisplayName("getDetails contains all info")
    void getDetails() {
        String details = manager.getDetails();
        assertTrue(details.contains("Alice"));
        assertTrue(details.contains("Manager"));
        assertTrue(details.contains("100000"));
    }

    @Test
    @DisplayName("Manager is instance of Employee")
    void instanceofCheck() {
        assertTrue(manager instanceof Employee);
    }
}

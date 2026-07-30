package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testEmployeeCreation() {
        Employee emp = new Employee("E001", "John Smith", "Manager", "MANAGER");
        
        assertEquals("E001", emp.employeeId());
        assertEquals("John Smith", emp.name());
        assertEquals("Manager", emp.role());
        assertEquals("MANAGER", emp.employeeType());
    }

    @Test
    void testEmployeeEqualsAndHashCode() {
        Employee e1 = new Employee("E001", "John", "Manager", "MANAGER");
        Employee e2 = new Employee("E001", "John", "Manager", "MANAGER");
        Employee e3 = new Employee("E002", "Jane", "Teller", "TELLER");
        
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1, e3);
    }
}
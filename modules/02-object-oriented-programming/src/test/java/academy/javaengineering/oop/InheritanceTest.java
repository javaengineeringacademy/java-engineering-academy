package academy.javaengineering.oop;

import academy.javaengineering.oop.`02-inheritance`.InheritanceExample.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Inheritance Tests")
class InheritanceTest {

    @Test
    @DisplayName("Employee base compensation")
    void employeeCompensation() {
        Employee emp = new Employee(1L, "John", 50000);
        assertEquals(50000, emp.calculateCompensation(), 0.001);
        assertEquals("EMPLOYEE", emp.getEmployeeType());
    }

    @Test
    @DisplayName("FullTimeEmployee includes bonus in compensation")
    void fullTimeEmployeeCompensation() {
        FullTimeEmployee fte = new FullTimeEmployee(2L, "Jane", 75000, 10000, 20);
        assertEquals(85000, fte.calculateCompensation(), 0.001);
        assertEquals("FULL_TIME", fte.getEmployeeType());
        assertEquals(20, fte.getVacationDays());
        assertEquals(10000, fte.getAnnualBonus(), 0.001);
    }

    @Test
    @DisplayName("Contractor annual compensation calculation")
    void contractorCompensation() {
        Contractor ctr = new Contractor(3L, "Bob", 95.00, 40, "PROJ-1");
        // 95 * 40 * 52 = 197,600
        assertEquals(197600, ctr.calculateCompensation(), 0.001);
        assertEquals("CONTRACTOR", ctr.getEmployeeType());
        assertEquals("PROJ-1", ctr.getProjectCode());
    }

    @Test
    @DisplayName("Manager has management premium")
    void managerCompensation() {
        Manager mgr = new Manager(4L, "Carol", 95000, 20000, 25, "Engineering");
        // (95000 + 20000) * 1.15 = 132,250
        assertEquals(132250, mgr.calculateCompensation(), 0.001);
        assertEquals("MANAGER", mgr.getEmployeeType());
        assertEquals("Engineering", mgr.getDepartment());
    }

    @Test
    @DisplayName("Polymorphic array of employees")
    void polymorphicArray() {
        Employee[] employees = {
                new Employee(1L, "A", 50000),
                new FullTimeEmployee(2L, "B", 75000, 10000, 20),
                new Contractor(3L, "C", 95.00, 40, "P1")
        };

        assertEquals(3, employees.length);
        // Each should return correct type via overriding
        assertEquals("EMPLOYEE", employees[0].getEmployeeType());
        assertEquals("FULL_TIME", employees[1].getEmployeeType());
        assertEquals("CONTRACTOR", employees[2].getEmployeeType());
    }

    @Test
    @DisplayName("Review generation includes type-specific details")
    void reviewGeneration() {
        FullTimeEmployee fte = new FullTimeEmployee(1L, "Alice", 80000, 5000, 15);
        String review = fte.generateReview();
        assertTrue(review.contains("FULL_TIME"));
        assertTrue(review.contains("Alice"));
        assertTrue(review.contains("vacation days"));
    }
}

package academy.javaengineering.oop.compositionaggregation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for composition and aggregation demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class CompositionAggregationTest {

    @Test
    void shouldCreateCarWithEngine() {
        Engine engine = new Engine("V8", 400);
        Car car = new Car("Toyota", "Camry", engine);
        
        assertEquals("Toyota", car.getMake());
        assertEquals("Camry", car.getModel());
        assertEquals("V8", car.getEngine().getType());
    }

    @Test
    void shouldStartEngine() {
        Engine engine = new Engine("V6", 300);
        Car car = new Car("Honda", "Accord", engine);
        
        assertFalse(engine.isRunning());
        car.start();
        assertTrue(engine.isRunning());
    }

    @Test
    void shouldManageDepartmentEmployees() {
        Employee emp1 = new Employee("Alice", "Engineering");
        Employee emp2 = new Employee("Bob", "Engineering");
        
        Department dept = new Department("Engineering", emp1);
        dept.addEmployee(emp2);
        
        assertEquals(2, dept.getEmployeeCount());
        assertEquals("Engineering", dept.getName());
    }

    @Test
    void shouldAllowEmployeeIndependence() {
        Employee emp = new Employee("Alice", "Engineering");
        Department dept = new Department("Engineering", emp);
        
        // Employee exists independently
        assertEquals("Alice", emp.getName());
        assertEquals("Engineering", emp.getDepartment());
        
        // Can be removed from department
        dept.removeEmployee(emp);
        assertEquals(0, dept.getEmployeeCount());
    }

    @Test
    void shouldCreateComputerComposition() {
        Computer computer = new Computer("Intel", 16, 512);
        
        assertEquals("Intel", computer.getCpu().getModel());
        assertEquals(16, computer.getRamGB());
        assertEquals(512, computer.getStorageGB());
    }

    @Test
    void shouldCreateWorkstation() {
        Computer computer = new Computer("AMD", 32, 1024);
        Workstation workstation = new Workstation("Dev Station", computer);
        
        assertEquals("Dev Station", workstation.getName());
        assertSame(computer, workstation.getComputer());
    }
}
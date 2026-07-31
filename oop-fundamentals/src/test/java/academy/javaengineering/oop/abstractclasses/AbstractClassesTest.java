package academy.javaengineering.oop.abstractclasses;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for abstract class demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class AbstractClassesTest {

    @Test
    void shouldCalculateCircleArea() {
        Shape2D circle = new CircleShape(5.0);
        assertEquals(Math.PI * 25, circle.getArea(), 0.001);
    }

    @Test
    void shouldCalculateRectangleArea() {
        Shape2D rect = new RectangleShape(4.0, 6.0);
        assertEquals(24.0, rect.getArea(), 0.001);
    }

    @Test
    void shouldTrackShapeCount() {
        int initial = Shape2D.getShapeCount();
        new CircleShape(1.0);
        new RectangleShape(2.0, 3.0);
        
        assertEquals(initial + 2, Shape2D.getShapeCount());
    }

    @Test
    void shouldCreateEmployee() {
        Employee emp = new Developer("Alice", 1001, "Java");
        assertEquals("Alice", emp.getName());
        assertEquals(1001, emp.getId());
        assertTrue(emp.getRole().contains("Java"));
    }

    @Test
    void shouldCalculatePay() {
        Employee dev = new Developer("Bob", 1002, "Python");
        assertTrue(dev.calculatePay() > 0);
    }

    @Test
    void shouldCreateSavingsAccount() {
        BankAccount savings = new SavingsAccount("John", 1000, 0.05);
        assertEquals(1000.0, savings.getBalance(), 0.001);
        assertEquals("Savings Account", savings.getAccountType());
        assertEquals(0.05, savings.getInterestRate(), 0.001);
    }

    @Test
    void shouldApplyInterest() {
        BankAccount savings = new SavingsAccount("John", 1000, 0.10);
        savings.deposit(0); // Trigger interest
        // 1000 + (1000 * 0.10) = 1100
        assertEquals(1100.0, savings.getBalance(), 0.001);
    }

    @Test
    void shouldPlayGame() {
        Game game = new Chess();
        assertDoesNotThrow(game::play);
    }

    @Test
    void shouldNotInstantiateAbstractClass() {
        // This would fail to compile:
        // Shape2D shape = new Shape2D();
        // Employee emp = new Employee("test");
        // BankAccount acc = new BankAccount("test", 0);
        // Game game = new Game();
        
        // Just verify concrete classes work
        assertDoesNotThrow(() -> new CircleShape(1.0));
        assertDoesNotThrow(() -> new Developer("Test", 1, "Java"));
    }
}
package academy.javaengineering.patterns.structural.decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {

    @Test
    void testSimpleCoffee() {
        Coffee coffee = new SimpleCoffee();
        assertEquals("Simple coffee", coffee.getDescription());
        assertEquals(5.0, coffee.getCost(), 0.001);
    }

    @Test
    void testMilkDecorator() {
        Coffee coffee = new MilkDecorator(new SimpleCoffee());
        assertEquals("Simple coffee, milk", coffee.getDescription());
        assertEquals(6.5, coffee.getCost(), 0.001);
    }

    @Test
    void testSugarDecorator() {
        Coffee coffee = new SugarDecorator(new SimpleCoffee());
        assertEquals("Simple coffee, sugar", coffee.getDescription());
        assertEquals(5.5, coffee.getCost(), 0.001);
    }

    @Test
    void testMultipleDecorators() {
        Coffee coffee = new MilkDecorator(new SugarDecorator(new SimpleCoffee()));
        assertEquals("Simple coffee, sugar, milk", coffee.getDescription());
        assertEquals(7.0, coffee.getCost(), 0.001);
    }

    @Test
    void testDecoratorInterface() {
        Coffee coffee = new MilkDecorator(new SimpleCoffee());
        assertTrue(coffee instanceof Coffee);
    }
}

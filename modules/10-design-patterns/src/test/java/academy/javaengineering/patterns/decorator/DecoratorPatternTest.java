package academy.javaengineering.patterns.decorator;

import academy.javaengineering.patterns.decorator.DecoratorExample.Coffee;
import academy.javaengineering.patterns.decorator.DecoratorExample.SimpleCoffee;
import academy.javaengineering.patterns.decorator.DecoratorExample.MilkDecorator;
import academy.javaengineering.patterns.decorator.DecoratorExample.SugarDecorator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorPatternTest {

    @Test
    @DisplayName("Simple coffee should cost 5.00")
    void simpleCoffeeShouldCostFive() {
        Coffee coffee = new SimpleCoffee();
        assertEquals(5.00, coffee.getCost(), 0.01);
    }

    @Test
    @DisplayName("Simple coffee description should be correct")
    void simpleCoffeeDescriptionShouldBeCorrect() {
        Coffee coffee = new SimpleCoffee();
        assertEquals("Simple coffee", coffee.getDescription());
    }

    @Test
    @DisplayName("Milk decorator should add 1.50 to cost")
    void milkDecoratorShouldAddCost() {
        Coffee coffee = new MilkDecorator(new SimpleCoffee());
        assertEquals(6.50, coffee.getCost(), 0.01);
    }

    @Test
    @DisplayName("Milk decorator should update description")
    void milkDecoratorShouldUpdateDescription() {
        Coffee coffee = new MilkDecorator(new SimpleCoffee());
        assertEquals("Simple coffee, milk", coffee.getDescription());
    }

    @Test
    @DisplayName("Sugar decorator should add 0.75 to cost")
    void sugarDecoratorShouldAddCost() {
        Coffee coffee = new SugarDecorator(new SimpleCoffee());
        assertEquals(5.75, coffee.getCost(), 0.01);
    }

    @Test
    @DisplayName("Sugar decorator should update description")
    void sugarDecoratorShouldUpdateDescription() {
        Coffee coffee = new SugarDecorator(new SimpleCoffee());
        assertEquals("Simple coffee, sugar", coffee.getDescription());
    }

    @Test
    @DisplayName("Stacking milk and sugar should combine costs")
    void stackingDecoratorsShouldCombineCosts() {
        Coffee coffee = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        assertEquals(7.25, coffee.getCost(), 0.01);
    }

    @Test
    @DisplayName("Stacking milk and sugar should combine descriptions")
    void stackingDecoratorsShouldCombineDescriptions() {
        Coffee coffee = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        assertEquals("Simple coffee, milk, sugar", coffee.getDescription());
    }

    @Test
    @DisplayName("Multiple same decorators should stack additively")
    void multipleSameDecoratorsShouldStack() {
        Coffee coffee = new MilkDecorator(new MilkDecorator(new SimpleCoffee()));
        assertEquals(8.00, coffee.getCost(), 0.01);
        assertEquals("Simple coffee, milk, milk", coffee.getDescription());
    }

    @Test
    @DisplayName("Decorators should implement Coffee interface")
    void decoratorsShouldImplementInterface() {
        Coffee milk = new MilkDecorator(new SimpleCoffee());
        Coffee sugar = new SugarDecorator(new SimpleCoffee());
        assertInstanceOf(Coffee.class, milk);
        assertInstanceOf(Coffee.class, sugar);
    }
}

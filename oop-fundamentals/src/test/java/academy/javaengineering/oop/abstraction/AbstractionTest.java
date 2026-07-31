package academy.javaengineering.oop.abstraction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for abstraction demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class AbstractionTest {

    @Test
    void shouldCreateCar() {
        Vehicle car = new Car("Toyota", "Camry", 2024);
        assertEquals("Toyota", car.getMake());
        assertEquals("Camry", car.getModel());
        assertEquals(2024, car.getYear());
    }

    @Test
    void shouldStartAndStopCar() {
        Vehicle car = new Car("Toyota", "Camry", 2024);
        assertFalse(car.isRunning());
        car.start();
        assertTrue(car.isRunning());
        car.stop();
        assertFalse(car.isRunning());
    }

    @Test
    void shouldCalculateFuelEfficiency() {
        Vehicle car = new Car("Toyota", "Camry", 2024);
        Vehicle bike = new Motorcycle("Honda", "CBR600", 2023);
        
        assertTrue(car.getFuelEfficiency() > 0);
        assertTrue(bike.getFuelEfficiency() > 0);
    }

    @Test
    void shouldDrawShapes() {
        Drawable circle = new Circle2D(5.0);
        Drawable rect = new Rectangle2D(4.0, 6.0);
        
        assertDoesNotThrow(circle::draw);
        assertDoesNotThrow(rect::draw);
    }

    @Test
    void shouldCalculateResizableArea() {
        Resizable circle = new Circle2D(5.0);
        Resizable rect = new Rectangle2D(4.0, 6.0);
        
        assertEquals(Math.PI * 25, circle.getArea(), 0.001);
        assertEquals(24.0, rect.getArea(), 0.001);
    }

    @Test
    void shouldMakePhoneCalls() {
        Phone phone = new SmartPhone("iPhone", 15);
        assertDoesNotThrow(() -> phone.call("555-1234"));
    }

    @Test
    void shouldProcessPayments() {
        PaymentProcessor creditCard = new CreditCardProcessor();
        PaymentProcessor paypal = new PayPalProcessor();
        
        assertDoesNotThrow(() -> creditCard.processPayment(100.0));
        assertDoesNotThrow(() -> paypal.processPayment(50.0));
    }

    @Test
    void shouldRejectInvalidAmount() {
        PaymentProcessor processor = new CreditCardProcessor();
        assertFalse(processor.validateAmount(-10));
        assertFalse(processor.validateAmount(0));
        assertFalse(processor.validateAmount(10001));
        assertTrue(processor.validateAmount(100));
    }
}
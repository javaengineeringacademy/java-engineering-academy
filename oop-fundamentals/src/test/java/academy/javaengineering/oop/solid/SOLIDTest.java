package academy.javaengineering.oop.solid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SOLID principle demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class SOLIDTest {

    @Test
    void shouldFollowSingleResponsibility() {
        UserService2 userService = new UserService2();
        NotificationService2 notifService = new NotificationService2();
        ReportService reportService = new ReportService();
        
        // Each service handles only its responsibility
        assertDoesNotThrow(() -> userService.createUser("Alice"));
        assertDoesNotThrow(() -> notifService.sendEmail("test@test.com", "Hi"));
        assertDoesNotThrow(reportService::generateSalesReport);
    }

    @Test
    void shouldFollowOpenClosed() {
        ShapeAreaCalculator calculator = new ShapeAreaCalculator();
        
        // Can add new shapes without modifying calculator
        Shape3 circle = new Circle3(5.0);
        Shape3 rect = new Rectangle3(4.0, 6.0);
        Shape3 triangle = new Triangle3(3.0, 4.0, 5.0);
        
        assertDoesNotThrow(() -> calculator.calculate(circle));
        assertDoesNotThrow(() -> calculator.calculate(rect));
        assertDoesNotThrow(() -> calculator.calculate(triangle));
    }

    @Test
    void shouldFollowLiskovSubstitution() {
        PaymentProcessor2[] processors = {
            new CreditCardProcessor2(),
            new PayPalProcessor2(),
            new CryptoProcessor2()
        };
        
        for (PaymentProcessor2 processor : processors) {
            assertNotNull(processor.getPaymentMethod());
            assertDoesNotThrow(() -> processor.process(100.0));
        }
    }

    @Test
    void shouldFollowInterfaceSegregation() {
        Printer printer = new SimplePrinter();
        Scanner scanner = new SimpleScanner();
        FaxMachine fax = new SimpleFaxMachine();
        
        // Each implements only what it needs
        assertDoesNotThrow(() -> printer.print("Doc"));
        assertDoesNotThrow(() -> scanner.scan("Doc"));
        assertDoesNotThrow(() -> fax.fax("Doc"));
    }

    @Test
    void shouldFollowDependencyInversion() {
        MessageService emailService = new EmailService();
        MessageService smsService = new SmsService();
        
        OrderService3 orderWithEmail = new OrderService3(emailService);
        OrderService3 orderWithSms = new OrderService3(smsService);
        
        // Both depend on abstraction, not concretion
        assertDoesNotThrow(() -> orderWithEmail.placeOrder("Laptop"));
        assertDoesNotThrow(() -> orderWithSms.placeOrder("Phone"));
    }

    @Test
    void shouldCalculateShapeAreas() {
        assertEquals(Math.PI * 25, new Circle3(5.0).getArea(), 0.001);
        assertEquals(24.0, new Rectangle3(4.0, 6.0).getArea(), 0.001);
        assertEquals(6.0, new Triangle3(3.0, 4.0, 5.0).getArea(), 0.001);
    }

    @Test
    void shouldProcessWithDifferentPaymentMethods() {
        CreditCardProcessor2 cc = new CreditCardProcessor2();
        PayPalProcessor2 paypal = new PayPalProcessor2();
        CryptoProcessor2 crypto = new CryptoProcessor2();
        
        assertEquals("Credit Card", cc.getPaymentMethod());
        assertEquals("PayPal", paypal.getPaymentMethod());
        assertEquals("Cryptocurrency", crypto.getPaymentMethod());
        
        assertTrue(cc.supportsRefund());
        assertTrue(paypal.supportsRefund());
        assertFalse(crypto.supportsRefund());
    }
}
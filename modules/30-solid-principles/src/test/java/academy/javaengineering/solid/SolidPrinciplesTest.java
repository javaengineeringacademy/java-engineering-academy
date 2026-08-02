package academy.javaengineering.solid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SOLID Principles Tests")
class SolidPrinciplesTest {

    @Test
    @DisplayName("Single Responsibility: UserService should create users")
    void testSingleResponsibility() {
        var validator = new SingleResponsibilityExample.UserValidator();
        var repository = new SingleResponsibilityExample.UserRepository();
        var emailService = new SingleResponsibilityExample.UserEmailService();
        var service = new SingleResponsibilityExample.UserService(validator, repository, emailService);
        
        assertDoesNotThrow(() -> service.createUser("John"));
    }

    @Test
    @DisplayName("Open/Closed: New shapes should work without modification")
    void testOpenClosed() {
        var calculator = new OpenClosedExample.BetterAreaCalculator();
        var circle = new OpenClosedExample.Circle(5);
        var rectangle = new OpenClosedExample.Rectangle(4, 6);
        var triangle = new OpenClosedExample.Triangle(3, 8);
        
        assertEquals(Math.PI * 25, calculator.calculate(circle), 0.001);
        assertEquals(24, calculator.calculate(rectangle));
        assertEquals(12, calculator.calculate(triangle));
    }

    @Test
    @DisplayName("Liskov Substitution: All shapes should be substitutable")
    void testLiskovSubstitution() {
        LiskovSubstitutionExample.Shape rectangle = new LiskovSubstitutionExample.BetterRectangle(4, 5);
        LiskovSubstitutionExample.Shape square = new LiskovSubstitutionExample.BetterSquare(4);
        
        assertEquals(20, rectangle.area());
        assertEquals(16, square.area());
    }
}

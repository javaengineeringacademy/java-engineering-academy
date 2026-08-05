package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionTest {

    @Test
    void testInvalidAgeExceptionMessage() {
        InvalidAgeException e = assertThrows(InvalidAgeException.class, () -> {
            AgeValidator.validate(-1);
        });
        assertTrue(e.getMessage().contains("negative"));
    }

    @Test
    void testInvalidAgeExceptionAgeValue() {
        InvalidAgeException e = assertThrows(InvalidAgeException.class, () -> {
            AgeValidator.validate(200);
        });
        assertEquals(200, e.getAge());
    }

    @Test
    void testValidateValidAge() {
        assertDoesNotThrow(() -> AgeValidator.validate(25));
    }

    @Test
    void testIsAdultTrue() throws InvalidAgeException {
        assertTrue(AgeValidator.isAdult(20));
    }

    @Test
    void testGetAgeCategoryChild() throws InvalidAgeException {
        assertEquals("Child", AgeValidator.getAgeCategory(10));
    }

    @Test
    void testGetAgeCategoryAdult() throws InvalidAgeException {
        assertEquals("Adult", AgeValidator.getAgeCategory(30));
    }

    @Test
    void testRegisterValid() {
        assertDoesNotThrow(() -> AgeValidator.register("Alice", 25));
    }

    @Test
    void testRegisterInvalid() {
        assertThrows(InvalidAgeException.class, () -> {
            AgeValidator.register("Bob", -5);
        });
    }
}

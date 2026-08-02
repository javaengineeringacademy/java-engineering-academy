package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @Test
    void shouldValidateEmail() {
        ValidationExample.EmailValidator validator = new ValidationExample.EmailValidator();
        assertTrue(validator.validate("test@test.com").isEmpty());
        assertFalse(validator.validate("invalid").isEmpty());
        assertFalse(validator.validate(null).isEmpty());
    }
}

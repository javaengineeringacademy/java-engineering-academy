package academy.javaengineering.testing.coverage.practices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Coverage Exclusions
 *
 * Tasks:
 * 1. Identify code to exclude from coverage
 * 2. Use @Generated annotation
 * 3. Configure JaCoCo exclusions
 */
class Exercise2CoverageExclusions {

    // This DTO doesn't need coverage testing
    static class UserDTO {
        private String name;
        private String email;
        // getters, setters, toString, equals, hashCode
    }

    @Test
    void shouldCreateDTO() {
        UserDTO dto = new UserDTO();
        assertNotNull(dto);
    }
}

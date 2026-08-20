package academy.javaengineering.testing.coverage.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Exercise2CoverageExclusionsSolution {

    // Exclude DTO from coverage via @Generated or JaCoCo config
    static class UserDTO {
        private String name;
        private String email;
        UserDTO() {}
        UserDTO(String name, String email) { this.name = name; this.email = email; }
        String getName() { return name; }
        void setName(String name) { this.name = name; }
        String getEmail() { return email; }
        void setEmail(String email) { this.email = email; }
    }

    @Test
    void shouldCreateDTO() {
        UserDTO dto = new UserDTO("Alice", "alice@test.com");
        assertEquals("Alice", dto.getName());
        assertEquals("alice@test.com", dto.getEmail());
    }
}

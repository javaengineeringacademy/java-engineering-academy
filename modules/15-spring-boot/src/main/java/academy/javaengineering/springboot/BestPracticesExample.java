package academy.javaengineering.springboot;

import java.util.ArrayList;
import java.util.List;

/**
 * Best Practices - Project Structure, Layering, DTOs.
 */
public class BestPracticesExample {

    public record UserDTO(Long id, String name, String email) {}

    public static class UserEntity {
        private Long id;
        private String name;
        private String email;

        public UserEntity(Long id, String name, String email) {
            this.id = id; this.name = name; this.email = email;
        }

        public UserDTO toDTO() { return new UserDTO(id, name, email); }
        public static UserEntity fromDTO(UserDTO dto) {
            return new UserEntity(dto.id(), dto.name(), dto.email());
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    public static class UserController {
        private final List<UserDTO> users = new ArrayList<>();

        public UserDTO createUser(UserDTO dto) {
            users.add(dto);
            return dto;
        }

        public List<UserDTO> getAllUsers() { return users; }
    }

    public static void main(String[] args) {
        UserController controller = new UserController();
        UserDTO user = new UserDTO(1L, "John", "john@test.com");
        controller.createUser(user);
        System.out.println("Users: " + controller.getAllUsers());
    }
}

package academy.javaengineering.patterns.enterprise.dto;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the DTO pattern showing how domain entities are
 * transformed to DTOs for safe external transfer.
 */
public class DTOExample {

    public static void main(String[] args) {
        System.out.println("=== DTO Pattern Demo ===\n");

        // Domain entities with sensitive data
        User user1 = new User(1L, "alice", "s3cret!", "alice@example.com", "Alice Smith", true);
        User user2 = new User(2L, "bob", "p@ssw0rd", "bob@example.com", "Bob Jones", true);
        User user3 = new User(3L, "charlie", "mypass", "charlie@example.com", "Charlie Brown", false);

        List<User> users = Arrays.asList(user1, user2, user3);

        // Convert to DTOs
        System.out.println("--- Domain Entities (internal) ---");
        users.forEach(u -> System.out.println("  " + u));

        List<UserDTO> dtos = UserMapper.toDTOList(users);

        System.out.println("\n--- DTOs (API response) ---");
        dtos.forEach(dto -> System.out.println("  " + dto));

        System.out.println("\n--- Single Conversion ---");
        UserDTO singleDTO = UserMapper.toDTO(user1);
        System.out.println("Entity: " + user1);
        System.out.println("DTO:    " + singleDTO);
        System.out.println("Note: DTO has no password field!");

        System.out.println("\n--- DTO to Entity ---");
        User restored = UserMapper.toEntity(singleDTO);
        System.out.println("Restored entity (no password): " + restored);
    }
}

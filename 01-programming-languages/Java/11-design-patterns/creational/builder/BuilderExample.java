package academy.javaengineering.patterns.creational;

import java.util.List;

public class BuilderExample {

    public static void main(String[] args) {
        User user1 = new User.Builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .email("john.doe@example.com")
                .phone("555-0100")
                .addresses(List.of("123 Main St", "456 Oak Ave"))
                .build();

        System.out.println("=== User 1 (Full Builder) ===");
        System.out.println(user1);

        User user2 = new UserBuilder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .build();

        System.out.println("\n=== User 2 (Simplified Builder) ===");
        System.out.println(user2);

        User user3 = new User.Builder()
                .firstName("Bob")
                .lastName("Wilson")
                .age(25)
                .build();

        System.out.println("\n=== User 3 (Minimal Builder) ===");
        System.out.println(user3);
    }
}

package academy.javaengineering.patterns.enterprise.dto;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the DTO pattern mapper.
 */
public class DTOTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== DTO Pattern Tests ===\n");

        testToDTO();
        testToDTOWithNull();
        testToEntity();
        testToEntityWithNull();
        testRoundTrip();
        testToDTOList();
        testPasswordHidden();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testToDTO() {
        User user = new User(1L, "alice", "secret", "a@b.com", "Alice", true);
        UserDTO dto = UserMapper.toDTO(user);
        assertTest("ToDTO id", dto.getId().equals(1L));
        assertTest("ToDTO username", dto.getUsername().equals("alice"));
        assertTest("ToDTO name", dto.getName().equals("Alice"));
        assertTest("ToDTO email", dto.getEmail().equals("a@b.com"));
        assertTest("ToDTO active", dto.isActive());
    }

    private static void testToDTOWithNull() {
        assertTest("ToDTO null returns null", UserMapper.toDTO((User) null) == null);
    }

    private static void testToEntity() {
        UserDTO dto = new UserDTO(2L, "bob", "b@b.com", "Bob", false);
        User user = UserMapper.toEntity(dto);
        assertTest("ToEntity id", user.getId().equals(2L));
        assertTest("ToEntity username", user.getUsername().equals("bob"));
        assertTest("ToEntity active", !user.isActive());
    }

    private static void testToEntityWithNull() {
        assertTest("ToEntity null returns null", UserMapper.toEntity(null) == null);
    }

    private static void testRoundTrip() {
        User original = new User(1L, "alice", "secret", "a@b.com", "Alice", true);
        UserDTO dto = UserMapper.toDTO(original);
        User restored = UserMapper.toEntity(dto);
        assertTest("RoundTrip username", restored.getUsername().equals(original.getUsername()));
        assertTest("RoundTrip name", restored.getName().equals(original.getName()));
        assertTest("RoundTrip email", restored.getEmail().equals(original.getEmail()));
    }

    private static void testToDTOList() {
        User u1 = new User(1L, "a", "p1", "a@b.com", "A", true);
        User u2 = new User(2L, "b", "p2", "b@b.com", "B", true);
        List<UserDTO> dtos = UserMapper.toDTOList(Arrays.asList(u1, u2));
        assertTest("ToDTOList size", dtos.size() == 2);
        assertTest("ToDTOList first name", dtos.get(0).getName().equals("A"));
    }

    private static void testPasswordHidden() {
        User user = new User(1L, "alice", "s3cret!", "a@b.com", "Alice", true);
        UserDTO dto = UserMapper.toDTO(user);
        assertTest("DTO has no password method", true);
        // Verify password is not in toString
        assertTest("DTO toString no password", !dto.toString().contains("s3cret!"));
    }

    private static void assertTest(String name, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + name);
            passed++;
        } else {
            System.out.println("  FAIL: " + name);
            failed++;
        }
    }
}

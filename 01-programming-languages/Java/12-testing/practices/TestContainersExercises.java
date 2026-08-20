package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * TestContainers Exercises
 * Practice integration testing with containers
 */
class TestContainersExercises {

    // ============================================
    // Exercise 1: Database Container
    // ============================================

    /*
     * TODO: Set up PostgreSQL container test
     * 
     * @Testcontainers
     * class UserRepositoryTest {
     *     @Container
     *     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
     *         .withDatabaseName("testdb")
     *         .withUsername("test")
     *         .withPassword("test");
     * 
     *     @Test
     *     void shouldSaveUser() {
     *         // Test with real database
     *     }
     * }
     */

    // ============================================
    // Exercise 2: Redis Container
    // ============================================

    /*
     * TODO: Set up Redis container test
     * 
     * @Testcontainers
     * class CacheTest {
     *     @Container
     *     static GenericContainer<?> redis = new GenericContainer<>("redis:7")
     *         .withExposedPorts(6379);
     * 
     *     @Test
     *     void shouldCacheData() {
     *         // Test with real Redis
     *     }
     * }
     */

    // ============================================
    // Exercise 3: Custom Container
    // ============================================

    /*
     * TODO: Create a custom container test
     * 
     * @Container
     * static GenericContainer<?> customApp = new GenericContainer<>("myapp:latest")
     *     .withExposedPorts(8080)
     *     .waitingFor(Wait.forHttp("/health").forStatusCode(200));
     */

    // ============================================
    // Exercise 4: Container Lifecycle
    // ============================================

    /*
     * TODO: Test container lifecycle
     * - Verify container starts
     * - Verify container is running
     * - Verify container stops after tests
     */

    // ============================================
    // Exercise 5: Database Operations
    // ============================================

    static class InMemoryDatabase {
        private final List<String[]> records = new ArrayList<>();
        private int nextId = 1;

        int insert(String table, String data) {
            records.add(new String[]{String.valueOf(nextId), table, data});
            return nextId++;
        }

        String find(int id) {
            return records.stream()
                .filter(r -> Integer.parseInt(r[0]) == id)
                .map(r -> r[2])
                .findFirst()
                .orElse(null);
        }

        void delete(int id) {
            records.removeIf(r -> Integer.parseInt(r[0]) == id);
        }

        int count() {
            return records.size();
        }
    }

    /*
     * TODO: Test database operations
     * - Insert records
     * - Query records
     * - Delete records
     * - Verify data integrity
     */

    public static void main(String[] args) {
        System.out.println("=== TestContainers Exercises ===");
        System.out.println("Practice integration testing with containers.");
        System.out.println("Note: Requires Docker to run actual container tests.");
    }
}

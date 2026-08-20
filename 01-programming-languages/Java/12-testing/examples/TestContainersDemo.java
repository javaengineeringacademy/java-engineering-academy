package academy.javaengineering.testing.examples;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TestContainers Demo - Integration Testing with Containers
 */
class TestContainersDemo {

    // ============================================
    // TestContainers Concept
    // ============================================

    /*
     * TestContainers is a Java library that supports JUnit tests,
     * providing lightweight, throwaway instances of common databases,
     * Selenium web browsers, or anything else that can run in a Docker container.
     * 
     * Dependencies (Maven):
     * 
     * <dependency>
     *     <groupId>org.testcontainers</groupId>
     *     <artifactId>testcontainers</artifactId>
     *     <version>1.19.3</version>
     *     <scope>test</scope>
     * </dependency>
     * <dependency>
     *     <groupId>org.testcontainers</groupId>
     *     <artifactId>junit-jupiter</artifactId>
     *     <version>1.19.3</version>
     *     <scope>test</scope>
     * </dependency>
     * <dependency>
     *     <groupId>org.testcontainers</groupId>
     *     <artifactId>postgresql</artifactId>
     *     <version>1.19.3</version>
     *     <scope>test</scope>
     * </dependency>
     */

    // ============================================
    // Database Container Example
    // ============================================

    /*
     * PostgreSQL Container Test:
     * 
     * @Testcontainers
     * class UserRepositoryTest {
     * 
     *     @Container
     *     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
     *         .withDatabaseName("testdb")
     *         .withUsername("test")
     *         .withPassword("test");
     * 
     *     @DynamicPropertySource
     *     static void configureProperties(DynamicPropertyRegistry registry) {
     *         registry.add("spring.datasource.url", postgres::getJdbcUrl);
     *         registry.add("spring.datasource.username", postgres::getUsername);
     *         registry.add("spring.datasource.password", postgres::getPassword);
     *     }
     * 
     *     @Autowired
     *     private UserRepository userRepository;
     * 
     *     @Test
     *     void shouldSaveUser() {
     *         // Given
     *         User user = new User("John", "john@example.com");
     * 
     *         // When
     *         User saved = userRepository.save(user);
     * 
     *         // Then
     *         assertThat(saved.getId()).isNotNull();
     *         assertThat(userRepository.findById(saved.getId())).isPresent();
     *     }
     * }
     */

    // ============================================
    // MySQL Container Example
    // ============================================

    /*
     * MySQL Container Test:
     * 
     * @Testcontainers
     * class MySQLTest {
     * 
     *     @Container
     *     static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
     *         .withDatabaseName("testdb")
     *         .withUsername("test")
     *         .withPassword("test");
     * 
     *     @Test
     *     void shouldConnectToMySQL() {
     *         assertThat(mysql.isRunning()).isTrue();
     *         assertThat(mysql.getJdbcUrl()).contains("testdb");
     *     }
     * }
     */

    // ============================================
    // Redis Container Example
    // ============================================

    /*
     * Redis Container Test:
     * 
     * @Testcontainers
     * class RedisCacheTest {
     * 
     *     @Container
     *     static GenericContainer<?> redis = new GenericContainer<>("redis:7")
     *         .withExposedPorts(6379);
     * 
     *     @DynamicPropertySource
     *     static void configureRedis(DynamicPropertyRegistry registry) {
     *         registry.add("spring.redis.host", redis::getHost);
     *         registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
     *     }
     * 
     *     @Test
     *     void shouldCacheData() {
     *         // Test with real Redis instance
     *     }
     * }
     */

    // ============================================
    // Kafka Container Example
    // ============================================

    /*
     * Kafka Container Test:
     * 
     * @Testcontainers
     * class KafkaTest {
     * 
     *     @Container
     *     static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
     * 
     *     @Test
     *     void shouldProduceAndConsumeMessage() {
     *         // Test Kafka messaging
     *     }
     * }
     */

    // ============================================
    // Custom Container Example
    // ============================================

    /*
     * Custom Docker Container:
     * 
     * @Container
     * static GenericContainer<?> customApp = new GenericContainer<>("myapp:latest")
     *     .withExposedPorts(8080)
     *     .withEnv("SPRING_PROFILES_ACTIVE", "test")
     *     .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(200));
     * 
     * @Test
     * void shouldCallCustomApp() {
     *     String baseUrl = "http://" + customApp.getHost() + ":" + customApp.getMappedPort(8080);
     *     // Make HTTP calls to the container
     * }
     */

    // ============================================
    // Database Test Simulation (without Docker)
    // ============================================

    static class InMemoryDatabase {
        private final List<String[]> users = new ArrayList<>();
        private int nextId = 1;

        void connect() {
            System.out.println("Connected to in-memory database");
        }

        int insertUser(String name, String email) {
            users.add(new String[]{String.valueOf(nextId), name, email});
            return nextId++;
        }

        String[] getUser(int id) {
            return users.stream()
                .filter(u -> Integer.parseInt(u[0]) == id)
                .findFirst()
                .orElse(null);
        }

        List<String[]> getAllUsers() {
            return new ArrayList<>(users);
        }

        void close() {
            System.out.println("Database connection closed");
        }
    }

    static class UserRepositoryTest {
        private InMemoryDatabase db;

        @org.junit.jupiter.api.BeforeEach
        void setUp() {
            db = new InMemoryDatabase();
            db.connect();
        }

        @org.junit.jupiter.api.AfterEach
        void tearDown() {
            db.close();
        }

        @org.junit.jupiter.api.Test
        void testInsertUser() {
            // Act
            int id = db.insertUser("John", "john@example.com");

            // Assert
            assert id > 0;
            String[] user = db.getUser(id);
            assert user != null;
            assert "John".equals(user[1]);
            assert "john@example.com".equals(user[2]);
        }

        @org.junit.jupiter.api.Test
        void testGetAllUsers() {
            // Arrange
            db.insertUser("Alice", "alice@example.com");
            db.insertUser("Bob", "bob@example.com");

            // Act
            List<String[]> users = db.getAllUsers();

            // Assert
            assert users.size() == 2;
        }
    }

    // ============================================
    // CI/CD Configuration
    // ============================================

    /*
     * TestContainers in CI/CD:
     * 
     * GitHub Actions:
     * - TestContainers works out of the box with GitHub Actions
     * - Docker is pre-installed on GitHub Actions runners
     * 
     * Docker Compose Override:
     * 
     * @Testcontainers
     * @SpringBootTest
     * @ActiveProfiles("test")
     * class IntegrationTest {
     * 
     *     @Container
     *     @ServiceConnection
     *     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
     * 
     *     @Container
     *     @ServiceConnection
     *     static RedisContainer redis = new RedisContainer("redis:7");
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== TestContainers Demo ===\n");

        System.out.println("--- In-Memory Database Simulation ---");
        UserRepositoryTest test = new UserRepositoryTest();
        test.setUp();
        test.testInsertUser();
        test.testGetAllUsers();
        test.tearDown();

        System.out.println("\n--- TestContainers Benefits ---");
        System.out.println("1. Real integration testing with actual dependencies");
        System.out.println("2. Disposable containers - no cleanup needed");
        System.out.println("3. Consistent environment across CI/CD");
        System.out.println("4. Supports databases, message brokers, and more");
        System.out.println("5. Automatic port mapping and lifecycle management");

        System.out.println("\n--- Supported Containers ---");
        System.out.println("- PostgreSQL, MySQL, MariaDB, Oracle, MSSQL");
        System.out.println("- Redis, MongoDB, Elasticsearch");
        System.out.println("- Kafka, RabbitMQ, ActiveMQ");
        System.out.println("- Selenium, BrowserStack");
        System.out.println("- Any Docker image");

        System.out.println("\n=== TestContainers Demo Complete ===");
    }
}

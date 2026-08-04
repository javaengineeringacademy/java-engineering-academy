# Testcontainers

## Overview

Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container. It enables integration testing with real dependencies.

## Dependencies

### Maven

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <version>1.19.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.19.3</version>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
dependencies {
    testImplementation 'org.testcontainers:testcontainers:1.19.3'
    testImplementation 'org.testcontainers:junit-jupiter:1.19.3'
}
```

## Database Containers

### PostgreSQL

```java
@Testcontainers
class PostgreSQLTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldConnectToPostgreSQL() {
        String jdbcUrl = postgres.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("testdb"));
    }
}
```

### MySQL

```java
@Testcontainers
class MySQLTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    void shouldRunMySQLQuery() throws SQLException {
        Connection conn = DriverManager.getConnection(
            mysql.getJdbcUrl(),
            mysql.getUsername(),
            mysql.getPassword()
        );

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
    }
}
```

### Redis

```java
@Testcontainers
class RedisTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7")
        .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureRedis(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Test
    void shouldStoreAndRetrieveFromRedis() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();

        template.opsForValue().set("key", "value");
        String value = template.opsForValue().get("key");

        assertEquals("value", value);
    }
}
```

### MongoDB

```java
@Testcontainers
class MongoDBTest {

    @Container
    static MongoDBContainer mongoDB = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void configureMongo(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDB::getReplicaSetUrl);
    }

    @Test
    void shouldInsertDocument() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "testdb");

        Document doc = new Document("name", "John").append("age", 25);
        mongoTemplate.save(doc, "users");

        Document found = mongoTemplate.findOne(
            Query.query(Criteria.where("name").is("John")),
            Document.class,
            "users"
        );

        assertNotNull(found);
        assertEquals(25, found.get("age"));
    }
}
```

### Kafka

```java
@Testcontainers
class KafkaTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void configureKafka(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void shouldProduceAndConsumeMessage() throws Exception {
        // Producer
        KafkaTemplate<String, String> producer = new KafkaTemplate<>(producerFactory);
        producer.send("test-topic", "key", "value").get();

        // Consumer
        ConsumerRecord<String, String> record = consumer.poll(Duration.ofSeconds(5))
            .records("test-topic")
            .iterator()
            .next();

        assertEquals("key", record.key());
        assertEquals("value", record.value());
    }
}
```

## Custom Containers

### GenericContainer

```java
@Testcontainers
class CustomServiceTest {

    @Container
    static GenericContainer<?> customService = new GenericContainer<>(DockerImageName.parse("my-service:latest"))
        .withExposedPorts(8080)
        .withEnv("API_KEY", "test-key")
        .withLogConsumer(new Slf4jLogConsumer LoggerFactory.getLogger("custom-service"))
        .waitingFor(Wait.forHttp("/health").forPort(8080));

    @Test
    void shouldCallCustomService() {
        String baseUrl = "http://" + customService.getHost() + ":" + customService.getMappedPort(8080);

        given()
            .baseUri(baseUrl)
        .when()
            .get("/api/data")
        .then()
            .statusCode(200);
    }
}
```

### DockerComposeContainer

```java
@Testcontainers
class DockerComposeTest {

    @Container
    static DockerComposeContainer<?> environment = new DockerComposeContainer<>(
        new File("src/test/resources/docker-compose.yml")
    )
        .withExposedService("postgres", 5432)
        .withExposedService("redis", 6379)
        .withLocalCompose(true);

    @Test
    void shouldStartAllServices() {
        // All services from docker-compose.yml are running
        assertNotNull(environment);
    }
}
```

## Lifecycle Management

### Shared Containers (Static)

```java
@Testcontainers
class SharedContainerTest {

    // Shared across all test methods - started once per class
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Test
    void test1() {
        // Uses shared postgres instance
    }

    @Test
    void test2() {
        // Uses same shared postgres instance
    }
}
```

### Per-Test Containers

```java
@Testcontainers
class PerTestContainerTest {

    // Started fresh for each test method
    @Container
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Test
    void test1() {
        // Fresh postgres instance
        postgres.execInContainer("psql", "-U", "test", "-c", "CREATE TABLE test (id INT)");
    }

    @Test
    void test2() {
        // Another fresh postgres instance (test1's changes are gone)
    }
}
```

### Custom Init Containers

```java
@Container
static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
    .withInitScript("schema.sql"); // Runs SQL on startup

@BeforeAll
static void initDatabase() {
    // Container is already initialized with schema.sql
}
```

## Wait Strategies

```java
@Container
static GenericContainer<?> app = new GenericContainer<>("my-app:latest")
    .withExposedPorts(8080)
    // Wait for HTTP endpoint
    .waitingFor(Wait.forHttp("/health").forStatusCode(200))
    // Wait for log message
    .waitingFor(Wait.forLogMessage(".*Application started.*", 1))
    // Wait for fixed duration
    .waitingFor(Wait.forListeningPort())
    // Wait for custom condition
    .waitingFor(Wait.forHttp("/ready")
        .forPort(8080)
        .forStatusCode(200)
        .withStartupTimeout(Duration.ofSeconds(60)));
```

## Resource Reuse

### Singleton Containers

```java
@Containers
class SingletonContainerTest {

    // Reuse container across test runs
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withReuse(true); // Requires testcontainers.properties with reus

    @Test
    void test1() {
        // Container persists across test runs
    }
}
```

### testcontainers.properties

```properties
# Enable container reuse
testcontainers.reuse.enable=true
```

## Resource Limits

```java
@Container
static GenericContainer<?> redis = new GenericContainer<>("redis:7")
    .withCreateContainerCmdModifier(cmd -> {
        cmd.getHostConfig()
            .withMemory(512 * 1024 * 1024L) // 512MB
            .withCpuPeriod(100000L)
            .withCpuQuota(50000L); // 50% CPU
    });
```

## Network Configuration

```java
@Container
static Network network = Network.newNetwork();

@Container
static GenericContainer<?> postgres = new GenericContainer<>("postgres:15")
    .withNetwork(network)
    .withNetworkAliases("db");

@Container
static GenericContainer<?> app = new GenericContainer<>("my-app:latest")
    .withNetwork(network)
    .withEnv("DB_HOST", "db"); // Connect via network alias
```

## Advanced Patterns

### Cleanup Between Tests

```java
@Testcontainers
class CleanupTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @BeforeEach
    void setup() {
        // Clean database before each test
        postgres.execInContainer(
            "psql", "-U", "test", "-c", "TRUNCATE TABLE users CASCADE"
        );
    }

    @Test
    void test1() {
        // Clean slate
    }
}
```

### Custom Container Class

```java
public class PostgresWithExtensions extends PostgreSQLContainer<PostgresWithExtensions> {

    private static final String IMAGE = "postgres:15";

    public PostgresWithExtensions() {
        super(IMAGE);
    }

    public PostgresWithExtensions withExtension(String extension) {
        withCommand("postgres", "-c", "shared_preload_libraries=" + extension);
        return this;
    }

    @Override
    public void start() {
        super.start();
        // Additional setup after container starts
    }
}

// Usage
@Container
static PostgresWithExtensions postgres = new PostgresWithExtensions()
    .withExtension("pg_trgm");
```

## CI/CD Integration

### GitHub Actions

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'
      - name: Run Tests
        run: mvn test
        env:
          TESTCONTAINERS_RYUK_DISABLED: true
```

### Docker Requirements

```yaml
services:
  docker:
    image: docker:24-dind
    privileged: true
```

## Performance Tips

1. **Use Static Containers**: Share containers across tests in the same class
2. **Reuse Containers**: Enable reuse for faster subsequent runs
3. **Minimize Image Size**: Use slim Docker images
4. **Parallel Execution**: Use `@Testcontainers(parallel = true)` for parallel tests
5. **Wait Efficiently**: Use appropriate wait strategies

## Common Issues

### Port Binding

```java
// Problem: Port already in use
@Container
static GenericContainer<?> app = new GenericContainer<>("my-app:latest")
    .withExposedPorts(8080);

// Solution: Use random port
int mappedPort = app.getMappedPort(8080);
```

### Container Startup Time

```java
// Problem: Container not ready
@Container
static GenericContainer<?> app = new GenericContainer<>("my-app:latest")
    .waitingFor(Wait.forListeningPort()); // Add wait strategy
```

### Resource Exhaustion

```java
// Problem: Too many containers
// Solution: Reuse containers and clean up properly

@Container
static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
    .withReuse(true);
```

## Resources

- [Testcontainers Documentation](https://www.testcontainers.org/)
- [Testcontainers Java](https://www.testcontainers.org/features/containers/)
- [Testcontainers GitHub](https://github.com/testcontainers/testcontainers-java)
- [Testcontainers Examples](https://github.com/testcontainers/testcontainers-java/tree/main/examples)

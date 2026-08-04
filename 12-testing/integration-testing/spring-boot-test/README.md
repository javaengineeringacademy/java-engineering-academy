# Spring Boot Test

## Overview

Spring Boot Test provides utilities and annotations for testing Spring Boot applications. It includes support for unit testing, integration testing, and slice testing with minimal configuration.

## Dependencies

### Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Core Annotations

### @SpringBootTest

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}
```

### @WebMvcTest (Slice Testing)

```java
@WebMvcTest(UserController.class)
class UserControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldGetUser() throws Exception {
        when(userService.findById(1L))
            .thenReturn(new User(1, "John", "john@example.com"));

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

### @DataJpaTest

```java
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        // Given
        User user = new User("John", "john@example.com");
        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByEmail("john@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getName());
    }
}
```

### @RestClientTest

```java
@RestClientTest(UserClient.class)
class UserClientTest {

    @Autowired
    private UserClient userClient;

    @Autowired
    private MockRestServiceServer server;

    @MockBean
    private UserService userService;

    @Test
    void shouldFetchUser() {
        server.expect(requestTo("/users/1"))
            .andExpect(method(GET))
            .andRespond(withSuccess(
                "{\"id\":1,\"name\":\"John\"}",
                MediaType.APPLICATION_JSON
            ));

        User user = userClient.getUser(1L);

        assertEquals("John", user.getName());
        server.verify();
    }
}
```

## MockMvc Testing

### GET Request

```java
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnProduct() throws Exception {
        when(productService.findById(1L))
            .thenReturn(new Product(1, "Laptop", 999.99));

        mockMvc.perform(get("/api/products/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Laptop"))
            .andExpect(jsonPath("$.price").value(999.99));
    }
}
```

### POST Request

```java
@Test
void shouldCreateProduct() throws Exception {
    Product newProduct = new Product("Phone", 599.99);
    when(productService.create(any())).thenReturn(new Product(1, "Phone", 599.99));

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Phone"));
}
```

### PUT Request

```java
@Test
void shouldUpdateProduct() throws Exception {
    Product updated = new Product("Phone Pro", 799.99);
    when(productService.update(eq(1L), any())).thenReturn(updated);

    mockMvc.perform(put("/api/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Phone Pro"));
}
```

### DELETE Request

```java
@Test
void shouldDeleteProduct() throws Exception {
    mockMvc.perform(delete("/api/products/1"))
        .andExpect(status().isNoContent());

    verify(productService).delete(1L);
}
```

### Request Parameters

```java
@Test
void shouldFilterProducts() throws Exception {
    when(productService.findByCategory("electronics"))
        .thenReturn(List.of(new Product("Laptop", 999.99)));

    mockMvc.perform(get("/api/products")
            .param("category", "electronics")
            .param("minPrice", "100")
            .param("maxPrice", "2000"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].name").value("Laptop"));
}
```

### Request Headers

```java
@Test
void shouldAcceptRequestWithAuthHeader() throws Exception {
    mockMvc.perform(get("/api/users/me")
            .header("Authorization", "Bearer valid-token"))
        .andExpect(status().isOk());
}
```

### File Upload

```java
@Test
void shouldUploadFile() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello World".getBytes()
    );

    mockMvc.perform(multipart("/api/upload").file(file))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.filename").value("test.txt"));
}
```

## Test Configuration

### Test Properties

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.feature.new-ui=true",
    "app.api.url=http://localhost:8081"
})
class FeatureFlagTest {

    @Value("${app.feature.new-ui}")
    private boolean newUiEnabled;

    @Test
    void shouldLoadFeatureFlags() {
        assertTrue(newUiEnabled);
    }
}
```

### Test Profiles

```java
@SpringBootTest
@ActiveProfiles("test")
class ProfileSpecificTest {

    @Autowired
    private Environment environment;

    @Test
    void shouldUseTestProfile() {
        assertEquals("test", environment.getActiveProfiles()[0]);
    }
}
```

### Test Configuration Classes

```java
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public EmailService mockEmailService() {
        return mock(EmailService.class);
    }

    @Bean
    public Clock testClock() {
        return Clock.fixed(
            Instant.parse("2024-01-15T10:00:00Z"),
            ZoneId.of("UTC")
        );
    }
}

@SpringBootTest
@Import(TestConfig.class)
class ConfiguredTest {

    @Autowired
    private EmailService emailService;

    @Test
    void shouldUseTestConfiguration() {
        verify(emailService, never()).send(any());
    }
}
```

## Test Containers Integration

### Database Testing

```java
@SpringBootTest
@Testcontainers
class DatabaseIntegrationTest {

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

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPersistUser() {
        User user = new User("John", "john@example.com");
        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getName());
    }
}
```

### Redis Testing

```java
@SpringBootTest
@Testcontainers
class CacheIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7")
        .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureRedis(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private CacheManager cacheManager;

    @Test
    void shouldCacheData() {
        Cache cache = cacheManager.getCache("users");
        cache.put("1", new User("John"));

        Cache.ValueWrapper wrapper = cache.get("1");
        assertNotNull(wrapper);
        assertEquals("John", ((User) wrapper.get()).getName());
    }
}
```

## Test Data Management

### @Sql

```java
@DataJpaTest
@Sql(scripts = "/test-data/users.sql")
class UserRepositorySqlTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldLoadTestData() {
        List<User> users = userRepository.findAll();
        assertEquals(5, users.size());
    }
}
```

### Test Data Factory

```java
@Component
public class TestDataFactory {

    private final Faker faker = new Faker();

    public User createRandomUser() {
        return new User(
            faker.name().fullName(),
            faker.internet().emailAddress(),
            faker.number().numberBetween(18, 80)
        );
    }

    public List<User> createRandomUsers(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> createRandomUser())
            .collect(Collectors.toList());
    }

    public Product createProduct(String name, double price) {
        return new Product(name, price, faker.commerce().productName());
    }
}
```

## Async Testing

```java
@SpringBootTest
class AsyncServiceTest {

    @Autowired
    private AsyncService asyncService;

    @Test
    void shouldCompleteAsyncOperation() throws Exception {
        CompletableFuture<String> future = asyncService.processAsync("input");

        String result = future.get(5, TimeUnit.SECONDS);

        assertEquals("processed:input", result);
    }

    @Test
    void shouldHandleAsyncWithError() throws Exception {
        CompletableFuture<String> future = asyncService.processAsync(null);

        ExecutionException exception = assertThrows(
            ExecutionException.class,
            () -> future.get(5, TimeUnit.SECONDS)
        );

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}
```

## WebSocket Testing

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void setup() {
        stompClient = new WebSocketStompClient(new SockJsClient(
            new RestTemplateWebSocketTransportRequestFactory()
        ));
    }

    @Test
    void shouldReceiveMessage() throws Exception {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

        stompClient.connect("ws://localhost:" + port + "/ws",
            new StompSessionHandlerAdapter() {});

        // Verify message received
        String message = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertEquals("Hello", message);
    }
}
```

## Test Slices Summary

| Annotation | Tests | Auto-configured |
|------------|-------|-----------------|
| @WebMvcTest | Controllers | MockMvc, JSON, Validations |
| @DataJpaTest | Repositories | JPA, H2, Liquibase/Flyway |
| @RestClientTest | REST Clients | MockRestServiceServer |
| @JsonTest | JSON Serialization | Jackson, Gson |
| @WebFluxTest | WebFlux Controllers | WebTestClient |
| @DataRedisTest | Redis Repositories | Redis, Lettuce |
| @JdbcTest | JDBC Repositories | DataSource, JdbcTemplate |

## Best Practices

### Use Slice Tests

```java
// Good: Fast, focused test
@WebMvcTest(UserController.class)
class UserControllerTest {
    // Only loads web layer
}

// Bad: Loads entire context
@SpringBootTest
class UserControllerSlowTest {
    // Loads everything
}
```

### Mock External Dependencies

```java
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @MockBean
    private PaymentGateway paymentGateway; // External service

    @MockBean
    private OrderService orderService;

    @Test
    void shouldProcessPayment() throws Exception {
        when(paymentGateway.charge(any())).thenReturn(PaymentResult.success());

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderId\":1,\"amount\":99.99}"))
            .andExpect(status().isOk());
    }
}
```

### Use TestRestTemplate for Integration Tests

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndGetUser() {
        // Create user
        User newUser = new User("John", "john@example.com");
        ResponseEntity<User> createResponse = restTemplate.postForEntity(
            "/api/users", newUser, User.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        // Get user
        ResponseEntity<User> getResponse = restTemplate.getForEntity(
            "/api/users/" + createResponse.getBody().getId(), User.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("John", getResponse.getBody().getName());
    }
}
```

## Resources

- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Spring Test Documentation](https://docs.spring.io/spring-framework/reference/testing.html)
- [Testcontainers with Spring](https://www.testcontainers.org/usage/spring-boot.html)

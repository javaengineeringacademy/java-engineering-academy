# 11.6 Integration Testing

## 1. Introduction

Integration testing verifies how multiple components work together. This module covers Spring Boot Test, Testcontainers for database testing, and strategies for testing integrations between services, databases, and external APIs.

## 2. Learning Objectives

- Use Spring Boot Test for integration testing
- Apply Testcontainers for database testing
- Test REST APIs with MockMvc and WebTestClient
- Verify database operations with @DataJpaTest
- Test message queue integrations

## 3. Prerequisites

- Spring Boot basics
- Understanding of dependency injection
- Familiarity with JUnit 5
- Basic database knowledge

## 4. Why This Concept Exists

Unit tests verify components in isolation, but real bugs often occur at integration points. Integration testing ensures:
- Database queries work correctly
- API contracts are maintained
- Service interactions function properly
- Configuration is correct

## 5. Problem Statement

How do we test that our application components work together correctly, including databases, APIs, and message queues?

## 6. Theory

### Integration Test Types

| Type | Scope | Tools |
|------|-------|-------|
| Component | Multiple classes | Spring Context |
| Database | Repository layer | @DataJpaTest |
| API | REST endpoints | MockMvc, WebTestClient |
| Service | Service interactions | @SpringBootTest |
| E2E | Full application | Testcontainers |

### Testcontainers

- Docker-based integration testing
- Real database instances
- Disposable containers
- Parallel execution support
- Clean state per test

### Spring Boot Test Slices

- `@WebMvcTest` - Controller layer only
- `@DataJpaTest` - JPA repositories only
- `@RestClientTest` - REST clients only
- `@JsonTest` - JSON serialization
- `@SpringBootTest` - Full context

## 7. Internal Working

### Spring Boot Test Execution

1. Test slices load only relevant beans
2. Auto-configuration creates test context
3. Embedded databases or Testcontainers started
4. Transactions managed for isolation
5. Context cached for performance

### Testcontainers Lifecycle

1. Docker images pulled/cached
2. Containers started with random ports
3. Applications connect via exposed ports
4. Containers stopped after tests
5. Resources cleaned up

## 8. JVM Perspective

- Test context shares JVM with tests
- Embedded databases run in-process
- Testcontainers run in Docker (separate JVM)
- Connection pools managed per context
- Transactions isolated via AOP

## 9. Memory Representation

```
Integration Test Memory:
┌─────────────────────────────────┐
│         Test JVM                │
├─────────────────────────────────┤
│  ┌───────────────────────────┐  │
│  │   Spring Context          │  │
│  │   - Bean definitions      │  │
│  │   - Configuration         │  │
│  │   - AOP proxies           │  │
│  └───────────────────────────┘  │
├─────────────────────────────────┤
│  ┌───────────────────────────┐  │
│  │   Test Infrastructure     │  │
│  │   - MockMvc/WebTestClient │  │
│  │   - TestRestTemplate      │  │
│  │   - @MockBean             │  │
│  └───────────────────────────┘  │
├─────────────────────────────────┤
│  ┌───────────────────────────┐  │
│  │   Database Connection     │  │
│  │   - H2 (embedded)         │  │
│  │   - Testcontainers (Docker)│  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

## 10. Architecture Diagram

```mermaid
graph TD
    A[Integration Testing] --> B[Spring Boot Test]
    A --> C[Testcontainers]
    A --> D[API Testing]
    
    B --> B1[@SpringBootTest]
    B --> B2[Test Slices]
    B --> B3[@MockBean]
    
    C --> C1[Database Containers]
    C --> C2[Message Queues]
    C --> C3[External Services]
    
    D --> D1[MockMvc]
    D --> D2[WebTestClient]
    D --> D3[RestTemplate]
    
    B1 --> E[Test Execution]
    C1 --> E
    D1 --> E
    
    E --> F[Transaction Management]
    E --> G[Context Caching]
```

## 11. Flow Diagram

```mermaid
flowchart TD
    Start([Integration Test Start]) --> Context[Load Spring Context]
    
    Context --> Slice{Test Slice?}
    
    Slice -->|WebMvcTest| Controller[Load Controllers Only]
    Slice -->|DataJpaTest| Repo[Load Repositories Only]
    Slice -->|SpringBootTest| Full[Load Full Context]
    
    Controller --> Setup[Setup Test Data]
    Repo --> Setup
    Full --> Setup
    
    Setup --> Test[Execute Test]
    
    Test --> DB{Database?}
    DB -->|Yes| Container[Start Testcontainer]
    DB -->|No| InMemory[Use Embedded DB]
    
    Container --> Exec[Run Test]
    InMemory --> Exec
    
    Exec --> Verify[Verify Results]
    Verify --> Cleanup[Cleanup Resources]
    
    Cleanup --> More{More Tests?}
    More -->|Yes| Setup
    More -->|No| End([Complete])
```

## 12. Syntax

```java
// Spring Boot Test
@SpringBootTest
class ApplicationTest {
    @Autowired
    private ApplicationContext context;
    
    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}

// WebMvc Test
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void shouldGetUser() throws Exception {
        when(userService.findById(1L)).thenReturn(new User("John"));
        
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}

// DataJpa Test
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void shouldFindUserByUsername() {
        User user = new User("john", "john@example.com");
        entityManager.persistAndFlush(user);
        
        Optional<User> found = userRepository.findByUsername("john");
        assertTrue(found.isPresent());
    }
}

// Testcontainers
@SpringBootTest
@Testcontainers
class IntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }
}
```

## 13. Easy Example

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationStartupTest {
    
    @Autowired
    private ApplicationContext context;
    
    @Test
    void contextLoads() {
        // Verify Spring context starts successfully
        assertNotNull(context);
        assertTrue(context.getBeanDefinitionCount() > 0);
    }
    
    @Test
    void shouldLoadAllBeans() {
        // Verify critical beans are present
        assertTrue(context.containsBean("userRepository"));
        assertTrue(context.containsBean("userService"));
        assertTrue(context.containsBean("userController"));
    }
}
```

## 14. Medium Example

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        User user1 = new User(1L, "John", "john@example.com");
        User user2 = new User(2L, "Jane", "jane@example.com");
        
        when(userService.findAll()).thenReturn(List.of(user1, user2));
        when(userService.findById(1L)).thenReturn(user1);
    }
    
    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("John"))
            .andExpect(jsonPath("$[1].name").value("Jane"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));
        
        verify(userService, times(1)).findAll();
    }
    
    @Test
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
        
        verify(userService).findById(1L);
    }
    
    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.findById(99L)).thenThrow(new UserNotFoundException("User not found"));
        
        mockMvc.perform(get("/api/users/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("User not found"));
    }
    
    @Test
    void shouldCreateUser() throws Exception {
        User newUser = new User(3L, "Bob", "bob@example.com");
        when(userService.create(any(User.class))).thenReturn(newUser);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Bob\",\"email\":\"bob@example.com\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Bob"));
        
        verify(userService).create(any(User.class));
    }
}
```

## 15. Hard Example

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryIntegrationTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @MockBean
    private CacheService cacheService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@example.com");
        testUser.setPassword("encodedPassword");
        entityManager.persistAndFlush(testUser);
    }
    
    @Test
    void shouldPersistUser() {
        // Arrange
        User newUser = new User("newuser", "new@example.com");
        
        // Act
        User saved = userRepository.save(newUser);
        
        // Assert
        assertNotNull(saved.getId());
        assertEquals("newuser", saved.getUsername());
        
        // Verify in database
        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("new@example.com", found.get().getEmail());
    }
    
    @Test
    void shouldFindUserByUsername() {
        // Act
        Optional<User> found = userRepository.findByUsername("testuser");
        
        // Assert
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }
    
    @Test
    void shouldUpdateUser() {
        // Arrange
        testUser.setEmail("updated@example.com");
        
        // Act
        User updated = userRepository.save(testUser);
        
        // Assert
        assertEquals("updated@example.com", updated.getEmail());
        
        // Verify database state
        Optional<User> found = userRepository.findById(testUser.getId());
        assertEquals("updated@example.com", found.get().getEmail());
    }
    
    @Test
    void shouldDeleteUser() {
        // Act
        userRepository.delete(testUser);
        
        // Assert
        Optional<User> found = userRepository.findById(testUser.getId());
        assertFalse(found.isPresent());
    }
    
    @Test
    void shouldHandleConcurrentModification() {
        // This test verifies optimistic locking
        User user = userRepository.findByUsername("testuser").orElseThrow();
        user.setVersion(0L); // Simulate stale version
        
        assertThrows(OptimisticLockingFailureException.class,
            () -> userRepository.save(user));
    }
}
```

## 16. Enterprise Example

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("integration")
class EnterpriseIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("enterprise_test")
        .withUsername("test")
        .withPassword("test")
        .withInitScript("schema.sql");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private PaymentService paymentService;
    
    @MockBean
    private NotificationService notificationService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @BeforeEach
    void setUp() {
        // Clean up test data
        orderRepository.deleteAll();
        userRepository.deleteAll();
        
        // Setup test user
        User user = new User("enterprise_user", "enterprise@example.com");
        userRepository.save(user);
    }
    
    @Test
    void shouldProcessOrderEndToEnd() {
        // Arrange
        User user = userRepository.findByUsername("enterprise_user").orElseThrow();
        OrderRequest request = new OrderRequest(user.getId(), "PRODUCT-1", 2, 99.99);
        
        // Act
        OrderResponse response = orderService.createOrder(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("CREATED", response.getStatus());
        
        // Verify order persisted
        Order order = orderRepository.findById(response.getOrderId()).orElseThrow();
        assertEquals(2, order.getQuantity());
        assertEquals(199.98, order.getTotalAmount(), 0.01);
        
        // Verify notification sent
        verify(notificationService).sendOrderConfirmation(anyString(), eq(user.getEmail()));
    }
    
    @Test
    void shouldHandlePaymentFailure() {
        // Arrange
        User user = userRepository.findByUsername("enterprise_user").orElseThrow();
        OrderRequest request = new OrderRequest(user.getId(), "PRODUCT-2", 1, 149.99);
        
        // Mock payment to fail
        doThrow(new PaymentException("Card declined"))
            .when(paymentService).processPayment(any());
        
        // Act & Assert
        assertThrows(PaymentException.class,
            () -> orderService.createOrder(request));
        
        // Verify order not created
        assertEquals(0, orderRepository.count());
        
        // Verify failure notification
        verify(notificationService).sendPaymentFailure(eq(user.getEmail()));
    }
    
    @Test
    void shouldMaintainDataConsistency() {
        // Arrange
        User user = userRepository.findByUsername("enterprise_user").orElseThrow();
        
        // Act - Create multiple orders
        for (int i = 0; i < 5; i++) {
            OrderRequest request = new OrderRequest(user.getId(), "PRODUCT-" + i, 1, 10.00);
            orderService.createOrder(request);
        }
        
        // Assert - Verify consistency
        assertEquals(5, orderRepository.count());
        
        // Verify user's orders
        List<Order> userOrders = orderRepository.findByUserId(user.getId());
        assertEquals(5, userOrders.size());
    }
}
```

## 17. Performance

| Test Type | Startup Time | Execution | Memory |
|-----------|-------------|-----------|--------|
| @WebMvcTest | ~2s | Fast | Low |
| @DataJpaTest | ~3s | Medium | Medium |
| @SpringBootTest | ~5s | Medium | High |
| Testcontainers | ~10s | Fast | High |

**Performance Tips:**
- Use test slices (@WebMvcTest, @DataJpaTest) when possible
- Cache Spring context between tests
- Use @Transactional for database tests
- Share Testcontainers across test classes
- Use @TestConfiguration for test-specific beans

## 18. Time & Space Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Context Load | O(b) | O(b) |
| Container Start | O(1) | O(c) |
| Test Execution | O(t) | O(m) |
| Cleanup | O(d) | O(1) |

Where: b = beans, c = container resources, t = test operations, m = mock objects, d = data operations

## 19. Thread Safety

- Spring context is thread-safe
- Testcontainers containers are thread-safe
- Database connections must be pooled
- Use @Transactional for isolation
- MockBeans are thread-safe

## 20. Best Practices

1. **Use test slices** for focused testing
2. **Share Testcontainers** across test classes
3. **Use @Transactional** for database tests
4. **Mock external services** with @MockBean
5. **Use @TestConfiguration** for test beans
6. **Clean up data** in @BeforeEach
7. **Use profiles** for different environments
8. **Cache context** when possible
9. **Use @DynamicPropertySource** for configuration
10. **Verify database state** not just responses

## 21. Common Mistakes

1. **Loading full context** when slice would suffice
2. **Not cleaning up data** between tests
3. **Hardcoding ports** in Testcontainers
4. **Mocking everything** defeats integration testing
5. **Ignoring transactions** leads to flaky tests
6. **Not testing error scenarios**
7. **Overusing @SpringBootTest**
8. **Forgetting @ActiveProfiles**
9. **Not verifying database state**
10. **Ignoring test data isolation**

## 22. Pitfalls

- **Slow test execution** from full context loading
- **Flaky tests** from external dependencies
- **Context pollution** from shared state
- **Port conflicts** with Testcontainers
- **Memory leaks** from unclosed containers
- **Transaction rollback issues**

## 23. Debugging Tips

1. **Use @Sql** for test data setup
2. **Enable SQL logging** for database tests
3. **Check Testcontainer logs** for issues
4. **Use @DirtiesContext** when needed
5. **Verify mock interactions** with verify()
6. **Check Spring profiles** are active
7. **Use debugger** with @Disabled for investigation

## 24. Comparison Table

| Feature | Embedded DB | Testcontainers | Docker Compose |
|---------|-------------|----------------|----------------|
| Speed | Fast | Medium | Slow |
| Realism | Low | High | High |
| Setup | Minimal | Medium | Complex |
| Isolation | High | High | Medium |
| Portability | High | Medium | Low |

## 25. Decision Tree

```
Which integration test approach?
│
├─ Testing controllers only?
│  └─ Use @WebMvcTest
│
├─ Testing repositories only?
│  └─ Use @DataJpaTest
│
├─ Testing full application?
│  ├─ Need real database? → Use Testcontainers
│  └─ Need mock DB? → Use @SpringBootTest with H2
│
├─ Testing external API?
│  └─ Use WireMock or MockRestServiceServer
│
└─ Testing message queues?
   └─ Use Testcontainers with embedded broker
```

## 26. Interview Questions

1. **What is the difference between unit and integration tests?**
   - Answer: Unit tests verify components in isolation; integration tests verify component interactions.

2. **When should you use @SpringBootTest vs @WebMvcTest?**
   - Answer: @SpringBootTest for full context; @WebMvcTest for controller layer only.

3. **What are Testcontainers and why use them?**
   - Answer: Docker-based containers for realistic integration testing with real databases/services.

4. **How do you ensure test data isolation?**
   - Answer: Use @Transactional, clean data in @BeforeEach, use separate databases.

5. **What is @MockBean and how does it differ from @Mock?**
   - Answer: @MockBean replaces Spring beans; @Mock creates standalone mocks.

6. **How do you test REST APIs in Spring Boot?**
   - Answer: Use MockMvc for servlet apps or WebTestClient for reactive apps.

7. **What is the test context cache and why is it important?**
   - Answer: Caches Spring context between tests to avoid reloading, improving performance.

8. **How do you handle external service dependencies in tests?**
   - Answer: Mock with @MockBean or use WireMock for HTTP stubbing.

9. **What is @DataJpaTest and what does it provide?**
   - Answer: Tests JPA repositories with embedded database and automatic rollback.

10. **How do you test database migrations?**
    - Answer: Use Testcontainers to run actual migrations against real database.

11. **What are the benefits of using profiles in tests?**
    - Answer: Allow different configurations for test vs production environments.

12. **How do you test transactional behavior?**
    - Answer: Use @Transactional and verify rollback/commit behavior.

13. **What is @DynamicPropertySource used for?**
    - Answer: Dynamically configures Spring properties, useful for Testcontainers.

14. **How do you test async operations?**
    - Answer: Use awaitility or verify with timeouts.

15. **What are common integration test anti-patterns?**
    - Answer: Testing too much, not cleaning data, using production databases, ignoring errors.

## 27. Exercises

### Beginner

1. **Basic Spring Boot Test**
   - Create @SpringBootTest for application context
   - Verify all beans are loaded
   - Test a simple service method

2. **Controller Test**
   - Use @WebMvcTest for UserController
   - Mock UserService with @MockBean
   - Test GET and POST endpoints

### Intermediate

3. **Repository Test**
   - Use @DataJpaTest for UserRepository
   - Test CRUD operations
   - Verify custom queries

4. **Testcontainers Setup**
   - Create PostgreSQL container
   - Configure dynamic properties
   - Test database operations

### Advanced

5. **Enterprise Integration Test**
   - Create full integration test with multiple services
   - Use Testcontainers for database
   - Mock external APIs
   - Verify end-to-end flow

6. **Performance Testing**
   - Measure context load time
   - Optimize test execution
   - Implement parallel test execution

## 28. Summary

Integration testing ensures components work together correctly. Spring Boot Test provides slices for focused testing, Testcontainers enables realistic database testing, and proper strategies ensure fast, reliable integration tests.

## 29. References

- Spring Boot Testing: https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing
- Testcontainers: https://www.testcontainers.org/
- MockMvc Documentation: https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework
- Baeldung Integration Testing: https://www.baeldung.com/spring-boot-testing

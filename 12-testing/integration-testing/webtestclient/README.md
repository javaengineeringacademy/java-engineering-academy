# WebTestClient

## Overview

WebTestClient is a testing facade for testing WebFlux server endpoints. It can also be used to test any server endpoint via a mock request and response strategy.

## Setup

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-starter-webflux'
}
```

## Testing WebFlux Endpoints

### @WebFluxTest

```java
@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void shouldGetUser() {
        when(userService.findById(1L))
            .thenReturn(Mono.just(new User(1, "John", "john@example.com")));

        webTestClient.get()
            .uri("/api/users/{id}", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo("John")
            .jsonPath("$.email").isEqualTo("john@example.com");
    }
}
```

### Testing Reactive Streams

```java
@Test
void shouldReturnFluxOfUsers() {
    List<User> users = List.of(
        new User(1, "John", "john@example.com"),
        new User(2, "Jane", "jane@example.com")
    );

    when(userService.findAll()).thenReturn(Flux.fromIterable(users));

    webTestClient.get()
        .uri("/api/users")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(User.class)
        .hasSize(2)
        .contains(new User(1, "John", "john@example.com"));
}
```

### Testing Mono

```java
@Test
void shouldCreateUser() {
    User newUser = new User(null, "John", "john@example.com");
    User savedUser = new User(1L, "John", "john@example.com");

    when(userService.create(any())).thenReturn(Mono.just(savedUser));

    webTestClient.post()
        .uri("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(User.class)
        .value(user -> {
            assertNotNull(user.getId());
            assertEquals("John", user.getName());
        });
}
```

## Request Building

### GET Requests

```java
@Test
void shouldGetUsersWithQueryParams() {
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/users")
            .queryParam("page", 0)
            .queryParam("size", 10)
            .queryParam("sort", "name")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(User.class)
        .hasSize(10);
}
```

### POST Requests

```java
@Test
void shouldCreateUserWithHeaders() {
    User newUser = new User("John", "john@example.com");

    webTestClient.post()
        .uri("/api/users")
        .header("X-Request-Id", UUID.randomUUID().toString())
        .header("Authorization", "Bearer token")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated();
}
```

### PUT Requests

```java
@Test
void shouldUpdateUser() {
    User updated = new User(1L, "John Updated", "john@example.com");

    webTestClient.put()
        .uri("/api/users/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(updated)
        .exchange()
        .expectStatus().isOk()
        .expectBody(User.class)
        .value(user -> assertEquals("John Updated", user.getName()));
}
```

### DELETE Requests

```java
@Test
void shouldDeleteUser() {
    webTestClient.delete()
        .uri("/api/users/{id}", 1L)
        .exchange()
        .expectStatus().isNoContent();
}
```

## Response Assertions

### Status Assertions

```java
webTestClient.get()
    .uri("/api/users/1")
    .exchange()
    .expectStatus().isOk();

webTestClient.post()
    .uri("/api/users")
    .exchange()
    .expectStatus().isCreated();

webTestClient.delete()
    .uri("/api/users/1")
    .exchange()
    .expectStatus().isNoContent();
```

### Header Assertions

```java
webTestClient.get()
    .uri("/api/users/1")
    .exchange()
    .expectHeader().contentType(MediaType.APPLICATION_JSON)
    .expectHeader().exists("X-Request-Id")
    .expectHeader().value("X-Custom-Header", "custom-value");
```

### Body Assertions

```java
// JSONPath assertions
webTestClient.get()
    .uri("/api/users/1")
    .exchange()
    .expectBody()
    .jsonPath("$.id").isEqualTo(1)
    .jsonPath("$.name").isNotEmpty()
    .jsonPath("$.email").value(email -> assertTrue(email.contains("@")));

// Object assertions
webTestClient.get()
    .uri("/api/users/1")
    .exchange()
    .expectBody(User.class)
    .value(user -> {
        assertEquals("John", user.getName());
        assertNotNull(user.getId());
    });
```

### Error Assertions

```java
@Test
    void shouldReturn404WhenUserNotFound() {
        webTestClient.get()
            .uri("/api/users/999")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").isEqualTo("User not found")
            .jsonPath("$.status").isEqualTo(404);
    }

@Test
    void shouldReturn400ForInvalidInput() {
        User invalidUser = new User("", "invalid-email");

        webTestClient.post()
            .uri("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidUser)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.errors").isArray();
    }
```

## Mock Server Setup

### Standalone WebTestClient

```java
class StandaloneTest {

    private WebTestClient testClient;

    @BeforeEach
    void setup() {
        UserController controller = new UserController(userService);

        testClient = WebTestClient
            .bindToController(controller)
            .configureWebTestClient()
            .baseUrl("/api")
            .defaultHeader(MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Test
    void shouldTestController() {
        when(userService.findById(1L))
            .thenReturn(Mono.just(new User(1, "John", "john@example.com")));

        testClient.get()
            .uri("/users/{id}", 1L)
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class)
            .value(user -> assertEquals("John", user.getName()));
    }
}
```

### RouterFunction Testing

```java
class RouterFunctionTest {

    private WebTestClient testClient;

    @BeforeEach
    void setup() {
        RouterFunction<ServerResponse> routes = route(GET("/api/users/{id}"), this::getUser);

        testClient = WebTestClient
            .bindToRouterFunction(routes)
            .build();
    }

    private Mono<ServerResponse> getUser(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return ServerResponse.ok()
            .bodyValue(new User(id, "John", "john@example.com"));
    }

    @Test
    void shouldTestRouter() {
        testClient.get()
            .uri("/api/users/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class)
            .value(user -> assertEquals("John", user.getName()));
    }
}
```

## Testing Filters

```java
@WebFluxTest
class FilterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldApplyFilter() {
        webTestClient.get()
            .uri("/api/users/1")
            .header("X-Request-Id", "test-123")
            .exchange()
            .expectHeader().valueEquals("X-Response-Id", "test-123");
    }
}
```

## Testing Error Handling

```java
@Test
    void shouldHandleBusinessException() {
        when(userService.findById(999L))
            .thenReturn(Mono.error(new UserNotFoundException("User not found")));

        webTestClient.get()
            .uri("/api/users/999")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.message").isEqualTo("User not found");
    }

@Test
    void shouldHandleValidationException() {
        when(userService.create(any()))
            .thenReturn(Mono.error(new ValidationException("Invalid input")));

        webTestClient.post()
            .uri("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new User("", "invalid"))
            .exchange()
            .expectStatus().isBadRequest();
    }
```

## Integration Testing

### Full Stack Test

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll().block();
    }

    @Test
    void shouldCreateAndRetrieveUser() {
        User newUser = new User(null, "John", "john@example.com");

        // Create
        webTestClient.post()
            .uri("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newUser)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(User.class)
            .value(created -> {
                assertNotNull(created.getId());

                // Retrieve
                webTestClient.get()
                    .uri("/api/users/{id}", created.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(User.class)
                    .value(retrieved -> assertEquals("John", retrieved.getName()));
            });
    }
}
```

## Test Data

### Body Publishers

```java
@Test
    void shouldUploadFile() {
        byte[] fileContent = "test content".getBytes();

        webTestClient.post()
            .uri("/api/upload")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData("file", 
                new ByteArrayResource(fileContent) {
                    @Override
                    public String getFilename() {
                        return "test.txt";
                    }
                }))
            .exchange()
            .expectStatus().isOk();
    }
```

### JSON Assertions

```java
String json = """
    {
        "name": "John",
        "email": "john@example.com",
        "age": 25
    }
    """;

webTestClient.post()
    .uri("/api/users")
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(json)
    .exchange()
    .expectStatus().isCreated();
```

## Best Practices

### Use MockBean for External Dependencies

```java
@WebFluxTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService; // Mock external service

    @MockBean
    private EmailService emailService; // Mock email service

    @Test
    void shouldCreateUser() {
        when(userService.create(any())).thenReturn(Mono.just(savedUser));

        webTestClient.post()
            .uri("/api/users")
            .bodyValue(newUser)
            .exchange()
            .expectStatus().isCreated();

        verify(emailService).sendWelcomeEmail(any());
    }
}
```

### Test Reactor Operators

```java
@Test
    void shouldHandleBackpressure() {
        Flux<User> users = Flux.range(1, 1000)
            .map(i -> new User((long) i, "User" + i, "user" + i + "@example.com"));

        when(userService.findAll()).thenReturn(users);

        webTestClient.get()
            .uri("/api/users")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(User.class)
            .hasSize(1000);
    }
```

## Resources

- [Spring WebFlux Testing](https://docs.spring.io/spring-framework/reference/web/webflux-webtestclient/testing.html)
- [WebTestClient Reference](https://docs.spring.io/spring-framework/reference/web/webflux-webtestclient.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

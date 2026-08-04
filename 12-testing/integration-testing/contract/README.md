# Contract Testing

## Overview

Contract testing is a testing technique that verifies interactions between service providers and consumers. It ensures that services can communicate with each other by validating that the API contract is maintained.

## Types of Contract Testing

### Consumer-Driven Contracts (CDC)

Consumer-driven contracts define expectations from the consumer's perspective. The provider must fulfill these expectations.

### Provider-Driven Contracts

Provider-driven contracts define what the provider offers. Consumers must adapt to the provider's contract.

## Pact (Consumer-Driven Contracts)

### Dependencies

```xml
<dependency>
    <groupId>au.com.dius</groupId>
    <artifactId>pact-jvm-consumer-junit5</artifactId>
    <version>4.6.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>au.com.dius</groupId>
    <artifactId>pact-jvm-provider-junit5</artifactId>
    <version>4.6.3</version>
    <scope>test</scope>
</dependency>
```

### Consumer Test

```java
@PactTestFor(pactVersion = PactVer.V3)
class UserServiceConsumerTest {

    @Pact
    public RequestResponsePact getUserPact(PactDslWithProvider builder) {
        return builder
            .given("user exists")
            .uponReceiving("a request for user 1")
            .path("/api/users/1")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(new PactDslJsonBody()
                .integerType("id", 1)
                .stringType("name", "John")
                .stringType("email", "john@example.com"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getUserPact")
    void shouldGetUser(MockServer mockServer) {
        // Given
        UserService client = new UserService(mockServer.getUrl());

        // When
        User user = client.getUser(1L);

        // Then
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }
}
```

### Provider Test

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("UserService")
@PactFolder("pacts")
class UserServiceProviderTest {

    @LocalServerPort
    private int port;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void setUp(PactVerificationContext context) {
        System.setProperty("pact.provider.version", "1.0.0");
        System.setProperty("pact.provider.tag", "test");
    }

    @State("user exists")
    void userExists() {
        // Setup state
        userRepository.save(new User(1L, "John", "john@example.com"));
    }
}
```

## Spring Cloud Contract

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-contract-stub-runner</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-contract-verifier</artifactId>
    <scope>test</scope>
</dependency>
```

### Contract Definition (Groovy)

```groovy
Contract.make {
    description "Should get user by ID"
    name "getUserById"
    request {
        method GET()
        url("/api/users/1")
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body(
            id: 1,
            name: "John",
            email: "john@example.com"
        )
    }
}
```

### Contract Definition (YAML)

```yaml
description: Should get user by ID
name: getUserById
request:
  method: GET
  url: /api/users/1
  headers:
    Content-Type: application/json
response:
  status: 200
  headers:
    Content-Type: application/json
  body:
    id: 1
    name: John
    email: john@example.com
```

### Generated Test

```java
public class UserContractTest extends ContractVerifierBase {

    @Test
    public void validate_getUserById() {
        // Given
        String url = "/api/users/1";

        // When
        Response response = restTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("\"name\":\"John\"");
    }
}
```

### Stub Runner

```java
@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.example:users-service:+:stubs:8080",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
class UserServiceTest {

    @Autowired
    private StubRunnerRule stubRunner;

    @Test
    void shouldGetUser() {
        // Stub is running on port 8080
        // Test your service against it
    }
}
```

## Pact Broker

### Publishing Pacts

```java
@PactTestFor(pactVersion = PactVer.V3)
@PactBroker(
    host = "localhost",
    port = "9292",
    scheme = "http"
)
class UserServiceConsumerPactTest {

    @Pact
    public RequestResponsePact getUserPact(PactDslWithProvider builder) {
        // Pact definition
    }
}
```

### Verifying Against Broker

```java
@SpringBootTest
@Provider("UserService")
@PactBroker(
    host = "localhost",
    port = "9292"
)
class UserServiceProviderVerificationTest {

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
```

## Pact Flow

### Consumer Side

1. Consumer writes test describing expected behavior
2. Test generates pact file (JSON)
3. Pact file is published to broker

### Provider Side

1. Provider retrieves pact files from broker
2. Provider verifies it fulfills the contract
3. Verification results are published to broker

## Contract Testing Patterns

### Request/Response Contracts

```java
@Pact
public RequestResponsePact createPact(PactDslWithProvider builder) {
    return builder
        .given("user exists")
        .uponReceiving("get user request")
        .path("/api/users/1")
        .method("GET")
        .willRespondWith()
        .status(200)
        .body(new PactDslJsonBody()
            .integerType("id", 1)
            .stringType("name", "John"))
        .toPact();
}
```

### State-Based Contracts

```java
@Pact
public RequestResponsePact stateBasedPact(PactDslWithProvider builder) {
    return builder
        .given("user with ID 1 exists")
        .uponReceiving("get existing user")
        .path("/api/users/1")
        .method("GET")
        .willRespondWith()
        .status(200)
        .body(new PactDslJsonBody()
            .integerType("id", 1)
            .stringType("name", "John"))
        .given("no user with ID 999")
        .uponReceiving("get non-existing user")
        .path("/api/users/999")
        .method("GET")
        .willRespondWith()
        .status(404)
        .toPact();
}
```

### Message Contracts (Async)

```java
@Pact
public MessagePact userCreatedPact(PactDslWithProvider builder) {
    PactDslJsonBody body = new PactDslJsonBody()
        .integerType("userId", 1)
        .stringType("name", "John")
        .stringType("email", "john@example.com");

    return builder
        .hasPactWith("OrderService")
        .expectsToReceive("user created event")
        .withContent(body)
        .toPact();
}

@Test
@PactTestFor(pactMethod = "userCreatedPact")
void shouldHandleUserCreatedEvent(MessagePact pact, PactDslJsonBody body) {
    // Verify message handling
    assertEquals(1, body.getInteger("userId"));
    assertEquals("John", body.getString("name"));
}
```

## Verification Strategies

### Tag-Based Verification

```java
@SpringBootTest
@Provider("UserService")
@PactBroker(
    host = "localhost",
    port = "9292",
    providerTags = "main",
    consumerTags = "main"
)
class TaggedProviderTest {

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
```

### Pending Pact

```java
@SpringBootTest
@Provider("UserService")
@PactBroker(
    host = "localhost",
    port = "9292",
    enablePendingPacts = "true"
)
class PendingPactTest {

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
```

## Best Practices

### Consumer Side

```java
// Good: Test actual behavior
@Test
void shouldGetUser() {
    User user = userService.getUser(1L);
    assertNotNull(user);
    assertEquals("John", user.getName());
}

// Bad: Test implementation details
@Test
void shouldCallApi() {
    verify(restTemplate).getForObject("/api/users/1", User.class);
}
```

### Provider Side

```java
// Good: Use state-based setup
@State("user exists")
void userExists() {
    userRepository.save(new User(1L, "John", "john@example.com"));
}

// Bad: Use static data
@BeforeEach
void setup() {
    // Hardcoded test data
}
```

### Contract Design

```groovy
// Good: Clear, descriptive contracts
Contract.make {
    description "Should return 404 when user not found"
    name "getUserNotFound"
    request {
        method GET()
        url("/api/users/999")
    }
    response {
        status 404
        body(
            error: "User not found"
        )
    }
}

// Bad: Vague contracts
Contract.make {
    description "Should work"
    request {
        method GET()
        url("/api/users/1")
    }
    response {
        status 200
    }
}
```

## CI/CD Integration

### GitHub Actions

```yaml
jobs:
  consumer-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Run Consumer Tests
        run: mvn test -Pconsumer
      - name: Publish Pacts
        run: mvn pact:publish

  provider-tests:
    runs-on: ubuntu-latest
    needs: consumer-tests
    steps:
      - uses: actions/checkout@v4
      - name: Verify Provider
        run: mvn test -Pprovider
```

## Resources

- [Pact Documentation](https://docs.pact.io/)
- [Spring Cloud Contract](https://spring.io/projects/spring-cloud-contract)
- [Pact JVM](https://github.com/pact-foundation/pact-jvm)
- [Consumer-Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html)

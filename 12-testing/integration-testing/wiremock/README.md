# WireMock

## Overview

WireMock is a simulator for HTTP-based APIs. It enables you to stub HTTP services, record and replay real interactions, and verify test expectations. It's useful for testing HTTP clients without relying on external services.

## Setup

### Maven

```xml
<dependency>
    <groupId>com.github.tomakehurst</groupId>
    <artifactId>wiremock-jre8</artifactId>
    <version>2.35.0</version>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
dependencies {
    testImplementation 'com.github.tomakehurst:wiremock-jre8:2.35.0'
}
```

## Basic Stubbing

### Static Method

```java
class WireMockTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    void shouldStubGetRequest() {
        stubFor(get(urlEqualTo("/api/users/1"))
            .withHeader("Accept", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"id\":1,\"name\":\"John\"}")));

        // Your test code here
        Response response = httpClient.get("http://localhost:8089/api/users/1");
        assertEquals(200, response.getCode());
    }
}
```

### WireMockServer

```java
class WireMockServerTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void shouldStubRequest() {
        stubFor(get(urlEqualTo("/api/products"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("[{\"id\":1,\"name\":\"Laptop\"}]")));

        // Test code
    }
}
```

## Response Configuration

### Status Codes

```java
stubFor(get(urlEqualTo("/api/users/1"))
    .willReturn(aResponse()
        .withStatus(200)));

stubFor(get(urlEqualTo("/api/users/999"))
    .willReturn(aResponse()
        .withStatus(404)
        .withBody("{\"error\":\"User not found\"}")));

stubFor(get(urlEqualTo("/api/users/500"))
    .willReturn(aResponse()
        .withStatus(500)
        .withBody("Internal Server Error")));
```

### Headers

```java
stubFor(get(urlEqualTo("/api/data"))
    .withHeader("Authorization", equalTo("Bearer token"))
    .willReturn(aResponse()
        .withHeader("Content-Type", "application/json")
        .withHeader("X-Request-Id", UUID.randomUUID().toString())
        .withHeader("Cache-Control", "no-cache")
        .withBody("{\"data\":\"value\"}")));
```

### Response Body

```java
// JSON body
stubFor(get(urlEqualTo("/api/user"))
    .willReturn(aResponse()
        .withHeader("Content-Type", "application/json")
        .withBody("{\"id\":1,\"name\":\"John\",\"email\":\"john@example.com\"}")));

// XML body
stubFor(get(urlEqualTo("/api/user/xml"))
    .willReturn(aResponse()
        .withHeader("Content-Type", "application/xml")
        .withBody("<user><id>1</id><name>John</name></user>"));

// File body
stubFor(get(urlEqualTo("/api/user/file"))
    .willReturn(aResponse()
        .withBodyFile("user.json"))); // From __files/user.json
```

### Delayed Response

```java
// Fixed delay
stubFor(get(urlEqualTo("/api/slow"))
    .willReturn(aResponse()
        .withStatus(200)
        .withFixedDelay(2000))); // 2 second delay

// Random delay
stubFor(get(urlEqualTo("/api/random-delay"))
    .willReturn(aResponse()
        .withStatus(200)
        .withRandomDelay(100, 1000))); // 100ms to 1 second
```

## URL Matching

### Exact Match

```java
stubFor(get(urlEqualTo("/api/users/1"))
    .willReturn(ok()));
```

### Path Matching

```java
stubFor(get(urlPathEqualTo("/api/users"))
    .willReturn(ok()));

stubFor(get(urlPathMatching("/api/users/\\d+"))
    .willReturn(ok()));
```

### Query Parameters

```java
stubFor(get(urlEqualTo("/api/users?page=0&size=10"))
    .willReturn(ok()));

stubFor(get(urlPathEqualTo("/api/users"))
    .withQueryParam("page", equalTo("0"))
    .withQueryParam("size", equalTo("10"))
    .willReturn(ok()));
```

## Request Matching

### Headers

```java
stubFor(get(urlEqualTo("/api/secure"))
    .withHeader("Authorization", containing("Bearer"))
    .withHeader("Accept", equalTo("application/json"))
    .willReturn(ok()));
```

### Body Matching

```java
stubFor(post(urlEqualTo("/api/users"))
    .withRequestBody(containing("John"))
    .withRequestBody(jsonPath("$.name", equalTo("John")))
    .willReturn(created()));
```

### Content Type

```java
stubFor(post(urlEqualTo("/api/users"))
    .withHeader("Content-Type", equalTo("application/json"))
    .willReturn(created()));
```

## Dynamic Responses

### Response Transformers

```java
stubFor(get(urlEqualTo("/api/user"))
    .willReturn(ok()
        .withTransformers("response-template")
        .withBody("{\"id\":{{randomValue type='UUID'}},\"name\":\"John\"}")));
```

### Fixed-Width Response

```java
stubFor(get(urlEqualTo("/api/fixed"))
    .willReturn(ok()
        .withFixedDelay(2000)
        .withBody("Response with delay")));
```

### Proxied Responses

```java
// Proxy to real API
stubFor(get(urlPathMatching("/api/.*"))
    .willReturn(aResponse()
        .proxiedFrom("https://api.real-service.com")));
```

## Record and Replay

### Recording Mode

```java
WireMockServer wireMockServer = new WireMockServer(
    WireMockConfiguration.options()
        .port(8089)
        .withRecordingMode(RecordAndReplay录制模式.RECORDING)
        .proxyRecordingMode(ServletContainer.class)
);

wireMockServer.start();

// Record requests
wireMockServer.stubFor(get(urlPathEqualTo("/api/"))
    .atPriority(10)
    .willReturn(aResponse()
        .proxiedFrom("https://api.real-service.com")));
```

### Replay Recorded Requests

```java
// Load recorded mappings
wireMockServer.loadRecordingMappings("recorded-mappings");

// Replay
wireMockServer.stubFor(get(urlEqualTo("/api/users/1"))
    .willReturn(aResponse()
        .withStatus(200)
        .withBody("{\"id\":1,\"name\":\"John\"}")));
```

## Verification

### Basic Verification

```java
@Test
void shouldVerifyRequestMade() {
    stubFor(get(urlEqualTo("/api/users/1"))
        .willReturn(ok()));

    // Make request
    httpClient.get("http://localhost:8089/api/users/1");

    // Verify
    verify(getRequestedFor(urlEqualTo("/api/users/1")));
}
```

### Multiple Requests

```java
@Test
void shouldVerifyMultipleRequests() {
    stubFor(get(urlEqualTo("/api/users"))
        .willReturn(ok()));

    // Make 3 requests
    httpClient.get("http://localhost:8089/api/users");
    httpClient.get("http://localhost:8089/api/users");
    httpClient.get("http://localhost:8089/api/users");

    // Verify exactly 3 times
    verify(3, getRequestedFor(urlEqualTo("/api/users")));
}
```

### Verify with Headers

```java
@Test
void shouldVerifyHeaders() {
    stubFor(get(urlEqualTo("/api/secure"))
        .willReturn(ok()));

    httpClient.get("http://localhost:8089/api/secure")
        .header("Authorization", "Bearer token");

    verify(getRequestedFor(urlEqualTo("/api/secure"))
        .withHeader("Authorization", equalTo("Bearer token")));
}
```

## Stateful Mocking

### Scenarios

```java
stubFor(get(urlEqualTo("/api/users/1"))
    .inScenario("User Creation")
    .whenScenarioStateIs(Scenario.STARTED)
    .willReturn(aResponse().withStatus(404))
    .willSetStateTo("User Exists"));

stubFor(get(urlEqualTo("/api/users/1"))
    .inScenario("User Creation")
    .whenScenarioStateIs("User Exists")
    .willReturn(ok().withBody("{\"id\":1,\"name\":\"John\"}")));
```

### Priority

```java
stubFor(get(urlEqualTo("/api/specific"))
    .atPriority(1)
    .willReturn(ok().withBody("Specific")));

stubFor(get(urlPathEqualTo("/api/"))
    .atPriority(2)
    .willReturn(ok().withBody("Default")));
```

## Integration with Spring Boot

### Spring Boot Test

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceIntegrationTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCallExternalService() {
        stubFor(get(urlEqualTo("/external/api/user"))
            .willReturn(ok()
                .withBody("{\"id\":1,\"name\":\"External User\"}")));

        ResponseEntity<User> response = restTemplate.getForEntity(
            "/api/users/external", User.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("External User", response.getBody().getName());
    }
}
```

## Admin API

### Reset Mappings

```java
wireMockServer.resetAll();
```

### List Mappings

```java
List<LoggedRequest> requests = findAll(getRequestedFor(urlEqualTo("/api/users")));
assertEquals(1, requests.size());
```

### Get Serve Events

```java
List<ServeEvent> events = wireMockServer.getAllServeEvents();
assertEquals(1, events.size());
```

## Best Practices

### Clean Up After Tests

```java
@AfterEach
void teardown() {
    wireMockServer.resetAll();
}
```

### Use Scenarios for Complex Flows

```java
// Test a complete workflow
stubFor(post(urlEqualTo("/api/users"))
    .inScenario("User Lifecycle")
    .whenScenarioStateIs(Scenario.STARTED)
    .willReturn(created().withBody("{\"id\":1}"))
    .willSetStateTo("User Created"));

stubFor(get(urlEqualTo("/api/users/1"))
    .inScenario("User Lifecycle")
    .whenScenarioStateIs("User Created")
    .willReturn(ok().withBody("{\"id\":1,\"status\":\"active\"}")));
```

### Mock External Dependencies Only

```java
// Good: Mock external API
stubFor(get(urlEqualTo("https://api.stripe.com/charges"))
    .willReturn(ok().withBody("[{\"id\":\"ch_123\"}]")));

// Bad: Mock internal service
// Don't mock your own services
```

## Resources

- [WireMock Documentation](http://wiremock.org/docs/)
- [WireMock GitHub](https://github.com/tomakehurst/wiremock)
- [WireMock Spring Boot](https://github.com/wiremock/wiremock-spring-boot)

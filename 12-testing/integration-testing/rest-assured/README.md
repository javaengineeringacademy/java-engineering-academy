# REST Assured

## Overview

REST Assured is a Java DSL for simplifying testing of REST services. It provides a simple, BDD-style syntax for sending HTTP requests and validating responses.

## Setup

### Maven

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
dependencies {
    testImplementation 'io.rest-assured:rest-assured:5.3.2'
}
```

## Basic Usage

### GET Request

```java
class ApiTest {

    @Test
    void shouldGetUser() {
        given()
            .baseUri("http://localhost:8080")
        .when()
            .get("/api/users/1")
        .then()
            .statusCode(200)
            .body("name", equalTo("John"))
            .body("email", equalTo("john@example.com"));
    }
}
```

### POST Request

```java
@Test
void shouldCreateUser() {
    given()
        .baseUri("http://localhost:8080")
        .contentType(ContentType.JSON)
        .body("""
            {
                "name": "John",
                "email": "john@example.com"
            }
            """)
    .when()
        .post("/api/users")
    .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("name", equalTo("John"));
}
```

### PUT Request

```java
@Test
void shouldUpdateUser() {
    given()
        .baseUri("http://localhost:8080")
        .contentType(ContentType.JSON)
        .body("""
            {
                "name": "John Updated"
            }
            """)
    .when()
        .put("/api/users/1")
    .then()
        .statusCode(200)
        .body("name", equalTo("John Updated"));
}
```

### DELETE Request

```java
@Test
void shouldDeleteUser() {
    given()
        .baseUri("http://localhost:8080")
    .when()
        .delete("/api/users/1")
    .then()
        .statusCode(204);
}
```

## Request Configuration

### Headers

```java
given()
    .header("Authorization", "Bearer token")
    .header("X-Request-Id", UUID.randomUUID().toString())
    .header("Accept-Language", "en-US")
.when()
    .get("/api/users")
.then()
    .statusCode(200);
```

### Query Parameters

```java
given()
    .queryParam("page", 0)
    .queryParam("size", 10)
    .queryParam("sort", "name")
.when()
    .get("/api/users")
.then()
    .statusCode(200)
    .body("content.size()", equalTo(10));
```

### Path Parameters

```java
given()
    .pathParam("userId", 1)
    .pathParam("orderId", 123)
.when()
    .get("/api/users/{userId}/orders/{orderId}")
.then()
    .statusCode(200);
```

### Cookies

```java
given()
    .cookie("sessionId", "abc123")
    .cookie("csrfToken", "xyz789")
.when()
    .get("/api/users")
.then()
    .statusCode(200);
```

### Multi-part Form Data

```java
given()
    .multiPart("file", new File("test.txt"))
    .multiPart("name", "Test File")
.when()
    .post("/api/upload")
.then()
    .statusCode(200);
```

## Response Assertions

### Status Code

```java
.then()
    .statusCode(200)
    .statusCode(lessThan(400))
    .statusCode(greaterThanOrEqualTo(200));
```

### Body Assertions

```java
.then()
    .body("name", equalTo("John"))
    .body("age", greaterThan(18))
    .body("email", containsString("@"))
    .body("active", is(true))
    .body("roles", hasSize(2))
    .body("roles", hasItem("ADMIN"))
    .body("address.city", equalTo("New York"))
    .body("[0].name", equalTo("John"));
```

### JSON Path Expressions

```java
.then()
    .body("users.find { it.id == 1 }.name", equalTo("John"))
    .body("users.collect { it.name }", hasItem("John"))
    .body("users.findAll { it.active }", hasSize(3));
```

### Header Assertions

```java
.then()
    .header("Content-Type", containsString("application/json"))
    .header("X-Request-Id", notNullValue());
```

### Cookie Assertions

```java
.then()
    .cookie("sessionId", notNullValue());
```

## BDD Style

```java
@Test
void shouldCreateUser() {
    // Given
    RequestSpecification given = given()
        .baseUri("http://localhost:8080")
        .contentType(ContentType.JSON)
        .body("""
            {
                "name": "John",
                "email": "john@example.com"
            }
            """);

    // When
    Response response = given
        .when()
        .post("/api/users");

    // Then
    response.then()
        .statusCode(201)
        .body("name", equalTo("John"));
}
```

## Extracting Values

### Extract Single Value

```java
String name = given()
    .when()
    .get("/api/users/1")
    .then()
    .extract()
    .path("name");

int statusCode = given()
    .when()
    .get("/api/users/1")
    .then()
    .extract()
    .statusCode();
```

### Extract Object

```java
User user = given()
    .when()
    .get("/api/users/1")
    .then()
    .extract()
    .as(User.class);
```

### Extract List

```java
List<String> names = given()
    .when()
    .get("/api/users")
    .then()
    .extract()
    .jsonPath()
    .getList("name", String.class);
```

### Extract Response

```java
Response response = given()
    .when()
    .get("/api/users/1")
    .then()
    .extract()
    .response();

String header = response.getHeader("X-Request-Id");
String body = response.getBody().asString();
```

## Request Logging

### Log Request

```java
given()
    .log().all() // Log everything
    .baseUri("http://localhost:8080")
.when()
    .get("/api/users")
.then()
    .log().all(); // Log response
```

### Log Specific Parts

```java
given()
    .log().uri() // Log URI
    .log().method() // Log method
    .log().headers() // Log headers
    .log().body() // Log body
.when()
    .post("/api/users")
.then()
    .log().ifValidationFails(); // Log only on failure
```

## Filters

### Pretty Print

```java
given()
    .when()
    .get("/api/users")
.then()
    .log().all()
    .prettyPrint();
```

### Pretty Log

```java
given()
    .log().all()
    .filter(new RequestLoggingFilter())
    .filter(new ResponseLoggingFilter())
.when()
    .get("/api/users")
.then()
    .log().all();
```

## Integration with Spring Boot

### @SpringBootTest

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @Test
    void shouldGetUser() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("name", equalTo("John"));
    }
}
```

### With Testcontainers

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ApiWithTestcontainersTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateUser() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "name": "John",
                    "email": "john@example.com"
                }
                """)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .body("id", notNullValue());
    }
}
```

## Advanced Usage

### Authentication

```java
given()
    .auth().basic("username", "password")
.when()
    .get("/api/secure")
.then()
    .statusCode(200);

given()
    .auth().oauth2("access-token")
.when()
    .get("/api/secure")
.then()
    .statusCode(200);
```

### SSL

```java
given()
    .relaxedHTTPSValidation()
.when()
    .get("https://localhost:8443/api/users")
.then()
    .statusCode(200);
```

### Timeouts

```java
given()
    .config(RestAssuredConfig.config()
        .httpClient(HttpClientConfig.httpClientConfig()
            .setParam("http.connection.timeout", 5000)
            .setParam("http.socket.timeout", 5000)))
.when()
    .get("/api/users")
.then()
    .statusCode(200);
```

## Best Practices

### Use Base URI Configuration

```java
@BeforeAll
static void setup() {
    RestAssured.baseURI = "http://localhost:8080";
    RestAssured.basePath = "/api";
    RestAssured.port = 8080;
}
```

### Chain Assertions

```java
given()
    .when()
    .get("/api/users/1")
.then()
    .statusCode(200)
    .body("name", equalTo("John"))
    .body("email", containsString("@"))
    .header("Content-Type", containsString("json"));
```

### Use Extract for Further Assertions

```java
Response response = given()
    .when()
    .get("/api/users/1")
    .then()
    .extract()
    .response();

assertEquals(200, response.statusCode());
assertEquals("John", response.path("name"));
```

## Resources

- [REST Assured GitHub](https://github.com/rest-assured/rest-assured)
- [REST Assured Documentation](https://rest-assured.io/)
- [Baeldung REST Assured](https://www.baeldung.com/rest-assured-tutorial)

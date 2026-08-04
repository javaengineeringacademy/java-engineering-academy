# OpenAPI Code Generation

## Comprehensive Guide to API Code Generation

Code generation from OpenAPI specifications automates the creation of client libraries, server stubs, and documentation. This guide covers OpenAPI Generator and related tools.

---

## Table of Contents

1. [Code Generation Overview](#code-generation-overview)
2. [OpenAPI Generator](#openapi-generator)
3. [Server Generation](#server-generation)
4. [Client Generation](#client-generation)
5. [Custom Templates](#custom-templates)
6. [Integration](#integration)
7. [Best Practices](#best-practices)

---

## Code Generation Overview

### What Can Be Generated?

```
OpenAPI Spec
      |
      v
+-------------------+
| Code Generator    |
+-------------------+
      |
      +---> Server Stubs
      +---> Client Libraries
      +---> Models
      +---> API Interfaces
      +---> Documentation
      +---> Test Cases
```

### Supported Languages

| Language | Server | Client | Quality |
|----------|--------|--------|---------|
| Java | Yes | Yes | High |
| Kotlin | Yes | Yes | High |
| Python | Yes | Yes | High |
| TypeScript | Yes | Yes | High |
| Go | Yes | Yes | High |
| Rust | No | Yes | Medium |
| Swift | No | Yes | Medium |

---

## OpenAPI Generator

### Installation

```bash
# Using Homebrew (macOS)
brew install openapi-generator

# Using npm
npm install @openapitools/openapi-generator-cli -g

# Using Docker
docker pull openapitools/openapi-generator-cli

# Using Maven
# pom.xml
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>7.1.0</version>
</dependency>

# Using Gradle
plugins {
    id 'org.openapi.generator' version '7.1.0'
}
```

### CLI Usage

```bash
# List available generators
openapi-generator-cli list

# Generate server
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g spring \
  -o ./generated/server \
  -p package=com.example.api

# Generate client
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g java \
  -o ./generated/client \
  -p library=retrofit2

# Generate from URL
openapi-generator-cli generate \
  -i https://api.example.com/openapi.json \
  -g typescript-axios \
  -o ./generated/typescript
```

---

## Server Generation

### Spring Boot

```bash
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g spring \
  -o ./generated/spring-server \
  --additional-properties=\
    package=com.example.api,\
    dateLibrary=java8,\
    useSpringBoot3=true,\
    openApiNullable=true,\
    interfaceOnly=true,\
    delegatePattern=true
```

### Generated Code Structure

```
generated/spring-server/
  src/main/java/com/example/api/
    ApiApi.java              # API interface
    model/
      User.java              # Model classes
      CreateUserRequest.java
    delegate/
      ApiApiDelegate.java    # Delegate interface
    config/
      OpenApiConfig.java     # Configuration
  pom.xml
```

### Generated API Interface

```java
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-01-15T10:30:00.000Z")
@RestController
@Api(tags = "Users", description = "User operations")
@RequestMapping("/api/v1")
public interface UsersApi {

    @Operation(summary = "List users", description = "Returns a list of users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<User>> listUsers(
        @Parameter(description = "Maximum items to return")
        @RequestParam(defaultValue = "20") Integer limit,

        @Parameter(description = "Items to skip")
        @RequestParam(defaultValue = "0") Integer offset
    );

    @Operation(summary = "Create user", description = "Creates a new user")
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> createUser(
        @Parameter(description = "User to create") @Valid @RequestBody CreateUserRequest body
    );
}
```

### Implementation

```java
@Component
public class UsersApiDelegateImpl implements UsersApiDelegate {

    private final UserService userService;

    @Override
    public ResponseEntity<List<User>> listUsers(Integer limit, Integer offset) {
        List<User> users = userService.findAll(limit, offset);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<User> createUser(CreateUserRequest body) {
        User user = userService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
```

---

## Client Generation

### Java with Retrofit

```bash
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g java \
  -o ./generated/java-client \
  --additional-properties=\
    library=retrofit2,\
    useRxJava3=true,\
    dateLibrary=java8,\
    hideGenerationTimestamp=true
```

### Generated Client

```java
@Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class UsersApi {
    private ApiClient localVarApiClient;

    public UsersApi() {
        this(Configuration.getDefaultApiClient());
    }

    public UsersApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public Call listUsers(Integer limit, Integer offset) throws ApiException {
        okhttp3.Call localVarCall = listUsersValidateBeforeCall(limit, offset, null);
        Type localVarReturnType = new TypeToken<List<User>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call listUsers(Integer limit, Integer offset,
                          ApiCallback<List<User>> callback) throws ApiException {
        okhttp3.Call localVarCall = listUsersValidateBeforeCall(limit, offset, callback);
        Type localVarReturnType = new TypeToken<List<User>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
        return localVarCall;
    }
}
```

### TypeScript with Axios

```bash
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g typescript-axios \
  -o ./generated/typescript-client \
  --additional-properties=\
    supportsES6=true,\
    withInterfaces=true,\
    npmName=@example/api-client
```

### Generated TypeScript

```typescript
import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';

export interface User {
    id: string;
    name: string;
    email: string;
}

export class UsersApi {
    protected basePath = 'https://api.example.com/v1';

    constructor(protected axios: AxiosInstance) {}

    async listUsers(
        limit?: number,
        offset?: number,
        options?: AxiosRequestConfig
    ): Promise<{ data: User[] }> {
        return this.axios.get(`${this.basePath}/users`, {
            params: { limit, offset },
            ...options
        });
    }

    async createUser(
        body: CreateUserRequest,
        options?: AxiosRequestConfig
    ): Promise<{ data: User }> {
        return this.axios.post(`${this.basePath}/users`, body, options);
    }
}
```

### Python with Requests

```bash
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g python \
  -o ./generated/python-client \
  --additional-properties=\
    packageName=example_api,\
    generateSourceCodeOnly=false
```

---

## Custom Templates

### Custom Java Template

```bash
# Generate with custom template
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g spring \
  -o ./generated/custom \
  -t ./custom-templates

# Template directory structure
custom-templates/
  apiDelegate.mustache
  model.mustache
  pojo.mustache
```

### Custom Mustache Template

```mustache
// apiDelegate.mustache
package {{package}}.delegate;

import {{package}}.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * Delegate interface for {{className}}.
 * Custom implementation should extend this interface.
 */
public interface {{className}}Delegate {
{{#operations}}
    {{#operation}}

    /**
     * {{summary}}
     * {{description}}
     */
    default ResponseEntity<{{returnType}}> {{operationId}}({{#allParams}}{{paramName}}: {{dataType}}{{#hasMore}}, {{/hasMore}}{{/allParams}}) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
    {{/operation}}
{{/operations}}
}
```

---

## Integration

### Maven Integration

```xml
<plugin>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>7.1.0</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>${project.basedir}/src/main/resources/api-spec.yaml</inputSpec>
                <generatorName>spring</generatorName>
                <apiPackage>com.example.api</apiPackage>
                <modelPackage>com.example.model</modelPackage>
                <additionalProperties>
                    <dateLibrary>java8</dateLibrary>
                    <interfaceOnly>true</interfaceOnly>
                    <delegatePattern>true</delegatePattern>
                </additionalProperties>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Gradle Integration

```gradle
plugins {
    id 'org.openapi.generator' version '7.1.0'
}

openApiGenerate {
    generatorName = 'spring'
    inputSpec = "$rootDir/src/main/resources/api-spec.yaml".toString()
    outputDir = "$buildDir/generated/openapi".toString()
    apiPackage = 'com.example.api'
    modelPackage = 'com.example.model'
    additionalProperties = [
        dateLibrary: 'java8',
        interfaceOnly: 'true',
        delegatePattern: 'true'
    ]
}

openApiGenerate.dependsOn.tasks.matching {
    it.name == 'processResources'
}

sourceSets.main.java.srcDir "$buildDir/generated/openapi/src/main/java"
```

### Docker Integration

```dockerfile
FROM openapitools/openapi-generator-cli:latest AS generator

COPY api-spec.yaml /api-spec.yaml

RUN openapi-generator-cli generate \
    -i /api-spec.yaml \
    -g spring \
    -o /generated

FROM maven:3.9-eclipse-temurin-21 AS build

COPY --from=generator /generated /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Best Practices

### 1. Use Generated Code as Foundation

```java
// Generated code
@Generated
public interface UsersApi {
    ResponseEntity<List<User>> listUsers(Integer limit, Integer offset);
}

// Custom implementation
@Component
public class UsersApiImpl implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<List<User>> listUsers(Integer limit, Integer offset) {
        List<User> users = userService.findAll(limit, offset);
        return ResponseEntity.ok(users);
    }
}
```

### 2. Version Your API

```yaml
# api-spec-v1.yaml
openapi: 3.0.3
info:
  title: User API
  version: 1.0.0

# api-spec-v2.yaml
openapi: 3.0.3
info:
  title: User API
  version: 2.0.0
paths:
  /users:
    get:
      responses:
        '200':
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserListV2'
```

### 3. Generate Tests

```bash
# Generate test files
openapi-generator-cli generate \
  -i api-spec.yaml \
  -g spring \
  -o ./generated \
  --additional-properties=\
    useSpringBoot3=true

# Generated test
@SpringBootTest
class UsersApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListUsers() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
```

### 4. CI/CD Integration

```yaml
# .github/workflows/api-generate.yml
name: Generate API Code

on:
  push:
    paths:
      - 'api-spec.yaml'

jobs:
  generate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Generate server
        run: |
          openapi-generator-cli generate \
            -i api-spec.yaml \
            -g spring \
            -o ./generated/server

      - name: Generate client
        run: |
          openapi-generator-cli generate \
            -i api-spec.yaml \
            -g typescript-axios \
            -o ./generated/client

      - name: Commit changes
        run: |
          git config user.name "github-actions"
          git config user.email "github-actions@github.com"
          git add generated/
          git commit -m "chore: regenerate API code" || true
          git push
```

### 5. Validate Specifications

```bash
# Validate spec
openapi-generator-cli validate -i api-spec.yaml

# Lint with Spectral
npm install -g @stoplight/spectral-cli
spectral lint api-spec.yaml
```

---

## Further Reading

- [OpenAPI Generator](https://openapi-generator.tech/)
- [OpenAPI Generator GitHub](https://github.com/OpenAPITools/openapi-generator)
- [Mustache Templates](https://mustache.github.io/)
- [Spectral Linter](https://stoplight.io/open-source/spectral)

# WebFlux

## Comprehensive Guide to Spring WebFlux

Spring WebFlux is the reactive web framework in Spring that provides non-blocking, reactive programming support. This guide covers RouterFunction, HandlerFunction, WebClient, and Server-Sent Events.

---

## Table of Contents

1. [WebFlux Basics](#webflux-basics)
2. [RouterFunction](#routerfunction)
3. [HandlerFunction](#handlerfunction)
4. [WebClient](#webclient)
5. [Server-Sent Events (SSE)](#server-sent-events-sse)
6. [Error Handling](#error-handling)
7. [Best Practices](#best-practices)

---

## WebFlux Basics

### Setup

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

### Reactive Types: Mono and Flux

```java
@RestController
@RequestMapping("/api/reactive")
public class ReactiveController {
    
    // Mono - Single reactive value
    @GetMapping("/mono")
    public Mono<String> getMono() {
        return Mono.just("Hello from Mono");
    }
    
    // Flux - Multiple reactive values
    @GetMapping("/flux")
    public Flux<String> getFlux() {
        return Flux.fromIterable(List.of("One", "Two", "Three"));
    }
    
    // Mono with transformation
    @GetMapping("/mono/transform")
    public Mono<String> getMonoTransformed() {
        return Mono.just("hello")
            .map(String::toUpperCase)
            .map(s -> s + " WORLD");
    }
    
    // Flux with operators
    @GetMapping("/flux/filtered")
    public Flux<Integer> getFilteredFlux() {
        return Flux.range(1, 100)
            .filter(n -> n % 2 == 0)
            .take(10);
    }
}
```

### WebClient Configuration

```java
@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(ExchangeFilterFunctions.basicAuthentication("user", "password"))
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .responseTimeout(Duration.ofSeconds(10))
                    .observe(doOnResponse())))
            .build();
    }
    
    private Consumer<Observation> doOnResponse() {
        return observation -> {
            // Observe response
        };
    }
}

// Custom WebClient with retry
@Bean
public WebClient retryWebClient() {
    Retry retry = Retry.backoff(3, Duration.ofSeconds(1))
        .filter(throwable -> throwable instanceof WebClientResponseException)
        .doAfterRetry(signal -> log.warn("Retry attempt: {}", signal));
    
    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(
            HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5))))
        .filter((request, next) -> 
            next.exchange(request)
                .retryWhen(retry))
        .build();
}
```

### Application Configuration

```yaml
# application.yml
server:
  port: 8080
  netty:
    connection-timeout: 10s

spring:
  webflux:
    base-path: /api
  
  codecs:
    max-in-memory-size: 10MB

logging:
  level:
    org.springframework.web.reactive: DEBUG
```

---

## RouterFunction

### Functional Endpoints

```java
@Configuration
public class RouterConfig {
    
    @Bean
    public RouterFunction<ServerResponse> routes(UserHandler userHandler) {
        return RouterFunctions.route()
            .path("/api/users", builder -> builder
                .GET("", userHandler::getAllUsers)
                .GET("/{id}", userHandler::getUserById)
                .POST("", userHandler::createUser)
                .PUT("/{id}", userHandler::updateUser)
                .DELETE("/{id}", userHandler::deleteUser)
                .GET("/search", userHandler::searchUsers))
            .build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> orderRoutes(OrderHandler orderHandler) {
        return RouterFunctions.route()
            .path("/api/orders", builder -> builder
                .GET("", orderHandler::getAllOrders)
                .GET("/{id}", orderHandler::getOrderById)
                .POST("", orderHandler::createOrder)
                .PUT("/{id}/status", orderHandler::updateOrderStatus))
            .build();
    }
}
```

### Route with Filters

```java
@Bean
public RouterFunction<ServerResponse> filteredRoutes(UserHandler userHandler) {
    return RouterFunctions.route()
        .path("/api/users", builder -> builder
            .GET("", userHandler::getAllUsers)
            .GET("/{id}", userHandler::getUserById))
        .filter((request, next) -> {
            // Pre-processing
            long startTime = System.currentTimeMillis();
            return next.handle(request)
                .doOnSuccess(response -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("Request: {} {} - Duration: {}ms",
                        request.method(), request.path(), duration);
                });
        })
        .filter((request, next) -> {
            // Authentication check
            String authHeader = request.headers().firstHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .bodyValue("Unauthorized");
            }
            return next.handle(request);
        })
        .build();
}
```

### Route with Path Variables

```java
@Bean
public RouterFunction<ServerResponse> pathVariableRoutes(ProductHandler handler) {
    return RouterFunctions.route()
        .path("/api/products/{category}", builder -> builder
            .GET("", handler::getProductsByCategory)
            .GET("/{id}", handler::getProductById))
        .build();
}

// Handler using path variables
@Component
public class ProductHandler {
    
    public Mono<ServerResponse> getProductsByCategory(ServerRequest request) {
        String category = request.pathVariable("category");
        return productRepository.findByCategory(category)
            .collectList()
            .flatMap(products -> ServerResponse.ok()
                .bodyValue(products));
    }
    
    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String category = request.pathVariable("category");
        String id = request.pathVariable("id");
        return productRepository.findByCategoryAndId(category, id)
            .flatMap(product -> ServerResponse.ok()
                .bodyValue(product))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
```

### Route with Query Parameters

```java
@Bean
public RouterFunction<ServerResponse> queryParamRoutes(SearchHandler handler) {
    return RouterFunctions.route()
        .path("/api/search", builder -> builder
            .GET("", handler::search))
        .build();
}

@Component
public class SearchHandler {
    
    public Mono<ServerResponse> search(ServerRequest request) {
        String query = request.queryParam("q").orElse("");
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        
        return searchService.search(query, PageRequest.of(page, size))
            .collectList()
            .flatMap(results -> ServerResponse.ok()
                .bodyValue(results));
    }
}
```

---

## HandlerFunction

### Basic Handler

```java
@Component
public class UserHandler {
    
    private final UserRepository userRepository;
    
    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return userRepository.findAll()
            .collectList()
            .flatMap(users -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(users));
    }
    
    public Mono<ServerResponse> getUserById(ServerRequest request) {
        String id = request.pathVariable("id");
        return userRepository.findById(id)
            .flatMap(user -> ServerResponse.ok()
                .bodyValue(user))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
            .flatMap(user -> userRepository.save(user))
            .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                .bodyValue(savedUser));
    }
    
    public Mono<ServerResponse> updateUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(User.class)
            .flatMap(user -> userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    return userRepository.save(existingUser);
                }))
            .flatMap(updatedUser -> ServerResponse.ok()
                .bodyValue(updatedUser))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return userRepository.deleteById(id)
            .then(ServerResponse.noContent().build());
    }
}
```

### Handler with Validation

```java
@Component
public class ValidatedUserHandler {
    
    private final UserRepository userRepository;
    private final Validator validator;
    
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
            .flatMap(user -> {
                Set<ConstraintViolation<User>> violations = validator.validate(user);
                if (!violations.isEmpty()) {
                    return Mono.error(new ValidationException(violations.toString()));
                }
                return userRepository.save(user);
            })
            .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                .bodyValue(savedUser));
    }
}
```

### Handler with Pagination

```java
@Component
public class PaginatedUserHandler {
    
    private final UserRepository userRepository;
    
    public Mono<ServerResponse> getUsersPaginated(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        
        return userRepository.findAllBy(PageRequest.of(page, size))
            .collectList()
            .zipWith(userRepository.count())
            .flatMap(tuple -> {
                List<User> users = tuple.getT1();
                long total = tuple.getT2();
                
                Map<String, Object> response = Map.of(
                    "content", users,
                    "page", page,
                    "size", size,
                    "totalElements", total,
                    "totalPages", (int) Math.ceil((double) total / size));
                
                return ServerResponse.ok()
                    .bodyValue(response);
            });
    }
}
```

---

## WebClient

### GET Requests

```java
@Service
public class UserApiClient {
    
    private final WebClient webClient;
    
    public UserApiClient(WebClient webClient) {
        this.webClient = webClient;
    }
    
    public Mono<User> getUserById(String id) {
        return webClient.get()
            .uri("/users/{id}", id)
            .retrieve()
            .bodyToMono(User.class);
    }
    
    public Flux<User> getAllUsers() {
        return webClient.get()
            .uri("/users")
            .retrieve()
            .bodyToFlux(User.class);
    }
    
    public Mono<List<User>> getUsersAsList() {
        return webClient.get()
            .uri("/users")
            .retrieve()
            .bodyToFlux(User.class)
            .collectList();
    }
}
```

### POST Requests

```java
public Mono<User> createUser(User user) {
    return webClient.post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(user)
        .retrieve()
        .bodyToMono(User.class);
}

public Mono<User> createUserWithHeaders(User user) {
    return webClient.post()
        .uri("/users")
        .headers(headers -> {
            headers.setBearerAuth("token");
            headers.set("X-Request-Id", UUID.randomUUID().toString());
        })
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(user)
        .retrieve()
        .bodyToMono(User.class);
}
```

### PUT Requests

```java
public Mono<User> updateUser(String id, User user) {
    return webClient.put()
        .uri("/users/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(user)
        .retrieve()
        .bodyToMono(User.class);
}
```

### DELETE Requests

```java
public Mono<Void> deleteUser(String id) {
    return webClient.delete()
        .uri("/users/{id}", id)
        .retrieve()
        .bodyToMono(Void.class);
}
```

### Error Handling with WebClient

```java
public Mono<User> getUserWithErrorHandling(String id) {
    return webClient.get()
        .uri("/users/{id}", id)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> {
            if (response.statusCode() == HttpStatus.NOT_FOUND) {
                return Mono.error(new ResourceNotFoundException("User not found"));
            }
            return response.bodyToMono(ErrorResponse.class)
                .flatMap(error -> Mono.error(new ApiException(error.getMessage())));
        })
        .onStatus(HttpStatusCode::is5xxServerError, response -> {
            return Mono.error(new ServerException("Server error"));
        })
        .bodyToMono(User.class);
}
```

### WebClient with Retry

```java
@Bean
public WebClient webClientWithRetry() {
    Retry retry = Retry.backoff(3, Duration.ofSeconds(1))
        .filter(throwable -> throwable instanceof WebClientResponseException)
        .doAfterRetry(signal -> log.warn("Retry attempt: {}", signal));
    
    return WebClient.builder()
        .baseUrl("http://external-api")
        .filter((request, next) -> 
            next.exchange(request)
                .retryWhen(retry))
        .build();
}

public Mono<String> callWithRetry() {
    return webClientWithRetry.get()
        .uri("/data")
        .retrieve()
        .bodyToMono(String.class);
}
```

### WebClient with Timeouts

```java
public Mono<User> getUserWithTimeout(String id) {
    return webClient.get()
        .uri("/users/{id}", id)
        .retrieve()
        .bodyToMono(User.class)
        .timeout(Duration.ofSeconds(5))
        .onErrorResume(TimeoutException.class, e -> {
            log.error("Timeout calling user service for user: {}", id);
            return Mono.error(new ServiceUnavailableException("Service timeout"));
        });
}
```

---

## Server-Sent Events (SSE)

### SSE Producer

```java
@RestController
@RequestMapping("/api/events")
public class SseController {
    
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(seq -> ServerSentEvent.<String>builder()
                .id(String.valueOf(seq))
                .event("message")
                .data("Event " + seq)
                .build());
    }
    
    @GetMapping(value = "/user-events/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<UserEvent>> streamUserEvents(@PathVariable String userId) {
        return userService.getUserEvents(userId)
            .map(event -> ServerSentEvent.<UserEvent>builder()
                .id(event.getId())
                .event(event.getType())
                .data(event)
                .build());
    }
    
    @GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Notification>> streamNotifications(
            @RequestParam String userId) {
        return notificationService.getNotifications(userId)
            .map(notification -> ServerSentEvent.<Notification>builder()
                .id(notification.getId())
                .event("notification")
                .data(notification)
                .build());
    }
}
```

### SSE Consumer

```java
@Component
public class SseClient {
    
    private final WebClient webClient;
    
    public SseClient(WebClient webClient) {
        this.webClient = webClient;
    }
    
    public Flux<ServerSentEvent<String>> consumeSse() {
        return webClient.get()
            .uri("/api/events/stream")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .retrieve()
            .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {});
    }
    
    public void consumeEvents() {
        consumeSse()
            .subscribe(
                event -> {
                    System.out.println("Event ID: " + event.id());
                    System.out.println("Event Type: " + event.event());
                    System.out.println("Event Data: " + event.data());
                },
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Stream completed")
            );
    }
    
    public Mono<Void> consumeEventsWithRetry() {
        return consumeSse()
            .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(1)))
            .doOnNext(event -> processEvent(event))
            .then();
    }
    
    private void processEvent(ServerSentEvent<String> event) {
        System.out.println("Processing event: " + event.data());
    }
}
```

### SSE with Spring WebFlux

```java
@RestController
@RequestMapping("/api/sse")
public class SseWebFluxController {
    
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Map<String, Object>>> stream() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(seq -> {
                Map<String, Object> data = Map.of(
                    "timestamp", Instant.now(),
                    "sequence", seq,
                    "message", "Message " + seq);
                
                return ServerSentEvent.<Map<String, Object>>builder()
                    .id(String.valueOf(seq))
                    .event("data")
                    .data(data)
                    .build();
            });
    }
    
    @GetMapping(value = "/user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<UserActivity>> streamUserActivity(@PathVariable String userId) {
        return activityService.getUserActivity(userId)
            .map(activity -> ServerSentEvent.<UserActivity>builder()
                .id(activity.getId())
                .event("activity")
                .data(activity)
                .build());
    }
}
```

---

## Error Handling

### Global Error Handler

```java
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ServerResponse> handleNotFound(ResourceNotFoundException ex) {
        return ServerResponse.status(HttpStatus.NOT_FOUND)
            .bodyValue(Map.of(
                "error", "Not Found",
                "message", ex.getMessage(),
                "timestamp", Instant.now()));
    }
    
    @ExceptionHandler(ValidationException.class)
    public Mono<ServerResponse> handleValidation(ValidationException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
            .bodyValue(Map.of(
                "error", "Validation Error",
                "message", ex.getMessage(),
                "timestamp", Instant.now()));
    }
    
    @ExceptionHandler(Exception.class)
    public Mono<ServerResponse> handleGeneral(Exception ex) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .bodyValue(Map.of(
                "error", "Internal Server Error",
                "message", "An unexpected error occurred",
                "timestamp", Instant.now()));
    }
}
```

### Functional Error Handler

```java
@Bean
public RouterFunction<ServerResponse> errorRoutes() {
    return RouterFunctions.route()
        .onError(ResourceNotFoundException.class, (ex, request) -> {
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                .bodyValue(Map.of("error", ex.getMessage()));
        })
        .onError(ValidationException.class, (ex, request) -> {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .bodyValue(Map.of("error", ex.getMessage()));
        })
        .build();
}
```

---

## Best Practices

### 1. Use Appropriate Reactive Types

```java
// Good - Use Mono for single values
public Mono<User> getUser(String id) {
    return userRepository.findById(id);
}

// Good - Use Flux for multiple values
public Flux<User> getAllUsers() {
    return userRepository.findAll();
}

// Avoid - Converting Flux to List unnecessarily
public Mono<List<User>> getAllUsersAsList() {
    return userRepository.findAll().collectList();
}
```

### 2. Handle Backpressure

```java
Flux.range(1, 1000)
    .onBackpressureBuffer(100)
    .flatMap(this::processItem, 10) // Concurrency limit
    .subscribe();
```

### 3. Use Proper Timeouts

```java
webClient.get()
    .uri("/users/{id}", id)
    .retrieve()
    .bodyToMono(User.class)
    .timeout(Duration.ofSeconds(5))
    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
    .onErrorResume(TimeoutException.class, e -> Mono.empty());
```

### 4. Prefer Functional Endpoints for Simple Cases

```java
// Good - Functional endpoints for simple routing
@Bean
public RouterFunction<ServerResponse> routes() {
    return RouterFunctions.route()
        .GET("/api/users", this::getUsers)
        .build();
}
```

### 5. Use WebClient for External Calls

```java
// Good - WebClient for external service calls
@Service
public class ExternalServiceClient {
    
    private final WebClient webClient;
    
    public Mono<ExternalData> getData(String id) {
        return webClient.get()
            .uri("/external/data/{id}", id)
            .retrieve()
            .bodyToMono(ExternalData.class);
    }
}
```

---

## Common Pitfalls

### 1. Blocking in Reactive Code

```java
// Bad - Blocking call
public Mono<User> getUser(String id) {
    User user = userRepository.findById(id).block(); // Don't do this!
    return Mono.just(user);
}

// Good - Non-blocking
public Mono<User> getUser(String id) {
    return userRepository.findById(id);
}
```

### 2. Not Handling Errors

```java
// Bad - No error handling
public Mono<User> getUser(String id) {
    return webClient.get()
        .uri("/users/{id}", id)
        .retrieve()
        .bodyToMono(User.class);
}

// Good - With error handling
public Mono<User> getUser(String id) {
    return webClient.get()
        .uri("/users/{id}", id)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> 
            Mono.error(new ResourceNotFoundException("User not found")))
        .bodyToMono(User.class);
}
```

### 3. Ignoring Backpressure

```java
// Bad - No backpressure handling
Flux.range(1, 1000000)
    .flatMap(this::processItem)
    .subscribe();

// Good - With backpressure
Flux.range(1, 1000000)
    .onBackpressureBuffer(1000)
    .flatMap(this::processItem, 10)
    .subscribe();
```

---

## Further Reading

- [Spring WebFlux Official Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Reactor Core Reference](https://projectreactor.io/docs)
- [Baeldung Spring WebFlux](https://www.baeldung.com/spring-webflux)
- [Reactive Programming in Spring](https://spring.io/guides/gs/reactive-rest-service)

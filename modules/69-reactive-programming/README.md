# Module 69: Reactive Programming

## Overview
Reactive programming is a paradigm for asynchronous, non-blocking data streams. It enables handling concurrent operations efficiently with backpressure support.

## Learning Objectives
- Understand reactive streams
- Use Project Reactor
- Implement backpressure
- Handle errors reactively
- Apply reactive patterns

## Prerequisites
- Java streams
- Functional programming
- Concurrency basics

## Why This Concept Exists
Traditional blocking code:
- Wastes threads waiting
- Poor resource utilization
- Limited scalability

Reactive programming provides:
- Non-blocking I/O
- Efficient resource use
- High scalability
- Backpressure support

## Problem Statement
How do you handle high-concurrency with efficient resource usage?

## Theory

### Reactive Types

| Type | Library | Description |
|------|---------|-------------|
| Mono | Reactor | 0 or 1 element |
| Flux | Reactor | 0 to N elements |
| Flowable | RxJava | Reactive streams |
| CompletionStage | JDK | Async completion |

### Operators

| Category | Operators |
|----------|-----------|
| Creation | just, fromIterable, generate |
| Transformation | map, flatMap, transform |
| Filtering | filter, distinct, take |
| Combining | merge, zip, combine |
| Error | onErrorReturn, onErrorResume |

## Enterprise Example

```java
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;

@Service
public class ReactiveService {
    private final WebClient webClient;
    private final UserRepository userRepository;
    
    public ReactiveService(WebClient.Builder builder, UserRepository userRepository) {
        this.webClient = builder.baseUrl("https://api.external.com").build();
        this.userRepository = userRepository;
    }
    
    // Reactive repository
    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Flux<User> findAll() {
        return userRepository.findAll();
    }
    
    // Reactive web client
    public Mono<String> callExternalApi(String endpoint) {
        return webClient.get()
            .uri(endpoint)
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(5))
            .retry(3)
            .onErrorResume(e -> Mono.just("fallback"));
    }
    
    // Reactive stream processing
    public Flux<ProcessedData> processStream(Flux<RawData> rawData) {
        return rawData
            .filter(data -> data.isValid())
            .map(this::transform)
            .flatMap(this::enrich)
            .buffer(100)
            .flatMap(this::batchSave)
            .onErrorResume(e -> {
                log.error("Processing error", e);
                return Flux.empty();
            });
    }
    
    // Backpressure handling
    public Flux<Event> handleBackpressure() {
        return Flux.interval(Duration.ofMillis(100))
            .onBackpressureBuffer(1000)
            .onBackpressureDrop(event -> 
                log.warn("Dropped event: {}", event));
    }
    
    // Parallel processing
    public Mono<AggregatedResult> parallelProcess(List<Request> requests) {
        return Flux.fromIterable(requests)
            .parallel()
            .runOn(Schedulers.parallel())
            .flatMap(this::processSingle)
            .sequential()
            .collectList()
            .map(this::aggregate);
    }
}

// Reactive controller
@RestController
@RequestMapping("/api/reactive")
public class ReactiveController {
    private final ReactiveService service;
    
    @GetMapping("/users/{id}")
    public Mono<UserDTO> getUser(@PathVariable Long id) {
        return service.findById(id)
            .map(UserMapper::toDTO)
            .switchIfEmpty(Mono.error(new NotFoundException(id)));
    }
    
    @GetMapping("/stream")
    public Flux<Event> streamEvents() {
        return service.getEventStream();
    }
}
```

## Performance Considerations
- Use non-blocking I/O
- Implement backpressure
- Avoid blocking in reactive chains
- Use appropriate schedulers

## Best Practices
1. Stay reactive end-to-end
2. Handle errors gracefully
3. Implement backpressure
4. Use operators correctly
5. Test reactive code

## Interview Questions

### Q1: What is reactive programming?
**Answer:** Programming paradigm for asynchronous, non-blocking data streams.

### Q2: What is backpressure?
**Answer:** Mechanism to handle slow consumers in reactive streams.

### Q3: What is the difference between Mono and Flux?
**Answer:** Mono emits 0-1 elements, Flux emits 0-N elements.

### Q4: What is Project Reactor?
**Answer:** Reactive library for Java used by Spring WebFlux.

### Q5: What is the difference between imperative and reactive?
**Answer:** Imperative is blocking, reactive is non-blocking.

## Summary
Reactive programming enables efficient handling of concurrent operations with non-blocking I/O.

## References
- Project Reactor Documentation
- Reactive Streams Specification
- Spring WebFlux Guide

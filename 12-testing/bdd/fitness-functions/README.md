# Fitness Functions

## Overview
Fitness functions are measurable criteria evaluating how well a system achieves architectural goals.

## Structural Fitness Functions
```java
@Test
void noCyclesBetweenModules() {
    Architecture arch = ArchitectureAnalyzer.analyze("com.example");
    assertTrue(arch.findCycles().isEmpty());
}

@Test
void layerDependenciesRespected() {
    Architecture arch = ArchitectureAnalyzer.analyze("com.example");
    assertFalse(arch.hasDependency("ui", "repository"));
}
```

## Behavioral Fitness Functions
```java
@Test
void responseTimeUnderThreshold() {
    PerformanceMetrics metrics = performanceTestRunner.run(
        PerformanceTest.builder().endpoint("/api/products")
            .expectedP99(Duration.ofMillis(200)).build());
    assertTrue(metrics.getP99Latency().compareTo(Duration.ofMillis(200)) <= 0);
}
```

## Registry Pattern
```java
@Component
public class FitnessFunctionRegistry {
    private final Map<String, FitnessFunction> functions = new ConcurrentHashMap<>();
    public void register(String name, FitnessFunction fn) { functions.put(name, fn); }
    public List<FitnessResult> evaluateAll() {
        return functions.values().stream().map(FitnessFunction::evaluate).collect(Collectors.toList());
    }
}
```

## Common Fitness Functions

| Category | Function | Threshold |
|----------|----------|-----------|
| Performance | P99 latency | < 200ms |
| Reliability | Error rate | < 0.1% |
| Security | CVE count | 0 critical |
| Code Quality | Test coverage | > 80% |

# Architecture Fitness Functions

## Overview

Architecture Fitness Functions are automated checks that evaluate whether an architecture meets its intended goals. They provide objective, measurable criteria for architectural health.

## Definition

A fitness function is any automated mechanism that evaluates an architectural characteristic. Think of them as "tests for your architecture."

## Types of Fitness Functions

### Atomic vs Holistic

| Type | Description | Example |
|------|-------------|---------|
| **Atomic** | Tests single architectural attribute | Module coupling score |
| **Holistic** | Tests multiple attributes together | Overall system health |

### Triggered vs Automated

| Type | Description | Example |
|------|-------------|---------|
| **Triggered** | Run on-demand (CI/CD) | Build-time checks |
| **Automated** | Run continuously | Runtime monitoring |

### Static vs Dynamic

| Type | Description | Example |
|------|-------------|---------|
| **Static** | Analyze code/structure | Dependency analysis |
| **Dynamic** | Analyze runtime behavior | Performance tests |

## Implementation with ArchUnit

```java
@AnalyzeClasses(packages = "com.example")
public class ArchitectureTest {

    // Layer dependency rule
    @ArchTest
    static final ArchRule layer_dependencies = layered()
        .consideringOnlyDependenciesInAnyPackage("com.example..")
        .layer("Controllers").definedBy("..controller..")
        .layer("Services").definedBy("..service..")
        .layer("Repositories").definedBy("..repository..")
        .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
        .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
        .whereLayer("Repositories").mayOnlyBeAccessedByLayers("Services");

    // No cycles between packages
    @ArchTest
    static final ArchRule no_cycles = slices()
        .matching("com.example.(*)..")
        .should().beFreeOfCycles();

    // Dependency rules
    @ArchTest
    static final ArchRule no_cycles_in_domain = slices()
        .matching("com.example.domain.(*)..")
        .should().beFreeOfCycles();

    // Naming conventions
    @ArchTest
    static final ArchRule classes_suffix = classes()
        .that().resideInAPackage("..service..")
        .should().haveSimpleNameEndingWith("Service");

    // No field injection
    @ArchTest
    static final ArchRule no_field_injection = noFields()
        .should().beAnnotatedWith(Autowired.class);
}
```

## Fitness Function Examples

### Code Metrics

```java
// Coupling between objects
@ArchTest
static final ArchRule low_coupling = slices()
    .matching("com.example.(*)..")
    .should().haveSizeBetween(0, 10); // max dependencies

// Cyclomatic complexity
@ArchTest
static final ArchRule low_complexity = methods()
    .should().haveCyclomaticComplexity(10);
```

### Dependency Rules

```java
// Domain has no infrastructure dependencies
@ArchTest
static final ArchRule domain_isolation = noClasses()
    .that().resideInAPackage("..domain..")
    .should().dependOnClassesThat()
    .resideInAPackage("..infrastructure..");

// Adapters depend on ports (hexagonal)
@ArchTest
static final ArchRule hexagonal_architecture = layered()
    .consideringAllDependencies()
    .layer("Domain").definedBy("..domain..")
    .layer("Ports").definedBy("..ports..")
    .layer("Adapters").definedBy("..adapters..")
    .whereLayer("Domain").mayNotBeAccessedByAnyLayer()
    .whereLayer("Adapters").mayOnlyBeAccessedByLayers("Ports");
```

### Performance Fitness

```java
// Response time fitness function
@Test
void apiResponseTime() {
    long start = System.currentTimeMillis();
    mockMvc.perform(get("/api/users"));
    long duration = System.currentTimeMillis() - start;
    assertThat(duration).isLessThan(200); // 200ms threshold
}
```

### Test Coverage

```java
// Minimum test coverage
@Test
void testCoverage() {
    CoverageResult result = runCoverageAnalysis();
    assertThat(result.getLineCoverage()).isGreaterThan(80.0);
    assertThat(result.getBranchCoverage()).isGreaterThan(75.0);
}
```

## Integration Points

| Integration | Description |
|-------------|-------------|
| **CI/CD Pipeline** | Run fitness checks on every build |
| **Code Review** | Automated architecture review |
| **Monitoring** | Runtime fitness monitoring |
| **Documentation** | Auto-generated architecture docs |

## CI/CD Integration

```yaml
# GitHub Actions example
- name: Architecture Fitness Check
  run: |
    mvn test -Parchitecture-tests
    mvn dependency:analyze
    mvn verify -Pmutation-testing
```

## Best Practices

1. **Start simple** - Begin with basic dependency rules
2. **Automate everything** - Manual checks get skipped
3. **Set thresholds** - Define acceptable ranges
4. **Monitor trends** - Track fitness over time
5. **Fail builds** - Break builds when fitness degrades
6. **Document rationale** - Explain why each fitness function exists

## Key Takeaways

- Fitness functions automate architecture validation
- Use ArchUnit for Java dependency rules
- Integrate into CI/CD pipeline
- Track metrics over time
- Start with simple rules, add complexity gradually

# ADR: Java 8 to Java 21 Migration

## Status

Accepted — Q1 2024

## Context

Our production systems have been running on Java 8 since 2016. The platform is now over 8 years old and presents several critical risks:

- **Security vulnerabilities**: Oracle ended public updates for Java 8 in 2019. Extended support requires paid subscriptions. CVEs are disclosed regularly without free patches.
- **Missing modern features**: Records, sealed classes, pattern matching, virtual threads, and the HTTP client are unavailable. This slows development and limits architectural options.
- **Talent acquisition**: New hires increasingly expect modern Java. Recruiting against teams stuck on Java 8 is difficult.
- **Library compatibility**: Many popular libraries are dropping Java 8 support. Spring Boot 3.x requires Java 17 minimum.
- **Performance**: JVM improvements in Java 11–21 (ZGC, G1 improvements, startup time) are inaccessible.

The migration affects 47 microservices, 12 libraries, and approximately 2.3 million lines of Java code.

## Decision

Migrate all services to Java 21 LTS over an 18-month phased timeline.

## Alternatives Considered

### Stay on Java 8
- Pros: Zero migration effort, no risk of regressions
- Cons: Increasing security exposure, library compatibility loss, developer productivity loss, eventual forced migration under worse conditions
- **Rejected**: The cost of inaction exceeds the cost of migration.

### Migrate to Java 17 (Intermediate LTS)
- Pros: Lower risk incremental step, access to most features, wider library support
- Cons: Would require another migration to Java 21 within 2 years, doubles total migration cost
- **Rejected**: Java 21 is the clear long-term target; stopping at 17 defers cost without eliminating it.

### Rewrite in Another Language (Go, Rust, Kotlin)
- Pros: Fresh start, potentially better performance for specific workloads
- Cons: Rewrite risk is extreme, team lacks expertise, 3–5x cost increase, 2–3 year timeline
- **Rejected**: Rewrite economics don't justify the outcome for our use cases.

## Evaluation Criteria

| Criterion | Weight | Java 21 | Java 17 | Stay Java 8 |
|-----------|--------|---------|---------|-------------|
| Security posture | 25% | Excellent | Good | Poor |
| Feature completeness | 20% | Excellent | Good | Poor |
| Migration cost | 20% | Medium | Low | None |
| Long-term viability | 20% | Excellent | Good | Poor |
| Team readiness | 15% | Moderate | High | N/A |

## Consequences

### Positive
- Access to virtual threads eliminates thread pool tuning for I/O-bound services
- Sealed classes and pattern matching improve domain modeling
- ZGC provides sub-millisecond pause times for latency-sensitive services
- HTTP client replaces deprecated Apache HttpClient dependencies
- Strong encapsulation reduces attack surface
- Improved startup time reduces container scaling speed

### Negative
- **Code changes**: ~15% of codebase requires modification (deprecated APIs, removed APIs)
- **Testing effort**: Full regression testing required for each service migration
- **Training**: Team needs training on new language features and JVM behavior changes
- **Build system updates**: Maven/Gradle plugins, CI/CD pipelines need updates
- **Third-party dependencies**: Some internal libraries need refactoring
- **Timeline pressure**: 18-month window is aggressive for 47 services

### Risks
- Subtle behavioral changes in Collections, reflection, and security managers
- Some internal libraries use sun.* APIs that are removed
- Performance regression possible if GC tuning is not adapted

## Implementation Plan

### Phase 1: Foundation (Months 1–3)
- Upgrade build toolchain to support Java 21 compilation
- Update CI/CD pipelines
- Migrate shared libraries (12 libraries)
- Establish compatibility test suite
- Train engineering team (workshops + documentation)

### Phase 2: Pilot Services (Months 4–6)
- Migrate 5 low-risk, low-complexity services
- Validate deployment processes
- Benchmark performance against Java 8 baselines
- Document migration playbooks

### Phase 3: Core Services (Months 7–12)
- Migrate 25 medium-complexity services
- Address discovered compatibility issues
- Update monitoring and alerting for new JVM metrics

### Phase 4: Critical Services (Months 13–18)
- Migrate remaining 17 high-complexity services
- Decommission Java 8 infrastructure
- Final performance tuning and validation

## Timeline

| Phase | Duration | Services | Milestone |
|-------|----------|----------|-----------|
| Foundation | Months 1–3 | Libraries + CI/CD | Toolchain ready |
| Pilot | Months 4–6 | 5 services | First Java 21 in production |
| Core | Months 7–12 | 25 services | 64% migrated |
| Critical | Months 13–18 | 17 services | 100% migrated |

## Cost

- **Migration cost**: $500,000 (engineering time, training, tooling)
- **Annual savings**: $200,000 (reduced security risk, improved developer productivity, lower infrastructure costs from performance improvements)
- **Payback period**: 2.5 years
- **3-year ROI**: 20%

## Interview Questions

1. **Why migrate from Java 8 to Java 21 directly instead of stopping at Java 17?**
   Java 21 is the latest LTS with virtual threads (Project Loom), pattern matching for switch, record patterns, and sequenced collections. Java 17 LTS reaches end of premier support in 2027, while Java 21 extends to 2031. Migrating directly saves $150K-$300K by avoiding a second migration in 2-3 years.

2. **What are the most common breaking changes between Java 8 and Java 21?**
   Removal of Security Manager (deprecated in 17, removed in 18), `finalize()` deprecated, `sun.*` internal APIs removed, module system encapsulation, removal of Java EE modules (JAXB, JAX-WS), and changes to reflection access. Approximately 15% of typical codebases require modification.

3. **How do you estimate migration cost for a large codebase?**
   Formula: `(LOC × 0.0015 × hourly_rate) + (test_hours × hourly_rate) + (infrastructure_cost)`. For 2.3M LOC at $100/hr: code changes ($345K), testing ($150K), infrastructure ($50K) = ~$545K. Actual cost varies by code quality, test coverage, and dependency complexity.

4. **What tools help automate Java version migration?**
   OpenRewrite provides automated refactoring recipes for Java version upgrades. JDepend analyzes package dependencies. jdeps identifies module system violations. SpotBugs/ErrorProne detect deprecated API usage. Eclipse JDT Compiler can compile against multiple versions simultaneously.

5. **What is the typical timeline for migrating 47 microservices from Java 8 to Java 21?**
   Phase 1 (Months 1-3): Toolchain and library migration. Phase 2 (Months 4-6): 5 pilot services. Phase 3 (Months 7-12): 25 core services. Phase 4 (Months 13-18): 17 critical services. Total: 18 months with 3-5 engineers dedicated full-time.

## Pitfalls

**Ignoring `sun.*` API usage:**
```java
// BAD: Using internal APIs that are removed in Java 17+
import sun.misc.Unsafe;
Unsafe unsafe = Unsafe.getUnsafe(); // Fails at runtime

// GOOD: Use standard Java APIs
// Java 9+: Use VarHandle instead of Unsafe for compare-and-swap
import java.lang.invoke.VarHandle;
VarHandle vh = MethodHandles.lookup()
    .findVarHandle(MyClass.class, "field", int.class);
vh.compareAndSet(this, expected, newValue);
```

**Not updating build toolchain first:**
```bash
# BAD: Updating Java version without updating Maven/Gradle
mvn compile -Djava.version=21  # Fails: old compiler plugin

# GOOD: Update toolchain first
# pom.xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
        </plugin>
    </plugins>
</build>
```

**Not testing behavioral changes:**
```java
// BAD: Assuming code works the same on Java 21
// Collections.unmodifiableList() behavior changed
List<String> list = List.of("a", "b", "c");
list.add("d"); // Throws UnsupportedOperationException (same as Java 8)

// BAD: HashMap iteration order may differ
Map<String, Integer> map = new HashMap<>();
map.put("a", 1); map.put("b", 2); map.put("c", 3);
// Don't rely on iteration order — it's implementation-dependent

// GOOD: Test all edge cases with new JVM version
// Use testcontainers or CI matrices to test against multiple JDK versions
```

## Performance

**Java 8 vs Java 21 Performance Benchmarks:**

| Benchmark | Java 8 | Java 21 | Improvement |
|-----------|--------|---------|-------------|
| Startup time | 3.2s | 1.8s | 44% faster |
| Throughput (ops/sec) | 100K | 125K | 25% faster |
| P99 latency | 50ms | 15ms | 70% lower |
| Memory (idle) | 256MB | 180MB | 30% less |
| Full GC pause | 800ms | 200ms (ZGC) | 75% faster |
| Docker image | 450MB | 320MB | 29% smaller |

**Virtual Threads Impact:**
```java
// Java 8: Platform threads — 200 threads max for I/O-bound work
ExecutorService pool = Executors.newFixedThreadPool(200);
// 200 concurrent HTTP calls max

// Java 21: Virtual threads — 100K+ concurrent calls
ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
// 100,000+ concurrent HTTP calls with same memory

// Throughput improvement: 10-50x for I/O-bound workloads
// Memory improvement: 50-100x (virtual threads are ~1KB vs ~1MB)
```

## Internal Working

**JVM Migration Process:**
1. **Source compilation**: Java source compiled against Java 21 class files
2. **Module system**: Strong encapsulation prevents access to internal APIs
3. **Class file verification**: New bytecode format verified by JVM
4. **Runtime behavior**: Some APIs behave differently (Collections, reflection)
5. **GC algorithms**: New algorithms (ZGC, Shenandoah) available for tuning

**OpenRewrite Migration Process:**
1. Parse Java source code into AST (Abstract Syntax Tree)
2. Apply rewrite recipes (e.g., `org.openrewrite.java.migrate.UpgradeToJava21`)
3. Transform AST nodes (replace deprecated APIs, update syntax)
4. Generate modified source code
5. Validate with compilation and tests

## Why This Concept Exists

Java version migration is necessary because:

1. **Security**: Java 8 no longer receives free security patches. CVEs are disclosed without free fixes.
2. **Library compatibility**: Spring Boot 3.x requires Java 17+. Libraries are dropping Java 8 support.
3. **Developer productivity**: Modern language features (records, pattern matching, virtual threads) reduce code complexity.
4. **Performance**: JVM improvements in Java 11-21 provide 25-50% performance gains.
5. **Talent acquisition**: New hires expect modern Java. Recruiting against teams stuck on Java 8 is difficult.
6. **Compliance**: Regulated industries require up-to-date, supported software.

## Overview

Java version migration from Java 8 to Java 21 is a significant architectural decision involving 47 microservices, 2.3M lines of code, and 18 months of phased migration. The migration eliminates security risks, enables modern language features, and provides 25-50% performance improvements. The decision framework evaluates security posture, feature completeness, migration cost, long-term viability, and team readiness.

## References

- OpenJDK Release Notes — Java 21: https://openjdk.org/projects/jdk/21/release-notes/
- OpenRewrite Java 21 Migration Recipe: https://docs.openrewrite.org/recipes/java/migrate/upgradetojava21
- Inside Java Blog — Migration Guides: https://inside.java/
- Spring Boot 3.x Migration Guide: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide
- "Effective Java" by Joshua Bloch (3rd Edition) — Covers modern Java patterns
- JEP 444: Virtual Threads: https://openjdk.org/jeps/444
- JEP 441: Pattern Matching for switch: https://openjdk.org/jeps/441

# Java Platform: Decision Guide

## When to Apply Platform Knowledge

### JVM Tuning Decisions

| Scenario | Action | Tool |
|----------|--------|------|
| High latency, GC pauses | Switch to ZGC or Shenandoah | `-XX:+UseZGC` |
| High throughput needed | Use Parallel GC | `-XX:+UseParallelGC` |
| Large heap (>8GB) | Consider G1GC or ZGC | `-XX:+UseG1GC` |
| Low memory (<4GB) | Use Serial GC | `-XX:+UseSerialGC` |
| Container environment | Set `-XX:MaxRAMPercentage=75` | Docker/K8s |

### JDK Distribution Selection

| Distribution | License | Support | Best For |
|-------------|---------|---------|----------|
| Oracle JDK | Oracle No-Fee License | Commercial support | Enterprise with support contract |
| Eclipse Temurin | GPL v2 | Community | Production, cloud |
| Amazon Corretto | GPL v2 | Amazon support | AWS environments |
| Azul Zulu | GPL v2 | Azul support | Low-latency (with Zing) |
| GraalVM CE | GPL v2 | Community | Native compilation |

**Choose Oracle JDK when:** You need commercial support and are within license terms.

**Choose Temurin/Corretto when:** You want a free, production-ready distribution.

### Java Version Upgrade Strategy

| From → To | Key Changes | Risk |
|-----------|-------------|------|
| 8 → 11 | Modules, HTTP client, var | Medium |
| 11 → 17 | Sealed classes, pattern matching, records | Low |
| 17 → 21 | Virtual threads, string templates, patterns in switch | Low-Medium |

**Upgrade guidelines:**
1. Always upgrade one LTS version at a time
2. Run full test suite before and after
3. Check third-party library compatibility
4. Review deprecated APIs being removed

### Bytecode Optimization Awareness

| Pattern | Bytecode Impact | Alternative |
|---------|----------------|-------------|
| String concatenation in loop | Creates StringBuilder per iteration | Pre-build StringBuilder |
| Autoboxing in tight loop | Creates wrapper objects | Use primitive types |
| Reflection in hot path | Method lookup overhead | Cache MethodHandle or use direct call |
| Exception in control flow | Stack trace creation | Use return values or optional |

## JEP Lifecycle Awareness

| Stage | Meaning | Action |
|-------|---------|--------|
| Proposed | Under discussion | Monitor, provide feedback |
| Draft | Initial design | Evaluate for future projects |
| Preview | Experimental, may change | Test in non-production |
| Incubating | In module jdk.incubator | Evaluate features |
| Final | Stable, part of JDK | Adopt in production |

## Java Ecosystem Decision Matrix

| Need | Standard Library | Third-party |
|------|-----------------|-------------|
| HTTP client | `java.net.http.HttpClient` | Apache HttpClient, OkHttp |
| JSON | None (use records + text blocks) | Jackson, Gson |
| Logging | `System.Logger` | SLF4J + Logback |
| DI | None (use ServiceLoader) | Spring, Guice |
| Testing | None (use JUnit) | JUnit 5, TestNG |

## Further Reading

- [OpenJDK JEP Dashboard](https://openjdk.org/jeps/)
- [JVM Ergonomics](https://docs.oracle.com/en/java/javase/21/gctuning/ergonomics.html)
- [Java Language Specification](https://docs.oracle.com/javase/specs/)
- [Inside the JVM](https://www.infoq.com/presentations/inside-jvm/)

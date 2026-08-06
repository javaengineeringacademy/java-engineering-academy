# OpenJDK Release Process

## Release Cadence

### Modern Release Schedule (Since Java 10)

OpenJDK follows a **6-month release cadence**:

```
Java 8 (LTS) → Java 9 → Java 10 → Java 11 (LTS) → Java 12 → Java 13 →
Java 14 → Java 15 → Java 16 → Java 17 (LTS) → Java 18 → Java 19 →
Java 20 → Java 21 (LTS) → Java 22 → Java 23 → Java 24 (LTS) → ...
```

- **Non-LTS releases**: Every 6 months (March and September)
- **LTS releases**: Every 2 years (Java 11, 17, 21, 25...)
- **Feature releases**: Include new features and improvements

### Historical Releases (Pre-Java 10)

- **Java 1.0** (1996): Initial release
- **Java 1.2** (1998): Major redesign (Swing, Collections)
- **Java 5** (2004): Generics, annotations, autoboxing
- **Java 6** (2006): Performance improvements
- **Java 7** (2011): Diamonds, try-with-resources
- **Java 8** (2014): Lambda, Streams (last traditional LTS)

## LTS Releases

### What is LTS?

Long-Term Support releases receive extended updates and security patches.

| Version | Release Date | LTS Until | Extended Support |
|---------|-------------|-----------|------------------|
| Java 8 | March 2014 | March 2022 | 2030+ (Oracle) |
| Java 11 | September 2018 | September 2023 | 2028+ (various) |
| Java 17 | September 2021 | September 2026 | 2031+ (various) |
| Java 21 | September 2023 | September 2028 | 2033+ (various) |
| Java 25 | September 2025 | September 2030 | TBD |

### LTS Benefits

- **Stability**: Fewer breaking changes
- **Support**: Longer security updates
- **Certification**: More vendors provide LTS builds
- **Enterprise adoption**: Preferred for production systems

## JEP Lifecycle

### Stages

```
Draft → Posted → Candidate → Final → Delivered
```

1. **Draft**
   - Initial proposal
   - Not yet public
   - Author refines the idea
   - May be withdrawn at this stage

2. **Posted**
   - Published for community review
   - Open for feedback
   - Author iterates based on comments
   - May be withdrawn or moved to Candidate

3. **Candidate**
   - Accepted for inclusion in a release
   - Implementation begins
   - Author works with reviewers
   - May be deferred to a later release

4. **Final**
   - Implementation complete
   - All tests passing
   - Documentation complete
   - Ready for release

5. **Delivered**
   - Released in a specific Java version
   - Available in JDK binaries
   - Part of the Java specification

### JEP Timeline

- **Draft → Posted**: 1-6 months (depends on complexity)
- **Posted → Candidate**: 1-3 months (community feedback)
- **Candidate → Final**: 1-6 months (implementation)
- **Final → Delivered**: Next release cycle

## Feature Selection Process

### How Features are Chosen

1. **Proposal submission**: Authors submit JEPs
2. **Community discussion**: Feedback on mailing lists
3. **Project sponsorship**: Oracle/Red Hat engineers sponsor JEPs
4. **Release planning**: Features grouped into releases
5. **Implementation**: Authors implement and test
6. **Integration**: Features merged into mainline

### Feature Categories

| Category | Examples |
|----------|---------|
| Language features | Records, sealed classes, pattern matching |
| JVM improvements | ZGC, Shenandoah, foreign memory API |
| Library additions | HTTP Client, Process API, Files API |
| Tooling | JShell, Java Compiler API |
| Performance | Compact strings, Vector API |

### Release Content

Each release includes:
- **New features**: JEPs marked as "Delivered"
- **Bug fixes**: Critical and non-critical fixes
- **Performance improvements**: Optimizations and tuning
- **Deprecations**: Warnings for future removals
- **Removals**: APIs or features removed (with prior deprecation)

## Testing and Certification

### Test Suites

| Test Suite | Purpose |
|------------|---------|
| JDK tests | Unit and regression tests |
|jtreg | Test framework for JDK |
| JCStress | Concurrency stress tests |
| JMH | Microbenchmarks |
| TCK | Technology Compatibility Kit |

### Certification Process

1. **Build JDK**: Compile from source
2. **Run test suite**: Execute all tests
3. **Pass TCK**: Technology Compatibility Kit (for commercial distributions)
4. **Internal testing**: Vendor-specific testing
5. **Release binary**: Distribute certified binary

### Quality Gates

- **Zero P1 bugs**: No critical bugs in release
- **Test pass rate**: 100% of tier-1 tests
- **Performance**: No regressions from previous release
- **Documentation**: All public API documented
- **Security**: No known vulnerabilities

## Release Announcements

### Communication Channels

- **OpenJDK blog**: Official announcements
- **Mailing lists**: Development discussions
- **Java Magazine**: Feature articles
- **JavaOne/Devoxx**: Conference presentations
- **Social media**: Twitter, LinkedIn

### Release Notes

Each release includes:
- **New features**: What's new
- **Enhancements**: Improvements to existing features
- **Bug fixes**: Resolved issues
- **Deprecations**: What's being phased out
- **Removals**: What's been removed
- **Known issues**: Outstanding problems

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)

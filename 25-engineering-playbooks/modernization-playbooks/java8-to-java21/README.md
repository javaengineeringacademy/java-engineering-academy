# Java 8 to Java 21 Migration Guide

## Overview

Java 8 was a landmark release, but the language has evolved significantly since then. Migrating to Java 21 (LTS) provides benefits including improved performance, modern language features, enhanced security, and long-term support. This playbook covers the migration path from Java 8 to Java 21.

## Migration Strategy

### Dependency Assessment

Audit all dependencies for Java version compatibility. Some libraries may require updates to support Java 21. Check build tools (Maven, Gradle) and plugins for compatibility.

### Incremental Upgrade

Upgrade through intermediate LTS versions (Java 11, Java 17) rather than jumping directly to Java 21. Each version has specific migration steps and deprecations.

### Testing

Run the existing test suite on each Java version to identify compatibility issues. Pay special attention to reflection-based code, deprecated APIs, and JVM flag changes.

## Key Language Changes

### Java 9-10: Module System and Local Variables

The module system (JPMS) affects how applications are packaged and deployed. Local variable type inference (var) simplifies variable declarations.

### Java 11: HTTP Client, String Methods, ZGC

The HTTP Client API replaces legacy HttpURLConnection. New String methods (isBlank, strip, repeat) simplify string manipulation. ZGC provides low-latency garbage collection.

### Java 14-16: Records, Text Blocks, Pattern Matching

Records reduce boilerplate for data classes. Text blocks simplify multi-line strings. Pattern matching for instanceof eliminates explicit casting.

### Java 17: Sealed Classes, Enhanced Switch

Sealed classes control class hierarchies. Enhanced switch expressions eliminate fall-through and support pattern matching.

### Java 21: Virtual Threads, Pattern Matching for Switch

Virtual threads (Project Loom) simplify concurrent programming. Pattern matching for switch eliminates explicit type checks and casting.

## Breaking Changes

### Removed APIs

Java 9+ removed several APIs deprecated in Java 8:

- javax.annotation (moved to Jakarta)
- CORBA and Java EE modules
- Applet API
- Nashorn JavaScript engine

### JVM Flag Changes

Several JVM flags were removed or changed between Java 8 and 21:

- PermGen replaced by Metaspace
- CMS garbage collector removed
- UseConcMarkSweepGC replaced by G1 or ZGC
- CompressedOops behavior changes

### Reflection Restrictions

Java 9+ restricts reflective access to internal APIs. Applications using deep reflection may encounter IllegalAccessError. Use --add-opens flags or migrate to supported APIs.

## Performance Improvements

### Garbage Collection

Java 21 includes improved GC algorithms:

- G1 GC improvements for lower latency
- ZGC for sub-millisecond pause times
- Shenandoah GC for concurrent collection

### Runtime Performance

Java 21 includes optimizations including:

- Improved JIT compilation
- Better string deduplication
- Enhanced vector API performance
- Class-data sharing improvements

## Lessons Learned

### Test Thoroughly

Java version upgrades can introduce subtle behavioral changes. Run comprehensive tests including unit, integration, and performance tests on each version.

### Update Dependencies First

Update third-party libraries before upgrading Java. Modern library versions are more likely to support Java 21 and take advantage of new features.

### Use Migration Tools

Oracle provides the Java Migration Toolkit and jdeps for analyzing dependencies. Use these tools to identify compatibility issues before upgrading.

### Leverage New Features Gradually

Adopt new language features incrementally. Start with low-risk features like var and text blocks, then progress to more complex features like virtual threads.

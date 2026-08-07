# Java Version History

## Java 1.0 (JDK 1.0)
- **Release Date:** January 23, 1996
- **Features:** Applet support, AWT GUI toolkit, basic collections (Vector, Hashtable), Java bytecode compiler, JVM specification
- **Deprecated:** N/A (initial release)
- **Removed:** N/A
- **Performance:** Basic interpreter-based execution model
- **Security:** Sandbox model for applets, SecurityManager introduced
- **Why Introduced:** Sun Microsystems created Java to provide a platform-independent language for embedded systems and web applets

## Java 1.1 (JDK 1.1)
- **Release Date:** February 19, 1997
- **Features:** Inner classes, JDBC for database access, JavaBeans component model, RMI, JIT compilation, Internationalization support
- **Deprecated:** AWT event model (replaced by delegation model), Thread.stop(), Thread.suspend(), Thread.resume()
- **Removed:** N/A
- **Performance:** JIT compiler significantly improved execution speed over pure interpretation
- **Security:** Enhanced SecurityManager, signed applets
- **Why Introduced:** Addressed major gaps in Java 1.0, added enterprise connectivity and better component model

## Java 1.2 (J2SE 1.2)
- **Release Date:** December 8, 1998
- **Features:** Swing GUI framework, Collections Framework (List, Set, Map), Java 2D, Drag and Drop, CORBA integration, JIT compiler default
- **Deprecated:** Many AWT components replaced by Swing equivalents
- **Removed:** N/A
- **Performance:** HotSpot JVM introduced as default, generational garbage collection
- **Security:** JCE (Java Cryptography Extension), JAAS (Java Authentication and Authorization Service)
- **Why Introduced:** Major overhaul to address enterprise needs and modernize the platform with Collections and Swing

## Java 1.3 (J2SE 1.3)
- **Release Date:** May 8, 2000
- **Features:** HotSpot JVM as default, Java Platform Debugger Architecture (JPDA), JavaSound API, JNDI as core API, CORBA IIOP
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** HotSpot JVM default brought major performance improvements with adaptive optimization
- **Security:** Security improvements in the JVM and class loaders
- **Why Introduced:** Focused on performance, reliability, and fixing issues from previous releases

## Java 1.4 (J2SE 1.4)
- **Release Date:** February 6, 2002
- **Features:** NIO (Non-blocking I/O), regular expressions, logging API, assertions, cryptography enhancements, XML processing, JDBC 3.0
- **Deprecated:** Some legacy networking classes
- **Removed:** N/A
- **Performance:** NIO channels and buffers enabled high-performance I/O operations
- **Security:** SSL/TLS support built-in, JAAS improvements, Elliptic Curve Cryptography
- **Why Introduced:** First community-driven release (JCP), added essential I/O and text processing capabilities

## Java 5 (J2SE 5.0)
- **Release Date:** September 30, 2004
- **Features:** Generics, enhanced for-loop, autoboxing/unboxing, varargs, enums, annotations, concurrent utilities (java.util.concurrent), Swing threading improvements
- **Deprecated:** Some Thread methods, System.runFinalizersOnExit()
- **Removed:** N/A
- **Performance:** Improved concurrent utilities with lock-free algorithms, memory model improvements
- **Security:** Security annotations, improved JAAS integration
- **Why Introduced:** Major language enhancement release to reduce boilerplate code and improve type safety

## Java 6 (Java SE 6)
- **Release Date:** December 11, 2006
- **Features:** Scripting engine support (JSR 223), JDBC 4.0,@WebService annotations, compiler API, improved Collections, desktop integration, JVM improvements
- **Deprecated:** Some JavaScript engine APIs, Java Browser Plugin (later)
- **Removed:** N/A
- **Performance:** Significantly improved startup time, memory footprint, and throughput
- **Security:** Improved XML digital signature, smart card I/O, stronger cipher suites
- **Why Introduced:** Enterprise-focused release improving web services, database connectivity, and performance

## Java 7 (Java SE 7)
- **Release Date:** July 28, 2011
- **Features:** Diamond operator (<>, try-with-resources, string in switch, NIO.2 file system, invokedynamic bytecode, fork/join framework, try-with-resources, multi-catch
- **Deprecated:** Some Thread methods (suspend, resume, stop)
- **Removed:** N/A
- **Performance:** invokedynamic enabled better dynamic language support on JVM, G1 garbage collector available
- **Security:** Elliptic curve cryptography, TLS 1.1/1.2 support, enhanced SecurityManager
- **Why Introduced:** First OpenJDK release, modernized language features and improved I/O handling

## Java 8 (Java SE 8)
- **Release Date:** March 18, 2014
- **Features:** Lambda expressions, Stream API, Optional class, new Date/Time API (JSR 310), default methods in interfaces, Nashorn JavaScript engine, method references, functional interfaces
- **Deprecated:** Applet API (deprecated in 9), Nashorn JavaScript engine (deprecated in 11)
- **Removed:** N/A
- **Performance:** Parallel streams, improved ConcurrentMap operations, compact strings
- **Security:** TLS 1.2 default, enhanced encryption algorithms, security improvements in JCE
- **Why Introduced:** Most significant Java release since Java 5, bringing functional programming to Java

## Java 9 (Java SE 9)
- **Release Date:** September 21, 2017
- **Features:** Java Platform Module System (JPMS), JShell REPL, HTTP/2 client, improved Javadoc (HTML5), multi-release JARs, private interface methods, process API, reactive streams
- **Deprecated:** Applet API, Java EE modules (corba, activation, xml.ws)
- **Removed:** N/A
- **Performance:** Better startup and memory through modular runtime, smaller runtime images
- **Security:** Improved cryptographic algorithms, TLS 1.3 support, platform security enhancements
- **Why Introduced:** Modularization of Java platform to improve maintainability, security, and performance

## Java 10 (Java SE 10)
- **Release Date:** March 20, 2018
- **Features:** Local variable type inference (var), application CDS, G1 garbage collector parallel full GC, thread-local control, root certificates, Java-based JIT compiler (Graal experimental)
- **Deprecated:** Some CMS garbage collector flags
- **Removed:** N/A
- **Performance:** G1 parallel full GC, application class-data sharing for faster startup
- **Security:** Root certificate authority added, TLS 1.3 default
- **Why Introduced:** Shorter release cycle (6 months), focused on developer productivity and performance

## Java 11 (Java SE 11)
- **Release Date:** September 25, 2018
- **Features:** HTTP Client standardized, new String methods (isBlank, strip, lines, repeat), local variable syntax for lambda parameters, single-file source launch, ZGC experimental, Flight Recorder open-sourced
- **Deprecated:** Nashorn JavaScript engine, Java EE modules (removed from default)
- **Removed:** Java EE modules (corba, activation, xml.ws, xml.bind, xml.ws.annotation, transaction)
- **Performance:** ZGC experimental low-latency garbage collector, Flight Recorder for production profiling
- **Security:** TLS 1.3 default, ChaCha20-Poly1305 cipher suites, Curve25519 key agreement
- **Why Introduced:** First LTS release under 6-month cycle, removed legacy Java EE modules

## Java 12 (Java SE 12)
- **Release Date:** March 19, 2019
- **Features:** Switch expressions (preview), Shenandoah GC (experimental), JVM Constants API, default CDS archives, Teeing Collectors, String.indent(), indent(), transform()
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Shenandoah GC for ultra-low pause times, CDS archives improve startup
- **Security:** Security enhancements in cryptographic operations
- **Why Introduced:** Preview features for switch expressions, new garbage collector options

## Java 13 (Java SE 13)
- **Release Date:** September 17, 2019
- **Features:** Text blocks (preview), ZGC improvements, dynamic CDS archiving, Socket API reimplementation, switch expressions (second preview)
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** ZGC now supports concurrent class unloading, dynamic CDS for better startup
- **Security:** Improved socket implementation security
- **Why Introduced:** Continued preview of text blocks and switch expressions, performance improvements

## Java 14 (Java SE 14)
- **Release Date:** March 17, 2020
- **Features:** Switch expressions (standard), records (preview), pattern matching for instanceof (preview), helpful NullPointerExceptions, streams gatherers (preview), NUMA-aware G1, foreign memory API (incubator)
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** NUMA-aware G1 for better memory allocation, improved GC logging
- **Security:** Enhanced exception details for better debugging without exposing internals
- **Why Introduced:** Preview of major language features (records, pattern matching), improved diagnostics

## Java 15 (Java SE 15)
- **Release Date:** September 15, 2020
- **Features:** Sealed classes (preview), hidden classes, text blocks (standard), Edwards-Curve Digital Signature Algorithm, ZGC production, Shenandoah production, Nashorn removed
- **Deprecated:** N/A
- **Removed:** Nashorn JavaScript engine, RMI Activation
- **Performance:** ZGC and Shenandoah now production-ready, low-latency GC options
- **Security:** EdDSA digital signature algorithm, improved TLS support
- **Why Introduced:** Text blocks standardized, major GC improvements production-ready, removed deprecated Nashorn

## Java 16 (Java SE 16)
- **Release Date:** March 16, 2021
- **Features:** Records (standard), pattern matching for instanceof (standard), Vector API (incubator), foreign linker API (incubator), unix domain socket channels, macOS/AArch64 support, Alpine Linux support
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Vector API for SIMD computations, improved AArch64 support
- **Security:** Strong encapsulation of JDK internals by default, enhanced crypto support
- **Why Introduced:** Records and pattern matching standardized, platform expansion to ARM and Alpine

## Java 17 (Java SE 17)
- **Release Date:** September 14, 2021
- **Features:** Sealed classes (standard), pattern matching for instanceof (standard), text blocks (standard), records (standard), Foreign Function & Memory API (incubator), enhanced pseudo-random number generators, stream.toList(), macOS/AArch64 delivery, strong encapsulation of JDK internals
- **Deprecated:** Security Manager (deprecated for removal), Applet API (deprecated for removal)
- **Removed:** N/A
- **Performance:** New PRNG algorithms, improved Stream.toList() performance, enhanced memory management
- **Security:** Strong encapsulation enforced, Apple Silicon native support, improved random number generation, reduced attack surface through encapsulation
- **Why Introduced:** Second LTS release under new 6-month cycle. Sealed classes, pattern matching, text blocks, and records all finalized. Major security hardening through strong encapsulation. Foreign Function & Memory API incubated for safer native interop

## Java 18 (Java SE 18)
- **Release Date:** March 22, 2022
- **Features:** Simple web server (jwebserver) for testing and prototyping, code snippets in Javadoc (@snippet tag), UTF-8 as default charset, Internet-address resolution SPI, Foreign Function & Memory API (second incubator), Vector API (third incubator), finalization deprecated for removal
- **Deprecated:** Finalization (Object.finalize()) deprecated for removal
- **Removed:** N/A
- **Performance:** Simple web server for quick HTTP testing, improved Javadoc generation with code snippets
- **Security:** UTF-8 default eliminates charset-related vulnerabilities, more predictable encoding behavior across platforms
- **Why Introduced:** Developer convenience tools, continued incubation of Foreign Function API, deprecating finalization to prepare for its removal and improve memory safety

## Java 19 (Java SE 19)
- **Release Date:** September 20, 2022
- **Features:** Virtual threads (preview), structured concurrency (incubator), Foreign Function & Memory API (preview), record patterns (preview), Vector API (fourth incubator), pattern matching for switch (third preview), concurrent thread-local handshakes
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Virtual threads enable massive concurrency with lightweight threads, improved thread scheduling, reduced context-switching overhead
- **Security:** Enhanced concurrency model security, virtual thread isolation, safer foreign memory access
- **Why Introduced:** Major concurrency revolution with virtual threads preview. Foreign Function & Memory API reaching preview maturity. Record patterns extending pattern matching capabilities

## Java 20 (Java SE 20)
- **Release Date:** March 21, 2023
- **Features:** Scoped values (incubator), record patterns (second preview), pattern matching for switch (fourth preview), virtual threads (second preview), structured concurrency (second incubator), Foreign Function & Memory API (third preview)
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Continued optimization of virtual threads, improved pattern matching performance, refined scoped value access
- **Security:** Continued work on memory safety through Foreign Function API, enhanced type safety in pattern matching
- **Why Introduced:** Continued preview/incubation of major features for finalization in Java 21 LTS. Incremental refinements based on community feedback

## Java 21 (Java SE 21)
- **Release Date:** September 19, 2023
- **Features:** Virtual threads (standard), pattern matching for switch (standard), record patterns (standard), sequenced collections (SequencedCollection, SequencedMap, SequencedSet), string templates (preview), unnamed patterns and variables (preview), generational mode for ZGC (default), Key Encapsulation Mechanism API, Deprecate Windows 32-bit x86 port for removal
- **Deprecated:** Windows 32-bit x86 port deprecated for removal, finalization for removal (continued)
- **Removed:** N/A
- **Performance:** Virtual threads now standard for high-concurrency workloads, generational ZGC default for better throughput and lower latency, improved collection iteration performance
- **Security:** Key Encapsulation Mechanism API for post-quantum cryptography, enhanced security policies, stronger platform encapsulation
- **Why Introduced:** Major LTS release finalizing virtual threads (Project Loom), pattern matching, and record patterns for production use. Sequenced collections provide predictable iteration order. ZGC generational mode becomes default for production workloads

## Java 22 (Java SE 22)
- **Release Date:** March 19, 2024
- **Features:** Unnamed variables (standard), statements before super() (preview), Stream gatherers (preview), Foreign Function & Memory API (standard), region pinning for G1 GC, class-file API (preview), launch multi-file source programs
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Region pinning for G1 GC reduces GC pause times by avoiding full-pinning of heap regions, improved native interop performance through standardized Foreign Function & Memory API
- **Security:** Foreign Function & Memory API provides safer alternative to JNI for native code access, enhanced memory safety guarantees, reduced risk of memory corruption
- **Why Introduced:** Foreign Function & Memory API standardized for production native interop without JNI safety risks. Unnamed variables reduce code noise. G1 region pinning improves GC performance for native-heavy workloads

## Java 23 (Java SE 23)
- **Release Date:** September 17, 2024
- **Features:** Primitive types in patterns (preview), Class-File API (preview), Markdown documentation comments (/**/ with Markdown), ZGC: Generational mode by default (non-generational removed), module import declarations (preview), primitive type patterns in switch
- **Deprecated:** N/A
- **Removed:** ZGC non-generational mode (generational only)
- **Performance:** ZGC generational mode default improves throughput for most applications, better garbage collection for large heaps
- **Security:** Class-File API enables safer bytecode manipulation tools, reduced attack surface in class loading
- **Why Introduced:** Continued language evolution with primitive pattern matching. ZGC simplification by making generational mode the only mode. Markdown comments improve documentation readability

## Java 24 (Java SE 24)
- **Release Date:** March 18, 2025
- **Features:** Class-File API (standard), Stream gatherers (standard), ahead-of-time class loading & linking, permanently disable Security Manager, synchronize virtual threads without pinning, ZGC: Remove non-generational mode, quantum-resistant cryptography (ML-KEM, ML-DSA, SLH-DSA)
- **Deprecated:** Security Manager permanently disabled (cannot be re-enabled)
- **Removed:** ZGC non-generational mode fully removed
- **Performance:** Ahead-of-time class loading and linking improves startup time and reduces memory footprint, virtual threads can now synchronize without pinning to platform threads
- **Security:** Quantum-resistant cryptography algorithms (ML-KEM, ML-DSA, SLH-DSA) provide defense against future quantum computing attacks, Security Manager disabled to reduce attack surface
- **Why Introduced:** Post-quantum cryptography prepares Java for the quantum computing era. Virtual thread synchronization without pinning completes the virtual threads story. Ahead-of-time class loading improves cloud-native deployments

## Java 25 (Java SE 25)
- **Release Date:** September 16, 2025
- **Features:** Stable values (preview), scoped values (standard), module import declarations (preview), compact source files and instance main methods (preview), flexible constructor bodies (preview), ahead-of-time command-line ergonomics, JFR CPU-time profiling, key derivation function API (standard), compact object headers (experimental)
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Ahead-of-time command-line ergonomics simplify AOT compilation, compact object headers reduce memory usage by 25% for small objects, JFR CPU-time profiling for production performance analysis
- **Security:** Key derivation function API standardizes secure key generation, improved cryptographic operations, enhanced monitoring capabilities
- **Why Introduced:** LTS release focusing on developer ergonomics (compact source files, flexible constructors), memory efficiency (compact object headers), and production observability (JFR CPU-time profiling). Scoped values standardized for structured concurrency patterns

## Java 26 (Java SE 26)
- **Release Date:** March 17, 2026
- **Features:** HTTP/3 for HTTP Client API (preview), PEM encodings of cryptographic objects (preview), structured concurrency (preview), lazy constants (preview), Vector API (11th incubator), primitive types in patterns (preview), remove Applet API, ahead-of-time object caching with any GC, G1 GC: Improve throughput by reducing synchronization, prepare to make final mean final
- **Deprecated:** Applet API removed
- **Removed:** Applet API (final removal)
- **Performance:** Ahead-of-time object caching with any GC improves startup and reduces allocation overhead, G1 GC throughput improved by reducing synchronization overhead between GC threads
- **Security:** HTTP/3 support provides improved security and performance for web communications, PEM encodings simplify cryptographic object handling, structured concurrency improves error handling safety
- **Why Introduced:** HTTP/3 modernizes web connectivity. Applet API removal completes the transition away from browser-based Java. Lazy constants and AOT object caching improve runtime performance. Preparing final keyword semantics for stricter enforcement in future releases

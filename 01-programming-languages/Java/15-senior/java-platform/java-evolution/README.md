# Java Version Evolution

## Complete Timeline from Java 1.0 to Java 23

| Version | Year | Key Features |
|---------|------|--------------|
| 1.0 | 1996 | Applets, AWT, basic OOP |
| 1.1 | 1997 | Inner classes, JDBC, Beans |
| 1.2 | 1998 | Collections, Swing, JIT |
| 1.3 | 2000 | HotSpot JVM |
| 1.4 | 2002 | NIO, regex, logging |
| 5 | 2004 | Generics, enums, autoboxing, varargs, enhanced for |
| 6 | 2006 | Scripting, JDBC 4.0 |
| 7 | 2011 | Diamond operator, try-with-resources, NIO.2 |
| 8 | 2014 | Lambdas, streams, Optional, default methods |
| 9 | 2017 | Module system, JShell, private methods in interfaces |
| 10 | 2018 | Local variable inference (var), HTTP client |
| 11 | 2018 | LTS, HTTP client standard, ZGC experimental |
| 12 | 2019 | Switch expressions preview, JVM Constants API |
| 13 | 2019 | Text blocks preview, ZGC improvements |
| 14 | 2020 | Records preview, pattern matching instanceof preview, Switch expressions |
| 15 | 2020 | Text blocks, sealed classes preview, hidden classes |
| 16 | 2021 | Records, pattern matching instanceof, Vector API preview |
| 17 | 2021 | LTS, sealed classes, pattern matching switch preview |
| 18 | 2022 | Simple web server, code snippets, foreign function preview |
| 19 | 2022 | Record patterns preview, pattern matching switch third preview |
| 20 | 2023 | Record patterns, pattern matching switch, virtual threads |
| 21 | 2023 | LTS, virtual threads, record patterns, pattern matching switch |
| 22 | 2024 | Unnamed variables, statements before super(), foreign function & memory |
| 23 | 2024 | Primitive types in patterns, module import declarations |

---

## Java 1.0 (1996)

### Motivation
Java was created to solve the "write once, run anywhere" problem. The language needed to be simple enough for web developers while providing object-oriented capabilities for enterprise applications.

### New Features
- **Applets**: Browser-embedded applications (revolutionary at the time)
- **Abstract Window Toolkit (AWT)**: Basic GUI components
- **Core OOP**: Classes, objects, inheritance, polymorphism
- **Garbage Collection**: Automatic memory management
- **Thread support**: Built-in multithreading

### Deprecated APIs
- `Thread.stop()` (deprecated in 1.2, removed in later versions)
- `Thread.suspend()` and `Thread.resume()`

### Removed APIs
- None (first release)

### Impact on Existing Code
- Established the foundation for all future Java development
- Applets became the primary distribution mechanism

### Migration Effort
- Low (new language, no existing codebase)

---

## Java 1.1 (1997)

### Motivation
Response to developer feedback requesting better event handling, database connectivity, and component reuse.

### New Features
- **Inner Classes**: Nested classes for better encapsulation
- **JDBC**: Database connectivity API
- **JavaBeans**: Reusable component architecture
- **Event Delegation Model**: Replaced the previous event model
- **Object Serialization**: Converting objects to byte streams
- **Internationalization**: Unicode support
- **Reflection**: Runtime class inspection

### Deprecated APIs
- `java.util.Date` constructors (deprecated in favor of `Calendar`)
- `java.util.Date.getHours()`, `getMinutes()`, `getSeconds()`

### Removed APIs
- None

### Impact on Existing Code
- Event handling model changes required code updates
- Inner classes added new organizational capabilities

### Migration Effort
- Low to Medium (event model changes)

---

## Java 1.2 (1998)

### Motivation
Major overhaul to position Java as a serious enterprise platform. Needed better collections, modern GUI, and performance improvements.

### New Features
- **Collections Framework**: List, Set, Map interfaces with implementations
- **Swing**: Modern GUI toolkit replacing AWT
- **Just-In-Time (JIT) Compiler**: Significant performance improvement
- **Java Foundation Classes (JFC)**: Integrated Swing, accessibility, and drag-and-drop
- **Java 2D API**: Advanced 2D graphics
- **Drag and Drop**: Native drag-and-drop support
- **Accessibility API**: Assistive technology support

### Deprecated APIs
- `java.util.Vector`, `java.util.Hashtable` (replaced by Collections)
- `java.util.Stack` (replaced by `Deque`)

### Removed APIs
- None

### Impact on Existing Code
- Collections framework became the standard
- AWT code required updates for Swing migration

### Migration Effort
- Medium (Collections adoption, Swing migration)

---

## Java 1.3 (2000)

### Motivation
Performance optimization and enterprise features. HotSpot JVM was crucial for Java's performance reputation.

### New Features
- **HotSpot JVM**: Dynamic compilation for better performance
- **Java Naming and Directory Interface (JNDI)**: Naming and directory services
- **Java Platform Debugger Architecture (JPDA)**: Debugging infrastructure
- **RMI over IIOP**: CORBA integration
- **Java Sound API**: Audio playback and recording
- **Java Cryptography Extension (JCE)**: Security features
- **Java IDL**: CORBA support

### Deprecated APIs
- `java.lang.Thread.destroy()` and `Thread.stop()` (continued deprecation)

### Removed APIs
- None

### Impact on Existing Code
- Performance improvements benefited all applications
- Minimal code changes required

### Migration Effort
- Low (primarily JVM improvements)

---

## Java 1.4 (2002)

### Motivation
Enterprise readiness: networking, security, and performance improvements. Response to growing enterprise adoption.

### New Features
- **Non-blocking I/O (NIO)**: Scalable I/O operations
- **Regular Expressions**: `java.util.regex` package
- **Logging API**: `java.util.logging`
- **Assertions**: `assert` keyword for debugging
- **Exception Chaining**: `initCause()` method
- **Image I/O**: Reading and writing images
- **Preferences API**: User preferences storage
- **XML Processing**: JAXP support
- **IPv6 Support**: Network protocol support

### Deprecated APIs
- `java.lang.Thread.stop(Throwable)` 
- `java.lang.Thread.destroy()`
- `java.lang.Runtime.runFinalizersOnExit()`
- `java.lang.System.runFinalizersOnExit()`

### Removed APIs
- None

### Impact on Existing Code
- NIO required significant learning curve
- Assertions enabled better debugging practices

### Migration Effort
- Medium (NIO learning curve, regex adoption)

---

## Java 5 (2004)

### Motivation
Major language enhancement to reduce boilerplate code and improve type safety. The most significant language update since Java 1.0.

### New Features
- **Generics**: Type-safe collections and classes
- **Enums**: First-class enumeration types
- **Autoboxing/Unboxing**: Automatic primitive-wrapper conversion
- **Varargs**: Variable-length argument lists
- **Enhanced for Loop**: Simplified iteration
- **Static Import**: Import static members
- **Annotations**: Metadata for compile-time and runtime processing
- **Covariant Return Types**: Overridden methods can return subtypes
- **Formatted I/O**: `printf`-style output
- **Thread-safe Collections**: `ConcurrentHashMap`, `CopyOnWriteArrayList`

### Deprecated APIs
- `java.lang.Thread.stop()`, `suspend()`, `resume()`, `destroy()` (final deprecation)
- `java.lang.Runtime.exec(String)` 
- `java.util.Date` constructors

### Removed APIs
- None

### Impact on Existing Code
- Generics required type parameter updates to collections
- Enhanced for loop replaced traditional indexed loops
- Annotations became ubiquitous in frameworks

### Migration Effort
- Medium to High (generics adoption, collection type parameters)

---

## Java 6 (2006)

### Motivation
Enterprise and web services focus. Improving database connectivity and web service support.

### New Features
- **Scripting Language Support**: JSR 223 (Rhino, JRuby, Groovy)
- **JDBC 4.0**: Improved database connectivity
- **Java Compiler API**: Programmatic compilation
- **Pluggable Annotation Processing**: Compile-time annotation processing
- **Web Services**: WS-* stack support
- **JAXB 2.0**: XML binding
- **StAX**: Streaming API for XML
- **Common Annotations**: JSR 250
- **Scripting API**: `javax.script` package
- **Compiler API**: `javax.tools` package

### Deprecated APIs
- `java.lang.Thread.stop()`, `suspend()`, `resume()`, `destroy()` (final deprecation in later versions)
- `javax.xml.soap.SOAPException` (moved to SAAJ)

### Removed APIs
- None

### Impact on Existing Code
- JDBC 4.0 required driver updates
- Scripting support enabled polyglot programming

### Migration Effort
- Low to Medium (JDBC driver updates, scripting integration)

---

## Java 7 (2011)

### Motivation
Language simplification and modernization. Reducing boilerplate and improving developer productivity.

### New Features
- **Diamond Operator (`<>`)**: Type inference for generic instantiation
- **Try-with-resources**: Automatic resource management
- **String in Switch**: String case statements
- **NIO.2**: Complete file system API (`java.nio.file`)
- **Automatic Resource Management**: Enhanced try statement
- **Binary Literals**: `0b` prefix for binary numbers
- **Underscores in Numeric Literals**: `1_000_000`
- **Multi-catch**: Catching multiple exception types
- **Fork/Join Framework**: Parallel execution
- **Invokedynamic**: JVM instruction for dynamic languages

### Deprecated APIs
- `java.lang.Thread.stop()`, `suspend()`, `resume()`, `destroy()` (final deprecation)
- `javax.xml.soap.SOAPException`

### Removed APIs
- `java.util.logging.LogManager.addPropertyChangeListener()`
- `java.util.logging.LogManager.removePropertyChangeListener()`
- `java.util.zip.ZipFile.finalize()`

### Impact on Existing Code
- Try-with-resources simplified resource handling
- Diamond operator reduced type verbosity
- NIO.2 replaced many File operations

### Migration Effort
- Low (backward compatible improvements)

---

## Java 8 (2014)

### Motivation
Functional programming support, addressing the rise of multi-core processors and the need for more expressive code. The most impactful update since Java 5.

### New Features
- **Lambda Expressions**: Anonymous functions for functional interfaces
- **Stream API**: Declarative data processing pipelines
- **Optional**: Null safety wrapper
- **Default Methods**: Interface implementations
- **Static Methods in Interfaces**: Utility methods
- **Method References**: Shorthand for lambdas
- **Date/Time API**: `java.time` package (JSR 310)
- **Repeating Annotations**: Multiple annotations of same type
- **Base64 Encoding/Decoding**: `java.util.Base64`
- **Nashorn JavaScript Engine**: JavaScript on JVM
- **Parallel Array Sorting**: `Arrays.parallelSort()`

### Deprecated APIs
- `java.util.Date` constructors (continued deprecation)
- `java.sql.Date`, `java.sql.Time`, `java.sql.Timestamp` constructors

### Removed APIs
- `java.lang.Thread.stop()`, `suspend()`, `resume()`, `destroy()`
- `java.util.logging.LogManager.addPropertyChangeListener()`
- `java.util.logging.LogManager.removePropertyChangeListener()`
- `java.util.zip.ZipFile.finalize()`

### Impact on Existing Code
- Streams replaced many for-loops
- Lambdas simplified anonymous classes
- Optional reduced null pointer exceptions
- Default methods enabled interface evolution

### Migration Effort
- Medium (paradigm shift to functional programming)

---

## Java 9 (2017)

### Motivation
Modularity and encapsulation to address the "JAR hell" problem and improve security. Large-scale application organization.

### New Features
- **Module System (Project Jigsaw)**: `module-info.java`
- **JShell**: REPL for interactive Java
- **Private Methods in Interfaces**: Interface encapsulation
- **Process API**: `ProcessHandle`, `ProcessHandle.Info`
- **Multi-Release JARs**: Version-specific classes
- **Interface Methods Enhancements**: `private` and `static` methods
- **Reactive Streams**: `java.util.concurrent.Flow`
- **HTTP/2 Client**: `java.net.http.HttpClient`
- **Collection Factory Methods**: `List.of()`, `Set.of()`, `Map.of()`

### Deprecated APIs
- `java.applet.Applet` (deprecated)
- `java.util.jar.Pack200` classes
- `javax.xml.soap.SOAPException`

### Removed APIs
- `java.util.logging.LogManager.addPropertyChangeListener()`
- `java.util.logging.LogManager.removePropertyChangeListener()`
- `java.util.zip.ZipFile.finalize()`
- `sun.misc.Unsafe` internal methods

### Impact on Existing Code
- Module system required `module-info.java` files
- Strong encapsulation broke some reflective access
- Multi-release JARs enabled gradual migration

### Migration Effort
- High (module system adoption, encapsulation changes)

---

## Java 10 (2018)

### Motivation
Developer productivity and modern language features. Response to language competition from Kotlin, Scala, and C#.

### New Features
- **Local Variable Type Inference (`var`)**: Simplified variable declarations
- **Application Class-Data Sharing**: Improved startup time
- **Parallel Full GC for G1**: Better garbage collection
- **Thread-Local Handshakes**: Efficient thread operations
- **JVM Interface Methods**: Internal optimization
- **Graal JVM Compiler**: Experimental JIT compiler
- **HTTP Client Standard**: `java.net.http.HttpClient`

### Deprecated APIs
- None significant

### Removed APIs
- `java.util.concurrent.ThreadLocal.remove()` (deprecated removal)

### Impact on Existing Code
- `var` reduced type verbosity
- Application CDS improved deployment

### Migration Effort
- Low (backward compatible)

---

## Java 11 (2018)

### Motivation
Long-term support release focusing on enterprise features, performance, and removing deprecated APIs.

### New Features
- **LTS Release**: 8 years of support


---

**Continue to Part 2**: README-part2.md | Part 3

## Related Topics
- [Java Philosophy](../java-philosophy/) — Why features were introduced
- [JEPs](../openjdk/jeps/) — How features are proposed
- [JDK Distributions](../jdk-distributions/) — Which JDK to use
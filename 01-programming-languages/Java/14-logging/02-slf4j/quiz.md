# Quiz: SLF4J

## Multiple Choice

### Q1: What does SLF4J stand for?
- A) Simple Logging Framework for Java
- B) Simple Logging Facade for Java
- C) Standard Logging Framework for Java
- D) Structured Logging for Java Applications

### Q2: What is the primary purpose of SLF4J?
- A) To provide a logging implementation
- B) To provide a logging facade/abstraction
- C) To replace Log4j
- D) To write logs to files

### Q3: How does SLF4J find its implementation at runtime?
- A) Configuration file only
- B) Static binding via classpath scanning (ServiceLoader)
- C) Dependency injection
- D) JNDI lookup

### Q4: What is the correct way to create a logger in SLF4J?
- A) `Logger logger = new Logger();`
- B) `Logger logger = Logger.getInstance();`
- C) `Logger logger = LoggerFactory.getLogger(MyClass.class);`
- D) `Logger logger = new LoggerFactory().getLogger();`

### Q5: What happens if multiple SLF4J bindings are found on the classpath?
- A) All implementations are used
- B) The first one found is used, with warnings about others
- C) The application fails to start
- D) The last one found wins

### Q6: Which artifact provides a bridge from java.util.logging to SLF4J?
- A) `jcl-over-slf4j`
- B) `log4j-over-slf4j`
- C) `jul-to-slf4j`
- D) `slf4j-jdk14`

### Q7: What is the SLF4J 2.0 fluent API advantage?
- A) Faster execution
- B) Type-safe structured logging with method chaining
- C) Less memory usage
- D) Automatic MDC management

## Answers

1. **B** - Simple Logging Facade for Java
2. **B** - SLF4J is an abstraction layer, not an implementation
3. **B** - Uses ServiceLoader (2.x) or static binding to find providers on classpath
4. **C** - `LoggerFactory.getLogger()` is the factory method; pass `.class` for naming
5. **B** - First binding wins; SLF4J prints warnings about additional bindings
6. **C** - `jul-to-slf4j` bridges java.util.logging to SLF4J
7. **B** - Fluent API provides method chaining with `atDebug()`, `addKeyValue()`, etc.

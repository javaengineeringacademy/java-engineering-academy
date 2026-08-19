# 12. Module System Internals Deep Dive

## JPMS Architecture

### Module Declaration Syntax

```java
module com.example.myapp {
    // Dependencies
    requires java.sql;
    requires java.logging;
    requires transitive java.xml;

    // Exports (compile-time + runtime access)
    exports com.example.api;
    exports com.example.model to com.example.client;

    // Opens (reflection-only access)
    opens com.example.impl to com.example.testing;

    // Services
    uses com.example.spi.MyService;
    provides com.example.spi.MyService with com.example.impl.MyServiceImpl;
}
```

### Module Resolution Process

```
1. Module Graph Construction
   ├── Parse module-info.java for each module
   ├── Build directed graph of requires relationships
   ├── Check for split packages (prohibited)
   └── Verify all required modules exist

2. Resolution
   ├── Start from root modules (main class or --add-modules)
   ├── Follow requires edges transitively
   ├── Add all reachable modules to module graph
   └── Fail if any required module is missing

3. Linking
   ├── Verify exports and opens are valid
   ├── Check service provider availability
   ├── Validate module version compatibility
   └── Prepare module for execution

4. Initialization
   ├── Initialize module descriptors
   ├── Set up package access control
   ├── Configure reflection restrictions
   └── Start application
```

### Strong Encapsulation

```
Encapsulation Rules:
├── Unexported package: No compile-time access
│   ├── Import fails at compile time
│   ├── Direct reference fails at compile time
│   └── Only accessible within the module
├── Exported package: Full access
│   ├── Import succeeds
│   ├── Direct reference succeeds
│   └── Public types accessible
├── Opened package: Reflection access only
│   ├── Compile-time access NOT granted
│   ├── Reflection access granted
│   └── Used by frameworks (Spring, Hibernate)
└── Module boundary: Strong encapsulation
    ├── No escape of internal types
    ├── Clean API boundaries
    └── Prevents internal API usage
```

### Module Path vs Classpath

```
Classpath (Legacy):
├── Flat namespace (all classes visible)
├── No encapsulation
├── Fragile (missing classes at runtime)
├── JAR hell (version conflicts)
└── No reliable configuration

Module Path (Modern):
├── Named modules with encapsulation
├── Reliable configuration (fail-fast)
├── Better performance (JIT, AOT)
├── Startup optimization
└── Custom runtime images (jlink)

Migration:
├── Automatic modules (JARs without module-info.java)
├── Named modules (with module-info.java)
├── Unnamed module (classpath, compatibility)
└── Mixed module path and classpath
```

### Key JDK Modules

```
java.base:
├── Core classes: String, Object, Integer, etc.
├── Automatically required by all modules
├── No module-info.java needed (implicit)
└── Contains java.lang, java.util, java.io, etc.

java.sql:
├── JDBC API
├── Connection, Statement, ResultSet
└── Requires: java.base

java.logging:
├── java.util.logging
├── Logger, Handler, Formatter
└── Requires: java.base

java.management:
├── JMX API
├── MXBeans for monitoring
└── Requires: java.base

java.desktop:
├── AWT/Swing
├── GUI components
└── Requires: java.base

java.xml:
├── XML processing
├── DOM, SAX, StAX parsers
└── Requires: java.base
```

### Service Provider Interface (SPI)

```
Service Declaration (Consumer):
module com.example.app {
    uses com.example.spi.MessageService;
}

Service Declaration (Provider):
module com.example.email {
    provides com.example.spi.MessageService
        with com.example.email.EmailService;
}

Service Loading:
ServiceLoader<MessageService> loader =
    ServiceLoader.load(MessageService.class);
for (MessageService service : loader) {
    service.send("Hello");
}

Resolution:
├── Module system discovers all providers
├── Validates provider modules are accessible
├── Returns service instances via ServiceLoader
└── No classpath scanning needed
```

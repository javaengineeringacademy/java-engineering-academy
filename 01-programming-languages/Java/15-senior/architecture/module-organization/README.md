# Java Module Organization

Best practices for organizing Java packages and modules.

## Package Naming Conventions

### Reverse Domain Name
```java
// Standard convention
com.company.project.module
org.apache.commons.lang
net.example.utilities

// Examples
com.google.guava
org.springframework.boot
io.netty.channel
```

### Package Structure
```
com.company.project/
├── api/                    # Public API
│   ├── model/
│   └── service/
├── internal/               # Internal implementation
│   ├── dao/
│   └── util/
├── spi/                    # Service Provider Interface
└── module-info.java
```

### Naming Rules
- All lowercase
- No underscores (use camelCase for multi-word)
- Start with reverse domain
- Avoid `java.*`, `javax.*` prefixes

## Module Structure Patterns

### Layered Architecture
```
com.company.project/
├── api/                    # Contracts
├── service/                # Business logic
├── repository/             # Data access
├── model/                  # Domain objects
└── util/                   # Utilities
```

### Feature-Based Modules
```
com.company.project/
├── auth/                   # Authentication feature
│   ├── api/
│   ├── service/
│   └── repository/
├── payment/                # Payment feature
│   ├── api/
│   ├── service/
│   └── repository/
└── notification/           # Notification feature
```

### Hexagonal Architecture
```
com.company.project/
├── core/                   # Domain logic
│   ├── model/
│   └── service/
├── ports/                  # Interfaces
│   ├── inbound/
│   └── outbound/
└── adapters/               # Implementations
    ├── web/
    └── persistence/
```

## API vs Implementation Packages

### Public API
```java
// api/UserService.java
package com.company.api;

public interface UserService {
    User findById(String id);
    User create(CreateUserRequest request);
}

// api/model/User.java
package com.company.api.model;

public class User {
    private final String id;
    private final String name;
    // Immutable
}
```

### Internal Implementation
```java
// internal/UserServiceImpl.java
package com.company.internal;

import com.company.api.UserService;

class UserServiceImpl implements UserService {
    private final UserRepository repository;
    
    @Override
    public User findById(String id) {
        return repository.findById(id);
    }
}
```

### Module System (Java 9+)
```java
// module-info.java
module com.company.project {
    // Public API
    exports com.company.api;
    exports com.company.api.model;
    
    // Internal (not exported)
    // com.company.internal is not exported
    
    // Dependencies
    requires java.sql;
    requires java.net.http;
    
    // Service usage
    uses com.company.spi.DataProvider;
    
    // Service provider
    provides com.company.spi.DataProvider
        with com.company.internal.DataProviderImpl;
}
```

## Service Provider Interface (SPI)

### Define Service
```java
// spi/DataProvider.java
package com.company.spi;

public interface DataProvider {
    String getSource();
    List<String> getData(String query);
}
```

### Implement Service
```java
// internal/DatabaseDataProvider.java
package com.company.internal;

import com.company.spi.DataProvider;

public class DatabaseDataProvider implements DataProvider {
    @Override
    public String getSource() {
        return "database";
    }
    
    @Override
    public List<String> getData(String query) {
        // Database implementation
    }
}
```

### Module Declaration
```java
// module-info.java
module com.company.project {
    uses com.company.spi.DataProvider;
}

// module-info.java (provider)
module com.company.database {
    provides com.company.spi.DataProvider
        with com.company.internal.DatabaseDataProvider;
}
```

### Service Loader
```java
// Loading services
ServiceLoader<DataProvider> loader = ServiceLoader.load(DataProvider.class);
for (DataProvider provider : loader) {
    System.out.println("Provider: " + provider.getSource());
}
```

## Multi-Module Projects

### Maven Structure
```xml
<!-- Parent POM -->
<project>
    <groupId>com.company</groupId>
    <artifactId>project-parent</artifactId>
    <packaging>pom</packaging>
    
    <modules>
        <module>project-api</module>
        <module>project-impl</module>
        <module>project-app</module>
    </modules>
</project>

<!-- API Module -->
<project>
    <artifactId>project-api</artifactId>
</project>

<!-- Implementation Module -->
<project>
    <artifactId>project-impl</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.company</groupId>
            <artifactId>project-api</artifactId>
        </dependency>
    </dependencies>
</project>
```

### Gradle Structure
```groovy
// settings.gradle
include 'project-api', 'project-impl', 'project-app'

// project-impl/build.gradle
dependencies {
    implementation project(':project-api')
}
```

### Module Communication
```java
// Module A (API)
module com.company.api {
    exports com.company.api;
}

// Module B (Implementation)
module com.company.impl {
    requires com.company.api;
    exports com.company.impl;
}

// Module C (Application)
module com.company.app {
    requires com.company.api;
    requires com.company.impl;
}
```

## Best Practices

### Package Design Principles
1. **High Cohesion**: Related classes together
2. **Low Coupling**: Minimize dependencies
3. **Stable Dependencies**: Depend on stable packages
4. **Acyclic Dependencies**: No circular dependencies

### Package Organization Tips
```
com.company.project/
├── domain/                 # Domain objects (entities, value objects)
│   ├── model/
│   ├── repository/
│   └── service/
├── application/            # Application services
│   ├── dto/
│   └── service/
├── infrastructure/         # Technical implementation
│   ├── persistence/
│   ├── messaging/
│   └── web/
└── common/                 # Shared utilities
    ├── util/
    └── exception/
```

### Package Naming Tips
- Use nouns for packages (model, service, util)
- Use verbs for methods within packages
- Avoid abbreviations (auth not authn)
- Keep consistent naming across modules

### Documentation
```java
/**
 * User service for managing user operations.
 * 
 * <p>This package provides the core user management functionality
 * including creation, retrieval, and deletion of users.</p>
 * 
 * @see com.company.api.UserService
 * @since 1.0
 */
package com.company.api;
```

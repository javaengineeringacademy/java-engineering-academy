# When to Use Project Structure

## Decision Guide

### Package Organization

| Structure | Use When | Example |
|-----------|----------|---------|
| By layer | Standard web apps | `controller/`, `service/`, `repository/` |
| By feature | Microservices, modular apps | `user/`, `order/`, `payment/` |
| By domain | Domain-driven design | `sales/`, `inventory/`, `shipping/` |
| Hybrid | Large enterprise apps | `user/controller/`, `user/service/` |

### Package Naming

| Convention | Use When | Example |
|------------|----------|---------|
| Reversed domain | Standard Java | `com.company.project` |
| Functional | Small projects | `models/`, `utils/`, `services` |
| Layer-based | Traditional apps | `web/`, `business/`, `data/` |

### Module Decision Tree

| Scenario | Approach |
|----------|----------|
| Single executable | Single module |
| Shared library | Separate module |
| Multiple deployments | Separate modules |
| Team boundaries | Separate modules |
| Different lifecycles | Separate modules |

### Class Organization

| Class Type | Package Location |
|------------|-----------------|
| Domain models | `model/` or `domain/` |
| Business logic | `service/` |
| Data access | `repository/` or `dao/` |
| Web endpoints | `controller/` |
| Utilities | `util/` or `common/` |
| Configuration | `config/` |
| Constants | `constants/` |

## Production Guidelines

### Standard Maven Layout
```
src/
├── main/
│   ├── java/           # Source code
│   ├── resources/      # Configuration files
│   └── webapp/         # Web resources (if applicable)
├── test/
│   ├── java/           # Test code
│   └── resources/      # Test configuration
└── pom.xml             # Build configuration
```

### Package Naming Best Practices
```java
// GOOD: Clear, hierarchical
com.company.project.module.feature

// BAD: Too generic
com.utils.helpers

// BAD: Too deep
com.company.project.module.submodule.subsubmodule.feature.class
```

### File Organization
```java
// One public class per file
// File name matches class name
// Package declaration first
// Imports organized (java.*, javax.*, org.*, com.*)
```

### Resource Organization
```
src/main/resources/
├── application.properties
├── log4j2.xml
├── templates/
│   └── email/
└── static/
    ├── css/
    └── js/
```

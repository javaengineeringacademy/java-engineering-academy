# Decision Framework: Best Practices Application

## When to Apply Each Practice

### Critical (Always Apply)

| Practice | When | Impact |
|----------|------|--------|
| Parameterized logging | Every log statement | Performance, readability |
| Exception as last argument | Every exception log | Debugging capability |
| Private static final logger | Every class | Correctness, memory |
| MDC cleanup in finally | Every MDC usage | Memory leaks |
| No sensitive data | Every log statement | Security |

### High Priority (Apply in Production)

| Practice | When | Impact |
|----------|------|--------|
| Async appenders | File/network logging | Performance |
| Level-specific thresholds | Per-package config | Noise reduction |
| Structured logging | Centralized logging | Analysis capability |
| Log rotation | All file appenders | Disk management |

### Medium Priority (Apply When Needed)

| Practice | When | Impact |
|----------|------|--------|
| Guard expensive operations | Before costly toString() | Performance |
| Marker-based logging | Cross-cutting concerns | Filtering |
| Performance monitoring | High-throughput systems | Observability |
| Custom log levels | Domain-specific events | Clarity |

## Anti-Pattern Detection

### Code Smells

```java
// SMELL: String concatenation
logger.debug("User " + userId + " action " + action);

// SMELL: System.out for logging
System.out.println("Processing: " + data);

// SMELL: Logger not static final
private Logger log = LoggerFactory.getLogger(getClass());

// SMELL: Missing exception in error log
logger.error("Failed: " + e.getMessage());

// SMELL: Logging sensitive data
logger.info("Password: " + password);

// SMELL: MDC not cleaned
MDC.put("key", value);
processRequest();
// Missing MDC.clear()
```

### Refactoring Priority

1. **Security issues** - Sensitive data in logs → Fix immediately
2. **Memory leaks** - MDC not cleaned → Fix before production
3. **Performance** - String concatenation → Fix in hot paths
4. **Correctness** - Logger declaration → Fix when touched
5. **Readability** - Vague messages → Improve incrementally

## Team Adoption Strategy

### Phase 1: Education
- Share this best practices guide
- Conduct code review training
- Create shared code templates

### Phase 2: Enforcement
- Add checkstyle rules for logger declaration
- Add SonarQube rules for logging patterns
- Include in PR review checklist

### Phase 3: Automation
- Custom linting rules
- Automated code review comments
- Logging metrics dashboards

## Code Review Checklist

For every PR, verify:

- [ ] Logger is `private static final` with `.class` reference
- [ ] All log statements use parameterized messages
- [ ] Exceptions logged as last argument (not in message)
- [ ] Log messages include relevant context
- [ ] No sensitive data (passwords, tokens, PII)
- [ ] MDC is cleaned up in `finally` blocks
- [ ] Expensive operations are guarded
- [ ] Log levels are appropriate
- [ ] No `System.out.println()` in production code
- [ ] Configuration changes are documented

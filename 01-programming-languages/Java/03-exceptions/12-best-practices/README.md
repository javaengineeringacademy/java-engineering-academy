# Exception Best Practices

## Scope

This module establishes practical, battle-tested guidelines for exception handling in
production Java code. Every rule has been distilled from real-world incidents at scale and
from the collective experience of the Java engineering community.

## Why It Exists

Poor exception handling is one of the most common sources of production incidents. Swallowed
exceptions hide failures. Overly broad catches mask bugs. Missing context makes debugging
impossible. These best practices exist so that every exception in your system is
**meaningful, traceable, and actionable**.

## Design Rationale

Java's exception model separates checked and unchecked exceptions deliberately. The language
gives you a powerful framework; these practices ensure you use it well. The guiding
principles are:

- **Fail fast** — detect problems at the earliest possible point.
- **Preserve information** — every exception must carry enough context to diagnose it.
- **Separate concerns** — use exception types to distinguish business errors from
  programming bugs.
- **Resource safety** — guarantee cleanup with language-level constructs, not manual
  bookkeeping.

---

## What Are Exception Best Practices?

Exception best practices are **coding guidelines** that govern how you throw, catch, wrap,
and propagate exceptions. They are not optional style preferences — violating them leads to
lost data, silent corruption, and outages.

---

## 10 Core Rules

### Rule 1: Catch Specific Types

Never catch `Exception`, `Throwable`, or `RuntimeException` unless you have a very
deliberate reason.

```java
// BAD
try {
    parseConfig(path);
} catch (Exception e) {
    log.error("failed", e);
}

// GOOD
try {
    parseConfig(path);
} catch (FileNotFoundException e) {
    log.error("config file not found: {}", path, e);
    throw new ConfigException("Missing config: " + path, e);
} catch (MalformedConfigException e) {
    log.error("config file is malformed: {}", path, e);
    throw e;
}
```

**Why:** Broad catches swallow bugs. A `NullPointerException` inside your block silently
disappears, turned into a generic log line. Catching specific types forces you to think
about each failure mode.

---

### Rule 2: Don't Swallow Exceptions

An empty catch block is almost always a bug. At minimum, log the exception.

```java
// BAD
try {
    user.updateEmail(newEmail);
} catch (Exception e) {
    // TODO: handle this
}

// GOOD
try {
    user.updateEmail(newEmail);
} catch (EmailValidationException e) {
    log.warn("Invalid email for user {}: {}", user.getId(), newEmail, e);
    throw new UserUpdateException("Email update failed", e);
}
```

**Why:** Swallowed exceptions destroy observability. When a customer reports a problem, the
absence of any log entry makes diagnosis impossible.

---

### Rule 3: Chain Exceptions

Always pass the cause as the second argument to the constructor.

```java
// BAD
try {
    configService.reload();
} catch (IOException e) {
    throw new ConfigException("reload failed");
}

// GOOD
try {
    configService.reload();
} catch (IOException e) {
    throw new ConfigException("reload failed", e);
}
```

**Why:** Chaining preserves the full stack trace. Without it, you lose the root cause —
the most valuable piece of diagnostic information.

---

### Rule 4: Use try-with-resources

Any `AutoCloseable` resource must be managed with try-with-resources.

```java
// BAD
Connection conn = dataSource.getConnection();
try {
    PreparedStatement ps = conn.prepareStatement(sql);
    try {
        ResultSet rs = ps.executeQuery();
        // ...
    } finally {
        ps.close();
    }
} finally {
    conn.close();
}

// GOOD
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {
    // ...
}
```

**Why:** Manual `finally` blocks are error-prone, especially with early returns or multiple
resources. try-with-resources is guaranteed correct and far more readable.

---

### Rule 5: Document Checked Exceptions

Every method that throws a checked exception must have a `@throws` Javadoc tag.

```java
/**
 * Loads configuration from the given path.
 *
 * @param path absolute path to the configuration file
 * @return parsed configuration object
 * @throws FileNotFoundException  if the file does not exist
 * @throws ConfigException        if the file is malformed
 */
public Config load(String path) throws FileNotFoundException, ConfigException {
    // ...
}
```

**Why:** Checked exceptions are part of the method's contract. Callers cannot write correct
error-handling code without knowing which exceptions to expect.

---

### Rule 6: Don't Use Exceptions for Control Flow

Exceptions are for exceptional conditions, not for branching logic.

```java
// BAD
try {
    String value = map.get(key);
    process(value);
} catch (NullPointerException e) {
    // key not present — that's normal
    log.debug("key {} not found, using default", key);
    process(defaultValue);
}

// GOOD
String value = map.getOrDefault(key, defaultValue);
process(value);
```

**Why:** Exception handling is orders of magnitude slower than a conditional check. More
importantly, it obscures intent and makes debugging harder — stack traces are generated for
expected behavior.

---

### Rule 7: Include Context in Messages

Exception messages must be meaningful enough to diagnose without reading the code.

```java
// BAD
throw new ValidationException("Invalid input");

// GOOD
throw new ValidationException(
    String.format("Invalid input for field '%s': expected %s, got '%s'",
        field, expectedType, actualValue));
```

**Why:** When an exception appears in a log at 3 AM, the message is the first thing you
see. "Invalid input" tells you nothing. A descriptive message tells you exactly what went
wrong.

---

### Rule 8: Avoid Catching Exception/Throwable

The only acceptable broad catches are at infrastructure boundaries.

```java
// Acceptable: top-level handler for an HTTP request framework
try {
    servlet.service(req, resp);
} catch (Throwable t) {
    log.error("Unhandled error processing {}", req.getRequestURI(), t);
    resp.setStatus(500);
    resp.getWriter().write("Internal Server Error");
}

// BAD: business logic catching Throwable
try {
    orderService.placeOrder(order);
} catch (Throwable t) {
    log.error("something went wrong", t);
}
```

**Why:** Broad catches in business logic hide programming errors. Infrastructure boundaries
are the only place where you need a safety net to prevent process-wide damage.

---

---
**Continue:** [Part 2](README-Part2.md)
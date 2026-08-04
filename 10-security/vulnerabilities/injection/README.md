# Injection Vulnerabilities

## Overview

Injection flaws occur when untrusted data is sent to an interpreter as part of a command or query.

## SQL Injection

### Vulnerable Code
```java
// NEVER DO THIS
String query = "SELECT * FROM users WHERE username = '" + username + "'";
Statement stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery(query);
```

### Prevention
```java
// Use parameterized queries
@Query("SELECT u FROM User u WHERE u.username = :username")
User findByUsername(@Param("username") String username);

// Or use PreparedStatement
String query = "SELECT * FROM users WHERE username = ?";
PreparedStatement stmt = connection.prepareStatement(query);
stmt.setString(1, username);
ResultSet rs = stmt.executeQuery();
```

## NoSQL Injection

### MongoDB
```java
// Vulnerable
Document query = Document.parse("{username: '" + username + "'}");

// Safe
Document query = new Document("username", username);
```

## Command Injection

### Vulnerable Code
```java
// NEVER DO THIS
Runtime.getRuntime().exec("ping " + userInput);
```

### Prevention
```java
// Use ProcessBuilder with array
ProcessBuilder pb = new ProcessBuilder("ping", "-c", "1", validatedInput);
Process process = pb.start();

// Validate input
private String validateInput(String input) {
    if (!input.matches("^[a-zA-Z0-9.]+$")) {
        throw new ValidationException("Invalid input");
    }
    return input;
}
```

## LDAP Injection

```java
// Vulnerable
String filter = "(&(uid=" + userId + ")(userPassword=" + password + "))";

// Safe
String filter = "(&(uid={0})(userPassword={1}))";
ldapTemplate.search("", filter, new Object[]{userId, password}, ...);
```

## Best Practices

1. Use parameterized queries always
2. Validate and sanitize input
3. Use ORM frameworks
4. Apply principle of least privilege
5. Escape special characters
6. Use stored procedures
7. Implement input validation
8. Monitor for injection attempts

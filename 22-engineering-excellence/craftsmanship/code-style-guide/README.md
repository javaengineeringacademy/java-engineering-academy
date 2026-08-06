# Code Style Guide

Comprehensive guide to code formatting, indentation, line length, imports, whitespace, and other style conventions.

---

## Table of Contents

1. [Overview](#overview)
2. [Why Style Matters](#why-style-matters)
3. [Formatting Rules](#formatting-rules)
4. [Indentation](#indentation)
5. [Line Length](#line-length)
6. [Imports](#imports)
7. [Whitespace](#whitespace)
8. [Braces and Brackets](#braces-and-brackets)
9. [Comments](#comments)
10. [Language-Specific Styles](#language-specific-styles)
11. [Best Practices](#best-practices)
12. [Common Mistakes](#common-mistakes)
13. [Key Takeaways](#key-takeaways)

---

## Overview

A code style guide defines how code should be formatted and organized. It ensures consistency, readability, and maintainability across a codebase.

### What Style Guides Cover

- **Formatting**: Indentation, spacing, line breaks
- **Structure**: File organization, class structure
- **Comments**: Documentation and inline comments
- **Naming**: Variable and function naming (see Naming Conventions)
- **Imports**: How to organize imports

---

## Why Style Matters

### Benefits

**Readability**
- Consistent format reduces cognitive load
- Code reads like prose
- Easier to understand for new team members
- Faster code reviews

**Maintainability**
- Easier to find and fix bugs
- Simpler to add new features
- Reduced technical debt
- Better code quality

**Collaboration**
- Team members can work on any code
- Reduced merge conflicts
- Consistent expectations
- Faster onboarding

**Tool Support**
- IDEs can auto-format
- Linters can enforce rules
- Code reviews focus on logic
- Automated quality checks

### Real-World Impact

**Without Style Guide**
```java
public class user_manager{
private string name;
public void getname(){return name;}
public void setname(string n){name=n;}
}
```

**With Style Guide**
```java
public class UserManager {
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
```

---

## Formatting Rules

### Consistent Formatting

**Key Principle**: Format code consistently throughout the project.

**Rules**
1. Use the same indentation everywhere
2. Use consistent spacing around operators
3. Use consistent line breaks
4. Use consistent brace placement

### Auto-Formatting

**IDE Settings**
- Configure IDE to format on save
- Use project-specific settings
- Share settings with team
- Use consistent formatter across team

**Build Tools**
- Maven/Gradle plugins
- Pre-commit hooks
- CI/CD checks
- Automated formatting

---

## Indentation

### Conventions

**Spaces vs. Tabs**
- Use spaces, not tabs
- 4 spaces (Java, JavaScript, C#)
- 2 spaces (Python, YAML)
- Consistent throughout project

**Continuation Lines**
- Align with opening delimiter
- Use 8 spaces for continuation
- Break at logical points

### Examples

**Java (4 spaces)**
```java
public class UserService {
    private final UserRepository userRepository;
    
    public User createUser(String email, String password) {
        validateInput(email, password);
        User user = new User(email, password);
        return userRepository.save(user);
    }
    
    private void validateInput(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
    }
}
```

**Python (4 spaces)**
```python
class UserService:
    def __init__(self, user_repository):
        self.user_repository = user_repository
    
    def create_user(self, email, password):
        self.validate_input(email, password)
        user = User(email, password)
        return self.user_repository.save(user)
    
    def validate_input(self, email, password):
        if not email or not email.strip():
            raise ValueError("Email is required")
```

**Continuation Lines**
```java
// Good: Aligned with opening delimiter
User user = userRepository.findByEmailAndStatus(
        email,
        Status.ACTIVE);

// Good: Broken at logical point
if (userRepository.existsByEmail(email)
        && userRepository.findByEmail(email).isActive()) {
    // ...
}
```

---

## Line Length

### Conventions

**Maximum Line Length**
- 80 characters (traditional)
- 100 characters (modern)
- 120 characters (many teams)
- Consistent throughout project

**Breaking Long Lines**
- Break at logical points
- Align with opening delimiter
- Use continuation indent
- Keep related code together

### Examples

**Long Line**
```java
// Bad: Too long
if (userRepository.findByEmail(email).getStatus() == Status.ACTIVE && userRepository.findByEmail(email).hasPermission(Permission.ADMIN)) {
    // ...
}
```

**Broken Line**
```java
// Good: Broken at logical point
if (userRepository.findByEmail(email).getStatus() == Status.ACTIVE
        && userRepository.findByEmail(email).hasPermission(Permission.ADMIN)) {
    // ...
}
```

**Method Parameters**
```java
// Bad: Too long
public User createUser(String email, String password, String firstName, String lastName, String phoneNumber, Address address) {
    // ...
}

// Good: Broken into multiple lines
public User createUser(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        Address address) {
    // ...
}
```

**Chained Methods**
```java
// Bad: Too long
List<User> activeUsers = userRepository.findByStatus(Status.ACTIVE).stream().filter(user -> user.getEmail().contains("@example.com")).collect(Collectors.toList());

// Good: Broken into multiple lines
List<User> activeUsers = userRepository.findByStatus(Status.ACTIVE)
        .stream()
        .filter(user -> user.getEmail().contains("@example.com"))
        .collect(Collectors.toList());
```

---

## Imports

### Conventions

**Import Order**
1. Standard library imports
2. Third-party library imports
3. Project imports
4. Alphabetical order within groups

**Import Style**
- Use specific imports, not wildcards
- One import per line
- Group related imports
- Remove unused imports

### Examples

**Java Imports**
```java
// Standard library
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// Third-party
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Project
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
```

**Python Imports**
```python
# Standard library
import os
import sys
from datetime import datetime
from typing import List, Optional

# Third-party
import requests
from flask import Flask, request

# Project
from my_project.models import User
from my_project.repositories import UserRepository
```

**Import Organization**
```java
// Bad: Wildcard import
import java.util.*;

// Good: Specific imports
import java.util.List;
import java.util.Map;
import java.util.HashMap;
```

---

## Whitespace

### Conventions

**Around Operators**
```java
// Good: Space around operators
int result = a + b;
boolean isActive = (status == ACTIVE);
double percentage = (value / total) * 100;

// Bad: No spaces
int result=a+b;
boolean isActive=(status==ACTIVE);
double percentage=(value/total)*100;
```

**Around Keywords**
```java
// Good: Space after keywords
if (condition) {
    // ...
}

for (int i = 0; i < count; i++) {
    // ...
}

while (condition) {
    // ...
}

// Bad: No space after keyword
if(condition){
    // ...
}
```

**Around Braces**
```java
// Good: Space before opening brace
public class UserService {
    // ...
}

public void method() {
    // ...
}

// Bad: No space before brace
public class UserService{
    // ...
}
```

**Around Parameters**
```java
// Good: No space inside parentheses
public void method(int a, int b) {
    // ...
}

// Bad: Space inside parentheses
public void method( int a, int b ) {
    // ...
}
```

**Blank Lines**
```java
// Good: One blank line between methods
public class UserService {
    
    private final UserRepository userRepository;
    
    public User createUser(String email) {
        // Method body
    }
    
    // One blank line between methods
    public void deleteUser(String email) {
        // Method body
    }
    
    // Two blank lines between logical sections
    private void validateEmail(String email) {
        // Method body
    }
}
```

---

## Braces and Brackets

### Conventions

**Brace Style**
- K&R style (opening brace on same line)
- Allman style (opening brace on new line)
- Consistent throughout project

**Empty Blocks**
- Use empty braces for empty blocks
- Or add comment to indicate empty block

### Examples

**K&R Style (Java, JavaScript)**
```java
public class UserService {
    public void method() {
        if (condition) {
            // ...
        } else {
            // ...
        }
        
        for (int i = 0; i < count; i++) {
            // ...
        }
    }
}
```

**Allman Style (C#)**
```csharp
public class UserService
{
    public void Method()
    {
        if (condition)
        {
            // ...
        }
        else
        {
            // ...
        }
    }
}
```

**Empty Blocks**
```java
// Good: Empty braces
public void doNothing() {
}

// Or with comment
public void doNothing() {
    // Intentionally empty
}
```

---

## Comments

### Conventions

**Comment Styles**
- Javadoc/Docstrings for documentation
- Block comments for complex logic
- Line comments for brief explanations
- TODO/FIXME for future work

**Comment Rules**
- Explain why, not what
- Keep comments updated
- Remove outdated comments
- Use meaningful comments

### Examples

**Documentation Comments**
```java
/**
 * Processes customer orders and handles payment transactions.
 * 
 * <p>This service validates orders, calculates totals, and processes
 * payments through the configured payment gateway.</p>
 * 
 * @author John Doe
 * @version 1.0
 * @since 1.0
 */
public class OrderService {
    
    /**
     * Processes an order and returns a confirmation.
     * 
     * @param order the order to process
     * @return the order confirmation
     * @throws IllegalArgumentException if order is invalid
     */
    public OrderConfirmation processOrder(Order order) {
        // Implementation
    }
}
```

**Block Comments**
```java
public void complexMethod() {
    // This section handles the complex business logic
    // for calculating discounts based on customer type,
    // order history, and current promotions.
    double discount = calculateDiscount(customer, order);
    
    // Apply the discount to the order total
    double finalAmount = order.getTotal() - discount;
}
```

**Inline Comments**
```java
public void method() {
    int count = 0; // Initialize counter
    
    // Process each item
    for (Item item : items) {
        count++; // Increment for each item
    }
}
```

**TODO Comments**
```java
// TODO: Implement caching for better performance
public User getUser(String email) {
    return userRepository.findByEmail(email);
}

// FIXME: This is a temporary workaround
public void workaround() {
    // Temporary solution
}
```

---

## Language-Specific Styles

### Java Style

**Class Structure**
```java
package com.example.project;

import java.util.List;

import com.example.project.model.User;

/**
 * Service for managing users.
 */
public class UserService {
    
    // Constants
    private static final int MAX_RETRY_COUNT = 3;
    
    // Fields
    private final UserRepository userRepository;
    
    // Constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Public methods
    public User createUser(String email) {
        // Implementation
    }
    
    // Private methods
    private void validateEmail(String email) {
        // Implementation
    }
}
```

### Python Style

**PEP 8**
```python
"""Module docstring."""

import os
from typing import List, Optional

# Constants
MAX_RETRY_COUNT = 3
DEFAULT_CURRENCY = "USD"


class UserService:
    """Service for managing users."""
    
    def __init__(self, user_repository):
        """Initialize the service."""
        self.user_repository = user_repository
    
    def create_user(self, email: str) -> User:
        """Create a new user."""
        self.validate_email(email)
        user = User(email)
        return self.user_repository.save(user)
    
    def validate_email(self, email: str) -> None:
        """Validate email format."""
        if not email or not email.strip():
            raise ValueError("Email is required")
```

### JavaScript Style

**ES6+**
```javascript
/**
 * Service for managing users.
 */
class UserService {
    /**
     * Create a new user.
     * @param {string} email - User email
     * @returns {User} Created user
     */
    createUser(email) {
        this.validateEmail(email);
        const user = new User(email);
        return this.userRepository.save(user);
    }
    
    /**
     * Validate email format.
     * @param {string} email - Email to validate
     */
    validateEmail(email) {
        if (!email || !email.trim()) {
            throw new Error('Email is required');
        }
    }
}

export default UserService;
```

---

## Best Practices

### Getting Started

1. **Choose a style guide**: Use existing guides (Google, Oracle, etc.)
2. **Customize as needed**: Adapt to team preferences
3. **Document decisions**: Record why choices were made
4. **Get team buy-in**: Everyone must agree
5. **Automate enforcement**: Use tools to ensure consistency

### Maintaining Style

1. **Use auto-formatting**: Configure IDE to format on save
2. **Run linters**: Check for violations automatically
3. **Review in code reviews**: Catch style issues
4. **Update documentation**: Keep style guide current
5. **Train new members**: Teach the style guide

### Tools

1. **IDE settings**: Configure formatting rules
2. **Linters**: Check for violations
3. **Formatters**: Auto-format code
4. **Pre-commit hooks**: Prevent non-compliant code
5. **CI/CD checks**: Enforce in pipeline

---

## Common Mistakes

### Formatting Mistakes

1. **Inconsistent indentation**: Mixing tabs and spaces
2. **Inconsistent spacing**: Some places have spaces, others don't
3. **Inconsistent braces**: Different brace styles in same file
4. **Inconsistent line breaks**: Some files have blank lines, others don't
5. **Inconsistent imports**: Different import styles

### Style Mistakes

1. **Not following language conventions**: Using wrong case
2. **Not using auto-formatting**: Relying on manual formatting
3. **Not reviewing style**: In code reviews
4. **Not updating style guide**: When conventions change
5. **Not training new members**: Not teaching the style guide

### Tool Mistakes

1. **Not using linters**: Missing violations
2. **Not configuring formatters**: Inconsistent formatting
3. **Not automating checks**: Relying on manual checks
4. **Not integrating with CI/CD**: Missing pipeline checks
5. **Not sharing settings**: Team uses different configurations

---

## Key Takeaways

1. **Style matters**: Consistent formatting improves readability
2. **Choose a guide**: Use existing style guides
3. **Automate enforcement**: Use tools to ensure consistency
4. **Get team buy-in**: Everyone must agree
5. **Review regularly**: Keep style guide current
6. **Train new members**: Teach the style guide
7. **Use auto-formatting**: Configure IDE to format on save
8. **Be consistent**: Follow the same style throughout

---

## Additional Resources

- [Naming Conventions](../naming-conventions/README.md) - Naming guidelines
- [Coding Standards](../coding-standards/README.md) - Overall standards
- [Clean Code](../clean-code/README.md) - Writing quality code
- [Engineering Principles](../../../README.md) - Core principles
- [Books](../../../README.md) - Recommended reading

---

*Last Updated: August 2026*

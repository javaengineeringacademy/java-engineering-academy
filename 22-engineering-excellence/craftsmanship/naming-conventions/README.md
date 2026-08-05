# Naming Conventions

Comprehensive guide to naming variables, functions, classes, packages, constants, and files across programming languages.

---

## Table of Contents

1. [Overview](#overview)
2. [Why Naming Matters](#why-naming-matters)
3. [General Principles](#general-principles)
4. [Variable Naming](#variable-naming)
5. [Function Naming](#function-naming)
6. [Class Naming](#class-naming)
7. [Package Naming](#package-naming)
8. [Constant Naming](#constant-naming)
9. [File Naming](#file-naming)
10. [Language-Specific Conventions](#language-specific-conventions)
11. [Best Practices](#best-practices)
12. [Common Mistakes](#common-mistakes)
13. [Key Takeaways](#key-takeaways)

---

## Overview

Naming conventions are rules for choosing identifiers for variables, functions, classes, and other entities in code. They ensure consistency, readability, and maintainability across a codebase.

### What Naming Conventions Cover

- **Variables**: Data storage identifiers
- **Functions/Methods**: Action identifiers
- **Classes/Types**: Blueprint identifiers
- **Packages/Modules**: Organization identifiers
- **Constants**: Fixed value identifiers
- **Files**: Resource identifiers

---

## Why Naming Matters

### Impact on Code Quality

**Readability**
- Good names make code self-documenting
- Reduces need for comments
- Makes code easier to understand
- Improves code navigation

**Maintainability**
- Easier to find and fix bugs
- Simpler to add new features
- Reduced cognitive load
- Better code organization

**Collaboration**
- Team members understand each other's code
- Faster code reviews
- Easier onboarding
- Consistent expectations

### Real-World Example

**Bad Naming**
```java
// What does this do?
public int calc(int a, int b) {
    int c = a * b;
    int d = c + (c * 0.1);
    return d;
}
```

**Good Naming**
```java
// Clear what this does
public double calculateTotalWithTax(double price, double quantity) {
    double subtotal = price * quantity;
    double tax = subtotal * TAX_RATE;
    return subtotal + tax;
}
```

---

## General Principles

### 1. Be Descriptive

**Bad**
```java
int x = 10;
String s = "hello";
boolean b = true;
```

**Good**
```java
int itemCount = 10;
String userName = "hello";
boolean isActive = true;
```

### 2. Be Consistent

**Bad**
```java
int itemCount = 10;
int numItems = 20;
int totalItems = 30;
```

**Good**
```java
int itemCount = 10;
int itemCount = 20;
int totalItemCount = 30;
```

### 3. Avoid Abbreviations

**Bad**
```java
int cnt = 10;
String usrNm = "john";
boolean isActive = true;
```

**Good**
```java
int count = 10;
String userName = "john";
boolean isActive = true;
```

### 4. Use Pronounceable Names

**Bad**
```java
int mrkdwnPrsct = 85;
String usrLstRsp = "success";
```

**Good**
```java
int markdownPercentage = 85;
String userListResponse = "success";
```

### 5. Use Searchable Names

**Bad**
```java
// Magic number
if (status == 4) {
    // ...
}

// Hard to search
for (int i = 0; i < list.size(); i++) {
    // ...
}
```

**Good**
```java
// Named constant
if (status == STATUS_COMPLETED) {
    // ...
}

// Searchable
for (int index = 0; index < list.size(); index++) {
    // ...
}
```

---

## Variable Naming

### Conventions

**camelCase (Java, JavaScript, C#)**
```java
String firstName = "John";
int itemCount = 10;
boolean isActive = true;
double totalAmount = 99.99;
```

**snake_case (Python, Ruby, Go)**
```python
first_name = "John"
item_count = 10
is_active = True
total_amount = 99.99
```

**SCREAMING_SNAKE_CASE for constants**
```java
public static final int MAX_RETRY_COUNT = 3;
public static final String DEFAULT_CURRENCY = "USD";
```

### Variable Types

**Boolean Variables**
```java
// Good: Use is, has, can, should prefixes
boolean isActive = true;
boolean hasPermission = false;
boolean canEdit = true;
boolean shouldRetry = false;
```

**Collection Variables**
```java
// Good: Use plural nouns
List<User> users = new ArrayList<>();
Map<String, Integer> itemCounts = new HashMap<>();
Set<String> uniqueIds = new HashSet<>();
```

**Temporary Variables**
```java
// Good: Use descriptive names even for temporaries
double discountedPrice = calculateDiscount(originalPrice);
String formattedDate = formatDate(originalDate);
```

---

## Function Naming

### Conventions

**camelCase (Java, JavaScript, C#)**
```java
public void processOrder() { }
public String getUserName() { }
public boolean isValidEmail() { }
public int calculateTotal() { }
```

**snake_case (Python, Ruby, Go)**
```python
def process_order():
    pass

def get_user_name():
    pass
```

**Verb First**
```java
// Good: Start with verb
public void createUser() { }
public String getEmail() { }
public boolean hasAccess() { }
public int calculateTotal() { }
```

### Function Prefixes

**Getters and Setters**
```java
// Getters
public String getName() { }
public int getAge() { }

// Setters
public void setName(String name) { }
public void setAge(int age) { }
```

**Boolean Functions**
```java
// Use is, has, can, should prefixes
public boolean isActive() { }
public boolean hasPermission() { }
public boolean canEdit() { }
public boolean shouldRetry() { }
```

**Conversion Functions**
```java
// Use toX or asX prefixes
public String toString() { }
public int toInt() { }
public Date toDate() { }
public User asUser() { }
```

---

## Class Naming

### Conventions

**PascalCase (Java, JavaScript, C#)**
```java
public class UserService { }
public class OrderController { }
public class PaymentGateway { }
public class UserAuthentication { }
```

**snake_case (Python, Ruby)**
```python
class UserService:
    pass

class OrderController:
    pass
```

### Class Name Patterns

**Nouns or Noun Phrases**
```java
// Good: Classes are things
public class User { }
public class Order { }
public class Payment { }
public class Configuration { }
```

**Interfaces**
```java
// Good: Interfaces are capabilities
public interface Serializable { }
public interface Comparable<T> { }
public interface Repository<T> { }
public interface Service<T> { }
```

**Abstract Classes**
```java
// Good: Abstract classes describe partial implementations
public abstract class BaseRepository<T> { }
public abstract class AbstractController { }
public abstract class TemplateMethod { }
```

**Enums**
```java
// Good: Enums are categories
public enum Status { ACTIVE, INACTIVE, PENDING }
public enum Color { RED, GREEN, BLUE }
public enum Priority { LOW, MEDIUM, HIGH }
```

---

## Package Naming

### Conventions

**Lowercase with dots (Java)**
```java
package com.example.project.module;
package org.apache.commons.lang;
package java.util;
```

**Lowercase with underscores (Python)**
```python
package my_project.module
package utils.helpers
```

### Package Structure

**Reverse Domain Name**
```java
com.company.project.module
org.organization.project.module
```

**Layer-Based**
```java
com.example.project.controller
com.example.project.service
com.example.project.repository
com.example.project.model
```

**Feature-Based**
```java
com.example.project.user
com.example.project.order
com.example.project.payment
```

---

## Constant Naming

### Conventions

**SCREAMING_SNAKE_CASE**
```java
public static final int MAX_RETRY_COUNT = 3;
public static final String DEFAULT_CURRENCY = "USD";
public static final double TAX_RATE = 0.08;
public static final Duration TIMEOUT = Duration.ofSeconds(30);
```

### Constant Categories

**Configuration Constants**
```java
public static final int MAX_CONNECTIONS = 100;
public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mydb";
public static final Duration CACHE_TTL = Duration.ofMinutes(5);
```

**Business Constants**
```java
public static final double TAX_RATE = 0.08;
public static final int MIN_PASSWORD_LENGTH = 8;
public static final String DEFAULT_LANGUAGE = "en";
```

**Magic Numbers**
```java
// Bad: Magic number
if (status == 4) {
    // ...
}

// Good: Named constant
public static final int STATUS_COMPLETED = 4;
if (status == STATUS_COMPLETED) {
    // ...
}
```

---

## File Naming

### Conventions

**Java**
```
UserService.java
OrderController.java
PaymentGatewayTest.java
application.properties
```

**Python**
```
user_service.py
order_controller.py
test_payment_gateway.py
config.yaml
```

**JavaScript**
```
UserService.js
OrderController.js
PaymentGateway.test.js
config.json
```

### File Name Patterns

**Source Files**
```
[ClassName].java
[module_name].py
[component_name].js
```

**Test Files**
```
[ClassName]Test.java
test_[module_name].py
[component_name].test.js
```

**Configuration Files**
```
application.properties
config.yaml
.env
settings.json
```

---

## Language-Specific Conventions

### Java

**Variables and Methods**
```java
// camelCase
String firstName;
int itemCount;
public void processOrder() { }
public String getUserName() { }
```

**Classes and Interfaces**
```java
// PascalCase
public class UserService { }
public interface Repository { }
```

**Constants**
```java
// SCREAMING_SNAKE_CASE
public static final int MAX_RETRY_COUNT = 3;
```

**Packages**
```java
// lowercase with dots
com.example.project.module;
```

### Python

**Variables and Functions**
```python
# snake_case
first_name = "John"
def process_order():
    pass
```

**Classes**
```python
# PascalCase
class UserService:
    pass
```

**Constants**
```python
# SCREAMING_SNAKE_CASE
MAX_RETRY_COUNT = 3
DEFAULT_CURRENCY = "USD"
```

**Packages and Modules**
```python
# lowercase with underscores
my_project.module
utils.helpers
```

### JavaScript

**Variables and Functions**
```javascript
// camelCase
let firstName = "John";
function processOrder() { }
const getUserName = () => { };
```

**Classes**
```javascript
// PascalCase
class UserService { }
```

**Constants**
```javascript
// SCREAMING_SNAKE_CASE or camelCase
const MAX_RETRY_COUNT = 3;
const DEFAULT_CURRENCY = "USD";
```

**Files**
```javascript
// camelCase or kebab-case
userService.js
order-controller.js
```

### C#

**Variables and Methods**
```csharp
// camelCase for local variables and parameters
string firstName = "John";
int itemCount = 10;

// PascalCase for methods and properties
public void ProcessOrder() { }
public string GetUserName() { }
```

**Classes and Interfaces**
```csharp
// PascalCase
public class UserService { }
public interface IRepository { }
```

**Constants**
```csharp
// PascalCase (C# convention)
public const int MaxRetryCount = 3;
public const string DefaultCurrency = "USD";
```

---

## Best Practices

### Do's

1. **Be descriptive**: Names should explain purpose
2. **Be consistent**: Use same pattern throughout
3. **Use pronounceable names**: Easy to say and remember
4. **Avoid abbreviations**: Unless commonly understood
5. **Use searchable names**: Easy to find in codebase

### Don'ts

1. **Don't use single letters**: Except for loop counters
2. **Don't use numbers**: user1, user2, etc.
3. **Don't use Hungarian notation**: Not needed in modern languages
4. **Don't use ambiguous names**: data, info, thing
5. **Don't use misleading names**: Don't confuse readers

### Code Review Checklist

- [ ] Names are descriptive and clear
- [ ] Consistent naming conventions used
- [ ] No abbreviations unless common
- [ ] Boolean variables have clear prefixes
- [ ] Constants are properly named
- [ ] File names follow conventions
- [ ] Package names follow conventions

---

## Common Mistakes

### Naming Mistakes

1. **Single letter names**: x, y, z (except loops)
2. **Abbreviations**: cnt, usr, msg
3. **Hungarian notation**: strName, intCount
4. **Misleading names**: data, info, thing
5. **Inconsistent naming**: Mixing conventions

### Convention Mistakes

1. **Not following language conventions**: Using wrong case
2. **Inconsistent within project**: Different styles in different files
3. **Not updating names**: When purpose changes
4. **Ignoring team agreements**: Not following agreed standards
5. **Not using tools**: Not leveraging IDE support

### Communication Mistakes

1. **Not documenting naming decisions**: Why certain names were chosen
2. **Not sharing conventions**: Team doesn't know the rules
3. **Not reviewing names**: In code reviews
4. **Not training new members**: Not teaching conventions
5. **Not updating documentation**: When conventions change

---

## Key Takeaways

1. **Naming matters**: Good names make code readable
2. **Be descriptive**: Names should explain purpose
3. **Be consistent**: Use same pattern throughout
4. **Follow language conventions**: Use standard case rules
5. **Avoid abbreviations**: Unless commonly understood
6. **Use searchable names**: Easy to find in codebase
7. **Review names**: In code reviews
8. **Update names**: When purpose changes

---

## Additional Resources

- [Code Style Guide](../code-style-guide/README.md) - Formatting guidelines
- [Coding Standards](../coding-standards/README.md) - Overall standards
- [Clean Code](../clean-code/README.md) - Writing quality code
- [Engineering Principles](../engineering-principles/README.md) - Core principles
- [Books](../books/README.md) - Recommended reading

---

*Last Updated: August 2026*

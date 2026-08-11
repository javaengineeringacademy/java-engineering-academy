# Solutions: The throw Keyword

## Solution 1: Basic throw
```java
static int divide(int a, int b) {
    if (b == 0) {
        throw new ArithmeticException("Division by zero");
    }
    return a / b;
}
```

---

## Solution 2: Parameter Validation
```java
static void setEmail(String email) {
    if (email == null) {
        throw new NullPointerException("Email cannot be null");
    }
    if (!email.contains("@")) {
        throw new IllegalArgumentException("Invalid email: missing @ symbol");
    }
    System.out.println("Email set: " + email);
}
```

---

## Solution 3: Exception Chaining
```java
static class ConfigException extends RuntimeException {
    ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}

static Config readConfig(String path) {
    try {
        return readFile(path);
    } catch (IOException e) {
        throw new ConfigException("Failed to read config from: " + path, e);
    }
}
```

---

## Solution 4: Rethrow After Logging
```java
static void processPayment(double amount) throws PaymentException {
    if (amount <= 0) {
        throw new PaymentException("Amount must be positive: " + amount);
    }
    try {
        // simulate payment processing
        chargePayment(amount);
    } catch (Exception e) {
        System.err.println("Payment processing failed: " + e.getMessage());
        throw e; // rethrow original
    }
}
```

---

## Solution 5: Multiple Validation Rules
```java
static void createProduct(String name, double price, int stock) {
    if (name == null) {
        throw new NullPointerException("Product name cannot be null");
    }
    if (name.isEmpty()) {
        throw new IllegalArgumentException("Product name cannot be empty");
    }
    if (price < 0) {
        throw new IllegalArgumentException("Price cannot be negative: " + price);
    }
    if (stock < 0) {
        throw new IllegalArgumentException("Stock cannot be negative: " + stock);
    }
    System.out.println("Product created: " + name);
}
```

---

## Solution 6: Defensive Builder
```java
static class Response {
    private final int statusCode;
    private final String body;

    private Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    static class Builder {
        private Integer statusCode;
        private String body;

        Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        Builder body(String body) {
            this.body = body;
            return this;
        }

        Response build() {
            if (statusCode == null) {
                throw new IllegalStateException("Status code is required");
            }
            if (body == null) {
                throw new IllegalStateException("Body is required");
            }
            if (statusCode < 100 || statusCode > 599) {
                throw new IllegalArgumentException("Invalid status code: " + statusCode);
            }
            return new Response(statusCode, body);
        }
    }
}
```

---

## Solution 7: Rethrow as Different Type
```java
static int parseInteger(String s) throws ParseException {
    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        throw new ParseException("Cannot parse as integer: " + s, 0);
    }
}
```

---

## Solution 8: Exception Hierarchy
```java
static class ServiceException extends RuntimeException {
    ServiceException(String message) { super(message); }
    ServiceException(String message, Throwable cause) { super(message, cause); }
}

static class ValidationException extends ServiceException {
    ValidationException(String message) { super(message); }
}

static class NotFoundException extends ServiceException {
    NotFoundException(String message) { super(message); }
}

static Object findById(int id) {
    if (id == 0) {
        throw new ValidationException("ID cannot be zero");
    }
    if (id < 0) {
        throw new NotFoundException("Not found for negative ID: " + id);
    }
    return new Object();
}
```

---

## Solution 9: Throw in Lambda
```java
static List<String> filterOrThrow(List<String> items, Predicate<String> predicate) {
    List<String> result = items.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    if (result.isEmpty()) {
        throw new IllegalArgumentException("No items matched the predicate");
    }
    return result;
}
```

---

## Solution 10: Multi-Cause Exception
```java
static void attemptBoth() {
    Exception first = null;
    Exception second = null;

    try {
        operationOne();
    } catch (Exception e) {
        first = e;
    }

    try {
        operationTwo();
    } catch (Exception e) {
        second = e;
    }

    if (first != null && second != null) {
        first.addSuppressed(second);
        throw new RuntimeException("Both operations failed", first);
    } else if (first != null) {
        throw new RuntimeException("Operation one failed", first);
    } else if (second != null) {
        throw new RuntimeException("Operation two failed", second);
    }
    // Both succeeded — return normally
}
```

---

## Instructions
- Compare your solutions to these implementations
- Focus on the exception chaining pattern in Solutions 3 and 10
- Verify that exception messages match the expected format

# Immutable Objects

## Introduction

Immutable objects are objects whose state cannot be modified after creation, providing thread safety, simplicity, and reliability by ensuring that once an object is constructed, its internal state remains constant throughout its lifetime. This fundamental concept in object-oriented programming is essential for creating robust, concurrent applications because immutable objects are inherently thread-safe without requiring synchronization, can be freely shared between threads without race conditions, and simplify code by eliminating concerns about state changes. The Java Language Specification recommends making classes immutable when possible, and many core Java classes (String, Integer, LocalDate) are immutable. Understanding how to design and implement immutable objects is crucial for writing correct, efficient, and maintainable Java code, especially in multi-threaded environments where shared mutable state is a common source of bugs.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the principles and benefits of immutable objects
- [ ] Implement immutable classes following the Java specification guidelines
- [ ] Recognize the trade-offs between immutable and mutable objects
- [ ] Apply immutable objects to create thread-safe, reliable applications

## Prerequisites

- [02-classes](../02-classes/README.md) - Class structure, fields, and constructors
- [03-objects](../03-objects/README.md) - Object creation and memory allocation
- [05-methods](../05-methods/README.md) - Method design and return values
- [08-encapsulation](../08-encapsulation/README.md) - Data hiding and access control

## Why This Concept Exists

### The Problem

Mutable objects create several challenges:

1. **Thread safety issues**: Shared mutable state requires synchronization
2. **Unpredictable behavior**: Objects can change unexpectedly
3. **Complex debugging**: Hard to track when and where state changes occur
4. **Defensive copying**: Need to create copies to prevent unwanted modifications

```java
// Problem: Mutable objects in concurrent environment
class BankAccount {
    private double balance;

    public void deposit(double amount) {
        balance += amount; // Race condition in multi-threaded environment
    }

    public void withdraw(double amount) {
        balance -= amount; // Race condition
    }
}
// Multiple threads accessing this object can cause data corruption
```

### The Solution

Immutable objects solve these problems by:

- Eliminating state changes after construction
- Providing inherent thread safety
- Enabling safe sharing between threads
- Simplifying debugging and reasoning about code

### Real-World Analogy

Think of immutable objects as **historical events**. Once a historical event occurs, it cannot be changed. We can:
- Create new records about the event (new objects)
- Analyze the event from different perspectives (methods that return new objects)
- Share information about the event with others (safe sharing)
- But we cannot change what actually happened (immutable state)

This is different from mutable objects like a bank account where the balance can change over time.

## Internal Working

### JVM Perspective

Immutable objects have special characteristics in the JVM:

1. **String Pool**: Immutable strings can be safely shared and interned
2. **Class Loading**: Immutable classes can be cached and reused
3. **Optimization**: JVM can optimize immutable object operations
4. **Memory**: Immutable objects can be shared without copying

### Memory Representation

```
Immutable Object in Memory:

String Object (Immutable):
┌─────────────────────────────────┐
│ Object Header                   │
├─────────────────────────────────┤
│ final char[] value              │  ← Cannot be modified
│ final int hash                  │  ← Computed once
└─────────────────────────────────┘

When "modified":
String s1 = "Hello";
String s2 = s1.concat(" World"); // Creates NEW String object

s1: ┌─────────────┐     s2: ┌─────────────────┐
    │ "Hello"     │         │ "Hello World"   │
    └─────────────┘         └─────────────────┘
    (Unchanged)             (New object)
```

### Immutability Guarantees

```
Immutability Rules:

1. Class is declared final
   → Cannot be extended

2. All fields are final
   → Cannot be modified after construction

3. No setter methods
   → No way to change state

4. No methods that modify internal state
   → All operations return new objects

5. No references to mutable objects
   → Prevents external modification
```

## Syntax

### Basic Immutable Class

```java
public final class ImmutablePoint {
    private final int x;
    private final int y;

    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ImmutablePoint translate(int dx, int dy) {
        return new ImmutablePoint(x + dx, y + dy); // Returns new object
    }
}
```

### Immutable Class with Mutable Components

```java
import java.util.Collections;
import java.util.List;

public final class ImmutableRecord {
    private final String id;
    private final List<String> tags;

    public ImmutableRecord(String id, List<String> tags) {
        this.id = id;
        // Defensive copy of mutable component
        this.tags = Collections.unmodifiableList(new ArrayList<>(tags));
    }

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return tags; // Already unmodifiable
    }
}
```

### Builder Pattern for Immutable Objects

```java
public final class ImmutableConfig {
    private final String host;
    private final int port;
    private final boolean ssl;

    private ImmutableConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.ssl = builder.ssl;
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public boolean isSsl() { return ssl; }

    public static class Builder {
        private String host;
        private int port = 80;
        private boolean ssl = false;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public ImmutableConfig build() {
            return new ImmutableConfig(this);
        }
    }
}

// Usage
ImmutableConfig config = new ImmutableConfig.Builder()
    .host("example.com")
    .port(443)
    .ssl(true)
    .build();
```

## Easy Examples

### Example 1: Immutable Color Class

**Problem Statement**: Create an immutable Color class that demonstrates basic immutability principles and shows how immutable objects can be safely shared and used.

**Implementation**:

```java
package academy.javaengineering.oop.immutable;

import java.util.Objects;

public final class Color {
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;
    private final String name;
    private final int hash;

    // Constructor
    public Color(int red, int green, int blue, int alpha, String name) {
        // Validate inputs
        this.red = validateComponent(red);
        this.green = validateComponent(green);
        this.blue = validateComponent(blue);
        this.alpha = validateComponent(alpha);
        this.name = name != null ? name : "Unnamed";
        this.hash = computeHash(); // Compute once
    }

    public Color(int red, int green, int blue, String name) {
        this(red, green, blue, 255, name);
    }

    public Color(int red, int green, int blue) {
        this(red, green, blue, 255, "RGB(" + red + "," + green + "," + blue + ")");
    }

    private int validateComponent(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private int computeHash() {
        return Objects.hash(red, green, blue, alpha, name);
    }

    // Getters only - no setters
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
    public int getAlpha() { return alpha; }
    public String getName() { return name; }

    // Methods that return new objects
    public Color withRed(int newRed) {
        return new Color(newRed, green, blue, alpha, name);
    }

    public Color withGreen(int newGreen) {
        return new Color(red, newGreen, blue, alpha, name);
    }

    public Color withBlue(int newBlue) {
        return new Color(red, green, newBlue, alpha, name);
    }

    public Color withAlpha(int newAlpha) {
        return new Color(red, green, blue, newAlpha, name);
    }

    public Color brighter() {
        int factor = 50;
        return new Color(
            Math.min(255, red + factor),
            Math.min(255, green + factor),
            Math.min(255, blue + factor),
            alpha,
            name
        );
    }

    public Color darker() {
        int factor = 50;
        return new Color(
            Math.max(0, red - factor),
            Math.max(0, green - factor),
            Math.max(0, blue - factor),
            alpha,
            name
        );
    }

    public Color blend(Color other, double ratio) {
        int newRed = (int) (red * (1 - ratio) + other.red * ratio);
        int newGreen = (int) (green * (1 - ratio) + other.green * ratio);
        int newBlue = (int) (blue * (1 - ratio) + other.blue * ratio);
        return new Color(newRed, newGreen, newBlue, alpha, "Blended");
    }

    // Convert to different formats
    public String toHex() {
        return String.format("#%02X%02X%02X%02X", alpha, red, green, blue);
    }

    public String toRgbString() {
        return String.format("rgb(%d, %d, %d)", red, green, blue);
    }

    public String toRgbaString() {
        return String.format("rgba(%d, %d, %d, %.2f)", red, green, blue, alpha / 255.0);
    }

    // Override Object methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Color other = (Color) obj;
        return red == other.red &&
               green == other.green &&
               blue == other.blue &&
               alpha == other.alpha;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return String.format("Color{name='%s', rgba=(%d,%d,%d,%d), hex='%s'}",
            name, red, green, blue, alpha, toHex());
    }

    // Static factory methods
    public static Color fromHex(String hex) {
        if (hex == null || !hex.matches("#[0-9A-Fa-f]{6,8}")) {
            throw new IllegalArgumentException("Invalid hex color: " + hex);
        }

        hex = hex.substring(1); // Remove #
        int alpha = hex.length() == 8 ? Integer.parseInt(hex.substring(0, 2), 16) : 255;
        int red = Integer.parseInt(hex.substring(hex.length() - 6, hex.length() - 4), 16);
        int green = Integer.parseInt(hex.substring(hex.length() - 4, hex.length() - 2), 16);
        int blue = Integer.parseInt(hex.substring(hex.length() - 2), 16);

        return new Color(red, green, blue, alpha, "Hex(" + hex + ")");
    }

    public static final Color RED = new Color(255, 0, 0, "Red");
    public static final Color GREEN = new Color(0, 128, 0, "Green");
    public static final Color BLUE = new Color(0, 0, 255, "Blue");
    public static final Color WHITE = new Color(255, 255, 255, "White");
    public static final Color BLACK = new Color(0, 0, 0, "Black");
}

// Demo class
class ColorDemo {
    public static void main(String[] args) {
        System.out.println("=== Immutable Color Demo ===\n");

        // Create colors
        Color red = new Color(255, 0, 0, "Red");
        Color blue = new Color(0, 0, 255, "Blue");

        System.out.println("Original colors:");
        System.out.println("  " + red);
        System.out.println("  " + blue);

        // "Modify" color (creates new object)
        Color brighterRed = red.brighter();
        System.out.println("\nAfter brightening:");
        System.out.println("  Original: " + red); // Unchanged
        System.out.println("  Brighter: " + brighterRed); // New object

        // Blend colors
        Color purple = red.blend(blue, 0.5);
        System.out.println("\nBlended colors:");
        System.out.println("  " + purple);

        // Immutable sharing
        Color sharedColor = new Color(128, 128, 128, "Gray");
        Color ref1 = sharedColor;
        Color ref2 = sharedColor;
        System.out.println("\nImmutable sharing:");
        System.out.println("  ref1 == ref2: " + (ref1 == ref2));
        System.out.println("  Both reference same object safely");

        // Using static factory methods
        Color fromHex = Color.fromHex("#FF5733");
        System.out.println("\nFrom hex: " + fromHex);
    }
}
```

**Expected Output**:
```
=== Immutable Color Demo ===

Original colors:
  Color{name='Red', rgba=(255,0,0,255), hex='#FF0000FF'}
  Color{name='Blue', rgba=(0,0,255,255), hex='#0000FFFF'}

After brightening:
  Original: Color{name='Red', rgba=(255,0,0,255), hex='#FF0000FF'}
  Brighter: Color{name='Red', rgba=(255,50,50,255), hex='#FF3232FF'}

Blended colors:
  Color{name='Blended', rgba=(127,0,127,255), hex='#FF7F00FF'}

Immutable sharing:
  ref1 == ref2: true
  Both reference same object safely

From hex: Color{name='Hex(FF5733)', rgba=(255,87,51,255), hex='#FFFF5733'}
```

**Best Practices**:
- Declare the class as final
- Make all fields final
- Provide only getters, no setters
- Return new objects for "modification" methods
- Perform defensive copying of mutable components

### Example 2: Immutable Money Class

**Problem Statement**: Create an immutable Money class that demonstrates immutability with mathematical operations, showing how immutable objects can safely perform calculations without side effects.

**Implementation**:

```java
package academy.javaengineering.oop.immutable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;
    private final int hash;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        this.amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
        this.currency = currency;
        this.hash = Objects.hash(amount, currency);
    }

    public Money(double amount, String currencyCode) {
        this(BigDecimal.valueOf(amount), Currency.getInstance(currencyCode));
    }

    public Money(String amount, String currencyCode) {
        this(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    // Getters
    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    // Arithmetic operations that return new Money objects
    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies: " +
                currency + " and " + other.currency);
        }
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract different currencies: " +
                currency + " and " + other.currency);
        }
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(double multiplier) {
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    public Money divide(double divisor, RoundingMode roundingMode) {
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new Money(amount.divide(BigDecimal.valueOf(divisor),
            currency.getDefaultFractionDigits(), roundingMode), currency);
    }

    public Money negate() {
        return new Money(amount.negate(), currency);
    }

    public Money abs() {
        return new Money(amount.abs(), currency);
    }

    public Money round(int scale, RoundingMode roundingMode) {
        return new Money(amount.setScale(scale, roundingMode), currency);
    }

    // Comparison operations
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isGreaterThan(Money other) {
        checkSameCurrency(other);
        return amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        checkSameCurrency(other);
        return amount.compareTo(other.amount) < 0;
    }

    private void checkSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare different currencies");
        }
    }

    // Utility methods
    public Money withCurrency(Currency newCurrency) {
        // This is a conversion placeholder - real conversion would need rates
        return new Money(amount, newCurrency);
    }

    public String format() {
        return currency.getSymbol() + amount.toString();
    }

    public String format(java.util.Locale locale) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(locale);
        formatter.setCurrency(currency);
        return formatter.format(amount);
    }

    // Override Object methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money other = (Money) obj;
        return amount.compareTo(other.amount) == 0 &&
               currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return format();
    }

    // Static factory methods
    public static Money of(double amount, String currencyCode) {
        return new Money(amount, currencyCode);
    }

    public static Money usd(double amount) {
        return new Money(amount, "USD");
    }

    public static Money eur(double amount) {
        return new Money(amount, "EUR");
    }

    public static Money zero(String currencyCode) {
        return new Money(0, currencyCode);
    }
}

// Demo class
class MoneyDemo {
    public static void main(String[] args) {
        System.out.println("=== Immutable Money Demo ===\n");

        // Create money objects
        Money price1 = Money.usd(19.99);
        Money price2 = Money.usd(29.99);
        Money tax = Money.usd(4.50);

        System.out.println("Original values:");
        System.out.println("  Price 1: " + price1);
        System.out.println("  Price 2: " + price2);
        System.out.println("  Tax: " + tax);

        // Arithmetic operations (return new objects)
        Money subtotal = price1.add(price2);
        Money total = subtotal.add(tax);
        System.out.println("\nCalculations:");
        System.out.println("  Subtotal: " + subtotal);
        System.out.println("  Total: " + total);

        // "Modify" operations
        Money discount = total.multiply(0.1);
        Money finalTotal = total.subtract(discount);
        System.out.println("\nWith discount:");
        System.out.println("  Discount (10%): " + discount);
        System.out.println("  Final total: " + finalTotal);
        System.out.println("  Original total unchanged: " + total);

        // Comparison
        System.out.println("\nComparisons:");
        System.out.println("  price1 > price2: " + price1.isGreaterThan(price2));
        System.out.println("  total is positive: " + total.isPositive());
        System.out.println("  total is zero: " + total.isZero());

        // Immutable sharing
        Money shared = Money.usd(100);
        Money ref1 = shared;
        Money ref2 = shared;
        System.out.println("\nImmutable sharing:");
        System.out.println("  ref1 == ref2: " + (ref1 == ref2));
        System.out.println("  Both reference same object safely");

        // Different currencies
        Money euros = Money.eur(25.00);
        System.out.println("\nDifferent currencies:");
        System.out.println("  USD: " + price1);
        System.out.println("  EUR: " + euros);

        // Rounding
        Money precise = new Money("123.456", "USD");
        Money rounded = precise.round(2, RoundingMode.HALF_UP);
        System.out.println("\nRounding:");
        System.out.println("  Original: " + precise);
        System.out.println("  Rounded: " + rounded);
    }
}
```

**Expected Output**:
```
=== Immutable Money Demo ===

Original values:
  Price 1: $19.99
  Price 2: $29.99
  Tax: $4.50

Calculations:
  Subtotal: $49.98
  Total: $54.48

With discount:
  Discount (10%): $5.448
  Final total: $49.032
  Original total unchanged: $54.48

Comparisons:
  price1 > price2: false
  total is positive: true
  total is zero: false

Immutable sharing:
  ref1 == ref2: true
  Both reference same object safely

Different currencies:
  USD: $19.99
  EUR: €25.00

Rounding:
  Original: $123.456
  Rounded: $123.46
```

**Best Practices**:
- Use BigDecimal for precise monetary calculations
- Validate inputs in constructor
- Return new objects for all operations
- Provide static factory methods for common cases
- Implement proper equals() and hashCode()

## Medium Examples

### Example 1: Immutable Configuration System

**Problem Statement**: Design an immutable configuration system that demonstrates builder pattern, defensive copying, and immutable collections for storing application configuration.

**Requirements**:

- Support complex configuration structures
- Use builder pattern for construction
- Handle mutable components defensively
- Support configuration merging and inheritance

**Implementation**:

```java
package academy.javaengineering.oop.immutable;

import java.util.*;

public final class Configuration {
    private final String applicationName;
    private final String version;
    private final Environment environment;
    private final Map<String, String> properties;
    private final Map<String, DatabaseConfig> databases;
    private final CacheConfig cacheConfig;
    private final SecurityConfig securityConfig;
    private final int hash;

    private Configuration(Builder builder) {
        this.applicationName = builder.applicationName;
        this.version = builder.version;
        this.environment = builder.environment;

        // Defensive copies of mutable collections
        this.properties = Collections.unmodifiableMap(new HashMap<>(builder.properties));
        this.databases = Collections.unmodifiableMap(new HashMap<>(builder.databases));
        this.cacheConfig = builder.cacheConfig;
        this.securityConfig = builder.securityConfig;

        this.hash = computeHash();
    }

    private int computeHash() {
        return Objects.hash(applicationName, version, environment,
            properties, databases, cacheConfig, securityConfig);
    }

    // Getters
    public String getApplicationName() { return applicationName; }
    public String getVersion() { return version; }
    public Environment getEnvironment() { return environment; }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    public Map<String, String> getProperties() {
        return properties; // Already unmodifiable
    }

    public DatabaseConfig getDatabase(String name) {
        return databases.get(name);
    }

    public Map<String, DatabaseConfig> getDatabases() {
        return databases; // Already unmodifiable
    }

    public CacheConfig getCacheConfig() { return cacheConfig; }
    public SecurityConfig getSecurityConfig() { return securityConfig; }

    // Methods that return new Configuration objects
    public Configuration withProperty(String key, String value) {
        Builder builder = toBuilder();
        builder.properties.put(key, value);
        return builder.build();
    }

    public Configuration withProperties(Map<String, String> newProperties) {
        Builder builder = toBuilder();
        builder.properties.putAll(newProperties);
        return builder.build();
    }

    public Configuration withEnvironment(Environment environment) {
        Builder builder = toBuilder();
        builder.environment = environment;
        return builder.build();
    }

    public Configuration merge(Configuration other) {
        Builder builder = toBuilder();
        builder.properties.putAll(other.properties);
        builder.databases.putAll(other.databases);
        return builder.build();
    }

    // Convert to builder for modifications
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.applicationName = this.applicationName;
        builder.version = this.version;
        builder.environment = this.environment;
        builder.properties.putAll(this.properties);
        builder.databases.putAll(this.databases);
        builder.cacheConfig = this.cacheConfig;
        builder.securityConfig = this.securityConfig;
        return builder;
    }

    // Override Object methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Configuration other = (Configuration) obj;
        return Objects.equals(applicationName, other.applicationName) &&
               Objects.equals(version, other.version) &&
               environment == other.environment &&
               properties.equals(other.properties) &&
               databases.equals(other.databases) &&
               Objects.equals(cacheConfig, other.cacheConfig) &&
               Objects.equals(securityConfig, other.securityConfig);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return String.format("Configuration{app='%s', v='%s', env=%s, props=%d}",
            applicationName, version, environment, properties.size());
    }

    // Nested immutable classes
    public enum Environment {
        DEVELOPMENT, TESTING, STAGING, PRODUCTION
    }

    public static final class DatabaseConfig {
        private final String url;
        private final String username;
        private final int maxConnections;
        private final boolean sslEnabled;

        public DatabaseConfig(String url, String username, int maxConnections, boolean sslEnabled) {
            this.url = url;
            this.username = username;
            this.maxConnections = maxConnections;
            this.sslEnabled = sslEnabled;
        }

        public String getUrl() { return url; }
        public String getUsername() { return username; }
        public int getMaxConnections() { return maxConnections; }
        public boolean isSslEnabled() { return sslEnabled; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            DatabaseConfig other = (DatabaseConfig) obj;
            return maxConnections == other.maxConnections &&
                   sslEnabled == other.sslEnabled &&
                   Objects.equals(url, other.url) &&
                   Objects.equals(username, other.username);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, username, maxConnections, sslEnabled);
        }
    }

    public static final class CacheConfig {
        private final int maxSize;
        private final long ttlSeconds;
        private final boolean enabled;

        public CacheConfig(int maxSize, long ttlSeconds, boolean enabled) {
            this.maxSize = maxSize;
            this.ttlSeconds = ttlSeconds;
            this.enabled = enabled;
        }

        public int getMaxSize() { return maxSize; }
        public long getTtlSeconds() { return ttlSeconds; }
        public boolean isEnabled() { return enabled; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CacheConfig other = (CacheConfig) obj;
            return maxSize == other.maxSize &&
                   ttlSeconds == other.ttlSeconds &&
                   enabled == other.enabled;
        }

        @Override
        public int hashCode() {
            return Objects.hash(maxSize, ttlSeconds, enabled);
        }
    }

    public static final class SecurityConfig {
        private final boolean authenticationRequired;
        private final List<String> allowedOrigins;
        private final int tokenExpirationHours;

        public SecurityConfig(boolean authenticationRequired,
                             List<String> allowedOrigins,
                             int tokenExpirationHours) {
            this.authenticationRequired = authenticationRequired;
            this.allowedOrigins = Collections.unmodifiableList(new ArrayList<>(allowedOrigins));
            this.tokenExpirationHours = tokenExpirationHours;
        }

        public boolean isAuthenticationRequired() { return authenticationRequired; }
        public List<String> getAllowedOrigins() { return allowedOrigins; }
        public int getTokenExpirationHours() { return tokenExpirationHours; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            SecurityConfig other = (SecurityConfig) obj;
            return authenticationRequired == other.authenticationRequired &&
                   tokenExpirationHours == other.tokenExpirationHours &&
                   allowedOrigins.equals(other.allowedOrigins);
        }

        @Override
        public int hashCode() {
            return Objects.hash(authenticationRequired, allowedOrigins, tokenExpirationHours);
        }
    }

    // Builder class
    public static class Builder {
        private String applicationName;
        private String version;
        private Environment environment = Environment.DEVELOPMENT;
        private final Map<String, String> properties = new HashMap<>();
        private final Map<String, DatabaseConfig> databases = new HashMap<>();
        private CacheConfig cacheConfig = new CacheConfig(1000, 3600, true);
        private SecurityConfig securityConfig = new SecurityConfig(false, Arrays.asList("*"), 24);

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder property(String key, String value) {
            this.properties.put(key, value);
            return this;
        }

        public Builder properties(Map<String, String> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder database(String name, DatabaseConfig config) {
            this.databases.put(name, config);
            return this;
        }

        public Builder cacheConfig(CacheConfig cacheConfig) {
            this.cacheConfig = cacheConfig;
            return this;
        }

        public Builder securityConfig(SecurityConfig securityConfig) {
            this.securityConfig = securityConfig;
            return this;
        }

        public Configuration build() {
            validate();
            return new Configuration(this);
        }

        private void validate() {
            if (applicationName == null || applicationName.isEmpty()) {
                throw new IllegalStateException("Application name is required");
            }
            if (version == null || version.isEmpty()) {
                throw new IllegalStateException("Version is required");
            }
        }
    }

    // Static factory method
    public static Builder builder() {
        return new Builder();
    }
}

// Demo class
class ConfigurationDemo {
    public static void main(String[] args) {
        System.out.println("=== Immutable Configuration Demo ===\n");

        // Build configuration using builder
        Configuration config = Configuration.builder()
            .applicationName("MyApp")
            .version("1.0.0")
            .environment(Configuration.Environment.PRODUCTION)
            .property("debug", "false")
            .property("log.level", "INFO")
            .database("primary", new Configuration.DatabaseConfig(
                "jdbc:mysql://localhost:3306/mydb",
                "admin",
                10,
                true
            ))
            .cacheConfig(new Configuration.CacheConfig(5000, 1800, true))
            .securityConfig(new Configuration.SecurityConfig(
                true,
                Arrays.asList("https://example.com", "https://app.example.com"),
                48
            ))
            .build();

        System.out.println("Original configuration:");
        System.out.println("  " + config);
        System.out.println("  Debug: " + config.getProperty("debug"));
        System.out.println("  Database: " + config.getDatabase("primary").getUrl());

        // "Modify" configuration (creates new object)
        Configuration devConfig = config.toBuilder()
            .environment(Configuration.Environment.DEVELOPMENT)
            .property("debug", "true")
            .property("log.level", "DEBUG")
            .build();

        System.out.println("\nDevelopment configuration:");
        System.out.println("  " + devConfig);
        System.out.println("  Debug: " + devConfig.getProperty("debug"));

        // Original unchanged
        System.out.println("\nOriginal still production:");
        System.out.println("  Debug: " + config.getProperty("debug"));

        // Merge configurations
        Map<String, String> overrides = new HashMap<>();
        overrides.put("timeout", "30");
        overrides.put("max.retries", "5");

        Configuration merged = config.withProperties(overrides);
        System.out.println("\nMerged configuration:");
        System.out.println("  Timeout: " + merged.getProperty("timeout"));
        System.out.println("  Max Retries: " + merged.getProperty("max.retries"));

        // Immutable sharing
        System.out.println("\nImmutable sharing:");
        Configuration ref1 = config;
        Configuration ref2 = config;
        System.out.println("  ref1 == ref2: " + (ref1 == ref2));
        System.out.println("  Both reference same object safely");
    }
}
```

**Expected Output**:
```
=== Immutable Configuration Demo ===

Original configuration:
  Configuration{app='MyApp', v='1.0.0', env=PRODUCTION, props=2}
  Debug: false
  Database: jdbc:mysql://localhost:3306/mydb

Development configuration:
  Configuration{app='MyApp', v='1.0.0', env=DEVELOPMENT, props=4}
  Debug: true

Original still production:
  Debug: false

Merged configuration:
  Timeout: 30
  Max Retries: 5

Immutable sharing:
  ref1 == ref2: true
  Both reference same object safely
```

**Code Walkthrough**:

1. **Final Class**: Class is declared final to prevent subclassing
2. **Final Fields**: All fields are final and assigned in constructor
3. **Defensive Copies**: Collections are copied and wrapped in unmodifiable views
4. **Builder Pattern**: Complex object construction with validation
5. **Immutable "Modification"**: Methods return new objects instead of modifying state

## Hard Examples

### Example 1: Immutable Event Sourcing System

**Problem Statement**: Design an event sourcing system using immutable events and state objects, demonstrating how immutability enables reliable state reconstruction and audit trails.

**Requirements**:

- Immutable event objects
- Immutable state objects
- Event replay for state reconstruction
- Complete audit trail
- Thread-safe event processing

**Architecture**:

```
Event Sourcing Architecture
├── Event (Immutable)
│   ├── EventMetadata
│   └── EventData
├── State (Immutable)
│   ├── AggregateState
│   └── StateSnapshot
├── EventStore
│   ├── Store Events
│   ├── Retrieve Events
│   └── Replay Events
└── Aggregate
    ├── Apply Events
    └── Reconstruct State
```

**Implementation**:

```java
package academy.javaengineering.oop.immutable;

import java.time.Instant;
import java.util.*;

// Immutable Event class
public final class Event {
    private final String eventId;
    private final String aggregateId;
    private final String eventType;
    private final Map<String, Object> data;
    private final EventMetadata metadata;
    private final int hash;

    public Event(String aggregateId, String eventType, Map<String, Object> data) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.data = Collections.unmodifiableMap(new HashMap<>(data));
        this.metadata = new EventMetadata(Instant.now(), "system");
        this.hash = computeHash();
    }

    private Event(Builder builder) {
        this.eventId = builder.eventId;
        this.aggregateId = builder.aggregateId;
        this.eventType = builder.eventType;
        this.data = Collections.unmodifiableMap(new HashMap<>(builder.data));
        this.metadata = builder.metadata;
        this.hash = computeHash();
    }

    private int computeHash() {
        return Objects.hash(eventId, aggregateId, eventType, data, metadata);
    }

    // Getters
    public String getEventId() { return eventId; }
    public String getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public Map<String, Object> getData() { return data; }
    public EventMetadata getMetadata() { return metadata; }

    public <T> T getData(String key, Class<T> type) {
        Object value = data.get(key);
        return value != null ? type.cast(value) : null;
    }

    // Override Object methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event other = (Event) obj;
        return Objects.equals(eventId, other.eventId);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return String.format("Event{id='%s', type='%s', aggregate='%s'}",
            eventId, eventType, aggregateId);
    }

    // Builder
    public static class Builder {
        private String eventId = UUID.randomUUID().toString();
        private String aggregateId;
        private String eventType;
        private final Map<String, Object> data = new HashMap<>();
        private EventMetadata metadata = new EventMetadata(Instant.now(), "system");

        public Builder aggregateId(String aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder data(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public Builder data(Map<String, Object> data) {
            this.data.putAll(data);
            return this;
        }

        public Builder metadata(EventMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

// Immutable Event Metadata
public final class EventMetadata {
    private final Instant timestamp;
    private final String userId;
    private final Map<String, String> properties;

    public EventMetadata(Instant timestamp, String userId) {
        this(timestamp, userId, Collections.emptyMap());
    }

    public EventMetadata(Instant timestamp, String userId, Map<String, String> properties) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.properties = Collections.unmodifiableMap(new HashMap<>(properties));
    }

    public Instant getTimestamp() { return timestamp; }
    public String getUserId() { return userId; }
    public Map<String, String> getProperties() { return properties; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EventMetadata other = (EventMetadata) obj;
        return Objects.equals(timestamp, other.timestamp) &&
               Objects.equals(userId, other.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, userId);
    }
}

// Immutable State class
public final class AccountState {
    private final String accountId;
    private final String owner;
    private final BigDecimal balance;
    private final AccountStatus status;
    private final List<String> transactionHistory;
    private final Instant lastUpdated;
    private final int version;
    private final int hash;

    public AccountState(String accountId, String owner) {
        this.accountId = accountId;
        this.owner = owner;
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
        this.transactionHistory = Collections.emptyList();
        this.lastUpdated = Instant.now();
        this.version = 0;
        this.hash = computeHash();
    }

    private AccountState(Builder builder) {
        this.accountId = builder.accountId;
        this.owner = builder.owner;
        this.balance = builder.balance;
        this.status = builder.status;
        this.transactionHistory = Collections.unmodifiableList(new ArrayList<>(builder.transactionHistory));
        this.lastUpdated = builder.lastUpdated;
        this.version = builder.version;
        this.hash = computeHash();
    }

    private int computeHash() {
        return Objects.hash(accountId, owner, balance, status, version);
    }

    // Getters
    public String getAccountId() { return accountId; }
    public String getOwner() { return owner; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public List<String> getTransactionHistory() { return transactionHistory; }
    public Instant getLastUpdated() { return lastUpdated; }
    public int getVersion() { return version; }

    // State transition methods (return new state)
    public AccountState deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot deposit to inactive account");
        }

        Builder builder = toBuilder();
        builder.balance = this.balance.add(amount);
        builder.version = this.version + 1;
        builder.lastUpdated = Instant.now();
        builder.transactionHistory.add("DEPOSIT:" + amount);
        return builder.build();
    }

    public AccountState withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot withdraw from inactive account");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        Builder builder = toBuilder();
        builder.balance = this.balance.subtract(amount);
        builder.version = this.version + 1;
        builder.lastUpdated = Instant.now();
        builder.transactionHistory.add("WITHDRAWAL:" + amount);
        return builder.build();
    }

    public AccountState close() {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Account already closed");
        }

        Builder builder = toBuilder();
        builder.status = AccountStatus.CLOSED;
        builder.version = this.version + 1;
        builder.lastUpdated = Instant.now();
        builder.transactionHistory.add("CLOSED");
        return builder.build();
    }

    // Convert to builder
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.accountId = this.accountId;
        builder.owner = this.owner;
        builder.balance = this.balance;
        builder.status = this.status;
        builder.transactionHistory.addAll(this.transactionHistory);
        builder.lastUpdated = this.lastUpdated;
        builder.version = this.version;
        return builder;
    }

    // Override Object methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountState other = (AccountState) obj;
        return version == other.version &&
               Objects.equals(accountId, other.accountId) &&
               Objects.equals(owner, other.owner) &&
               balance.compareTo(other.balance) == 0 &&
               status == other.status;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return String.format("AccountState{id='%s', owner='%s', balance=%s, status=%s, v=%d}",
            accountId, owner, balance, status, version);
    }

    public enum AccountStatus {
        ACTIVE, SUSPENDED, CLOSED
    }

    // Builder
    public static class Builder {
        private String accountId;
        private String owner;
        private BigDecimal balance = BigDecimal.ZERO;
        private AccountStatus status = AccountStatus.ACTIVE;
        private final List<String> transactionHistory = new ArrayList<>();
        private Instant lastUpdated = Instant.now();
        private int version = 0;

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder status(AccountStatus status) {
            this.status = status;
            return this;
        }

        public Builder transactionHistory(List<String> history) {
            this.transactionHistory.addAll(history);
            return this;
        }

        public Builder version(int version) {
            this.version = version;
            return this;
        }

        public AccountState build() {
            return new AccountState(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

// Event Store
class EventStore {
    private final List<Event> events;
    private final Map<String, List<Event>> aggregateEvents;

    public EventStore() {
        this.events = new ArrayList<>();
        this.aggregateEvents = new HashMap<>();
    }

    public void append(Event event) {
        events.add(event);
        aggregateEvents.computeIfAbsent(event.getAggregateId(), k -> new ArrayList<>()).add(event);
    }

    public List<Event> getEvents(String aggregateId) {
        return aggregateEvents.getOrDefault(aggregateId, Collections.emptyList());
    }

    public List<Event> getAllEvents() {
        return Collections.unmodifiableList(events);
    }

    public List<Event> getEventsByType(String eventType) {
        return events.stream()
            .filter(e -> e.getEventType().equals(eventType))
            .toList();
    }
}

// Aggregate that reconstructs state from events
class AccountAggregate {
    private final EventStore eventStore;

    public AccountAggregate(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public AccountState getState(String accountId) {
        List<Event> events = eventStore.getEvents(accountId);
        AccountState state = null;

        for (Event event : events) {
            state = applyEvent(state, event);
        }

        return state;
    }

    private AccountState applyEvent(AccountState currentState, Event event) {
        if (currentState == null) {
            // Initial state creation
            return new AccountState(event.getAggregateId(), event.getData("owner", String.class));
        }

        return switch (event.getEventType()) {
            case "DEPOSIT" -> currentState.deposit(event.getData("amount", BigDecimal.class));
            case "WITHDRAWAL" -> currentState.withdraw(event.getData("amount", BigDecimal.class));
            case "CLOSED" -> currentState.close();
            default -> currentState;
        };
    }

    public void createAccount(String accountId, String owner) {
        Event event = Event.builder()
            .aggregateId(accountId)
            .eventType("CREATED")
            .data("owner", owner)
            .build();
        eventStore.append(event);
    }

    public void deposit(String accountId, BigDecimal amount) {
        Event event = Event.builder()
            .aggregateId(accountId)
            .eventType("DEPOSIT")
            .data("amount", amount)
            .build();
        eventStore.append(event);
    }

    public void withdraw(String accountId, BigDecimal amount) {
        Event event = Event.builder()
            .aggregateId(accountId)
            .eventType("WITHDRAWAL")
            .data("amount", amount)
            .build();
        eventStore.append(event);
    }

    public void closeAccount(String accountId) {
        Event event = Event.builder()
            .aggregateId(accountId)
            .eventType("CLOSED")
            .build();
        eventStore.append(event);
    }
}

// Demo class
class EventSourcingDemo {
    public static void main(String[] args) {
        System.out.println("=== Immutable Event Sourcing Demo ===\n");

        EventStore eventStore = new EventStore();
        AccountAggregate account = new AccountAggregate(eventStore);

        // Create account
        String accountId = "ACC-001";
        account.createAccount(accountId, "Alice Johnson");

        // Perform transactions
        account.deposit(accountId, new BigDecimal("1000.00"));
        account.deposit(accountId, new BigDecimal("500.00"));
        account.withdraw(accountId, new BigDecimal("200.00"));

        // Get current state
        AccountState currentState = account.getState(accountId);
        System.out.println("Current State:");
        System.out.println("  " + currentState);

        // Show event history
        System.out.println("\nEvent History:");
        List<Event> events = eventStore.getEvents(accountId);
        for (Event event : events) {
            System.out.println("  " + event);
        }

        // Show transaction history
        System.out.println("\nTransaction History:");
        for (String transaction : currentState.getTransactionHistory()) {
            System.out.println("  " + transaction);
        }

        // Reconstruct state from events (event sourcing)
        System.out.println("\n=== State Reconstruction ===");
        AccountState reconstructed = account.getState(accountId);
        System.out.println("Reconstructed State: " + reconstructed);
        System.out.println("States equal: " + currentState.equals(reconstructed));

        // Immutable sharing
        System.out.println("\n=== Immutable Sharing ===");
        AccountState ref1 = currentState;
        AccountState ref2 = currentState;
        System.out.println("ref1 == ref2: " + (ref1 == ref2));
        System.out.println("Both reference same object safely");

        // Close account
        System.out.println("\n=== Closing Account ===");
        account.closeAccount(accountId);
        AccountState closedState = account.getState(accountId);
        System.out.println("Closed State: " + closedState);
    }
}
```

**Execution Flow**:

1. **Event Creation**: Events are created as immutable objects
2. **Event Storage**: Events are stored in the event store
3. **State Reconstruction**: State is reconstructed by replaying events
4. **State Transitions**: Each operation creates a new state object
5. **Audit Trail**: Complete history is maintained through events

**Unit Tests**:

```java
public class ImmutableEventTest {
    public static void main(String[] args) {
        System.out.println("=== Running Immutable Event Tests ===\n");

        testEventCreation();
        testStateTransitions();
        testEventReplay();
        testImmutableSharing();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testEventCreation() {
        System.out.println("Test 1: Event Creation");
        Event event = Event.builder()
            .aggregateId("agg-1")
            .eventType("TEST")
            .data("key", "value")
            .build();

        assert event.getAggregateId().equals("agg-1") : "Aggregate ID incorrect";
        assert event.getEventType().equals("TEST") : "Event type incorrect";
        assert event.getData("key", String.class).equals("value") : "Data incorrect";

        System.out.println("  PASS: Event creation test passed\n");
    }

    private static void testStateTransitions() {
        System.out.println("Test 2: State Transitions");
        AccountState state = new AccountState("ACC-001", "Alice");

        AccountState afterDeposit = state.deposit(new BigDecimal("100"));
        assert afterDeposit.getBalance().compareTo(new BigDecimal("100")) == 0 : "Balance incorrect";
        assert afterDeposit.getVersion() == 1 : "Version incorrect";

        AccountState afterWithdraw = afterDeposit.withdraw(new BigDecimal("50"));
        assert afterWithdraw.getBalance().compareTo(new BigDecimal("50")) == 0 : "Balance incorrect";

        System.out.println("  PASS: State transitions test passed\n");
    }

    private static void testEventReplay() {
        System.out.println("Test 3: Event Replay");
        EventStore store = new EventStore();
        AccountAggregate account = new AccountAggregate(store);

        account.createAccount("ACC-001", "Alice");
        account.deposit("ACC-001", new BigDecimal("100"));

        AccountState state = account.getState("ACC-001");
        assert state.getBalance().compareTo(new BigDecimal("100")) == 0 : "Replay failed";

        System.out.println("  PASS: Event replay test passed\n");
    }

    private static void testImmutableSharing() {
        System.out.println("Test 4: Immutable Sharing");
        AccountState state = new AccountState("ACC-001", "Alice");
        AccountState ref1 = state;
        AccountState ref2 = state;

        assert ref1 == ref2 : "Should be same object";
        assert ref1.equals(ref2) : "Should be equal";

        System.out.println("  PASS: Immutable sharing test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(n) for state reconstruction where n is number of events
- **Space Complexity**: O(n) for storing events

**Best Practices**:

- Make event and state classes immutable
- Use builder pattern for complex object creation
- Store events for complete audit trail
- Reconstruct state by replaying events
- Validate state transitions in state classes

## Exercises

### Easy

1. **Immutable Point**: Create an immutable Point class with x, y coordinates and methods that return new points.

2. **Immutable Date Range**: Design an immutable DateRange class with start and end dates and methods for comparison and manipulation.

3. **Immutable Rectangle**: Create an immutable Rectangle class with width, height, and methods for area, perimeter, and transformations.

### Medium

1. **Immutable Matrix**: Design an immutable Matrix class with operations for addition, multiplication, and transposition.

2. **Immutable Configuration**: Create an immutable Configuration class with builder pattern for complex configurations.

3. **Immutable Money**: Design an immutable Money class with currency and operations for arithmetic and comparison.

### Hard

1. **Immutable Tree**: Create an immutable tree data structure with operations that return new trees.

2. **Immutable State Machine**: Design an immutable state machine where each transition creates a new state.

3. **Immutable Event Sourcing**: Build an event sourcing system with immutable events and state reconstruction.

## Interview Questions

### Easy

1. **What is an immutable object?**
   An immutable object is an object whose state cannot be modified after it is created. Once constructed, all its fields remain constant throughout its lifetime.

2. **Why are immutable objects thread-safe?**
   Immutable objects are thread-safe because their state cannot change. Multiple threads can read the same object without synchronization because there are no writes to worry about.

3. **What is the benefit of immutable objects?**
   Benefits include: thread safety, simplicity, security, ability to share freely, no defensive copying needed, and predictable behavior.

### Medium

1. **How do you create an immutable class in Java?**
   Steps include: declare class final, make all fields final, no setters, defensive copy of mutable components, no methods that modify state, and compute hash code once.

2. **How do you handle mutable components in immutable objects?**
   Create defensive copies in constructor and getter methods. Use Collections.unmodifiableList/Map for collections. Never expose internal mutable objects.

3. **What is the performance impact of immutable objects?**
   Immutable objects may create more garbage due to new objects for each modification. However, they can be optimized by JVM (string pooling) and are often faster in concurrent scenarios due to no synchronization.

### Hard

1. **How do you implement complex operations with immutable objects?**
   Use builder pattern for construction, return new objects for modifications, and consider copy-on-write strategies. For complex transformations, use functional approaches with streams.

2. **What are the trade-offs between immutable and mutable objects?**
   Immutable objects provide safety and simplicity but may have performance overhead from object creation. Mutable objects are more efficient for frequent modifications but require careful synchronization.

## Common Pitfalls

### 1. Exposing Mutable Internal State

**Wrong**:
```java
public final class BadImmutable {
    private final List<String> items;

    public BadImmutable(List<String> items) {
        this.items = items; // Original list can be modified!
    }

    public List<String> getItems() {
        return items; // Caller can modify the list!
    }
}

// Problem
List<String> myList = new ArrayList<>();
myList.add("item1");
BadImmutable obj = new BadImmutable(myList);
myList.add("item2"); // Modifies internal state!
obj.getItems().add("item3"); // Also modifies internal state!
```

**Right**:
```java
public final class GoodImmutable {
    private final List<String> items;

    public GoodImmutable(List<String> items) {
        this.items = new ArrayList<>(items); // Defensive copy
    }

    public List<String> getItems() {
        return new ArrayList<>(items); // Return copy
        // Or use: return Collections.unmodifiableList(items);
    }
}
```

### 2. Not Making Class Final

**Wrong**:
```java
public class MutableChild extends ImmutableParent {
    // Can override methods to make mutable!
    @Override
    public String getValue() {
        return "modified"; // Breaks immutability
    }
}
```

**Right**:
```java
public final class ImmutableClass {
    // Cannot be extended
    private final String value;

    public ImmutableClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
```

### 3. Forgetting to Compute Hash Code

**Wrong**:
```java
public final class BadImmutable {
    private final int x;
    private final int y;

    public BadImmutable(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        // Computed every time - inefficient and inconsistent
        return Objects.hash(x, y);
    }
}
```

**Right**:
```java
public final class GoodImmutable {
    private final int x;
    private final int y;
    private final int hash; // Computed once

    public GoodImmutable(int x, int y) {
        this.x = x;
        this.y = y;
        this.hash = Objects.hash(x, y); // Compute in constructor
    }

    @Override
    public int hashCode() {
        return hash; // Return cached value
    }
}
```

## Best Practices

1. **Declare classes final**: Prevents subclassing that could break immutability.

2. **Make all fields final**: Ensures fields are assigned exactly once in constructor.

3. **No setter methods**: Prevents modification of state after construction.

4. **Defensive copying**: Create copies of mutable components in constructor and getters.

5. **Compute derived values once**: Cache hash codes and other computed values.

## Real World Usage

### How Spring Uses This

Spring Framework uses immutable objects for:

- **Configuration Properties**: Immutable configuration beans
- **Value Objects**: Immutable DTOs for data transfer
- **Bean Definitions**: Immutable metadata about beans

### How Hibernate Uses This

Hibernate ORM uses immutable objects for:

- **Entity Metadata**: Immutable descriptions of entity mappings
- **Query Plans**: Cached, immutable query execution plans
- **Cache Keys**: Immutable keys for second-level cache

### How JDK Uses This

The Java Development Kit uses immutable objects throughout:

- **String**: The most famous immutable class
- **Integer, Long, etc**: Immutable wrapper classes
- **LocalDate, LocalDateTime**: Immutable date/time classes
- **Collections.unmodifiable***: Unmodifiable collection views

### Enterprise Usage

In enterprise applications, immutable objects are used for:

- **Domain Events**: Immutable event objects for event sourcing
- **Configuration**: Immutable configuration objects
- **Value Objects**: Immutable domain primitives
- **DTOs**: Immutable data transfer objects

## References

1. **Effective Java** by Joshua Bloch - Item 17: Minimize mutability
2. **Java Concurrency in Practice** - Immutable objects for thread safety
3. **Domain-Driven Design** by Eric Evans - Value objects as immutable
4. **Functional Programming in Java** - Immutable collections and transformations
5. **Clean Code** by Robert C. Martin - Immutable data structures

## Summary

- Immutable objects cannot be modified after creation
- They provide inherent thread safety without synchronization
- Use final class, final fields, no setters, and defensive copying
- Builder pattern is useful for constructing complex immutable objects
- Immutable objects enable safe sharing and simplify reasoning about code
- Trade-off: may create more garbage but improve correctness and safety

**Next Steps**: [24-object-lifecycle](../24-object-lifecycle/README.md)

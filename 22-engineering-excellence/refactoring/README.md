# Refactoring

Improving code structure without changing behavior.

## Refactoring Techniques

### Extract Method
**When**: Method is too long or does too many things.

```java
// Before
public void printOwing() {
    // 10 lines of printing logic
}

// After
public void printOwing() {
    printBanner();
    printDetails();
}

private void printBanner() {
    // banner logic
}

private void printDetails() {
    // details logic
}
```

### Extract Class
**When**: Class does too many things.

```java
// Before
class Person {
    private String name;
    private String street;
    private String city;
    private String phoneNumber;
}

// After
class Person {
    private String name;
    private Address address;
    private PhoneNumber phoneNumber;
}
```

### Extract Interface
**When**: Multiple clients use only part of a class.

```java
// Before
class Ticket {
    public double getPrice() { }
    public String getVenue() { }
}

// After
interface Priceable {
    double getPrice();
}

class Ticket implements Priceable {
    public double getPrice() { }
    public String getVenue() { }
}
```

## Move Method/Field

### Move Method
**When**: Method is used more in another class.

```java
// Before
class Account {
    private AccountType type;
    
    double overdraftCharge() {
        return type.overdraftCharge();
    }
}

// After
class Account {
    private AccountType type;
}

class AccountType {
    double overdraftCharge() { }
}
```

### Move Field
**When**: Field is used more in another class.

```java
// Before
class Customer {
    private Address address;
}

// After
class Customer {
    private CustomerData data;
}

class CustomerData {
    private Address address;
}
```

## Replace Conditional with Polymorphism

### Before
```java
class Shape {
    double area() {
        switch (type) {
            case CIRCLE: return Math.PI * r * r;
            case RECTANGLE: return width * height;
            case TRIANGLE: return 0.5 * base * height;
        }
    }
}
```

### After
```java
interface Shape {
    double area();
}

class Circle implements Shape {
    public double area() { return Math.PI * r * r; }
}

class Rectangle implements Shape {
    public double area() { return width * height; }
}

class Triangle implements Shape {
    public double area() { return 0.5 * base * height; }
}
```

## Martin Fowler's Catalog

### Organizing Tools
- **Composing Methods**: Extract, Inline, Extract Method
- **Moving Features Between Objects**: Move Method, Move Field
- **Organizing Data**: Replace Type Code with Subclasses
- **Simplifying Conditional Expressions**: Consolidate Conditional
- **Simplifying Method Calls**: Rename Method, Introduce Parameter Object
- **Dealing with Generalization**: Extract Subclass, Extract Superclass

### Key Refactorings
1. **Extract Method**: Most common
2. **Inline Method**: When method body is clearer
3. **Move Method**: When method belongs elsewhere
4. **Replace Temp with Query**: Eliminate temporary variables
5. **Introduce Explaining Variable**: Name complex expressions
6. **Decompose Conditional**: Simplify if/else logic
7. **Replace Conditional with Polymorphism**: Use inheritance
8. **Extract Class**: When class has multiple responsibilities
9. **Inline Class**: When class does too little
10. **Hide Delegate**: Reduce coupling

## Refactoring Patterns

### Code Smell to Refactoring
| Code Smell | Refactoring |
|------------|-------------|
| Long Method | Extract Method |
| Large Class | Extract Class |
| Divergent Change | Extract Class |
| Shotgun Surgery | Move Method |
| Feature Envy | Move Method |
| Data Clumps | Extract Class |
| Primitive Obsession | Replace with Object |
| Switch Statements | Replace with Polymorphism |
| Parallel Inheritance | Move Method |
| Lazy Class | Inline Class |
| Speculative Generality | Remove Parameter |
| Temporary Field | Extract Class |
| Message Chains | Hide Delegate |
| Middle Man | Remove Middle Man |
| Alternate Classes | Merge Classes |
| Incomplete Library Class | Introduce Foreign Method |

## Safe Refactoring Practices

### Before Refactoring
1. **Ensure Tests Exist**: Have comprehensive tests
2. **Establish Baseline**: Measure current behavior
3. **Understand Code**: Know what it does

### During Refactoring
1. **Small Steps**: Make incremental changes
2. **Commit Often**: Save progress frequently
3. **Run Tests**: Verify behavior unchanged
4. **Pair Program**: Get second opinion

### After Refactoring
1. **Run Full Test Suite**: Ensure no regressions
2. **Review Changes**: Check for missed issues
3. **Document**: Update documentation
4. **Measure**: Compare before/after metrics

## Refactoring Tools

### IDE Support
- **IntelliJ IDEA**: Built-in refactoring
- **Eclipse**: Refactoring tools
- **VS Code**: Java refactorings

### Command Line
```bash
# jscodeshift (JavaScript)
npx jscodeshift -t transform.js src/

# GumTree (Java)
gumtree diff file1.java file2.java
```

## Common Refactoring Scenarios

### Legacy Code
```java
// 1. Sprout Method
public void process() {
    // Existing code...
    sproutMethod();
    // More existing code...
}

// 2. Wrap Method
public void process() {
    try {
        processWithLogging();
    } catch (Exception e) {
        log(e);
    }
}

// 3. Characterization Tests
@Test
void testExistingBehavior() {
    // Document current behavior
    assertEquals(expected, system.process(input));
}
```

### Performance Refactoring
```java
// Before
List<String> result = list.stream()
    .filter(x -> expensiveOperation(x))
    .collect(Collectors.toList());

// After
List<String> result = new ArrayList<>();
for (String s : list) {
    if (cheapCheck(s)) {
        if (expensiveOperation(s)) {
            result.add(s);
        }
    }
}
```

### Thread Safety Refactoring
```java
// Before
class Counter {
    private int count = 0;
    public void increment() { count++; }
}

// After
class Counter {
    private final AtomicInteger count = new AtomicInteger(0);
    public void increment() { count.incrementAndGet(); }
}
```

## Measuring Refactoring Impact

### Metrics to Track
- **Cyclomatic Complexity**: Should decrease
- **Lines of Code**: Should decrease
- **Coupling**: Should decrease
- **Cohesion**: Should increase
- **Test Coverage**: Should maintain/increase

### Tools
```bash
# PMD CPD (Copy-Paste Detector)
pmd cpd --minimum-tokens 50 --language java --dir src/

# SonarQube
mvn sonar:sonar

# Checkstyle
mvn checkstyle:check
```

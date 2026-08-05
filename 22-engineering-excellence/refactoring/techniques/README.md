# Refactoring Techniques

## Overview

This guide covers specific refactoring techniques from Martin Fowler's catalog. Each technique addresses particular code smells and provides step-by-step instructions for safe refactoring.

## Extract Method

### Problem
A code fragment that can be grouped together.

### Solution
Turn the fragment into a named method with a explanatory name.

### Example
```java
// Before
void printOwing() {
    printBanner();
    
    // Print details
    System.out.println("name: " + _name);
    System.out.println("amount: " + getOutstanding());
}

// After
void printOwing() {
    printBanner();
    printDetails();
}

void printDetails() {
    System.out.println("name: " + _name);
    System.out.println("amount: " + getOutstanding());
}
```

### Steps
1. Create a new method with a descriptive name
2. Copy the code fragment to the new method
3. Replace the fragment with a call to the new method
4. Check for local variables and parameters

## Inline Method

### Problem
A method's body is as clear as its name.

### Solution
Put the method's body into the caller and remove the method.

### Example
```java
// Before
int getRating() {
    return moreThanFiveLateDeliveries() ? 2 : 1;
}

boolean moreThanFiveLateDeliveries() {
    return _numberOfLateDeliveries > 5;
}

// After
int getRating() {
    return _numberOfLateDeliveries > 5 ? 2 : 1;
}
```

### Steps
1. Check that the method is not polymorphic
2. Find all callers of the method
3. Replace calls with the method body
4. Remove the method

## Inline Temp

### Problem
A temporary variable is assigned the result of a simple expression and not changed.

### Solution
Replace the temporary variable with the expression.

### Example
```java
// Before
double basePrice = anOrder.basePrice();
return basePrice > 1000;

// After
return anOrder.basePrice() > 1000;
```

## Replace Temp with Query

### Problem
A temporary variable holds the result of an expression that can be put in a method.

### Solution
Replace the temporary variable with a method call.

### Example
```java
// Before
double basePrice = _quantity * _itemPrice;
if (basePrice > 1000) {
    return basePrice * 0.95;
} else {
    return basePrice * 0.98;
}

// After
if (basePrice() > 1000) {
    return basePrice() * 0.95;
} else {
    return basePrice() * 0.98;
}

double basePrice() {
    return _quantity * _itemPrice;
}
```

## Introduce Explaining Variable

### Problem
A complicated expression is assigned to a temporary variable.

### Solution
Assign the result of the expression to temporary variables with explanatory names.

### Example
```java
// Before
if ((platform.toUpperCase().indexOf("MAC") > -1) &&
    (browser.toUpperCase().indexOf("IE") > -1) &&
    wasInitialized()) {
    // do something
}

// After
boolean isMacOs = platform.toUpperCase().indexOf("MAC") > -1;
boolean isIEBrowser = browser.toUpperCase().indexOf("IE") > -1;
boolean wasRecentlyInitialized = wasInitialized();

if (isMacOs && isIEBrowser && wasRecentlyInitialized) {
    // do something
}
```

## Split Temporary Variable

### Problem
A temporary variable is assigned more than once but is not a loop variable.

### Solution
Make a separate temporary variable for each assignment.

### Example
```java
// Before
double temp = 2 * (_length + _width);
System.out.println(temp);
temp = _length * _width;
System.out.println(temp);

// After
double perimeter = 2 * (_length + _width);
System.out.println(perimeter);
double area = _length * _width;
System.out.println(area);
```

## Replace Method with Method Object

### Problem
A long method has local variables that make Extract Method difficult.

### Solution
Turn the method into its own object so that local variables become fields.

### Example
```java
// Before
class Order {
    void score() {
        int primaryBasePrice;
        int secondaryBasePrice;
        int tertiaryBasePrice;
        // complex calculations...
    }
}

// After
class Order {
    void score() {
        new OrderScorer(this).execute();
    }
}

class OrderScorer {
    private Order _order;
    private int _primaryBasePrice;
    private int _secondaryBasePrice;
    private int _tertiaryBasePrice;
    
    OrderScorer(Order order) {
        _order = order;
    }
    
    void execute() {
        _primaryBasePrice = _order.basePrice();
        // ... rest of method
    }
}
```

## Substitute Algorithm

### Problem
You have an algorithm that's hard to understand.

### Solution
Replace the algorithm with one that's easier to understand.

### Example
```java
// Before
String foundPerson(String[] people) {
    for (int i = 0; i < people.length; i++) {
        if (people[i].equals("Don")) {
            return "Don";
        }
        if (people[i].equals("John")) {
            return "John";
        }
        if (people[i].equals("Kent")) {
            return "Kent";
        }
    }
    return "";
}

// After
String foundPerson(String[] people) {
    List<String> candidates = Arrays.asList("Don", "John", "Kent");
    for (String person : people) {
        if (candidates.contains(person)) {
            return person;
        }
    }
    return "";
}
```

## Move Method

### Problem
A method is used more in another class than in its own class.

### Solution
Move the method to the other class.

### Example
```java
// Before
class Order {
    double getWeight() {
        return _items.stream().mapToDouble(Item::getWeight).sum();
    }
}

class Warehouse {
    double calculateShippingCost(Order order) {
        return order.getWeight() * SHIPPING_RATE;
    }
}

// After
class Order {
    double getWeight() {
        return _items.stream().mapToDouble(Item::getWeight).sum();
    }
    
    double calculateShippingCost() {
        return getWeight() * SHIPPING_RATE;
    }
}
```

## Move Field

### Problem
A field is used more in another class than in its own class.

### Solution
Move the field to the other class.

### Example
```java
// Before
class Customer {
    private String _name;
    private Currency _currency;
}

class Account {
    void calculateInterest() {
        // uses customer._currency
    }
}

// After
class Customer {
    private String _name;
}

class Account {
    private Currency _currency;
}
```

## Convert Procedural to Object

### Problem
Procedural-style code.

### Solution
Turn the record into an object and move the procedures into it.

### Example
```java
// Before
class OrderCalculator {
    double calculateTotal(Order order) {
        return order.getItems().stream()
            .mapToDouble(Item::getPrice)
            .sum();
    }
}

// After
class Order {
    double calculateTotal() {
        return _items.stream()
            .mapToDouble(Item::getPrice)
            .sum();
    }
}
```

## Best Practices

### When to Refactor
```markdown
## Refactoring Triggers

- Before adding a feature
- During code review
- When fixing a bug
- During pair programming
- When understanding code
- Before writing tests
```

### Refactoring Rules
```markdown
## Rules

1. Refactor under test coverage
2. Make small changes
3. Commit frequently
4. Don't refactor and add features
5. One refactoring at a time
6. Keep the code compilable
7. Run tests after each change
```

## Related Topics

- [Refactoring Overview](../README.md)
- [Refactoring to Patterns](../patterns/README.md)
- [Technical Debt](../../tech-debt/README.md)
- [Clean Code](../../craftsmanship/clean-code/README.md)

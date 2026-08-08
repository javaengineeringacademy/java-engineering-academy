# Comparator

## Overview

A `Comparator` defines custom ordering for objects. Unlike `Comparable` which defines natural ordering within a class, `Comparator` is an external comparison strategy. This allows multiple orderings for the same class without modifying the class itself.

## Learning Objectives

- Create and use Comparators
- Understand Comparator vs Comparable
- Learn Comparator composition (chaining)
- Apply Comparators in sorted collections and sorting algorithms

## Creating Comparators

```java
// Anonymous class
Comparator<Type> comparator = new Comparator<Type>() {
    @Override
    public int compare(Type o1, Type o2) {
        return Integer.compare(o1.getValue(), o2.getValue());
    }
};

// Lambda syntax
Comparator<Type> comparator = (o1, o2) -> Integer.compare(o1.getValue(), o2.getValue());

// Method reference
Comparator<Type> comparator = Comparator.comparingInt(Type::getValue);
```

## Comparator Composition

```java
Comparator<Person> comparator = Comparator
    .comparing(Person::getName)              // Sort by name
    .thenComparingInt(p -> p.age)            // Then by age
    .reversed();                             // Reverse order
```

## Usage Examples

```java
// Sort by age
Comparator<Person> byAge = Comparator.comparingInt(p -> p.age);
people.sort(byAge);

// Sort by name, then age
Comparator<Person> byNameThenAge = Comparator
    .comparing(Person::getName)
    .thenComparingInt(p -> p.age);
people.sort(byNameThenAge);

// Use with sorted collections
TreeSet<Person> sortedPeople = new TreeSet<>(Comparator.comparingInt(p -> p.age));
```

## Comparable vs Comparator

| Feature | Comparable | Comparator |
|---------|-----------|------------|
| Location | Inside the class | External class/lambda |
| Method | `compareTo(T o)` | `compare(T o1, T o2)` |
| Orderings | One (natural) | Multiple |
| Modifies class | Yes | No |
| Use case | Intrinsic ordering | Extrinsic ordering |

## Best Practices

- Use `Comparator.comparing()` for cleaner code
- Use Comparator composition for multiple sorting criteria
- Handle null values explicitly with `Comparator.nullsFirst()` or `Comparator.nullsLast()`
- Reuse comparators when possible (store as constants)

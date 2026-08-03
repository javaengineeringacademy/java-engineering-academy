# Comparators

## Introduction

Comparators define custom ordering for objects. They're used with sorted collections and sorting algorithms.

## Learning Objectives

- Create and use Comparators
- Understand Comparator vs Comparable
- Learn Comparator composition
- Apply Comparators in sorted collections

## Prerequisites

- Comparable interface
- TreeSet and TreeMap
- Lambda expressions

## Why This Matters

Comparators allow flexible, external ordering of objects without modifying the original class, essential for sorting and sorted collections.

## Syntax

```java
// Creating Comparator
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

// Comparator composition
Comparator<Type> comparator = Comparator
    .comparingInt(Type::getValue)
    .thenComparing(Type::getName)
    .reversed();
```

## Examples

```java
// Example 1: Basic Comparator
class Person {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

Comparator<Person> byAge = Comparator.comparingInt(p -> p.age);
Comparator<Person> byName = Comparator.comparing(p -> p.name);

List<Person> people = new ArrayList<>(Arrays.asList(
    new Person("Alice", 30),
    new Person("Bob", 25),
    new Person("Charlie", 35)
));

people.sort(byAge);
System.out.println(people);  // Sorted by age

// Example 2: Comparator composition
Comparator<Person> personComparator = Comparator
    .comparing(Person::getName)
    .thenComparingInt(p -> p.age)
    .reversed();

people.sort(personComparator);

// Example 3: Using with sorted collections
TreeSet<Person> sortedPeople = new TreeSet<>(Comparator.comparingInt(p -> p.age));
sortedPeople.add(new Person("Alice", 30));
sortedPeople.add(new Person("Bob", 25));
// Sorted by age

// Example 4: Chained comparators
public static <T> Comparator<T> chainComparators(Comparator<T>... comparators) {
    return Arrays.stream(comparators)
        .reduce(Comparator::thenComparing)
        .orElseThrow(IllegalArgumentException::new);
}
```

## Exercises

1. Create a Comparator that sorts strings by length, then alphabetically.
2. Write a method that sorts a list of objects by multiple fields.
3. Implement a Comparator that sorts integers in descending order.

## Interview Questions

- What is the difference between Comparable and Comparator?
- How do you reverse a Comparator?
- How do you create a Comparator that handles null values?

## Common Pitfalls

- Returning wrong values (positive, negative, zero)
- Not handling null values
- Creating comparators that don't implement equals()

## Best Practices

- Use Comparator.comparing() for cleaner code
- Use Comparator composition for multiple sorting criteria
- Handle null values explicitly
- Reuse comparators when possible

## Real World Applications

- Sorting database results
- Custom list ordering
- Priority queue ordering
- Search result ranking

## References

- [Comparator Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/Comparator.html)
- [Java Sorting Tutorial](https://docs.oracle.com/en/java/javase/21/collections/algorithms/ordering.html)

## Summary

In this topic, you learned about Comparators and how to define custom ordering for objects. They're essential for sorting and sorted collections. Practice with the exercises before learning about Sorting.

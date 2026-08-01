# HashSet

## Introduction

HashSet is the most commonly used Set implementation. It uses a HashMap internally to store elements, providing fast lookup and insertion.

## Learning Objectives

- Create and use HashSet
- Understand HashSet's performance characteristics
- Learn about hashing and equals()/hashCode()
- Know when to use HashSet vs alternatives

## Prerequisites

- Set Interface
- equals() and hashCode() methods

## Why This Matters

HashSet is the default choice for most Set use cases due to its O(1) performance for add, remove, and contains operations.

## Syntax

```java
// Creating HashSet
Set<E> set = new HashSet<>();           // Empty
Set<E> set = new HashSet<>(capacity);   // With initial capacity
Set<E> set = new HashSet<>(collection); // From another collection

// Standard Set operations (all O(1) average)
set.add(element);        // Returns false if duplicate
set.remove(element);     // Returns false if not found
set.contains(element);   // Returns true if exists
set.size();              // Get size
```

## Examples

```java
// Example 1: Basic HashSet
Set<String> names = new HashSet<>();
names.add("Alice");
names.add("Bob");
names.add("Charlie");
names.add("Alice");  // Duplicate ignored

System.out.println(names.size());  // 3
System.out.println(names);  // [Alice, Bob, Charlie] (order may vary)

// Example 2: Custom objects in HashSet
class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

Set<Person> people = new HashSet<>();
people.add(new Person("Alice", 30));
people.add(new Person("Alice", 30));  // Same person, won't add duplicate
System.out.println(people.size());  // 1

// Example 3: Fast membership testing
Set<Integer> validIds = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
int userId = 3;
if (validIds.contains(userId)) {
    System.out.println("Valid user");
}
```

## Exercises

1. Create a HashSet of custom objects and verify duplicate prevention.
2. Write a method that finds unique characters in a string using HashSet.
3. Implement a simple caching mechanism using HashSet.

## Interview Questions

- What is the time complexity of add(), remove(), and contains()?
- Why must you override equals() and hashCode() together?
- How does HashSet handle collisions internally?

## Common Pitfalls

- Not overriding equals() and hashCode() for custom objects
- Using HashSet when you need ordered elements
- Assuming HashSet maintains insertion order

## Best Practices

- Set initial capacity to avoid rehashing
- Always override equals() and hashCode() together
- Use LinkedHashSet when you need insertion order
- Use TreeSet when you need sorted order

## Real World Applications

- Tracking unique visitors
- Removing duplicate records
- Fast membership testing
- Implementing caches
- Finding unique elements in data

## References

- [HashSet Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/implementations/hashset.html)

## Summary

In this topic, you learned about HashSet and its O(1) performance for basic operations. It's the most commonly used Set implementation. Practice with the exercises before learning about LinkedHashSet.

# Comparable

## Overview

The `Comparable` interface defines the natural ordering of a class. A class that implements `Comparable` can be compared with instances of itself, providing a single, consistent ordering. This is used by `TreeSet`, `TreeMap`, `Collections.sort()`, and `Arrays.sort()`.

## Learning Objectives

- Understand the `Comparable` interface and its `compareTo()` method
- Implement natural ordering for custom classes
- Learn the `compareTo()` contract (transitivity, consistency with equals)
- Understand how `Comparable` is used by sorted collections
- Compare `Comparable` vs `Comparator`

## The Comparable Interface

```java
public interface Comparable<T> {
    int compareTo(T o);
}
```

### compareTo() Contract

- Returns negative integer if `this < o`
- Returns zero if `this == o`
- Returns positive integer if `this > o`
- Must be consistent with `equals()` (optional but recommended)
- Must be transitive: if a.compareTo(b) > 0 and b.compareTo(c) > 0, then a.compareTo(c) > 0

## Implementation Example

```java
public class Student implements Comparable<Student> {
    private String name;
    private double gpa;

    public Student(String name, double gpa) {
        this.name = name;
        this.gpa = gpa;
    }

    @Override
    public int compareTo(Student other) {
        // Sort by GPA descending (higher GPA first)
        return Double.compare(other.gpa, this.gpa);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Double.compare(student.gpa, gpa) == 0 &&
               Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gpa);
    }
}
```

## Usage with Sorted Collections

```java
TreeSet<Student> students = new TreeSet<>();
students.add(new Student("Alice", 3.8));
students.add(new Student("Bob", 3.5));
students.add(new Student("Charlie", 3.9));
// Automatically sorted by GPA (due to compareTo)
```

## Best Practices

- Keep `compareTo()` consistent with `equals()` when possible
- Use `Double.compare()`, `Integer.compare()`, etc. for primitive comparisons
- Never return arbitrary values; always follow the negative/zero/positive contract
- For multiple fields, chain comparisons: `compare(a, b) != 0 ? compare(a, b) : compare(c, d)`

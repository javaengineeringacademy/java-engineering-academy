# Comparator Internal Details

## How Comparator Works Internally

### The Comparator Interface
The `Comparator<T>` interface defines comparison logic:
```java
public int compare(T o1, T o2);
```

### Comparison Logic
- Returns negative integer if `o1 < o2`
- Returns zero if `o1 == o2`
- Returns positive integer if `o1 > o2`

### Internal Sorting Algorithm
When you use a Comparator with sorting:
1. **Strategy pattern**: Comparator provides sorting strategy
2. **Decoupled comparison**: Separates comparison from object structure
3. **Flexible ordering**: Can define multiple orderings for same type

### Memory Behavior
- **No object modification**: Comparator doesn't modify objects being compared
- **Stateless comparators**: Most comparators are stateless
- **Closures**: Lambda comparators capture variables from surrounding scope

### Performance Characteristics
- **Comparison cost**: Depends on Comparator implementation
- **Cache efficiency**: Comparator calls may cause cache misses
- **Branch prediction**: Complex comparators may cause branch mispredictions

### Implementation Details
TimSort with Comparator works by:
1. Using Comparator for all element comparisons
2. Maintaining stability for equal elements
3. Optimizing for common patterns in comparisons

### Common Comparator Patterns
```java
// Natural ordering
Comparator.naturalOrder()

// Reverse ordering
Comparator.reverseOrder()

// Null-safe
Comparator.nullsFirst(Comparator.naturalOrder())
Comparator.nullsLast(Comparator.naturalOrder())

// Chaining
Comparator.comparingInt(String::length)
         .thenComparing(Comparator.naturalOrder())
```

### Lambda vs Anonymous Class
```java
// Lambda (more efficient)
list.sort((a, b) -> a.compareTo(b));

// Anonymous class (more verbose)
list.sort(new Comparator<String>() {
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});
```

### Memory Considerations
- **Lambda capture**: Lambdas capture variables, increasing memory usage
- **Anonymous classes**: Create new class files, increasing metaspace
- **Method references**: Most efficient, no additional memory overhead

### Best Practices
1. **Keep comparators simple**: Minimize comparison logic
2. **Avoid side effects**: Don't modify objects during comparison
3. **Handle nulls**: Decide on null handling strategy
4. **Consistency**: Ensure transitivity and consistency with equals
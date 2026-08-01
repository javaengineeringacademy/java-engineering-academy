# Mini Project: Generic Type-Safe Collections Library

## Introduction

Build a complete generic collections library demonstrating all generics concepts learned in this module.

## Learning Objectives

- Apply all generics concepts in a complete project
- Design type-safe generic APIs
- Implement generic data structures
- Create reusable generic utilities

## Prerequisites

- All previous topics in this module
- Understanding of data structures
- Collection framework knowledge

## Project Description

Create a type-safe collections library with custom implementations using generics.

### Requirements

1. **Core Data Structures**
   ```java
   // Generic Stack
   public class GenericStack<E> {
       private E[] elements;
       private int size;

       @SuppressWarnings("unchecked")
       public GenericStack(int capacity) {
           elements = (E[]) new Object[capacity];
       }

       public void push(E element) {
           if (size == elements.length) {
               throw new StackOverflowException("Stack is full");
           }
           elements[size++] = element;
       }

       public E pop() {
           if (isEmpty()) {
               throw new EmptyStackException();
           }
           return elements[--size];
       }

       public boolean isEmpty() {
           return size == 0;
       }
   }

   // Generic Pair
   public class GenericPair<F, S> {
       private final F first;
       private final S second;

       public GenericPair(F first, S second) {
           this.first = first;
           this.second = second;
       }

       public F getFirst() { return first; }
       public S getSecond() { return second; }

       public <T> GenericPair<T, S> mapFirst(Function<F, T> mapper) {
           return new GenericPair<>(mapper.apply(first), second);
       }

       public <T> GenericPair<F, T> mapSecond(Function<S, T> mapper) {
           return new GenericPair<>(first, mapper.apply(second));
       }
   }
   ```

2. **Generic Utilities**
   ```java
   public class GenericUtils {
       // Type-safe max
       public static <T extends Comparable<T>> T max(T a, T b) {
           return a.compareTo(b) >= 0 ? a : b;
       }

       // Type-safe filter
       public static <T> List<T> filter(List<? extends T> list,
                                        Predicate<? super T> predicate) {
           return list.stream()
               .filter(predicate)
               .collect(Collectors.toList());
       }

       // Type-safe transform
       public static <T, R> List<R> map(List<? extends T> list,
                                        Function<? super T, ? extends R> mapper) {
           return list.stream()
               .map(mapper)
               .collect(Collectors.toList());
       }

       // Type-safe zip
       public static <T, U> List<GenericPair<T, U>> zip(List<T> list1,
                                                          List<U> list2) {
           int minSize = Math.min(list1.size(), list2.size());
           List<GenericPair<T,>> result = new ArrayList<>();
           for (int i = 0; i < minSize; i++) {
               result.add(new GenericPair<>(list1.get(i), list2.get(i)));
           }
           return result;
       }
   }
   ```

3. **Generic Repository Pattern**
   ```java
   public interface Repository<T, ID> {
       Optional<T> findById(ID id);
       List<T> findAll();
       T save(T entity);
       void deleteById(ID id);
       boolean existsById(ID id);
   }

   public class InMemoryRepository<T, ID> implements Repository<T, ID> {
       private final Map<ID, T> store = new HashMap<>();
       private final Function<T, ID> idExtractor;
       private final IDGenerator<ID> idGenerator;

       public InMemoryRepository(Function<T, ID> idExtractor,
                                  IDGenerator<ID> idGenerator) {
           this.idExtractor = idExtractor;
           this.idGenerator = idGenerator;
       }

       @Override
       public Optional<T> findById(ID id) {
           return Optional.ofNullable(store.get(id));
       }

       @Override
       public List<T> findAll() {
           return new ArrayList<>(store.values());
       }

       @Override
       public T save(T entity) {
           ID id = idExtractor.apply(entity);
           if (id == null) {
               id = idGenerator.generate();
           }
           store.put(id, entity);
           return entity;
       }

       @Override
       public void deleteById(ID id) {
           store.remove(id);
       }

       @Override
       public boolean existsById(ID id) {
           return store.containsKey(id);
       }
   }
   ```

## Implementation Steps

1. Create GenericStack with proper generics and array handling
2. Implement GenericPair with map operations
3. Create GenericUtils with type-safe utility methods
4. Implement Repository interface and InMemoryRepository
5. Add IDGenerator interface with implementations
6. Create demo classes showing all features
7. Add comprehensive tests
8. Document all classes and methods

## Exercises

1. Complete the GenericStack implementation with iteration support
2. Implement the GenericPair with additional operations (swap, etc.)
3. Create additional GenericUtils methods (groupBy, partition, etc.)
4. Add a GenericQueue implementation
5. Implement a GenericTree structure
6. Create comprehensive tests for all components

## Interview Questions

- How would you make this library thread-safe?
- What are the limitations of generic arrays?
- How would you add serialization support?

## Common Pitfalls

- Not handling array creation properly
- Overcomplicating generic hierarchies
- Not considering performance implications

## Best Practices

1. Use proper generic type parameters
2. Handle type erasure properly
3. Provide comprehensive documentation
4. Test with multiple types
5. Consider performance implications
6. Follow SOLID principles

## Real World Applications

- Custom collection libraries
- Framework development
- Utility libraries
- Type-safe APIs

## References

- [Java Generics Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Effective Java - Generics](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Google Guava Collections](https://github.com/google/guava)

## Summary

You have completed the Generics module by building a complete type-safe collections library. This project demonstrates proper generic class design, type-safe APIs, and reusable generic utilities. Apply these patterns in your future projects.

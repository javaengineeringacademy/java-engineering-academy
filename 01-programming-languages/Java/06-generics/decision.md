# Generics - Decision Guide

## When to Use Generics
- **Type safety at compile time** - Catch type errors before runtime
- **Eliminate casts** - No need for explicit casting after retrieval
- **Code reuse** - Write one class/method that works with any type
- **API clarity** - Make contracts explicit in the type system

## Generic Types

### Classes
- `class Box<T>` - Single type parameter
- `class Pair<K, V>` - Multiple type parameters
- `class Container<T extends Comparable<T>>` - Bounded type

### Interfaces
- `interface Repository<T>` - Generic interface
- `interface Transformer<I, O>` - Multiple type params

### Methods
- `public <T> T getLast(List<T> list)` - Independent type parameter
- `public <T extends Comparable<T>> T max(T a, T b)` - Bounded method

## Bounded Type Parameters
- `T extends Number` - Upper bound (Number or subclass)
- `T super Integer` - Lower bound (Integer or superclass)
- `T extends Comparable<T> & Serializable` - Multiple bounds

## Wildcards
- `List<?>` - Unknown type, read-only
- `List<? extends Number>` - Upper bounded, read-only (covariant)
- `List<? super Integer>` - Lower bounded, write-only (contravariant)

### PECS Rule
- **Producer Extends**: Use `? extends T` when you only **read** from a structure
- **Consumer Super**: Use `? super T` when you only **write** to a structure
- **Both**: Use exact type `T` when you both read and write

## Type Erasure
- Generic type parameters are erased at runtime
- `List<String>` and `List<Integer>` are the same class at runtime
- Cannot use `instanceof` with parameterized types
- Cannot create `new T()` or `new T[]`
- Use `Class<T>` tokens or `TypeReference` for runtime type info

## Restrictions
- Cannot use primitives: `List<int>` → use `List<Integer>`
- Cannot use `new T()`, `new T[]`, `class T`
- Cannot have static fields of generic type
- Cannot catch or throw parameterized types
- Cannot create generic arrays: `new List<String>[10]` is illegal

## Best Practices
- Use meaningful type parameter names: `T` (type), `K` (key), `V` (value), `E` (element)
- Prefer bounded parameters when you need operations on the type
- Use wildcards for flexibility in API design
- Avoid raw types; always use parameterized types
- Use `@SuppressWarnings("unchecked")` sparingly and document why

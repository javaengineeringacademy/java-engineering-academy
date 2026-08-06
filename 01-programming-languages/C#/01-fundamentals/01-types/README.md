# C# Types

## Overview
C# has two main categories of types: Value Types and Reference Types.

## Value Types
Store data directly in memory:
- `bool` - true/false
- `byte`, `short`, `int`, `long` - Integer types
- `float`, `double`, `decimal` - Floating point
- `char` - Single character
- `struct` - Custom value types
- `enum` - Enumeration types

```csharp
int number = 42;
bool isActive = true;
DateTime now = DateTime.Now;
```

## Reference Types
Store reference to data in memory:
- `string` - Text
- `object` - Base type
- `dynamic` - Runtime type
- `array` - Collection
- `class` - Reference type

```csharp
string name = "Alice";
int[] numbers = { 1, 2, 3 };
```

## Nullable Types
```csharp
int? nullableInt = null;
double? nullableDouble = 5.5;
```

## Boxing and Unboxing
```csharp
// Boxing (value to object)
int boxed = 42;
object boxedObj = boxed;

// Unboxing (object to value)
int unboxed = (int)boxedObj;
```

## Type Checking
```csharp
typeof(int).IsValueType  // true
typeof(string).IsValueType  // false
```

## Key Takeaways
1. Value types are stored on the stack
2. Reference types are stored on the heap
3. Use nullable types for optional values
4. Avoid unnecessary boxing/unboxing
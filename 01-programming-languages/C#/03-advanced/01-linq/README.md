# C# LINQ

## Overview
LINQ (Language Integrated Query) provides query syntax for collections.

## Where (Filtering)
```csharp
var even = numbers.Where(n => n % 2 == 0);
var adults = people.Where(p => p.Age >= 18);
```

## Select (Projection)
```csharp
var doubled = numbers.Select(n => n * 2);
var names = people.Select(p => p.Name);
```

## OrderBy (Sorting)
```csharp
var sorted = numbers.OrderBy(n => n);
var descending = numbers.OrderByDescending(n => n);
```

## GroupBy (Grouping)
```csharp
var grouped = people.GroupBy(p => p.City);
foreach (var group in grouped)
{
    Console.WriteLine($"{group.Key}: {group.Count()}");
}
```

## Aggregate Operations
```csharp
numbers.Sum()      // Sum
numbers.Average()  // Average
numbers.Min()      // Minimum
numbers.Max()      // Maximum
numbers.Count()    // Count
```

## Element Operations
```csharp
numbers.First()              // First element
numbers.Last()               // Last element
numbers.Single()             // Only element
numbers.First(n => n > 5)    // First matching
```

## Quantifiers
```csharp
numbers.Any(n => n > 5)    // Any match
numbers.All(n => n > 0)    // All match
numbers.Contains(5)        // Contains
```

## Key Takeaways
1. Chain operations for complex queries
2. Use lambda expressions for predicates
3. Leverage built-in aggregate methods
4. Use LINQ for readable, declarative code
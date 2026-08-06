# C# Arrays and Collections

## Overview
C# provides various collection types for storing data.

## Array
Fixed-size collection:
```csharp
int[] numbers = new int[5];
string[] fruits = { "Apple", "Banana" };

// Access
fruits[0]           // First element
fruits[^1]          // Last element
fruits.Length        // Size
```

## List<T>
Dynamic-size collection:
```csharp
List<string> names = new List<string>();
names.Add("Alice");
names.Insert(1, "Bob");
names.Remove("Charlie");
names.Count          // Size
names.Contains("Bob") // Check existence
```

## Dictionary<TKey, TValue>
Key-value pairs:
```csharp
Dictionary<string, int> ages = new Dictionary<string, int>();
ages["Alice"] = 30;
ages.ContainsKey("Bob")     // Check key
ages.ContainsValue(30)      // Check value
ages.TryGetValue("Bob", out int age) // Safe access
```

## Tuples
Multiple values without a class:
```csharp
var person = (Name: "Alice", Age: 30);
var (name, age) = person; // Deconstruction
```

## Common Operations
```csharp
// Sort
Array.Sort(numbers);
names.Sort();

// Find
int index = Array.IndexOf(fruits, "Banana");
string found = names.Find(n => n.StartsWith("A"));

// Filter
names.RemoveAll(n => n.Length > 5);
```

## Key Takeaways
1. Use Array for fixed-size collections
2. Use List for dynamic-size collections
3. Use Dictionary for key-value pairs
4. Use Tuples for lightweight data structures
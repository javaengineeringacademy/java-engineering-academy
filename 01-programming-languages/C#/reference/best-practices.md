# C# Best Practices

## Overview
Guidelines for writing clean, maintainable C# code.

## 1. Use var for Type Inference
```csharp
// Good - type is obvious
var name = "Alice";
var numbers = new List<int>();

// Bad - redundant type
string name = "Alice";
List<int> numbers = new List<int>();
```

## 2. Use Expression-Bodied Members
```csharp
// Good
public string FullName => $"{FirstName} {LastName}";
public int Add(int a, int b) => a + b;

// Bad
public string FullName
{
    get { return FirstName + " " + LastName; }
}
```

## 3. Use Null-Conditional Operators
```csharp
// Good
string name = user?.Name ?? "Unknown";

// Bad
string name = user != null ? user.Name : "Unknown";
```

## 4. Use Pattern Matching
```csharp
// Good
if (shape is Circle circle)
{
    Console.WriteLine(circle.Radius);
}

// Bad
Circle circle = shape as Circle;
if (circle != null)
{
    Console.WriteLine(circle.Radius);
}
```

## 5. Use LINQ
```csharp
// Good
var adults = people.Where(p => p.Age >= 18).ToList();

// Bad
var adults = new List<Person>();
foreach (var person in people)
{
    if (person.Age >= 18)
    {
        adults.Add(person);
    }
}
```

## 6. Use Records for Immutable Data
```csharp
// Good
public record Person(string Name, int Age);

// Bad
public class Person
{
    public string Name { get; }
    public int Age { get; }
    
    public Person(string name, int age)
    {
        Name = name;
        Age = age;
    }
}
```

## 7. Use async/await Properly
```csharp
// Good
async Task<string> FetchDataAsync()
{
    return await httpClient.GetStringAsync(url);
}

// Bad
Task<string> FetchDataAsync()
{
    return httpClient.GetStringAsync(url);
}
```

## 8. Use Using for Resources
```csharp
// Good
using var conn = new SqlConnection(connString);
using var reader = conn.ExecuteReader();

// Bad
SqlConnection conn = new SqlConnection(connString);
conn.Open();
```

## Key Takeaways
1. Use modern C# features
2. Leverage LINQ for collections
3. Use async/await for I/O
4. Use records for immutable data
5. Always dispose resources
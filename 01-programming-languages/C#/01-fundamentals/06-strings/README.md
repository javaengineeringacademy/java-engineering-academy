# C# Strings

## Overview
Strings are immutable sequences of characters.

## String Creation
```csharp
string s1 = "Hello";
string s2 = new string('A', 5); // "AAAAA"
string s3 = string.Concat("Hello", " ", "World");
```

## String Methods
```csharp
s.ToUpper()              // "HELLO"
s.ToLower()              // "hello"
s.Trim()                 // Remove whitespace
s.Substring(1, 3)        // Extract substring
s.Contains("ell")        // Check if contains
s.StartsWith("He")       // Check start
s.EndsWith("lo")         // Check end
```

## String Formatting
```csharp
// Interpolation
$"Name: {name}, Age: {age}"

// Composite
"Name: {0}, Age: {1}", name, age

// String.Format
string.Format("Name: {0}", name)
```

## StringBuilder
```csharp
// Efficient for multiple operations
var sb = new StringBuilder();
sb.Append("Hello");
sb.Append(" World");
string result = sb.ToString();
```

## String Operations
```csharp
// Split
"apple,banana".Split(',') // ["apple", "banana"]

// Join
string.Join(", ", fruits) // "apple, banana"

// Replace
"Hello World".Replace("World", "C#") // "Hello C#"
```

## Verbatim Strings
```csharp
string path = @"C:\Users\Alice\Documents";
```

## Key Takeaways
1. Strings are immutable
2. Use StringBuilder for many operations
3. Use interpolation for formatting
4. Use verbatim strings for paths
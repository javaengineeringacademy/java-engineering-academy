# C# Operators

## Overview
C# provides various operators for performing operations on values.

## Arithmetic Operators
```csharp
int a = 10, b = 3;
a + b   // Addition: 13
a - b   // Subtraction: 7
a * b   // Multiplication: 30
a / b   // Division: 3 (integer)
a % b   // Remainder: 1
```

## Comparison Operators
```csharp
a == b  // Equal
a != b  // Not equal
a > b   // Greater than
a < b   // Less than
a >= b  // Greater or equal
a <= b  // Less or equal
```

## Logical Operators
```csharp
p && q  // AND
p || q  // OR
!p      // NOT
```

## Bitwise Operators
```csharp
m & n   // AND
m | n   // OR
m ^ n   // XOR
m << 2  // Left shift
m >> 2  // Right shift
```

## Null Operators
```csharp
nullStr ?? "default"  // Null-coalescing
name?.Length          // Null-conditional
```

## Ternary Operator
```csharp
int age = 20;
string status = age >= 18 ? "Adult" : "Minor";
```

## Key Takeaways
1. Integer division truncates decimals
2. Use null-coalescing for defaults
3. Use null-conditional for safe access
4. Understand operator precedence
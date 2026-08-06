# C# Methods

## Overview
Methods are blocks of code that perform specific tasks.

## Basic Method
```csharp
static int Add(int a, int b)
{
    return a + b;
}
```

## Default Parameters
```csharp
static string Greet(string name, string greeting = "Hello")
{
    return $"{greeting}, {name}!";
}
```

## Params Array
```csharp
static int Sum(params int[] numbers)
{
    return numbers.Sum();
}
```

## Ref Parameters
```csharp
static void DoubleValue(ref int value)
{
    value *= 2;
}
```

## Out Parameters
```csharp
static void Divide(int a, int b, out int quotient, out int remainder)
{
    quotient = a / b;
    remainder = a % b;
}
```

## Named Arguments
```csharp
Greet(greeting: "Hi", name: "Alice");
```

## Method Overloading
```csharp
static double CalculateArea(double radius) => Math.PI * radius * radius;
static double CalculateArea(double width, double height) => width * height;
```

## Local Functions
```csharp
static void ProcessNumbers(int[] numbers)
{
    bool IsValid(int num) => num > 0;
    
    foreach (int num in numbers)
    {
        if (IsValid(num)) Console.WriteLine(num);
    }
}
```

## Key Takeaways
1. Use default parameters for optional values
2. Use ref for pass-by-reference
3. Use out for multiple return values
4. Use named arguments for clarity
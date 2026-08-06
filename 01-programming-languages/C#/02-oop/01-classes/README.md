# C# Classes

## Overview
Classes are blueprints for creating objects.

## Basic Class
```csharp
public class Person
{
    public string Name { get; set; }
    public int Age { get; set; }
    
    public Person(string name, int age)
    {
        Name = name;
        Age = age;
    }
}
```

## Encapsulation
```csharp
public class BankAccount
{
    private decimal balance;
    
    public decimal Balance => balance;
    
    public void Deposit(decimal amount)
    {
        balance += amount;
    }
}
```

## Static Classes
```csharp
public static class MathHelper
{
    public static double PI = 3.14159;
    
    public static double Add(double a, double b) => a + b;
}
```

## Abstract Classes
```csharp
public abstract class Shape
{
    public string Name { get; set; }
    
    public abstract double Area();
    public abstract double Perimeter();
}
```

## Sealed Classes
```csharp
public sealed class Logger
{
    // Cannot be inherited
}
```

## Properties
```csharp
// Auto-property
public string Name { get; set; }

// Read-only
public int Age { get; }

// Private set
public string Email { get; private set; }
```

## Key Takeaways
1. Use encapsulation for data protection
2. Use abstract classes for base contracts
3. Use sealed classes to prevent inheritance
4. Use static classes for utility methods
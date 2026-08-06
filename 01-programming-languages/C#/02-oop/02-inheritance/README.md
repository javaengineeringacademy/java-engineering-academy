# C# Inheritance

## Overview
Inheritance allows creating new classes from existing ones.

## Basic Inheritance
```csharp
public class Animal
{
    public string Name { get; set; }
    
    public virtual string Speak() => "Makes a sound";
}

public class Dog : Animal
{
    public override string Speak() => "Barks";
}
```

## Constructor Chaining
```csharp
public class Dog : Animal
{
    public string Breed { get; set; }
    
    public Dog(string name, int age, string breed) 
        : base(name, age)
    {
        Breed = breed;
    }
}
```

## Virtual Methods
```csharp
public virtual string Speak()
{
    return "Makes a sound";
}

// Override in derived class
public override string Speak()
{
    return "Barks";
}
```

## Abstract Classes
```csharp
public abstract class Vehicle
{
    public abstract double CalculateFuelEfficiency();
}

public class Car : Vehicle
{
    public override double CalculateFuelEfficiency() => 30.5;
}
```

## Type Checking
```csharp
if (dog is Dog d)
{
    d.Fetch("ball");
}
```

## Key Takeaways
1. Use inheritance for "is-a" relationships
2. Use virtual/override for polymorphism
3. Use abstract for base class contracts
4. Prefer composition over inheritance
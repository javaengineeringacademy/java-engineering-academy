# C# Control Flow

## Overview
Control flow statements determine the order of execution.

## if-else Statement
```csharp
if (condition)
{
    // code
}
else if (anotherCondition)
{
    // code
}
else
{
    // code
}
```

## switch Statement
```csharp
switch (expression)
{
    case value1:
        // code
        break;
    case value2:
        // code
        break;
    default:
        // code
        break;
}
```

## Switch Expression (C# 8.0+)
```csharp
string result = value switch
{
    1 => "One",
    2 => "Two",
    _ => "Other"
};
```

## for Loop
```csharp
for (int i = 0; i < 10; i++)
{
    Console.WriteLine(i);
}
```

## while Loop
```csharp
int i = 0;
while (i < 10)
{
    Console.WriteLine(i);
    i++;
}
```

## do-while Loop
```csharp
int i = 0;
do
{
    Console.WriteLine(i);
    i++;
} while (i < 10);
```

## foreach Loop
```csharp
string[] names = { "Alice", "Bob" };
foreach (string name in names)
{
    Console.WriteLine(name);
}
```

## break and continue
```csharp
for (int i = 0; i < 10; i++)
{
    if (i == 5) break;      // Exit loop
    if (i % 2 == 0) continue; // Skip iteration
    Console.WriteLine(i);
}
```

## Key Takeaways
1. Use switch for multiple conditions
2. Prefer for when count is known
3. Use foreach for collections
4. Avoid goto statements
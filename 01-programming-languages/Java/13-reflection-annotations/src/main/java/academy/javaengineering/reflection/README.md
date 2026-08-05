# Reflection Basics

## Overview
Demonstrates fundamental Java Reflection concepts including obtaining Class objects, inspecting members, accessing private members, and dynamic instantiation.

## Key Concepts

### 1. Obtaining Class Objects
```java
Class<?> clazz1 = Class.forName("java.lang.String");
Class<?> clazz2 = String.class;
Class<?> clazz3 = obj.getClass();
```

### 2. Inspecting Fields
```java
Field[] fields = clazz.getDeclaredFields();
for (Field field : fields) {
    field.setAccessible(true);
    Object value = field.get(instance);
}
```

### 3. Invoking Methods
```java
Method method = clazz.getMethod("methodName", paramTypes);
Object result = method.invoke(instance, args);
```

### 4. Dynamic Instantiation
```java
Constructor<?> ctor = clazz.getConstructor(String.class);
Object instance = ctor.newInstance("value");
```

## Code References
- `ReflectionBasics.java` - Main demonstration class
- `ReflectionBasicsTest.java` - Unit tests

## Common Mistakes
1. Not calling `setAccessible(true)` for private members
2. Not handling reflection exceptions
3. Breaking encapsulation unnecessarily
4. Performance overhead in tight loops

## Interview Questions
1. What are the three ways to get a Class object?
2. How do you access private fields?
3. What exceptions can reflection throw?
4. When should you avoid using reflection?

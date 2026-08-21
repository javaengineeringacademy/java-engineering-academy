# Practices: Introduction to Reflection

Complete the following exercises to reinforce your understanding of Java Reflection fundamentals.

## Exercise 1: Class Introspector

Write a method that takes any object and prints its complete class hierarchy (superclasses) and all implemented interfaces.

```java
package academy.javaengineering.reflection.intro.practices;

public class Exercise1_ClassIntrospector {
    /**
     * Print the full class hierarchy and interfaces for the given object.
     * 
     * Example output for "hello":
     *   Hierarchy: String -> Object
     *   Interfaces: Serializable, Comparable, CharSequence
     */
    public static void printClassInfo(Object obj) {
        // TODO: Implement this method
        // 1. Get the class of obj
        // 2. Walk up the superclass chain
        // 3. Collect all interfaces at each level
        // 4. Print hierarchy and interfaces
    }

    public static void main(String[] args) {
        printClassInfo("hello");
        printClassInfo(new java.util.ArrayList<>());
        printClassInfo(42);
    }
}
```

## Exercise 2: Class Name Resolver

Write a method that takes a simple class name (like "String" or "ArrayList") and returns the fully-qualified Class object, searching through common packages.

```java
package academy.javaengineering.reflection.intro.practices;

public class Exercise2_ClassNameResolver {
    private static final String[] COMMON_PACKAGES = {
        "java.lang",
        "java.util",
        "java.io",
        "java.net",
        "java.math",
        "java.time"
    };

    /**
     * Resolve a simple class name to a Class object by trying common packages.
     * 
     * resolve("String") should return String.class
     * resolve("ArrayList") should return java.util.ArrayList.class
     * resolve("NoSuchClass") should return null
     */
    public static Class<?> resolve(String simpleName) {
        // TODO: Implement this method
        // 1. Try Class.forName() with each common package prefix
        // 2. Return the first match found
        // 3. Return null if no match
    }

    public static void main(String[] args) {
        System.out.println(resolve("String"));
        System.out.println(resolve("ArrayList"));
        System.out.println(resolve("NoSuchClass"));
    }
}
```

## Exercise 3: Reflection Safety Checker

Write a method that checks whether a given class can be safely instantiated via reflection (has a public no-arg constructor, is not abstract, is not an interface).

```java
package academy.javaengineering.reflection.intro.practices;

import java.lang.reflect.*;

public class Exercise3_SafetyChecker {
    /**
     * Check if a class is safe to instantiate via reflection.
     * 
     * Returns a list of reasons why the class cannot be instantiated.
     * Returns an empty list if the class is safe.
     */
    public static java.util.List<String> checkInstantiationSafety(Class<?> clazz) {
        java.util.List<String> issues = new java.util.ArrayList<>();
        // TODO: Implement this method
        // Check for: interface, abstract, no public no-arg constructor, primitive, array
        return issues;
    }

    public static void main(String[] args) {
        System.out.println(checkInstantiationSafety(String.class));
        System.out.println(checkInstantiationSafety(java.util.ArrayList.class));
        System.out.println(checkInstantiationSafety(java.io.Serializable.class));
    }
}
```

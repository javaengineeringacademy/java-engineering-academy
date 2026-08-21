# Practices: Constructor Access

## Exercise 1: Constructor Finder

Write a method that finds the constructor with the fewest parameters in a class.

```java
package academy.javaengineering.reflection.constructor.practices;

import java.lang.reflect.Constructor;

public class Exercise1_ConstructorFinder {
    public static Constructor<?> findSimplestConstructor(Class<?> clazz) {
        // TODO: Return the constructor with fewest parameters
        return null;
    }
}
```

## Exercise 2: Safe Instantiator

Write a method that tries all constructors and returns the first successful instantiation.

```java
package academy.javaengineering.reflection.constructor.practices;

import java.lang.reflect.Constructor;

public class Exercise2_SafeInstantiator {
    public static <T> T safeNewInstance(Class<T> clazz) {
        // TODO: Try each constructor until one succeeds
        return null;
    }
}
```

## Exercise 3: Constructor Signature Formatter

Write a method that returns all constructor signatures sorted by parameter count.

```java
package academy.javaengineering.reflection.constructor.practices;

import java.lang.reflect.Constructor;
import java.util.*;

public class Exercise3_ConstructorSignatureFormatter {
    public static List<String> getSortedSignatures(Class<?> clazz) {
        // TODO: Return sorted constructor signatures
        return null;
    }
}
```

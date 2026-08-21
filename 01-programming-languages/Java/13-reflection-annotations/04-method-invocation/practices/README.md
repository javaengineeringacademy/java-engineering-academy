# Practices: Method Invocation

## Exercise 1: Method Finder

Write a method that finds all methods with a given return type in a class and its superclasses.

```java
package academy.javaengineering.reflection.methodinvocation.practices;

import java.lang.reflect.Method;
import java.util.*;

public class Exercise1_MethodFinder {
    public static List<String> findMethodsByReturnType(Class<?> clazz, Class<?> returnType) {
        // TODO: Find all methods (including inherited) with the given return type
        return null;
    }
}
```

## Exercise 2: Safe Invoker

Write a method that invokes a method and properly unwraps InvocationTargetException.

```java
package academy.javaengineering.reflection.methodinvocation.practices;

import java.lang.reflect.*;

public class Exercise2_SafeInvoker {
    public static Object safeInvoke(Object obj, String methodName, Object... args) throws Exception {
        // TODO: Find method by name and args, invoke, unwrap exceptions
        return null;
    }
}
```

## Exercise 3: Method Signatures Formatter

Write a method that returns all method signatures sorted alphabetically.

```java
package academy.javaengineering.reflection.methodinvocation.practices;

import java.lang.reflect.Method;
import java.util.*;

public class Exercise3_MethodSignaturesFormatter {
    public static List<String> getSortedSignatures(Class<?> clazz) {
        // TODO: Return sorted list of method signatures like "int add(int, int)"
        return null;
    }
}
```

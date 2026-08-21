# Practices: Dynamic Proxy

## Exercise 1: Null-Safe Proxy

Write a proxy that throws NullPointerException if any method argument is null.

```java
package academy.javaengineering.reflection.proxy.practices;

import java.lang.reflect.*;

public class Exercise1_NullSafeProxy {
    public static <T> T createNullSafeProxy(T target) {
        // TODO: Create proxy that checks all args for null
        return null;
    }
}
```

## Exercise 2: Retry Proxy

Write a proxy that retries failed method calls up to N times.

```java
package academy.javaengineering.reflection.proxy.practices;

import java.lang.reflect.*;

public class Exercise2_RetryProxy {
    public static <T> T createRetryProxy(T target, int maxRetries) {
        // TODO: Create proxy that retries on exception
        return null;
    }
}
```

## Exercise 3: Access Control Proxy

Write a proxy that checks if a method has a specific annotation before allowing invocation.

```java
package academy.javaengineering.reflection.proxy.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;

public class Exercise3_AccessControlProxy {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Secured {}

    public static <T> T createSecuredProxy(T target) {
        // TODO: Create proxy that only allows @Secured methods
        return null;
    }
}
```

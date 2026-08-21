# Solutions: Method Invocation

## Exercise 1: Method Finder

```java
package academy.javaengineering.reflection.methodinvocation.solutions;

import java.lang.reflect.Method;
import java.util.*;

public class Solution1_MethodFinder {

    public static List<String> findMethodsByReturnType(Class<?> clazz, Class<?> returnType) {
        List<String> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null) {
            for (Method m : current.getDeclaredMethods()) {
                if (returnType.isAssignableFrom(m.getReturnType())) {
                    result.add(m.getName());
                }
            }
            current = current.getSuperclass();
        }
        Collections.sort(result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(findMethodsByReturnType(ArrayList.class, boolean.class));
    }
}
```

## Exercise 2: Safe Invoker

```java
package academy.javaengineering.reflection.methodinvocation.solutions;

import java.lang.reflect.*;

public class Solution2_SafeInvoker {

    public static Object safeInvoke(Object obj, String methodName, Object... args) throws Exception {
        Class<?>[] paramTypes = args == null ? new Class<?>[0] : new Class<?>[args.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = args[i].getClass();
            if (paramTypes[i] == Integer.class) paramTypes[i] = int.class;
            if (paramTypes[i] == Long.class) paramTypes[i] = long.class;
            if (paramTypes[i] == Double.class) paramTypes[i] = double.class;
            if (paramTypes[i] == Boolean.class) paramTypes[i] = boolean.class;
        }

        Method method = obj.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);

        try {
            return method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            if (cause instanceof Exception) throw (Exception) cause;
            throw new RuntimeException(cause);
        }
    }

    static class Greeter {
        private String greet(String name) { return "Hello, " + name; }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(safeInvoke(new Greeter(), "greet", "World"));
    }
}
```

## Exercise 3: Method Signatures Formatter

```java
package academy.javaengineering.reflection.methodinvocation.solutions;

import java.lang.reflect.Method;
import java.util.*;

public class Solution3_MethodSignaturesFormatter {

    public static List<String> getSortedSignatures(Class<?> clazz) {
        List<String> sigs = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            StringBuilder sb = new StringBuilder();
            sb.append(m.getReturnType().getSimpleName()).append(" ");
            sb.append(m.getName()).append("(");
            Class<?>[] params = m.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(params[i].getSimpleName());
            }
            sb.append(")");
            sigs.add(sb.toString());
        }
        Collections.sort(sigs);
        return sigs;
    }

    public static void main(String[] args) {
        getSortedSignatures(String.class).forEach(System.out::println);
    }
}
```

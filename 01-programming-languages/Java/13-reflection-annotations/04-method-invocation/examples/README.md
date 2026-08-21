# Examples: Method Invocation

## Example 1: Basic Method Invocation

```java
package academy.javaengineering.reflection.methodinvocation;

import java.lang.reflect.Method;

public class BasicMethodExample {

    static class Calculator {
        public int add(int a, int b) { return a + b; }
        private int multiply(int a, int b) { return a * b; }
        static int parse(String s) { return Integer.parseInt(s); }
        public void greet(String name) { System.out.println("Hello, " + name + "!"); }
    }

    public static void main(String[] args) throws Exception {
        Calculator calc = new Calculator();

        Method addMethod = Calculator.class.getMethod("add", int.class, int.class);
        int sum = (int) addMethod.invoke(calc, 3, 5);
        System.out.println("3 + 5 = " + sum);

        Method mulMethod = Calculator.class.getDeclaredMethod("multiply", int.class, int.class);
        mulMethod.setAccessible(true);
        int product = (int) mulMethod.invoke(calc, 3, 5);
        System.out.println("3 * 5 = " + product);

        Method greetMethod = Calculator.class.getMethod("greet", String.class);
        greetMethod.invoke(calc, "World");

        Method parseMethod = Calculator.class.getMethod("parse", String.class);
        int parsed = (int) parseMethod.invoke(null, "42");
        System.out.println("Parsed: " + parsed);
    }
}
```

## Example 2: Method Metadata Inspector

```java
package academy.javaengineering.reflection.methodinvocation;

import java.lang.reflect.*;
import java.util.*;

public class MethodMetadataInspector {

    public static void inspectMethods(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.printf("%s %s %s(%s) throws %s%n",
                Modifier.toString(method.getModifiers()),
                method.getReturnType().getSimpleName(),
                method.getName(),
                Arrays.toString(method.getParameterTypes()),
                Arrays.toString(method.getExceptionTypes()));
        }
    }

    public static void main(String[] args) {
        inspectMethods(ArrayList.class);
    }
}
```

## Example 3: Method Dispatcher

```java
package academy.javaengineering.reflection.methodinvocation;

import java.lang.reflect.Method;
import java.util.*;

public class MethodDispatcher {

    private final Object target;
    private final Map<String, Method> methodCache;

    public MethodDispatcher(Object target) {
        this.target = target;
        this.methodCache = new HashMap<>();
        for (Method m : target.getClass().getDeclaredMethods()) {
            m.setAccessible(true);
            methodCache.put(m.getName(), m);
        }
    }

    public Object dispatch(String methodName, Object... args) throws Exception {
        Method method = methodCache.get(methodName);
        if (method == null) throw new NoSuchMethodException(methodName);
        return method.invoke(target, args);
    }

    static class Service {
        private String process(String input) { return "Processed: " + input; }
        private int compute(int x) { return x * 2; }
    }

    public static void main(String[] args) throws Exception {
        MethodDispatcher dispatcher = new MethodDispatcher(new Service());
        System.out.println(dispatcher.dispatch("process", "hello"));
        System.out.println(dispatcher.dispatch("compute", 21));
    }
}
```

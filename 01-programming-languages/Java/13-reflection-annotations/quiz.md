# Reflection and Annotations Quiz

## Question 1 (MCQ)
Which method is used to access a private field of a class via reflection?
- A) `field.get(obj)`
- B) `field.setAccessible(true)` followed by `field.get(obj)`
- C) `field.readAs(obj)`
- D) `field.private(obj)`

**Answer: B**
**Explanation:** To access private fields via reflection, you must first call `setAccessible(true)` to bypass the access check, then use `field.get(obj)` to retrieve the value.

---

## Question 2 (MCQ)
What is the difference between `@Retention(RetentionPolicy.SOURCE)` and `@Retention(RetentionPolicy.RUNTIME)`?
- A) SOURCE annotations are faster, RUNTIME are slower
- B) SOURCE annotations are discarded after compilation, RUNTIME are available via reflection at runtime
- C) There is no difference
- D) RUNTIME annotations only work with interfaces

**Answer: B**
**Explanation:** SOURCE retention means the annotation exists only in source code and is discarded by the compiler. RUNTIME retention means the annotation is stored in the .class file and can be accessed via reflection at runtime.

---

## Question 3 (MCQ)
What is a Dynamic Proxy in Java?
- A) A proxy pattern implemented at compile time
- B) A mechanism to create proxy instances at runtime that implement one or more interfaces
- C) A type of network proxy
- D) A class that extends java.lang.Proxy

**Answer: B**
**Explanation:** Dynamic Proxy creates proxy objects at runtime using `java.lang.reflect.Proxy`. It requires an `InvocationHandler` and is commonly used for AOP, logging, and transaction management.

---

## Question 4 (MCQ)
What is a significant performance implication of using reflection in production code?
- A) Reflection makes code run faster
- B) Reflection calls are slower than direct calls due to boxing/unboxing, method lookup, and access checks
- C) Reflection has no performance impact
- D) Reflection only affects memory, not speed

**Answer: B**
**Explanation:** Reflection involves runtime method lookup, parameter boxing/unboxing, and security checks, making it significantly slower than direct method calls. It should be used judiciously in performance-critical code.

---

## Question 5 (Code Output)
What does this code print?

```java
import java.lang.reflect.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.lang.String");
        Method[] methods = clazz.getDeclaredMethods();
        int count = 0;
        for (Method m : methods) {
            if (m.getName().equals("length")) {
                count++;
                System.out.println("Found: " + m);
            }
        }
        System.out.println("Total length methods: " + count);
    }
}
```

**Answer:** Found: public int java.lang.String.length() and Total length methods: 1
**Explanation:** Reflection finds the `length()` method in the String class. `getDeclaredMethods()` returns all methods declared in the class (not inherited).

---

## Question 6 (Code Output)
What does this code print?

```java
import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    String value();
}

@MyAnnotation("Hello")
class MyClass {}

public class Main {
    public static void main(String[] args) {
        MyAnnotation ann = MyClass.class.getAnnotation(MyAnnotation.class);
        System.out.println(ann.value());
    }
}
```

**Answer:** Hello
**Explanation:** The annotation has RUNTIME retention, so it's available via reflection. `getAnnotation()` retrieves the annotation instance, and `value()` returns "Hello".

---

## Question 7 (Bug Finding)
Find the bug:

```java
import java.lang.reflect.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Field field = MyClass.class.getDeclaredField("secret");
        Object value = field.get(null);
        System.out.println(value);
    }
}

class MyClass {
    private static String secret = "hidden";
}
```

**Bug:** The code attempts to access a private static field without calling `setAccessible(true)`. This will throw an `IllegalAccessException` at runtime.
**Fix:**
```java
Field field = MyClass.class.getDeclaredField("secret");
field.setAccessible(true);
Object value = field.get(null);
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface LogExecutionTime {}

class Service {
    @LogExecutionTime
    public void processData() {
        // processing...
    }
}

public class Main {
    public static void main(String[] args) {
        for (Method method : Service.class.getMethods()) {
            if (method.isAnnotationPresent(LogExecutionTime.class)) {
                System.out.println("Found annotated method: " + method.getName());
                // Bug: no timing logic implemented
            }
        }
    }
}
```

**Bug:** The code detects the annotation but doesn't actually measure execution time. The annotation is just metadata — without implementing timing logic (e.g., recording start/end time), it serves no purpose.
**Fix:** Implement the timing logic:
```java
long start = System.nanoTime();
method.invoke(serviceInstance);
long end = System.nanoTime();
System.out.println("Execution time: " + (end - start) + "ns");
```

---

## Question 9 (Scenario-based)
You need to build a dependency injection framework that automatically wires dependencies based on custom annotations. Which reflection features should you use?

- A) Class.forName() to load classes, getDeclaredFields() to find @Inject annotations, setAccessible(true) to inject dependencies
- B) Use static factory methods for all dependencies
- C) Use XML configuration files
- D) Use global variables for dependency sharing

**Answer: A**
**Explanation:** Reflection enables scanning classes for custom annotations (like @Inject), discovering fields that need injection, and setting their values dynamically. This is how frameworks like Spring and Guice work internally.

---

## Question 10 (Architecture Decision)
You are building an AOP (Aspect-Oriented Programming) framework that needs to intercept method calls for logging, transactions, and security checks. How should you implement this?

- A) Modify every method to add cross-cutting concerns
- B) Use Dynamic Proxy with InvocationHandler to intercept method calls at runtime, combined with custom annotations to mark methods that need interception
- C) Use inheritance to create proxy classes
- D) Use bytecode manipulation at compile time

**Answer: B**
**Explanation:** Dynamic Proxy creates proxy objects at runtime that intercept method calls via InvocationHandler. Custom annotations (like @Transactional, @Secured) mark methods for interception. This approach doesn't require modifying target classes and supports composition-based AOP.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
import java.lang.reflect.*;

class Person {
    private String name = "Alice";
    private int age = 30;
}

public class Main {
    public static void main(String[] args) throws Exception {
        Person person = new Person();

        Field nameField = Person.class.getDeclaredField("name");
        nameField.setAccessible(true);
        String name = (String) nameField.get(person);

        Field ageField = Person.class.getDeclaredField("age");
        ageField.setAccessible(true);
        int age = (int) ageField.get(person);

        nameField.set(person, "Bob");
        ageField.set(person, 25);

        System.out.println(name + " " + age);
        System.out.println(nameField.get(person) + " " + ageField.get(person));
    }
}
```

A) Alice 30 Bob 25
B) Alice 30 Alice 30
C) Bob 25 Bob 25
D) IllegalAccessException

**Answer: A**
**Explanation:** Reflection with `setAccessible(true)` bypasses access control. Initial read: name="Alice", age=30. After `set()` calls: name becomes "Bob", age becomes 25. First print: original values read before modification → "Alice 30". Second print: modified values → "Bob 25". Output: `Alice 30 Bob 25`.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Version { String value(); }

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Deprecated { String reason(); }

class ApiService {
    @Deprecated(reason = "Use v2 instead")
    public void oldMethod() {}

    @Deprecated(reason = "Removed in next release")
    public void anotherOld() {}

    public void newMethod() {}
}

public class Main {
    public static void main(String[] args) {
        int count = 0;
        for (Method m : ApiService.class.getMethods()) {
            if (m.isAnnotationPresent(Deprecated.class)) {
                Deprecated ann = m.getAnnotation(Deprecated.class);
                System.out.println(m.getName() + ": " + ann.reason());
                count++;
            }
        }
        System.out.println("Deprecated methods: " + count);
    }
}
```

A) oldMethod: Use v2 instead
   anotherOld: Removed in next release
   Deprecated methods: 2
B) newMethod: null
   Deprecated methods: 1
C) Deprecated methods: 0
D) Compilation error

**Answer: A**
**Explanation:** Iterating over `getMethods()` finds methods annotated with `@Deprecated`. `oldMethod` has reason "Use v2 instead", `anotherOld` has "Removed in next release". `newMethod` has no annotation, so it's skipped. Two deprecated methods are found. Output: `oldMethod: Use v2 instead` then `anotherOld: Removed in next release` then `Deprecated methods: 2`.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
import java.lang.reflect.*;

interface Greeter {
    String greet(String name);
}

public class Main {
    public static void main(String[] args) {
        Greeter proxy = (Greeter) Proxy.newProxyInstance(
            Greeter.class.getClassLoader(),
            new Class[]{Greeter.class},
            (proxy1, method, methodArgs) -> {
                System.out.println("Intercepted: " + method.getName());
                return "Hello, " + methodArgs[0] + "!";
            }
        );

        System.out.println(proxy.greet("World"));
        System.out.println(proxy.greet("Java"));
    }
}
```

A) Intercepted: greet
   Hello, World!
   Intercepted: greet
   Hello, Java!
B) Hello, World!
   Hello, Java!
C) Interceptor: greet Hello, World!
D) Compilation error

**Answer: A**
**Explanation:** Dynamic Proxy intercepts all method calls on the proxy object. `proxy.greet("World")` triggers the InvocationHandler, which prints "Intercepted: greet" and returns "Hello, World!". Second call: same behavior for "Java". Output: `Intercepted: greet` then `Hello, World!` then `Intercepted: greet` then `Hello, Java!`.


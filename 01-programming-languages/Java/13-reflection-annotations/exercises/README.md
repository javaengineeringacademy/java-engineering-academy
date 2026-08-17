# Reflection Exercises

Practice Java reflection and annotations through hands-on exercises.

## Exercise 1: Simple Dependency Injector

**Problem Statement:**
Create a simple dependency injection framework using reflection. Define a `@Inject` annotation and a container that scans classes for fields annotated with `@Inject`, then automatically wires dependencies by creating and injecting instances.

**Expected Behavior:**
- A class with `@Inject` annotated fields receives instances automatically.
- The container creates instances of dependencies using no-arg constructors.
- Circular dependencies are detected and reported with a clear error.
- The container supports singleton and transient (new instance each time) scopes.
- `@Inject` on a field of an interface type is resolved to its implementation.

**Hints:**
- Use `Class.getDeclaredFields()` and `field.getAnnotation(Inject.class)` to find injectable fields.
- Use `field.setAccessible(true)` to inject into private fields.
- Maintain a `Map<Class<?>, Object>` for singleton instances.
- Track a `Set<Class<?>>` during resolution to detect circular dependencies.

---

## Exercise 2: Validation Framework

**Problem Statement:**
Build a validation framework using custom annotations and reflection. Create annotations like `@NotNull`, `@Min(value)`, `@Max(value)`, `@Size(min, max)`, and `@Email` that can be placed on fields. A `Validator` class uses reflection to read these annotations and validate objects.

**Expected Behavior:**
- `@NotNull` fails if the field value is null.
- `@Min(0)` fails if the numeric value is less than 0.
- `@Max(100)` fails if the numeric value exceeds 100.
- `@Size(min=2, max=50)` fails if string length is outside the range.
- `@Email` fails if the string does not match email pattern.
- A `ValidationResult` collects all errors with field names and messages.

**Hints:**
- Use `field.getAnnotation(NotNull.class)` to read annotations.
- Use `field.get(object)` to read the field value from an instance.
- Build a `Map<String, List<String>>` mapping field names to error messages.
- Use `@Retention(RUNTIME)` and `@Target(FIELD)` on all annotations.

---

## Exercise 3: Custom Annotation Processor

**Problem Statement:**
Create a custom annotation `@AutoToString` that, when placed on a class, generates a `toString()` method at compile time using the annotation processor API. The generated `toString()` includes all fields with their names and values.

**Expected Behavior:**
- Classes annotated with `@AutoToString` get a generated `toString()` method.
- The generated method includes field names and their string values.
- The annotation processor runs during `mvn compile`.
- A warning is issued if `@AutoToString` is placed on a record (which already has toString).
- The generated code handles null field values gracefully.

**Hints:**
- Extend `AbstractProcessor` and override `process()`.
- Use `@SupportedAnnotationTypes("com.example.AutoToString")`.
- Generate Java source files using `JavaFileObject` and `Writer`.
- Access element annotations using `element.getAnnotation(AutoToString.class)`.

---

## Exercise 4: JSON Serializer with Reflection

**Problem Statement:**
Implement a basic JSON serializer using reflection that converts Java objects to JSON strings. Handle primitive types, String, nested objects, and arrays. Support `@JsonProperty` to customize field names in the output.

**Expected Behavior:**
- Primitives and their wrappers serialize as their JSON equivalents.
- Strings serialize with double quotes.
- Nested objects serialize as nested JSON objects.
- Arrays serialize as JSON arrays.
- `@JsonProperty("custom_name")` renames the field in output.
- Null values are included as `null` in the JSON.

**Hints:**
- Use `Class.getDeclaredFields()` and iterate over each field.
- Use `field.get(object)` to read values and `field.getType()` for type checking.
- Handle `String` with quotes, primitives without, and objects recursively.
- Use `field.getAnnotation(JsonProperty.class)` for custom names.

---

## Exercise 5: Mock Framework

**Problem Statement:**
Build a minimal mock framework using `java.lang.reflect.Proxy`. Create a `MockFactory` that generates proxy implementations of interfaces where every method returns a default value and records all invocations for later verification.

**Expected Behavior:**
- `MockFactory.create(Service.class)` returns a proxy implementing `Service`.
- All methods return default values (0 for int, null for objects, false for boolean).
- Each invocation is recorded with method name, arguments, and return value.
- `mock.verify(methodName).wasCalledOnce()` checks if a method was called.
- `mock.verify(methodName).wasCalledWith(args)` checks arguments.

**Hints:**
- Use `Proxy.newProxyInstance(classLoader, interfaces, handler)`.
- In the `InvocationHandler`, record each call in a list.
- Store invocations as `Map<String, List<Invocation>>`.
- Build a fluent verification API on the recorded invocations.

---

## Exercise 6: Property Observer

**Problem Statement:**
Implement a property change observer system using reflection. A `@Observable` annotation marks fields that should be watched. When a field value changes, all registered `PropertyChangeListener`s are notified with the old and new values.

**Expected Behavior:**
- Fields annotated with `@Observable` trigger notifications on change.
- Listeners receive `propertyChanged(fieldName, oldValue, newValue)`.
- Changes from null to a value and value to null both trigger notifications.
- Setting the same value does not trigger a notification.
- Multiple listeners can be registered for the same property.
- Listeners can be removed dynamically.

**Hints:**
- Scan the class for `@Observable` fields and wrap them with getter/setter proxies.
- Use `java.beans.PropertyChangeSupport` for built-in listener management.
- Compare old and new values with `Objects.equals()` before firing events.
- Provide a `addPropertyChangeListener(String property, listener)` method.

## Interview Questions

1. **What is Java Reflection and when should you use it?**
   Reflection allows inspecting and modifying classes, methods, fields, and constructors at runtime. Use it for frameworks (Spring, Hibernate), dependency injection, serialization, and testing. Avoid it in application code when compile-time alternatives exist—it's slower, bypasses compile-time checks, and reduces IDE support.

2. **What is the difference between `Class.forName()` and `ClassName.class`?**
   `Class.forName("com.example.MyClass")` loads the class by string name at runtime, requiring the full qualified name. `MyClass.class` is a compile-time reference that returns the `Class` object. `Class.forName()` is used when the class name is dynamic (e.g., from configuration). `MyClass.class` is type-safe and preferred when the class is known at compile time.

3. **How do annotations work internally in Java?**
   Annotations are metadata stored in the `.class` file's `RuntimeVisibleAnnotations` attribute. At runtime, `AnnotatedElement` methods (`getAnnotation()`, `getAnnotations()`) read this metadata via reflection. Annotation values are stored as constant pool entries. `@Retention(RUNTIME)` ensures annotations are available at runtime; `CLASS` retains them in bytecode only; `SOURCE` discards them after compilation.

4. **What are the performance implications of using Reflection?**
   Reflection is 10-50x slower than direct method calls due to: type checking at runtime, security checks, and dynamic dispatch. Mitigations: cache `Method`/`Field` objects, use `setAccessible(true)` to bypass access checks, consider bytecode generation (ByteBuddy) for performance-critical paths. For most applications, the overhead is negligible compared to I/O.

5. **What is `setAccessible(true)` and when should you use it?**
   `setAccessible(true)` disables Java's access control checks on a `Field`, `Method`, or `Constructor`. Use it to access private members from outside the class (e.g., framework code injecting dependencies). It bypasses encapsulation, so use sparingly and document why. It may not work with Java module system without `--add-opens`.

6. **What is the difference between compile-time and runtime annotations?**
   Compile-time annotations (e.g., `@Override`, `@SuppressWarnings`) are processed by annotation processors during compilation and don't exist at runtime. Runtime annotations (e.g., `@Autowired`, `@Test`) are retained via `@Retention(RUNTIME)` and accessible via reflection. Frameworks use runtime annotations to configure behavior dynamically.

7. **How do you create a custom annotation in Java?**
   Define an `@interface` with `@Retention` (when it's available) and `@Target` (where it can be placed). Add annotation elements as abstract methods with default values: `@Retention(RUNTIME) @Target(FIELD) public @interface MyAnnotation { String value() default ""; int priority() default 0; }`. Access values via `element.getAnnotation(MyAnnotation.class).value()`.

8. **What are annotation processors and how do they work?**
   Annotation processors run during `javac` compilation, processing annotations in source code. They extend `AbstractProcessor`, override `process()`, and generate new Java source files or resources. They're used by Lombok (`@Data`), MapStruct (`@Mapper`), and Dagger (`@Inject`). Processors are registered via `META-INF/services/javax.annotation.processing.Processor`.

9. **What are the risks of using Reflection?**
   - Bypasses compile-time type checking, causing runtime `ClassCastException`
   - Breaks encapsulation, violating object-oriented principles
   - Performance overhead for method invocation
   - Fragile code that breaks when internal APIs change
   - Security concerns—can access private members
   - Incompatible with Java modules without explicit `--add-opens`

10. **What is the Java Module System's impact on Reflection?**
    Java 9+ modules restrict reflective access to public APIs by default. Accessing private members requires `--add-opens module/package=target-module` JVM args. This breaks many frameworks (Spring, Hibernate) that rely on deep reflection. Solutions: add `--add-opens` flags, migrate to public APIs, or use Java agent modules.

## Pitfalls

1. **Overusing Reflection** — Using reflection for simple operations like calling a known method. If you know the class at compile time, use direct invocation. Reserve reflection for genuinely dynamic scenarios.

2. **Not Caching Method/Field Objects** — `Class.getDeclaredFields()` and `getDeclaredMethods()` are expensive. Call them once during initialization and cache the results in a `Map` or static fields.

3. **Ignoring `setAccessible` Security Implications** — Using `setAccessible(true)` without documentation. This can break with Java modules and creates security vulnerabilities. Document the reason and consider alternatives.

4. **Creating Annotations Without `@Retention`** — Forgetting `@Retention(RUNTIME)` means your annotation won't be available via reflection. Always specify retention unless you only need compile-time processing.

5. **Not Handling `IllegalAccessException`** — Catching and ignoring access errors. Reflection should fail loudly when it can't access required members. Log the error and handle it appropriately.

6. **Using Reflection for Serialization Instead of Dedicated Libraries** — Building custom JSON/XML serializers with reflection when Jackson/Gson already handle edge cases (circular references, custom adapters, date formats).

7. **Annotation Hell** — Adding annotations to every class and method. Annotations should solve real problems (configuration, validation, documentation), not serve as comments. If an annotation doesn't affect runtime behavior, reconsider its necessity.

8. **Not Considering `instanceof` Before Reflection** — Using reflection to check types when `instanceof` or pattern matching is simpler and faster. Only use reflection when the type isn't known at compile time.

## Performance

1. **Method Invocation Speed** — Direct method call: ~1ns. Reflective call: ~50ns. Cached reflective call with `setAccessible(true)`: ~10ns. For millions of calls, cache `Method` objects.

2. **Field Access** — `Field.get()` is ~10x slower than direct field access. Cache `Field` objects and use `setAccessible(true)` to bypass access checks. Consider using `MethodHandle` for ~5x better performance than reflection.

3. **Annotation Lookup** — `getAnnotation()` is O(n) in the number of annotations. Cache results during class scanning (e.g., at framework startup, not per-request).

4. **Class Loading** — `Class.forName()` triggers class loading and initialization. Use `ClassLoader.loadClass()` when you want to load without initializing static blocks.

5. **`getDeclaredFields()` vs `getFields()`** — `getDeclaredFields()` returns all fields including private (slower). `getFields()` returns only public fields including inherited. Use `getFields()` when you only need public API.

6. **Annotation Processing at Compile-Time** — Annotation processors add ~10-20% to compile time. Use `@SupportedSourceVersion(SourceVersion.RELEASE_17)` and optimize `process()` to return quickly for irrelevant annotations.

7. **MethodHandle Performance** — `MethodHandle.invoke()` is 2-5x faster than reflective `Method.invoke()` after warmup. Use `MethodHandles.Lookup` for performance-critical dynamic dispatch.

8. **Proxy Creation Overhead** — `Proxy.newProxyInstance()` creates a new class each time. Cache proxy instances or use `Proxy.getProxyClass()` to reuse class definitions.

## Examples

```java
// Reflection - Accessing Private Field
Field nameField = User.class.getDeclaredField("name");
nameField.setAccessible(true);
String name = (String) nameField.get(userInstance);
nameField.set(userInstance, "New Name");

// Reflection - Invoking Method Dynamically
Method method = calculatorClass.getMethod("add", int.class, int.class);
int result = (int) method.invoke(calculatorInstance, 2, 3);

// Custom Annotation
@Retention(RUNTIME)
@Target(FIELD)
public @interface JsonProperty {
    String value() default "";
}

// Reading Annotations
for (Field field : clazz.getDeclaredFields()) {
    JsonProperty prop = field.getAnnotation(JsonProperty.class);
    if (prop != null) {
        String jsonKey = prop.value().isEmpty() ? field.getName() : prop.value();
        // serialize with jsonKey
    }
}

// Annotation Processor (compile-time)
@SupportedAnnotationTypes("com.example.AutoToString")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class AutoToStringProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AutoToString.class)) {
            // Generate toString() method
        }
        return true;
    }
}

// Dynamic Proxy with Reflection
Object proxy = Proxy.newProxyInstance(
    MyClass.class.getClassLoader(),
    new Class[]{MyInterface.class},
    (proxy1, method, args) -> {
        System.out.println("Calling: " + method.getName());
        return method.invoke(targetObject, args);
    }
);
```

## Internal Working

Java Reflection accesses metadata stored in the `.class` file format (Classfile structure). Each class file contains a constant pool with method descriptors, field descriptors, and annotation data. When `getDeclaredMethods()` is called, the JVM parses the method_info table from the class file. Annotation values are stored in `RuntimeVisibleAnnotations` attribute and parsed into `Annotation` proxy instances. `Method.invoke()` uses the JVM's dynamic dispatch mechanism, performing type checks and boxing/unboxing of arguments at runtime.

## Why This Concept Exists

Reflection exists to enable dynamic behavior in statically-typed languages. Frameworks like Spring need to discover and wire beans at runtime without compile-time knowledge of all classes. Testing frameworks need to invoke test methods and access private fields. Serialization libraries need to read arbitrary object fields. Without reflection, Java would require compile-time code generation or explicit configuration for every class interaction.

## Overview

Java Reflection provides runtime introspection of classes, methods, fields, and annotations. Key APIs: `Class`, `Method`, `Field`, `Constructor`, and `AnnotatedElement`. Annotations (`@Retention`, `@Target`) control metadata availability and placement. Compile-time annotation processors generate code during `javac`. Reflection enables frameworks but has performance and encapsulation trade-offs. The Java Module System (Java 9+) restricts reflective access, requiring explicit `--add-opens` flags.

## References

- [Java Reflection Tutorial (Oracle)](https://docs.oracle.com/javase/tutorial/reflect/)
- [Java Annotation Processing (Oracle)](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/package-summary.html)
- [Understanding Java Reflection](https://www.baeldung.com/java-reflection)
- [Custom Annotations in Java](https://www.baeldung.com/java-custom-annotation)
- [Java Module System and Reflection](https://blog.idrsolutions.com/java-9-module-system-and-reflection/)
- [Related: Spring Dependency Injection](https://docs.spring.io/spring-framework/reference/core/core-container/beans.html)
- [Related: Lombok Annotations](https://projectlombok.org/features/all)
- [Related: ByteBuddy](https://bytebuddy.net/)

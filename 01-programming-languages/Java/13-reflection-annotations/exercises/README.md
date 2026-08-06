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

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)

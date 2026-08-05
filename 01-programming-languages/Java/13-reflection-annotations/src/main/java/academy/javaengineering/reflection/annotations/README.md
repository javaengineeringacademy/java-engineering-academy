# Annotations Demo

## Overview
Demonstrates working with Java annotations including built-in annotations, custom annotations, and annotation processing.

## Key Concepts

### 1. Built-in Annotations
- `@Override` - Method overrides superclass method
- `@Deprecated` - Element is deprecated
- `@SuppressWarnings` - Suppress compiler warnings

### 2. Custom Annotations
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
    String value() default "";
    int priority() default 0;
}
```

### 3. Annotation Processing
```java
Annotation[] annotations = method.getAnnotations();
for (Annotation annotation : annotations) {
    if (annotation instanceof MyAnnotation) {
        MyAnnotation my = (MyAnnotation) annotation;
        System.out.println(my.value());
    }
}
```

## Code References
- `AnnotationsDemo.java` - Main demonstration class
- `AnnotationsDemoTest.java` - Unit tests

## Common Mistakes
1. Wrong retention policy (SOURCE vs RUNTIME)
2. Not specifying target element type
3. Forgetting default values in annotations
4. Not processing annotations at runtime

## Interview Questions
1. What are the retention policies for annotations?
2. How do you create a custom annotation?
3. How do you process annotations at runtime?
4. What are meta-annotations?

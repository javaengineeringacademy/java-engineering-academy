# Practices: Annotation Processing

## Exercise 1: Getter Generator

Create a @GenerateGetters annotation and a processor that generates getter methods.

```java
package academy.javaengineering.reflection.annotationprocessing.practices;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface GenerateGetters {}
```

Write a processor that reads fields and generates getFieldName() methods.

## Exercise 2: ToString Generator

Create a @GenerateToString annotation and a processor that generates toString().

```java
package academy.javaengineering.reflection.annotationprocessing.practices;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface GenerateToString {}
```

## Exercise 3: Immutable Builder

Create a processor that generates an immutable builder for a class annotated with @ImmutableBuilder.

```java
package academy.javaengineering.reflection.annotationprocessing.practices;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ImmutableBuilder {}
```

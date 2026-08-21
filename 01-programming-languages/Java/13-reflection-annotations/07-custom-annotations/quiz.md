# Quiz: Custom Annotations

## Question 1 (MCQ)
What are the three retention policies?
- A) SOURCE, CLASS, RUNTIME
- B) COMPILE, LOAD, EXECUTE
- C) PRE, POST, FINAL
- D) STATIC, DYNAMIC, NONE

**Answer: A**

---

## Question 2 (MCQ)
Which retention policy is the default?
- A) SOURCE
- B) CLASS
- C) RUNTIME
- D) None

**Answer: B**

---

## Question 3 (MCQ)
Can annotations have constructors?
- A) Yes, like regular classes
- B) No, annotations cannot have constructors
- C) Only with RUNTIME retention
- D) Only static constructors

**Answer: B**

---

## Question 4 (Code Output)
What does this print?

```java
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation { String value(); }

@MyAnnotation("Hello")
class MyClass {}

MyAnnotation ann = MyClass.class.getAnnotation(MyAnnotation.class);
System.out.println(ann.value());
```

**Answer:** Hello

---

## Question 5 (MCQ)
What does @Inherited do?
- A) Subclasses inherit the annotation
- B) The annotation can be applied to subclasses
- C) The annotation is inherited from interfaces
- D) The annotation is inherited from the package

**Answer: A**

---

## Question 6 (MCQ)
Which element types are allowed in annotations?
- A) Primitives, String, Class, Enum, Annotation, Array
- B) Only String and int
- C) Only String
- D) Any Java type

**Answer: A**

---

## Question 7 (MCQ)
What does @Target specify?
- A) When the annotation is available
- B) Where the annotation can be applied
- C) How the annotation is processed
- D) Who can use the annotation

**Answer: B**

---

## Question 8 (MCQ)
How do you read annotations at runtime?
- A) clazz.getAnnotation(Class)
- B) clazz.readAnnotation("name")
- C) Annotation.read(clazz)
- D) Reflect.getAnnotation(clazz)

**Answer: A**

---

## Question 9 (MCQ)
What is @Repeatable used for?
- A) The annotation can be applied multiple times
- B) The annotation is processed multiple times
- C) The annotation repeats at runtime
- D) The annotation is inherited multiple times

**Answer: A**

---

## Question 10 (MCQ)
Which retention policy is needed for runtime reflection?
- A) SOURCE
- B) CLASS
- C) RUNTIME
- D) Any

**Answer: C**

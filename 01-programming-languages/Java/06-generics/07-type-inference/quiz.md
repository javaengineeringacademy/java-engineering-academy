# Quiz: Type Inference

### Q1:
What does the diamond operator `<>` do?

A) Creates a raw type
B) Lets the compiler infer the type arguments from the left-hand side
C) Creates an unbounded wildcard type
D) Enables runtime type checking

**Answer:** B — The diamond operator allows the compiler to infer type arguments from the assignment context.

---

### Q2:
True or False: Java's type inference can determine the type of a generic method return value from the assignment context.

**Answer:** True — Target-type inference uses the assignment or parameter context to infer the return type.

---

### Q3:
What happens when you write `var list = new ArrayList<>()`?

A) `list` is `ArrayList<Object>`
B) Compile error — cannot infer type without target type on the right
C) `list` is `ArrayList<?>`
D) `list` is a raw `ArrayList`

**Answer:** B — `var` requires the compiler to infer from the right-hand side, but `<>` infers from the left — circular dependency causes an error.

---

### Q4:
How do you resolve type inference failure in a generic method call?

A) Add a type witness: `<String>myMethod()`
B) Cast the return value
C) Use raw types
D) Both A and B

**Answer:** D — Type witness or casting both resolve inference ambiguity.

---

### Q5:
Which is the preferred approach for creating a `HashMap` with inferred types?

A) `new HashMap<String, List<Integer>>()`
B) `new HashMap<>()`
C) `(HashMap<String, List<Integer>>) new HashMap()`
D) `HashMap.new()`

**Answer:** B — Diamond operator is preferred for verbosity reduction when type can be inferred.

---

### Q6:
When does `var` inference fail?

A) When initialized with a method call returning a generic type
B) When initialized with `null`
C) When used in a for-each loop
D) When the variable is a field

**Answer:** B — `var x = null` fails because the compiler cannot determine the type from `null`.

---

### Q7:
True or False: Type inference in Java is purely compile-time.

**Answer:** True — Type inference happens at compile time; at runtime, types are erased.

---

### Q8:
What is target-type inference?

A) Inferring the type of a target variable from its name
B) Using the expected type at the assignment/parameter site to infer generic types
C) Inferring types from exception targets
D) Targeting specific JVM instructions

**Answer:** B — Target-type inference uses the context (assignment, parameter) to determine the generic type.

---

### Q9:
Given:
```java
var numbers = List.of(1, 2, 3);
```
What is the inferred type of `numbers`?

A) `List<Integer>`
B) `List<Number>`
C) `List<Object>`
D) `List<?>`

**Answer:** A — `List.of(1, 2, 3)` infers `Integer` from the arguments, and `var` captures `List<Integer>`.

---

### Q10:
Can you use `var` with diamond operator in a constructor?

A) Yes, always
B) Only if the type is inferrable from the constructor arguments
C) No, never
D) Only for final variables

**Answer:** B — `var x = new ArrayList<>()` fails, but `var x = new ArrayList<String>()` works (explicit type args).

---

### Q11:
What Java version introduced the diamond operator?

A) JDK 5
B) JDK 7
C) JDK 8
D) JDK 10

**Answer:** B — The diamond operator `<>` was introduced in JDK 7 (JDK 126).

---

### Q12:
True or False: `var` can be used for method parameters in Java.

**Answer:** False (as of JDK 22) — `var` is only for local variables, not method parameters. It was proposed but not included.

---

### Q13:
When should you avoid using `var`?

A) When the type is obvious from context
B) When the inferred type is not clear (e.g., `var result = compute()`)
C) When working with streams
D) When using diamond operator

**Answer:** B — Avoid `var` when the type isn't obvious, as it reduces code readability.

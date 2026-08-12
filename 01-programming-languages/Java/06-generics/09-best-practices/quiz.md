# Quiz: Best Practices

### Q1:
What is the standard type parameter letter for collection elements?

A) `T`
B) `E`
C) `K`
D) `V`

**Answer:** B — `E` stands for Element and is the convention for collection type parameters.

---

### Q2:
According to PECS, if a method reads from a collection, which wildcard should you use?

A) `? super T`
B) `? extends T`
C) `<?>`
D) `<T>`

**Answer:** B — Producer Extends: if the collection produces items, use `? extends T`.

---

### Q3:
True or False: You should use wildcards in return types of public methods.

**Answer:** False — Wildcards in return types cause compile errors and lose type information for callers.

---

### Q4:
What does `K` stand for in generic naming conventions?

A) Key
B) Kind
C) Kotlin
D) Kernel

**Answer:** A — `K` is the convention for Map key type parameters.

---

### Q5:
When should you use a type parameter `<T>` instead of a wildcard?

A) When you only read from the collection
B) When you read AND write to the collection
C) When you don't care about the type
D) When you want maximum flexibility

**Answer:** B — Wildcards prevent writing; type parameters allow both reading and writing.

---

### Q6:
What is F-bounded polymorphism used for?

A) Forcing a type parameter to extend a specific class
B) Self-referential types like builders: `<T extends Builder<T>>`
C) Creating final generic classes
D) Bounding primitives in generics

**Answer:** B — F-bounded polymorphism enables self-referential generic types.

---

### Q7:
How many type parameters should a generic class typically have?

A) Exactly 1
B) 1-2, rarely more than 3
C) As many as needed, no limit
D) Exactly 2

**Answer:** B — More than 3 type parameters usually indicates the design needs simplification.

---

### Q8:
True or False: `? extends T` and `? super T` can be used in the same method signature.

**Answer:** True — e.g., `copy(List<? super T> dest, List<? extends T> src)` uses both.

---

### Q9:
Which naming convention is recommended for a third type parameter?

A) `T3`
B) `S`
C) `Third`
D) `Type3`

**Answer:** B — After `T`, use `U`, then `S` for additional general type parameters.

---

### Q10:
When is `<?>` (unbounded wildcard) appropriate?

A) When you need to read specific types
B) When you need to write to the collection
C) When you just iterate or print without caring about the type
D) When you need to store elements

**Answer:** C — Unbounded wildcards are for when the element type is irrelevant.

---

### Q11:
True or False: A method should use a wildcard if it needs to reference the type parameter later in the body.

**Answer:** False — If you need to reference the type, use a type parameter `<T>`, not a wildcard.

---

### Q12:
What is the recommended way to document type parameters in a public API?

A) No documentation needed
B) Use Javadoc `@param` tags explaining what each type parameter represents
C) Add inline comments only
D) Use README files

**Answer:** B — Javadoc `@param <T>` tags should explain each type parameter's purpose and constraints.

---

### Q13:
In the PECS pattern, what is a "consumer"?

A) A method that only reads from a generic type
B) A method that only writes to a generic type
C) A method that creates generic instances
D) A method that destroys generic instances

**Answer:** B — A consumer is something you put data into, hence `? super T`.

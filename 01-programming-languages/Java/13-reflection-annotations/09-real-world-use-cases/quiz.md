# Quiz: Real-World Use Cases

## Question 1 (MCQ)
How does Spring implement @Autowired?
- A) Compile-time code generation
- B) Reflection to scan for annotations and inject dependencies
- C) XML configuration only
- D) Bytecode manipulation

**Answer: B**

---

## Question 2 (MCQ)
Why does Lombok not use runtime reflection?
- A) It cannot access private fields
- B) It generates code at compile time for zero runtime cost
- C) It uses annotation processing at runtime
- D) It is impossible

**Answer: B**

---

## Question 3 (MCQ)
How does JUnit find and run @Test methods?
- A) Scans for methods ending in "test"
- B) Uses reflection to find methods annotated with @Test
- C) Requires explicit method names in config
- D) Uses compile-time code generation

**Answer: B**

---

## Question 4 (MCQ)
How does Spring create @Transactional proxies?
- A) JDK dynamic proxy or CGLIB
- B) Compile-time code generation
- C) Manual proxy classes
- D) Java Agent

**Answer: A**

---

## Question 5 (MCQ)
What is the advantage of compile-time annotation processing over runtime reflection?
- A) Faster startup, zero runtime reflection overhead
- B) More flexible
- C) Easier to implement
- D) Better error messages

**Answer: A**

---

## Question 6 (MCQ)
How does JPA map Java objects to database tables?
- A) Reads @Entity, @Column annotations via reflection
- B) Requires XML mapping files
- C) Uses compile-time code generation
- D) Manual SQL writing

**Answer: A**

---

## Question 7 (MCQ)
How does Jackson serialize objects to JSON?
- A) Reads field values via reflection
- B) Requires manual serialization code
- C) Uses XML mapping
- D) Only works with specific interfaces

**Answer: A**

---

## Question 8 (MCQ)
What is the performance impact of Spring DI at startup?
- A) Negligible — one-time cost at boot
- B) Severe — affects every request
- C) None — no reflection used
- D) Moderate — affects every method call

**Answer: A**

---

## Question 9 (MCQ)
How does Hibernate implement lazy loading?
- A) Creates proxies for collection fields
- B) Loads all data eagerly
- C) Uses compile-time code generation
- D) Requires manual proxy creation

**Answer: A**

---

## Question 10 (Scenario)
You are building a high-performance service that processes 100K requests/second. Which serialization approach is best?
- A) Jackson (reflection-based, cached)
- B) Manual serialization (no reflection)
- C) Lombok with Jackson (reflection for serialization)
- D) Both B and C are good choices

**Answer: D**

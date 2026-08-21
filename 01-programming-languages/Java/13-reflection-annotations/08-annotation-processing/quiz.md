# Quiz: Annotation Processing

## Question 1 (MCQ)
What is the difference between compile-time and runtime annotation processing?
- A) Compile-time generates code; runtime reads annotations via reflection
- B) They are identical
- C) Runtime is faster
- D) Compile-time only works with SOURCE retention

**Answer: A**

---

## Question 2 (MCQ)
What does AbstractProcessor.process() return?
- A) The generated code
- B) true to claim the annotations, false to pass to next processor
- C) void
- D) The number of annotations processed

**Answer: B**

---

## Question 3 (MCQ)
What is the Filer API used for?
- A) Reading annotations
- B) Creating new source, class, or resource files
- C) Reporting errors
- D) Finding elements

**Answer: B**

---

## Question 4 (MCQ)
How do you register an annotation processor?
- A) Via META-INF/services/javax.annotation.processing.Processor
- B) Via @Register annotation
- C) Via config.xml
- D) It is automatic

**Answer: A**

---

## Question 5 (MCQ)
What is a processing round?
- A) One call to the process() method for all matching annotations
- B) One compilation
- C) One test execution
- D) One runtime cycle

**Answer: A**

---

## Question 6 (MCQ)
What is the Messager API used for?
- A) Logging
- B) Reporting errors and warnings to the compiler
- C) Creating files
- D) Finding elements

**Answer: B**

---

## Question 7 (MCQ)
What is the default retention policy for annotations?
- A) SOURCE
- B) CLASS
- C) RUNTIME
- D) None

**Answer: B**

---

## Question 8 (MCQ)
How many times can annotation processing run?
- A) Exactly once
- B) Multiple rounds until no new annotations are generated
- C) Twice
- D) It depends on the processor

**Answer: B**

---

## Question 9 (MCQ)
What does @SupportedAnnotationTypes specify?
- A) The annotations this processor handles
- B) The return types of generated methods
- C) The target elements for the annotation
- D) The source version

**Answer: A**

---

## Question 10 (MCQ)
Where does generated source code typically go?
- A) src/main/java
- B) target/generated-sources
- C) build/annotations
- D) bin/generated

**Answer: B**

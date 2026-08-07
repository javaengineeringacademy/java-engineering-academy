# Garbage Collection Quiz

## Question 1
What happens when an object loses all references to it?

A) It is immediately destroyed
B) It becomes eligible for garbage collection
C) It throws a NullPointerException
D) It is moved to the Old Generation

**Answer: B**

---

## Question 2
Which of the following is NOT a GC algorithm?

A) G1
B) ZGC
C) CMS
D) DFS

**Answer: D**

---

## Question 3
What does `System.gc()` do?

A) Guarantees immediate garbage collection
B) Requests garbage collection (not guaranteed)
C) Disables garbage collection
D) Forces a Full GC

**Answer: B**

---

## Question 4
What is the purpose of the Young Generation?

A) Store long-lived objects
B) Store recently created objects
C) Store class metadata
D) Store JVM internals

**Answer: B**

---

## Question 5
Which GC algorithm is the default since Java 9?

A) Parallel GC
B) Serial GC
C) G1 GC
D) ZGC

**Answer: C**

---

## Question 6
What is a `WeakReference`?

A) A reference that prevents GC
B) A reference that is always strong
C) A reference that is cleared on next GC regardless of memory
D) A reference that survives all GC cycles

**Answer: C**

---

## Question 7
What does `-Xms` flag set?

A) Maximum heap size
B) Initial heap size
C) Young generation size
D) Stack size

**Answer: B**

---

## Question 8
What is the purpose of Metaspace?

A) Store object instances
B) Store class metadata
C) Store thread stacks
D) Store JIT compiled code

**Answer: B**

---

## Question 9
Which of these is a memory leak pattern?

A) Using try-with-resources
B) Static collection that grows unbounded
C) Using WeakHashMap
D) Using final fields

**Answer: B**

---

## Question 10
What is the purpose of `-XX:MaxGCPauseMillis`?

A) Set maximum heap size
B) Set target maximum GC pause time
C) Set GC thread count
D) Set young generation ratio

**Answer: B**

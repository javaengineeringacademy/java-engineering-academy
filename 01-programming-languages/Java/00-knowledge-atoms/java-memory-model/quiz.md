# Java Memory Model Quiz

## Question 1
What is the Java Memory Model (JMM)?

A) A physical memory layout specification
B) A formal specification defining how threads interact through memory
C) A garbage collection algorithm
D) A thread scheduling mechanism

**Answer: B**

---

## Question 2
Which variable type is thread-private?

A) Instance field
B) Static field
C) Local variable
D) Any object reference

**Answer: C**

---

## Question 3
What does `volatile` guarantee?

A) Atomicity of compound operations
B) Visibility and ordering (memory barriers)
C) Mutual exclusion
D) Thread safety for all operations

**Answer: B**

---

## Question 4
What happens-before rule applies to `synchronized` blocks?

A) Program Order Rule
B) Monitor Lock Rule
C) Volatile Variable Rule
D) Thread Start Rule

**Answer: B**

---

## Question 5
Why is `count++` not thread-safe even with `volatile`?

A) volatile is broken
B) count++ is a compound operation (read-modify-write)
C) JVM doesn't support volatile
D) volatile only works for boolean

**Answer: B**

---

## Question 6
What is safe publication?

A) Making objects immutable
B) Ensuring object state is visible when reference is published
C) Using final fields only
D) Avoiding null references

**Answer: B**

---

## Question 7
What is wrong with double-checked locking without volatile?

A) Performance issue
B) Reference may be seen partially constructed
C) It doesn't compile
D) It always throws exception

**Answer: B**

---

## Question 8
Which mechanism ensures both visibility and atomicity?

A) volatile
B) synchronized
C) final fields
D) WeakReference

**Answer: B**

---

## Question 9
What is the Thread Start Rule?

A) Thread.start() happens-before any action in started thread
B) Thread.run() happens-before Thread.start()
C) Thread.interrupt() happens-before Thread.join()
D) Thread.stop() happens-before Thread.start()

**Answer: A**

---

## Question 10
When are `final` fields guaranteed to be visible?

A) Always
B) After constructor completes (no premature publication)
C) Only with synchronized
D) Only with volatile

**Answer: B**

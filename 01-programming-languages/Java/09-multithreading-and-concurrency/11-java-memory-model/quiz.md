# Java Memory Model Quiz

## Question 1
What does the happens-before relationship guarantee?

- A) Order of execution
- B) Memory visibility of writes
- C) Atomicity of operations
- D) Thread scheduling order

**Answer: B**
Happens-before guarantees that writes by one thread are visible to reads by another thread when the happens-before relationship exists.

## Question 2
Why is volatile needed for a 64-bit variable?

- A) It's not needed
- B) JVM may split 64-bit reads/writes into two 32-bit operations
- C) For performance
- D) For atomicity

**Answer: B**
Without volatile, the JVM may perform two separate 32-bit reads/writes for long/double, causing word tearing.

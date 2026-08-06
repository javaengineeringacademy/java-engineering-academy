# Best Practices Quiz

## Question 1
What is the "measure before optimizing" principle in performance engineering?
- A) Always optimize code before measuring
- B) Use profiling tools to identify actual bottlenecks before making optimization changes
- C) Never optimize code
- D) Only optimize when users complain

**Answer: B**
**Explanation:** Premature optimization wastes time on non-bottlenecks. Profiling reveals the actual hotspots (CPU, memory, I/O) so you can focus optimization efforts where they have the most impact.

## Question 2
What is the purpose of connection pooling in application development?
- A) To create more database connections
- B) To reuse existing database connections instead of creating/destroying them repeatedly
- C) To encrypt database traffic
- D) To cache query results

**Answer: B**
**Explanation:** Connection pooling maintains a pool of reusable database connections. Instead of opening a new connection for each query (expensive), applications borrow and return connections, significantly improving performance.

## Question 3
Why is logging considered a best practice in production applications?
- A) It slows down the application
- B) It provides visibility into application behavior, errors, and performance for debugging and monitoring
- C) It's required by Java syntax
- D) It replaces the need for testing

**Answer: B**
**Explanation:** Proper logging provides a record of application behavior, helping diagnose issues, audit operations, monitor performance, and understand user behavior in production environments where debugging isn't possible.

## Question 4
What is the benefit of using try-with-resources over manual try-finally?
- A) It's less code
- B) It automatically closes resources and handles suppressed exceptions properly
- C) It prevents all exceptions
- D) It's only for file I/O

**Answer: B**
**Explanation:** try-with-resources automatically calls `close()` on resources implementing AutoCloseable, even if exceptions occur. It also properly chains suppressed exceptions, which manual try-finally blocks don't handle elegantly.

## Question 5
What is the SOLID principle?
- A) A set of database design rules
- B) Five design principles for writing maintainable, flexible, and scalable object-oriented code
- C) A testing methodology
- D) A code formatting standard

**Answer: B**
**Explanation:** SOLID stands for Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion. These principles guide the design of classes and systems that are easier to maintain, extend, and test.
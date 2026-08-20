# Performance Testing Quiz

## Question 1
What does JMH stand for?

- A) Java Microbenchmark Harness
- B) Java Memory Hierarchy
- C) Java Method Handler
- D) Java Multi-Host

**Answer: A**
**Explanation:** JMH is the Java Microbenchmark Harness for writing reliable benchmarks.

---

## Question 2
What does @BenchmarkMode do?

- A) Sets the benchmark algorithm
- B) Configures measurement mode
- C) Specifies output format
- D) Defines iteration count

**Answer: B**
**Explanation:** @BenchmarkMode configures how results are measured (Throughput, AverageTime).

---

## Question 3
Why is warmup important?

- A) Cleans memory
- B) Allows JIT compilation
- C) Initializes state
- D) Resets counters

**Answer: B**
- **Explanation:** Warmup allows JIT to compile and optimize code before measurement.

---

## Question 4
What is the purpose of @Fork?

- A) Create threads
- B) Fork new JVM instances
- C) Copy data
- D) Split benchmarks

**Answer: B**
**Explanation:** @Fork specifies how many JVM forks to use for isolation.

---

## Question 5
What does Blackhole do?

- A) Prevents JIT optimization
- B) Stores benchmark data
- C) Manages memory
- D) Handles concurrency

**Answer: A**
**Explanation:** Blackhole prevents dead code elimination by consuming values.

---

## Question 6
What is @State used for?

- A) Benchmark configuration
- B) Shared state across iterations
- C) Result storage
- D) Thread management

**Answer: B**
**Explanation:** @State defines shared state that persists across benchmark iterations.

---

## Question 7
What is the difference between Throughput and AverageTime?

- A) Throughput = ops/time, AverageTime = time/op
- B) Throughput = time/op, AverageTime = ops/time
- C) They are the same
- D) Neither is correct

**Answer: A**
**Explanation:** Throughput measures operations per time; AverageTime measures time per operation.

---

## Question 8
How many iterations should you run?

- A) 1
- B) 3-5
- C) 5-10
- D) 100+

**Answer: C**
**Explanation:** 5-10 iterations provide reliable results with statistical significance.

---

## Question 9
What does @Setup do?

- A) Initializes benchmark state
- B) Configures JMH
- C) Starts measurement
- D) Generates reports

**Answer: A**
**Explanation:** @Setup methods initialize state before benchmark execution.

---

## Question 10
When should you use JMH?

- A) Load testing
- B) Microbenchmarking
- C) Integration testing
- D) Unit testing

**Answer: B**
**Explanation:** JMH is designed for microbenchmarks measuring method-level performance.

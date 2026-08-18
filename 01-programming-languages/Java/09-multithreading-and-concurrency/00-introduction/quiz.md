# Introduction Quiz

## Question 1
What is the difference between a thread and a process?

- A) Threads share memory; processes do not
- B) Processes share memory; threads do not
- C) They are the same thing
- D) Threads are slower than processes

**Answer: A**
Threads within the same process share heap memory but have separate stacks. Processes have separate memory spaces.

## Question 2
What is the difference between concurrency and parallelism?

- A) They are the same thing
- B) Concurrency is multiple tasks making progress; parallelism is simultaneous execution
- C) Parallelism is multiple tasks making progress; concurrency is simultaneous execution
- D) Neither involves multiple tasks

**Answer: B**
Concurrency means tasks can be interleaved (even on one core). Parallelism requires multiple cores for true simultaneous execution.

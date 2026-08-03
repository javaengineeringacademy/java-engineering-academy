# Multithreading Quiz

## Quiz 1: Thread Basics

### Question 1
What is a thread?
- A) Lightweight process
- B) Heavy process
- C) Single process
- D) No process

**Answer: A**

### Question 2
What is the difference between Thread and Runnable?
- A) Thread is class, Runnable is interface
- B) Thread is interface, Runnable is class
- C) Both are classes
- D) Both are interfaces

**Answer: A**

### Question 3
How do you create a thread?
- A) extends Thread
- B) implements Runnable
- C) Both A and B
- D) Neither

**Answer: C**

### Question 4
What is the start() method?
- A) Creates new thread and calls run()
- B) Calls run() directly
- C) Creates new process
- D) Calls main() method

**Answer: A**

### Question 5
What is the difference between start() and run()?
- A) start() creates new thread, run() doesn't
- B) start() calls run(), run() creates new thread
- C) Both create new thread
- D) Both don't create new thread

**Answer: A**

---

## Quiz 2: Synchronization

### Question 1
What is synchronization?
- A) Controlling access to shared resources
- B) Creating new threads
- C) Deleting threads
- D) Starting threads

**Answer: A**

### Question 2
What is the synchronized keyword?
- A) Method or block can be accessed by one thread at a time
- B) Method or block can be accessed by multiple threads
- C) Method or block can't be accessed
- D) Method or block can be accessed by any thread

**Answer: A**

### Question 3
What is a deadlock?
- A) Two threads waiting for each other
- B) One thread waiting
- C) Multiple threads waiting
- D) No threads waiting

**Answer: A**

### Question 4
What is the volatile keyword?
- A) Variable is always read from main memory
- B) Variable is always read from cache
- C) Variable is always written to main memory
- D) Variable is always written to cache

**Answer: A**

### Question 5
What is the difference between synchronized and volatile?
- A) synchronized is for methods, volatile is for variables
- B) synchronized is for variables, volatile is for methods
- C) Both are for methods
- D) Both are for variables

**Answer: A**

---

## Quiz 3: Wait and Notify

### Question 1
What is the wait() method?
- A) Thread releases lock and waits
- B) Thread acquires lock and waits
- C) Thread releases lock and continues
- D) Thread acquires lock and continues

**Answer: A**

### Question 2
What is the notify() method?
- A) Wakes up one waiting thread
- B) Wakes up all waiting threads
- C) Puts thread to sleep
- D) Stops thread

**Answer: A**

### Question 3
What is the difference between wait() and sleep()?
- A) wait() releases lock, sleep() doesn't
- B) wait() doesn't release lock, sleep() does
- C) Both release lock
- D) Neither releases lock

**Answer: A**

### Question 4
Can we call wait() without synchronized block?
- A) No
- B) Yes
- C) Only in Java 8+
- D) Only with notify()

**Answer: A**

### Question 5
What is the difference between notify() and notifyAll()?
- A) notify() wakes one thread, notifyAll() wakes all
- B) notify() wakes all, notifyAll() wakes one
- C) Both wake one thread
- D) Both wake all threads

**Answer: A**

---

## Quiz 4: Executor Framework

### Question 1
What is Executor?
- A) Interface for executing tasks
- B) Class for executing tasks
- C) Thread for executing tasks
- D) Process for executing tasks

**Answer: A**

### Question 2
What is ExecutorService?
- A) Interface for managing threads
- B) Class for managing threads
- C) Thread for managing threads
- D) Process for managing threads

**Answer: A**

### Question 3
What is the difference between submit() and execute()?
- A) submit() returns Future, execute() doesn't
- B) submit() doesn't return Future, execute() does
- C) Both return Future
- D) Neither returns Future

**Answer: A**

### Question 4
What is a Thread Pool?
- A) Collection of reusable threads
- B) Single thread
- C) Multiple processes
- D) Single process

**Answer: A**

### Question 5
What is the benefit of using Thread Pool?
- A) Reduced overhead
- B) Better performance
- C) Both A and B
- D) Neither

**Answer: C**

---

## Quiz 5: Concurrent Collections

### Question 1
What is ConcurrentHashMap?
- A) Thread-safe HashMap
- B) Sorted HashMap
- C) Unordered HashMap
- D) Synchronized HashMap

**Answer: A**

### Question 2
What is CopyOnWriteArrayList?
- A) Thread-safe ArrayList
- B) Sorted ArrayList
- C) Unordered ArrayList
- D) Synchronized ArrayList

**Answer: A**

### Question 3
What is BlockingQueue?
- A) Queue that blocks when empty or full
- B) Queue that doesn't block
- C) Queue that blocks when empty
- D) Queue that blocks when full

**Answer: A**

### Question 4
What is the difference between BlockingQueue and Queue?
- A) BlockingQueue blocks, Queue doesn't
- B) BlockingQueue doesn't block, Queue blocks
- C) Both block
- D) Neither blocks

**Answer: A**

### Question 5
What is the benefit of using Concurrent Collections?
- A) Thread safety
- B) Better performance
- C) Both A and B
- D) Neither

**Answer: C**

---

## Score Sheet

| Quiz | Questions | Correct | Score |
|------|-----------|---------|-------|
| Thread Basics | 5 | /5 | % |
| Synchronization | 5 | /5 | % |
| Wait and Notify | 5 | /5 | % |
| Executor Framework | 5 | /5 | % |
| Concurrent Collections | 5 | /5 | % |
| **Total** | **25** | **/25** | **%** |

---

## Passing Score: 80% (20/25)

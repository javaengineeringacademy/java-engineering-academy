# ThreadLocal Quiz

## Question 1
What happens if you don't call remove() on ThreadLocal in a thread pool?

- A) Nothing
- B) Memory leak — value stays in thread's storage
- C) Exception
- D) Value is garbage collected

**Answer: B**
In thread pools, threads are reused. Previous ThreadLocal values persist until removed or the thread dies, causing memory leaks.

## Question 2
What is InheritableThreadLocal?

- A) ThreadLocal that is inherited from parent thread
- B) ThreadLocal that is synchronized
- C) ThreadLocal that is volatile
- D) ThreadLocal with default value

**Answer: A**
InheritableThreadLocal copies the parent thread's value to the child thread when the child is created.

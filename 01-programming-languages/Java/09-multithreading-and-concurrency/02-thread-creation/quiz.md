# Thread Creation Quiz

## Question 1
What is the difference between Runnable and Callable?

- A) They are the same
- B) Callable returns a value and can throw checked exceptions
- C) Runnable returns a value
- D) Callable cannot throw exceptions

**Answer: B**
Callable has a `call()` method that returns a value and can throw checked exceptions. Runnable's `run()` returns void.

## Question 2
What does `executor.submit(callable)` return?

- A) The result directly
- B) A Future object
- C) Nothing
- D) An ExecutorService

**Answer: B**
`submit()` returns a Future that can be used to retrieve the result or check for exceptions.

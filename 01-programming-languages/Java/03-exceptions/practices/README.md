# Exception Handling Practices

10 mixed-concept exercises combining multiple exception handling topics.

## Exercises

| # | Exercise | Concepts | Difficulty |
|---|----------|----------|------------|
| 1 | Basic Try-Catch | try-catch, multiple catch blocks, exception message | Easy |
| 2 | Multiple Catch | catch ordering, specific vs generic exceptions | Easy |
| 3 | Multi-Catch with Throw | multi-catch syntax, manual throw, exception selection | Medium |
| 4 | Finally Cleanup | finally block, resource release, conditional logic in finally | Medium |
| 5 | Try-with-Resources and Translation | TWR, exception translation, suppressed exceptions | Medium |
| 6 | Exception Chaining | cause chaining, getCause(), wrapped exceptions | Medium |
| 7 | Runtime Recovery | catching exceptions, fallback values, state restoration | Hard |
| 8 | Custom Hierarchy | custom exception classes, inheritance, checked vs unchecked | Hard |
| 9 | Retry Pattern | loops with exception handling, retry limits, exponential backoff | Hard |
| 10 | Global Handler | Thread.setDefaultUncaughtExceptionHandler, logging, shutdown hooks | Hard |

## How to Use

1. Open `Exception Practices.java`
2. Complete each exercise by implementing the TODO sections
3. Run `ExceptionSolutions.java` to verify your approach
4. Each solution includes comments explaining the exception handling technique

## Running Solutions

```bash
cd practices
javac "ExceptionSolutions.java"
java academy.javaengineering.exceptions.practices.ExceptionSolutions
```

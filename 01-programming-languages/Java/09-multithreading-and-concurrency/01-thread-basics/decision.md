# Thread Basics Decision Guide

## Extend Thread vs Implement Runnable

| Aspect | Extend Thread | Implement Runnable |
|--------|---------------|-------------------|
| Inheritance | Uses single inheritance | Allows extending other classes |
| Flexibility | Tightly coupled to Thread | Decoupled task from thread |
| Reusability | Task tied to thread | Task can run on any executor |
| Testability | Harder to test | Easy to test without threading |

**Rule:** Always prefer Runnable over extending Thread. It provides better separation of concerns and allows the task to be submitted to an ExecutorService.

## Daemon vs User Threads

| Aspect | User Thread | Daemon Thread |
|--------|-------------|---------------|
| JVM shutdown | Prevents shutdown | Allows shutdown |
| Use case | Main application work | Background tasks (logging, monitoring) |
| Reliability | Guaranteed to complete | May be terminated abruptly |

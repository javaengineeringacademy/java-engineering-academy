# Concurrency Exercises

## Exercise 1: Thread Creation
Create multiple threads that increment a shared counter.

```c
#include <stdio.h>
#include <pthread.h>

#define NUM_THREADS 4
#define INCREMENTS 1000000

int counter = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *increment(void *arg) {
    for (int i = 0; i < INCREMENTS; i++) {
        pthread_mutex_lock(&mutex);
        counter++;
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main(void) {
    pthread_t threads[NUM_THREADS];
    for (int i = 0; i < NUM_THREADS; i++)
        pthread_create(&threads[i], NULL, increment, NULL);
    for (int i = 0; i < NUM_THREADS; i++)
        pthread_join(threads[i], NULL);
    printf("Counter: %d\n", counter);
    return 0;
}
```

## Exercise 2: Producer-Consumer
Implement producer-consumer with condition variables.

## Exercise 3: Thread Pool
Create a simple thread pool with task queue.

## Exercise 4: Read-Write Lock
Implement a read-write lock for concurrent readers.

## Exercise 5: Atomic Stack
Implement a lock-free stack using atomic operations.

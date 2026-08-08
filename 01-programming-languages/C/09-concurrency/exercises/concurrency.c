/*
 * Exercise: Concurrency in C
 * Difficulty: ★★★★★ (5/5)
 * Learning Objectives:
 *   - Understand threads and the POSIX threads API
 *   - Practice mutexes and condition variables
 *   - Learn about race conditions and synchronization
 *   - Implement producer-consumer patterns
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

/* ============================================================
 * Problem 1: Basic Thread Creation
 *
 * Write a function that creates 3 threads, each printing its
 * thread ID and a message. The main thread should wait for
 * all threads to complete using pthread_join().
 * ============================================================ */
void *thread_func(void *arg) {
    /* TODO: Cast arg to int, print thread info, return NULL */
    (void)arg;
    return NULL;
}

void problem1_basic_threads(void) {
    /* TODO: Create 3 threads, join them all */
    printf("TODO: Problem 1 - Create and join threads\n\n");
}

/* ============================================================
 * Problem 2: Shared Counter with Mutex
 *
 * Create a shared counter incremented by multiple threads.
 * Use a mutex to prevent race conditions.
 * Without mutex, the count will be incorrect.
 * ============================================================ */
#define NUM_THREADS 4
#define INCREMENTS 100000

typedef struct {
    int counter;
    pthread_mutex_t mutex;
} SharedCounter;

void *increment_counter(void *arg) {
    /* TODO: Lock mutex, increment counter INCREMENTS times, unlock */
    (void)arg;
    return NULL;
}

void problem2_mutex(void) {
    /* TODO: Initialize counter, create threads, join, print result */
    printf("TODO: Problem 2 - Mutex-protected counter\n\n");
}

/* ============================================================
 * Problem 3: Producer-Consumer with Condition Variable
 *
 * Implement a bounded buffer (size 5) with:
 * - Producer thread adding items (0-19) with small delays
 * - Consumer thread removing items and printing them
 * - Use mutex + condition variables for synchronization
 * ============================================================ */
#define BUFFER_SIZE 5

typedef struct {
    int buffer[BUFFER_SIZE];
    int count;
    int in;
    int out;
    pthread_mutex_t mutex;
    pthread_cond_t not_full;
    pthread_cond_t not_empty;
    int done;  /* Signal producer is done */
} BoundedBuffer;

void *producer(void *arg) {
    /* TODO: Produce items 0-19, wait if buffer full, signal consumer */
    (void)arg;
    return NULL;
}

void *consumer(void *arg) {
    /* TODO: Consume items until done signal, wait if buffer empty */
    (void)arg;
    return NULL;
}

void problem3_producer_consumer(void) {
    /* TODO: Init buffer, create threads, join, cleanup */
    printf("TODO: Problem 3 - Producer-Consumer\n\n");
}

/* ============================================================
 * Problem 4: Reader-Writer Problem
 *
 * Multiple reader threads can read simultaneously.
 * Writer threads need exclusive access.
 * Use mutexes to coordinate readers and writers.
 * ============================================================ */
typedef struct {
    int data;
    int readers;
    pthread_mutex_t read_mutex;
    pthread_mutex_t write_mutex;
} SharedData;

void *reader(void *arg) {
    /* TODO: Increment readers, read data, decrement readers */
    (void)arg;
    return NULL;
}

void *writer(void *arg) {
    /* TODO: Wait for no readers, write data */
    (void)arg;
    return NULL;
}

void problem4_reader_writer(void) {
    /* TODO: Init shared data, create reader/writer threads */
    printf("TODO: Problem 4 - Reader-Writer\n\n");
}

/* ============================================================
 * Problem 5: Thread-Safe Queue
 *
 * Implement a thread-safe FIFO queue using a linked list.
 * Support enqueue, dequeue, and size operations.
 * Use mutex for thread safety.
 * ============================================================ */
typedef struct QueueNode {
    int data;
    struct QueueNode *next;
} QueueNode;

typedef struct {
    QueueNode *front;
    QueueNode *rear;
    int size;
    pthread_mutex_t mutex;
} ThreadSafeQueue;

void queue_init(ThreadSafeQueue *q) { /* TODO */ (void)q; }
void queue_enqueue(ThreadSafeQueue *q, int data) { /* TODO */ (void)q; (void)data; }
int queue_dequeue(ThreadSafeQueue *q) { /* TODO */ (void)q; return 0; }
int queue_size(ThreadSafeQueue *q) { /* TODO */ (void)q; return 0; }
void queue_destroy(ThreadSafeQueue *q) { /* TODO */ (void)q; }

void problem5_thread_safe_queue(void) {
    /* TODO: Init queue, create threads that enqueue/dequeue */
    printf("TODO: Problem 5 - Thread-Safe Queue\n\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Concurrency — Exercises\n");
    printf("====================================\n\n");

    problem1_basic_threads();
    problem2_mutex();
    problem3_producer_consumer();
    problem4_reader_writer();
    problem5_thread_safe_queue();

    return 0;
}

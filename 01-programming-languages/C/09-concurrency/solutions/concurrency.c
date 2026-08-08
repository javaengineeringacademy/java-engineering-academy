/*
 * Concurrency — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -pthread -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

/* ============================================================
 * Problem 1: Basic Thread Creation
 * ============================================================ */
void *thread_func(void *arg) {
    int id = *(int *)arg;
    printf("  Thread %d: Hello from thread (tid=%lu)\n", id, (unsigned long)pthread_self());
    return NULL;
}

void problem1_basic_threads(void) {
    printf("=== Problem 1: Basic Threads ===\n");

    pthread_t threads[3];
    int ids[3] = {1, 2, 3};

    for (int i = 0; i < 3; i++) {
        pthread_create(&threads[i], NULL, thread_func, &ids[i]);
    }

    for (int i = 0; i < 3; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("  All threads completed\n\n");
}

/* ============================================================
 * Problem 2: Shared Counter with Mutex
 * ============================================================ */
#define NUM_THREADS 4
#define INCREMENTS 100000

typedef struct {
    int counter;
    pthread_mutex_t mutex;
} SharedCounter;

void *increment_counter(void *arg) {
    SharedCounter *sc = (SharedCounter *)arg;
    for (int i = 0; i < INCREMENTS; i++) {
        pthread_mutex_lock(&sc->mutex);
        sc->counter++;
        pthread_mutex_unlock(&sc->mutex);
    }
    return NULL;
}

void problem2_mutex(void) {
    printf("=== Problem 2: Mutex Counter ===\n");

    SharedCounter sc = {0, PTHREAD_MUTEX_INITIALIZER};

    pthread_t threads[NUM_THREADS];
    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_create(&threads[i], NULL, increment_counter, &sc);
    }

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("  Expected: %d, Actual: %d\n", NUM_THREADS * INCREMENTS, sc.counter);
    pthread_mutex_destroy(&sc.mutex);
    printf("\n");
}

/* ============================================================
 * Problem 3: Producer-Consumer
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
    int done;
} BoundedBuffer;

void *producer(void *arg) {
    BoundedBuffer *bb = (BoundedBuffer *)arg;

    for (int i = 0; i < 20; i++) {
        pthread_mutex_lock(&bb->mutex);
        while (bb->count == BUFFER_SIZE) {
            pthread_cond_wait(&bb->not_full, &bb->mutex);
        }

        bb->buffer[bb->in] = i;
        bb->in = (bb->in + 1) % BUFFER_SIZE;
        bb->count++;
        printf("  Produced: %d (buffer count: %d)\n", i, bb->count);

        pthread_cond_signal(&bb->not_empty);
        pthread_mutex_unlock(&bb->mutex);
        usleep(1000);
    }

    pthread_mutex_lock(&bb->mutex);
    bb->done = 1;
    pthread_cond_broadcast(&bb->not_empty);
    pthread_mutex_unlock(&bb->mutex);

    return NULL;
}

void *consumer(void *arg) {
    BoundedBuffer *bb = (BoundedBuffer *)arg;

    while (1) {
        pthread_mutex_lock(&bb->mutex);
        while (bb->count == 0 && !bb->done) {
            pthread_cond_wait(&bb->not_empty, &bb->mutex);
        }

        if (bb->count == 0 && bb->done) {
            pthread_mutex_unlock(&bb->mutex);
            break;
        }

        int item = bb->buffer[bb->out];
        bb->out = (bb->out + 1) % BUFFER_SIZE;
        bb->count--;
        printf("  Consumed: %d (buffer count: %d)\n", item, bb->count);

        pthread_cond_signal(&bb->not_full);
        pthread_mutex_unlock(&bb->mutex);
    }

    return NULL;
}

void problem3_producer_consumer(void) {
    printf("=== Problem 3: Producer-Consumer ===\n");

    BoundedBuffer bb = {
        .count = 0, .in = 0, .out = 0, .done = 0,
        .mutex = PTHREAD_MUTEX_INITIALIZER,
        .not_full = PTHREAD_COND_INITIALIZER,
        .not_empty = PTHREAD_COND_INITIALIZER
    };

    pthread_t prod, cons;
    pthread_create(&prod, NULL, producer, &bb);
    pthread_create(&cons, NULL, consumer, &bb);

    pthread_join(prod, NULL);
    pthread_join(cons, NULL);

    pthread_mutex_destroy(&bb.mutex);
    pthread_cond_destroy(&bb.not_full);
    pthread_cond_destroy(&bb.not_empty);
    printf("\n");
}

/* ============================================================
 * Problem 4: Reader-Writer
 * ============================================================ */
typedef struct {
    int data;
    int readers;
    pthread_mutex_t read_mutex;
    pthread_mutex_t write_mutex;
} SharedData;

void *reader(void *arg) {
    SharedData *sd = (SharedData *)arg;

    for (int i = 0; i < 5; i++) {
        pthread_mutex_lock(&sd->read_mutex);
        sd->readers++;
        if (sd->readers == 1) pthread_mutex_lock(&sd->write_mutex);
        pthread_mutex_unlock(&sd->read_mutex);

        printf("  Reader: data = %d (readers: %d)\n", sd->data, sd->readers);

        pthread_mutex_lock(&sd->read_mutex);
        sd->readers--;
        if (sd->readers == 0) pthread_mutex_unlock(&sd->write_mutex);
        pthread_mutex_unlock(&sd->read_mutex);

        usleep(5000);
    }
    return NULL;
}

void *writer(void *arg) {
    SharedData *sd = (SharedData *)arg;

    for (int i = 0; i < 3; i++) {
        pthread_mutex_lock(&sd->write_mutex);
        sd->data = i * 100;
        printf("  Writer: wrote %d\n", sd->data);
        pthread_mutex_unlock(&sd->write_mutex);
        usleep(10000);
    }
    return NULL;
}

void problem4_reader_writer(void) {
    printf("=== Problem 4: Reader-Writer ===\n");

    SharedData sd = {0, 0, PTHREAD_MUTEX_INITIALIZER, PTHREAD_MUTEX_INITIALIZER};

    pthread_t readers[3], writers[2];
    for (int i = 0; i < 3; i++) pthread_create(&readers[i], NULL, reader, &sd);
    for (int i = 0; i < 2; i++) pthread_create(&writers[i], NULL, writer, &sd);

    for (int i = 0; i < 3; i++) pthread_join(readers[i], NULL);
    for (int i = 0; i < 2; i++) pthread_join(writers[i], NULL);

    pthread_mutex_destroy(&sd.read_mutex);
    pthread_mutex_destroy(&sd.write_mutex);
    printf("\n");
}

/* ============================================================
 * Problem 5: Thread-Safe Queue
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

void queue_init(ThreadSafeQueue *q) {
    q->front = q->rear = NULL;
    q->size = 0;
    pthread_mutex_init(&q->mutex, NULL);
}

void queue_enqueue(ThreadSafeQueue *q, int data) {
    QueueNode *node = malloc(sizeof(QueueNode));
    node->data = data;
    node->next = NULL;

    pthread_mutex_lock(&q->mutex);
    if (!q->rear) {
        q->front = q->rear = node;
    } else {
        q->rear->next = node;
        q->rear = node;
    }
    q->size++;
    pthread_mutex_unlock(&q->mutex);
}

int queue_dequeue(ThreadSafeQueue *q) {
    pthread_mutex_lock(&q->mutex);
    if (!q->front) {
        pthread_mutex_unlock(&q->mutex);
        return -1;
    }

    QueueNode *temp = q->front;
    int data = temp->data;
    q->front = temp->next;
    if (!q->front) q->rear = NULL;
    q->size--;

    pthread_mutex_unlock(&q->mutex);
    free(temp);
    return data;
}

int queue_size(ThreadSafeQueue *q) {
    pthread_mutex_lock(&q->mutex);
    int s = q->size;
    pthread_mutex_unlock(&q->mutex);
    return s;
}

void queue_destroy(ThreadSafeQueue *q) {
    while (queue_dequeue(q) != -1);
    pthread_mutex_destroy(&q->mutex);
}

typedef struct { ThreadSafeQueue *q; int thread_id; } ProducerArg;

void *queue_producer(void *arg) {
    ProducerArg *pa = (ProducerArg *)arg;
    for (int i = 0; i < 10; i++) {
        int val = pa->thread_id * 100 + i;
        queue_enqueue(pa->q, val);
        printf("  Producer %d enqueued: %d\n", pa->thread_id, val);
        usleep(1000);
    }
    return NULL;
}

void *queue_consumer(void *arg) {
    ThreadSafeQueue *q = (ThreadSafeQueue *)arg;
    int consumed = 0;
    while (consumed < 10) {
        int val = queue_dequeue(q);
        if (val != -1) {
            printf("  Consumer dequeued: %d\n", val);
            consumed++;
        }
        usleep(2000);
    }
    return NULL;
}

void problem5_thread_safe_queue(void) {
    printf("=== Problem 5: Thread-Safe Queue ===\n");

    ThreadSafeQueue q;
    queue_init(&q);

    ProducerArg pa1 = {&q, 1}, pa2 = {&q, 2};
    pthread_t p1, p2, c;
    pthread_create(&p1, NULL, queue_producer, &pa1);
    pthread_create(&p2, NULL, queue_producer, &pa2);
    pthread_create(&c, NULL, queue_consumer, &q);

    pthread_join(p1, NULL);
    pthread_join(p2, NULL);
    pthread_join(c, NULL);

    printf("  Final queue size: %d\n", queue_size(&q));
    queue_destroy(&q);
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Concurrency — Solutions\n");
    printf("====================================\n\n");

    problem1_basic_threads();
    problem2_mutex();
    problem3_producer_consumer();
    problem4_reader_writer();
    problem5_thread_safe_queue();

    return 0;
}

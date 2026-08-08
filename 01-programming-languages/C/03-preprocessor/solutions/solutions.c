/*
 * Preprocessor — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -DDEBUG -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Problem 1: Macro Safety — Safe MAX/MIN
 * ============================================================ */
#define MAX(a, b) ({     \
    __typeof__(a) _a = (a); \
    __typeof__(b) _b = (b); \
    _a > _b ? _a : _b;     \
})

#define MIN(a, b) ({     \
    __typeof__(a) _a = (a); \
    __typeof__(b) _b = (b); \
    _a < _b ? _a : _b;     \
})

void problem1_macros(void) {
    printf("=== Problem 1: Macro Safety ===\n");
    int i = 5, j = 10;
    int x = 0xFF, y = 0x10;

    printf("  MAX(2+3, 4*5) = %d\n", MAX(2+3, 4*5));
    printf("  MIN(2+3, 4*5) = %d\n", MIN(2+3, 4*5));

    int result = MAX(i++, j++);
    printf("  MAX(i++, j++) → result=%d, i=%d, j=%d\n", result, i, j);

    printf("  MAX(x & 0xFF, y | 0x10) = 0x%X\n", MAX(x & 0xFF, y | 0x10));
    printf("  Each argument evaluated exactly once via statement expressions.\n\n");
}

/* ============================================================
 * Problem 2: Type-Safe Container Macro
 * ============================================================ */
#define DECLARE_STACK(type, name, capacity) \
    static type name##_data[capacity];      \
    static int name##_size = 0;             \
    static inline int name##_push(type val) { \
        if (name##_size >= (capacity)) return -1; \
        name##_data[name##_size++] = val;   \
        return 0;                           \
    }                                       \
    static inline type name##_pop(void) {   \
        if (name##_size <= 0) return (type){0}; \
        return name##_data[--name##_size];  \
    }                                       \
    static inline type name##_peek(void) {  \
        if (name##_size <= 0) return (type){0}; \
        return name##_data[name##_size - 1];\
    }                                       \
    static inline int name##_empty(void) {  \
        return name##_size == 0;            \
    }

DECLARE_STACK(int, int_stack, 10)
DECLARE_STACK(float, float_stack, 10)

void problem2_container_macro(void) {
    printf("=== Problem 2: Container Macro ===\n");
    int_stack_push(10);
    int_stack_push(20);
    int_stack_push(30);
    printf("  int_stack peek: %d\n", int_stack_pop());
    printf("  int_stack peek: %d\n", int_stack_pop());

    float_stack_push(1.5f);
    float_stack_push(2.5f);
    printf("  float_stack peek: %.1f\n", float_stack_pop());
    printf("  DECLARE_STACK creates type-safe push/pop/peek functions.\n\n");
}

/* ============================================================
 * Problem 3: Debug Logging
 * ============================================================ */
typedef enum { LOG_ERROR, LOG_WARN, LOG_INFO, LOG_DEBUG } LogLevel;

static const char *level_names[] = {"ERROR", "WARN", "INFO", "DEBUG"};

#ifdef DEBUG
    #define debug_log(level, fmt, ...) \
        fprintf(stderr, "[%s] %s:%d: " fmt "\n", \
                level_names[level], __FILE__, __LINE__, ##__VA_ARGS__)
#else
    #define debug_log(level, fmt, ...) ((void)0)
#endif

void problem3_debug_logging(void) {
    printf("=== Problem 3: Debug Logging ===\n");
    debug_log(LOG_INFO, "Server started on port %d", 8080);
    debug_log(LOG_ERROR, "Connection failed: %s", "timeout");
    debug_log(LOG_DEBUG, "x = %d, y = %d", 42, 99);
    printf("  With -DDEBUG: logs appear. Without: zero overhead.\n\n");
}

/* ============================================================
 * Problem 4: Stringification and Token Pasting
 * ============================================================ */
#define STRINGIFY(x) #x
#define CONCAT(a, b) a##b

#define MAKE_STRUCT(name)                          \
    typedef struct {                               \
        int id;                                    \
        char name[50];                             \
    } name;                                        \
    name *create_##name(int id, const char *n) {   \
        name *p = malloc(sizeof(name));            \
        if (p) { p->id = id;                       \
        strncpy(p->name, n, 49); p->name[49]='\0';}\
        return p;                                  \
    }

MAKE_STRUCT(Student)
MAKE_STRUCT(Employee)

void problem4_stringification(void) {
    printf("=== Problem 4: Stringification & Token Pasting ===\n");
    printf("  STRINGIFY(123) = \"%s\"\n", STRINGIFY(123));
    printf("  STRINGIFY(hello world) = \"%s\"\n", STRINGIFY(hello world));

    int helloworld = 42;
    printf("  CONCAT(hello, world) = %d (token pasting)\n", CONCAT(hello, world));

    Student *s = create_Student(1, "Alice");
    Employee *e = create_Employee(100, "Bob");
    printf("  Student: id=%d name=%s\n", s->id, s->name);
    printf("  Employee: id=%d name=%s\n", e->id, e->name);
    free(s);
    free(e);
    printf("\n");
}

/* ============================================================
 * Problem 5: Include Guard Simulation
 * ============================================================ */

/* --- Content of mylib.h --- */
#ifndef MYLIB_H
#define MYLIB_H

#define MYLIB_VERSION "1.0.0"

#ifdef __cplusplus
extern "C" {
#endif

int add(int a, int b);

#ifdef __cplusplus
}
#endif

#endif /* MYLIB_H */

/* --- Content of mylib.c (implementation) --- */
int add(int a, int b) {
    return a + b;
}

void problem5_include_guards(void) {
    printf("=== Problem 5: Include Guards ===\n");
    printf("  mylib.h defines MYLIB_VERSION = \"%s\"\n", MYLIB_VERSION);
    printf("  add(3, 4) = %d\n", add(3, 4));
    printf("  Include guards prevent multiple inclusion of the same header.\n\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Preprocessor — Solutions\n");
    printf("====================================\n\n");

    problem1_macros();
    problem2_container_macro();
    problem3_debug_logging();
    problem4_stringification();
    problem5_include_guards();

    return 0;
}

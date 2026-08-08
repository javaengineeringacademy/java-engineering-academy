# Testing — C Language

## The Problem

C code has no runtime type safety, no bounds checking, and no garbage collector. Bugs manifest as crashes, memory corruption, or silent wrong answers. Without testing, you discover these bugs in production — when customers are affected, data is lost, or systems go down.

Testing catches bugs early, when they are cheap to fix. A bug found in development costs 1x to fix. In production, it costs 10-100x more (customer impact, data recovery, reputation damage).

## What It Is

Testing in C involves multiple strategies:

| Type | Purpose | Tool |
|------|---------|------|
| Unit tests | Verify individual functions | assert, Unity, Check |
| Integration tests | Verify component interaction | Custom test harnesses |
| Memory tests | Find leaks and corruption | Valgrind, AddressSanitizer |
| Fuzz tests | Find crashes with random input | AFL, libFuzzer |
| Performance tests | Verify performance requirements | Custom benchmarks |
| Static analysis | Find bugs without execution | Clang Static Analyzer, Cppcheck |

## Why It Exists

C testing exists because:
- C has no exceptions — errors are return values you might forget to check
- Memory bugs are silent — they corrupt data silently before crashing
- Undefined behavior is unpredictable — it works on your machine, crashes in production
- Regressions are common — changes break existing functionality

### Architecture: Testing Pyramid

```
        /\
       /  \        Fuzz Tests
      /    \       (find crashes)
     /------\
    /        \     Integration Tests
   /          \    (verify components work together)
  /------------\
 /              \  Unit Tests
/                \ (verify individual functions)
```

## Expanded Code Examples

### Unit Testing with assert

```c
#include <stdio.h>
#include <assert.h>
#include <string.h>

// Functions to test
int add(int a, int b) { return a + b; }
int factorial(int n) {
    if (n < 0) return -1;
    if (n <= 1) return 1;
    int result = 1;
    for (int i = 2; i <= n; i++) result *= i;
    return result;
}

// Test cases
void test_add(void) {
    assert(add(2, 3) == 5);
    assert(add(-1, 1) == 0);
    assert(add(0, 0) == 0);
    assert(add(-5, -3) == -8);
    printf("test_add: PASSED\n");
}

void test_factorial(void) {
    assert(factorial(0) == 1);
    assert(factorial(1) == 1);
    assert(factorial(5) == 120);
    assert(factorial(-1) == -1);  // Error case
    printf("test_factorial: PASSED\n");
}

int main(void) {
    test_add();
    test_factorial();
    printf("All tests passed!\n");
    return 0;
}
```

### Custom Test Framework

```c
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define TEST(name) static void name(void)
#define ASSERT_TRUE(expr) do { \
    if (!(expr)) { \
        fprintf(stderr, "FAIL: %s:%d: %s\n", __FILE__, __LINE__, #expr); \
        exit(1); \
    } \
} while(0)

#define ASSERT_EQ(a, b) do { \
    if ((a) != (b)) { \
        fprintf(stderr, "FAIL: %s:%d: %s == %s\n", __FILE__, __LINE__, #a, #b); \
        exit(1); \
    } \
} while(0)

#define ASSERT_STR_EQ(a, b) do { \
    if (strcmp((a), (b)) != 0) { \
        fprintf(stderr, "FAIL: %s:%d: \"%s\" == \"%s\"\n", __FILE__, __LINE__, (a), (b)); \
        exit(1); \
    } \
} while(0)

// Test functions
TEST(test_empty_string) {
    ASSERT_STR_EQ("", "");
}

TEST(test_string_length) {
    ASSERT_EQ(strlen("hello"), 5);
    ASSERT_EQ(strlen(""), 0);
}

TEST(test_string_copy) {
    char dest[32] = {0};
    strcpy(dest, "hello");
    ASSERT_STR_EQ(dest, "hello");
}

TEST(test_addition) {
    ASSERT_EQ(2 + 3, 5);
    ASSERT_EQ(-1 + 1, 0);
}

// Test runner
typedef struct {
    const char *name;
    void (*func)(void);
} TestCase;

TestCase tests[] = {
    {"empty_string", test_empty_string},
    {"string_length", test_string_length},
    {"string_copy", test_string_copy},
    {"addition", test_addition},
};

int main(void) {
    int passed = 0, failed = 0;
    int total = sizeof(tests) / sizeof(tests[0]);

    for (int i = 0; i < total; i++) {
        printf("Running: %s ... ", tests[i].name);
        tests[i].func();
        printf("PASSED\n");
        passed++;
    }

    printf("\n%d/%d tests passed\n", passed, total);
    return (failed > 0) ? 1 : 0;
}
```

### Memory Leak Testing

```c
// Compile with:
// gcc -g -fsanitize=address -o test_program test_program.c
// ./test_program
//
// Or use Valgrind:
// valgrind --leak-check=full --track-origins=yes ./test_program

#include <stdio.h>
#include <stdlib.h>

void leak(void) {
    int *p = malloc(100);  // Intentional leak for testing
    // No free — Valgrind will report this
}

void no_leak(void) {
    int *p = malloc(100);
    free(p);  // Correct
}

int main(void) {
    no_leak();
    // leak();  // Uncomment to see Valgrind report
    return 0;
}
```

### Fuzz Testing with AFL

```c
// fuzz_target.c — Compile with AFL
// afl-gcc -g -o fuzz_target fuzz_target.c
// afl-fin -i input/ -o output/ ./fuzz_target

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Target function to fuzz
int parse_header(const unsigned char *data, size_t len) {
    if (len < 4) return -1;
    if (data[0] != 0xDE || data[1] != 0xAD) return -2;

    unsigned int payload_len = (data[2] << 8) | data[3];
    if (payload_len > len - 4) return -3;  // Buffer overflow prevented

    // Process payload
    return 0;
}

int main(int argc, char **argv) {
    FILE *fp = fopen(argv[1], "rb");
    if (!fp) return 1;

    fseek(fp, 0, SEEK_END);
    size_t size = ftell(fp);
    rewind(fp);

    unsigned char *buf = malloc(size);
    if (!buf) { fclose(fp); return 1; }

    fread(buf, 1, size, fp);
    fclose(fp);

    int result = parse_header(buf, size);
    free(buf);

    return (result == 0) ? 0 : 1;
}
```

### Integration Testing

```c
#include <stdio.h>
#include <assert.h>
#include <string.h>

// Integration test: file write then read
void test_file_roundtrip(void) {
    const char *filename = "test_roundtrip.dat";

    // Write
    FILE *fp = fopen(filename, "wb");
    assert(fp != NULL);
    int values[] = {1, 2, 3, 4, 5};
    fwrite(values, sizeof(int), 5, fp);
    fclose(fp);

    // Read back
    fp = fopen(filename, "rb");
    assert(fp != NULL);
    int read_values[5];
    size_t n = fread(read_values, sizeof(int), 5, fp);
    assert(n == 5);
    fclose(fp);

    // Verify
    for (int i = 0; i < 5; i++) {
        assert(read_values[i] == values[i]);
    }

    remove(filename);
    printf("test_file_roundtrip: PASSED\n");
}

// Integration test: producer-consumer
void test_producer_consumer(void) {
    // Test that items produced are consumed in order
    // (Simplified — real test would use threads)
    int buffer[10];
    int count = 0;

    // Producer
    for (int i = 0; i < 10; i++) {
        buffer[count++] = i;
    }

    // Consumer
    for (int i = 0; i < 10; i++) {
        assert(buffer[i] == i);
    }

    printf("test_producer_consumer: PASSED\n");
}
```

## Production Incidents

### Incident 1: Unchecked Return Value

**Problem**: Program crashes intermittently on low-memory systems.

**Cause**: `malloc` return value not checked:

```c
void process(int n) {
    int *buf = malloc(n * sizeof(int));
    // buf might be NULL — no check
    for (int i = 0; i < n; i++) buf[i] = i;  // Crash
}
```

**Solution**: Test with memory pressure:

```c
void process(int n) {
    int *buf = malloc(n * sizeof(int));
    if (buf == NULL) {
        fprintf(stderr, "Allocation failed\n");
        return;
    }
    for (int i = 0; i < n; i++) buf[i] = i;
    free(buf);
}
```

**Testing**: Run under `ulimit -v` to simulate low memory, or use AddressSanitizer.

### Incident 2: Test Coverage Gap

**Problem**: Edge case in string parsing causes crash in production.

**Cause**: Tests only covered happy path, not empty strings:

```c
char *parse(const char *input) {
    // Assumes input is non-NULL and non-empty
    return strdup(input + 1);  // Crash on empty string
}
```

**Solution**: Test edge cases:

```c
void test_parse(void) {
    assert(parse("hello") != NULL);
    assert(parse("") != NULL);      // Edge case
    assert(parse(NULL) == NULL);    // NULL case
}
```

## Production Checklist

- [ ] Write tests for all public functions
- [ ] Test edge cases (NULL, empty, boundary values)
- [ ] Test error conditions (allocation failure, file not found)
- [ ] Run tests in CI/CD pipeline
- [ ] Run with Valgrind or AddressSanitizer
- [ ] Achieve reasonable code coverage (>80%)
- [ ] Write regression tests for every bug fixed
- [ ] Use fuzz testing for parsers and input handlers
- [ ] Test with different compilers and platforms
- [ ] Document test procedures

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Writes basic assert tests | Tests happy path |
| **Intermediate** | Uses test frameworks, tests edge cases | Tests error conditions |
| **Advanced** | Implements TDD, integration tests, fuzzing | CI/CD integration |
| **Expert** | Designs testable architectures, writes property-based tests | Mutation testing, fuzzing campaigns |

## Common Myths Debunked

1. **Myth**: Testing is optional in C
   **Truth**: C has no runtime safety net. Testing is the only way to catch bugs before production.

2. **Myth**: 100% code coverage means no bugs
   **Truth**: Coverage measures code paths, not correctness. You can have 100% coverage with wrong assertions.

3. **Myth**: Manual testing is sufficient
   **Truth**: Manual testing doesn't catch memory leaks, race conditions, or edge cases. Automated testing is essential.

4. **Myth**: Tests slow down development
   **Truth**: Tests catch bugs early, reducing debugging time. Long-term, tests speed up development.

## One-Minute Revision

| Concept | Description | Key Detail |
|---------|-------------|------------|
| Unit test | Test individual functions | Fast, focused |
| Integration test | Test component interaction | Verify interfaces |
| Assert | Verify expected condition | Aborts on failure |
| Coverage | Percentage of code tested | Don't chase 100% |
| TDD | Write tests before code | Drives design |
| Valgrind | Memory error detector | Finds leaks, corruption |
| AddressSanitizer | Runtime error detector | Faster than Valgrind |
| Fuzz testing | Random input testing | Finds crashes |

## Related Topics

- [Best Practices](../15-best-practices/README.md) — Writing testable code
- [Security](../11-security/README.md) — Security testing (fuzzing, static analysis)
- [Build Systems](../14-build-systems/README.md) — CI/CD integration

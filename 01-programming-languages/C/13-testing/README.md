# Testing — C Language

## Why It Matters

When you're building C code with no runtime type safety, no bounds checking, and no garbage collector, bugs manifest as crashes, memory corruption, or silent wrong answers. Without testing, you discover these in production — when customers are affected, data is lost, or systems go down. A bug found in development costs 1x to fix; in production, it costs 10-100x more due to customer impact, data recovery, and reputation damage.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | All production C code, especially parsers and input handlers | Manual testing only for throwaway scripts |
| When NOT to use | 100% coverage is not the goal — focus on critical paths | Coverage measures paths, not correctness |
| Alternatives | Property-based testing (QuickCheck), fuzzing campaigns | More thorough, more effort |
| Production Examples | SQLite (>99% branch coverage), Linux kernel (0day bots), OpenSSL | Fuzz testing catches real vulnerabilities |
| Common Mistakes | Testing only happy paths, not running under Valgrind/ASan | Test edge cases, NULL, empty, boundary values |

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

### Incident 3: Non-Deterministic Test Failure

**Problem**: A unit test fails intermittently, passing 99% of the time but failing occasionally in CI.

```c
void test_thread_counter(void) {
    int counter = 0;
    pthread_t threads[4];
    for (int i = 0; i < 4; i++)
        pthread_create(&threads[i], NULL, increment, &counter);
    for (int i = 0; i < 4; i++)
        pthread_join(threads[i], NULL);
    assert(counter == 4000000);  // Fails intermittently due to race condition
}
```

**Cause**: Race condition in test code — shared counter without synchronization.

**Impact**: Flaky tests erode confidence in test suite; developers ignore test failures.

**Solution**: Fix the race condition in the test:

```c
atomic_int counter = 0;

void *increment(void *arg) {
    for (int i = 0; i < 1000000; i++)
        atomic_fetch_add(&counter, 1);
    return NULL;
}

void test_thread_counter(void) {
    counter = 0;
    pthread_t threads[4];
    for (int i = 0; i < 4; i++)
        pthread_create(&threads[i], NULL, increment, NULL);
    for (int i = 0; i < 4; i++)
        pthread_join(threads[i], NULL);
    assert(atomic_load(&counter) == 4000000);
}
```

**Prevention**: Use ThreadSanitizer (`-fsanitize=thread`) to detect races; fix all data races in tests; make tests deterministic.

---

### Incident 4: Test Contamination Between Test Cases

**Problem**: Tests pass individually but fail when run together, indicating shared state between tests.

```c
static Database *db = NULL;

void setup(void) {
    if (db == NULL) db = db_open(":memory:");
    db_reset(db);  // Reset doesn't clear all state
}

void test_insert(void) {
    setup();
    db_insert(db, "user1", "Alice");
    assert(db_count(db) == 1);
}

void test_delete(void) {
    setup();
    db_insert(db, "user1", "Alice");
    db_delete(db, "user1");
    assert(db_count(db) == 0);  // Fails when run after test_insert
}
```

**Cause**: `db_reset` doesn't clear all state; previous test's data leaks into next test.

**Impact**: Tests are unreliable; order-dependent failures are hard to debug.

**Solution**: Create a fresh database for each test:

```c
void setup(void) {
    if (db) db_close(db);
    db = db_open(":memory:");  // Fresh database each time
}

void teardown(void) {
    if (db) { db_close(db); db = NULL; }
}
```

**Prevention**: Each test should be independent; create fresh resources in setup; clean up in teardown; run tests in random order.

---

### Incident 5: Missing Assert in Test

**Problem**: A test function runs but doesn't actually verify anything, giving false confidence.

```c
void test_parse_email(void) {
    char *result = parse_email("user@example.com");
    // No assert — test always passes
    printf("Result: %s\n", result);
}
```

**Cause**: Test function has no assertions; it runs without verifying the result.

**Impact**: Bug in `parse_email` goes undetected; test coverage numbers are inflated.

**Solution**: Add assertions to verify the result:

```c
void test_parse_email(void) {
    char *result = parse_email("user@example.com");
    assert(result != NULL);
    assert(strcmp(result, "user@example.com") == 0);
    free(result);
    
    // Test edge cases
    assert(parse_email(NULL) == NULL);
    assert(parse_email("") == NULL);
    assert(parse_email("invalid") == NULL);
}
```

**Prevention**: Every test must have at least one assertion; use static analysis to detect empty test functions; review test code in code review.

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

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Memory leaks in test code | Valgrind `--leak-check=full` | Run `valgrind --leak-check=full --error-exitcode=1 ./test`; exit code 1 on any leak |
| Test passes in debug but fails in release | Compare `-O0` vs `-O2` behavior | Compile with `-O2` and `-g`; undefined behavior may manifest differently at optimization levels |
| Flaky tests (non-deterministic failures) | Run under ThreadSanitizer | Compile with `-fsanitize=thread`; race conditions cause intermittent test failures |
| Edge case not covered by tests | Fuzz testing with AFL/libFuzzer | Feed random/mutated inputs to parser functions; crashes reveal untested code paths |
| Test framework assertions not descriptive | Custom assertion macros | Write macros that print file, line, expression, and actual/expected values on failure |

## Code Review Checklist

- [ ] Tests cover happy path, edge cases (NULL, empty, boundary), and error conditions
- [ ] Tests run with AddressSanitizer and Valgrind in CI/CD pipeline
- [ ] Fuzz tests included for all parsers and input handlers
- [ ] Tests are independent (no shared state between tests)
- [ ] Regression test written for every bug fixed
- [ ] Test output is clear (PASS/FAIL with descriptive messages)
- [ ] Tests run on multiple compilers and platforms

## Architecture Considerations

Testing in C is critical because there is no runtime safety net — no exceptions, no bounds checking, no garbage collector. The testing pyramid applies: many fast unit tests at the base, integration tests in the middle, and fuzz tests at the top for crash detection. Memory testing tools (Valgrind, AddressSanitizer) are not optional — they catch bugs that manifest silently in production.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Unit test + assert | Individual function verification | Fast, focused, but doesn't test integration |
| Fuzz testing (AFL/libFuzzer) | Parser and input handler robustness | Finds real crashes; requires seed corpus and coverage guidance |
| Property-based testing | Algorithm correctness verification | Tests invariants rather than specific cases; more thorough but harder to write |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Untrusted input causing crashes in production | Denial of service | Fuzz test all input handlers; run under AddressSanitizer |
| Memory leaks accumulating in long-running tests | Test environment exhaustion | Run Valgrind in CI/CD; fail build on any leak |
| Undefined behavior masked by test environment | Bugs only manifest on specific platforms/compilers | Test on multiple platforms; compile with `-fsanitize=undefined` |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `_Bool`, `snprintf` for safer test output | Use `snprintf` for test output formatting; adopt `stdbool.h` for test assertions |
| C99 → C11 | Added `_Static_assert` for compile-time test validation, `<stdatomic.h>` | Use `_Static_assert` to validate test data structure sizes; use atomics for concurrent test infrastructure |
| C11 → C23 | Added `typeof`, improved `_Generic` | Use `typeof` for type-generic test assertions; use `_Generic` for type-safe comparison macros |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| `assert` (runtime assertion) | C89 | Standard — use for internal invariants, not input validation |
| `_Static_assert` (compile-time assertion) | C11 | Standard — use for test data structure size validation |
| `<stdatomic.h>` for concurrent test infrastructure | C11 | Standard — use for thread-safe test counters |
| `typeof` for type-generic test macros | C23 (standardized) | Use for type-safe comparison and assertion macros |

## Interview Questions

1. **Why is testing more important in C than in managed languages?**: C has no runtime safety net — no exceptions, no bounds checking, no garbage collector. Bugs manifest as crashes, memory corruption, or silent wrong answers. Testing is the only way to catch these before production, where fixes cost 10-100x more.
2. **What is fuzz testing and when should you use it?**: Fuzz testing feeds random or mutated inputs to a program to find crashes, hangs, and memory errors. Use it for all parsers, protocol handlers, and input processing code. Tools like AFL and libFuzzer automatically generate test cases and track code coverage.
3. **How do you test for memory leaks in C?**: Use Valgrind (`valgrind --leak-check=full`) or AddressSanitizer (`-fsanitize=address`). Both track every allocation and report leaks at program exit. Run these in CI/CD and fail the build on any leak.
4. **What is the difference between `assert` and input validation?**: `assert` is for catching programmer errors (internal invariants) and is removed in release builds (`-DNDEBUG`). Input validation handles user/data errors and must always be present. Never use `assert` for input validation.
5. **How do you write testable C code?**: Separate interface from implementation (`.h` files), use dependency injection (pass function pointers for external dependencies), keep functions small and focused, avoid global state, and design for observability (return error codes, log diagnostic information).

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Secure Coding in C and CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)
- [Advanced Linux Programming (free)](https://www.advancedlinuxprogramming.com/)

## Overview

The Testing module covers unit testing, integration testing, memory testing, fuzz testing, and static analysis for C code. Without testing, you discover bugs in production — when customers are affected, data is lost, or systems go down.

## Learning Objectives

- Write unit tests with assert and custom frameworks
- Detect memory leaks with Valgrind and AddressSanitizer
- Apply fuzz testing with AFL and libFuzzer
- Use static analysis tools (Clang, Cppcheck)
- Achieve meaningful test coverage

## Prerequisites

- Completion of Module 12 (Performance)
- Understanding of C functions and pointers
- Basic debugging concepts

## History

- **1972** — assert macro in original C
- **1978** — K&R C documented debugging techniques
- **1989** — ANSI C standardized assert macro
- **1998** — Valgrind released for memory debugging
- **2004** — AFL released for fuzz testing
- **2011** — C11 added `_Static_assert` for compile-time checks

## Production Notes

- **Where is it used?** All production C code, especially parsers and input handlers
- **Why is it useful?** Prevents crashes, memory corruption, silent wrong answers
- **When should it be avoided?** 100% coverage is not the goal — focus on critical paths
- **Alternative?** Property-based testing (QuickCheck), fuzzing campaigns

## Core Concepts

### Testing Types

| Type | Purpose | Tool |
|------|---------|------|
| Unit tests | Verify individual functions | assert, Unity, Check |
| Integration tests | Verify component interaction | Custom test harnesses |
| Memory tests | Find leaks and corruption | Valgrind, AddressSanitizer |
| Fuzz tests | Find crashes with random input | AFL, libFuzzer |
| Performance tests | Verify performance requirements | Custom benchmarks |
| Static analysis | Find bugs without execution | Clang Static Analyzer, Cppcheck |

### Testing Pyramid

```
        ┌─────────┐
        │  E2E    │  Few, slow, high confidence
        ├─────────┤
        │Integration│  Medium, moderate speed
        ├─────────┤
        │  Unit   │  Many, fast, low confidence
        └─────────┘
```

## Internal Working

### Valgrind Workflow

```
valgrind --leak-check=full ./program
    ↓
Heap summary: allocated, freed, leaked
    ↓
If leaks > 0: investigate and fix
```

### Fuzz Testing Workflow

```
AFL fuzzer
    ↓
Mutates input randomly
    ↓
Executes program
    ↓
Monitors for crashes
    ↓
Reports crashing inputs
```

## Syntax

```c
// assert macro
#include <assert.h>
#include <stdio.h>

int add(int a, int b) { return a + b; }

int main(void) {
    assert(add(2, 3) == 5);
    assert(add(-1, 1) == 0);
    assert(add(0, 0) == 0);
    printf("All tests passed\n");
    return 0;
}

// Custom test framework
#define TEST(name) void name(void)
#define ASSERT(expr) do { \
    if (!(expr)) { \
        fprintf(stderr, "FAIL: %s:%d: %s\n", __FILE__, __LINE__, #expr); \
        return 1; \
    } \
} while(0)

// Memory leak detection
// valgrind --leak-check=full ./program
// Or compile with -fsanitize=address
```

## Examples

### Easy Example: Basic Assert

```c
#include <assert.h>
#include <stdio.h>

int max(int a, int b) {
    return (a > b) ? a : b;
}

int main(void) {
    assert(max(1, 2) == 2);
    assert(max(2, 1) == 2);
    assert(max(1, 1) == 1);
    assert(max(-1, -2) == -1);
    printf("All tests passed\n");
    return 0;
}
```

### Medium Example: Test Framework

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    const char *name;
    int (*test)(void);
} TestCase;

#define TEST(name) int test_##name(void)
#define ASSERT_EQ(a, b) do { \
    if ((a) != (b)) { \
        fprintf(stderr, "FAIL: %s:%d: %d != %d\n", __FILE__, __LINE__, (a), (b)); \
        return 1; \
    } \
} while(0)

int run_tests(TestCase *tests, int count) {
    int passed = 0, failed = 0;
    for (int i = 0; i < count; i++) {
        printf("Running %s... ", tests[i].name);
        if (tests[i].test() == 0) {
            printf("PASSED\n");
            passed++;
        } else {
            printf("FAILED\n");
            failed++;
        }
    }
    printf("\n%d passed, %d failed\n", passed, failed);
    return failed > 0 ? 1 : 0;
}
```

### Hard Example: Memory Leak Test

```c
#include <stdio.h>
#include <stdlib.h>

// Run with: valgrind --leak-check=full ./test
// Or compile with: gcc -fsanitize=address -g test.c

void leak_memory(void) {
    int *p = malloc(100);  // Intentional leak for testing
    // Missing free(p);
}

void no_leak(void) {
    int *p = malloc(100);
    free(p);  // Properly freed
}

int main(void) {
    printf("Testing memory leaks...\n");
    // leak_memory();  // Uncomment to see Valgrind report
    no_leak();
    printf("Done\n");
    return 0;
}
```

### Enterprise Example: Fuzz Testing Harness

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// libFuzzer harness
int LLVMFuzzerTestOneInput(const uint8_t *data, size_t size) {
    if (size < 4) return 0;
    
    // Parse input
    int value = *(int *)data;
    
    // Use value in some logic
    if (value > 0) {
        // Do something
    }
    
    return 0;
}

// AFL harness
int main(int argc, char **argv) {
    FILE *f = fopen(argv[1], "r");
    if (!f) return 1;
    
    fseek(f, 0, SEEK_END);
    long size = ftell(f);
    rewind(f);
    
    char *buffer = malloc(size);
    fread(buffer, 1, size, f);
    fclose(f);
    
    LLVMFuzzerTestOneInput((const uint8_t *)buffer, size);
    free(buffer);
    return 0;
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Test speed | Fast feedback loop | Run unit tests frequently |
| Valgrind overhead | 5-20x slowdown | Use selectively, not in CI |
| ASan overhead | 2-5x slowdown | Use in development |
| Fuzz testing | Time-consuming | Use continuous fuzzing |
| Coverage | Meaningless without context | Focus on critical paths |

## Best Practices

- Do:
  - Test edge cases (NULL, empty, boundary values)
  - Run tests under Valgrind/ASan
  - Use continuous integration
  - Test error paths, not just happy paths
  - Document test expectations
  
- Don't:
  - Only test happy paths
  - Ignore memory leaks
  - Assume 100% coverage means correctness
  - Skip tests in production code
  - Use assert for runtime errors (use error handling)

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Testing only happy paths | Missed edge cases | Test NULL, empty, boundary |
| Ignoring memory leaks | Resource exhaustion | Run Valgrind regularly |
| Using assert for errors | Crashes in production | Use error handling |
| Not testing error paths | Undetected bugs | Test all code paths |
| Skipping integration tests | Undetected interactions | Test component interactions |

## Interview Questions

### Q1: What is the difference between unit and integration tests?
**Answer:** Unit tests verify individual functions in isolation. Integration tests verify components working together.

### Q2: What is Valgrind?
**Answer:** A memory debugging tool that detects memory leaks, use-after-free, and other memory errors. Run with `valgrind ./program`.

### Q3: What is AddressSanitizer?
**Answer:** A compiler feature that detects memory errors at runtime. Compile with `-fsanitize=address`. Faster than Valgrind.

### Q4: What is fuzz testing?
**Answer:** Testing with random inputs to find crashes and vulnerabilities. Tools: AFL, libFuzzer.

### Q5: What is the difference between `assert` and error handling?
**Answer:** `assert`: debugging aid, removed in release builds. Error handling: runtime error recovery, always active.

### Q6: What is static analysis?
**Answer:** Analyzing code without executing it to find bugs. Tools: Clang Static Analyzer, Cppcheck, Coverity.

### Q7: What is the difference between code coverage and test quality?
**Answer:** Code coverage measures which lines are executed. Test quality measures whether tests actually verify correctness.

### Q8: What is the difference between `assert` and `_Static_assert`?
**Answer:** `assert`: runtime check, can be disabled. `_Static_assert`: compile-time check, always active.

### Q9: What is the purpose of `__attribute__((constructor))`?
**Answer:** Runs a function before `main()`. Useful for test setup.

### Q10: What is the difference between `fprintf(stderr)` and `fprintf(stdout)`?
**Answer:** `stderr`: unbuffered, always displayed. `stdout`: buffered, may be delayed. Use `stderr` for error messages.

### Q11: What is the difference between `exit(0)` and `return 0` from `main`?
**Answer:** `exit(0)`: terminates program, calls `atexit` handlers. `return 0`: equivalent to `exit(0)` from `main`.

### Q12: What is the difference between `gdb` and `valgrind`?
**Answer:** `gdb`: debugger, step through code. `valgrind`: memory debugger, detect leaks and errors.

### Q13: What is the difference between `make check` and `make test`?
**Answer:** Both run tests, but `make check` is more common in GNU projects. Implementation varies.

### Q14: What is the difference between `CUnit` and `Unity`?
**Answer:** Both are unit testing frameworks for C. `CUnit`: more features, larger. `Unity`: simpler, embedded-friendly.

### Q15: What is the purpose of `catchsegv`?
**Answer:** Catches segmentation faults and prints a backtrace. Useful for debugging crashes.

## Cross-References

- **Previous Module:** [12 - Performance](../12-performance/)
- **Next Module:** [14 - Build Systems](../14-build-systems/)
- **Related:** [11 - Security](../11-security/) — Security testing
- **Related:** [15 - Best Practices](../15-best-practices/) — Coding standards
- **External:** [Valgrind](https://valgrind.org/)
- **External:** [AFL Fuzzer](https://lcamtuf.coredump.cx/afl/)

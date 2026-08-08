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

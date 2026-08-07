# Testing — C Language

## What it is
Testing involves verifying that code works correctly and meets requirements.

## Why it exists
To catch bugs early, ensure reliability, and enable confident refactoring.

## When to use it
Always. Test-driven development leads to better code quality.

## How it works

### Unit Testing

```c
#include <assert.h>

void test_add(void) {
    assert(add(2, 3) == 5);
    assert(add(-1, 1) == 0);
    assert(add(0, 0) == 0);
}

int main(void) {
    test_add();
    printf("All tests passed\n");
    return 0;
}
```

### Test Frameworks

```c
// Using a simple framework
#define TEST(name) void name(void)
#define ASSERT_EQ(a, b) do { \
    if ((a) != (b)) { \
        fprintf(stderr, "FAIL: %s:%d: %s != %s\n", \
                __FILE__, __LINE__, #a, #b); \
        return 1; \
    } \
} while(0)
```

### Integration Testing

```c
void test_file_operations(void) {
    FILE *fp = fopen("test.txt", "w");
    assert(fp != NULL);
    fprintf(fp, "test data\n");
    fclose(fp);

    fp = fopen("test.txt", "r");
    assert(fp != NULL);
    char buffer[100];
    fgets(buffer, sizeof(buffer), fp);
    assert(strcmp(buffer, "test data\n") == 0);
    fclose(fp);
    remove("test.txt");
}
```

### Memory Leak Testing

```bash
valgrind --leak-check=full ./test_program
```

## Production Checklist

- [ ] Write tests for all functions
- [ ] Test edge cases
- [ ] Test error conditions
- [ ] Run tests in CI/CD
- [ ] Use memory checking tools

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Writes basic assert tests |
| Intermediate | Uses test frameworks |
| Advanced | Implements TDD and integration tests |

## Common Myths

1. **Myth**: Testing is optional
   **Truth**: Testing is essential for reliable software

2. **Myth**: 100% coverage means no bugs
   **Truth**: Coverage measures code, not edge cases

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Unit test | Test individual functions |
| Integration test | Test component interaction |
| Assertion | Verify expected conditions |
| Coverage | Percentage of code tested |
| TDD | Write tests before code |
| Valgrind | Memory error detector |

## Related Topics

- [Best Practices](../15-best-practices/README.md)
- [Security](../11-security/README.md)

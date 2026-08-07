# Testing Exercises

## Exercise 1: Unit Test Suite
Write unit tests for a math library.

```c
#include <stdio.h>
#include <math.h>
#include <assert.h>

void test_add(void) {
    assert(add(2, 3) == 5);
    assert(add(-1, 1) == 0);
    printf("test_add passed\n");
}

void test_multiply(void) {
    assert(multiply(2, 3) == 6);
    assert(multiply(0, 5) == 0);
    printf("test_multiply passed\n");
}

int main(void) {
    test_add();
    test_multiply();
    printf("All tests passed\n");
    return 0;
}
```

## Exercise 2: Test Framework
Create a simple test framework with assertions.

## Exercise 3: Edge Case Testing
Write tests for edge cases in string functions.

## Exercise 4: Memory Leak Test
Use valgrind to detect memory leaks.

## Exercise 5: Integration Test
Write integration tests for file I/O functions.

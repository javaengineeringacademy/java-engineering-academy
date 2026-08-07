# Security Exercises

## Exercise 1: Safe String Copy
Implement a safe string copy function with bounds checking.

```c
#include <stdio.h>
#include <string.h>

size_t safe_strcpy(char *dest, const char *src, size_t dest_size) {
    if (dest_size == 0) return 0;
    size_t i;
    for (i = 0; i < dest_size - 1 && src[i] != '\0'; i++) {
        dest[i] = src[i];
    }
    dest[i] = '\0';
    return i;
}
```

## Exercise 2: Input Validation
Create a function that validates integer input.

## Exercise 3: Memory Safety Wrapper
Implement safe malloc with error handling.

## Exercise 4: Buffer Overflow Detector
Write code that detects potential buffer overflows.

## Exercise 5: Secure Password Hashing
Implement a simple password hashing function.

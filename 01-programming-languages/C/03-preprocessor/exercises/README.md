# Preprocessor Exercises

## Exercise 1: Include Guards
Create a header file with proper include guards.

```c
#ifndef MYHEADER_H
#define MYHEADER_H

int add(int a, int b);

#endif
```

## Exercise 2: Safe Macros
Write a safe macro for finding the minimum of two values.

```c
#define MIN(a, b) ((a) < (b) ? (a) : (b))
```

## Exercise 3: Debug Macro
Create a debug macro that prints file, line, and function.

```c
#define DEBUG printf("DEBUG %s:%d in %s\n", __FILE__, __LINE__, __func__)
```

## Exercise 4: Platform Detection
Write conditional compilation for different operating systems.

## Exercise 5: Static Assert
Implement a compile-time assertion using macros.

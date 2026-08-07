# Knowledge Atoms Exercises

## Exercise 1: Hello World Compilation
Write a simple Hello World program and compile it step by step.

```c
#include <stdio.h>

int main(void) {
    printf("Hello, World!\n");
    return 0;
}
```

Compile with: `gcc -E hello.c -o hello.i` (preprocess only)
Then: `gcc -S hello.i -o hello.s` (compile to assembly)
Then: `gcc -c hello.s -o hello.o` (assemble to object)
Then: `gcc hello.o -o hello` (link)

## Exercise 2: Type Sizes
Write a program that prints the size of all basic types.

## Exercise 3: Memory Layout
Use `size` command to examine the memory layout of a compiled program.

# Knowledge Atoms Quiz

## Questions

### MCQ

**1.** What are the four stages of C compilation in order?

A) Compilation, Preprocessing, Assembly, Linking
B) Preprocessing, Compilation, Assembly, Linking
C) Preprocessing, Assembly, Compilation, Linking
D) Linking, Compilation, Assembly, Preprocessing

**2.** What is the memory management model in C?

A) Automatic garbage collection
B) Reference counting
C) Manual memory management (malloc/free)
D) Automatic with optional manual control

**3.** What header provides the `printf` function declaration?

A) `stdlib.h`
B) `string.h`
C) `stdio.h`
D) `math.h`

### Code Output

**4.** What is the output of this code?

```c
#define SQUARE(x) x * x
int main(void) {
    printf("%d\n", SQUARE(3 + 1));
    return 0;
}
```

**5.** What does this code print?

```c
#include <stdio.h>
int main(void) {
    int a = 5;
    int *p = &a;
    printf("%p %d\n", (void *)p, *p);
    return 0;
}
```

### Bug Finding

**6.** Find the bug in this macro:

```c
#define MAX(a, b) a > b ? a : b
int x = MAX(1+2, 3+4);
```

Explain the bug and provide the corrected macro.

**7.** What is wrong with this code?

```c
int *get_value(void) {
    int local = 42;
    return &local;
}
```

### Scenario

**8.** You are compiling a C program and get this linker error:

```
undefined reference to `calculate_total'
```

The function `calculate_total` is declared in `utils.h` and defined in `utils.c`. Both files are in the same directory. List three possible causes and their fixes.

**9.** You write portable C code that must work on both Linux (64-bit, GCC) and an embedded ARM Cortex-M4 (32-bit). You need a variable that is always exactly 32 bits. What type do you use, what header do you include, and why?

**10.** You are debugging a production server that randomly crashes. Valgrind reports "invalid read of size 4" in a function that accesses an array. The array is allocated with `malloc(n * sizeof(int))` where `n` comes from user input. Identify the likely vulnerability, explain how it could be exploited, and describe two mitigations.

## Answers

### 1. Answer: B

Preprocessing, Compilation, Assembly, Linking. The preprocessor handles `#include`, `#define`, and conditional compilation. The compiler translates preprocessed C to assembly. The assembler converts assembly to object files. The linker combines object files into an executable.

### 2. Answer: C

C uses manual memory management. The programmer must explicitly allocate (`malloc`, `calloc`, `realloc`) and free (`free`) memory. There is no garbage collector. This gives full control but requires discipline to avoid leaks, dangling pointers, and double frees.

### 3. Answer: C

`stdio.h` (Standard Input/Output) declares `printf`, `scanf`, `fopen`, `fclose`, and other I/O functions. It also defines `NULL`, `FILE`, and EOF.

### 4. Answer: 7 (not 16)

The macro expands to `3 + 1 * 3 + 1 = 3 + 3 + 1 = 7`. Due to operator precedence, multiplication happens before addition. The corrected macro wraps arguments in parentheses: `#define SQUARE(x) ((x) * (x))` which gives `((3+1) * (3+1)) = 16`.

### 5. Answer: `<address> 42` (address varies)

The code stores the address of `a` in pointer `p`, then prints the address (in hex) and the value `42` dereferenced through `p`. The exact address depends on the system.

### 6. Answer: Missing parentheses around macro arguments

The macro expands to `1+2 > 3+4 ? 1+2 : 3+4`. Due to precedence, `>` binds tighter than `+`, so it becomes `1 + (2 > 3) + 4 ? 1+2 : 3+4 = 1 + 0 + 4 ? 3 : 7 = 5 ? 3 : 7 = 3`. Corrected:

```c
#define MAX(a, b) ((a) > (b) ? (a) : (b))
```

### 7. Answer: Returns pointer to local (stack) variable

The variable `local` is allocated on the stack. When `get_value()` returns, its stack frame is destroyed. The returned pointer becomes a dangling pointer — dereferencing it is undefined behavior. Fix: allocate on the heap with `malloc`, use a `static` local, or have the caller provide a buffer.

### 8. Answer: Three possible causes

1. **Missing source file in compilation**: `utils.c` was not compiled or linked. Fix: `gcc main.c utils.c -o program`.
2. **Function name mismatch**: The function might be named differently in `utils.c` (e.g., `calculateTotal` vs `calculate_total`). Fix: check the exact function name or use `nm utils.o` to list symbols.
3. **Function declared but not defined**: `utils.h` declares the function but `utils.c` doesn't implement it. Fix: add the function body in `utils.c`.
4. **Link order issue** (less common): If linking with a static library, the library must come after the object files that reference it. Fix: `gcc main.o -lutils`.

### 9. Answer: Use `int32_t` from `<stdint.h>`

Include `<stdint.h>` and use `int32_t`. This guarantees exactly 32 bits on both platforms. The `<stdint.h>` header provides fixed-width types: `int8_t`, `int16_t`, `int32_t`, `int64_t` (and unsigned variants). Using `int` is not portable — it could be 16 bits on the embedded ARM or 64 bits on other systems. Using `long` is also unreliable — it's 32-bit on Windows 64-bit but 64-bit on Linux 64-bit.

### 10. Answer: Buffer overflow via unchecked user input

**Vulnerability**: If `n` is very large (e.g., `n = 0x7FFFFFFF`), `n * sizeof(int)` can overflow to a small value, causing `malloc` to allocate a tiny buffer. The subsequent loop writes past the allocated memory, corrupting the heap.

**Exploitation**: An attacker controls `n`, causing a heap buffer overflow. By carefully crafting the overflow, they can overwrite adjacent heap metadata or function pointers to redirect execution to shellcode.

**Mitigations**:
1. **Validate `n` before allocation**: Check that `n <= MAX_REASONABLE_SIZE` and that `n * sizeof(int)` does not overflow (use `if (n > SIZE_MAX / sizeof(int)) return -1;`).
2. **Use compiler sanitizers**: Compile with `-fsanitize=undefined,address` to detect overflows at runtime. Use AddressSanitizer (ASan) in production builds for early detection.

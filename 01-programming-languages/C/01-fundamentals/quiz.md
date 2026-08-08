# Fundamentals Quiz

## Questions

### MCQ

**1.** What is the result of `5 / 2` in C?

A) 2.5
B) 2
C) 3
D) Undefined

**2.** Which of the following is the correct way to find the number of elements in an array `int arr[10]`?

A) `sizeof(arr)`
B) `sizeof(arr) / sizeof(arr[0])`
C) `sizeof(arr) / sizeof(int *)`
D) `sizeof(arr) / sizeof(int)`

**3.** What marks the end of a C string?

A) `\n` (newline)
B) `\0` (null character)
C) `EOF`
D) `0x00` only when using `char[]`

### Code Output

**4.** What does this code print?

```c
int arr[] = {1, 2, 3, 4, 5};
int *p = arr;
printf("%d %d %d\n", *p, *(p + 2), p[3]);
```

**5.** What is the output of this code?

```c
char str[] = "Hello";
char *ptr = str;
*ptr = 'h';
printf("%s\n", str);
```

**6.** What does this code print?

```c
int x = 5;
int *p = &x;
int **pp = &p;
**pp = 10;
printf("x = %d, *p = %d\n", x, *p);
```

### Bug Finding

**7.** Find the bug in this function:

```c
void copy_string(char *dest, const char *src) {
    while (*src != '\0') {
        *dest = *src;
        dest++;
        src++;
    }
    // Missing something — what?
}
```

What will happen if this function is used? Fix the bug.

**8.** This function has undefined behavior. Explain why:

```c
int *get_value(void) {
    int val = 42;
    return &val;
}
```

### Scenario

**9.** You are writing a function `int find_max(int *arr, int size)` that returns the maximum value in an array. A junior developer wrote this:

```c
int find_max(int *arr, int size) {
    int max = 0;
    for (int i = 0; i < size; i++) {
        if (arr[i] > max) max = arr[i];
    }
    return max;
}
```

Identify THREE bugs or issues with this implementation and fix them.

**10.** You have two strings: `char a[] = "abc"` and `char b[] = "abc"`. You write `if (a == b)`. What happens and why? How should you correctly compare them?

## Answers

### 1. Answer: B

In C, `5 / 2` performs integer division (both operands are `int`), which truncates toward zero, giving `2`. To get floating-point division, at least one operand must be a float: `5.0 / 2` or `5 / 2.0` gives `2.5`.

### 2. Answer: B

`sizeof(arr)` gives total bytes (40 on a 32-bit int system). `sizeof(arr[0])` gives bytes per element (4). Dividing gives element count (10). This works only for actual arrays, not pointers (arrays decay to pointers when passed to functions).

### 3. Answer: B

C strings are null-terminated character arrays. The `\0` (null character, value 0) marks the end. Functions like `strlen`, `printf("%s")`, and `strcpy` all rely on this terminator. A string literal `"abc"` automatically includes the null terminator.

### 4. Answer: 1 3 4

- `*p` dereferences `p` (points to `arr[0]`) → `1`
- `*(p + 2)` advances pointer by 2 ints → `arr[2]` → `3`
- `p[3]` is equivalent to `*(p + 3)` → `arr[3]` → `4`

### 5. Answer: `hello`

`ptr` points to the first character of `str`. `*ptr = 'h'` changes `'H'` to `'h'`. Since `str` is a modifiable array (not a string literal), this is valid. The array becomes `{'h', 'e', 'l', 'l', 'o', '\0'}`.

### 6. Answer: `x = 10, *p = 10`

`pp` points to `p`, and `p` points to `x`. `**pp = 10` dereferences twice: `pp → p → x`, setting `x = 10`. Since `p` still points to `x`, `*p` is also `10`.

### 7. Answer: Missing null terminator

The function copies characters but doesn't add `\0` at the end of `dest`. The resulting string will not be null-terminated, causing `printf("%s")` and other string functions to read past the buffer (undefined behavior). Fix:

```c
void copy_string(char *dest, const char *src) {
    while (*src != '\0') {
        *dest = *src;
        dest++;
        src++;
    }
    *dest = '\0';  // Add null terminator
}
```

### 8. Answer: Returns pointer to local (stack) variable

`val` is allocated on the stack. When `get_value()` returns, its stack frame is destroyed. The returned pointer becomes a dangling pointer — `*p` on the next line is undefined behavior. The value might happen to be `42` by luck, or anything else. Fix: use `malloc`, `static`, or pass a pointer parameter.

### 9. Answer: Three bugs

1. **`max = 0` is wrong for all-negative arrays**: If the array is `{-5, -3, -8}`, the function returns `0` (not in the array). Fix: initialize `max = arr[0]`.

2. **No null/size check**: If `arr` is `NULL` or `size <= 0`, accessing `arr[i]` is undefined behavior. Fix: add `if (arr == NULL || size <= 0) return 0;` or handle error.

3. **`int` may overflow for very large sums** (if this were a sum function): Not applicable here, but a general issue. For max specifically, the main issue is the initial value.

Fixed version:

```c
int find_max(int *arr, int size) {
    if (arr == NULL || size <= 0) {
        fprintf(stderr, "Invalid input\n");
        return INT_MIN;  // Signal error
    }
    int max = arr[0];
    for (int i = 1; i < size; i++) {
        if (arr[i] > max) max = arr[i];
    }
    return max;
}
```

### 10. Answer: `a == b` compares pointer addresses, not string content

`a` and `b` are arrays that decay to pointers when used in expressions. `a == b` compares the **memory addresses** of the arrays, not their contents. Since `a` and `b` are separate arrays at different addresses, the result is always `false` (0), even though they contain the same content.

To compare string content, use `strcmp(a, b)` which returns `0` for equal strings, or write a manual character-by-character comparison:

```c
#include <string.h>
if (strcmp(a, b) == 0) {
    // Strings are equal
}
```

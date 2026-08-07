# Strings — C Language

## What it is
Strings are arrays of characters terminated by a null character ('\0').

## Why it exists
To work with text data in programs.

## When to use it
Whenever you need to process or display text.

## How it works

### String Declaration

```c
char str1[] = "Hello";           // Array syntax
char *str2 = "World";            // Pointer syntax
char str3[6] = {'H', 'e', 'l', 'l', 'o', '\0'}; // Explicit
```

### String Functions (string.h)

```c
#include <string.h>

strlen(str)          // Length (excluding '\0')
strcpy(dest, src)    // Copy
strcat(dest, src)    // Concatenate
strcmp(s1, s2)       // Compare
strchr(str, c)       // Find character
strstr(str, sub)     // Find substring
```

### String Input/Output

```c
char name[50];
scanf("%49s", name);         // Read word
fgets(name, 50, stdin);     // Read line
printf("%s\n", name);       // Print
```

### String Arrays

```c
const char *fruits[] = {
    "Apple",
    "Banana",
    "Cherry"
};
```

### String Manipulation

```c
// Manual copy
void str_copy(char *dest, const char *src) {
    while (*src) {
        *dest++ = *src++;
    }
    *dest = '\0';
}
```

## Production Checklist

- [ ] Always allocate sufficient buffer size
- [ ] Use strncpy instead of strcpy
- [ ] Always null-terminate strings
- [ ] Check for buffer overflow
- [ ] Use const for string literals

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses string literals and printf |
| Intermediate | Manipulates strings with functions |
| Advanced | Implements custom string functions |

## Common Myths

1. **Myth**: Strings are a built-in type
   **Truth**: Strings are just char arrays with a null terminator

2. **Myth**: "Hello" == "Hello" works
   **Truth**: This compares pointers, not content; use strcmp

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Declaration | char str[] or char *str |
| Null terminator | '\0' marks end |
| Length | strlen() |
| Copy | strcpy() |
| Concatenate | strcat() |
| Compare | strcmp() |
| Input | fgets() |
| Output | printf() |

## Related Topics

- [Arrays](../05-arrays/README.md)
- [Pointers](../07-pointers/README.md)

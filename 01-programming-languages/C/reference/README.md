# C Standard Library Reference

## Data Types

| Type | Size | Range |
|------|------|-------|
| `char` | 1 byte | -128 to 127 |
| `unsigned char` | 1 byte | 0 to 255 |
| `short` | 2 bytes | -32768 to 32767 |
| `unsigned short` | 2 bytes | 0 to 65535 |
| `int` | 4 bytes | -2^31 to 2^31-1 |
| `unsigned int` | 4 bytes | 0 to 2^32-1 |
| `long` | 4-8 bytes | Platform dependent |
| `long long` | 8 bytes | -2^63 to 2^63-1 |
| `float` | 4 bytes | 3.4e-38 to 3.4e+38 |
| `double` | 8 bytes | 1.7e-308 to 1.7e+308 |

---

## String Functions (`<string.h>`)

| Function | Prototype | Description |
|----------|-----------|-------------|
| `strlen` | `size_t strlen(const char *s)` | Returns length of string (excluding `\0`) |
| `strcpy` | `char *strcpy(char *dest, const char *src)` | Copies string src to dest |
| `strncpy` | `char *strncpy(char *dest, const char *src, size_t n)` | Copies n bytes from src to dest |
| `strcat` | `char *strcat(char *dest, const char *src)` | Appends src to dest |
| `strncat` | `char *strncat(char *dest, const char *src, size_t n)` | Appends n bytes from src to dest |
| `strcmp` | `int strcmp(const char *s1, const char *s2)` | Compares two strings |
| `strncmp` | `int strncmp(const char *s1, const char *s2, size_t n)` | Compares n bytes of two strings |
| `strchr` | `char *strchr(const char *s, int c)` | Finds first occurrence of c in s |
| `strrchr` | `char *strrchr(const char *s, int c)` | Finds last occurrence of c in s |
| `strstr` | `char *strstr(const char *haystack, const char *needle)` | Finds first occurrence of needle in haystack |
| `strtok` | `char *strtok(char *str, const char *delim)` | Tokenizes string |
| `memcpy` | `void *memcpy(void *dest, const void *src, size_t n)` | Copies n bytes from src to dest |
| `memmove` | `void *memmove(void *dest, const void *src, size_t n)` | Copies n bytes (handles overlap) |
| `memcmp` | `int memcmp(const void *s1, const void *s2, size_t n)` | Compares n bytes |
| `memset` | `void *memset(void *s, int c, size_t n)` | Sets n bytes to value c |

### Code Examples

```c
#include <string.h>
#include <stdio.h>

int main() {
    char src[] = "Hello, World!";
    char dest[20];

    // strlen - get string length
    printf("Length: %zu\n", strlen(src));  // 13

    // strcpy - copy string
    strcpy(dest, src);
    printf("Copied: %s\n", dest);  // Hello, World!

    // strncpy - copy n bytes
    char buf[6];
    strncpy(buf, src, 5);
    buf[5] = '\0';  // Always null-terminate manually!
    printf("Partial: %s\n", buf);  // Hello

    // strcat - concatenate strings
    char greeting[30] = "Hello";
    strcat(greeting, " World");
    printf("Concatenated: %s\n", greeting);  // Hello World

    // strcmp - compare strings
    int result = strcmp("abc", "abd");
    printf("Compare result: %d\n", result);  // Negative (abc < abd)

    // strchr - find character
    char *pos = strchr(src, 'W');
    if (pos) printf("Found 'W' at index: %ld\n", pos - src);  // 7

    // strstr - find substring
    char *sub = strstr(src, "World");
    if (sub) printf("Found substring: %s\n", sub);  // World!

    // memset - fill memory
    char buffer[10];
    memset(buffer, 'A', 9);
    buffer[9] = '\0';
    printf("Filled: %s\n", buffer);  // AAAAAAAAA

    return 0;
}
```

### Common Mistakes

```c
// WRONG: Buffer overflow - no bounds checking
char dest[5];
strcpy(dest, "Hello World!");  // Overflow!

// RIGHT: Use strncpy with proper bounds
char dest[5];
strncpy(dest, "Hello World!", sizeof(dest) - 1);
dest[sizeof(dest) - 1] = '\0';

// WRONG: Forgetting null terminator after strncpy
char buf[6];
strncpy(buf, "Hello", 5);
// buf might not be null-terminated!

// RIGHT: Always ensure null termination
buf[5] = '\0';

// WRONG: Using strcmp with NULL
strcmp(NULL, "test");  // Undefined behavior!

// WRONG: Modifying string literal
char *str = "Hello";
str[0] = 'h';  // Segfault! String literals are const
```

---

## Memory Functions (`<stdlib.h>`)

| Function | Prototype | Description |
|----------|-----------|-------------|
| `malloc` | `void *malloc(size_t size)` | Allocates size bytes (uninitialized) |
| `calloc` | `void *calloc(size_t nmemb, size_t size)` | Allocates zero-initialized memory |
| `realloc` | `void *realloc(void *ptr, size_t size)` | Resizes allocation |
| `free` | `void free(void *ptr)` | Deallocates memory |
| `alloca` | `void *alloca(size_t size)` | Allocates on stack (auto-freed) |

### Code Examples

```c
#include <stdlib.h>
#include <stdio.h>

int main() {
    // malloc - allocate single block
    int *arr = malloc(5 * sizeof(int));
    if (arr == NULL) {
        fprintf(stderr, "malloc failed\n");
        return 1;
    }
    for (int i = 0; i < 5; i++) arr[i] = i * 10;
    free(arr);

    // calloc - allocate zero-initialized block
    int *zeros = calloc(5, sizeof(int));
    // All elements are 0
    free(zeros);

    // realloc - resize allocation
    int *buf = malloc(2 * sizeof(int));
    buf[0] = 1; buf[1] = 2;

    int *newbuf = realloc(buf, 5 * sizeof(int));
    if (newbuf == NULL) {
        free(buf);  // Original still valid on failure
        return 1;
    }
    buf = newbuf;  // Update pointer
    newbuf[2] = 3;  // New elements are uninitialized
    free(buf);

    // Dynamic 2D array
    int rows = 3, cols = 4;
    int **matrix = malloc(rows * sizeof(int *));
    for (int i = 0; i < rows; i++) {
        matrix[i] = calloc(cols, sizeof(int));
    }
    matrix[1][2] = 42;  // Set element

    // Free 2D array
    for (int i = 0; i < rows; i++) free(matrix[i]);
    free(matrix);

    return 0;
}
```

### Common Mistakes

```c
// WRONG: Not checking malloc return value
int *p = malloc(100 * sizeof(int));
*p = 5;  // Crash if malloc failed!

// RIGHT: Always check return value
int *p = malloc(100 * sizeof(int));
if (p == NULL) {
    fprintf(stderr, "Out of memory\n");
    exit(1);
}

// WRONG: Using freed memory (use-after-free)
free(ptr);
*ptr = 10;  // Undefined behavior!

// WRONG: Double free
free(ptr);
free(ptr);  // Undefined behavior!

// WRONG: Memory leak
int *p = malloc(100);
// ... never called free(p)

// RIGHT: Always pair malloc/free
int *p = malloc(100);
if (p) free(p);

// WRONG: Using memset on non-byte types
int arr[5];
memset(arr, 1, sizeof(arr));  // Sets to 0x01010101, not 1!
```

---

## File Functions (`<stdio.h>`)

| Function | Prototype | Description |
|----------|-----------|-------------|
| `fopen` | `FILE *fopen(const char *path, const char *mode)` | Opens file |
| `fclose` | `int fclose(FILE *stream)` | Closes file |
| `fread` | `size_t fread(void *ptr, size_t size, size_t nmemb, FILE *stream)` | Reads block |
| `fwrite` | `size_t fwrite(const void *ptr, size_t size, size_t nmemb, FILE *stream)` | Writes block |
| `fprintf` | `int fprintf(FILE *stream, const char *format, ...)` | Formatted write |
| `fscanf` | `int fscanf(FILE *stream, const char *format, ...)` | Formatted read |
| `fgets` | `char *fgets(char *s, int size, FILE *stream)` | Reads line |
| `fputs` | `int fputs(const char *s, FILE *stream)` | Writes string |
| `fseek` | `int fseek(FILE *stream, long offset, int whence)` | Seeks position |
| `ftell` | `long ftell(FILE *stream)` | Gets current position |
| `rewind` | `void rewind(FILE *stream)` | Rewinds to beginning |
| `feof` | `int feof(FILE *stream)` | Tests end-of-file |
| `ferror` | `int ferror(FILE *stream)` | Tests error indicator |
| `printf` | `int printf(const char *format, ...)` | Formatted stdout |
| `scanf` | `int scanf(const char *format, ...)` | Formatted stdin |

### File Open Modes

| Mode | Description |
|------|-------------|
| `"r"` | Read (file must exist) |
| `"w"` | Write (creates/truncates) |
| `"a"` | Append (creates if needed) |
| `"r+"` | Read+Write (file must exist) |
| `"w+"` | Read+Write (creates/truncates) |
| `"a+"` | Read+Append (creates if needed) |
| `"rb"` | Read binary |
| `"wb"` | Write binary |
| `"ab"` | Append binary |

### Code Examples

```c
#include <stdio.h>
#include <stdlib.h>

int main() {
    // Text file I/O
    FILE *fp = fopen("example.txt", "w");
    if (fp == NULL) {
        perror("fopen failed");
        return 1;
    }
    fprintf(fp, "Hello %s\n", "World");
    fclose(fp);

    // Read text file
    fp = fopen("example.txt", "r");
    char line[256];
    while (fgets(line, sizeof(line), fp)) {
        printf("Line: %s", line);
    }
    fclose(fp);

    // Binary file I/O
    typedef struct {
        int id;
        double value;
    } Record;

    Record data[] = {{1, 3.14}, {2, 2.71}, {3, 1.618}};

    // Write binary
    fp = fopen("data.bin", "wb");
    fwrite(data, sizeof(Record), 3, fp);
    fclose(fp);

    // Read binary
    Record read_data[3];
    fp = fopen("data.bin", "rb");
    fread(read_data, sizeof(Record), 3, fp);
    fclose(fp);

    // File positioning
    fp = fopen("example.txt", "r");
    fseek(fp, 0, SEEK_END);  // Move to end
    long size = ftell(fp);   // Get file size
    printf("File size: %ld bytes\n", size);
    rewind(fp);              // Back to start
    fclose(fp);

    return 0;
}
```

### Common Mistakes

```c
// WRONG: Not checking fopen return
FILE *fp = fopen("file.txt", "r");
fread(buf, 1, 100, fp);  // Crash if file doesn't exist!

// RIGHT: Always check fopen
FILE *fp = fopen("file.txt", "r");
if (fp == NULL) {
    perror("Error opening file");
    return 1;
}

// WRONG: Using scanf for file input
scanf("%d", &x);  // Reads from stdin, not file!

// RIGHT: Use fscanf for file input
fscanf(fp, "%d", &x);

// WRONG: Forgetting to close file
FILE *fp = fopen("file.txt", "w");
fprintf(fp, "data");
// fp never closed - resource leak!

// RIGHT: Always close files
FILE *fp = fopen("file.txt", "w");
fprintf(fp, "data");
fclose(fp);
```

---

## Math Functions (`<math.h>`)

| Function | Prototype | Description |
|----------|-----------|-------------|
| `sqrt` | `double sqrt(double x)` | Square root |
| `cbrt` | `double cbrt(double x)` | Cube root |
| `pow` | `double pow(double base, double exp)` | Power |
| `exp` | `double exp(double x)` | e^x |
| `log` | `double log(double x)` | Natural logarithm |
| `log10` | `double log10(double x)` | Base-10 logarithm |
| `abs` | `int abs(int x)` | Absolute value (int) |
| `fabs` | `double fabs(double x)` | Absolute value (double) |
| `floor` | `double floor(double x)` | Round down |
| `ceil` | `double ceil(double x)` | Round up |
| `round` | `double round(double x)` | Round to nearest |
| `fmod` | `double fmod(double x, double y)` | Floating-point remainder |
| `sin` | `double sin(double x)` | Sine (radians) |
| `cos` | `double cos(double x)` | Cosine (radians) |
| `tan` | `double tan(double x)` | Tangent (radians) |
| `atan2` | `double atan2(double y, double x)` | Arc tangent of y/x |
| `fmax` | `double fmax(double x, double y)` | Maximum |
| `fmin` | `double fmin(double x, double y)` | Minimum |
| `trunc` | `double trunc(double x)` | Truncate toward zero |
| `fma` | `double fma(double x, double y, double z)` | Fused multiply-add |

### Code Examples

```c
#include <math.h>
#include <stdio.h>

int main() {
    printf("sqrt(16) = %.2f\n", sqrt(16.0));    // 4.00
    printf("pow(2, 10) = %.0f\n", pow(2, 10));   // 1024
    printf("floor(3.7) = %.0f\n", floor(3.7));   // 3
    printf("ceil(3.2) = %.0f\n", ceil(3.2));     // 4
    printf("round(3.5) = %.0f\n", round(3.5));   // 4
    printf("fabs(-5.0) = %.0f\n", fabs(-5.0));   // 5
    printf("fmod(7.5, 2) = %.1f\n", fmod(7.5, 2.0));  // 1.5

    // Trigonometry
    double pi = 3.14159265358979;
    printf("sin(π/2) = %.2f\n", sin(pi / 2));   // 1.00
    printf("cos(π) = %.2f\n", cos(pi));          // -1.00

    return 0;
}
```

---

## Time Functions (`<time.h>`)

| Function | Prototype | Description |
|----------|-----------|-------------|
| `time` | `time_t time(time_t *t)` | Current calendar time |
| `clock` | `clock_t clock(void)` | Processor time used |
| `difftime` | `double difftime(time_t t1, time_t t2)` | Difference in seconds |
| `mktime` | `time_t mktime(struct tm *t)` | Convert tm to time_t |
| `localtime` | `struct tm *localtime(const time_t *t)` | Convert to local time |
| `gmtime` | `struct tm *gmtime(const time_t *t)` | Convert to UTC |
| `strftime` | `size_t strftime(char *s, size_t max, const char *fmt, const struct tm *t)` | Format time |
| `asctime` | `char *asctime(const struct tm *t)` | Convert tm to string |
| `ctime` | `char *ctime(const time_t *t)` | Convert time_t to string |

### Code Examples

```c
#include <time.h>
#include <stdio.h>

int main() {
    // Current time
    time_t now = time(NULL);
    printf("Epoch time: %ld\n", (long)now);

    // Local time
    struct tm *local = localtime(&now);
    printf("Date: %04d-%02d-%02d\n",
           local->tm_year + 1900,
           local->tm_mon + 1,
           local->tm_mday);
    printf("Time: %02d:%02d:%02d\n",
           local->tm_hour,
           local->tm_min,
           local->tm_sec);

    // Custom format
    char buf[80];
    strftime(buf, sizeof(buf), "%A, %B %d, %Y %I:%M %p", local);
    printf("Formatted: %s\n", buf);

    // Timing code execution
    clock_t start = clock();
    // ... do work ...
    clock_t end = clock();
    double elapsed = (double)(end - start) / CLOCKS_PER_SEC;
    printf("Elapsed: %.6f seconds\n", elapsed);

    // Time difference
    time_t t1, t2;
    time(&t1);
    // ... some delay ...
    time(&t2);
    printf("Diff: %.0f seconds\n", difftime(t2, t1));

    return 0;
}
```

---

## Conversion Functions (`<stdlib.h>`)

| Function | Prototype | Description |
|----------|-----------|-------------|
| `atoi` | `int atoi(const char *s)` | String to int |
| `atol` | `long atol(const char *s)` | String to long |
| `atof` | `double atof(const char *s)` | String to double |
| `strtol` | `long strtol(const char *s, char **end, int base)` | String to long with error checking |
| `strtoul` | `unsigned long strtoul(const char *s, char **end, int base)` | String to unsigned long |
| `strtod` | `double strtod(const char *s, char **end)` | String to double with error checking |
| `itoa` | `char *itoa(int val, char *buf, int base)` | Int to string (non-standard) |
| `qsort` | `void qsort(void *base, size_t nmemb, size_t size, int (*compar)(...))` | Quick sort |
| `bsearch` | `void *bsearch(const void *key, const void *base, size_t nmemb, size_t size, int (*compar)(...))` | Binary search |
| `rand` | `int rand(void)` | Random number |
| `srand` | `void srand(unsigned int seed)` | Seed random number |

### Code Examples

```c
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int compare_ints(const void *a, const void *b) {
    return (*(int *)a - *(int *)b);
}

int main() {
    // Simple conversions (no error checking)
    int x = atoi("42");           // 42
    double d = atof("3.14");      // 3.14

    // Safer conversions with error checking
    char *endptr;
    long val = strtol("123abc", &endptr, 10);
    if (*endptr != '\0') {
        printf("Partial conversion: %ld (stopped at '%s')\n", val, endptr);
    }

    // Base conversion
    int hex = (int)strtol("FF", NULL, 16);  // 255
    int bin = (int)strtol("1010", NULL, 2); // 10

    // qsort example
    int arr[] = {5, 2, 8, 1, 9, 3};
    int n = sizeof(arr) / sizeof(arr[0]);
    qsort(arr, n, sizeof(int), compare_ints);
    // arr is now {1, 2, 3, 5, 8, 9}

    // Random numbers
    srand(time(NULL));
    int rand_num = rand() % 100;  // 0-99

    return 0;
}
```

### Common Mistakes

```c
// WRONG: atoi has no error detection
int x = atoi("abc");  // Returns 0, no error!

// RIGHT: Use strtol with error checking
char *end;
long val = strtol("abc", &end, 10);
if (end == str || *end != '\0') {
    printf("Conversion failed\n");
}

// WRONG: Not seeding random number generator
// Same sequence every run!
srand(time(NULL));  // Seed once at program start
```

---

## Common Error Codes

| Code | Macro | Description |
|------|-------|-------------|
| 0 | - | Success |
| 1 | `EXIT_FAILURE` | General failure |
| 2 | `EXIT_SUCCESS` | Success (POSIX) |
| EDOM | `EDOM` | Math argument out of domain |
| ERANGE | `ERANGE` | Result out of range |
| EILSEQ | `EILSEQ` | Invalid multibyte sequence |
| EINVAL | `EINVAL` | Invalid argument |
| ENOMEM | `ENOMEM` | Not enough memory |
| EIO | `EIO` | I/O error |
| ENOENT | `ENOENT` | File not found |
| EACCES | `EACCES` | Permission denied |

### Using `errno`

```c
#include <errno.h>
#include <math.h>
#include <stdio.h>

int main() {
    errno = 0;
    double result = sqrt(-1.0);
    if (errno != 0) {
        perror("sqrt");
        // Prints: sqrt: Numerical argument out of domain of function
    }
    return 0;
}
```

---

## One-Minute Revision Table

| Category | Key Functions | Header |
|----------|---------------|--------|
| **String** | `strlen`, `strcpy`, `strcat`, `strcmp`, `strstr` | `<string.h>` |
| **Memory** | `malloc`, `calloc`, `realloc`, `free` | `<stdlib.h>` |
| **File** | `fopen`, `fclose`, `fread`, `fwrite`, `fprintf` | `<stdio.h>` |
| **Math** | `sqrt`, `pow`, `abs`, `floor`, `ceil` | `<math.h>` |
| **Time** | `time`, `clock`, `difftime`, `strftime` | `<time.h>` |
| **Convert** | `atoi`, `atof`, `strtol`, `strtod` | `<stdlib.h>` |
| **I/O** | `printf`, `scanf`, `fgets`, `puts` | `<stdio.h>` |
| **Sort/Search** | `qsort`, `bsearch` | `<stdlib.h>` |
| **Random** | `rand`, `srand` | `<stdlib.h>` |

### Essential Macros

| Macro | Purpose |
|-------|---------|
| `NULL` | Null pointer constant |
| `EOF` | End-of-file indicator |
| `BUFSIZ` | Default buffer size |
| `FILENAME_MAX` | Max filename length |
| `EXIT_FAILURE` | Failed termination |
| `EXIT_SUCCESS` | Successful termination |
| `RAND_MAX` | Maximum random value |
| `ARRAY_SIZE(arr)` | `sizeof(arr)/sizeof(arr[0])` |

### Quick Syntax

```c
// Variable declaration
int x = 10;
const int y = 20;

// Array
int arr[5] = {1, 2, 3, 4, 5};
int *heap_arr = malloc(5 * sizeof(int));

// Function pointer
int (*func_ptr)(int, int);
func_ptr = &add;

// Struct
typedef struct {
    char name[50];
    int age;
} Person;

// Enum
typedef enum { RED, GREEN, BLUE } Color;

// Typedef for function
typedef int (*Comparator)(const void *, const void *);
```

### Compiler Flags Reference

| Flag | Purpose |
|------|---------|
| `-Wall -Wextra` | Enable all warnings |
| `-O2` | Optimization level 2 |
| `-O3` | Aggressive optimization |
| `-g` | Debug symbols |
| `-std=c11` | Use C11 standard |
| `-std=c99` | Use C99 standard |
| `-fsanitize=address` | Address sanitizer |
| `-fsanitize=undefined` | UB sanitizer |
| `-pedantic` | Strict ISO compliance |
| `-lm` | Link math library |

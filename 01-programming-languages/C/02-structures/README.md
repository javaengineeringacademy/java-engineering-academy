# Structures & Unions — C Language

## Why It Matters

When you're building anything beyond simple variables — a database record with name, age, and salary, or a network packet with source, destination, port, and payload — you need to group related data together. Without structures, you manage dozens of separate variables, which is error-prone and unmanageable. Structures provide the foundation for all data abstraction in C: linked lists, trees, hash tables, I/O buffers, and every complex data type in systems programming.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Grouping related data, API boundaries, ABI-stable interfaces | Simple variables for trivial cases |
| When NOT to use | Returning large structs by value (copies entire struct) | Use pointers or output parameters |
| Alternatives | C++ classes (OOP), Rust enums (tagged unions) | More features, different trade-offs |
| Production Examples | Linux `file_operations`, SQLite VFS, network protocols | Packed structs for wire formats |
| Common Mistakes | Forgetting padding, not checking union tags, shallow copying pointers | Use `offsetof()`, always check tags |

## What It Is

Structures and unions are user-defined data types that group related data under a single name:

- **struct**: Groups related data; each member has its own memory location
- **union**: Overlays members at the same memory location (only one active at a time)
- **bit field**: Allocates precise numbers of bits within a struct member
- **typedef**: Creates type aliases for cleaner syntax

## Why It Exists

C has no classes, no objects, no inheritance. Structures are C's mechanism for creating custom types that model real-world entities. Every data structure in C — linked lists, stacks, queues, trees, hash tables — is built from structures.

### Architecture: How Structs Map to Memory

```c
struct Person {
    char name[50];  // 50 bytes
    int age;        // 4 bytes
    float height;   // 4 bytes
};
// sizeof(struct Person) = 58 bytes (minimum)
// Actual: may be 60+ due to alignment padding
```

The compiler may insert padding bytes between members to satisfy alignment requirements. This is a hardware reality — accessing misaligned data causes performance penalties or bus errors on some architectures.

## Expanded Code Examples

### Structure Declaration and Usage

```c
#include <stdio.h>
#include <string.h>

// Named structure tag
struct Point {
    int x;
    int y;
};

// typedef for cleaner usage
typedef struct {
    char title[100];
    char author[50];
    int year;
    double price;
} Book;

// Structure with function pointers (behavior)
typedef struct {
    const char *name;
    double (*calculate)(double, double);
} Operation;

double add(double a, double b) { return a + b; }
double mul(double a, double b) { return a * b; }

int main(void) {
    // Direct initialization
    struct Point p1 = {10, 20};

    // Designated initializers (C99)
    struct Point p2 = {.x = 30, .y = 40};

    // Member access
    printf("p1: (%d, %d)\n", p1.x, p1.y);

    // struct Assignment (shallow copy)
    struct Point p3 = p1;  // p3.x = 10, p3.y = 20

    // Book usage
    Book b1 = {"The C Programming Language", "K&R", 1978, 49.99};
    printf("%s by %s (%d)\n", b1.title, b1.author, b1.year);

    // Operation dispatch
    Operation ops[] = {{"add", add}, {"mul", mul}};
    for (int i = 0; i < 2; i++) {
        printf("%s(2,3) = %g\n", ops[i].name, ops[i].calculate(2, 3));
    }

    return 0;
}
```

### Nested Structures

```c
#include <stdio.h>

typedef struct {
    int day, month, year;
} Date;

typedef struct {
    char name[100];
    Date date_of_birth;
    Date hire_date;
    double salary;
} Employee;

// Accessing nested members
void print_employee(const Employee *e) {
    printf("Name: %s\n", e->name);
    printf("Born: %02d/%02d/%04d\n",
           e->date_of_birth.day,
           e->date_of_birth.month,
           e->date_of_birth.year);
    printf("Hired: %02d/%02d/%04d\n",
           e->hire_date.day,
           e->hire_date.month,
           e->hire_date.year);
    printf("Salary: $%.2f\n", e->salary);
}
```

### Passing Structures Efficiently

```c
#include <stdio.h>
#include <string.h>

// BAD: Copies entire struct onto stack
void process_by_value(struct Point p) {
    p.x = 999;  // Modifies local copy, not original
}

// GOOD: Pass pointer, avoids copy
void process_by_pointer(struct Point *p) {
    if (p == NULL) return;  // Always check
    p->x = 999;  // Modifies original
}

// GOOD: Read-only, compiler enforces
void print_point(const struct Point *p) {
    if (p == NULL) return;
    printf("(%d, %d)\n", p->x, p->y);
    // p->x = 10;  // Compiler error: read-only
}
```

### Unions — Variant Data

```c
#include <stdio.h>
#include <string.h>

// Tagged union (manual variant type)
typedef enum { INT, FLOAT, STRING } Type;

typedef struct {
    Type type;
    union {
        int i;
        float f;
        char str[64];
    } data;
} Variant;

void print_variant(const Variant *v) {
    switch (v->type) {
        case INT:    printf("int: %d\n", v->data.i); break;
        case FLOAT:  printf("float: %f\n", v->data.f); break;
        case STRING: printf("string: %s\n", v->data.str); break;
    }
}

// Real-world: JSON value representation
typedef enum {
    JSON_NULL, JSON_BOOL, JSON_NUMBER, JSON_STRING, JSON_ARRAY, JSON_OBJECT
} JsonType;

typedef struct JsonValue JsonValue;  // Forward declaration

struct JsonValue {
    JsonType type;
    union {
        int boolean;         // 0 or 1
        double number;
        char string[256];
        struct { JsonValue *items; size_t count; } array;
        struct { char keys[16][64]; JsonValue *values; size_t count; } object;
    } as;
};
```

### Bit Fields — Compact Flags

```c
#include <stdio.h>

// Network packet flags (1 byte total)
struct PacketFlags {
    unsigned int syn     : 1;  // 1 bit
    unsigned int ack     : 1;  // 1 bit
    unsigned int fin     : 1;  // 1 bit
    unsigned int rst     : 1;  // 1 bit
    unsigned int padding : 4;  // 4 bits (unused)
};

// File permission flags (Unix-style)
struct FilePermissions {
    unsigned int read    : 1;
    unsigned int write   : 1;
    unsigned int execute : 1;
    unsigned int owner   : 1;
    unsigned int group   : 1;
    unsigned int other   : 1;
    unsigned int reserved: 2;
};

int main(void) {
    struct PacketFlags flags = {0};
    flags.syn = 1;
    flags.ack = 1;

    printf("Flags: syn=%d ack=%d fin=%d rst=%d\n",
           flags.syn, flags.ack, flags.fin, flags.rst);
    printf("Size: %zu byte(s)\n", sizeof(struct PacketFlags));

    return 0;
}
```

### Structure Padding and Alignment

```c
#include <stdio.h>

struct Bad {
    char a;      // 1 byte
    // 3 bytes padding (compiler inserts for int alignment)
    int b;       // 4 bytes
    char c;      // 1 byte
    // 3 bytes padding (trailing)
};  // Total: 12 bytes

struct Good {
    int b;       // 4 bytes (largest first)
    char a;      // 1 byte
    char c;      // 1 byte
    // 2 bytes padding (trailing)
};  // Total: 8 bytes

struct Packed {
    char a;
    int b __attribute__((packed));  // GCC: no padding
    char c;
};  // Total: 6 bytes (but may be slower)

int main(void) {
    printf("Bad:  %zu bytes\n", sizeof(struct Bad));    // 12
    printf("Good: %zu bytes\n", sizeof(struct Good));   // 8
    printf("Packed: %zu bytes\n", sizeof(struct Packed)); // 6

    // Offset of each member
    printf("Bad.a offset: %zu\n", offsetof(struct Bad, a));  // 0
    printf("Bad.b offset: %zu\n", offsetof(struct Bad, b));  // 4
    printf("Bad.c offset: %zu\n", offsetof(struct Bad, c));  // 8

    return 0;
}
```

### Opaque Structures (API Design)

```c
// image.h — Public API (users cannot see internals)
#ifndef IMAGE_H
#define IMAGE_H

typedef struct Image Image;

Image *image_create(int width, int height, int channels);
void image_destroy(Image *img);
int image_get_pixel(const Image *img, int x, int y, int channel);
void image_set_pixel(Image *img, int x, int y, int channel, int value);
int image_save(const Image *img, const char *filename);

#endif

// image.c — Implementation (private details hidden)
#include "image.h"
#include <stdlib.h>
#include <string.h>

struct Image {
    int width;
    int height;
    int channels;
    unsigned char *data;  // Pixel data
};

Image *image_create(int width, int height, int channels) {
    Image *img = malloc(sizeof(Image));
    if (!img) return NULL;

    img->width = width;
    img->height = height;
    img->channels = channels;
    img->data = calloc(width * height * channels, sizeof(unsigned char));

    if (!img->data) {
        free(img);
        return NULL;
    }
    return img;
}

void image_destroy(Image *img) {
    if (img) {
        free(img->data);
        free(img);
    }
}
```

## Production Incidents

### Incident 1: Structure Padding Causing Protocol Mismatch

**Problem**: A network protocol sends structures directly over the wire. Client and server have different padding, causing data corruption.

**Cause**: Different compilers/platforms add different padding:

```c
// Client (x86): struct is 8 bytes
// Server (ARM): struct is 12 bytes
struct Message {
    char type;     // 1 byte + 3 padding
    int payload;   // 4 bytes
    short flags;   // 2 bytes + 2 padding
};
```

**Solution**: Use `__attribute__((packed))` or serialize fields individually:

```c
// Option 1: Packed (slower but correct)
struct Message {
    char type;
    int payload;
    short flags;
} __attribute__((packed));

// Option 2: Manual serialization (preferred for network protocols)
void serialize_message(const struct Message *m, char *buf) {
    buf[0] = m->type;
    memcpy(buf + 1, &m->payload, sizeof(int));
    memcpy(buf + 5, &m->flags, sizeof(short));
}
```

### Incident 2: Union Type Confusion

**Problem**: A tagged union is accessed without checking the tag, reading garbage data.

```c
typedef struct {
    int type;
    union { int i; float f; } val;
} Var;

Var v = {.type = 1, .val.i = 42};
printf("%f\n", v.val.f);  // Undefined behavior: reading int as float
```

**Solution**: Always check the tag before accessing the union:

```c
void print_var(const Var *v) {
    switch (v->type) {
        case 0: printf("%d\n", v->val.i); break;
        case 1: printf("%f\n", v->val.f); break;
        default: printf("unknown type\n"); break;
    }
}
```

## Production Checklist

- [ ] Use `typedef` for cleaner syntax
- [ ] Initialize all structure members before use
- [ ] Pass large structures by pointer (with `const` for read-only)
- [ ] Be aware of padding/alignment — order members largest-first
- [ ] Use `__attribute__((packed))` for network protocols or file formats
- [ ] Use opaque structures for API boundaries
- [ ] Always check tag before accessing union members
- [ ] Use `offsetof()` to verify layout assumptions
- [ ] Free any dynamically allocated members in struct destructors

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Creates basic structures, accesses members | Uses `struct` and `.` operator |
| **Intermediate** | Uses nested structures, unions, and typedef | Implements tagged unions, passes by pointer |
| **Advanced** | Understands padding, bit fields, and opaque types | Designs APIs with opaque structs, optimizes layout |
| **Expert** | Designs ABI-stable interfaces, handles cross-platform compatibility | Uses packed structs, `offsetof`, serialization |

## Common Myths Debunked

1. **Myth**: Structures are always contiguous in memory
   **Truth**: The compiler inserts padding between members for alignment. Use `__attribute__((packed))` or reorder members to minimize padding.

2. **Myth**: Unions save memory
   **Truth**: Unions are for variant data (a value that can be one of several types), not for memory optimization. The union is sized to its largest member.

3. **Myth**: You can return structs from functions
   **Truth**: You can, but it copies the entire struct onto the stack. For large structs, return a pointer or use an output parameter.

4. **Myth**: `struct` assignment copies pointers
   **Truth**: Struct assignment does a shallow copy. If a struct contains a pointer, both copies point to the same memory — you need a deep copy function.

## One-Minute Revision

| Concept | Description | Key Detail |
|---------|-------------|------------|
| `struct` | Group of related data | Each member has its own memory location |
| `typedef` | Create type alias | `typedef struct { ... } Name;` |
| Union | Shared memory members | Only one member active at a time |
| Bit field | Precise bit allocation | Useful for flags and hardware registers |
| Padding | Alignment bytes | Reorder members largest-first to minimize |
| Member access | `.` and `->` operators | `->` for pointers, `.` for direct |
| Opaque struct | Hidden implementation | Public API, private internals |

## Related Topics

- [Fundamentals](../01-fundamentals/README.md) — Basic types and variables
- [Data Structures](../06-data-structures/README.md) — Linked lists, trees, and hash tables built from structs
- [Memory Management](../08-memory-management/README.md) — Dynamic allocation for struct members
- [Security](../11-security/README.md) — Preventing buffer overflows in struct members

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Unexpected struct padding/sizes | `offsetof()` and `sizeof()` | Print `offsetof(struct S, member)` and `sizeof(struct S)` to verify layout assumptions |
| Union type confusion (reading wrong member) | Compiler warnings + code review | Enable `-Wconversion` and verify tag is checked before every union access |
| Bit field portability issues | Compile on multiple architectures | Test bit field structs on x86, ARM, and big-endian targets to catch layout differences |
| Incorrect shallow vs deep copy | Valgrind / AddressSanitizer | Compile with `-fsanitize=address` to detect shared pointers after struct assignment |
| Opaque struct forward declaration mismatch | Compile-time assertion | Use `_Static_assert(sizeof(struct S) >= expected, ...)` to validate struct sizes across compilation units |

## Code Review Checklist

- [ ] Structure members ordered largest-first to minimize padding
- [ ] Union access always guarded by tag check (switch/enum)
- [ ] Bit fields used only for flags/hardware registers, not for general storage
- [ ] `typedef` used for cleaner syntax where appropriate
- [ ] Opaque struct pattern used for API boundaries (public header exposes `typedef struct X X`)
- [ ] `offsetof()` used to validate layout assumptions in serialization code
- [ ] Deep copy function provided for structs containing pointers

## Architecture Considerations

Structures are the fundamental data modeling tool in C. Every complex system — linked lists, trees, hash tables, network packets — is composed of structs. Proper struct design determines ABI stability, memory efficiency, and API ergonomics. For public APIs, use opaque pointers (forward-declared structs) to decouple interface from implementation, allowing internal layout changes without breaking consumers.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Opaque struct | API boundaries, library interfaces | Hides internals but prevents stack allocation |
| Packed struct | Network protocols, file formats | Saves space but may cause misaligned access penalties |
| Tagged union | Variant types (JSON values, config options) | Flexible but requires manual tag management |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Structure padding leaking sensitive data | Information disclosure in serialized structs | Zero-initialize structs; explicitly pad or pack for serialization |
| Union type confusion (reading wrong active member) | Undefined behavior, potential code execution | Always check tag before accessing union members |
| Bit field underflow/overflow | Incorrect flag interpretation, logic errors | Validate bit field values at assignment; use fixed-width backing types |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89/C90 → C99 | Added designated initializers, compound literals, flexible array members | Use `.member = value` syntax; replace `malloc(sizeof(T)+extra)` with flexible arrays |
| C99 → C11 | Added anonymous structs/unions, `_Static_assert` | Use anonymous members for nested access; add compile-time size checks |
| C11 → C23 | Added `typeof`, improved `_Generic` support | Use `typeof` for type-generic struct operations; adopt `constexpr` for compile-time struct constants |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| Designated initializers (`{.x = 1}`) | C99 | Standard — universally supported |
| Flexible array members (`int data[]`) | C99 | Standard — preferred over pointer + separate allocation |
| Anonymous structs/unions | C11 | Standard — widely supported |
| `_Static_assert` in structs | C11 | Standard — use for size/layout validation |

## Interview Questions

1. **What causes structure padding and how do you minimize it?**: The compiler inserts padding bytes between members to satisfy alignment requirements (e.g., `int` must be 4-byte aligned). Minimize by ordering members largest-first and using `__attribute__((packed))` when layout is critical (network protocols, file formats).
2. **What is the difference between a struct and a union?**: A struct allocates separate memory for each member; a union overlays all members at the same address. Structs model "all of these properties"; unions model "one of these variants." Unions require manual tag tracking to know which member is active.
3. **When would you use an opaque struct?**: Opaque structs hide implementation details behind a public API. Use them for library boundaries where internal layout may change across versions. The public header forward-declares the struct; only the implementation file defines it.
4. **What are flexible array members and why were they introduced?**: Flexible array members (`char data[]` at the end of a struct) allow variable-length trailing data without pointer indirection. They were introduced in C99 as a safer alternative to the "struct hack" and reduce allocation overhead.
5. **How do you safely serialize a struct for network transmission?**: Never send structs directly due to padding differences. Instead, serialize each field individually with fixed byte order (network byte order via `htonl`/`ntohl`) and validate sizes at both ends.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Secure Coding in C and CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)
- [Compilers and Alignment (深入理解计算机系统)](https://csapp.cs.cmu.edu/)

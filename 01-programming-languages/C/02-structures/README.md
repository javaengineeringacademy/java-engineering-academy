# Structures & Unions — C Language

## What it is
Structures and unions are user-defined data types that group related data together.

## Why it exists
To organize complex data into manageable, reusable units.

## When to use it
When you need to represent entities with multiple attributes.

## How it works

### Structure Declaration

```c
struct Person {
    char name[50];
    int age;
    float height;
};
```

### Structure Usage

```c
struct Person p1 = {"John", 30, 5.9};
printf("%s is %d years old\n", p1.name, p1.age);
```

### typedef

```c
typedef struct {
    char name[50];
    int age;
} Person;

Person p1 = {"John", 30};
```

### Nested Structures

```c
struct Date {
    int day, month, year;
};

struct Employee {
    char name[50];
    struct Date birth;
    struct Date hire;
};
```

### Unions

```c
union Data {
    int i;
    float f;
    char str[20];
};  // All members share same memory
```

### Bit Fields

```c
struct Flags {
    unsigned int active : 1;
    unsigned int admin  : 1;
    unsigned int mode   : 3;
};
```

### Structure Padding

```c
struct Padded {
    char a;     // 1 byte
    // 3 bytes padding
    int b;      // 4 bytes
};  // Total: 8 bytes

struct Packed {
    char a;
    int b __attribute__((packed));
};  // Total: 5 bytes
```

## Production Checklist

- [ ] Use typedef for cleaner syntax
- [ ] Initialize all structure members
- [ ] Pass large structures by pointer
- [ ] Be aware of padding/alignment
- [ ] Use const for read-only access

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Creates basic structures |
| Intermediate | Uses nested structures and unions |
| Advanced | Understands padding and bit fields |

## Common Myths

1. **Myth**: Structures are always contiguous
   **Truth**: Padding may be inserted for alignment

2. **Myth**: Unions save memory
   **Truth**: Unions are for variant data, not memory saving

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| struct | Group of related data |
| typedef | Create type alias |
| Union | Shared memory members |
| Bit field | Precise bit allocation |
| Padding | Alignment bytes |
| Member access | . and -> operators |

## Related Topics

- [Fundamentals](../01-fundamentals/README.md)
- [Data Structures](../06-data-structures/README.md)

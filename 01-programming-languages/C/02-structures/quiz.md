# Structures Quiz

## Questions

### MCQ

**1.** What is the size of a union containing `int` (4 bytes), `double` (8 bytes), and `char[4]`?

A) 16 bytes (sum of all members)
B) 8 bytes (size of largest member)
C) 4 bytes (size of smallest member)
D) Depends on compiler

**2.** Which operator is used to access a member through a struct pointer?

A) `.` (dot)
B) `->` (arrow)
C) `*` (dereference)
D) `&` (address-of)

**3.** What does `typedef struct { ... } Name;` do?

A) Creates a struct variable
B) Creates a struct type and an alias for it
C) Allocates memory for a struct
D) Defines a function that returns a struct

### Code Output

**4.** What does this code print?

```c
struct Point { int x; int y; };
struct Point p1 = {3, 7};
struct Point p2 = p1;
p2.x = 10;
printf("p1.x=%d p2.x=%d\n", p1.x, p2.x);
```

**5.** What is the output?

```c
union Data {
    int i;
    float f;
    char c;
};
union Data d;
d.i = 65;
printf("%c %d\n", d.c, d.i);
```

**6.** Predict the sizeof each struct:

```c
struct A { char a; int b; char c; };
struct B { int b; char a; char c; };
struct C { char a; char b; int c; };
printf("A=%zu B=%zu C=%zu\n", sizeof(struct A), sizeof(struct B), sizeof(struct C));
```

### Bug Finding

**7.** Find the bug in this code:

```c
struct Person {
    char *name;
    int age;
};
struct Person p;
p.name = malloc(50);
strcpy(p.name, "Alice");
free(p.name);
// Later...
printf("Name: %s\n", p.name);  // Bug?
```

**8.** This self-referential struct has a problem. What is it?

```c
struct Node {
    int data;
    struct Node next;  // Not a pointer!
};
```

### Scenario

**9.** You are designing a network packet structure. The packet has:
- Version (3 bits)
- Header Length (5 bits)
- Total Length (16 bits)
- Flags (3 bits)
- Fragment Offset (13 bits)

Design this using bit fields. Then explain one potential problem with bit fields for network protocols.

**10.** You have a function `process(struct Data *d)` that receives a pointer to a large struct (2KB). A colleague suggests changing the signature to `process(struct Data d)` (pass by value) to "simplify the code." Explain why this is a bad idea and describe the performance implications.

## Answers

### 1. Answer: B

A union's size equals its largest member. The `double` is 8 bytes, so the union is 8 bytes. All members overlap in the same memory location — writing to one member overwrites the others.

### 2. Answer: B

The arrow operator `->` dereferences a pointer and accesses a member in one step. `p->member` is equivalent to `(*p).member` but cleaner and less error-prone. The dot operator `.` is used for direct struct access (not through a pointer).

### 3. Answer: B

`typedef struct { ... } Name;` defines an anonymous struct and creates the alias `Name` for it. After this, you can use `Name variable;` instead of `struct { ... } variable;`. It combines struct definition and type aliasing in one statement.

### 4. Answer: p1.x=3 p2.x=10

Structs are copied by value in C. `p2 = p1` creates an independent copy. Modifying `p2.x` does not affect `p1.x`. This is different from copying a pointer, where both would reference the same memory.

### 5. Answer: A 65

The union stores `65` as an `int`. When read as a `char`, 65 is the ASCII value for `'A'`. The `int` representation is still `65`. The same 4 bytes are interpreted differently depending on which member you access.

### 6. Answer: A=12 B=8 C=8

- `struct A`: `char(1) + pad(3) + int(4) + char(1) + pad(3) = 12`
- `struct B`: `int(4) + char(1) + char(1) + pad(2) = 8`
- `struct C`: `char(1) + char(1) + pad(2) + int(4) = 8`

Ordering matters! Grouping same-sized types together minimizes padding.

### 7. Answer: Use-after-free

After `free(p.name)`, the pointer `p.name` becomes a dangling pointer. Using it in `printf` is undefined behavior — the freed memory may have been reallocated, causing a crash, data corruption, or printing garbage. Fix: set `p.name = NULL` after freeing and check before use.

### 8. Answer: Infinite size — self-referential struct must use a pointer

A struct cannot contain an instance of itself — it would need infinite memory (the member contains itself, which contains itself...). The correct definition uses a pointer: `struct Node *next;`. A pointer has a fixed size (4 or 8 bytes) and points to another instance of the same struct.

### 9. Answer: Bit field design

```c
typedef struct {
    unsigned int version     : 3;   // 0-7
    unsigned int header_len  : 5;   // 0-31
    unsigned int total_len   : 16;  // 0-65535
    unsigned int flags       : 3;   // 0-7
    unsigned int frag_offset : 13;  // 0-8191
} PacketHeader;  // Total: 32 bits = 4 bytes
```

**Problem with bit fields for networking**: Bit field layout (bit ordering within bytes) is implementation-defined and compiler-dependent. Two different compilers may lay out the same bit field differently, causing packets to be misinterpreted across platforms. For network protocols, use bitwise operators (`&`, `|`, `<<`, `>>`) instead of bit fields to ensure consistent byte-level layout.

### 10. Answer: Pass by value copies 2KB onto the stack

Passing a 2KB struct by value:
1. **Copies the entire struct** onto the call stack (2KB allocation per call)
2. **Stack overhead**: Deep call chains can cause stack overflow
3. **Cache thrashing**: The copy operation evicts useful data from cache
4. **No modification**: The function receives a copy, so changes don't affect the original (may require a return or output parameter)

Pass by pointer:
- Copies only 8 bytes (pointer size) regardless of struct size
- The function can modify the original struct
- Use `const struct Data *d` if the function should not modify it

**Rule**: Always pass large structs by pointer. Use `const` to indicate read-only access.

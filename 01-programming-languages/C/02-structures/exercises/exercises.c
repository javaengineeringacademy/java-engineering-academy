/*
 * Structures — C Language
 * Exercises: Structs, Unions, Enums, Bit Fields, Padding
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o exercises exercises.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Problem 1: Struct Basics — Student Record
 *
 * Define a struct `Student` with fields:
 *   - name[50] (char array)
 *   - age (int)
 *   - gpa (double)
 *   - student_id (unsigned long)
 *
 * Write functions:
 *   a) `create_student` — initialize a student
 *   b) `print_student` — display student info
 *   c) `is_honor_student` — returns 1 if GPA >= 3.5
 * ============================================================ */
typedef struct {
    char name[50];
    int age;
    double gpa;
    unsigned long student_id;
} Student;

Student create_student(const char *name, int age, double gpa, unsigned long id) {
    /* TODO: Create and return a Student */
    Student s;
    return s;
}

void print_student(const Student *s) {
    /* TODO: Print student details */
}

int is_honor_student(const Student *s) {
    /* TODO: Return 1 if GPA >= 3.5 */
    return 0;
}

void problem1_struct_basics(void) {
    printf("=== Problem 1: Struct Basics ===\n");
    Student s1 = create_student("Alice", 20, 3.8, 1001);
    Student s2 = create_student("Bob", 22, 2.9, 1002);
    print_student(&s1);
    printf("Honor student? %s\n", is_honor_student(&s1) ? "Yes" : "No");
    print_student(&s2);
    printf("Honor student? %s\n", is_honor_student(&s2) ? "Yes" : "No");
    printf("\n");
}

/* ============================================================
 * Problem 2: Struct Size and Padding
 *
 * Predict the sizeof each struct, then verify with printf.
 * Explain why padding exists.
 *
 * struct A { char a; int b; char c; };
 * struct B { int b; char a; char c; };
 * struct C { char a; char b; char c; };
 * struct D { double d; int i; char c; };
 * ============================================================ */
typedef struct { char a; int b; char c; } structA;
typedef struct { int b; char a; char c; } structB;
typedef struct { char a; char b; char c; } structC;
typedef struct { double d; int i; char c; } structD;

void problem2_padding(void) {
    printf("=== Problem 2: Struct Padding ===\n");
    printf("sizeof(structA) = %zu (predicted: ?)\n", sizeof(structA));
    printf("sizeof(structB) = %zu (predicted: ?)\n", sizeof(structB));
    printf("sizeof(structC) = %zu (predicted: ?)\n", sizeof(structC));
    printf("sizeof(structD) = %zu (predicted: ?)\n", sizeof(structD));
    /* TODO: Explain why structA and structB have different sizes */
    printf("\n");
}

/* ============================================================
 * Problem 3: Union — Memory-Efficient Variant
 *
 * Create a `Variant` union that can hold:
 *   - an int
 *   - a float
 *   - a char[4]
 *
 * Write a function `print_variant` that prints the type and value.
 * Demonstrate that all members share the same memory.
 * ============================================================ */
typedef union {
    int i;
    float f;
    char c[4];
} Variant;

typedef enum { TYPE_INT, TYPE_FLOAT, TYPE_CHAR } VariantType;

void print_variant(const Variant *v, VariantType type) {
    /* TODO: Print the value based on type */
}

void problem3_union(void) {
    printf("=== Problem 3: Union ===\n");
    Variant v;
    printf("sizeof(Variant) = %zu\n", sizeof(Variant));

    v.i = 42;
    print_variant(&v, TYPE_INT);

    v.f = 3.14f;
    print_variant(&v, TYPE_FLOAT);

    strcpy(v.c, "Hi!");
    print_variant(&v, TYPE_CHAR);

    printf("Note: sizeof(Variant) equals sizeof largest member\n\n");
}

/* ============================================================
 * Problem 4: Self-Referential Struct — Linked List Node
 *
 * Define a `Node` struct with:
 *   - data (int)
 *   - next (pointer to Node)
 *
 * Write functions to:
 *   a) `create_node` — allocate and initialize a node
 *   b) `append_node` — add to end of list
 *   c) `print_list` — traverse and print
 *   d) `free_list` — free all nodes
 * ============================================================ */
typedef struct Node {
    int data;
    struct Node *next;
} Node;

Node *create_node(int data) {
    /* TODO: malloc a new node, set data and next=NULL */
    return NULL;
}

void append_node(Node **head, int data) {
    /* TODO: Add new node at end of list */
}

void print_list(const Node *head) {
    /* TODO: Traverse and print each node's data */
}

void free_list(Node *head) {
    /* TODO: Free all nodes */
}

void problem4_linked_list(void) {
    printf("=== Problem 4: Self-Referential Struct ===\n");
    Node *head = NULL;
    for (int i = 1; i <= 5; i++) {
        append_node(&head, i * 10);
    }
    printf("List: ");
    print_list(head);
    free_list(head);
    printf("\n\n");
}

/* ============================================================
 * Problem 5: Bit Fields — Hardware Register
 *
 * Simulate a hardware register with these fields:
 *   - mode[3]: operating mode (0-7)
 *   - enable: 1 bit enable flag
 *   - interrupt: 1 bit interrupt flag
 *   - speed[4]: speed setting (0-15)
 *   - reserved[8]: unused bits
 *
 * Write functions to read/write individual fields and
 * print the register contents in binary.
 * ============================================================ */
typedef struct {
    unsigned int mode      : 3;
    unsigned int enable    : 1;
    unsigned int interrupt : 1;
    unsigned int speed     : 4;
    unsigned int reserved  : 8;
} HWRegister;

void print_register_binary(HWRegister *reg) {
    /* TODO: Print the register as 16-bit binary */
}

void problem5_bitfields(void) {
    printf("=== Problem 5: Bit Fields ===\n");
    HWRegister reg = {0};
    reg.mode = 5;
    reg.enable = 1;
    reg.speed = 12;
    printf("Mode: %d, Enable: %d, Speed: %d\n",
           reg.mode, reg.enable, reg.speed);
    printf("Register size: %zu bytes\n", sizeof(reg));
    printf("TODO: Implement print_register_binary\n\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Structures — Exercises\n");
    printf("====================================\n\n");

    problem1_struct_basics();
    problem2_padding();
    problem3_union();
    problem4_linked_list();
    problem5_bitfields();

    return 0;
}

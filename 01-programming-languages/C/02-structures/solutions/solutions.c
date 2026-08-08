/*
 * Structures — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Problem 1: Struct Basics
 * ============================================================ */
typedef struct {
    char name[50];
    int age;
    double gpa;
    unsigned long student_id;
} Student;

Student create_student(const char *name, int age, double gpa, unsigned long id) {
    Student s;
    strncpy(s.name, name, sizeof(s.name) - 1);
    s.name[sizeof(s.name) - 1] = '\0';
    s.age = age;
    s.gpa = gpa;
    s.student_id = id;
    return s;
}

void print_student(const Student *s) {
    printf("  Student: %s (ID: %lu), Age: %d, GPA: %.1f\n",
           s->name, s->student_id, s->age, s->gpa);
}

int is_honor_student(const Student *s) {
    return s->gpa >= 3.5;
}

void problem1_struct_basics(void) {
    printf("=== Problem 1: Struct Basics ===\n");
    Student s1 = create_student("Alice", 20, 3.8, 1001);
    Student s2 = create_student("Bob", 22, 2.9, 1002);
    print_student(&s1);
    printf("  Honor student? %s\n", is_honor_student(&s1) ? "Yes" : "No");
    print_student(&s2);
    printf("  Honor student? %s\n", is_honor_student(&s2) ? "Yes" : "No");
    printf("\n");
}

/* ============================================================
 * Problem 2: Struct Padding
 * ============================================================ */
typedef struct { char a; int b; char c; } structA;
typedef struct { int b; char a; char c; } structB;
typedef struct { char a; char b; char c; } structC;
typedef struct { double d; int i; char c; } structD;

void problem2_padding(void) {
    printf("=== Problem 2: Struct Padding ===\n");
    printf("  structA { char a; int b; char c; } → %zu bytes\n", sizeof(structA));
    printf("    Layout: a(1) + pad(3) + b(4) + c(1) + pad(3) = 12\n");
    printf("  structB { int b; char a; char c; } → %zu bytes\n", sizeof(structB));
    printf("    Layout: b(4) + a(1) + c(1) + pad(2) = 8\n");
    printf("  structC { char a; char b; char c; } → %zu bytes\n", sizeof(structC));
    printf("    Layout: a(1) + b(1) + c(1) + pad(1) = 4\n");
    printf("  structD { double d; int i; char c; } → %zu bytes\n", sizeof(structD));
    printf("    Layout: d(8) + i(4) + c(1) + pad(3) = 16\n");
    printf("  Padding ensures each member is aligned to its natural boundary.\n");
    printf("  Reordering members by size (largest first) minimizes padding.\n\n");
}

/* ============================================================
 * Problem 3: Union
 * ============================================================ */
typedef union {
    int i;
    float f;
    char c[4];
} Variant;

typedef enum { TYPE_INT, TYPE_FLOAT, TYPE_CHAR } VariantType;

void print_variant(const Variant *v, VariantType type) {
    switch (type) {
        case TYPE_INT:
            printf("  int: %d\n", v->i);
            break;
        case TYPE_FLOAT:
            printf("  float: %f\n", v->f);
            break;
        case TYPE_CHAR:
            printf("  char[4]: \"%s\"\n", v->c);
            break;
    }
}

void problem3_union(void) {
    printf("=== Problem 3: Union ===\n");
    Variant v;
    printf("  sizeof(Variant) = %zu (size of largest member)\n", sizeof(Variant));

    v.i = 42;
    print_variant(&v, TYPE_INT);

    v.f = 3.14f;
    print_variant(&v, TYPE_FLOAT);

    strcpy(v.c, "Hi!");
    print_variant(&v, TYPE_CHAR);

    printf("  Writing to one member overwrites others — they share memory.\n\n");
}

/* ============================================================
 * Problem 4: Self-Referential Struct — Linked List
 * ============================================================ */
typedef struct Node {
    int data;
    struct Node *next;
} Node;

Node *create_node(int data) {
    Node *node = malloc(sizeof(Node));
    if (node == NULL) {
        fprintf(stderr, "malloc failed\n");
        exit(1);
    }
    node->data = data;
    node->next = NULL;
    return node;
}

void append_node(Node **head, int data) {
    Node *new_node = create_node(data);
    if (*head == NULL) {
        *head = new_node;
        return;
    }
    Node *current = *head;
    while (current->next != NULL) {
        current = current->next;
    }
    current->next = new_node;
}

void print_list(const Node *head) {
    const Node *current = head;
    while (current != NULL) {
        printf("%d", current->data);
        if (current->next) printf(" → ");
        current = current->next;
    }
}

void free_list(Node *head) {
    Node *current = head;
    while (current != NULL) {
        Node *temp = current;
        current = current->next;
        free(temp);
    }
}

void problem4_linked_list(void) {
    printf("=== Problem 4: Self-Referential Struct ===\n");
    Node *head = NULL;
    for (int i = 1; i <= 5; i++) {
        append_node(&head, i * 10);
    }
    printf("  List: ");
    print_list(head);
    printf("\n");
    free_list(head);
    printf("\n");
}

/* ============================================================
 * Problem 5: Bit Fields
 * ============================================================ */
typedef struct {
    unsigned int mode      : 3;
    unsigned int enable    : 1;
    unsigned int interrupt : 1;
    unsigned int speed     : 4;
    unsigned int reserved  : 8;
} HWRegister;

void print_register_binary(HWRegister *reg) {
    unsigned short *raw = (unsigned short *)reg;
    printf("  Register: ");
    for (int i = 15; i >= 0; i--) {
        printf("%d", (*raw >> i) & 1);
        if (i == 13 || i == 12 || i == 8 || i == 7) printf(" ");
    }
    printf("\n");
}

void problem5_bitfields(void) {
    printf("=== Problem 5: Bit Fields ===\n");
    HWRegister reg = {0};
    reg.mode = 5;
    reg.enable = 1;
    reg.speed = 12;
    printf("  Mode: %d, Enable: %d, Speed: %d\n",
           reg.mode, reg.enable, reg.speed);
    printf("  Register size: %zu bytes\n", sizeof(reg));
    print_register_binary(&reg);
    printf("  Bit fields allow compact representation of hardware registers.\n\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Structures — Solutions\n");
    printf("====================================\n\n");

    problem1_struct_basics();
    problem2_padding();
    problem3_union();
    problem4_linked_list();
    problem5_bitfields();

    return 0;
}

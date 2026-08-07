# Structures Exercises

## Exercise 1: Student Record
Create a structure for student records with name, ID, and grades.

```c
#include <stdio.h>
#include <string.h>

typedef struct {
    char name[50];
    int id;
    float gpa;
} Student;

void print_student(const Student *s) {
    printf("Name: %s, ID: %d, GPA: %.2f\n", s->name, s->id, s->gpa);
}

int main(void) {
    Student s = {"Alice", 12345, 3.8};
    print_student(&s);
    return 0;
}
```

## Exercise 2: Linked List Node
Define a structure for a linked list node.

## Exercise 3: Matrix Structure
Create a structure to represent a matrix with dimensions and data.

## Exercise 4: Employee Database
Build an employee database using an array of structures.

## Exercise 5: Union for Variant Data
Use a union to store different data types in the same memory.

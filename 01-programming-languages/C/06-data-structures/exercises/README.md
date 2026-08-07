# Data Structures Exercises

## Exercise 1: Singly Linked List
Implement a singly linked list with insert, delete, and search operations.

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct Node {
    int data;
    struct Node *next;
} Node;

void insert_front(Node **head, int data) {
    Node *new = malloc(sizeof(Node));
    new->data = data;
    new->next = *head;
    *head = new;
}

void delete_node(Node **head, int data) {
    Node *temp = *head, *prev = NULL;
    while (temp && temp->data != data) {
        prev = temp;
        temp = temp->next;
    }
    if (temp) {
        if (prev) prev->next = temp->next;
        else *head = temp->next;
        free(temp);
    }
}
```

## Exercise 2: Stack Implementation
Implement a stack using an array and a linked list.

## Exercise 3: Queue Implementation
Implement a circular queue.

## Exercise 4: Binary Search Tree
Implement a BST with insert, search, and in-order traversal.

## Exercise 5: Hash Table
Implement a hash table with separate chaining.

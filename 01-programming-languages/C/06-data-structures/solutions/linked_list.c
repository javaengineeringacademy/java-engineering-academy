/*
 * Linked List — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>

/* ============================================================
 * Problem 1-2: Node and LinkedList Structures
 * ============================================================ */
typedef struct Node {
    int data;
    struct Node *next;
} Node;

typedef struct {
    Node *head;
    int size;
} LinkedList;

/* ============================================================
 * Problem 3: Create Node
 * ============================================================ */
Node *create_node(int data) {
    Node *node = malloc(sizeof(Node));
    if (!node) {
        fprintf(stderr, "malloc failed\n");
        exit(EXIT_FAILURE);
    }
    node->data = data;
    node->next = NULL;
    return node;
}

/* ============================================================
 * Problem 4: Append
 * ============================================================ */
void append(LinkedList *list, int data) {
    Node *new_node = create_node(data);
    if (!list->head) {
        list->head = new_node;
    } else {
        Node *current = list->head;
        while (current->next) current = current->next;
        current->next = new_node;
    }
    list->size++;
}

/* ============================================================
 * Problem 5: Prepend
 * ============================================================ */
void prepend(LinkedList *list, int data) {
    Node *new_node = create_node(data);
    new_node->next = list->head;
    list->head = new_node;
    list->size++;
}

/* ============================================================
 * Problem 6: Insert At
 * ============================================================ */
void insert_at(LinkedList *list, int index, int data) {
    if (index < 0 || index > list->size) {
        fprintf(stderr, "Invalid index: %d\n", index);
        return;
    }

    if (index == 0) {
        prepend(list, data);
        return;
    }

    Node *new_node = create_node(data);
    Node *current = list->head;
    for (int i = 0; i < index - 1; i++) current = current->next;

    new_node->next = current->next;
    current->next = new_node;
    list->size++;
}

/* ============================================================
 * Problem 7: Delete Node
 * ============================================================ */
void delete_node(LinkedList *list, int data) {
    if (!list->head) return;

    if (list->head->data == data) {
        Node *temp = list->head;
        list->head = list->head->next;
        free(temp);
        list->size--;
        return;
    }

    Node *current = list->head;
    while (current->next && current->next->data != data) {
        current = current->next;
    }

    if (current->next) {
        Node *temp = current->next;
        current->next = temp->next;
        free(temp);
        list->size--;
    }
}

/* ============================================================
 * Problem 8: Search
 * ============================================================ */
int search(LinkedList *list, int data) {
    Node *current = list->head;
    int index = 0;
    while (current) {
        if (current->data == data) return index;
        current = current->next;
        index++;
    }
    return -1;
}

/* ============================================================
 * Problem 9: Reverse
 * ============================================================ */
void reverse(LinkedList *list) {
    Node *prev = NULL;
    Node *current = list->head;
    Node *next = NULL;

    while (current) {
        next = current->next;
        current->next = prev;
        prev = current;
        current = next;
    }
    list->head = prev;
}

/* ============================================================
 * Problem 10: Print List
 * ============================================================ */
void print_list(LinkedList *list) {
    Node *current = list->head;
    while (current) {
        printf("[%d]", current->data);
        if (current->next) printf(" -> ");
        current = current->next;
    }
    printf(" -> NULL\n");
}

/* ============================================================
 * Problem 11: Free List
 * ============================================================ */
void free_list(LinkedList *list) {
    Node *current = list->head;
    while (current) {
        Node *temp = current;
        current = current->next;
        free(temp);
    }
    list->head = NULL;
    list->size = 0;
}

/* ============================================================
 * Problem 12: Get Middle (Slow/Fast Pointer)
 * ============================================================ */
int get_middle(LinkedList *list) {
    if (!list->head) return -1;

    Node *slow = list->head;
    Node *fast = list->head;

    while (fast && fast->next) {
        slow = slow->next;
        fast = fast->next->next;
    }
    return slow->data;
}

/* ============================================================
 * Helper: Create List
 * ============================================================ */
LinkedList *create_list(void) {
    LinkedList *list = malloc(sizeof(LinkedList));
    if (!list) { fprintf(stderr, "malloc failed\n"); exit(EXIT_FAILURE); }
    list->head = NULL;
    list->size = 0;
    return list;
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Linked List — Solutions\n");
    printf("====================================\n\n");

    LinkedList *list = create_list();

    printf("  Append 10, 20, 30:\n");
    append(list, 10);
    append(list, 20);
    append(list, 30);
    printf("    "); print_list(list);

    printf("  Prepend 5:\n");
    prepend(list, 5);
    printf("    "); print_list(list);

    printf("  Insert 15 at index 2:\n");
    insert_at(list, 2, 15);
    printf("    "); print_list(list);

    printf("  Search for 15: index %d\n", search(list, 15));
    printf("  Search for 99: index %d\n", search(list, 99));
    printf("  Size: %d\n", list->size);
    printf("  Middle element: %d\n", get_middle(list));

    printf("  Reverse:\n");
    reverse(list);
    printf("    "); print_list(list);

    printf("  Delete 20:\n");
    delete_node(list, 20);
    printf("    "); print_list(list);

    free_list(list);
    printf("  List freed successfully\n\n");

    free(list);
    return 0;
}

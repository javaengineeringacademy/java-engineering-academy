/*
 * Exercise: Linked List Implementation in C
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Implement a singly linked list from scratch
 *   - Understand node creation and memory management
 *   - Practice list traversal and modification
 *   - Learn about head and tail pointers
 */

#include <stdio.h>
#include <stdlib.h>

/*
 * TODO 1: Define a Node structure with:
 * - data (int)
 * - next pointer to Node
 */

/*
 * TODO 2: Define a LinkedList structure with:
 * - head pointer to Node
 * - size (int)
 */

/*
 * TODO 3: Implement create_node(int data)
 * Allocate memory for a new node, initialize it
 * Handle malloc failure
 */

/*
 * TODO 4: Implement append(LinkedList *list, int data)
 * Add a new node at the end of the list
 */

/*
 * TODO 5: Implement prepend(LinkedList *list, int data)
 * Add a new node at the beginning of the list
 */

/*
 * TODO 6: Implement insert_at(LinkedList *list, int index, int data)
 * Insert a node at a specific position
 * Handle invalid index gracefully
 */

/*
 * TODO 7: Implement delete_node(LinkedList *list, int data)
 * Delete the first occurrence of a node with the given data
 * Free the memory of the deleted node
 */

/*
 * TODO 8: Implement search(LinkedList *list, int data)
 * Return the index of the node with the given data, -1 if not found
 */

/*
 * TODO 9: Implement reverse(LinkedList *list)
 * Reverse the linked list in place
 */

/*
 * TODO 10: Implement print_list(LinkedList *list)
 * Print all elements in format: [1] -> [2] -> [3] -> NULL
 */

/*
 * TODO 11: Implement free_list(LinkedList *list)
 * Free all nodes and the list structure itself
 */

/*
 * TODO 12: Implement get_middle(LinkedList *list)
 * Return the data of the middle node (use slow/fast pointer technique)
 */

int main(void) {
    /* Test cases */
    /*
    printf("=== Test Cases ===\n");

    LinkedList *list = create_list();

    // Test append
    append(list, 10);
    append(list, 20);
    append(list, 30);
    printf("After append 10, 20, 30: ");
    print_list(list); // [10] -> [20] -> [30] -> NULL

    // Test prepend
    prepend(list, 5);
    printf("After prepend 5: ");
    print_list(list); // [5] -> [10] -> [20] -> [30] -> NULL

    // Test insert_at
    insert_at(list, 2, 15);
    printf("After insert 15 at index 2: ");
    print_list(list); // [5] -> [10] -> [15] -> [20] -> [30] -> NULL

    // Test search
    printf("Index of 15: %d (expected: 2)\n", search(list, 15));
    printf("Index of 99: %d (expected: -1)\n", search(list, 99));

    // Test size
    printf("Size: %d (expected: 5)\n", list->size);

    // Test middle
    printf("Middle element: %d (expected: 15)\n", get_middle(list));

    // Test reverse
    reverse(list);
    printf("After reverse: ");
    print_list(list); // [30] -> [20] -> [15] -> [10] -> [5] -> NULL

    // Test delete
    delete_node(list, 20);
    printf("After deleting 20: ");
    print_list(list);

    // Free memory
    free_list(list);
    printf("List freed successfully\n");
    */

    return 0;
}

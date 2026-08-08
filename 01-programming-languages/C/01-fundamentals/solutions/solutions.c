/*
 * Fundamentals — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

/* ============================================================
 * Problem 1: Variables — Temperature Converter
 * ============================================================ */
double celsius_to_fahrenheit(double celsius) {
    return celsius * 9.0 / 5.0 + 32.0;
}

double celsius_to_kelvin(double celsius) {
    return celsius + 273.15;
}

void problem1_variables(void) {
    printf("=== Problem 1: Variables ===\n");
    double temps[] = {0.0, 100.0, -40.0, 37.5};
    for (int i = 0; i < 4; i++) {
        printf("%.1f°C = %.1f°F = %.1fK\n",
               temps[i],
               celsius_to_fahrenheit(temps[i]),
               celsius_to_kelvin(temps[i]));
    }
    printf("\n");
}

/* ============================================================
 * Problem 2: Operators — Bitwise Flags
 * ============================================================ */
#define PERM_READ    0x01
#define PERM_WRITE   0x02
#define PERM_EXECUTE 0x04
#define PERM_DELETE  0x08

void set_permission(unsigned int *perms, unsigned int flag) {
    *perms |= flag;
}

void clear_permission(unsigned int *perms, unsigned int flag) {
    *perms &= ~flag;
}

int check_permission(unsigned int perms, unsigned int flag) {
    return (perms & flag) != 0;
}

void print_permissions(unsigned int perms) {
    printf("  Permissions: ");
    if (perms == 0) {
        printf("(none)");
    } else {
        if (perms & PERM_READ)    printf("READ ");
        if (perms & PERM_WRITE)   printf("WRITE ");
        if (perms & PERM_EXECUTE) printf("EXECUTE ");
        if (perms & PERM_DELETE)  printf("DELETE ");
    }
    printf("\n");
}

void problem2_operators(void) {
    printf("=== Problem 2: Operators ===\n");
    unsigned int perms = 0;
    set_permission(&perms, PERM_READ);
    set_permission(&perms, PERM_WRITE);
    print_permissions(perms);

    printf("Has READ? %s\n", check_permission(perms, PERM_READ) ? "yes" : "no");
    printf("Has EXECUTE? %s\n", check_permission(perms, PERM_EXECUTE) ? "yes" : "no");

    clear_permission(&perms, PERM_WRITE);
    print_permissions(perms);
    printf("\n");
}

/* ============================================================
 * Problem 3: Control Flow — FizzBuzz with Prime
 * ============================================================ */
int is_prime(int n) {
    if (n < 2) return 0;
    if (n == 2) return 1;
    if (n % 2 == 0) return 0;
    for (int i = 3; i * i <= n; i += 2) {
        if (n % i == 0) return 0;
    }
    return 1;
}

void problem3_control_flow(void) {
    printf("=== Problem 3: Control Flow ===\n");
    for (int i = 1; i <= 100; i++) {
        int fizz = (i % 3 == 0);
        int buzz = (i % 5 == 0);
        int prime = is_prime(i);

        if (fizz && buzz) {
            if (prime) printf("FizzBuzz+Prime");
            else       printf("FizzBuzz");
        } else if (fizz) {
            printf("Fizz");
        } else if (buzz) {
            printf("Buzz");
        } else if (prime) {
            printf("Prime");
        } else {
            printf("%d", i);
        }
        printf(" ");
    }
    printf("\n\n");
}

/* ============================================================
 * Problem 4: Functions — Recursive Array Operations
 * ============================================================ */
int array_sum(int *arr, int size) {
    if (size <= 0) return 0;
    return arr[0] + array_sum(arr + 1, size - 1);
}

int array_max(int *arr, int size) {
    if (size == 1) return arr[0];
    int rest_max = array_max(arr + 1, size - 1);
    return (arr[0] > rest_max) ? arr[0] : rest_max;
}

void array_reverse(int *arr, int size) {
    if (size <= 1) return;
    int temp = arr[0];
    arr[0] = arr[size - 1];
    arr[size - 1] = temp;
    array_reverse(arr + 1, size - 2);
}

int array_count_occurrences(int *arr, int size, int target) {
    if (size <= 0) return 0;
    int count = (arr[0] == target) ? 1 : 0;
    return count + array_count_occurrences(arr + 1, size - 1, target);
}

void problem4_functions(void) {
    printf("=== Problem 4: Functions ===\n");
    int arr[] = {3, 7, 2, 9, 4, 7, 1, 8, 7};
    int size = sizeof(arr) / sizeof(arr[0]);

    printf("Array: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");

    printf("Sum: %d\n", array_sum(arr, size));
    printf("Max: %d\n", array_max(arr, size));
    printf("Count of 7: %d\n", array_count_occurrences(arr, size, 7));

    array_reverse(arr, size);
    printf("Reversed: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n\n");
}

/* ============================================================
 * Problem 5: Strings — String Utilities (no string.h)
 * ============================================================ */
int my_strlen(const char *s) {
    int len = 0;
    while (s[len] != '\0') len++;
    return len;
}

void my_strcpy(char *dest, const char *src) {
    while (*src != '\0') {
        *dest++ = *src++;
    }
    *dest = '\0';
}

void my_strcat(char *dest, const char *src) {
    while (*dest != '\0') dest++;
    while (*src != '\0') {
        *dest++ = *src++;
    }
    *dest = '\0';
}

int my_strcmp(const char *a, const char *b) {
    while (*a != '\0' && *a == *b) {
        a++;
        b++;
    }
    return (unsigned char)*a - (unsigned char)*b;
}

void reverse_string(char *s) {
    int len = my_strlen(s);
    for (int i = 0; i < len / 2; i++) {
        char temp = s[i];
        s[i] = s[len - 1 - i];
        s[len - 1 - i] = temp;
    }
}

void problem5_strings(void) {
    printf("=== Problem 5: Strings ===\n");
    char dest[100] = "Hello";
    const char *src = " World!";

    printf("Length of \"%s\": %d\n", dest, my_strlen(dest));
    my_strcat(dest, src);
    printf("After strcat: \"%s\"\n", dest);

    reverse_string(dest);
    printf("After reverse: \"%s\"\n", dest);

    printf("strcmp(\"abc\", \"abd\"): %d\n", my_strcmp("abc", "abd"));
    printf("strcmp(\"abc\", \"abc\"): %d\n", my_strcmp("abc", "abc"));
    printf("strcmp(\"abd\", \"abc\"): %d\n", my_strcmp("abd", "abc"));
    printf("\n");
}

/* ============================================================
 * Problem 6: Arrays — Array Operations
 * ============================================================ */
void problem6_arrays(void) {
    printf("=== Problem 6: Arrays ===\n");
    int arr[] = {10, 20, 30, 40, 50};
    int size = sizeof(arr) / sizeof(arr[0]);

    printf("  Original: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");

    int sum = 0;
    for (int i = 0; i < size; i++) sum += arr[i];
    printf("  Sum: %d\n", sum);
    printf("  Average: %.1f\n", (double)sum / size);

    int max = arr[0], min = arr[0];
    for (int i = 1; i < size; i++) {
        if (arr[i] > max) max = arr[i];
        if (arr[i] < min) min = arr[i];
    }
    printf("  Max: %d, Min: %d\n", max, min);

    for (int i = 0; i < size / 2; i++) {
        int temp = arr[i];
        arr[i] = arr[size - 1 - i];
        arr[size - 1 - i] = temp;
    }
    printf("  Reversed: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n\n");
}

/* ============================================================
 * Problem 7: Pointers — Pointer Fundamentals
 * ============================================================ */
void swap(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

void problem7_pointers(void) {
    printf("=== Problem 7: Pointers ===\n");
    int x = 42;
    int *ptr = &x;
    printf("  Value: %d, Address: %p\n", x, (void *)ptr);
    printf("  Dereferenced: %d\n", *ptr);

    *ptr = 100;
    printf("  After *ptr = 100: x = %d\n", x);

    int a = 10, b = 20;
    printf("  Before swap: a=%d, b=%d\n", a, b);
    swap(&a, &b);
    printf("  After swap:  a=%d, b=%d\n", a, b);

    int arr[] = {1, 2, 3, 4, 5};
    int *p = arr;
    printf("  Array via pointer: ");
    for (int i = 0; i < 5; i++) printf("%d ", *(p + i));
    printf("\n\n");
}

/* ============================================================
 * Problem 8: Memory — Dynamic Memory Allocation
 * ============================================================ */
void problem8_memory(void) {
    printf("=== Problem 8: Memory ===\n");

    int *ip = malloc(sizeof(int));
    if (ip) { *ip = 42; printf("  Malloc int: %d\n", *ip); free(ip); }

    int n = 5;
    int *arr = calloc(n, sizeof(int));
    if (arr) {
        for (int i = 0; i < n; i++) arr[i] = (i + 1) * 10;
        printf("  Calloc array: ");
        for (int i = 0; i < n; i++) printf("%d ", arr[i]);
        printf("\n");

        int *tmp = realloc(arr, 10 * sizeof(int));
        if (tmp) {
            arr = tmp;
            for (int i = 5; i < 10; i++) arr[i] = (i + 1) * 10;
            printf("  After realloc: ");
            for (int i = 0; i < 10; i++) printf("%d ", arr[i]);
            printf("\n");
        }
        free(arr);
    }
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Fundamentals — Solutions\n");
    printf("====================================\n\n");

    problem1_variables();
    problem2_operators();
    problem3_control_flow();
    problem4_functions();
    problem5_strings();
    problem6_arrays();
    problem7_pointers();
    problem8_memory();

    return 0;
}

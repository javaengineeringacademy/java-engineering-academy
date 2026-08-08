/*
 * Best Practices — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o best_practices best_practices.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <limits.h>

/* ============================================================
 * Problem 1: Naming Conventions
 * ============================================================ */

#define MAX_BUFFER_SIZE 1024

typedef struct {
    int x;
    int y;
} Point;

int calculate_area(int width, int height) {
    return width * height;
}

void problem1_naming(void) {
    printf("=== Problem 1: Naming Conventions ===\n");

    int num_students = 42;
    int area = calculate_area(10, 5);
    Point p = { .x = 3, .y = 7 };

    printf("Students: %d\n", num_students);
    printf("Area: %d\n", area);
    printf("Point: (%d, %d)\n", p.x, p.y);
    printf("Max buffer: %d\n\n", MAX_BUFFER_SIZE);
}

/* ============================================================
 * Problem 2: Defensive Programming
 * ============================================================ */

int sum_array(int *arr, int size, int *result) {
    if (!arr || !result) return -1;
    if (size <= 0) return -1;

    long sum = 0;
    for (int i = 0; i < size; i++) {
        sum += arr[i];
        if (sum > INT_MAX || sum < INT_MIN) return -2;
    }
    *result = (int)sum;
    return 0;
}

char *safe_strdup(const char *s) {
    if (!s) return NULL;

    size_t len = strlen(s);
    char *copy = malloc(len + 1);
    if (!copy) return NULL;

    memcpy(copy, s, len + 1);
    return copy;
}

int parse_int(const char *str, int *out) {
    if (!str || !out) return -1;

    char *endptr;
    errno = 0;
    long val = strtol(str, &endptr, 10);

    if (errno != 0 || endptr == str || *endptr != '\0') return -1;
    if (val > INT_MAX || val < INT_MIN) return -2;

    *out = (int)val;
    return 0;
}

void problem2_defensive(void) {
    printf("=== Problem 2: Defensive Programming ===\n");

    int arr[] = {1, 2, 3, 4, 5};
    int result = 0;
    if (sum_array(arr, 5, &result) == 0) {
        printf("Sum: %d\n", result);
    }

    if (sum_array(NULL, 0, &result) != 0) {
        printf("Null check passed\n");
    }

    char *dup = safe_strdup("Hello, defensive programming!");
    if (dup) {
        printf("Dup: %s\n", dup);
        free(dup);
    }

    int parsed = 0;
    if (parse_int("42", &parsed) == 0) {
        printf("Parsed: %d\n", parsed);
    }
    if (parse_int("not_a_number", &parsed) != 0) {
        printf("Invalid input rejected\n");
    }
    printf("\n");
}

/* ============================================================
 * Problem 3: Error Handling Patterns
 * ============================================================ */

typedef enum {
    ERR_OK = 0,
    ERR_NULL_PTR,
    ERR_OUT_OF_MEMORY,
    ERR_INVALID_ARG,
    ERR_IO
} ErrorCode;

/* Pattern 1: Return codes */
ErrorCode process_data_return(int *data, int size, int *result) {
    if (!data || !result) return ERR_NULL_PTR;
    if (size <= 0) return ERR_INVALID_ARG;

    int *temp = malloc(size * sizeof(int));
    if (!temp) return ERR_OUT_OF_MEMORY;

    for (int i = 0; i < size; i++) {
        temp[i] = data[i] * 2;
    }

    long sum = 0;
    for (int i = 0; i < size; i++) {
        sum += temp[i];
    }
    free(temp);

    if (sum > INT_MAX) return ERR_OUT_OF_MEMORY;
    *result = (int)sum;
    return ERR_OK;
}

/* Pattern 2: Goto cleanup */
int process_data_goto(int *data, int size) {
    int *temp = NULL;
    int result = 0;

    if (!data || size <= 0) goto error;

    temp = malloc(size * sizeof(int));
    if (!temp) goto error;

    for (int i = 0; i < size; i++) {
        temp[i] = data[i] + 10;
    }

    for (int i = 0; i < size; i++) {
        result += temp[i];
    }

    free(temp);
    return result;

error:
    free(temp);
    return -1;
}

const char *error_string(ErrorCode err) {
    switch (err) {
        case ERR_OK:             return "OK";
        case ERR_NULL_PTR:       return "Null pointer";
        case ERR_OUT_OF_MEMORY:  return "Out of memory";
        case ERR_INVALID_ARG:    return "Invalid argument";
        case ERR_IO:             return "I/O error";
        default:                 return "Unknown error";
    }
}

void problem3_error_handling(void) {
    printf("=== Problem 3: Error Handling Patterns ===\n");

    int data[] = {1, 2, 3, 4, 5};
    int result = 0;

    ErrorCode err = process_data_return(data, 5, &result);
    printf("Return code pattern: %s, result=%d\n", error_string(err), result);

    int goto_result = process_data_goto(data, 5);
    printf("Goto cleanup pattern: result=%d\n", goto_result);

    err = process_data_return(NULL, 0, &result);
    printf("Error case: %s\n", error_string(err));
    printf("\n");
}

/* ============================================================
 * Problem 4: Documentation Standards
 * ============================================================ */

/**
 * Calculate the greatest common divisor of two integers.
 *
 * Uses the Euclidean algorithm to find the GCD.
 * Both parameters must be non-negative.
 *
 * @param a First non-negative integer
 * @param b Second non-negative integer
 * @return The GCD of a and b, or 0 if both are 0
 *
 * @pre a >= 0 && b >= 0
 * @post result * (a/gcd + b/gcd) == a + b (for gcd > 0)
 *
 * @code
 *   int g = gcd(12, 8);  // Returns 4
 *   int g2 = gcd(7, 13); // Returns 1
 * @endcode
 */
int gcd(int a, int b) {
    if (a < 0 || b < 0) return 0;
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}

/**
 * Perform binary search on a sorted integer array.
 *
 * Requires the array to be sorted in ascending order.
 * Returns the index of the target if found, -1 otherwise.
 *
 * @param arr    Pointer to a sorted integer array
 * @param size   Number of elements in the array (must be >= 0)
 * @param target The value to search for
 * @return Index of target in [0, size), or -1 if not found
 *
 * @pre arr != NULL || size == 0
 * @pre Array is sorted in ascending order
 * @post Returns -1 if target not in arr[0..size-1]
 *
 * @code
 *   int arr[] = {1, 3, 5, 7, 9};
 *   int idx = binary_search(arr, 5, 7); // Returns 3
 *   int idx2 = binary_search(arr, 5, 4); // Returns -1
 * @endcode
 */
int binary_search(int *arr, int size, int target) {
    if (!arr || size <= 0) return -1;

    int low = 0, high = size - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) low = mid + 1;
        else high = mid - 1;
    }
    return -1;
}

void problem4_documentation(void) {
    printf("=== Problem 4: Documentation Standards ===\n");

    printf("gcd(12, 8) = %d\n", gcd(12, 8));
    printf("gcd(7, 13) = %d\n", gcd(7, 13));
    printf("gcd(0, 5) = %d\n", gcd(0, 5));

    int arr[] = {1, 3, 5, 7, 9, 11};
    int idx = binary_search(arr, 6, 7);
    printf("binary_search({1,3,5,7,9,11}, 7) = %d\n", idx);
    idx = binary_search(arr, 6, 4);
    printf("binary_search({1,3,5,7,9,11}, 4) = %d\n", idx);
    printf("\n");
}

/* ============================================================
 * Problem 5: Code Organization
 * Demonstrates proper file structure:
 * 1. Header comment
 * 2. Includes
 * 3. Macros
 * 4. Types
 * 5. Static forward declarations
 * 6. Public forward declarations
 * 7. Static implementations
 * 8. Public implementations
 * 9. Main
 * ============================================================ */

/* --- Static helper functions --- */

static void to_uppercase(char *dest, const char *src) {
    while (*src) {
        *dest++ = (char)toupper((unsigned char)*src++);
    }
    *dest = '\0';
}

static int count_char(const char *s, char c) {
    int count = 0;
    while (*s) {
        if (*s == c) count++;
        s++;
    }
    return count;
}

/* --- Public API functions --- */

int clamp(int value, int min_val, int max_val) {
    if (value < min_val) return min_val;
    if (value > max_val) return max_val;
    return value;
}

void string_to_upper(char *dest, const char *src) {
    if (!dest || !src) return;
    to_uppercase(dest, src);
}

int char_frequency(const char *str, char ch) {
    if (!str) return 0;
    return count_char(str, ch);
}

void problem5_organization(void) {
    printf("=== Problem 5: Code Organization ===\n");

    printf("clamp(15, 0, 10) = %d\n", clamp(15, 0, 10));
    printf("clamp(-5, 0, 10) = %d\n", clamp(-5, 0, 10));
    printf("clamp(7, 0, 10) = %d\n", clamp(7, 0, 10));

    char upper[100];
    string_to_upper(upper, "hello world");
    printf("Uppercase: %s\n", upper);

    printf("Frequency of 'l' in 'hello': %d\n", char_frequency("hello", 'l'));
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */

int main(void) {
    printf("====================================\n");
    printf("  Best Practices — Solutions\n");
    printf("====================================\n\n");

    problem1_naming();
    problem2_defensive();
    problem3_error_handling();
    problem4_documentation();
    problem5_organization();

    return 0;
}

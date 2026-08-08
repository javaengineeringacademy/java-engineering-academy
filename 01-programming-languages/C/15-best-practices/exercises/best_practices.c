/*
 * Exercise: Best Practices in C
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Understand naming conventions and code style
 *   - Practice defensive programming techniques
 *   - Learn about error handling patterns
 *   - Master documentation and commenting standards
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Problem 1: Naming Conventions
 *
 * Rewrite the following poorly named code using proper
 * C naming conventions:
 * - Functions: snake_case, descriptive verbs
 * - Variables: snake_case, descriptive nouns
 * - Macros/Constants: UPPER_SNAKE_CASE
 * - Types: PascalCase or snake_case with _t suffix
 * ============================================================ */
/* BAD EXAMPLES: */
/* int x; */
/* int calc(int a, int b); */
/* #define maxsize 100 */
/* typedef struct { int x; int y; } point; */

/* TODO: Rewrite with proper naming:
 * - int num_students;
 * - int calculate_area(int width, int height);
 * - #define MAX_BUFFER_SIZE 1024
 * - typedef struct { int x; int y; } Point;
 */

/* ============================================================
 * Problem 2: Defensive Programming
 *
 * Add proper error checking to the following functions:
 * - Null pointer checks
 * - Array bounds checking
 * - Return value validation
 * - Resource cleanup on failure
 * ============================================================ */
int sum_array(int *arr, int size, int *result) {
    /* TODO: Add defensive checks before computation */
    (void)arr; (void)size; (void)result;
    return -1;
}

char *safe_strdup(const char *s) {
    /* TODO: Check for NULL, handle allocation failure */
    (void)s;
    return NULL;
}

int parse_int(const char *str, int *out) {
    /* TODO: Validate input, check for overflow, handle errors */
    (void)str; (void)out;
    return -1;
}

void problem2_defensive(void) {
    printf("TODO: Problem 2 - Defensive Programming\n\n");
}

/* ============================================================
 * Problem 3: Error Handling Patterns
 *
 * Implement three error handling patterns:
 * 1. Return code with error enum
 * 2. goto-based cleanup pattern
 * 3. Setjmp/longjmp (conceptual)
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
    /* TODO: Use return codes for error handling */
    (void)data; (void)size; (void)result;
    return ERR_OK;
}

/* Pattern 2: Goto cleanup */
int process_data_goto(int *data, int size) {
    int *temp = NULL;
    int result = 0;

    /* TODO: Use goto for cleanup on error
     * if (data == NULL) goto error;
     * temp = malloc(...);
     * if (!temp) goto error;
     * ... do work ...
     * free(temp);
     * return result;
     *
     * error:
     *     free(temp);
     *     return -1;
     */
    (void)data; (void)size; (void)temp; (void)result;
    return 0;
}

void problem3_error_handling(void) {
    printf("TODO: Problem 3 - Error Handling Patterns\n\n");
}

/* ============================================================
 * Problem 4: Documentation Standards
 *
 * Write proper documentation for these functions using
 * a standard format (Doxygen-style or similar):
 * - Function description
 * - Parameter descriptions
 * - Return value description
 * - Pre-conditions
 * - Example usage
 * ============================================================ */
/* TODO: Document these functions:
 *
 * /**
 *  * Calculate the greatest common divisor of two integers.
 *  *
 *  * Uses the Euclidean algorithm to find the GCD.
 *  * Both parameters must be non-negative.
 *  *
 *  * @param a First non-negative integer
 *  * @param b Second non-negative integer
 *  * @return The GCD of a and b, or 0 if both are 0
 *  *
 *  * @pre a >= 0 && b >= 0
 *  * @post result * (a/gcd + b/gcd) == a + b (for gcd > 0)
 *  *
 *  * @code
 *  *   int g = gcd(12, 8);  // Returns 4
 *  *   int g2 = gcd(7, 13); // Returns 1
 *  * @endcode
 *  *\/
 */

int gcd(int a, int b) {
    /* TODO: Implement with documentation */
    (void)a; (void)b;
    return 0;
}

/* TODO: Document this function */
int binary_search(int *arr, int size, int target) {
    /* TODO: Implement with documentation */
    (void)arr; (void)size; (void)target;
    return -1;
}

void problem4_documentation(void) {
    printf("TODO: Problem 4 - Documentation Standards\n\n");
}

/* ============================================================
 * Problem 5: Code Organization
 *
 * Show proper code organization:
 * - Forward declarations at the top
 * - Static helper functions grouped together
 * - Public API functions
 * - Main function last
 * ============================================================ */
/* TODO: Demonstrate proper code organization
 *
 * File structure:
 * 1. Header comment (file description, author, date)
 * 2. #include directives (system, then local)
 * 3. Macro definitions
 * 4. Type definitions
 * 5. Static (private) function forward declarations
 * 6. Public function forward declarations
 * 7. Static (private) function implementations
 * 8. Public function implementations
 * 9. Main function
 */

void problem5_organization(void) {
    printf("TODO: Problem 5 - Code Organization\n\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Best Practices — Exercises\n");
    printf("====================================\n\n");

    problem2_defensive();
    problem3_error_handling();
    problem4_documentation();
    problem5_organization();

    printf("Review the code above and apply best practices.\n");
    printf("Focus on: clarity, safety, maintainability.\n\n");

    return 0;
}

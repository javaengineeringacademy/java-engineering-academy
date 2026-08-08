/*
 * Exercise: Testing in C
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Understand unit testing in C
 *   - Practice writing test assertions
 *   - Learn about test frameworks and patterns
 *   - Master code coverage and test organization
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Sample code to test — A simple string utility library
 * ============================================================ */
int string_length(const char *s) {
    if (!s) return -1;
    int len = 0;
    while (s[len]) len++;
    return len;
}

int string_count_char(const char *s, char c) {
    if (!s) return -1;
    int count = 0;
    for (int i = 0; s[i]; i++) {
        if (s[i] == c) count++;
    }
    return count;
}

int string_reverse_copy(const char *src, char *dest, int dest_size) {
    if (!src || !dest || dest_size <= 0) return -1;
    int len = string_length(src);
    if (len >= dest_size) return -1;

    for (int i = 0; i < len; i++) {
        dest[i] = src[len - 1 - i];
    }
    dest[len] = '\0';
    return 0;
}

int string_to_upper(const char *src, char *dest, int dest_size) {
    if (!src || !dest || dest_size <= 0) return -1;
    int len = string_length(src);
    if (len >= dest_size) return -1;

    for (int i = 0; i <= len; i++) {
        dest[i] = (src[i] >= 'a' && src[i] <= 'z') ? src[i] - 32 : src[i];
    }
    return 0;
}

/* ============================================================
 * Problem 1: Test Assertion Macros
 *
 * Create a set of test assertion macros:
 * - ASSERT_EQ(expected, actual)
 * - ASSERT_STR_EQ(expected, actual)
 * - ASSERT_TRUE(condition)
 * - TEST_BEGIN(name) / TEST_END()
 * ============================================================ */
/* TODO: Define test assertion macros that:
 * 1. Print PASS/FAIL with file and line info
 * 2. Track total tests and failures
 * 3. Use __FILE__ and __LINE__ for location
 */

/* ============================================================
 * Problem 2: Unit Tests for string_length
 *
 * Write tests covering:
 * - Normal string
 * - Empty string
 * - NULL pointer
 * - Single character
 * - Long string
 * ============================================================ */
void test_string_length(void) {
    /* TODO: Write 5 test cases for string_length() */
    printf("TODO: Problem 2 - string_length tests\n\n");
}

/* ============================================================
 * Problem 3: Unit Tests for string_count_char
 *
 * Write tests covering:
 * - Character present multiple times
 * - Character not present
 * - Empty string
 * - NULL pointer
 * - Count of space character
 * ============================================================ */
void test_string_count_char(void) {
    /* TODO: Write 5 test cases for string_count_char() */
    printf("TODO: Problem 3 - string_count_char tests\n\n");
}

/* ============================================================
 * Problem 4: Test Fixtures and Setup/Teardown
 *
 * Create a test framework with:
 * - A setup function that allocates test resources
 * - A teardown function that frees test resources
 * - A test runner that calls setup/teardown around each test
 * ============================================================ */
typedef struct {
    char *buffer;
    int size;
} TestFixture;

TestFixture *fixture_setup(int size) {
    /* TODO: Allocate and initialize test fixture */
    (void)size;
    return NULL;
}

void fixture_teardown(TestFixture *fixture) {
    /* TODO: Free test fixture resources */
    (void)fixture;
}

void test_with_fixture(TestFixture *fix) {
    /* TODO: Use the fixture in a test */
    (void)fix;
}

void problem4_fixtures(void) {
    printf("TODO: Problem 4 - Test Fixtures\n\n");
}

/* ============================================================
 * Problem 5: Test Suite Organization
 *
 * Organize tests into a suite structure:
 * - Test suite with name and array of test functions
 * - Run all tests in a suite and report results
 * - Support multiple test suites
 * ============================================================ */
typedef void (*TestFunc)(void);

typedef struct {
    const char *name;
    TestFunc *tests;
    int count;
} TestSuite;

/* TODO: Implement test suite functions:
 * - suite_run(TestSuite *suite): runs all tests, prints results
 * - Create suites for string_length, string_count_char, etc.
 */

void problem5_test_suites(void) {
    printf("TODO: Problem 5 - Test Suites\n\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Testing — Exercises\n");
    printf("====================================\n\n");

    problem4_fixtures();
    problem5_test_suites();

    printf("Implement the test functions above.\n");
    printf("Aim for high code coverage of the string utility functions.\n\n");

    return 0;
}

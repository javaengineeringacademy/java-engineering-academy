/*
 * Testing — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Sample code to test — String utility library
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
    for (int i = 0; i < len; i++) dest[i] = src[len - 1 - i];
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
 * ============================================================ */
static int total_tests = 0;
static int failed_tests = 0;

#define ASSERT_EQ(expected, actual, msg) do { \
    total_tests++; \
    if ((expected) != (actual)) { \
        printf("  FAIL: %s (expected %d, got %d) at %s:%d\n", \
               msg, (int)(expected), (int)(actual), __FILE__, __LINE__); \
        failed_tests++; \
    } else { \
        printf("  PASS: %s\n", msg); \
    } \
} while(0)

#define ASSERT_STR_EQ(expected, actual, msg) do { \
    total_tests++; \
    if (strcmp((expected), (actual)) != 0) { \
        printf("  FAIL: %s (expected \"%s\", got \"%s\") at %s:%d\n", \
               msg, (expected), (actual), __FILE__, __LINE__); \
        failed_tests++; \
    } else { \
        printf("  PASS: %s\n", msg); \
    } \
} while(0)

#define ASSERT_TRUE(cond, msg) do { \
    total_tests++; \
    if (!(cond)) { \
        printf("  FAIL: %s at %s:%d\n", msg, __FILE__, __LINE__); \
        failed_tests++; \
    } else { \
        printf("  PASS: %s\n", msg); \
    } \
} while(0)

#define TEST_BEGIN(name) printf("  --- %s ---\n", name)
#define TEST_END() printf("\n")

/* ============================================================
 * Problem 2: Unit Tests for string_length
 * ============================================================ */
void test_string_length(void) {
    TEST_BEGIN("string_length");

    ASSERT_EQ(5, string_length("Hello"), "length of \"Hello\"");
    ASSERT_EQ(0, string_length(""), "length of empty string");
    ASSERT_EQ(-1, string_length(NULL), "length of NULL");
    ASSERT_EQ(1, string_length("A"), "length of single char");
    ASSERT_EQ(13, string_length("Hello, World!"), "length of \"Hello, World!\"");

    TEST_END();
}

/* ============================================================
 * Problem 3: Unit Tests for string_count_char
 * ============================================================ */
void test_string_count_char(void) {
    TEST_BEGIN("string_count_char");

    ASSERT_EQ(2, string_count_char("Hello", 'l'), "count 'l' in \"Hello\"");
    ASSERT_EQ(0, string_count_char("Hello", 'z'), "count 'z' in \"Hello\"");
    ASSERT_EQ(0, string_count_char("", 'a'), "count in empty string");
    ASSERT_EQ(-1, string_count_char(NULL, 'a'), "count in NULL");
    ASSERT_EQ(2, string_count_char("a b c a", ' '), "count spaces");

    TEST_END();
}

/* ============================================================
 * Problem 4: Test Fixtures
 * ============================================================ */
typedef struct {
    char *buffer;
    int size;
} TestFixture;

TestFixture *fixture_setup(int size) {
    TestFixture *fix = malloc(sizeof(TestFixture));
    if (!fix) return NULL;
    fix->size = size;
    fix->buffer = calloc(size, 1);
    if (!fix->buffer) { free(fix); return NULL; }
    return fix;
}

void fixture_teardown(TestFixture *fixture) {
    if (fixture) {
        free(fixture->buffer);
        free(fixture);
    }
}

void problem4_fixtures(void) {
    printf("=== Problem 4: Test Fixtures ===\n");

    TestFixture *fix = fixture_setup(100);
    ASSERT_TRUE(fix != NULL, "fixture created");
    ASSERT_TRUE(fix->buffer != NULL, "buffer allocated");
    ASSERT_EQ(100, fix->size, "buffer size is 100");

    /* Use fixture in tests */
    int rc = string_reverse_copy("test", fix->buffer, fix->size);
    ASSERT_EQ(0, rc, "reverse copy succeeds");
    ASSERT_STR_EQ("tset", fix->buffer, "reverse of \"test\"");

    fixture_teardown(fix);
    printf("  Fixture cleaned up\n\n");
}

/* ============================================================
 * Problem 5: Test Suites
 * ============================================================ */
typedef void (*TestFunc)(void);

typedef struct {
    const char *name;
    TestFunc *tests;
    int count;
} TestSuite;

int suite_run(TestSuite *suite) {
    printf("=== Test Suite: %s ===\n", suite->name);
    int before = failed_tests;
    for (int i = 0; i < suite->count; i++) {
        suite->tests[i]();
    }
    return failed_tests - before;
}

void test_string_reverse(void) {
    TEST_BEGIN("string_reverse_copy");
    char buf[50];

    ASSERT_EQ(0, string_reverse_copy("hello", buf, 50), "reverse \"hello\"");
    ASSERT_STR_EQ("olleh", buf, "result is \"olleh\"");

    ASSERT_EQ(0, string_reverse_copy("a", buf, 50), "reverse single char");
    ASSERT_STR_EQ("a", buf, "result is \"a\"");

    ASSERT_EQ(-1, string_reverse_copy("long", buf, 3), "dest too small");
    ASSERT_EQ(-1, string_reverse_copy(NULL, buf, 50), "NULL src");
    ASSERT_EQ(-1, string_reverse_copy("test", NULL, 50), "NULL dest");

    TEST_END();
}

void test_string_upper(void) {
    TEST_BEGIN("string_to_upper");
    char buf[50];

    ASSERT_EQ(0, string_to_upper("hello", buf, 50), "upper \"hello\"");
    ASSERT_STR_EQ("HELLO", buf, "result is \"HELLO\"");

    ASSERT_EQ(0, string_to_upper("HeLLo WoRLd", buf, 50), "mixed case");
    ASSERT_STR_EQ("HELLO WORLD", buf, "result is \"HELLO WORLD\"");

    ASSERT_EQ(0, string_to_upper("123!@#", buf, 50), "non-alpha unchanged");
    ASSERT_STR_EQ("123!@#", buf, "result unchanged");

    ASSERT_EQ(-1, string_to_upper(NULL, buf, 50), "NULL src");

    TEST_END();
}

void problem5_test_suites(void) {
    printf("=== Problem 5: Test Suites ===\n\n");

    /* String Length Suite */
    TestFunc length_tests[] = { test_string_length };
    TestSuite length_suite = { "String Length", length_tests, 1 };
    suite_run(&length_suite);

    /* String Count Char Suite */
    TestFunc count_tests[] = { test_string_count_char };
    TestSuite count_suite = { "String Count Char", count_tests, 1 };
    suite_run(&count_suite);

    /* Reverse & Upper Suite */
    TestFunc reverse_upper_tests[] = { test_string_reverse, test_string_upper };
    TestSuite reverse_upper_suite = { "Reverse & Upper", reverse_upper_tests, 2 };
    suite_run(&reverse_upper_suite);

    /* Fixtures Suite */
    TestFunc fixture_tests[] = { problem4_fixtures };
    TestSuite fixture_suite = { "Fixtures", fixture_tests, 1 };
    suite_run(&fixture_suite);

    printf("\n====================================\n");
    printf("  Total: %d tests, %d passed, %d failed\n",
           total_tests, total_tests - failed_tests, failed_tests);
    printf("====================================\n\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Testing — Solutions\n");
    printf("====================================\n\n");

    problem5_test_suites();

    return failed_tests > 0 ? 1 : 0;
}

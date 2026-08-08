/*
 * Exercise: Security in C
 * Difficulty: ★★★★★ (5/5)
 * Learning Objectives:
 *   - Understand common C vulnerabilities (buffer overflow, format strings)
 *   - Practice safe string handling
 *   - Learn about input validation and sanitization
 *   - Master cryptographic basics with OpenSSL
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

/* ============================================================
 * Problem 1: Buffer Overflow Prevention
 *
 * Write safe versions of string functions that prevent
 * buffer overflow:
 * - safe_strcpy: copies at most n-1 characters
 * - safe_strcat: concatenates with bounds checking
 * - safe_gets: reads a line with length limit
 * ============================================================ */
void safe_strcpy(char *dest, const char *src, size_t dest_size) {
    /* TODO: Copy at most dest_size-1 characters, always null-terminate */
    (void)dest; (void)src; (void)dest_size;
}

void safe_strcat(char *dest, const char *src, size_t dest_size) {
    /* TODO: Append src to dest, never exceed dest_size */
    (void)dest; (void)src; (void)dest_size;
}

int safe_gets(char *buf, size_t size) {
    /* TODO: Read a line, limit to size-1 chars, strip newline */
    (void)buf; (void)size;
    return -1;
}

void problem1_buffer_overflow(void) {
    printf("TODO: Problem 1 - Buffer Overflow Prevention\n\n");
}

/* ============================================================
 * Problem 2: Format String Vulnerability
 *
 * Write a safe printing function that uses format strings
 * correctly. Show the danger of passing user input as format.
 * Demonstrate the fix using "%s" format.
 * ============================================================ */
void unsafe_print(const char *user_input) {
    /* DANGEROUS: user_input used as format string */
    printf(user_input);  /* Intentionally vulnerable */
}

void safe_print(const char *user_input) {
    /* TODO: Fix the format string vulnerability */
    (void)user_input;
}

void problem2_format_strings(void) {
    /* TODO: Demonstrate vulnerability and fix */
    printf("TODO: Problem 2 - Format String Safety\n\n");
}

/* ============================================================
 * Problem 3: Input Validation
 *
 * Write validation functions for:
 * - Email address (basic check for @ and domain)
 * - Integer within range
 * - Alphanumeric string
 * - SQL injection prevention (escape special chars)
 * ============================================================ */
int validate_email(const char *email) {
    /* TODO: Check for exactly one '@', domain has '.', no spaces */
    (void)email;
    return 0;
}

int validate_int_range(const char *input, int min, int max, int *result) {
    /* TODO: Parse integer and check if within [min, max] */
    (void)input; (void)min; (void)max; (void)result;
    return 0;
}

int validate_alphanumeric(const char *str) {
    /* TODO: Return 1 if all characters are alphanumeric */
    (void)str;
    return 0;
}

void sanitize_sql(const char *input, char *output, size_t out_size) {
    /* TODO: Escape single quotes and other dangerous chars */
    (void)input; (void)output; (void)out_size;
}

void problem3_input_validation(void) {
    printf("TODO: Problem 3 - Input Validation\n\n");
}

/* ============================================================
 * Problem 4: Safe Memory Handling
 *
 * Write functions that securely handle sensitive data:
 * - Secure zeroing (prevent compiler optimization from removing)
 * - Safe comparison (constant-time to prevent timing attacks)
 * - Safe free (zero memory before freeing)
 * ============================================================ */
void secure_zero(void *ptr, size_t size) {
    /* TODO: Zero memory in a way the compiler cannot optimize away */
    (void)ptr; (void)size;
}

int secure_compare(const void *a, const void *b, size_t size) {
    /* TODO: Constant-time comparison to prevent timing attacks */
    (void)a; (void)b; (void)size;
    return 0;
}

void secure_free(void **ptr, size_t size) {
    /* TODO: Zero memory, then free, then set pointer to NULL */
    (void)ptr; (void)size;
}

void problem4_secure_memory(void) {
    printf("TODO: Problem 4 - Secure Memory Handling\n\n");
}

/* ============================================================
 * Problem 5: Simple Encryption (XOR Cipher)
 *
 * Implement a simple XOR-based encryption/decryption:
 * - encrypt: XOR each byte with a key byte (cycling the key)
 * - decrypt: same operation (XOR is its own inverse)
 * - Print encrypted bytes as hex
 * ============================================================ */
void xor_encrypt(const char *plaintext, const char *key, char *output, size_t out_size) {
    /* TODO: XOR each byte with cycling key bytes */
    (void)plaintext; (void)key; (void)output; (void)out_size;
}

void xor_decrypt(const char *ciphertext, const char *key, char *output, size_t out_size) {
    /* TODO: XOR is symmetric - same as encrypt */
    (void)ciphertext; (void)key; (void)output; (void)out_size;
}

void print_hex(const char *data, size_t len) {
    for (size_t i = 0; i < len; i++) {
        printf("%02x ", (unsigned char)data[i]);
    }
    printf("\n");
}

void problem5_xor_cipher(void) {
    printf("TODO: Problem 5 - XOR Cipher\n\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Security — Exercises\n");
    printf("====================================\n\n");

    problem1_buffer_overflow();
    problem2_format_strings();
    problem3_input_validation();
    problem4_secure_memory();
    problem5_xor_cipher();

    return 0;
}

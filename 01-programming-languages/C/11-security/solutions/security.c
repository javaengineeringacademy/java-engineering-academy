/*
 * Security — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

/* ============================================================
 * Problem 1: Buffer Overflow Prevention
 * ============================================================ */
void safe_strcpy(char *dest, const char *src, size_t dest_size) {
    if (dest_size == 0) return;
    size_t i;
    for (i = 0; i < dest_size - 1 && src[i] != '\0'; i++) {
        dest[i] = src[i];
    }
    dest[i] = '\0';
}

void safe_strcat(char *dest, const char *src, size_t dest_size) {
    size_t dest_len = strlen(dest);
    size_t i = 0;
    while (dest_len + i < dest_size - 1 && src[i] != '\0') {
        dest[dest_len + i] = src[i];
        i++;
    }
    dest[dest_len + i] = '\0';
}

int safe_gets(char *buf, size_t size) {
    if (size == 0) return -1;
    if (fgets(buf, size, stdin) == NULL) return -1;

    size_t len = strlen(buf);
    if (len > 0 && buf[len - 1] == '\n') {
        buf[len - 1] = '\0';
        len--;
    }
    return (int)len;
}

void problem1_buffer_overflow(void) {
    printf("=== Problem 1: Buffer Overflow Prevention ===\n");

    char dest[20];

    safe_strcpy(dest, "Hello, World!", sizeof(dest));
    printf("  safe_strcpy: \"%s\"\n", dest);

    safe_strcpy(dest, "This is a very long string that exceeds buffer", sizeof(dest));
    printf("  Truncated:  \"%s\"\n", dest);

    safe_strcpy(dest, "Hi", sizeof(dest));
    safe_strcat(dest, " there", sizeof(dest));
    printf("  safe_strcat: \"%s\"\n", dest);

    /* Demonstrate overflow danger */
    char dangerous[5];
    /* This would overflow: strcpy(dangerous, "This string is way too long!"); */
    safe_strcpy(dangerous, "This string is way too long!", sizeof(dangerous));
    printf("  Safe copy of long string: \"%s\" (truncated to %zu chars)\n",
           dangerous, sizeof(dangerous) - 1);
    printf("\n");
}

/* ============================================================
 * Problem 2: Format String Vulnerability
 * ============================================================ */
void safe_print(const char *user_input) {
    /* Fix: Always use "%s" format, never pass user input as format */
    printf("%s", user_input);
}

void problem2_format_strings(void) {
    printf("=== Problem 2: Format String Safety ===\n");

    printf("  Vulnerable:  printf(user_input) - DANGEROUS!\n");
    printf("    Input \"%%s%%s%%s\" could leak stack data\n");
    printf("    Input \"%%n\" could write to arbitrary memory\n\n");

    const char *user_input = "Hello, %s! Score: %d";
    printf("  DANGEROUS with user format string:\n");
    /* printf(user_input); - DO NOT RUN THIS */

    printf("  SAFE with fixed format:\n");
    safe_print(user_input);
    printf("\n");

    printf("  Always use: printf(\"%%s\", user_input)\n\n");
}

/* ============================================================
 * Problem 3: Input Validation
 * ============================================================ */
int validate_email(const char *email) {
    if (!email || !*email) return 0;

    int at_count = 0;
    int at_pos = -1;
    int has_dot_after_at = 0;
    int has_alpha_after_dot = 0;

    for (int i = 0; email[i]; i++) {
        if (email[i] == ' ') return 0;
        if (email[i] == '@') {
            at_count++;
            at_pos = i;
        }
    }

    if (at_count != 1 || at_pos == 0) return 0;

    /* Check for dot after @ */
    for (int i = at_pos + 1; email[i]; i++) {
        if (email[i] == '.') has_dot_after_at = 1;
    }
    if (!has_dot_after_at) return 0;

    /* Check for alpha after last dot */
    int len = strlen(email);
    for (int i = len - 1; i > at_pos; i--) {
        if (email[i] == '.') break;
        if (isalpha(email[i])) { has_alpha_after_dot = 1; break; }
    }

    return has_alpha_after_dot;
}

int validate_int_range(const char *input, int min, int max, int *result) {
    if (!input || !*input) return 0;

    char *end;
    long val = strtol(input, &end, 10);
    if (*end != '\0') return 0;
    if (val < min || val > max) return 0;

    *result = (int)val;
    return 1;
}

int validate_alphanumeric(const char *str) {
    if (!str) return 0;
    for (int i = 0; str[i]; i++) {
        if (!isalnum((unsigned char)str[i])) return 0;
    }
    return 1;
}

void sanitize_sql(const char *input, char *output, size_t out_size) {
    size_t j = 0;
    for (size_t i = 0; input[i] && j < out_size - 1; i++) {
        if (input[i] == '\'') {
            if (j + 2 < out_size) {
                output[j++] = '\'';
                output[j++] = '\'';
            }
        } else if (input[i] == ';') {
            if (j + 1 < out_size) output[j++] = ' ';
        } else {
            output[j++] = input[i];
        }
    }
    output[j] = '\0';
}

void problem3_input_validation(void) {
    printf("=== Problem 3: Input Validation ===\n");

    printf("  Email validation:\n");
    printf("    user@domain.com: %s\n", validate_email("user@domain.com") ? "valid" : "invalid");
    printf("    bad@@email.com:  %s\n", validate_email("bad@@email.com") ? "valid" : "invalid");
    printf("    no-at-sign:      %s\n", validate_email("noatsign") ? "valid" : "invalid");
    printf("    a@b:             %s\n", validate_email("a@b") ? "valid" : "invalid");

    printf("  Integer range validation (1-100):\n");
    int val;
    printf("    \"42\":  %s (val=%d)\n",
           validate_int_range("42", 1, 100, &val) ? "valid" : "invalid", val);
    printf("    \"abc\": %s\n",
           validate_int_range("abc", 1, 100, &val) ? "valid" : "invalid");
    printf("    \"200\": %s\n",
           validate_int_range("200", 1, 100, &val) ? "valid" : "invalid");

    printf("  Alphanumeric check:\n");
    printf("    \"hello123\": %s\n", validate_alphanumeric("hello123") ? "yes" : "no");
    printf("    \"hello 12\": %s\n", validate_alphanumeric("hello 12") ? "yes" : "no");

    char sanitized[100];
    sanitize_sql("O'Brien; DROP TABLE--", sanitized, sizeof(sanitized));
    printf("  SQL sanitized: \"%s\"\n", sanitized);
    printf("\n");
}

/* ============================================================
 * Problem 4: Secure Memory Handling
 * ============================================================ */
void secure_zero(void *ptr, size_t size) {
    /* Use volatile function pointer to prevent optimization */
    volatile unsigned char *vp = (volatile unsigned char *)ptr;
    while (size--) *vp++ = 0;
}

int secure_compare(const void *a, const void *b, size_t size) {
    const volatile unsigned char *pa = a;
    const volatile unsigned char *pb = b;
    volatile unsigned char result = 0;
    for (size_t i = 0; i < size; i++) {
        result |= pa[i] ^ pb[i];
    }
    return result == 0;
}

void secure_free(void **ptr, size_t size) {
    if (ptr && *ptr) {
        secure_zero(*ptr, size);
        free(*ptr);
        *ptr = NULL;
    }
}

void problem4_secure_memory(void) {
    printf("=== Problem 4: Secure Memory ===\n");

    char secret[] = "SuperSecretPassword123";
    printf("  Before secure_zero: \"%s\"\n", secret);
    secure_zero(secret, strlen(secret));
    printf("  After secure_zero:  \"");
    for (size_t i = 0; i < sizeof(secret); i++) {
        printf("%c", secret[i] ? secret[i] : '.');
    }
    printf("\"\n");

    char a[] = "secret123";
    char b[] = "secret123";
    char c[] = "secret124";
    printf("  Constant-time compare a==b: %s\n",
           secure_compare(a, b, 9) ? "match" : "no match");
    printf("  Constant-time compare a==c: %s\n",
           secure_compare(a, c, 9) ? "match" : "no match");

    char *sensitive = malloc(20);
    strcpy(sensitive, "password");
    printf("  Before secure_free: %p\n", (void *)sensitive);
    secure_free((void **)&sensitive, 20);
    printf("  After secure_free:  %p (should be NULL)\n", (void *)sensitive);
    printf("\n");
}

/* ============================================================
 * Problem 5: XOR Cipher
 * ============================================================ */
void xor_encrypt(const char *plaintext, const char *key, char *output, size_t out_size) {
    size_t key_len = strlen(key);
    size_t i;
    for (i = 0; plaintext[i] && i < out_size - 1; i++) {
        output[i] = plaintext[i] ^ key[i % key_len];
    }
    output[i] = '\0';
}

void xor_decrypt(const char *ciphertext, const char *key, char *output, size_t out_size) {
    /* XOR is symmetric: decrypt == encrypt */
    xor_encrypt(ciphertext, key, output, out_size);
}

void print_hex(const char *data, size_t len) {
    for (size_t i = 0; i < len; i++) {
        printf("%02x ", (unsigned char)data[i]);
    }
    printf("\n");
}

void problem5_xor_cipher(void) {
    printf("=== Problem 5: XOR Cipher ===\n");

    const char *plaintext = "Hello, Security!";
    const char *key = "secret";

    char encrypted[256];
    char decrypted[256];

    xor_encrypt(plaintext, key, encrypted, sizeof(encrypted));
    printf("  Plaintext:  \"%s\"\n", plaintext);
    printf("  Encrypted:  ");
    print_hex(encrypted, strlen(plaintext));

    xor_decrypt(encrypted, key, decrypted, sizeof(decrypted));
    printf("  Decrypted:  \"%s\"\n", decrypted);

    printf("  Match: %s\n", strcmp(plaintext, decrypted) == 0 ? "yes" : "no");
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Security — Solutions\n");
    printf("====================================\n\n");

    problem1_buffer_overflow();
    problem2_format_strings();
    problem3_input_validation();
    problem4_secure_memory();
    problem5_xor_cipher();

    return 0;
}

/*
 * Exercise: File I/O in C
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Understand file opening, reading, and closing
 *   - Practice different file modes (r, w, a, r+, w+, a+)
 *   - Learn about formatted and binary file I/O
 *   - Master error handling for file operations
 */

#include <stdio.h>
#include <stdlib.h>

/* ============================================================
 * Problem 1: Basic File Reading
 *
 * Write a function that opens a file and counts the number
 * of lines, words, and characters in it.
 * Handle the case where the file doesn't exist.
 * ============================================================ */
int count_lines_words_chars(const char *filename, int *lines, int *words, int *chars) {
    /* TODO: Implement line/word/char counting
     * 1. Open the file in read mode
     * 2. Read character by character using fgetc()
     * 3. Count newlines (lines), spaces/newlines/tabs (words), all non-EOF (chars)
     * 4. Close the file and return 0 on success, -1 on failure
     */
    (void)filename; (void)lines; (void)words; (void)chars;
    return -1;
}

/* ============================================================
 * Problem 2: File Writing and Appending
 *
 * Write a function that creates a file and writes a list of
 * names to it, one per line. Then write another function that
 * appends additional names to the same file.
 * ============================================================ */
int write_names(const char *filename, const char **names, int count) {
    /* TODO: Implement file writing
     * 1. Open the file in write mode ("w")
     * 2. Write each name followed by newline using fprintf()
     * 3. Close the file
     * 4. Return 0 on success, -1 on failure
     */
    (void)filename; (void)names; (void)count;
    return -1;
}

int append_names(const char *filename, const char **names, int count) {
    /* TODO: Implement file appending
     * 1. Open the file in append mode ("a")
     * 2. Write each name followed by newline
     * 3. Close the file
     * 4. Return 0 on success, -1 on failure
     */
    (void)filename; (void)names; (void)count;
    return -1;
}

/* ============================================================
 * Problem 3: Binary File I/O
 *
 * Write functions to write and read an array of integers
 * to/from a binary file.
 * ============================================================ */
int write_binary(const char *filename, const int *data, int count) {
    /* TODO: Implement binary write
     * 1. Open file in "wb" mode
     * 2. Use fwrite() to write count integers
     * 3. Close file
     * 4. Return 0 on success, -1 on failure
     */
    (void)filename; (void)data; (void)count;
    return -1;
}

int read_binary(const char *filename, int *data, int max_count) {
    /* TODO: Implement binary read
     * 1. Open file in "rb" mode
     * 2. Use fread() to read up to max_count integers
     * 3. Return the number of integers actually read
     */
    (void)filename; (void)data; (void)max_count;
    return 0;
}

/* ============================================================
 * Problem 4: File Copy Utility
 *
 * Write a function that copies a file's contents to another file.
 * Should handle both text and binary files.
 * ============================================================ */
int copy_file(const char *src, const char *dest) {
    /* TODO: Implement file copy
     * 1. Open source in "rb" mode
     * 2. Open destination in "wb" mode
     * 3. Read chunks (e.g., 1024 bytes) and write to dest
     * 4. Close both files
     * 5. Return 0 on success, -1 on failure
     */
    (void)src; (void)dest;
    return -1;
}

/* ============================================================
 * Problem 5: CSV Parsing
 *
 * Write a function that reads a CSV file and stores the data
 * in a 2D array of strings.
 * ============================================================ */
int parse_csv(const char *filename, char ***data, int *rows, int *cols) {
    /* TODO: Implement CSV parsing
     * 1. Open the file in read mode
     * 2. Count lines and columns from first line
     * 3. Allocate memory for the 2D string array
     * 4. Parse each line using strtok() with ',' delimiter
     * 5. Store results and return 0 on success
     */
    (void)filename; (void)data; (void)rows; (void)cols;
    return -1;
}

int main(void) {
    /* Test cases */
    printf("=== File I/O Exercises ===\n\n");

    printf("TODO: Implement the file I/O functions above.\n");
    printf("Test with:\n");
    printf("  1. Create a test file with sample text\n");
    printf("  2. Count lines/words/chars\n");
    printf("  3. Write and append names\n");
    printf("  4. Binary read/write integers\n");
    printf("  5. Copy a file\n");
    printf("  6. Parse a CSV file\n\n");

    return 0;
}

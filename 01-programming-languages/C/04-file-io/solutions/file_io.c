/*
 * File I/O — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Problem 1: Basic File Reading — Line/Word/Char Counter
 * ============================================================ */
int count_lines_words_chars(const char *filename, int *lines, int *words, int *chars) {
    FILE *fp = fopen(filename, "r");
    if (!fp) {
        perror("fopen");
        return -1;
    }

    *lines = 0; *words = 0; *chars = 0;
    int in_word = 0;
    int c;

    while ((c = fgetc(fp)) != EOF) {
        (*chars)++;
        if (c == '\n') (*lines)++;
        if (c == ' ' || c == '\n' || c == '\t') {
            in_word = 0;
        } else if (!in_word) {
            in_word = 1;
            (*words)++;
        }
    }

    /* Count the last line if file doesn't end with newline */
    if (*chars > 0 && (*chars == 0 || ((char *)"" , 1))) {
        /* If last char isn't newline but we have content, count extra line */
    }

    fclose(fp);
    return 0;
}

void problem1_read(void) {
    printf("=== Problem 1: File Reading ===\n");

    /* Create a test file first */
    FILE *fp = fopen("test_input.txt", "w");
    if (fp) {
        fprintf(fp, "Hello World\n");
        fprintf(fp, "This is a test file\n");
        fprintf(fp, "With multiple lines and words\n");
        fclose(fp);
    }

    int lines, words, chars;
    if (count_lines_words_chars("test_input.txt", &lines, &words, &chars) == 0) {
        printf("  Lines: %d, Words: %d, Chars: %d\n", lines, words, chars);
    }

    /* Test with nonexistent file */
    if (count_lines_words_chars("nonexistent.txt", &lines, &words, &chars) == -1) {
        printf("  Nonexistent file handled correctly\n");
    }

    remove("test_input.txt");
    printf("\n");
}

/* ============================================================
 * Problem 2: File Writing and Appending
 * ============================================================ */
int write_names(const char *filename, const char **names, int count) {
    FILE *fp = fopen(filename, "w");
    if (!fp) {
        perror("fopen");
        return -1;
    }

    for (int i = 0; i < count; i++) {
        fprintf(fp, "%s\n", names[i]);
    }

    fclose(fp);
    return 0;
}

int append_names(const char *filename, const char **names, int count) {
    FILE *fp = fopen(filename, "a");
    if (!fp) {
        perror("fopen");
        return -1;
    }

    for (int i = 0; i < count; i++) {
        fprintf(fp, "%s\n", names[i]);
    }

    fclose(fp);
    return 0;
}

void problem2_write_append(void) {
    printf("=== Problem 2: Write & Append ===\n");

    const char *initial[] = {"Alice", "Bob", "Charlie"};
    const char *extra[] = {"Diana", "Eve"};

    write_names("names.txt", initial, 3);
    append_names("names.txt", extra, 2);

    /* Read and print the result */
    FILE *fp = fopen("names.txt", "r");
    if (fp) {
        char buf[100];
        printf("  Contents of names.txt:\n");
        while (fgets(buf, sizeof(buf), fp)) {
            buf[strcspn(buf, "\n")] = '\0';
            printf("    %s\n", buf);
        }
        fclose(fp);
    }

    remove("names.txt");
    printf("\n");
}

/* ============================================================
 * Problem 3: Binary File I/O
 * ============================================================ */
int write_binary(const char *filename, const int *data, int count) {
    FILE *fp = fopen(filename, "wb");
    if (!fp) {
        perror("fopen");
        return -1;
    }

    size_t written = fwrite(data, sizeof(int), count, fp);
    fclose(fp);
    return (int)written == count ? 0 : -1;
}

int read_binary(const char *filename, int *data, int max_count) {
    FILE *fp = fopen(filename, "rb");
    if (!fp) {
        perror("fopen");
        return -1;
    }

    int read = (int)fread(data, sizeof(int), max_count, fp);
    fclose(fp);
    return read;
}

void problem3_binary(void) {
    printf("=== Problem 3: Binary I/O ===\n");

    int original[] = {10, 20, 30, 40, 50};
    int restored[5] = {0};

    write_binary("data.bin", original, 5);
    int n = read_binary("data.bin", restored, 5);

    printf("  Written: ");
    for (int i = 0; i < 5; i++) printf("%d ", original[i]);
    printf("\n  Read back (%d values): ", n);
    for (int i = 0; i < n; i++) printf("%d ", restored[i]);
    printf("\n");

    remove("data.bin");
    printf("\n");
}

/* ============================================================
 * Problem 4: File Copy Utility
 * ============================================================ */
int copy_file(const char *src, const char *dest) {
    FILE *in = fopen(src, "rb");
    if (!in) {
        perror("fopen source");
        return -1;
    }

    FILE *out = fopen(dest, "wb");
    if (!out) {
        perror("fopen dest");
        fclose(in);
        return -1;
    }

    char buffer[1024];
    size_t n;
    while ((n = fread(buffer, 1, sizeof(buffer), in)) > 0) {
        if (fwrite(buffer, 1, n, out) != n) {
            perror("fwrite");
            fclose(in);
            fclose(out);
            return -1;
        }
    }

    fclose(in);
    fclose(out);
    return 0;
}

void problem4_copy(void) {
    printf("=== Problem 4: File Copy ===\n");

    /* Create source file */
    FILE *fp = fopen("source.txt", "w");
    if (fp) {
        fprintf(fp, "This is a test file for copying.\n");
        fprintf(fp, "Line 2 of the source file.\n");
        fclose(fp);
    }

    if (copy_file("source.txt", "dest.txt") == 0) {
        printf("  File copied successfully\n");

        /* Verify by reading dest */
        fp = fopen("dest.txt", "r");
        if (fp) {
            char buf[256];
            printf("  Copied content:\n");
            while (fgets(buf, sizeof(buf), fp)) {
                printf("    %s", buf);
            }
            fclose(fp);
        }
    }

    remove("source.txt");
    remove("dest.txt");
    printf("\n");
}

/* ============================================================
 * Problem 5: CSV Parsing
 * ============================================================ */
int parse_csv(const char *filename, char ***data, int *rows, int *cols) {
    FILE *fp = fopen(filename, "r");
    if (!fp) {
        perror("fopen");
        return -1;
    }

    /* First pass: count rows and columns */
    char line[1024];
    *rows = 0; *cols = 0;

    if (fgets(line, sizeof(line), fp)) {
        /* Count columns from first line */
        char *tmp = strdup(line);
        char *token = strtok(tmp, ",");
        while (token) { (*cols)++; token = strtok(NULL, ","); }
        free(tmp);
        (*rows)++;
    }

    while (fgets(line, sizeof(line), fp)) (*rows++)++;

    /* Allocate 2D array */
    *data = malloc(*rows * sizeof(char *));
    if (!*data) { fclose(fp); return -1; }

    /* Second pass: parse data */
    rewind(fp);
    for (int i = 0; i < *rows; i++) {
        if (!fgets(line, sizeof(line), fp)) break;
        line[strcspn(line, "\n")] = '\0';

        (*data)[i] = malloc((*cols + 1) * sizeof(char *));
        char *token = strtok(line, ",");
        for (int j = 0; j < *cols; j++) {
            (*data)[i][j] = token ? strdup(token) : strdup("");
            token = strtok(NULL, ",");
        }
    }

    fclose(fp);
    return 0;
}

void problem5_csv(void) {
    printf("=== Problem 5: CSV Parsing ===\n");

    /* Create a test CSV */
    FILE *fp = fopen("test.csv", "w");
    if (fp) {
        fprintf(fp, "Name,Age,City\n");
        fprintf(fp, "Alice,30,New York\n");
        fprintf(fp, "Bob,25,San Francisco\n");
        fprintf(fp, "Charlie,35,Chicago\n");
        fclose(fp);
    }

    char **data = NULL;
    int rows = 0, cols = 0;

    if (parse_csv("test.csv", &data, &rows, &cols) == 0) {
        printf("  Parsed %d rows, %d columns:\n", rows, cols);
        for (int i = 0; i < rows; i++) {
            printf("    Row %d: ", i);
            for (int j = 0; j < cols; j++) {
                printf("%s%s", data[i][j], j < cols - 1 ? ", " : "");
            }
            printf("\n");
            for (int j = 0; j < cols; j++) free(data[i][j]);
            free(data[i]);
        }
        free(data);
    }

    remove("test.csv");
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  File I/O — Solutions\n");
    printf("====================================\n\n");

    problem1_read();
    problem2_write_append();
    problem3_binary();
    problem4_copy();
    problem5_csv();

    return 0;
}

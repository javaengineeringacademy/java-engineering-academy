# File I/O Exercises

## Exercise 1: File Copy
Write a program that copies a file content to another file.

```c
#include <stdio.h>

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <source> <dest>\n", argv[0]);
        return 1;
    }

    FILE *src = fopen(argv[1], "rb");
    FILE *dst = fopen(argv[2], "wb");

    if (!src || !dst) {
        perror("File error");
        return 1;
    }

    char buffer[4096];
    size_t bytes;
    while ((bytes = fread(buffer, 1, sizeof(buffer), src)) > 0) {
        fwrite(buffer, 1, bytes, dst);
    }

    fclose(src);
    fclose(dst);
    return 0;
}
```

## Exercise 2: Line Counter
Count the number of lines in a file.

## Exercise 3: Word Frequency
Count word occurrences in a text file.

## Exercise 4: CSV Parser
Parse a CSV file into an array of structures.

## Exercise 5: Binary Search
Create and search a binary file of sorted integers.

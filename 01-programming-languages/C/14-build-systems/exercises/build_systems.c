/*
 * Exercise: Build Systems in C
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Understand Makefile syntax and targets
 *   - Practice dependency management
 *   - Learn about compiler flags and optimization
 *   - Master incremental builds and clean targets
 */

#include <stdio.h>
#include <stdlib.h>

/* ============================================================
 * Problem 1: Basic Makefile Concepts
 *
 * Write a simple Makefile for a project with:
 * - main.o, utils.o, and math.o source files
 * - Proper compilation with gcc and -Wall -Wextra -std=c99
 * - An "all" target that builds the executable
 * - A "clean" target that removes build artifacts
 *
 * Write the Makefile as a comment block below.
 * ============================================================ */
/* TODO: Write a Makefile that compiles this program
 *
 * CC = gcc
 * CFLAGS = -Wall -Wextra -std=c99
 * TARGET = program
 * OBJS = main.o utils.o math.o
 *
 * all: $(TARGET)
 *
 * $(TARGET): $(OBJS)
 *     $(CC) $(CFLAGS) -o $@ $^
 *
 * %.o: %.c
 *     $(CC) $(CFLAGS) -c -o $@ $<
 *
 * clean:
 *     rm -f $(OBJS) $(TARGET)
 */

/* ============================================================
 * Problem 2: Header Dependencies
 *
 * Given these source files, determine the correct dependency
 * rules for automatic recompilation when headers change:
 * - main.c includes utils.h and math.h
 * - utils.c includes utils.h
 * - math.c includes math.h
 *
 * Write the dependency rules for the Makefile.
 * ============================================================ */
/* TODO: Write dependency rules
 * main.o: main.c utils.h math.h
 * utils.o: utils.c utils.h
 * math.o: math.c math.h
 */

/* ============================================================
 * Problem 3: Compiler Flags
 *
 * Document the purpose of each compiler flag:
 * -Wall -Wextra -Werror -std=c99 -O2 -g -DDEBUG -Iinclude -Llib -lmylib
 * Write a Makefile that uses appropriate flags for different
 * build configurations (debug, release, profile).
 * ============================================================ */
/* TODO: Write Makefile with build configurations
 * debug: CFLAGS += -g -DDEBUG -O0
 * release: CFLAGS += -O2 -DNDEBUG
 * profile: CFLAGS += -g -pg -O2
 */

/* ============================================================
 * Problem 4: Static and Shared Libraries
 *
 * Write Makefile rules to:
 * - Build a static library (libutils.a) from utils.o and math.o
 * - Build a shared library (libutils.so) with -fPIC
 * - Link the main program against the library
 * ============================================================ */
/* TODO: Write library build rules
 * libutils.a: utils.o math.o
 *     ar rcs $@ $^
 *
 * libutils.so: utils.o math.o
 *     $(CC) -shared -o $@ $^
 */

/* ============================================================
 * Problem 5: Conditional Compilation
 *
 * Write Makefile rules that:
 * - Detect the operating system (Linux vs macOS)
 * - Use appropriate flags (e.g., -lpthread on Linux)
 * - Support cross-compilation with a CROSS_COMPILE variable
 * ============================================================ */
/* TODO: Write platform-aware Makefile
 * UNAME_S := $(shell uname -s)
 * ifeq ($(UNAME_S),Linux)
 *     LDFLAGS += -lpthread
 * endif
 * ifeq ($(UNAME_S),Darwin)
 *     LDFLAGS += -framework CoreFoundation
 * endif
 */

/* Dummy functions so the file compiles */
void dummy(void) {
    printf("This file demonstrates Makefile concepts.\n");
    printf("The actual exercises are the Makefile comments above.\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Build Systems — Exercises\n");
    printf("====================================\n\n");

    dummy();

    printf("\nImplement the Makefile rules described in each problem.\n");
    printf("Test with: make, make debug, make release, make clean\n\n");

    return 0;
}

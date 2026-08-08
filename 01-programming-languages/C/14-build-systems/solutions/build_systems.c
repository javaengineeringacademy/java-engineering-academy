/*
 * Build Systems — C Language
 * Solutions: Complete implementations including Makefiles
 *
 * This solution file includes both the C code and complete Makefiles.
 * The Makefiles are embedded as string literals for reference.
 */

#include <stdio.h>
#include <stdlib.h>

/* ============================================================
 * Problem 1: Basic Makefile
 * ============================================================ */
void print_basic_makefile(void) {
    printf("=== Problem 1: Basic Makefile ===\n\n");
    printf("Contents of Makefile:\n");
    printf(
        "# Compiler and flags\n"
        "CC = gcc\n"
        "CFLAGS = -Wall -Wextra -std=c99\n"
        "\n"
        "# Target name\n"
        "TARGET = program\n"
        "\n"
        "# Object files\n"
        "OBJS = main.o utils.o math.o\n"
        "\n"
        "# Default target\n"
        "all: $(TARGET)\n"
        "\n"
        "# Link object files\n"
        "$(TARGET): $(OBJS)\n"
        "\t$(CC) $(CFLAGS) -o $@ $^\n"
        "\n"
        "# Compile .c to .o (generic rule)\n"
        "%%.o: %%.c\n"
        "\t$(CC) $(CFLAGS) -c -o $@ $<\n"
        "\n"
        "# Clean build artifacts\n"
        "clean:\n"
        "\trm -f $(OBJS) $(TARGET)\n"
        "\n"
        ".PHONY: all clean\n"
    );
    printf("\n");
}

/* ============================================================
 * Problem 2: Header Dependencies
 * ============================================================ */
void print_dependency_rules(void) {
    printf("=== Problem 2: Header Dependencies ===\n\n");
    printf(
        "# Dependencies ensure recompilation when headers change\n"
        "main.o: main.c utils.h math.h\n"
        "\t$(CC) $(CFLAGS) -c -o $@ $<\n"
        "\n"
        "utils.o: utils.c utils.h\n"
        "\t$(CC) $(CFLAGS) -c -o $@ $<\n"
        "\n"
        "math.o: math.c math.h\n"
        "\t$(CC) $(CFLAGS) -c -o $@ $<\n"
        "\n"
        "# With auto-dependency generation (GCC):\n"
        "CFLAGS += -MMD -MP\n"
        "-include $(OBJS:.o=.d)\n"
    );
    printf("\n");
}

/* ============================================================
 * Problem 3: Compiler Flags and Build Configs
 * ============================================================ */
void print_build_configs(void) {
    printf("=== Problem 3: Build Configurations ===\n\n");
    printf(
        "CC = gcc\n"
        "CFLAGS_BASE = -Wall -Wextra -std=c99\n"
        "TARGET = program\n"
        "SRC = main.c utils.c math.c\n"
        "OBJ = $(SRC:.c=.o)\n"
        "\n"
        "# Debug build (default)\n"
        "debug: CFLAGS = $(CFLAGS_BASE) -g -DDEBUG -O0\n"
        "debug: $(TARGET)\n"
        "\n"
        "# Release build\n"
        "release: CFLAGS = $(CFLAGS_BASE) -O2 -DNDEBUG\n"
        "release: $(TARGET)\n"
        "\n"
        "# Profile build\n"
        "profile: CFLAGS = $(CFLAGS_BASE) -g -pg -O2\n"
        "profile: $(TARGET)\n"
        "\n"
        "$(TARGET): $(OBJ)\n"
        "\t$(CC) $(CFLAGS) -o $@ $^\n"
        "\n"
        "%%.o: %%.c\n"
        "\t$(CC) $(CFLAGS) -c -o $@ $<\n"
        "\n"
        "clean:\n"
        "\trm -f $(OBJ) $(TARGET) gmon.out\n"
        "\n"
        ".PHONY: debug release profile clean\n"
        "\n"
        "# Flag reference:\n"
        "# -Wall -Wextra: Enable most warnings\n"
        "# -Werror: Treat warnings as errors\n"
        "# -std=c99: Use C99 standard\n"
        "# -O2: Optimization level 2\n"
        "# -g: Include debug symbols\n"
        "# -DDEBUG: Define DEBUG macro\n"
        "# -I<path>: Add include search path\n"
        "# -L<path>: Add library search path\n"
        "# -l<name>: Link against library\n"
    );
    printf("\n");
}

/* ============================================================
 * Problem 4: Static and Shared Libraries
 * ============================================================ */
void print_library_rules(void) {
    printf("=== Problem 4: Libraries ===\n\n");
    printf(
        "CC = gcc\n"
        "CFLAGS = -Wall -Wextra -std=c99 -fPIC\n"
        "\n"
        "# Static library\n"
        "libutils.a: utils.o math.o\n"
        "\tar rcs $@ $^\n"
        "\n"
        "# Shared library\n"
        "libutils.so: utils.o math.o\n"
        "\t$(CC) -shared -o $@ $^\n"
        "\n"
        "# Main program linked against static library\n"
        "program: main.o libutils.a\n"
        "\t$(CC) $(CFLAGS) -o $@ main.o -L. -lutils\n"
        "\n"
        "# Main program linked against shared library\n"
        "program-shared: main.o libutils.so\n"
        "\t$(CC) $(CFLAGS) -o $@ main.o -L. -lutils -Wl,-rpath,.\n"
        "\n"
        "%%.o: %%.c\n"
        "\t$(CC) $(CFLAGS) -c -o $@ $<\n"
        "\n"
        "clean:\n"
        "\trm -f *.o *.a *.so program program-shared\n"
        "\n"
        ".PHONY: clean\n"
    );
    printf("\n");
}

/* ============================================================
 * Problem 5: Platform-Aware Makefile
 * ============================================================ */
void print_platform_makefile(void) {
    printf("=== Problem 5: Platform Detection ===\n\n");
    printf(
        "CC = gcc\n"
        "CFLAGS = -Wall -Wextra -std=c99\n"
        "TARGET = program\n"
        "\n"
        "# Cross-compilation support\n"
        "ifdef CROSS_COMPILE\n"
        "    CC = $(CROSS_COMPILE)gcc\n"
        "endif\n"
        "\n"
        "# Detect OS\n"
        "UNAME_S := $(shell uname -s)\n"
        "\n"
        "ifeq ($(UNAME_S),Linux)\n"
        "    LDFLAGS += -lpthread\n"
        "    EXT =\n"
        "endif\n"
        "ifeq ($(UNAME_S),Darwin)\n"
        "    LDFLAGS += -framework CoreFoundation\n"
        "    EXT =\n"
        "endif\n"
        "ifneq (,$(findstring MINGW,$(UNAME_S)))\n"
        "    EXT = .exe\n"
        "    LDFLAGS += -lws2_32\n"
        "endif\n"
        "\n"
        "TARGET := $(TARGET)$(EXT)\n"
        "\n"
        "SRCS = main.c utils.c\n"
        "OBJS = $(SRCS:.c=.o)\n"
        "\n"
        "$(TARGET): $(OBJS)\n"
        "\t$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)\n"
        "\n"
        "%%.o: %%.c\n"
        "\t$(CC) $(CFLAGS) -c -o $@ $<\n"
        "\n"
        "clean:\n"
        "\trm -f $(OBJS) $(TARGET)\n"
        "\n"
        ".PHONY: clean\n"
    );
    printf("\n");
}

/* ============================================================
 * Complete Production Makefile Example
 * ============================================================ */
void print_complete_makefile(void) {
    printf("=== Complete Production Makefile ===\n\n");
    printf(
        "# ============================================================\n"
        "# Project: MyProject\n"
        "# ============================================================\n"
        "\n"
        "# Toolchain\n"
        "CC      = gcc\n"
        "AR      = ar\n"
        "CFLAGS  = -Wall -Wextra -Werror -std=c99\n"
        "LDFLAGS =\n"
        "\n"
        "# Directories\n"
        "SRC_DIR   = src\n"
        "INC_DIR   = include\n"
        "BUILD_DIR = build\n"
        "BIN_DIR   = bin\n"
        "\n"
        "# Source files\n"
        "SRCS := $(wildcard $(SRC_DIR)/*.c)\n"
        "OBJS := $(patsubst $(SRC_DIR)/%.c, $(BUILD_DIR)/%.o, $(SRCS))\n"
        "DEPS := $(OBJS:.o=.d)\n"
        "\n"
        "# Target\n"
        "TARGET = $(BIN_DIR)/myapp\n"
        "\n"
        "# Build configuration\n"
        "ifndef BUILD_TYPE\n"
        "    BUILD_TYPE = debug\n"
        "endif\n"
        "\n"
        "ifeq ($(BUILD_TYPE),release)\n"
        "    CFLAGS += -O2 -DNDEBUG\n"
        "else ifeq ($(BUILD_TYPE),profile)\n"
        "    CFLAGS += -g -pg -O2\n"
        "else\n"
        "    CFLAGS += -g -DDEBUG -O0\n"
        "endif\n"
        "\n"
        "# Default target\n"
        "all: dirs $(TARGET)\n"
        "\n"
        "dirs:\n"
        "\t@mkdir -p $(BUILD_DIR) $(BIN_DIR)\n"
        "\n"
        "$(TARGET): $(OBJS)\n"
        "\t$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)\n"
        "\t@echo \"Built: $@ ($(BUILD_TYPE))\"\n"
        "\n"
        "$(BUILD_DIR)/%%.o: $(SRC_DIR)/%%.c | dirs\n"
        "\t$(CC) $(CFLAGS) -MMD -MP -I$(INC_DIR) -c -o $@ $<\n"
        "\n"
        "-include $(DEPS)\n"
        "\n"
        "clean:\n"
        "\trm -rf $(BUILD_DIR) $(BIN_DIR)\n"
        "\n"
        "rebuild: clean all\n"
        "\n"
        "test: all\n"
        "\t./$(TARGET)\n"
        "\n"
        "install: $(TARGET)\n"
        "\tinstall -m 755 $(TARGET) /usr/local/bin/\n"
        "\n"
        ".PHONY: all dirs clean rebuild test install\n"
    );
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Build Systems — Solutions\n");
    printf("====================================\n\n");

    print_basic_makefile();
    print_dependency_rules();
    print_build_configs();
    print_library_rules();
    print_platform_makefile();
    print_complete_makefile();

    return 0;
}

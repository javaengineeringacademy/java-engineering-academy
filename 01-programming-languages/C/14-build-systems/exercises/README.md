# Build Systems Exercises

## Exercise 1: Simple Makefile
Create a Makefile for a multi-file project.

```makefile
CC = gcc
CFLAGS = -Wall -Wextra
SOURCES = main.c helper.c
OBJECTS = $(SOURCES:.c=.o)
TARGET = app

all: $(TARGET)

$(TARGET): $(OBJECTS)
	$(CC) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c $<

clean:
	rm -f $(OBJECTS) $(TARGET)

.PHONY: all clean
```

## Exercise 2: CMake Project
Create a CMakeLists.txt for a library and executable.

## Exercise 3: Debug Build
Add debug target with -g flag.

## Exercise 4: Dependency Detection
Use pkg-config to find and link libraries.

## Exercise 5: Cross-compilation
Set up cross-compilation for different architectures.

# Build Systems — C Language

## What it is
Build systems automate the compilation, linking, and deployment of C programs.

## Why it exists
To manage complex builds, dependencies, and ensure reproducibility.

## When to use it
When your project has multiple source files or external dependencies.

## How it works

### Makefile

```makefile
CC = gcc
CFLAGS = -Wall -Wextra -O2
SOURCES = main.c utils.c
OBJECTS = $(SOURCES:.c=.o)
TARGET = program

all: $(TARGET)

$(TARGET): $(OBJECTS)
	$(CC) $(CFLAGS) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c $<

clean:
	rm -f $(OBJECTS) $(TARGET)
```

### CMake

```cmake
cmake_minimum_required(VERSION 3.10)
project(MyProject C)

set(CMAKE_C_STANDARD 11)

add_executable(program main.c utils.c)
target_link_libraries(program m)
```

### pkg-config

```bash
pkg-config --cflags --libs gtk+-3.0
```

### Dependency Management

```makefile
LIBS = -lm -lpthread
LDFLAGS = $(LIBS)
```

## Production Checklist

- [ ] Use build automation
- [ ] Set compiler warnings
- [ ] Handle dependencies
- [ ] Support clean builds
- [ ] Use version control

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses simple gcc commands |
| Intermediate | Writes Makefiles |
| Advanced | Uses CMake and manages dependencies |

## Common Myths

1. **Myth**: Manual compilation is fine for small projects
   **Truth**: Build systems ensure consistency and reproducibility

2. **Myth**: Makefiles are obsolete
   **Truth**: Make is still widely used and effective

## One-Minute Revision

| Tool | Purpose |
|------|---------|
| gcc | Compiler |
| make | Build automation |
| CMake | Cross-platform build system |
| pkg-config | Library configuration |
| gdb | Debugger |
| valgrind | Memory checker |

## Related Topics

- [Preprocessor](../03-preprocessor/README.md)
- [Best Practices](../15-best-practices/README.md)

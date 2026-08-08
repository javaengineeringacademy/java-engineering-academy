# Build Systems — C Language

## The Problem

A C project with 10 source files has 10 compilation units. With dependencies between them, rebuilding everything from scratch takes minutes. Without a build system, you manually type `gcc` commands — error-prone, slow, and unrepeatable. With a build system, you type `make` and it:

- Recompiles only changed files
- Resolves dependencies automatically
- Runs tests
- Installs to the correct location
- Works identically on every developer's machine

## What It Is

Build systems automate the compilation, linking, and deployment of C programs:

| Tool | Purpose | Complexity |
|------|---------|------------|
| `gcc`/`clang` | Direct compilation | Simple projects |
| `make` | Build automation | Medium projects |
| CMake | Cross-platform build generation | Large projects |
| `pkg-config` | Library configuration | Dependency discovery |
| Meson | Modern build system | New projects |

## Why It Exists

Build systems exist because:
- **Incremental builds**: Only recompile changed files
- **Dependency tracking**: Automatically detect what needs rebuilding
- **Cross-platform**: Same build definition works on Linux, macOS, Windows
- **Reproducibility**: Same build on every machine
- **Automation**: Run tests, install, package as part of the build

### Architecture: Build Process

```
Source Files (.c)
    ↓ Dependency analysis (Makefile/CMakeLists.txt)
    ↓ Compilation (gcc -c file.c → file.o)
    ↓ Linking (gcc file1.o file2.o → program)
Executable
    ↓ Testing (make test)
    ↓ Installation (make install)
System directories
```

## Expanded Code Examples

### Makefile — Complete Example

```makefile
# Project configuration
CC = gcc
CFLAGS = -Wall -Wextra -Werror -Wpedantic -g -O2
LDFLAGS = -lm -lpthread

# Source files
SRCS = main.c utils.c network.c database.c
OBJS = $(SRCS:.c=.o)
TARGET = myapp

# Header dependencies (auto-generated)
DEPS = $(OBJS:.o=.d)

# Default target
all: $(TARGET)

# Link
$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)

# Compile with dependency tracking
%.o: %.c
	$(CC) $(CFLAGS) -MMD -MP -c $< -o $@

# Include auto-generated dependencies
-include $(DEPS)

# Phony targets
.PHONY: all clean test install

# Clean
clean:
	rm -f $(OBJS) $(DEPS) $(TARGET)

# Test
test: $(TARGET)
	./test_runner

# Install
install: $(TARGET)
	install -d $(DESTDIR)/usr/local/bin
	install -m 755 $(TARGET) $(DESTDIR)/usr/local/bin/
```

### CMake — Cross-Platform Build

```cmake
cmake_minimum_required(VERSION 3.16)
project(MyProject VERSION 1.0 LANGUAGES C)

# Set C standard
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# Compiler warnings
add_compile_options(-Wall -Wextra -Wpedantic)

# Source files
set(SOURCES
    src/main.c
    src/utils.c
    src/network.c
    src/database.c
)

# Create executable
add_executable(myapp ${SOURCES})

# Include directories
target_include_directories(myapp PRIVATE include)

# Link libraries
target_link_libraries(myapp PRIVATE m pthread)

# Enable testing
enable_testing()
add_executable(test_runner tests/test_runner.c)
target_link_libraries(test_runner PRIVATE myapp)
add_test(NAME unit_tests COMMAND test_runner)

# Install
install(TARGETS myapp DESTINATION bin)
```

### pkg-config Integration

```makefile
# Find GTK+ using pkg-config
GTK_CFLAGS = $(shell pkg-config --cflags gtk+-3.0)
GTK_LIBS = $(shell pkg-config --libs gtk+-3.0)

all: myapp

myapp: main.o
	$(CC) $(GTK_LIBS) -o $@ $^

main.o: main.c
	$(CC) $(GTK_CFLAGS) -c $< -o $@
```

### Static Library Build

```makefile
# Build a static library
CC = gcc
AR = ar
CFLAGS = -Wall -O2

LIB_SRCS = string_utils.c math_utils.c crypto_utils.c
LIB_OBJS = $(LIB_SRCS:.c=.o)
LIB = libmyutils.a

all: $(LIB)

$(LIB): $(LIB_OBJS)
	$(AR) rcs $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -f $(LIB_OBJS) $(LIB)

.PHONY: all clean
```

### Shared Library Build

```makefile
CC = gcc
CFLAGS = -Wall -O2 -fPIC
LDFLAGS = -shared

LIB_SRCS = utils.c
LIB_OBJS = $(LIB_SRCS:.c=.o)
LIB = libutils.so

all: $(LIB)

$(LIB): $(LIB_OBJS)
	$(CC) $(LDFLAGS) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

install: $(LIB)
	install -d $(DESTDIR)/usr/local/lib
	install -m 755 $(LIB) $(DESTDIR)/usr/local/lib/
	ldconfig

clean:
	rm -f $(LIB_OBJS) $(LIB)

.PHONY: all clean install
```

### Dependency Management with External Libraries

```cmake
# CMake with FetchContent (CMake 3.11+)
include(FetchContent)

FetchContent_Declare(
    jsonc
    GIT_REPOSITORY https://github.com/json-c/json-c.git
    GIT_TAG json-c-0.17-20230914
)

FetchContent_MakeAvailable(jsonc)

add_executable(myapp main.c)
target_link_libraries(myapp PRIVATE json-c)
```

## Production Incidents

### Incident 1: Missing Header Dependency

**Problem**: Build fails after changing a header file — old object files are used.

**Cause**: Makefile does not track header dependencies:

```makefile
# Bad: no dependency tracking
%.o: %.c
	$(CC) -c $< -o $@
# Changing utils.h does NOT trigger recompilation of main.o
```

**Solution**: Auto-generate dependencies:

```makefile
# Good: -MMD -MP generates .d dependency files
%.o: %.c
	$(CC) -MMD -MP -c $< -o $@

# Include auto-generated dependencies
-include $(OBJS:.o=.d)
```

### Incident 2: Build Not Reproducible

**Problem**: Build on developer machine works, build on CI server fails.

**Cause**: Implicit compiler flags differ between machines:

```bash
# Developer machine
gcc main.c  # Uses default flags, works

# CI server
gcc -Wall -Werror main.c  # Strict flags, fails
```

**Solution**: Explicit flags in build system:

```makefile
# Explicit flags in Makefile
CFLAGS = -Wall -Wextra -Werror -g -O2
# Same flags on every machine
```

## Production Checklist

- [ ] Use a build system (Make, CMake, Meson)
- [ ] Set compiler warnings (`-Wall -Wextra -Werror`)
- [ ] Track header dependencies (`-MMD -MP`)
- [ ] Separate debug and release builds
- [ ] Support `make clean` for fresh builds
- [ ] Add test target (`make test`)
- [ ] Support cross-compilation
- [ ] Document build requirements
- [ ] Use version control for build files
- [ ] Support `DESTDIR` for staged installs

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Uses simple `gcc` commands | Compiles manually |
| **Intermediate** | Writes Makefiles | Incremental builds, dependency tracking |
| **Advanced** | Uses CMake, manages dependencies | Cross-platform, external libraries |
| **Expert** | Designs build systems, CI/CD pipelines | Package managers, reproducible builds |

## Common Myths Debunked

1. **Myth**: Manual compilation is fine for small projects
   **Truth**: Build systems ensure consistency, track dependencies, and save time even for small projects.

2. **Myth**: Makefiles are obsolete
   **Truth**: Make is still the most widely used build tool. CMake generates Makefiles. Make is simple, powerful, and everywhere.

3. **Myth**: CMake is too complex
   **Truth**: CMake is complex for advanced features but simple for basic projects. The complexity pays off for cross-platform builds.

## One-Minute Revision

| Tool | Purpose | Key Detail |
|------|---------|------------|
| `gcc` | Compiler | Direct compilation |
| `make` | Build automation | Incremental builds, dependency tracking |
| CMake | Cross-platform build | Generates Makefiles, Ninja, etc. |
| `pkg-config` | Library configuration | Finds compiler/linker flags |
| `-Wall -Wextra` | Compiler warnings | Catch bugs early |
| `-MMD -MP` | Dependency tracking | Auto-generate .d files |
| `make clean` | Remove build artifacts | Fresh build |
| `make test` | Run tests | Verify correctness |

## Related Topics

- [Preprocessor](../03-preprocessor/README.md) — How `#include` and conditional compilation work
- [Best Practices](../15-best-practices/README.md) — Coding standards enforced by build flags
- [Testing](../13-testing/README.md) — CI/CD integration

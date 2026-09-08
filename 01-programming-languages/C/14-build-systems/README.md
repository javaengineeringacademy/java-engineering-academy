# Build Systems — C Language

## Why It Matters

When you're building a C project with multiple source files and dependencies, manually typing `gcc` commands is error-prone, slow, and unrepeatable. A build system automates recompilation of only changed files, resolves dependencies, runs tests, installs correctly, and works identically on every developer's machine — turning minutes of manual rebuilding into seconds of incremental compilation.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Any project with >1 source file, or needing CI/CD | Direct `gcc` for single-file prototypes |
| When NOT to use | One-off scripts or throwaway code | Keep it simple |
| Alternatives | Meson, Ninja, Bazel, xmake | Different trade-offs in complexity and features |
| Production Examples | Linux kernel (Make), CMake-based projects, Meson (systemd) | Build systems scale with project size |
| Common Mistakes | Not tracking header deps (`-MMD -MP`), implicit flags, no `make clean` | Use `-MMD -MP`, explicit `CFLAGS`, always support clean |

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

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Build not tracking header dependency changes | `make -d` debug output | Run `make -d` to see which targets are rebuilt; verify `.d` dependency files are generated with `-MMD -MP` |
| Build fails on different machines (implicit flags) | Compare `CFLAGS` in Makefile vs manual build | Add explicit `CFLAGS` in build system; avoid relying on compiler defaults |
| Slow rebuild after single file change | Check dependency graph | Use `make -Bn` to see rebuild plan; ensure header dependencies are correctly tracked |
| Cross-compilation failing silently | Verify compiler path and sysroot | Use `--print-search-dirs` to verify compiler paths; set `CROSS_COMPILE` prefix explicitly |
| `pkg-config` not finding library | Check `PKG_CONFIG_PATH` | Run `pkg-config --cflags --libs libname`; verify `.pc` files exist in `PKG_CONFIG_PATH` |

## Code Review Checklist

- [ ] Compiler warnings enabled (`-Wall -Wextra -Werror`) in build system
- [ ] Header dependency tracking enabled (`-MMD -MP` in Makefile)
- [ ] Debug and release builds separated (`-g` for debug, `-O2` for release)
- [ ] `make clean` target available for fresh builds
- [ ] Test target available (`make test` or `ctest`)
- [ ] Cross-compilation support documented and tested
- [ ] External dependencies managed through `pkg-config` or CMake `find_package`
- [ ] Build reproducible (same flags on every machine)

## Architecture Considerations

Build systems automate compilation, linking, and deployment. For small projects (< 5 source files), a simple Makefile suffices. For medium projects, CMake provides cross-platform support. For large projects with complex dependencies, Meson or Bazel may be appropriate. The key principle: the build system should be the single source of truth for how the project is built — no manual `gcc` commands.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Makefile with `-MMD -MP` | Small to medium projects | Simple, portable, but verbose for complex dependency trees |
| CMake | Cross-platform, large projects | Generates Makefiles/Ninja; powerful but has its own learning curve |
| Meson | Modern new projects | Fast, readable syntax; less ecosystem support than CMake |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Build reproducibility failure | Unverified binaries, supply chain risk | Pin compiler versions; use deterministic build flags; verify hashes |
| Insecure compiler flags in release | Missing security features | Enforce `-fstack-protector-strong`, `-D_FORTIFY_SOURCE=2`, `-pie -fPIE` in release builds |
| Dependency vulnerabilities | Known CVEs in third-party libraries | Use `cve-check` tools; pin dependency versions; audit dependencies regularly |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `bool`, `//` comments, improved `Makefile` compatibility | Adopt modern Makefile practices; use `:=` for immediate assignment |
| C99 → C11 | Added `_Static_assert`, `<stdatomic.h>` | Add `-std=c11` to `CFLAGS`; use C11 features for build-time checks |
| C11 → C23 | Added `typeof`, improved `constexpr`, `#embed` | Add `-std=c23` to `CFLAGS` for latest features; use `#embed` for binary data |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| `-std=c99` / `-std=c11` / `-std=c23` | Compiler flag | Use to specify C standard version in build system |
| `-Wall -Wextra -Werror` | Compiler flags | Enable in all builds; `-Werror` in CI/CD |
| `-MMD -MP` (dependency tracking) | GCC/Clang flag | Enable in Makefiles for automatic header dependency tracking |
| `-D_FORTIFY_SOURCE=2` (buffer overflow detection) | GCC/Clang flag | Enable in release builds for runtime buffer overflow detection |

## Interview Questions

1. **Why use `-MMD -MP` in Makefiles?**: `-MMD` generates `.d` dependency files listing header dependencies for each `.c` file. `-MP` adds phony targets for each header to prevent errors if headers are deleted. Together they ensure header changes trigger recompilation.
2. **What is the difference between `make` and CMake?**: `make` reads Makefiles directly and is simple but platform-specific. CMake generates platform-specific build files (Makefiles, Ninja, Visual Studio projects) from `CMakeLists.txt`. CMake is better for cross-platform projects.
3. **How do you handle external library dependencies in C?**: Use `pkg-config` to discover compiler/linker flags. For CMake, use `find_package` or `FetchContent`. For Makefiles, use `$(shell pkg-config --cflags --libs libname)`. Pin dependency versions for reproducibility.
4. **Why separate debug and release builds?**: Debug builds include `-g` (debug symbols) and skip optimization (`-O0`) for easier debugging. Release builds use `-O2` or `-O3` for performance and strip debug info. Mixing them causes confusing behavior (optimized-out variables in debugger).
5. **What flags should always be in a release build?**: `-O2` (optimization), `-DNDEBUG` (disable assert), `-fstack-protector-strong` (stack canaries), `-D_FORTIFY_SOURCE=2` (buffer overflow detection), `-pie -fPIE` (ASLR), `-Wl,-z,relro -Wl,-z,now` (read-only relocations).

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [GNU Make Manual](https://www.gnu.org/software/make/manual/)
- [CMake Documentation](https://cmake.org/cmake/help/latest/)

## Overview

The Build Systems module covers automating C project compilation with Make, CMake, and pkg-config. Build systems automate recompilation of only changed files, resolve dependencies, run tests, and work identically on every developer's machine.

## Learning Objectives

- Write Makefiles for C projects
- Use CMake for cross-platform builds
- Integrate external libraries with pkg-config
- Build static and shared libraries
- Manage dependencies effectively

## Prerequisites

- Completion of Module 13 (Testing)
- Understanding of compilation process
- Basic command-line skills

## History

- **1976** — Stuart Feldman created Make at Bell Labs
- **1988** — GNU Make released with powerful features
- **1995** — CMake created by Brad King
- **2000** — pkg-config created for library discovery
- **2010** — Meson created as modern alternative
- **2020** — Build systems support cross-compilation

## Production Notes

- **Where is it used?** All C projects with multiple source files
- **Why is it useful?** Automates recompilation, resolves dependencies, ensures reproducibility
- **When should it be avoided?** One-off scripts or throwaway code
- **Alternative?** Meson, Ninja, Bazel, xmake

## Core Concepts

### Build Tools

| Tool | Purpose | Complexity |
|------|---------|------------|
| `gcc`/`clang` | Direct compilation | Simple projects |
| `make` | Build automation | Medium projects |
| CMake | Cross-platform build generation | Large projects |
| `pkg-config` | Library configuration | Dependency discovery |
| Meson | Modern build system | New projects |

### Build Targets

| Target | Purpose | Example |
|--------|---------|---------|
| `all` | Build everything | `make all` |
| `clean` | Remove build artifacts | `make clean` |
| `install` | Install to system | `make install` |
| `test` | Run tests | `make test` |

## Internal Working

### Makefile Execution

```
Makefile
    ↓
Parse rules and dependencies
    ↓
Check timestamps (which files changed?)
    ↓
Execute commands for outdated targets
    ↓
Build complete
```

### Dependency Graph

```
main.o → main.c, utils.h
utils.o → utils.c, utils.h
math.o → math.c, math.h
    ↓
Link: main.o utils.o math.o → program
```

## Syntax

```makefile
# Simple Makefile
CC = gcc
CFLAGS = -Wall -Wextra -O2
SOURCES = main.c utils.c
OBJECTS = $(SOURCES:.c=.o)
TARGET = program

all: $(TARGET)

$(TARGET): $(OBJECTS)
	$(CC) $(CFLAGS) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm -f $(OBJECTS) $(TARGET)

.PHONY: all clean
```

```cmake
# CMakeLists.txt
cmake_minimum_required(VERSION 3.10)
project(MyProject C)

set(CMAKE_C_STANDARD 11)

add_executable(program main.c utils.c)

target_compile_options(program PRIVATE -Wall -Wextra)
```

## Examples

### Easy Example: Simple Makefile

```makefile
CC = gcc
CFLAGS = -Wall

program: main.o
	$(CC) -o $@ $^

main.o: main.c
	$(CC) $(CFLAGS) -c $<

clean:
	rm -f *.o program
```

### Medium Example: Multi-File Project

```makefile
CC = gcc
CFLAGS = -Wall -Wextra -Iinclude
SRCDIR = src
OBJDIR = obj
SOURCES = $(wildcard $(SRCDIR)/*.c)
OBJECTS = $(patsubst $(SRCDIR)/%.c,$(OBJDIR)/%.o,$(SOURCES))
TARGET = program

all: $(TARGET)

$(TARGET): $(OBJECTS)
	$(CC) -o $@ $^

$(OBJDIR)/%.o: $(SRCDIR)/%.c | $(OBJDIR)
	$(CC) $(CFLAGS) -c -o $@ $<

$(OBJDIR):
	mkdir -p $(OBJDIR)

clean:
	rm -rf $(OBJDIR) $(TARGET)

.PHONY: all clean
```

### Hard Example: CMake with Libraries

```cmake
# CMakeLists.txt
cmake_minimum_required(VERSION 3.10)
project(MyProject VERSION 1.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# Find packages
find_package(PkgConfig)
pkg_check_modules(SOCKETS REQUIRED IMPORTED_TARGET libsockets)

# Add library
add_library(mylib STATIC src/mylib.c)
target_include_directories(mylib PUBLIC include)

# Add executable
add_executable(program src/main.c)
target_link_libraries(program PRIVATE mylib PkgConfig::SOCKETS)

# Enable testing
enable_testing()
add_test(NAME mytest COMMAND program)
```

### Enterprise Example: Cross-Compilation

```cmake
# Toolchain file for cross-compilation
set(CMAKE_SYSTEM_NAME Linux)
set(CMAKE_SYSTEM_PROCESSOR arm)

set(CMAKE_C_COMPILER arm-linux-gnueabihf-gcc)
set(CMAKE_CXX_COMPILER arm-linux-gnueabihf-g++)

set(CMAKE_FIND_ROOT_PATH /usr/arm-linux-gnueabihf)
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Parallel builds | `-j` flag for make | Use all CPU cores |
| Incremental builds | Only rebuild changed files | Use dependency tracking |
| Clean builds | Full rebuild | Use `make clean` when needed |
| Cross-compilation | Different target | Use toolchain files |
| Caching | CMake cache | Reuse configuration |

## Best Practices

- Do:
  - Use `-MMD -MP` for automatic dependency tracking
  - Support `make clean` target
  - Use explicit `CFLAGS` and `LDFLAGS`
  - Test builds on clean environment
  - Document build requirements
  
- Don't:
  - Hardcode paths
  - Ignore compiler warnings
  - Use absolute paths in Makefiles
  - Forget to update dependencies
  - Use implicit rules without understanding

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Missing header dependency | Incorrect builds | Use `-MMD -MP` |
| Implicit rules | Unexpected behavior | Use explicit rules |
| No `make clean` | Stale artifacts | Always support clean |
| Hardcoded paths | Portability issues | Use variables |
| Ignoring warnings | Hidden bugs | Enable `-Wall -Wextra` |

## Interview Questions

### Q1: What is the difference between `make` and `cmake`?
**Answer:** `make`: runs Makefiles. `cmake`: generates Makefiles (or other build files) for cross-platform builds.

### Q2: What is the purpose of `-MMD -MP` flags?
**Answer:** `-MMD`: generates dependency files. `-MP`: adds phony targets for missing headers. Enables automatic dependency tracking.

### Q3: What is the difference between static and shared libraries?
**Answer:** Static: linked at compile time (larger binary). Shared: linked at runtime (smaller binary, shared code).

### Q4: What is `pkg-config` used for?
**Answer:** Discovers compiler flags and linker flags for installed libraries. Example: `pkg-config --cflags --libs gtk+-3.0`.

### Q5: What is the difference between `$@` and `$<` in Makefiles?
**Answer:** `$@`: target name. `$<`: first prerequisite. Used in implicit rules.

### Q6: What is the purpose of `.PHONY`?
**Answer:** Declares targets as phony (not files). Prevents conflicts with actual files named `clean`, `all`, etc.

### Q7: What is the difference between `CFLAGS` and `LDFLAGS`?
**Answer:** `CFLAGS`: compiler flags (e.g., `-O2`, `-Wall`). `LDFLAGS`: linker flags (e.g., `-L`, `-l`).

### Q8: What is the difference between `wildcard` and `patsubst` in Make?
**Answer:** `wildcard`: expands glob patterns (e.g., `*.c`). `patsubst`: substitutes patterns (e.g., `.c` → `.o`).

### Q9: What is the difference between `add_executable` and `add_library` in CMake?
**Answer:** `add_executable`: creates an executable. `add_library`: creates a static or shared library.

### Q10: What is the purpose of `find_package` in CMake?
**Answer:** Finds external packages/libraries. Example: `find_package(Threads REQUIRED)`.

### Q11: What is the difference between `include_directories` and `target_include_directories`?
**Answer:** `include_directories`: global, affects all targets. `target_include_directories`: per-target, more precise.

### Q12: What is the difference between `make -j` and `make -j4`?
**Answer:** `-j`: unlimited parallelism. `-j4`: limit to 4 parallel jobs. Use specific number to avoid overloading.

### Q13: What is the purpose of `export` in Makefiles?
**Answer:** Exports variables to sub-makes. Used for passing variables to recursive makes.

### Q14: What is the difference between `order-only prerequisites` and regular prerequisites?
**Answer:** Regular: rebuild target if prerequisite changes. Order-only: only build if prerequisite doesn't exist (e.g., directories).

### Q15: What is the difference between `CMAKE_BUILD_TYPE` values?
**Answer:** `Debug`: no optimization, debug info. `Release`: full optimization. `RelWithDebInfo`: optimized with debug info.

## Cross-References

- **Previous Module:** [13 - Testing](../13-testing/)
- **Next Module:** [15 - Best Practices](../15-best-practices/)
- **Related:** [03 - Preprocessor](../03-preprocessor/) — Build-time preprocessing
- **Related:** [00 - Knowledge Atoms](../00-knowledge-atoms/) — Compilation model
- **External:** [GNU Make Manual](https://www.gnu.org/software/make/manual/)
- **External:** [CMake Documentation](https://cmake.org/cmake/help/latest/)

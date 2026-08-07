# Build Systems

## What it is
Tools and processes for compiling, linking, and packaging C++ code.

## Why it exists
To automate the build process and manage dependencies.

## When to use it
For any non-trivial C++ project.

## How it works

### CMake
```cmake
cmake_minimum_required(VERSION 3.10)
project(MyProject)

set(CMAKE_CXX_STANDARD 17)

add_executable(main main.cpp)
target_link_libraries(main PRIVATE mylib)
```

### Makefile
```makefile
CXX = g++
CXXFLAGS = -std=c++17 -Wall -Wextra

main: main.o mylib.o
	$(CXX) $(CXXFLAGS) -o main main.o mylib.o

clean:
	rm -f main *.o
```

### Package Management
```bash
# vcpkg
vcpkg install boost

# Conan
conan install boost/1.80.0
```

## Production Checklist
- [ ] Use CMake for cross-platform builds
- [ ] Set C++ standard explicitly
- [ ] Enable compiler warnings
- [ ] Use separate build directory
- [ ] Implement clean target
- [ ] Use package manager for dependencies

## Maturity Levels
- **Beginner**: Basic Makefile, simple CMake
- **Intermediate**: Complex CMake, package management
- **Advanced**: Custom toolchains, cross-compilation

## Common Myths
- ❌ "Makefiles are always better"
- ❌ "CMake is too complex"
- ❌ "Build systems don't matter"

## One-Minute Revision
| Tool | Purpose |
|------|---------|
| Make | Build automation |
| CMake | Cross-platform build system |
| vcpkg | Package management |
| Conan | Package management |
| Ninja | Fast build system |

## Related Topics
- [Testing](../10-testing/)
- [Best Practices](../14-best-practices/)
- [Performance](../11-performance/)
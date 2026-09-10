# Build Systems — C++

## Overview

C++ build systems automate the process of compiling source files, resolving dependencies, linking object files, and producing executables or libraries. Unlike languages with package managers that handle everything (e.g., Rust's Cargo, Go's modules), C++ requires explicit build configuration due to its compilation model, platform diversity, and lack of a standard build system. The primary tools are CMake (meta-build system generating native build files), Make/Ninja (build automation), and package managers like vcpkg and Conan for dependency management. Choosing the right build system and configuration directly impacts compilation speed, cross-platform support, CI/CD reliability, and developer productivity.

## Learning Objectives

- Understand the role of build systems in the C++ compilation pipeline
- Configure CMake projects using modern target-based design (CMake 3.16+)
- Manage dependencies with vcpkg (manifest mode) and Conan (conanfile.py)
- Set up cross-platform CI/CD pipelines with multi-compiler testing
- Debug common build failures (missing headers, version conflicts, wrong build type)
- Optimize build performance using Ninja, ccache, sccache, and parallel builds
- Apply security hardening flags and validate build configurations in CI

## Prerequisites

- [Module 01: Fundamentals](../01-fundamentals/) — Basic C++ syntax, compilation model, header/source separation
- Familiarity with terminal/command-line operations
- Basic understanding of compiler flags and linking

## History

| Era | Tool | Year | Significance |
|-----|------|------|--------------|
| 1976 | Make | 1976 | Stuart Feldman created Make at Bell Labs. Introduced dependency-driven builds with rules. Became the Unix standard for decades. |
| 2000 | CMake | 2000 | Bill Hoffman created CMake at Kitware. Solved the cross-platform problem by generating native build files (Makefiles, VS solutions, Ninja). |
| 2004 | Bazel | 2014* | Google created Blaze (2004), open-sourced as Bazel (2014). Designed for massive monorepos with hermetic, reproducible builds. |
| 2013 | Ninja | 2013 | Evan Martin created Ninja. Minimalist build system focused on speed. Typically used as a CMake backend instead of Make. |
| 2016 | vcpkg | 2016 | Microsoft released vcpkg. Manifest mode (2020) brought version pinning and reproducible dependency management. |
| 2016 | Conan | 2016 | JFrog released Conan. Cross-platform package manager with profiles, binary management, and remote repositories. |
| 2020 | CMake Presets | 2020 | CMake 3.19 introduced presets (JSON configs). Eliminated shell-script-based build configuration, enabling team-wide consistency. |
| 2023 | C++20 Modules | Ongoing | Modules replace `#include` with `import`. Build systems are evolving to support module dependency graphs and header-unit compilation. |

*Bazel's open-source release; Blaze was internal at Google from 2004.

**Evolution trajectory**: Manual Makefiles → CMake cross-platform generation → Package managers (vcpkg/Conan) → Presets for consistency → C++20 modules for faster builds.

## Production Notes

- **Build times matter**: Large C++ projects can take 30+ minutes to build. Ninja + ccache/sccache + parallel builds (`-j`) reduce this to minutes.
- **CI costs scale with build time**: A 10-minute build at $0.10/min on 1000 daily CI runs costs ~$3,000/month. Optimizing builds directly reduces infrastructure costs.
- **Dependency resolution is slow**: vcpkg and Conan download and compile dependencies from source. Cache dependency builds in CI and use binary packages when available.
- **Cross-compilation requires toolchains**: Embedded systems, mobile (Android/iOS), and WASM targets need explicit toolchain files. Test cross-compilation in CI early.
- **Lock files are essential**: Without `vcpkg.json` lock files or Conan lock files, builds are not reproducible. Always commit lock files to version control.

## Core Concepts

### Build Pipeline

```
Source Files (.cpp/.h) → Preprocessing → Compilation (.o/.obj) → Linking → Executable/Library
```

### CMake Target Model

```cmake
# Targets are the fundamental units — they carry properties transitively
add_library(mylib STATIC src/mylib.cpp)
target_include_directories(mylib PUBLIC include/)          # Propagates to consumers
target_compile_features(mylib PUBLIC cxx_std_17)           # Propagates C++ standard
target_link_libraries(mylib PUBLIC fmt::fmt)               # Propagates fmt dependency
```

### Dependency Resolution

- **System packages**: Installed via apt/brew. Version uncontrolled, platform-specific.
- **vcpkg**: Manifest mode (`vcpkg.json`) pins versions. Toolchain integration with CMake.
- **Conan**: Profile-based configuration. Binary caching. Remote repositories.

### Build Types

| Type | Macros | Optimization | Debug Info | Use Case |
|------|--------|-------------|------------|----------|
| Debug | `NDEBUG` undefined | `-O0` | Full (`-g`) | Development |
| Release | `NDEBUG` defined | `-O2`/`-O3` | None | Production |
| RelWithDebInfo | `NDEBUG` defined | `-O2` | Minimal (`-g1`) | Profiling production |
| MinSizeRel | `NDEBUG` defined | `-Os` | None | Size-constrained deployment |

## Internal Working

### How CMake Generates Build Files

1. **Configure phase** (`cmake -B build`):
   - Reads `CMakeLists.txt` files recursively
   - Executes `find_package()` to locate dependencies
   - Evaluates `if()` conditions and `option()` values
   - Generates `CMakeCache.txt` with all configuration variables
   - Outputs native build files (Makefiles, `build.ninja`, or `.sln`)

2. **Generate phase** (part of configure):
   - Creates `CMakeFiles/` directory with compiler detection results
   - Generates dependency graphs between targets
   - Writes build rules (compile commands, link commands) to native build files

3. **Build phase** (`cmake --build build`):
   - Native build tool (Make/Ninja) reads generated rules
   - Compiles only changed source files (incremental builds)
   - Links object files into executables/libraries
   - CTest runs tests if configured

### Dependency Resolution Flow

```
find_package(Boost 1.80 REQUIRED)
  → CMake searches CMAKE_PREFIX_PATH, system paths
  → Checks BoostConfig.cmake or FindBoost.cmake
  → Sets Boost_FOUND, Boost_INCLUDE_DIRS, Boost_LIBRARIES
  → target_link_libraries(mylib PUBLIC Boost::system) propagates to consumers
```

### Ninja vs Make

| Aspect | Make | Ninja |
|--------|------|-------|
| Speed | Slower (rebuilds DAG each time) | Faster (pre-computed build graph) |
| Parallelism | `-j N` flag | Built-in parallel execution |
| Dependency tracking | Basic | Fine-grained, auto-generated by CMake |
| Use case | Legacy, simple projects | Modern CMake projects |

## Syntax

### CMake Minimum Version and Project Declaration

```cmake
cmake_minimum_required(VERSION 3.16)       # Enforces minimum CMake version
project(MyProject                           # Project name
    VERSION 1.0.0                           # Semantic version
    LANGUAGES CXX                           # Enable C++ compiler
    DESCRIPTION "My C++ project"            # Optional description
    HOMEPAGE_URL "https://example.com"      # Optional URL
)
```

### Target-Based Commands

```cmake
# Libraries
add_library(mylib STATIC src/a.cpp src/b.cpp)          # Static library
add_library(mylib SHARED src/a.cpp src/b.cpp)          # Shared library
add_library(mylib INTERFACE)                             # Header-only library

# Executables
add_executable(app src/main.cpp)

# Properties
target_include_directories(mylib PUBLIC ${CMAKE_CURRENT_SOURCE_DIR}/include)
target_compile_options(mylib PRIVATE -Wall -Wextra -Werror)
target_compile_definitions(mylib PRIVATE MY_DEFINE=1)
target_link_libraries(mylib PUBLIC Boost::system Threads::Threads)
target_compile_features(mylib PUBLIC cxx_std_17)
```

### Package Management

```cmake
# vcpkg integration (automatic with toolchain file)
find_package(fmt CONFIG REQUIRED)    # vcpkg provides config files
target_link_libraries(mylib PUBLIC fmt::fmt)

# Conan integration
find_package(fmt REQUIRED)           # Conan generates FindModule or config
target_link_libraries(mylib PUBLIC fmt::fmt)
```

### Install Rules

```cmake
install(TARGETS mylib
    EXPORT mylibTargets
    LIBRARY DESTINATION lib
    ARCHIVE DESTINATION lib
    RUNTIME DESTINATION bin
)
install(DIRECTORY include/ DESTINATION include)
install(EXPORT mylibTargets
    FILE mylibTargets.cmake
    NAMESPACE mylib::
    DESTINATION lib/cmake/mylib
)
```

## Examples

*(See existing "Expanded Code Examples" section above for CMake, vcpkg, Conan, CI/CD, and Presets examples.)*

## Performance Considerations

### Parallel Builds

```bash
# Use all CPU cores
cmake --build build -j$(nproc 2>/dev/null || sysctl -n hw.ncpu)

# Or set in CMake
cmake -B build -DCMAKE_BUILD_PARALLEL_LEVEL=8
```

### Build Cache (ccache / sccache)

```bash
# ccache — caches compiler output, skips recompilation of unchanged files
cmake -B build -DCMAKE_CXX_COMPILER_LAUNCHER=ccache

# sccache — distributed cache (works with CI caches)
cmake -B build -DCMAKE_CXX_COMPILER_LAUNCHER=sccache

# Verify it's working
ccache -s    # Show cache statistics
```

### Ninja Generator

```bash
# Ninja is 2-5x faster than Make for incremental builds
cmake -B build -G Ninja
```

### Build Time Optimization

| Technique | Speedup | Trade-off |
|-----------|---------|-----------|
| Ninja instead of Make | 2-5x | Less flexible than Make |
| ccache/sccache | 5-10x (cached) | Requires cache management |
| Parallel builds (`-j`) | Linear with cores | Memory usage increases |
| Unity builds (`CMAKE_UNITY_BUILD`) | 2-3x | Hides TU-specific errors |
| Precompiled headers (PCH) | 1.5-2x | Extra build step |
| LTO | Runtime 5-15% | Significantly slower link time |

### When to Use LTO

```cmake
# Release-only LTO (recommended)
include(CheckIPOSupported)
check_ipo_supported(RESULT ipo_supported)
if(ipo_supported AND CMAKE_BUILD_TYPE STREQUAL "Release")
    set_target_properties(mylib PROPERTIES INTERPROCEDURAL_OPTIMIZATION TRUE)
endif()
```

## Best Practices

- **Use modern CMake (target-based)**: Prefer `target_include_directories` over `include_directories`. Use `target_link_libraries` for all dependencies.
- **Pin dependency versions**: Always pin exact versions in `vcpkg.json` or `conanfile.py`. Use lock files.
- **Test with multiple compilers**: CI should test GCC, Clang, and MSVC (if targeting Windows).
- **Use CMake Presets**: Share build configurations across the team via `CMakePresets.json`.
- **Default to RelWithDebInfo**: Never default to Debug in production CMakeLists.txt. Use presets for Debug.
- **Enable compiler warnings**: Always `-Wall -Wextra -Wpedantic -Werror` (treat warnings as errors).
- **Separate build from source**: Build in a `build/` directory, never in the source tree.
- **Use `cmake_minimum_required`**: Enforce the minimum CMake version you actually need.
- **Cache CI builds**: Cache `~/.ccache`, vcpkg installed packages, and Conan caches in CI.
- **Validate build type in CI**: Add a step that checks the binary is not a Debug build.

## Common Mistakes

| Mistake | Why It's Wrong | Fix |
|---------|---------------|-----|
| Using `include_directories()` globally | Pollutes all targets, breaks encapsulation | Use `target_include_directories` per target |
| No version pinning in dependencies | Builds break when dependencies update | Pin versions in `vcpkg.json` / `conanfile.py` |
| Debug build in production | 10x slower, assertions crash service | CI validates build type; deployment uses `--config Release` |
| Not using `cmake_minimum_required` | CMake behavior changes between versions | Always set minimum CMake version |
| Hardcoded compiler flags | Breaks cross-platform/other compilers | Use `if(MSVC)...else()...endif()` or generator expressions |
| Committing `build/` directory | Wastes repo space, merge conflicts | Add `build/` to `.gitignore` |
| Ignoring CMake warnings | Warnings become errors in future versions | Fix CMake warnings immediately |
| Not using `find_package` for deps | Manual paths break on other machines | Use `find_package` or package manager integration |

## Cross-References

- **Testing** → [Module 10: Testing](../10-testing/) — CTest integration, test targets in CMake
- **Performance** → [Module 11: Performance](../11-performance/) — Compiler flags, LTO, PGO configuration
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Build configuration as best practice
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — C++ standard selection, feature detection
- **Networking** → [Module 12: Networking](../12-networking/) — Linking libcurl, Boost.Asio
- **Senior Level** → [Module 15: Senior](../15-senior/) — Build system architecture decisions
- **Fundamentals** → [Module 01: Fundamentals](../01-fundamentals/) — Compilation model, linking basics

## Engineering Decision Framework

| Decision | Approach | When to Use | When NOT to Use |
|----------|----------|-------------|-----------------|
| Build system | CMake vs Meson vs Bazel | CMake for industry standard, Meson for simplicity, Bazel for monorepos | Raw Makefiles for anything non-trivial |
| Package manager | vcpkg vs Conan vs system packages | vcpkg for Microsoft ecosystem, Conan for cross-platform | System packages for version-sensitive dependencies |
| Compiler | GCC vs Clang vs MSVC | Clang for warnings, GCC for performance, MSVC for Windows | Defaulting to one without testing others |
| C++ standard | C++17 vs C++20 vs C++23 | Match what your team and compilers support | Bleeding-edge standard without CI validation |
| Build type | Debug vs Release vs RelWithDebInfo | Debug for development, Release for deployment | Debug builds in production |
| Dependencies | Static vs dynamic linking | Static for distribution, dynamic for OS integration | Dynamic linking for small projects with few dependencies |

## Expanded Code Examples

### Modern CMake (3.16+)

```cmake
cmake_minimum_required(VERSION 3.16)
project(MyProject VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Build options
option(BUILD_TESTS "Build unit tests" ON)
option(BUILD_BENCHMARKS "Build benchmarks" OFF)

# Compiler warnings
if(MSVC)
    add_compile_options(/W4 /WX)
else()
    add_compile_options(-Wall -Wextra -Wpedantic -Werror)
endif()

# Find dependencies
find_package(Threads REQUIRED)
find_package(Boost 1.70 REQUIRED COMPONENTS system filesystem)

# Main library
add_library(mylib
    src/mylib.cpp
    src/utils.cpp
)
target_include_directories(mylib PUBLIC
    $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
    $<INSTALL_INTERFACE:include>
)
target_link_libraries(mylib PUBLIC
    Boost::system
    Boost::filesystem
    Threads::Threads
)

# Main executable
add_executable(main src/main.cpp)
target_link_libraries(main PRIVATE mylib)

# Tests
if(BUILD_TESTS)
    enable_testing()
    find_package(GTest REQUIRED)

    add_executable(tests
        tests/test_mylib.cpp
        tests/test_utils.cpp
    )
    target_link_libraries(tests PRIVATE
        mylib
        GTest::GTest
        GTest::Main
    )
    add_test(NAME MyTests COMMAND tests)
endif()

# Install rules
install(TARGETS mylib
    LIBRARY DESTINATION lib
    ARCHIVE DESTINATION lib
)
install(DIRECTORY include/ DESTINATION include)
```

### Handling Dependencies with vcpkg

```bash
# Initialize vcpkg in your project
git submodule add https://github.com/microsoft/vcpkg.git extern/vcpkg
./extern/vcpkg/bootstrap-vcpkg.sh

# Create vcpkg.json manifest
```

```json
{
    "name": "myproject",
    "version-string": "1.0.0",
    "dependencies": [
        "boost-system",
        "boost-filesystem",
        "fmt",
        "spdlog",
        "nlohmann-json",
        {
            "name": "gtest",
            "version>=": "1.12.0",
            "platform": "linux | osx"
        }
    ]
}
```

```bash
# Configure with vcpkg toolchain
cmake -B build -DCMAKE_TOOLCHAIN_FILE=extern/vcpkg/scripts/buildsystems/vcpkg.cmake

# Build
cmake --build build --config Release

# The toolchain file automatically resolves and installs dependencies
```

### Handling Dependencies with Conan

```python
# conanfile.py
from conans import ConanFile, CMake

class MyProjectConan(ConanFile):
    name = "myproject"
    version = "1.0.0"
    settings = "os", "compiler", "build_type", "arch"
    requires = (
        "boost/1.80.0",
        "fmt/9.1.0",
        "spdlog/1.11.0",
        "nlohmann_json/3.11.2",
        "gtest/1.12.1"
    )
    generators = "cmake", "cmake_find_package_multi"

    def build(self):
        cmake = CMake(self)
        cmake.configure()
        cmake.build()

    def imports(self):
        self.copy("*.dll", dst="bin", src="bin")
        self.copy("*.dylib*", dst="bin", src="lib")
```

```bash
# Install dependencies
conan install . --output-folder=build --build=missing

# Configure and build
cd build
cmake .. -DCMAKE_TOOLCHAIN_FILE=conan_toolchain.cmake -DCMAKE_BUILD_TYPE=Release
cmake --build .
```

### Cross-Platform Build Script

```bash
#!/bin/bash
# build.sh — Cross-platform build script

set -euo pipefail

BUILD_DIR="build"
BUILD_TYPE="${1:-Release}"

echo "Building project (${BUILD_TYPE})..."

# Create build directory
mkdir -p "${BUILD_DIR}"

# Configure
cmake -B "${BUILD_DIR}" \
    -DCMAKE_BUILD_TYPE="${BUILD_TYPE}" \
    -DCMAKE_EXPORT_COMPILE_COMMANDS=ON \
    -DBUILD_TESTS=ON

# Build with all available cores
cmake --build "${BUILD_DIR}" --config "${BUILD_TYPE}" -j$(nproc 2>/dev/null || sysctl -n hw.ncpu)

# Run tests
cd "${BUILD_DIR}"
ctest --output-on-failure -j$(nproc 2>/dev/null || sysctl -n hw.ncpu)

echo "Build complete!"
```

### CMake Presets (Modern Approach)

```json
{
    "version": 3,
    "configurePresets": [
        {
            "name": "default",
            "binaryDir": "${sourceDir}/build/${presetName}",
            "generator": "Ninja",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Release",
                "CMAKE_EXPORT_COMPILE_COMMANDS": "ON"
            }
        },
        {
            "name": "debug",
            "inherits": "default",
            "binaryDir": "${sourceDir}/build/debug",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Debug"
            }
        },
        {
            "name": "ci",
            "inherits": "default",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "RelWithDebInfo",
                "BUILD_TESTS": "ON"
            }
        }
    ],
    "buildPresets": [
        {
            "name": "default",
            "configurePreset": "default"
        },
        {
            "name": "debug",
            "configurePreset": "debug"
        }
    ],
    "testPresets": [
        {
            "name": "default",
            "configurePreset": "default",
            "output": {
                "outputOnFailure": true
            }
        }
    ]
}
```

### CI/CD Integration (GitHub Actions)

```yaml
name: CI
on: [push, pull_request]

jobs:
  build:
    strategy:
      matrix:
        os: [ubuntu-latest, macos-latest, windows-latest]
        compiler: [gcc, clang]
        exclude:
          - os: windows-latest
            compiler: gcc
          - os: macos-latest
            compiler: gcc

    runs-on: ${{ matrix.os }}

    steps:
      - uses: actions/checkout@v3
        with:
          submodules: recursive

      - name: Install dependencies
        run: |
          if [ "${{ matrix.os }}" = "ubuntu-latest" ]; then
            sudo apt-get update
            sudo apt-get install -y cmake ninja-build libgtest-dev
          elif [ "${{ matrix.os }}" = "macos-latest" ]; then
            brew install cmake ninja googletest
          fi

      - name: Configure
        run: cmake -B build -G Ninja -DCMAKE_BUILD_TYPE=RelWithDebInfo -DBUILD_TESTS=ON

      - name: Build
        run: cmake --build build -j$(nproc)

      - name: Test
        run: cd build && ctest --output-on-failure
```

## Production Incidents

### Incident 1: Missing Header Breaking CI
**Problem**: A developer committed code that included `<format>` (C++20) but the CI compiler (GCC 9) didn't support it. The build passed locally (GCC 12) but failed on CI.

**Cause**: No compiler version check in CMakeLists.txt. No CI matrix testing across compiler versions. The developer's local environment had a newer compiler than CI.

**Impact**: CI pipeline broken for 4 hours while the team scrambled to either upgrade the CI compiler or remove the dependency. Release candidate delayed by 1 day.

**Detection**: CI build failed with `<format>: No such file or directory`.

**Solution**: Added compiler version check in CMakeLists.txt:
```cmake
if(CMAKE_CXX_COMPILER_VERSION VERSION_LESS "10.0")
    message(FATAL_ERROR "GCC 10+ or Clang 12+ required for C++20 features")
endif()
```
Added a CI matrix that tests with GCC 9, GCC 11, GCC 13, Clang 12, and Clang 15.

**Prevention**: Always test with multiple compiler versions in CI. Use feature detection (`check_include_file`) instead of assuming compiler support. Document minimum compiler requirements in README.

### Incident 2: Debug Build in Production
**Problem**: A production server was running a Debug build with assertions enabled, causing 10x slower performance and occasional assertion failures that crashed the service.

**Cause**: The deployment script used `cmake --build build` without specifying `--config Release`. The default build type was Debug (set in CMakeLists.txt for developer convenience). The CI pipeline didn't validate the build type.

**Impact**: Production latency increased 10x for 2 days before detection. 3 assertion failures crashed the service. Customer-facing SLA was violated.

**Detection**: Performance monitoring showed 10x latency increase. Crash reports included assertion failure messages.

**Solution**: Changed CMakeLists.txt default to `RelWithDebInfo`. Added a build-type validation step in CI that checks for debug symbols and asserts the binary is optimized. Deployment script now explicitly passes `--config Release`.

**Prevention**: Never default to Debug in CMakeLists.txt. CI should validate build type. Deployment scripts must always specify Release configuration. Add a runtime check that logs build type on startup.

### Incident 3: Dependency Version Conflict
**Problem**: A project used both Boost 1.75 and a third-party library that required Boost 1.80. The build succeeded with Boost 1.80 but failed with Boost 1.75 due to API changes.

**Cause**: The CMakeLists.txt used `find_package(Boost)` without version constraints. Different developers had different Boost versions installed via system packages. CI used a Docker image with Boost 1.75.

**Impact**: Build failures on 40% of developer machines and in CI. Time wasted debugging build issues instead of feature development. 2-day delay in sprint.

**Detection**: Build errors referencing missing Boost symbols. `cmake --find-package` showed different Boost versions on different machines.

**Solution**: Added minimum version constraint: `find_package(Boost 1.80 REQUIRED)`. Added vcpkg manifest pinning Boost to 1.80.0. CI Docker image updated to include Boost 1.80.

**Prevention**: Always pin dependency versions. Use a package manager (vcpkg/Conan) instead of system packages. CI should use the same dependency versions as development. Document all dependency version requirements.

### Incident 4: Non-Reproducible Build Due to Unpinned Tools
**Problem**: A developer installed a new version of CMake (3.28) that changed the default behavior of `CMAKE_POLICY(SET CMP0077 NEW)`. The project built fine on their machine but failed on CI where CMake 3.22 was installed, with `option()` variables being silently ignored.

**Cause**: `cmake_minimum_required(VERSION 3.16)` was set but the project used features that silently changed behavior in CMake 3.27 (policy CMP0077). CI had an older CMake; developer had a newer one. No lock on CMake version.

**Impact**: Build failed on CI for 6 hours. Two developers spent time investigating the wrong root cause (suspected dependency issue). Sprint velocity dropped.

**Detection**: CI build error: `option() variable not being set despite being passed on command line`.

**Solution**: Pinned CMake version in CI via `pip install cmake==3.28.0`. Added a version check in CMakeLists.txt:
```cmake
if(CMAKE_VERSION VERSION_LESS "3.27")
    message(WARNING "CMake 3.27+ recommended for policy CMP0077")
endif()
```
Documented required CMake version in README and CI workflow.

**Prevention**: Pin tool versions in CI (CMake, Ninja, compiler). Use `cmake_minimum_required` with the actual minimum you test against. Document all tool version requirements. Consider using `cmake --version` validation in CI scripts.

### Incident 5: Race Condition in Parallel Builds
**Problem**: A large C++ project with 500+ translation units failed intermittently during parallel builds (`-j32`). The error was a missing generated header file — sometimes it compiled, sometimes it didn't.

**Cause**: A custom command generated a header file (`generated_config.h`) via `add_custom_command`, but the target that consumed it didn't properly declare the dependency. Under heavy parallelism, the consumer compiled before the generator finished.

**Impact**: CI builds failed ~30% of the time. Developers reran builds wasting 10-15 minutes each time. False positives in CI reduced trust in the pipeline.

**Detection**: Intermittent `fatal error: generated_config.h: No such file or directory` — only on high parallelism (`-j32`), not on `-j1`.

**Solution**: Fixed the CMake dependency declaration:
```cmake
add_custom_command(
    OUTPUT ${CMAKE_CURRENT_BINARY_DIR}/generated_config.h
    COMMAND ${CMAKE_COMMAND} -E env python3 ${CMAKE_SOURCE_DIR}/scripts/generate_config.py
    DEPENDS ${CMAKE_SOURCE_DIR}/scripts/generate_config.py
    COMMENT "Generating config header"
)

add_library(config SHARED)
target_sources(config PRIVATE
    src/config.cpp
    ${CMAKE_CURRENT_BINARY_DIR}/generated_config.h
)
# CMake now knows config.cpp depends on the generated header
```

**Prevention**: Always use `add_custom_command` with proper `OUTPUT`/`DEPENDS` declarations. Never assume build order in parallel builds. Test with high parallelism (`-j$(nproc)`) in CI. Use Ninja (better dependency tracking than Make).

## Production Checklist

- [ ] Use CMake 3.16+ with modern target-based approach
- [ ] Set C++ standard explicitly (`CMAKE_CXX_STANDARD`)
- [ ] Enable compiler warnings (`-Wall -Wextra -Wpedantic -Werror`)
- [ ] Use `cmake_minimum_required` to enforce minimum CMake version
- [ ] Use vcpkg or Conan for dependency management (not system packages)
- [ ] Pin all dependency versions in manifest files
- [ ] Test with multiple compilers in CI (GCC, Clang, MSVC)
- [ ] Use Ninja generator for faster builds
- [ ] Set `CMAKE_EXPORT_COMPILE_COMMANDS=ON` for IDE integration
- [ ] Use CMake Presets for consistent build configurations
- [ ] Validate build type in CI (prevent Debug in production)
- [ ] Add `install()` rules for library distribution

## Maturity Levels

| Level | Capabilities |
|-------|-------------|
| **Beginner** | Basic CMakeLists.txt, simple Makefile, `cmake --build` |
| **Intermediate** | CMake targets, vcpkg/Conan, CI integration, presets |
| **Advanced** | Cross-compilation, custom toolchains, Bazel, monorepo management |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Makefiles are always better" | Makefiles don't scale. CMake handles cross-platform, dependencies, and IDEs. |
| "CMake is too complex" | Modern CMake (target-based) is clean. Legacy CMake was complex. Learn the modern way. |
| "Build systems don't matter" | Build systems determine compilation speed, cross-platform support, and developer productivity. |
| "System packages are fine for dependencies" | System packages have version conflicts, missing packages, and platform differences. Use vcpkg/Conan. |
| "Debug builds are fine for testing" | Debug builds have different behavior than Release (assertions, optimizations). Always test both. |

## One-Minute Revision Table

| Tool/Concept | Purpose | Key Detail |
|--------------|---------|------------|
| CMake | Cross-platform build system | Target-based approach, find_package |
| Make | Build automation (Unix) | Rule-based, not cross-platform by default |
| vcpkg | Package management (Microsoft) | Manifest mode, toolchain integration |
| Conan | Package management (Cross-platform) | Profiles, generators, remote repositories |
| Ninja | Fast build system | Used as CMake generator for speed |
| CMake Presets | Consistent build configurations | JSON-based, shareable across team |
| LTO | Link-Time Optimization | Cross-module optimization |
| CTest | CMake test runner | Integrated with CMake build system |
| Compile commands | IDE integration | `CMAKE_EXPORT_COMPILE_COMMANDS=ON` |

## Cross-Linked Related Topics

- **Testing** → [Module 10: Testing](../10-testing/) — CTest integration, test targets in CMake
- **Performance** → [Module 11: Performance](../11-performance/) — Compiler flags, LTO, PGO configuration
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Build configuration as best practice
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — C++ standard selection, feature detection
- **Networking** → [Module 12: Networking](../12-networking/) — Linking libcurl, Boost.Asio
- **Senior Level** → [Module 15: Senior](../15-senior/) — Build system architecture decisions

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Missing header only failing on CI (different compiler version) | CMake version checks + CI matrix | Add `if(CMAKE_CXX_COMPILER_VERSION VERSION_LESS "10.0") message(FATAL_ERROR ...)`; test with multiple compilers |
| Debug build deployed to production | Build type validation in CI | Add CI step that checks for debug symbols: `readelf -S binary \| grep debug`; assert `RelWithDebInfo` or `Release` |
| Dependency version conflict between packages | Package manager pinning (vcpkg/Conan) | Pin exact versions in `vcpkg.json` or `conanfile.py`; use lock files |
| Slow build times from unnecessary recompilation | Ninja generator + `ccache` | Use `-G Ninja` for faster builds; install `ccache` and set `CMAKE_CXX_COMPILER_LAUNCHER=ccache` |
| `install()` rules missing causing broken packaging | Manual packaging test | Run `cmake --install build --prefix /tmp/test-install`; verify all targets installed correctly |

## Code Review Checklist

- [ ] CMake minimum version and C++ standard explicitly set
- [ ] All compiler warnings enabled (`-Wall -Wextra -Wpedantic -Werror`)
- [ ] Dependencies managed via vcpkg or Conan (not system packages)
- [ ] All dependency versions pinned in manifest files
- [ ] Tests run in CI on every commit
- [ ] Build type validated in CI (no Debug in production)
- [ ] `install()` rules defined for library distribution

## Architecture Considerations

The build system is the foundation of every software project. It determines compilation speed, cross-platform support, dependency management, CI/CD integration, and developer productivity. Modern CMake with target-based design enables modular, reusable build configurations. Package managers (vcpkg, Conan) ensure reproducible builds across developer machines and CI. Build system architecture must balance fast incremental builds with correct dependency tracking.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| CMake target-based design | Modular, reusable build configurations | Clean dependency graph vs. steeper learning curve than legacy CMake |
| vcpkg manifest mode | Reproducible dependency management | Pinned versions vs. slower initial setup |
| CMake Presets | Consistent build configurations across team | Shared JSON configs vs. limited customization |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Dependency supply chain attack (malicious package) | Code execution, data theft | Pin dependency versions; use private package feeds; audit dependencies |
| Debug symbols in production binary | Information leakage, reverse engineering | Validate build type in CI; strip debug symbols in release |
| Missing compiler security flags | Exploitable binary (no stack protector, no PIE) | Add `-fstack-protector-strong -D_FORTIFY_SOURCE=2 -fPIE` to CMake |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| CMake 3.16 | Target-based approach, `target_link_libraries` | Replace `include_directories` with `target_include_directories`; use `target_link_libraries` for dependencies |
| CMake 3.19+ | CMake Presets for consistent configurations | Replace ad-hoc build scripts with `CMakePresets.json` |
| C++20 modules | Replace headers with importable modules | Migrate `#include` to `import` for faster compilation and cleaner dependency management |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| CMake 3.16+ target-based approach | N/A (build tool) | Widely supported |
| vcpkg manifest mode | N/A (package manager) | Widely supported |
| CMake Presets (JSON) | N/A (CMake 3.19+) | Supported in CMake 3.19+ |
| C++20 modules | C++20 | Supported in MSVC 19.28+, Clang 14+, GCC 14+ (experimental) |

## Interview Questions

1. **Why use CMake instead of raw Makefiles?**: CMake is cross-platform (generates Makefiles, Ninja, VS solutions), handles dependencies via `find_package`, integrates with IDEs, and provides a declarative build model. Makefiles are platform-specific and don't scale to complex projects.
2. **What is the difference between `target_link_libraries` and `include_directories`?**: `target_link_libraries` propagates include directories, compile definitions, and dependencies transitively. `include_directories` adds global include paths (non-modern). Use `target_link_libraries` for proper dependency management.
3. **Why pin dependency versions?**: Unpinned dependencies may change between builds, causing "works on my machine" failures. Pinning ensures reproducible builds across all developer machines and CI. Use lock files for deterministic resolution.
4. **What is LTO (Link-Time Optimization) and when should you use it?**: LTO enables cross-module optimization during linking — the compiler can inline across TU boundaries, eliminate dead code, and optimize indirect calls. Use it for release builds; it increases link time but improves runtime performance.
5. **How do you set up cross-compilation in CMake?**: Use a toolchain file (`-DCMAKE_TOOLCHAIN_FILE=toolchain.cmake`) that sets `CMAKE_SYSTEM_NAME`, `CMAKE_C_COMPILER`, and `CMAKE_CXX_COMPILER`. CMake uses these to generate the correct build system for the target platform.

6. **What is the difference between `add_library(STATIC)` and `add_library(SHARED)`?**: Static libraries are archived into a single `.a`/`.lib` file and copied into the final executable at link time. Shared libraries (`.so`/`.dylib`/`.dll`) are loaded at runtime. Static = larger binary, no runtime dependency. Shared = smaller binary, requires library at runtime.

7. **How does ccache improve build performance?**: ccache caches compiler output keyed by source file, compiler, and flags. On subsequent builds with the same inputs, it returns the cached `.o` file instead of recompiling. Typical speedup: 5-10x for clean builds, near-instant for unchanged files. Setup: `cmake -DCMAKE_CXX_COMPILER_LAUNCHER=ccache`.

8. **What is a CMake Preset and why use it?**: CMake Presets (`CMakePresets.json`) define named build configurations (generator, flags, build directory). They replace shell scripts and ad-hoc cmake invocations. Benefits: team-wide consistency, version-controlled build configs, CI/dev parity. Use `cmake --preset <name>`.

9. **When would you choose Bazel over CMake?**: Bazel for monorepos with thousands of targets, strict hermetic builds, and remote caching/execution. CMake for most C++ projects (industry standard, better library ecosystem, easier learning curve). Bazel requires BUILD files everywhere; CMake uses centralized `CMakeLists.txt`.

10. **How do you handle transitive dependencies in CMake?**: Use `target_link_libraries(mylib PUBLIC dep)` — PUBLIC propagates include dirs, compile defs, and link dependencies to consumers. PRIVATE hides them. INTERFACE exposes to consumers but not to `mylib` itself. Use `find_package` and target-based commands, not global `include_directories`.

11. **What is the purpose of `CMAKE_EXPORT_COMPILE_COMMANDS=ON`?**: Generates `compile_commands.json` — a JSON file listing every compilation command. Used by clangd, CMake-based IDEs, and static analysis tools (clang-tidy). Enables accurate code navigation and linting without a full build. Place in build directory.

12. **How do you prevent Debug builds from reaching production?**: CI validation step: check the binary for debug symbols (`readelf -S binary | grep debug` or `file binary`). Deployment scripts must pass `--config Release`. CMakeLists.txt should not default to Debug. Use presets to enforce correct build types.

13. **What is the difference between `find_package` modes (Module vs Config)?**: Module mode uses `FindXxx.cmake` scripts (CMake provides many). Config mode uses `XxxConfig.cmake` files installed by the library (or package manager). Config mode is preferred for modern CMake — more reliable, generated by the library vendor. vcpkg/Conan provide config files.

14. **How do you version a CMake library for distribution?**: Use `project(MyLib VERSION 1.2.3)`. Pass version to `install(TARGETS ... EXPORT ...)`. Generate `*ConfigVersion.cmake` with `write_basic_package_version_file()`. Consumers use `find_package(MyLib 1.2)` with version constraints.

15. **What are the trade-offs between static and dynamic linking?**: Static: self-contained binary, no runtime dependency, larger binary, slower link time, no shared code between processes. Dynamic: smaller binary, shared code, faster link time, requires library at runtime, potential ABI/version issues. Use static for distribution; dynamic for OS integration and shared libraries.

## References

- [Modern CMake — Anastasia Kazakova](https://www.amazon.com/Modern-CMake-Projects-Cookbook-techniques/dp/1800208111)
- [CMake Documentation](https://cmake.org/cmake/help/latest/)
- [vcpkg Documentation](https://github.com/microsoft/vcpkg#documentation)
- [Professional CMake: A Practical Guide — Craig Scott](https://www.amazon.com/Professional-CMake-Practical-Guide-Scott/dp=1974403006)

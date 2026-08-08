# Build Systems — C++

## Why It Matters

A build system is the foundation of every software project. When it's well-configured, it enables cross-platform development, dependency management, CI/CD integration, and reproducible builds. A broken build system means wasted hours debugging compilation issues instead of writing code, especially when "it works on my machine" but fails on CI.

## What It Is

Build systems in C++ include CMake, Make, and package managers like vcpkg and Conan, providing declarative configurations for compiling, linking, testing, and distributing code across multiple platforms.

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

## References

- [Modern CMake — Anastasia Kazakova](https://www.amazon.com/Modern-CMake-Projects-Cookbook-techniques/dp/1800208111)
- [CMake Documentation](https://cmake.org/cmake/help/latest/)
- [vcpkg Documentation](https://github.com/microsoft/vcpkg#documentation)
- [Professional CMake: A Practical Guide — Craig Scott](https://www.amazon.com/Professional-CMake-Practical-Guide-Scott/dp=1974403006)

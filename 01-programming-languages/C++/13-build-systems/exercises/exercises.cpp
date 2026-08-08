// Module 13: Build Systems — Exercises
// Complete each exercise by filling in the TODO sections.
// This module focuses on CMake and build configuration concepts.

#include <iostream>
#include <string>
#include <vector>
#include <cassert>
#include <fstream>
#include <sstream>

// ============================================================================
// Exercise 1: CMakeLists.txt Generator
// Generate a basic CMakeLists.txt for a simple C++ project.
// ============================================================================

// TODO: Implement a function that generates a CMakeLists.txt string
// for a project with the given name and source files
std::string generate_cmake_project(
    const std::string& project_name,
    const std::vector<std::string>& source_files,
    const std::string& cxx_standard = "17"
) {
    std::ostringstream oss;
    // Your code here
    // Generate:
    // cmake_minimum_required(VERSION 3.14)
    // project(project_name)
    // set(CMAKE_CXX_STANDARD XX)
    // add_executable(name source1 source2 ...)
    return oss.str();
}

void exercise1() {
    std::cout << "\n=== Exercise 1: CMakeLists.txt Generator ===\n";

    auto cmake = generate_cmake_project("MyApp", {"main.cpp", "utils.cpp"}, "20");
    assert(cmake.find("cmake_minimum_required") != std::string::npos);
    assert(cmake.find("project(MyApp)") != std::string::npos);
    assert(cmake.find("CMAKE_CXX_STANDARD 20") != std::string::npos);
    assert(cmake.find("main.cpp") != std::string::npos);
    std::cout << "Generated CMakeLists.txt:\n" << cmake << "\n";

    std::cout << "Exercise 1: OK\n";
}

// ============================================================================
// Exercise 2: Build Configuration Analyzer
// Parse and analyze build configuration settings.
// ============================================================================

struct BuildConfig {
    std::string build_type;   // Debug, Release, RelWithDebInfo
    bool enable_warnings;
    bool enable_sanitizers;
    bool enable_optimizations;
    int optimization_level;   // 0, 1, 2, 3

    // TODO: Create a BuildConfig from a string like "Debug"
    static BuildConfig from_string(const std::string& type) {
        BuildConfig config;
        // Your code here
        return config;
    }

    // TODO: Generate compiler flags as a string
    std::string get_compiler_flags() const {
        std::ostringstream oss;
        // Your code here
        // Debug: -g -O0 -fsanitize=address
        // Release: -O2 -DNDEBUG
        return oss.str();
    }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: Build Config Analyzer ===\n";

    auto debug = BuildConfig::from_string("Debug");
    assert(debug.build_type == "Debug");
    assert(debug.enable_optimizations == false);

    auto release = BuildConfig::from_string("Release");
    assert(release.build_type == "Release");
    assert(release.enable_optimizations == true);

    std::cout << "Debug flags: " << debug.get_compiler_flags() << "\n";
    std::cout << "Release flags: " << release.get_compiler_flags() << "\n";

    std::cout << "Exercise 2: OK\n";
}

// ============================================================================
// Exercise 3: Dependency Tracker
// Track dependencies between source files and headers.
// ============================================================================

class DependencyTracker {
    std::map<std::string, std::vector<std::string>> dependencies_;

public:
    // TODO: Add a dependency (source depends on header)
    void add_dependency(const std::string& source, const std::string& header) {
        // Your code here
    }

    // TODO: Get all dependencies for a source file
    std::vector<std::string> get_dependencies(const std::string& source) const {
        // Your code here
        return {};
    }

    // TODO: Check if there are circular dependencies
    bool has_circular() const {
        // Your code here
        return false;
    }

    // TODO: Get the build order (topological sort)
    std::vector<std::string> get_build_order() const {
        // Your code here
        return {};
    }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Dependency Tracker ===\n";

    DependencyTracker tracker;
    tracker.add_dependency("main.cpp", "utils.h");
    tracker.add_dependency("main.cpp", "config.h");
    tracker.add_dependency("utils.cpp", "utils.h");

    auto deps = tracker.get_dependencies("main.cpp");
    assert(deps.size() == 2);
    std::cout << "main.cpp depends on: ";
    for (const auto& d : deps) std::cout << d << " ";
    std::cout << "\n";

    auto order = tracker.get_build_order();
    assert(!order.empty());
    std::cout << "Build order: ";
    for (const auto& f : order) std::cout << f << " ";
    std::cout << "\n";

    std::cout << "Exercise 3: OK\n";
}

// ============================================================================
// Exercise 4: Version Manager
// Manage semantic version numbers for a project.
// ============================================================================

struct SemanticVersion {
    int major;
    int minor;
    int patch;

    // TODO: Parse from string "1.2.3"
    static SemanticVersion parse(const std::string& version_str) {
        SemanticVersion v{0, 0, 0};
        // Your code here
        return v;
    }

    // TODO: Convert to string
    std::string to_string() const {
        // Your code here
        return "";
    }

    // TODO: Compare versions (-1, 0, 1)
    int compare(const SemanticVersion& other) const {
        // Your code here
        return 0;
    }

    // TODO: Bump major version (1.2.3 -> 2.0.0)
    void bump_major() {
        // Your code here
    }

    // TODO: Bump minor version (1.2.3 -> 1.3.0)
    void bump_minor() {
        // Your code here
    }

    // TODO: Bump patch version (1.2.3 -> 1.2.4)
    void bump_patch() {
        // Your code here
    }
};

void exercise4() {
    std::cout << "\n=== Exercise 4: Version Manager ===\n";

    auto v1 = SemanticVersion::parse("1.2.3");
    assert(v1.major == 1 && v1.minor == 2 && v1.patch == 3);
    assert(v1.to_string() == "1.2.3");

    auto v2 = SemanticVersion::parse("1.2.4");
    assert(v1.compare(v2) == -1);
    assert(v2.compare(v1) == 1);

    auto v3 = SemanticVersion::parse("1.2.3");
    assert(v1.compare(v3) == 0);

    v1.bump_patch();
    assert(v1.to_string() == "1.2.4");

    v1.bump_minor();
    assert(v1.to_string() == "1.3.0");

    v1.bump_major();
    assert(v1.to_string() == "2.0.0");

    std::cout << "Exercise 4: OK\n";
}

// ============================================================================
// Exercise 5: Build Script Generator
// Generate build scripts for different platforms.
// ============================================================================

// TODO: Generate a shell script for building on Linux/Mac
std::string generate_build_script(const std::string& project_name, const std::string& build_type) {
    std::ostringstream oss;
    // Your code here
    // Generate:
    // #!/bin/bash
    // mkdir -p build
    // cd build
    // cmake -DCMAKE_BUILD_TYPE=XXX ..
    // make -j$(nproc)
    return oss.str();
}

// TODO: Generate a batch script for building on Windows
std::string generate_windows_script(const std::string& project_name, const std::string& build_type) {
    std::ostringstream oss;
    // Your code here
    // Generate:
    // @echo off
    // mkdir build 2>nul
    // cd build
    // cmake -G "Visual Studio 17 2022" -A x64 ..
    // cmake --build . --config XXX
    return oss.str();
}

void exercise5() {
    std::cout << "\n=== Exercise 5: Build Script Generator ===\n";

    auto linux_script = generate_build_script("MyApp", "Release");
    assert(linux_script.find("#!/bin/bash") != std::string::npos);
    assert(linux_script.find("cmake") != std::string::npos);
    assert(linux_script.find("make") != std::string::npos);
    std::cout << "Linux script:\n" << linux_script << "\n";

    auto windows_script = generate_windows_script("MyApp", "Debug");
    assert(windows_script.find("cmake") != std::string::npos);
    std::cout << "Windows script:\n" << windows_script << "\n";

    std::cout << "Exercise 5: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 13: Build Systems Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}

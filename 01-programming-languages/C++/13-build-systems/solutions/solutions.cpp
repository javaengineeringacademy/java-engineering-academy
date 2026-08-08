// Module 13: Build Systems — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <cassert>
#include <sstream>
#include <map>
#include <algorithm>

// ============================================================================
// Exercise 1 Solution: CMakeLists.txt Generator
// ============================================================================

std::string generate_cmake_project(
    const std::string& project_name,
    const std::vector<std::string>& source_files,
    const std::string& cxx_standard
) {
    std::ostringstream oss;
    oss << "cmake_minimum_required(VERSION 3.14)\n";
    oss << "project(" << project_name << ")\n\n";
    oss << "set(CMAKE_CXX_STANDARD " << cxx_standard << ")\n";
    oss << "set(CMAKE_CXX_STANDARD_REQUIRED ON)\n\n";
    oss << "add_executable(" << project_name;
    for (const auto& file : source_files) {
        oss << " " << file;
    }
    oss << ")\n";
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
}

// ============================================================================
// Exercise 2 Solution: Build Configuration Analyzer
// ============================================================================

struct BuildConfig {
    std::string build_type;
    bool enable_warnings;
    bool enable_sanitizers;
    bool enable_optimizations;
    int optimization_level;

    static BuildConfig from_string(const std::string& type) {
        BuildConfig config;
        config.build_type = type;
        config.enable_warnings = true;

        if (type == "Debug") {
            config.enable_sanitizers = true;
            config.enable_optimizations = false;
            config.optimization_level = 0;
        } else if (type == "Release") {
            config.enable_sanitizers = false;
            config.enable_optimizations = true;
            config.optimization_level = 2;
        } else {  // RelWithDebInfo
            config.enable_sanitizers = false;
            config.enable_optimizations = true;
            config.optimization_level = 1;
        }
        return config;
    }

    std::string get_compiler_flags() const {
        std::ostringstream oss;
        if (build_type == "Debug") {
            oss << "-g -O0";
            if (enable_sanitizers) oss << " -fsanitize=address";
        } else if (build_type == "Release") {
            oss << "-O" << optimization_level << " -DNDEBUG";
        } else {
            oss << "-O" << optimization_level << " -g";
        }
        if (enable_warnings) oss << " -Wall -Wextra";
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
}

// ============================================================================
// Exercise 3 Solution: Dependency Tracker
// ============================================================================

class DependencyTracker {
    std::map<std::string, std::vector<std::string>> dependencies_;

public:
    void add_dependency(const std::string& source, const std::string& header) {
        dependencies_[source].push_back(header);
    }

    std::vector<std::string> get_dependencies(const std::string& source) const {
        auto it = dependencies_.find(source);
        if (it != dependencies_.end()) return it->second;
        return {};
    }

    bool has_circular() const {
        // Simple cycle detection using DFS
        std::map<std::string, int> visited; // 0=unvisited, 1=visiting, 2=done
        for (const auto& [file, _] : dependencies_) {
            visited[file] = 0;
        }

        std::function<bool(const std::string&)> dfs = [&](const std::string& node) {
            visited[node] = 1;
            for (const auto& dep : get_dependencies(node)) {
                if (visited.count(dep)) {
                    if (visited[dep] == 1) return true;
                    if (visited[dep] == 0 && dfs(dep)) return true;
                }
            }
            visited[node] = 2;
            return false;
        };

        for (const auto& [file, _] : dependencies_) {
            if (visited[file] == 0 && dfs(file)) return true;
        }
        return false;
    }

    std::vector<std::string> get_build_order() const {
        std::vector<std::string> order;
        std::map<std::string, bool> visited;

        std::function<void(const std::string&)> visit = [&](const std::string& node) {
            if (visited[node]) return;
            visited[node] = true;
            for (const auto& dep : get_dependencies(node)) {
                visit(dep);
            }
            order.push_back(node);
        };

        for (const auto& [file, _] : dependencies_) {
            visit(file);
        }
        return order;
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

    assert(!tracker.has_circular());
}

// ============================================================================
// Exercise 4 Solution: Version Manager
// ============================================================================

struct SemanticVersion {
    int major;
    int minor;
    int patch;

    static SemanticVersion parse(const std::string& version_str) {
        SemanticVersion v{0, 0, 0};
        std::istringstream iss(version_str);
        char dot;
        iss >> v.major >> dot >> v.minor >> dot >> v.patch;
        return v;
    }

    std::string to_string() const {
        return std::to_string(major) + "." + std::to_string(minor) + "." + std::to_string(patch);
    }

    int compare(const SemanticVersion& other) const {
        if (major != other.major) return (major < other.major) ? -1 : 1;
        if (minor != other.minor) return (minor < other.minor) ? -1 : 1;
        if (patch != other.patch) return (patch < other.patch) ? -1 : 1;
        return 0;
    }

    void bump_major() { major++; minor = 0; patch = 0; }
    void bump_minor() { minor++; patch = 0; }
    void bump_patch() { patch++; }
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
}

// ============================================================================
// Exercise 5 Solution: Build Script Generator
// ============================================================================

std::string generate_build_script(const std::string& project_name, const std::string& build_type) {
    std::ostringstream oss;
    oss << "#!/bin/bash\n";
    oss << "set -e\n\n";
    oss << "echo \"Building " << project_name << " (" << build_type << ")...\"\n";
    oss << "mkdir -p build\n";
    oss << "cd build\n";
    oss << "cmake -DCMAKE_BUILD_TYPE=" << build_type << " ..\n";
    oss << "make -j$(nproc)\n";
    oss << "echo \"Build complete!\"\n";
    return oss.str();
}

std::string generate_windows_script(const std::string& project_name, const std::string& build_type) {
    std::ostringstream oss;
    oss << "@echo off\n";
    oss << "echo Building " << project_name << " (" << build_type << ")...\n";
    oss << "mkdir build 2>nul\n";
    oss << "cd build\n";
    oss << "cmake -G \"Visual Studio 17 2022\" -A x64 ..\n";
    oss << "cmake --build . --config " << build_type << "\n";
    oss << "echo Build complete!\n";
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
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 13: Build Systems Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}

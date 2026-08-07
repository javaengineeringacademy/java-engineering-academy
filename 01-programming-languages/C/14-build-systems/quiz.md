# Build Systems Quiz

## Questions

1. What is a Makefile?
2. What are phony targets?
3. What is CMake?
4. What is pkg-config?
5. What are compiler flags?
6. What is the difference between -O0 and -O2?
7. What is a clean target?
8. What are dependencies in Make?
9. What is cross-compilation?
10. Why use build systems?
11. What is the difference between `make` and `cmake`?
12. What is a `compile_commands.json` file used for?
13. What are CFLAGS and LDFLAGS?
14. What is incremental compilation and how does Make support it?
15. What is a configure script?

## Answers

1. A file defining build rules for make
2. Targets that don't create files (e.g., clean)
3. Cross-platform build system generator
4. Tool to find compiler/linker flags for libraries
5. Options passed to compiler (-Wall, -O2, etc.)
6. -O0: no optimization; -O2: level 2 optimization
7. Target to remove generated files
8. Files that must exist before building
9. Compiling for a different platform/architecture
10. Automate builds and ensure consistency
11. `make` is a build tool executing Makefile rules; `cmake` generates Makefiles (or Ninja/VS files) from CMakeLists.txt
12. A JSON compilation database listing compiler commands for each source file; used by IDEs and clangd for code completion and analysis
13. CFLAGS: compiler options; LDFLAGS: linker options (e.g., `-L/path -lmylib`)
14. Only recompiles files whose dependencies changed; Make tracks timestamps to determine what needs rebuilding
15. A shell script that detects system capabilities and generates Makefiles; used by autotools (autoconf) for portability

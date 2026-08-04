# Go Programming Language

Go (also known as Golang) is a statically-typed, compiled programming language designed at Google. It is known for its simplicity, efficiency, and excellent support for concurrent programming.

## Table of Contents

- [Fundamentals](#fundamentals)
- [Advanced Concepts](#advanced-concepts)
- [Collections](#collections)
- [Concurrency](#concurrency)
- [Memory Management](#memory-management)
- [Internals](#internals)
- [Performance](#performance)
- [Best Practices](#best-practices)
- [Projects](#projects)
- [Interview Questions](#interview-questions)

## Key Features

- **Simple Syntax**: Clean and easy to learn
- **Fast Compilation**: Quick build times
- **Concurrency**: Built-in goroutines and channels
- **Memory Safety**: Garbage collection and pointer safety
- **Standard Library**: Rich standard library with built-in packages
- **Cross-Platform**: Build for multiple platforms from single codebase

## Getting Started

### Installation

```bash
# Download from https://golang.org/dl/
# Or using package managers:
brew install go        # macOS
sudo apt install golang # Ubuntu/Debian
```

### Hello World

```go
package main

import "fmt"

func main() {
    fmt.Println("Hello, World!")
}
```

### Running Go

```bash
# Run directly
go run main.go

# Build and run
go build -o myapp
./myapp

# Initialize module
go mod init myproject
```

## Learning Path

1. **Start with Fundamentals**: Variables, types, functions, structs
2. **Master Control Flow**: If, switch, for loops
3. **Learn Concurrency**: Goroutines, channels, sync package
4. **Explore Advanced Topics**: Interfaces, reflection, unsafe
5. **Apply Best Practices**: Error handling, project structure, testing

## Resources

- [Go Documentation](https://golang.org/doc/)
- [Go Tour](https://tour.golang.org/)
- [Go Playground](https://play.golang.org/)
- [Go Wiki](https://github.com/golang/go/wiki)

---

## Detailed Topics

### [Fundamentals](fundamentals/README.md)
Core concepts including variables, types, functions, structs, interfaces, and control flow.

### [Advanced Concepts](advanced/README.md)
Goroutines, channels, select, context, reflection, unsafe, cgo, and build tags.

### [Collections](collections/README.md)
Arrays, slices, maps, containers, sort, and sync.Map.

### [Concurrency](concurrency/README.md)
Goroutines, channels, sync package, context, worker pools, and fan-out/fan-in.

### [Memory Management](memory-management/README.md)
Stack vs heap, escape analysis, GC, memory profiling, and pprof.

### [Internals](internals/README.md)
Go runtime, scheduler, garbage collector, compiler, and linker.

### [Performance](performance/README.md)
Benchmarks, profiling, memory optimization, and concurrency patterns.

### [Best Practices](best-practices/README.md)
Error handling, project structure, testing, documentation, and idiomatic Go.

### [Projects](projects/README.md)
Project ideas to practice and apply Go knowledge.

### [Interview Questions](interview-questions/README.md)
Common Go interview questions and detailed answers.

---

## Quick Reference

```go
// Variables
var name string = "Go"
age := 25  // Short declaration

// Functions
func add(a, b int) int {
    return a + b
}

// Struct
type Person struct {
    Name string
    Age  int
}

// Goroutine
go func() {
    fmt.Println("Hello from goroutine")
}()

// Channel
ch := make(chan int)
go func() { ch <- 42 }()
value := <-ch
```

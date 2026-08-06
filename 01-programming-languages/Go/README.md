# Go (Golang) Programming

Go is an open-source programming language developed at Google. It's designed for simplicity, reliability, and efficiency.

## Key Features
- **Statically typed** with type inference
- **Garbage collected** memory management
- **Concurrency** built-in with goroutines and channels
- **Fast compilation** and execution
- **Rich standard library**

## Module Structure

| Module | Description |
|--------|-------------|
| 01-fundamentals | Variables, operators, control flow, functions, structs, interfaces, strings |
| 02-concurrency | Goroutines, channels, select, sync primitives |
| 03-advanced | Error handling, pointers, slices/maps, defer/panic, generics |
| 04-testing | Unit testing and benchmarks |
| reference | Anti-patterns, best practices, patterns, comparisons |

## Quick Start

```bash
# Create module
go mod init myproject

# Run a file
go run main.go

# Build
go build -o myapp main.go

# Test
go test ./...

# Format code
gofmt -w .
```

## Learn More
- [Go Tour](https://go.dev/tour/)
- [Go Docs](https://go.dev/doc/)
- [Effective Go](https://go.dev/doc/effective_go)

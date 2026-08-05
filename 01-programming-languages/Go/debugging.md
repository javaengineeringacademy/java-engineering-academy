# Go Debugging

## Delve Debugger

```bash
# Install
go install github.com/go-delve/delve/cmd/dlv@latest

# Debug a program
dlv debug main.go

# Debug tests
dlv test ./...

# Debug running process
dlv attach <pid>

# Listen for remote connections
dlv debug --headless --listen=:2345 --api-version=2
```

Common Delve commands:

- `break` or `b`: Set breakpoint
- `continue` or `c`: Continue execution
- `next` or `n`: Step over
- `step` or `s`: Step into
- `stepout`: Step out of function
- `print` or `p`: Print variable
- `goroutines`: List goroutines
- `goroutine`: Switch goroutine
- `stack`: Print stack trace
- `locals`: Print local variables

## Race Detector

```bash
# Run with race detector
go run -race main.go
go test -race ./...
go build -race -o binary main.go
```

Detects data races at runtime. Uses ThreadSanitizer to monitor memory access patterns. Add `-race` flag to all go commands during development.

## pprof Analysis

```go
import _ "net/http/pprof"

go func() {
    log.Println(http.ListenAndServe("localhost:6060", nil))
}()
```

```bash
# CPU profile
go tool pprof http://localhost:6060/debug/pprof/profile?seconds=30

# Memory profile
go tool pprof http://localhost:6060/debug/pprof/heap

# Goroutine dump
go tool pprof http://localhost:6060/debug/pprof/goroutine

# Interactive analysis
(pprof) top
(pprof) web
(pprof) list function
```

## Console Debugging

```go
fmt.Printf("Variable: %+v\n", variable)
fmt.Printf("Type: %T\n", variable)
fmt.Printf("Struct: %#v\n", struct)

// Debug logging
log.Printf("DEBUG: %s = %v", name, value)
```

## Trace Analysis

```go
import "runtime/trace"

f, _ := os.Create("trace.out")
trace.Start(f)
defer trace.Stop()
```

```bash
go tool trace trace.out
```

## Core Dumps

```bash
# Enable core dumps
ulimit -c unlimited

# Analyze core dump
dlv core <binary> <core>
```

## Common Debug Patterns

- Add `-v` flag to test output
- Use `t.Log()` and `t.Logf()` in tests
- Use `-run` flag to run specific tests
- Use build tags for debug code
- Log goroutine IDs for debugging concurrency

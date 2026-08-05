# Go Performance

## Profiling with pprof

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

# Goroutine profile
go tool pprof http://localhost:6060/debug/pprof/goroutine

# Web UI
go tool pprof -http=:8080 profile.pb.gz
```

## Benchmarking

```go
func BenchmarkFunction(b *testing.B) {
    for i := 0; i < b.N; i++ {
        Function()
    }
}
```

```bash
go test -bench=. -benchmem -count=5
go test -bench=BenchmarkFunction -benchmem -cpu=1,2,4
go test -bench=. -benchtime=10s
go test -bench=. -benchmem -cpuprofile cpu.out -memprofile mem.out
```

## Escape Analysis

Determine if variables escape to heap:

```bash
go build -gcflags="-m" ./...
go build -gcflags="-m -m" ./...  # Detailed
```

Common escape triggers:

- Returning pointer to local variable
- Interface assignment
- Closure captures variable
- Channel send/receive with pointer
- Slice/map append with pointer

## Inlining

Functions are inlined when simple enough:

```bash
# View inlining decisions
go build -gcflags="-m" ./...

# Disable inlining
go build -gcflags="-l" ./...

# Set inline threshold
go build -gcflags="-l -l" ./...
```

## Memory Optimization

- Use `sync.Pool` for frequently allocated objects
- Pre-allocate slices with `make([]T, 0, capacity)`
- Reuse buffers with `bytes.Buffer`
- Avoid string concatenation in loops
- Use `strings.Builder` for string building
- Pool database connections with `database/sql`

## Concurrency Optimization

- Use worker pools for bounded goroutines
- Limit goroutine count with semaphores
- Use buffered channels for batching
- Batch database operations
- Use `sync.WaitGroup` for coordination

## Garbage Collector Tuning

- `GOGC`: Target percentage for GC trigger (default 100)
- `GOMEMLIMIT`: Soft memory limit
- `GOMAXPROCS`: Number of CPUs to use
- Reduce allocations to lower GC pressure
- Use `runtime.KeepAlive` to prevent premature collection

## Compiler Optimizations

```bash
# View optimizations
go build -gcflags="-m" ./...

# Disable optimizations
go build -gcflags="-N -l" ./...

# Profile-guided optimization
go build -pgo=auto
```

## Common Bottlenecks

- Excessive allocations in hot paths
- Lock contention in goroutines
- Unnecessary interface conversions
- Deep recursion without tail call optimization
- Large struct copies
- String conversions in loops

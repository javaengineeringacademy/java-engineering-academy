# Benchmarks in Go

Go's testing package includes benchmark support for performance measurement.

## Benchmark Functions

```go
func BenchmarkSomething(b *testing.B) {
    for i := 0; i < b.N; i++ {
        // Code to benchmark
    }
}
```

## Run Benchmarks

```bash
go test -bench=.
go test -bench=BenchmarkName
go test -bench=. -benchmem
go test -bench=. -count=5
go test -bench=. -benchtime=3s
```

## Benchmark Output

```
BenchmarkConcat-8    5000000    320 ns/op    48 B/op    4 allocs/op
```

- `5000000`: iterations
- `320 ns/op`: nanoseconds per operation
- `48 B/op`: bytes allocated per operation
- `4 allocs/op`: allocations per operation

## Key Points
- Function must start with `Benchmark`
- Use `b.N` for iteration count
- Use `b.ResetTimer()` after setup
- Use `b.ReportAllocs()` for memory stats
- Use `b.RunParallel()` for parallel benchmarks
- Compare results with `benchstat`

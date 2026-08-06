package bench

import (
    "fmt"
    "strings"
    "testing"
)

// Benchmark string concatenation methods
func BenchmarkStringConcat(b *testing.B) {
    for i := 0; i < b.N; i++ {
        s := ""
        for j := 0; j < 100; j++ {
            s += "a"
        }
    }
}

func BenchmarkStringPlus(b *testing.B) {
    for i := 0; i < b.N; i++ {
        s := ""
        for j := 0; j < 100; j++ {
            s = s + "a"
        }
    }
}

func BenchmarkStringBuilder(b *testing.B) {
    for i := 0; i < b.N; i++ {
        var builder strings.Builder
        for j := 0; j < 100; j++ {
            builder.WriteString("a")
        }
        _ = builder.String()
    }
}

func BenchmarkSprintf(b *testing.B) {
    for i := 0; i < b.N; i++ {
        s := ""
        for j := 0; j < 100; j++ {
            s = fmt.Sprintf("%s%s", s, "a")
        }
    }
}

// Benchmark with setup
func BenchmarkWithSetup(b *testing.B) {
    // Setup (not measured)
    data := make([]int, 1000)
    for i := range data {
        data[i] = i
    }

    b.ResetTimer() // Reset after setup

    for i := 0; i < b.N; i++ {
        sum := 0
        for _, v := range data {
            sum += v
        }
    }
}

// Parallel benchmark
func BenchmarkParallel(b *testing.B) {
    b.RunParallel(func(pb *testing.PB) {
        for pb.Next() {
            // Parallel work
            _ = strings.Repeat("a", 100)
        }
    })
}

// Sub-benchmarks
func BenchmarkMath(b *testing.B) {
    b.Run("Add", func(b *testing.B) {
        for i := 0; i < b.N; i++ {
            _ = i + i
        }
    })

    b.Run("Multiply", func(b *testing.B) {
        for i := 0; i < b.N; i++ {
            _ = i * i
        }
    })

    b.Run("Divide", func(b *testing.B) {
        for i := 1; i < b.N; i++ {
            _ = i / i
        }
    })
}

// Memory allocation benchmark
func BenchmarkAllocations(b *testing.B) {
    b.ReportAllocs() // Report memory allocations

    for i := 0; i < b.N; i++ {
        s := make([]int, 100)
        _ = s
    }
}

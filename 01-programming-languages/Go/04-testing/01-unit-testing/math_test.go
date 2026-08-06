package math

import (
    "errors"
    "testing"
)

// Error variable used by Divide
var ErrDivisionByZero = errors.New("division by zero")

// Simple test
func TestAdd(t *testing.T) {
    result := Add(2, 3)
    if result != 5 {
        t.Errorf("Add(2, 3) = %d; want 5", result)
    }
}

// Table-driven tests
func TestSubtract(t *testing.T) {
    tests := []struct {
        name     string
        a, b     int
        expected int
    }{
        {"positive", 10, 3, 7},
        {"negative result", 3, 10, -7},
        {"zero", 5, 5, 0},
        {"negative numbers", -5, -3, -2},
    }

    for _, tt := range tests {
        t.Run(tt.name, func(t *testing.T) {
            result := Subtract(tt.a, tt.b)
            if result != tt.expected {
                t.Errorf("Subtract(%d, %d) = %d; want %d",
                    tt.a, tt.b, result, tt.expected)
            }
        })
    }
}

func TestMultiply(t *testing.T) {
    tests := []struct {
        a, b, want int
    }{
        {2, 3, 6},
        {-2, 3, -6},
        {0, 100, 0},
        {1, 1, 1},
    }

    for _, tt := range tests {
        if got := Multiply(tt.a, tt.b); got != tt.want {
            t.Errorf("Multiply(%d, %d) = %d; want %d",
                tt.a, tt.b, got, tt.want)
        }
    }
}

func TestDivide(t *testing.T) {
    tests := []struct {
        name    string
        a, b    int
        want    int
        wantErr bool
    }{
        {"normal", 10, 2, 5, false},
        {"zero divisor", 10, 0, 0, true},
        {"negative", -10, 2, -5, false},
    }

    for _, tt := range tests {
        t.Run(tt.name, func(t *testing.T) {
            got, err := Divide(tt.a, tt.b)
            if (err != nil) != tt.wantErr {
                t.Errorf("Divide(%d, %d) error = %v, wantErr %v",
                    tt.a, tt.b, err, tt.wantErr)
                return
            }
            if got != tt.want {
                t.Errorf("Divide(%d, %d) = %d; want %d",
                    tt.a, tt.b, got, tt.want)
            }
        })
    }
}

func TestMax(t *testing.T) {
    if Max(5, 3) != 5 {
        t.Error("Max(5, 3) should be 5")
    }
    if Max(3, 5) != 5 {
        t.Error("Max(3, 5) should be 5")
    }
    if Max(5, 5) != 5 {
        t.Error("Max(5, 5) should be 5")
    }
}

func TestMin(t *testing.T) {
    if Min(5, 3) != 3 {
        t.Error("Min(5, 3) should be 3")
    }
    if Min(3, 5) != 3 {
        t.Error("Min(3, 5) should be 3")
    }
}

func TestAbs(t *testing.T) {
    tests := []struct {
        input, want int
    }{
        {5, 5},
        {-5, 5},
        {0, 0},
    }
    for _, tt := range tests {
        if got := Abs(tt.input); got != tt.want {
            t.Errorf("Abs(%d) = %d; want %d", tt.input, got, tt.want)
        }
    }
}

func TestFactorial(t *testing.T) {
    tests := []struct {
        n, want int
    }{
        {0, 1},
        {1, 1},
        {5, 120},
        {10, 3628800},
        {-1, 0},
    }
    for _, tt := range tests {
        if got := Factorial(tt.n); got != tt.want {
            t.Errorf("Factorial(%d) = %d; want %d", tt.n, got, tt.want)
        }
    }
}

func TestFibonacci(t *testing.T) {
    expected := []int{0, 1, 1, 2, 3, 5, 8, 13, 21, 34}
    for i, want := range expected {
        if got := Fibonacci(i); got != want {
            t.Errorf("Fibonacci(%d) = %d; want %d", i, got, want)
        }
    }
}

// Benchmark
func BenchmarkAdd(b *testing.B) {
    for i := 0; i < b.N; i++ {
        Add(i, i+1)
    }
}

func BenchmarkFactorial(b *testing.B) {
    for i := 0; i < b.N; i++ {
        Factorial(20)
    }
}

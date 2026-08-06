package math

// Add returns the sum of two integers.
func Add(a, b int) int {
    return a + b
}

// Subtract returns the difference of two integers.
func Subtract(a, b int) int {
    return a - b
}

// Multiply returns the product of two integers.
func Multiply(a, b int) int {
    return a * b
}

// Divide returns the quotient and error if divisor is zero.
func Divide(a, b int) (int, error) {
    if b == 0 {
        return 0, ErrDivisionByZero
    }
    return a / b, nil
}

// Max returns the larger of two integers.
func Max(a, b int) int {
    if a > b {
        return a
    }
    return b
}

// Min returns the smaller of two integers.
func Min(a, b int) int {
    if a < b {
        return a
    }
    return b
}

// Abs returns the absolute value.
func Abs(n int) int {
    if n < 0 {
        return -n
    }
    return n
}

// Factorial returns n! (non-negative only).
func Factorial(n int) int {
    if n < 0 {
        return 0
    }
    if n == 0 {
        return 1
    }
    result := 1
    for i := 1; i <= n; i++ {
        result *= i
    }
    return result
}

// Fibonacci returns the nth Fibonacci number.
func Fibonacci(n int) int {
    if n <= 0 {
        return 0
    }
    if n == 1 {
        return 1
    }
    a, b := 0, 1
    for i := 2; i <= n; i++ {
        a, b = b, a+b
    }
    return b
}

package main

import (
    "errors"
    "fmt"
    "strings"
)

// Basic function
func add(a, b int) int {
    return a + b
}

// Multiple return values
func divide(a, b float64) (float64, error) {
    if b == 0 {
        return 0, errors.New("division by zero")
    }
    return a / b, nil
}

// Named return values
func swap(a, b int) (x, y int) {
    x = b
    y = a
    return // Naked return
}

// Variadic function
func sum(nums ...int) int {
    total := 0
    for _, n := range nums {
        total += n
    }
    return total
}

// Variadic with other parameters
func join(sep string, words ...string) string {
    return strings.Join(words, sep)
}

// Function as value
func apply(f func(int, int) int, a, b int) int {
    return f(a, b)
}

// Closure
func counter() func() int {
    count := 0
    return func() int {
        count++
        return count
    }
}

// Higher-order function
func filter(nums []int, predicate func(int) bool) []int {
    var result []int
    for _, n := range nums {
        if predicate(n) {
            result = append(result, n)
        }
    }
    return result
}

func main() {
    // Basic function
    fmt.Println("=== Basic Function ===")
    fmt.Printf("add(3, 4) = %d\n", add(3, 4))

    // Multiple returns
    fmt.Println("\n=== Multiple Returns ===")
    result, err := divide(10, 3)
    if err != nil {
        fmt.Println("Error:", err)
    } else {
        fmt.Printf("divide(10, 3) = %.2f\n", result)
    }

    // Named returns
    fmt.Println("\n=== Named Returns ===")
    x, y := swap(1, 2)
    fmt.Printf("swap(1, 2) = (%d, %d)\n", x, y)

    // Variadic
    fmt.Println("\n=== Variadic Functions ===")
    fmt.Printf("sum(1,2,3) = %d\n", sum(1, 2, 3))
    fmt.Printf("sum(1..5) = %d\n", sum(1, 2, 3, 4, 5))

    nums := []int{10, 20, 30}
    fmt.Printf("sum(slice) = %d\n", sum(nums...))

    fmt.Println(join("-", "Go", "is", "awesome"))

    // Function as value
    fmt.Println("\n=== Function as Value ===")
    fmt.Printf("apply(add, 5, 3) = %d\n", apply(add, 5, 3))

    mul := func(a, b int) int { return a * b }
    fmt.Printf("apply(mul, 5, 3) = %d\n", apply(mul, 5, 3))

    // Closure
    fmt.Println("\n=== Closure ===")
    inc := counter()
    fmt.Printf("Counter: %d\n", inc())
    fmt.Printf("Counter: %d\n", inc())
    fmt.Printf("Counter: %d\n", inc())

    // Higher-order function
    fmt.Println("\n=== Higher-Order Function ===")
    numbers := []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
    evens := filter(numbers, func(n int) bool { return n%2 == 0 })
    fmt.Printf("Even numbers: %v\n", evens)

    positives := filter(numbers, func(n int) bool { return n > 5 })
    fmt.Printf("Greater than 5: %v\n", positives)
}

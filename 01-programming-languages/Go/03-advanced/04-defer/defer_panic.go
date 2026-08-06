package main

import "fmt"

func main() {
    // 1. Defer basics
    fmt.Println("=== Defer Basics ===")
    deferOrder()

    // 2. Defer with return value
    fmt.Println("\n=== Defer with Return ===")
    fmt.Println("Result:", deferWithReturn())

    // 3. Panic
    fmt.Println("\n=== Panic ===")
    safeFunction()

    // 4. Recover
    fmt.Println("\n=== Recover ===")
    result, err := safeDivide(10, 0)
    if err != nil {
        fmt.Println("Error:", err)
    } else {
        fmt.Println("Result:", result)
    }

    result, err = safeDivide(10, 2)
    if err != nil {
        fmt.Println("Error:", err)
    } else {
        fmt.Println("Result:", result)
    }

    // 5. Defer for cleanup
    fmt.Println("\n=== Defer Cleanup ===")
    processResource()

    // 6. Multiple defers (LIFO)
    fmt.Println("\n=== Multiple Defers (LIFO) ===")
    multipleDefers()
}

// Defer executes in LIFO order
func deferOrder() {
    fmt.Println("Start")
    defer fmt.Println("First defer")
    defer fmt.Println("Second defer")
    defer fmt.Println("Third defer")
    fmt.Println("End")
}

// Defer executes before return, can modify named returns
func deferWithReturn() (result int) {
    defer func() {
        result *= 2
    }()
    return 5 // defer runs, result becomes 10
}

// Panic stops execution
func safeFunction() {
    defer func() {
        if r := recover(); r != nil {
            fmt.Println("Recovered from panic:", r)
        }
    }()

    fmt.Println("Before panic")
    panic("something went wrong")
    fmt.Println("After panic") // Never reached
}

// Recover in deferred function
func safeDivide(a, b int) (result int, err error) {
    defer func() {
        if r := recover(); r != nil {
            err = fmt.Errorf("recovered: %v", r)
        }
    }()

    return a / b, nil // Panics if b=0
}

// Defer for resource cleanup
func processResource() {
    fmt.Println("Opening resource")
    defer fmt.Println("Closing resource (deferred)")

    fmt.Println("Processing...")
}

// Multiple defers stack in LIFO
func multipleDefers() {
    for i := 0; i < 3; i++ {
        defer fmt.Println("Defer:", i)
    }
    fmt.Println("Function body")
}

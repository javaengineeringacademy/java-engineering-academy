package main

import "fmt"

// Package-level variable declarations
var (
    appName    string = "GoDemo"
    version    string = "1.0"
    maxRetries int    = 3
)

// Constants
const (
    Pi          = 3.14159
    MaxSize     = 1024
    DefaultPort = 8080
)

// Typed constant
const StatusOK int = 200

func main() {
    // 1. var declaration with type
    var firstName string = "Alice"
    fmt.Println("First name:", firstName)

    // 2. var with type inference
    var age = 30
    fmt.Println("Age:", age)

    // 3. Short declaration (:=) - type inferred
    email := "alice@example.com"
    fmt.Println("Email:", email)

    // 4. Multiple declarations
    var (
        height float64 = 5.6
        weight float64 = 140.5
    )
    fmt.Printf("Height: %.1f, Weight: %.1f\n", height, weight)

    // 5. Zero values
    var uninitializedInt int
    var uninitializedStr string
    var uninitializedBool bool
    fmt.Printf("Zero int: %d, Zero string: '%s', Zero bool: %t\n",
        uninitializedInt, uninitializedStr, uninitializedBool)

    // 6. Type conversions
    var i int = 42
    var f float64 = float64(i)
    var u uint = uint(f)
    fmt.Printf("int: %d, float64: %.1f, uint: %d\n", i, f, u)

    // 7. Constants
    fmt.Printf("Pi: %.5f, MaxSize: %d, Port: %d\n", Pi, MaxSize, DefaultPort)

    // 8. Blank identifier
    result, _ := divide(10, 3)
    fmt.Println("Result:", result)

    // 9. iota for enum-like constants
    const (
        Sunday = iota
        Monday
        Tuesday
        Wednesday
        Thursday
        Friday
        Saturday
    )
    fmt.Printf("Sunday: %d, Wednesday: %d, Saturday: %d\n", Sunday, Wednesday, Saturday)

    // 10. Package-level variables accessible
    fmt.Printf("App: %s v%s (max retries: %d)\n", appName, version, maxRetries)
}

func divide(a, b int) (int, error) {
    if b == 0 {
        return 0, fmt.Errorf("division by zero")
    }
    return a / b, nil
}

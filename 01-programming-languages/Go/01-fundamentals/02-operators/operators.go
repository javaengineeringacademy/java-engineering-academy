package main

import "fmt"

func main() {
    // =====================
    // ARITHMETIC OPERATORS
    // =====================
    fmt.Println("=== Arithmetic Operators ===")

    a, b := 17, 5

    fmt.Printf("%d + %d = %d\n", a, b, a+b)
    fmt.Printf("%d - %d = %d\n", a, b, a-b)
    fmt.Printf("%d * %d = %d\n", a, b, a*b)
    fmt.Printf("%d / %d = %d\n", a, b, a/b) // Integer division
    fmt.Printf("%d %% %d = %d\n", a, b, a%b)

    // Float division
    x, y := 17.0, 5.0
    fmt.Printf("%.1f / %.1f = %.2f\n", x, y, x/y)

    // ====================
    // COMPARISON OPERATORS
    // ====================
    fmt.Println("\n=== Comparison Operators ===")

    fmt.Printf("%d == %d: %t\n", a, b, a == b)
    fmt.Printf("%d != %d: %t\n", a, b, a != b)
    fmt.Printf("%d > %d: %t\n", a, b, a > b)
    fmt.Printf("%d < %d: %t\n", a, b, a < b)
    fmt.Printf("%d >= %d: %t\n", a, b, a >= b)
    fmt.Printf("%d <= %d: %t\n", a, b, a <= b)

    // ===================
    // LOGICAL OPERATORS
    // ===================
    fmt.Println("\n=== Logical Operators ===")

    p, q := true, false
    fmt.Printf("true && false = %t\n", p && q)
    fmt.Printf("true || false = %t\n", p || q)
    fmt.Printf("!true = %t\n", !p)

    // Short-circuit evaluation
    fmt.Println("\nShort-circuit examples:")
    if p && expensiveCheck() {
        fmt.Println("Both conditions true")
    }
    if q || expensiveCheck() {
        fmt.Println("At least one true")
    } else {
        fmt.Println("Both false, second not evaluated")
    }

    // ====================
    // BITWISE OPERATORS
    // ====================
    fmt.Println("\n=== Bitwise Operators ===")

    m, n := 12, 10 // 1100, 1010 in binary
    fmt.Printf("%d & %d = %d\n", m, n, m&n)
    fmt.Printf("%d | %d = %d\n", m, n, m|n)
    fmt.Printf("%d ^ %d = %d\n", m, n, m^n)
    fmt.Printf("%d << 2 = %d\n", m, m<<2)
    fmt.Printf("%d >> 2 = %d\n", m, m>>2)

    // ===================
    // ASSIGNMENT OPERATORS
    // ===================
    fmt.Println("\n=== Assignment Operators ===")

    val := 100
    val += 50
    fmt.Printf("After += 50: %d\n", val)
    val -= 25
    fmt.Printf("After -= 25: %d\n", val)
    val *= 2
    fmt.Printf("After *= 2: %d\n", val)

    // ====================
    // TYPE ASSERTION
    // ====================
    fmt.Println("\n=== Type Assertion ===")

    var iface interface{} = "hello"
    str, ok := iface.(string)
    if ok {
        fmt.Printf("Type asserted: %s\n", str)
    }
}

func expensiveCheck() bool {
    fmt.Println("  (checking...)")
    return true
}

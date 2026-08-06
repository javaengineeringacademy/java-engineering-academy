package main

import "fmt"

func main() {
    // ==================
    // IF STATEMENTS
    // ==================
    fmt.Println("=== If Statements ===")

    age := 25
    if age >= 18 {
        fmt.Println("Adult")
    } else {
        fmt.Println("Minor")
    }

    // If with initialization
    if score := getScore(); score >= 90 {
        fmt.Printf("Grade: A (score: %d)\n", score)
    } else if score >= 80 {
        fmt.Printf("Grade: B (score: %d)\n", score)
    } else {
        fmt.Printf("Grade: C (score: %d)\n", score)
    }

    // ==================
    // SWITCH STATEMENT
    // ==================
    fmt.Println("\n=== Switch Statement ===")

    day := "Wednesday"
    switch day {
    case "Monday", "Tuesday", "Wednesday", "Thursday", "Friday":
        fmt.Println("Weekday")
    case "Saturday", "Sunday":
        fmt.Println("Weekend")
    default:
        fmt.Println("Invalid day")
    }

    // Expressionless switch (like if-else chain)
    temp := 72
    switch {
    case temp >= 90:
        fmt.Println("Hot")
    case temp >= 70:
        fmt.Println("Warm")
    case temp >= 50:
        fmt.Println("Cool")
    default:
        fmt.Println("Cold")
    }

    // Switch with type assertion
    var val interface{} = "hello"
    switch v := val.(type) {
    case string:
        fmt.Printf("String: %s (len: %d)\n", v, len(v))
    case int:
        fmt.Printf("Integer: %d\n", v)
    default:
        fmt.Printf("Unknown type: %T\n", v)
    }

    // Switch with fallthrough
    n := 2
    switch n {
    case 1:
        fmt.Println("One")
        fallthrough
    case 2:
        fmt.Println("Two (fallthrough from 1)")
        fallthrough
    case 3:
        fmt.Println("Three (fallthrough from 2)")
    }

    // ==================
    // FOR LOOPS
    // ==================
    fmt.Println("\n=== For Loops ===")

    // Traditional for loop
    fmt.Print("Traditional: ")
    for i := 0; i < 5; i++ {
        fmt.Printf("%d ", i)
    }
    fmt.Println()

    // While-style loop
    fmt.Print("While-style: ")
    count := 0
    for count < 5 {
        fmt.Printf("%d ", count)
        count++
    }
    fmt.Println()

    // Infinite loop with break
    fmt.Print("Infinite: ")
    i := 0
    for {
        if i >= 5 {
            break
        }
        fmt.Printf("%d ", i)
        i++
    }
    fmt.Println()

    // Continue statement
    fmt.Print("Even only: ")
    for i := 0; i < 10; i++ {
        if i%2 != 0 {
            continue
        }
        fmt.Printf("%d ", i)
    }
    fmt.Println()

    // ==================
    // RANGE
    // ==================
    fmt.Println("\n=== Range ===")

    // Range over slice
    fruits := []string{"apple", "banana", "cherry"}
    for i, fruit := range fruits {
        fmt.Printf("Index %d: %s\n", i, fruit)
    }

    // Range over map
    colors := map[string]string{"red": "#FF0000", "green": "#00FF00"}
    for key, value := range colors {
        fmt.Printf("%s: %s\n", key, value)
    }

    // Range over string (Unicode code points)
    for i, ch := range "Go语言" {
        fmt.Printf("Byte %d: %c\n", i, ch)
    }
}

func getScore() int {
    return 85
}

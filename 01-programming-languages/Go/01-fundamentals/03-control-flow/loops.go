package main

import "fmt"

func main() {
    fmt.Println("=== Loop Patterns ===")

    // Nested loops with labels
    fmt.Println("\nNested loops with labels:")
outer:
    for i := 0; i < 3; i++ {
        for j := 0; j < 3; j++ {
            if i == 1 && j == 1 {
                continue outer
            }
            fmt.Printf("(%d,%d) ", i, j)
        }
    }
    fmt.Println()

    // Range with underscore (ignore index)
    fmt.Println("\nIgnoring index with _:")
    nums := []int{10, 20, 30}
    for _, v := range nums {
        fmt.Printf("%d ", v)
    }
    fmt.Println()

    // Range with only index
    fmt.Println("\nOnly index:")
    for i := range nums {
        fmt.Printf("Index: %d ", i)
    }
    fmt.Println()

    // Map iteration order
    fmt.Println("\nMap iteration (order not guaranteed):")
    scores := map[string]int{"Alice": 95, "Bob": 87, "Charlie": 92}
    for name, score := range scores {
        fmt.Printf("%s: %d\n", name, score)
    }

    // String range (byte vs rune)
    fmt.Println("\nString range (byte vs rune):")
    s := "Hello, 世界"
    fmt.Print("Bytes: ")
    for i := 0; i < len(s); i++ {
        fmt.Printf("%d ", s[i])
    }
    fmt.Println()
    fmt.Print("Runes: ")
    for _, r := range s {
        fmt.Printf("%c ", r)
    }
    fmt.Println()

    // Channel range
    fmt.Println("\nChannel range:")
    ch := make(chan int, 3)
    ch <- 1
    ch <- 2
    ch <- 3
    close(ch)
    for v := range ch {
        fmt.Printf("%d ", v)
    }
    fmt.Println()
}

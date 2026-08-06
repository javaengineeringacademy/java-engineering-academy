package main

import "fmt"

func main() {
    // 1. Map creation
    fmt.Println("=== Map Creation ===")

    // Literal
    m1 := map[string]int{"a": 1, "b": 2, "c": 3}
    fmt.Printf("Literal: %v\n", m1)

    // make
    m2 := make(map[string]int)
    m2["x"] = 10
    m2["y"] = 20
    fmt.Printf("make: %v\n", m2)

    // 2. Basic operations
    fmt.Println("\n=== Basic Operations ===")

    ages := map[string]int{
        "Alice":   30,
        "Bob":     25,
        "Charlie": 35,
    }

    // Access
    fmt.Printf("Alice's age: %d\n", ages["Alice"])

    // Add/Update
    ages["Dave"] = 28
    ages["Alice"] = 31
    fmt.Printf("After updates: %v\n", ages)

    // Delete
    delete(ages, "Bob")
    fmt.Printf("After delete: %v\n", ages)

    // Length
    fmt.Printf("Length: %d\n", len(ages))

    // 3. OK pattern
    fmt.Println("\n=== OK Pattern ===")

    age, ok := ages["Eve"]
    if ok {
        fmt.Printf("Eve's age: %d\n", age)
    } else {
        fmt.Println("Eve not found")
    }

    age, ok = ages["Alice"]
    if ok {
        fmt.Printf("Alice's age: %d\n", age)
    }

    // 4. Range over map
    fmt.Println("\n=== Range over Map ===")

    for name, age := range ages {
        fmt.Printf("%s: %d\n", name, age)
    }

    // 5. Map with slice values
    fmt.Println("\n=== Map with Slices ===")

    groups := make(map[string][]string)
    groups["fruits"] = append(groups["fruits"], "apple", "banana")
    groups["vegetables"] = append(groups["vegetables"], "carrot")
    fmt.Printf("Groups: %v\n", groups)

    // 6. Map of maps
    fmt.Println("\n=== Nested Maps ===")

    users := map[string]map[string]interface{}{
        "alice": {"age": 30, "email": "alice@example.com"},
        "bob":   {"age": 25, "email": "bob@example.com"},
    }

    for name, info := range users {
        fmt.Printf("%s: %v\n", name, info)
    }

    // 7. Nil map
    fmt.Println("\n=== Nil Map ===")

    var nilMap map[string]int
    // nilMap["key"] = 1 // PANIC: assignment to entry in nil map

    if nilMap == nil {
        fmt.Println("nilMap is nil")
    }

    // Initialize before use
    nilMap = make(map[string]int)
    nilMap["key"] = 1
    fmt.Printf("After init: %v\n", nilMap)

    // 8. Map as set
    fmt.Println("\n=== Map as Set ===")

    set := map[string]bool{}
    set["apple"] = true
    set["banana"] = true

    if set["apple"] {
        fmt.Println("Set contains apple")
    }
    delete(set, "apple")
    fmt.Printf("Set: %v\n", set)
}

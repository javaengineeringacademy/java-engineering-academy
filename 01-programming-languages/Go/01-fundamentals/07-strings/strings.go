package main

import (
    "fmt"
    "strings"
    "unicode"
)

func main() {
    // =====================
    // BASIC STRING OPS
    // =====================
    fmt.Println("=== Basic String Operations ===")

    s := "Hello, World!"
    fmt.Printf("String: %s\n", s)
    fmt.Printf("Length: %d bytes\n", len(s))
    fmt.Printf("First byte: %c\n", s[0])
    fmt.Printf("Substring: %s\n", s[0:5])

    // =====================
    // STRINGS PACKAGE
    // =====================
    fmt.Println("\n=== Strings Package ===")

    fmt.Printf("Contains 'World': %t\n", strings.Contains(s, "World"))
    fmt.Printf("HasPrefix 'Hello': %t\n", strings.HasPrefix(s, "Hello"))
    fmt.Printf("HasSuffix '!': %t\n", strings.HasSuffix(s, "!"))
    fmt.Printf("Index of 'World': %d\n", strings.Index(s, "World"))
    fmt.Printf("Count 'l': %d\n", strings.Count(s, "l"))

    // Transformations
    fmt.Printf("ToUpper: %s\n", strings.ToUpper(s))
    fmt.Printf("ToLower: %s\n", strings.ToLower(s))
    fmt.Printf("Title: %s\n", strings.Title("hello world"))

    // Split and Join
    csv := "apple,banana,cherry"
    fruits := strings.Split(csv, ",")
    fmt.Printf("Split: %v\n", fruits)
    fmt.Printf("Join: %s\n", strings.Join(fruits, " | "))

    // Trim
    padded := "  Hello  "
    fmt.Printf("TrimSpace: '%s'\n", strings.TrimSpace(padded))
    fmt.Printf("TrimLeft: '%s'\n", strings.TrimLeft(padded, " "))
    fmt.Printf("TrimRight: '%s'\n", strings.TrimRight(padded, " "))

    // Replace
    fmt.Printf("Replace: %s\n", strings.Replace(s, "World", "Go", 1))

    // =====================
    // FMT PACKAGE
    // =====================
    fmt.Println("\n=== Fmt Package ===")

    name := "Alice"
    age := 30
    pi := 3.14159

    fmt.Printf("Sprintf: Name: %s, Age: %d\n", name, age)
    fmt.Printf("Float: %.2f\n", pi)
    fmt.Printf("Binary: %b\n", 255)
    fmt.Printf("Hex: %x\n", 255)
    fmt.Printf("Pointer: %p\n", &name)
    fmt.Printf("Type: %T\n", name)

    // =====================
    // UNICODE
    // =====================
    fmt.Println("\n=== Unicode ===")

    text := "Hello, 世界!"
    for i, ch := range text {
        if unicode.IsLetter(ch) {
            fmt.Printf("Index %d: %c (letter)\n", i, ch)
        } else if unicode.IsDigit(ch) {
            fmt.Printf("Index %d: %c (digit)\n", i, ch)
        } else {
            fmt.Printf("Index %d: %c (other)\n", i, ch)
        }
    }

    // =====================
    // STRING BUILDER
    // =====================
    fmt.Println("\n=== String Builder ===")

    var builder strings.Builder
    for i := 0; i < 5; i++ {
        builder.WriteString(fmt.Sprintf("Word%d ", i))
    }
    fmt.Printf("Builder result: %s\n", builder.String())

    // =====================
    // RAW STRINGS
    // =====================
    fmt.Println("\n=== Raw Strings ===")

    raw := `This is a raw string.
It can span multiple lines.
No \n escapes needed.`
    fmt.Println(raw)
}

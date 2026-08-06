package main

import (
    "fmt"
    "math"
)

// Basic interface
type Shape interface {
    Area() float64
    Perimeter() float64
}

// Circle implements Shape
type Circle struct {
    Radius float64
}

func (c Circle) Area() float64 {
    return math.Pi * c.Radius * c.Radius
}

func (c Circle) Perimeter() float64 {
    return 2 * math.Pi * c.Radius
}

// Rectangle implements Shape
type Rectangle struct {
    Width, Height float64
}

func (r Rectangle) Area() float64 {
    return r.Width * r.Height
}

func (r Rectangle) Perimeter() float64 {
    return 2 * (r.Width + r.Height)
}

// Interface composition
type Stringer interface {
    String() string
}

type Formatter interface {
    Stringer
    Format(pattern string) string
}

type Document struct {
    Title string
    Pages int
}

func (d Document) String() string {
    return fmt.Sprintf("%s (%d pages)", d.Title, d.Pages)
}

func (d Document) Format(pattern string) string {
    return fmt.Sprintf(pattern, d.Title, d.Pages)
}

// Empty interface
func describe(i interface{}) {
    fmt.Printf("(%v, %T)\n", i, i)
}

// Type assertion
func processValue(i interface{}) {
    switch v := i.(type) {
    case int:
        fmt.Printf("Integer: %d\n", v)
    case string:
        fmt.Printf("String: %s (len: %d)\n", v, len(v))
    case bool:
        fmt.Printf("Boolean: %t\n", v)
    default:
        fmt.Printf("Unknown: %v\n", v)
    }
}

func main() {
    // 1. Interface implementation
    fmt.Println("=== Interface Implementation ===")

    shapes := []Shape{
        Circle{Radius: 5},
        Rectangle{Width: 4, Height: 6},
    }

    for _, s := range shapes {
        fmt.Printf("Shape: %T, Area: %.2f, Perimeter: %.2f\n",
            s, s.Area(), s.Perimeter())
    }

    // 2. Interface composition
    fmt.Println("\n=== Interface Composition ===")
    doc := Document{Title: "Go Guide", Pages: 42}
    fmt.Println(doc.String())
    fmt.Println(doc.Format("Title: %s, Pages: %d"))

    // 3. Empty interface
    fmt.Println("\n=== Empty Interface ===")
    describe(42)
    describe("hello")
    describe(true)
    describe([]int{1, 2, 3})

    // 4. Type assertions
    fmt.Println("\n=== Type Assertions ===")
    processValue(42)
    processValue("hello")
    processValue(true)

    // Safe type assertion with comma-ok
    var i interface{} = "hello"
    s, ok := i.(string)
    if ok {
        fmt.Printf("Assertion successful: %s\n", s)
    }

    n, ok := i.(int)
    if !ok {
        fmt.Println("Assertion failed: not an int")
    }

    // 5. Nil interface
    fmt.Println("\n=== Nil Interface ===")
    var p *int = nil
    var iface interface{} = p
    fmt.Printf("Interface is nil: %t, Value: %v\n", iface == nil, iface)

    // 6. Interface with nil value
    var empty interface{}
    fmt.Printf("Empty interface is nil: %t\n", empty == nil)
}

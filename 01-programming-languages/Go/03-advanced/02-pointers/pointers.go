package main

import "fmt"

// Counter with pointer receiver
type Counter struct {
    value int
}

func (c *Counter) Increment() {
    c.value++
}

func (c *Counter) Get() int {
    return c.value
}

// Person with pointer for mutation
type Person struct {
    Name string
    Age  int
}

func (p *Person) Birthday() {
    p.Age++
}

func (p Person) String() string {
    return fmt.Sprintf("%s (age %d)", p.Name, p.Age)
}

func main() {
    // 1. Basic pointer operations
    fmt.Println("=== Basic Pointers ===")

    x := 42
    fmt.Printf("x: %d, address: %p\n", x, &x)

    p := &x
    fmt.Printf("p: %p, *p: %d\n", p, *p)

    *p = 100
    fmt.Printf("After modification: x = %d\n", x)

    // 2. new function
    fmt.Println("\n=== new Function ===")

    pInt := new(int)
    pStr := new(string)

    fmt.Printf("*pInt: %d (zero value)\n", *pInt)
    fmt.Printf("*pStr: '%s' (zero value)\n", *pStr)

    *pInt = 42
    *pStr = "hello"
    fmt.Printf("*pInt: %d, *pStr: %s\n", *pInt, *pStr)

    // 3. Pointer receivers
    fmt.Println("\n=== Pointer Receivers ===")

    c := &Counter{}
    for i := 0; i < 5; i++ {
        c.Increment()
    }
    fmt.Printf("Counter: %d\n", c.Get())

    // 4. Struct with pointer
    fmt.Println("\n=== Struct Pointers ===")

    p1 := &Person{Name: "Alice", Age: 30}
    fmt.Println(p1)
    p1.Birthday()
    fmt.Println("After birthday:", p1)

    // 5. Pointer to slice
    fmt.Println("\n=== Pointer to Slice ===")

    nums := []int{1, 2, 3}
    fmt.Println("Before:", nums)

    modifySlice(&nums)
    fmt.Println("After:", nums)

    // 6. Nil pointer check
    fmt.Println("\n=== Nil Pointer ===")

    var nilPtr *int
    fmt.Printf("nilPtr is nil: %t\n", nilPtr == nil)

    if nilPtr != nil {
        fmt.Println(*nilPtr)
    } else {
        fmt.Println("Cannot dereference nil pointer")
    }

    // 7. Pointer comparison
    fmt.Println("\n=== Pointer Comparison ===")

    a := 10
    b := 10
    pA := &a
    pB := &b
    pC := &a

    fmt.Printf("pA == pB: %t (different addresses)\n", pA == pB)
    fmt.Printf("pA == pC: %t (same address)\n", pA == pC)
}

func modifySlice(nums *[]int) {
    *nums = append(*nums, 4, 5)
}

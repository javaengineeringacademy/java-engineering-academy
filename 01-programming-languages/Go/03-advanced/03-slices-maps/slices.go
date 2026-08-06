package main

import "fmt"

func main() {
    // 1. Slice creation
    fmt.Println("=== Slice Creation ===")

    // Literal
    s1 := []int{1, 2, 3}
    fmt.Printf("Literal: %v (len=%d, cap=%d)\n", s1, len(s1), cap(s1))

    // make
    s2 := make([]int, 5)
    fmt.Printf("make(len=5): %v (len=%d, cap=%d)\n", s2, len(s2), cap(s2))

    // make with capacity
    s3 := make([]int, 0, 10)
    fmt.Printf("make(len=0, cap=10): %v (len=%d, cap=%d)\n", s3, len(s3), cap(s3))

    // From array
    arr := [5]int{10, 20, 30, 40, 50}
    s4 := arr[1:4]
    fmt.Printf("From array: %v (len=%d, cap=%d)\n", s4, len(s4), cap(s4))

    // 2. Append
    fmt.Println("\n=== Append ===")

    s := []int{1, 2, 3}
    fmt.Printf("Original: %v (len=%d, cap=%d)\n", s, len(s), cap(s))

    s = append(s, 4)
    fmt.Printf("After append(4): %v (len=%d, cap=%d)\n", s, len(s), cap(s))

    s = append(s, 5, 6, 7)
    fmt.Printf("After append(5,6,7): %v (len=%d, cap=%d)\n", s, len(s), cap(s))

    // Append slice to slice
    s2 = []int{8, 9}
    s = append(s, s2...)
    fmt.Printf("After append slice: %v\n", s)

    // 3. Copy
    fmt.Println("\n=== Copy ===")

    src := []int{1, 2, 3, 4, 5}
    dst := make([]int, 3)
    n := copy(dst, src)
    fmt.Printf("Copied %d elements: %v\n", n, dst)

    // Copy with overlapping
    data := []int{1, 2, 3, 4, 5}
    copy(data[1:], data[:3])
    fmt.Printf("Overlapping copy: %v\n", data)

    // 4. Sub-slicing
    fmt.Println("\n=== Sub-slicing ===")

    s = []int{0, 1, 2, 3, 4, 5}
    fmt.Printf("Full: %v\n", s)
    fmt.Printf("s[1:4]: %v\n", s[1:4])
    fmt.Printf("s[:3]: %v\n", s[:3])
    fmt.Printf("s[2:]: %v\n", s[2:])

    // Sub-slice shares underlying array
    sub := s[1:3]
    sub[0] = 99
    fmt.Printf("After modifying sub: %v\n", s)

    // 5. Delete element
    fmt.Println("\n=== Delete Element ===")

    s = []int{1, 2, 3, 4, 5}
    i := 2 // Delete index 2
    s = append(s[:i], s[i+1:]...)
    fmt.Printf("After deleting index 2: %v\n", s)

    // 6. Nil vs empty slice
    fmt.Println("\n=== Nil vs Empty Slice ===")

    var nilSlice []int
    emptySlice := []int{}
    makeSlice := make([]int, 0)

    fmt.Printf("nil slice: %v, len=%d, cap=%d, == nil: %t\n",
        nilSlice, len(nilSlice), cap(nilSlice), nilSlice == nil)
    fmt.Printf("empty slice: %v, len=%d, cap=%d, == nil: %t\n",
        emptySlice, len(emptySlice), cap(emptySlice), emptySlice == nil)
    fmt.Printf("make slice: %v, len=%d, cap=%d, == nil: %t\n",
        makeSlice, len(makeSlice), cap(makeSlice), makeSlice == nil)

    // All serialize the same in JSON
    // Use make([]int, 0) for empty JSON array []
}

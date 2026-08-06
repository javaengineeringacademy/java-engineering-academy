package main

import "fmt"

// Generic function
func Max[T comparable](a, b T) T {
    if a > b {
        return a
    }
    return b
}

// Custom constraint
type Number interface {
    ~int | ~float64 | ~float32
}

func Sum[T Number](nums []T) T {
    var total T
    for _, n := range nums {
        total += n
    }
    return total
}

func Contains[T comparable](slice []T, item T) bool {
    for _, v := range slice {
        if v == item {
            return true
        }
    }
    return false
}

// Generic type
type Stack[T any] struct {
    items []T
}

func (s *Stack[T]) Push(item T) {
    s.items = append(s.items, item)
}

func (s *Stack[T]) Pop() (T, bool) {
    var zero T
    if len(s.items) == 0 {
        return zero, false
    }
    item := s.items[len(s.items)-1]
    s.items = s.items[:len(s.items)-1]
    return item, true
}

func (s *Stack[T]) Len() int {
    return len(s.items)
}

// Generic map function
func Map[T any, U any](slice []T, f func(T) U) []U {
    result := make([]U, len(slice))
    for i, v := range slice {
        result[i] = f(v)
    }
    return result
}

func main() {
    // 1. Generic Max function
    fmt.Println("=== Generic Functions ===")
    fmt.Printf("Max(3, 5) = %d\n", Max(3, 5))
    fmt.Printf("Max(3.14, 2.71) = %.2f\n", Max(3.14, 2.71))
    fmt.Printf("Max(\"apple\", \"banana\") = %s\n", Max("apple", "banana"))

    // 2. Sum with constraint
    fmt.Println("\n=== Sum ===")
    ints := []int{1, 2, 3, 4, 5}
    floats := []float64{1.1, 2.2, 3.3}
    fmt.Printf("Sum ints: %d\n", Sum(ints))
    fmt.Printf("Sum floats: %.1f\n", Sum(floats))

    // 3. Contains
    fmt.Println("\n=== Contains ===")
    nums := []int{1, 2, 3, 4, 5}
    fmt.Printf("Contains 3: %t\n", Contains(nums, 3))
    fmt.Printf("Contains 6: %t\n", Contains(nums, 6))

    words := []string{"a", "b", "c"}
    fmt.Printf("Contains 'b': %t\n", Contains(words, "b"))

    // 4. Generic Stack
    fmt.Println("\n=== Generic Stack ===")
    stack := &Stack[int]{}
    stack.Push(10)
    stack.Push(20)
    stack.Push(30)
    fmt.Printf("Stack len: %d\n", stack.Len())

    for stack.Len() > 0 {
        val, _ := stack.Pop()
        fmt.Printf("Popped: %d\n", val)
    }

    // 5. Generic Map function
    fmt.Println("\n=== Map Function ===")
    nums2 := []int{1, 2, 3, 4, 5}
    doubled := Map(nums2, func(n int) int { return n * 2 })
    fmt.Printf("Doubled: %v\n", doubled)

    strs := Map(nums2, func(n int) string { return fmt.Sprintf("#%d", n) })
    fmt.Printf("Stringified: %v\n", strs)
}

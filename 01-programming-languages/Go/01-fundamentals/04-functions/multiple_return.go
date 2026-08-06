package main

import (
	"fmt"
	"math"
	"strconv"
	"strings"
)

// Multiple Return Values - Idiomatic Go pattern
// Functions often return (result, error) or (value, bool)

// 1. Basic multiple returns
func divide(a, b float64) (float64, bool) {
	if b == 0 {
		return 0, false
	}
	return a / b, true
}

// 2. Named return values with error
func sqrt(x float64) (result float64, err error) {
	if x < 0 {
		return 0, fmt.Errorf("square root of negative number: %f", x)
	}
	return math.Sqrt(x), nil
}

// 3. Multiple returns with slice
func splitString(s string, delimiter string) (parts []string, count int) {
	parts = strings.Split(s, delimiter)
	return parts, len(parts)
}

// 4. Return multiple values conditionally
func classify(n int) (string, bool) {
	switch {
	case n < 0:
		return "negative", true
	case n == 0:
		return "zero", true
	case n > 0:
		return "positive", true
	default:
		return "", false // Should never happen
	}
}

// 5. Multiple returns with channel
func generateNumbers(count int) ([]int, error) {
	if count < 0 {
		return nil, fmt.Errorf("negative count: %d", count)
	}
	numbers := make([]int, count)
	for i := 0; i < count; i++ {
		numbers[i] = i * i
	}
	return numbers, nil
}

// 6. Return error as last value (idiomatic)
func parseAge(s string) (int, error) {
	age, err := strconv.Atoi(s)
	if err != nil {
		return 0, fmt.Errorf("invalid age: %w", err)
	}
	if age < 0 || age > 150 {
		return 0, fmt.Errorf("age out of range: %d", age)
	}
	return age, nil
}

// 7. Multiple returns with struct
type Result struct {
	Value float64
	Valid bool
	Count int
}

func processNumbers(nums []int) Result {
	if len(nums) == 0 {
		return Result{Valid: false}
	}
	sum := 0
	for _, n := range nums {
		sum += n
	}
	return Result{
		Value: float64(sum) / float64(len(nums)),
		Valid: true,
		Count: len(nums),
	}
}

// 8. Multiple returns with variadic
func minMax(nums ...int) (int, int, error) {
	if len(nums) == 0 {
		return 0, 0, fmt.Errorf("empty slice")
	}
	min, max := nums[0], nums[0]
	for _, n := range nums[1:] {
		if n < min {
			min = n
		}
		if n > max {
			max = n
		}
	}
	return min, max, nil
}

// 9. Return function and error
func createMultiplier(factor float64) (func(float64) float64, error) {
	if factor == 0 {
		return nil, fmt.Errorf("factor cannot be zero")
	}
	return func(x float64) float64 {
		return x * factor
	}, nil
}

// 10. Multiple returns with defer
func readFile(path string) (content string, err error) {
	defer func() {
		if r := recover(); r != nil {
			err = fmt.Errorf("panic recovered: %v", r)
		}
	}()

	// Simulate file reading
	if path == "" {
		panic("empty path")
	}
	return "file content here", nil
}

func main() {
	// Basic multiple returns
	if result, ok := divide(10, 3); ok {
		fmt.Printf("divide(10, 3) = %.2f\n", result)
	} else {
		fmt.Println("Cannot divide by zero")
	}

	// Named returns with error
	if result, err := sqrt(16); err != nil {
		fmt.Printf("Error: %v\n", err)
	} else {
		fmt.Printf("sqrt(16) = %.2f\n", result)
	}

	if _, err := sqrt(-4); err != nil {
		fmt.Printf("Error: %v\n", err)
	}

	// Split string
	parts, count := splitString("a,b,c,d", ",")
	fmt.Printf("Parts: %v, Count: %d\n", parts, count)

	// Classify numbers
	numbers := []int{-5, 0, 10}
	for _, n := range numbers {
		if desc, ok := classify(n); ok {
			fmt.Printf("%d is %s\n", n, desc)
		}
	}

	// Generate numbers
	if nums, err := generateNumbers(5); err != nil {
		fmt.Printf("Error: %v\n", err)
	} else {
		fmt.Printf("Generated: %v\n", nums)
	}

	// Parse age
	ageStrs := []string{"25", "abc", "-5", "200"}
	for _, s := range ageStrs {
		if age, err := parseAge(s); err != nil {
			fmt.Printf("Error parsing %q: %v\n", s, err)
		} else {
			fmt.Printf("Parsed age: %d\n", age)
		}
	}

	// Process numbers
	result := processNumbers([]int{1, 2, 3, 4, 5})
	if result.Valid {
		fmt.Printf("Average: %.2f (count: %d)\n", result.Value, result.Count)
	}

	// Min/max
	if min, max, err := minMax(3, 1, 4, 1, 5, 9, 2, 6); err != nil {
		fmt.Printf("Error: %v\n", err)
	} else {
		fmt.Printf("Min: %d, Max: %d\n", min, max)
	}

	// Create multiplier
	if multiplier, err := createMultiplier(3.14); err != nil {
		fmt.Printf("Error: %v\n", err)
	} else {
		fmt.Printf("3.14 * 10 = %.2f\n", multiplier(10))
	}

	// Read file with panic recovery
	if content, err := readFile("test.txt"); err != nil {
		fmt.Printf("Error: %v\n", err)
	} else {
		fmt.Printf("Content: %s\n", content)
	}

	// Idiomatic Go: check error first
	result2, err := divide(10, 0)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
	} else {
		fmt.Printf("Result: %.2f\n", result2)
	}

	// Discard unused returns with blank identifier
	result3, _ := divide(10, 2)
	fmt.Printf("Result (discarding ok): %.2f\n", result3)
}
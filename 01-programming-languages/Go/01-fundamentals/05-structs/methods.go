package main

import (
	"fmt"
	"math"
	"strings"
)

// Methods in Go - Functions with a receiver
// Methods are defined on types (structs or named types)

// 1. Basic struct for method examples
type Circle struct {
	Radius float64
}

// Value receiver - operates on copy, cannot modify
func (c Circle) Area() float64 {
	return math.Pi * c.Radius * c.Radius
}

func (c Circle) Circumference() float64 {
	return 2 * math.Pi * c.Radius
}

// Pointer receiver - operates on original, can modify
func (c *Circle) Scale(factor float64) {
	c.Radius *= factor
}

// 2. Method chaining with pointer receivers
type Builder struct {
	strings.Builder
}

func NewBuilder() *Builder {
	return &Builder{}
}

func (b *Builder) AddLine(s string) *Builder {
	b.WriteString(s + "\n")
	return b
}

// 3. Method with multiple return values
type Vector struct {
	X, Y float64
}

func (v Vector) Add(other Vector) Vector {
	return Vector{v.X + other.X, v.Y + other.Y}
}

func (v Vector) Scale(factor float64) Vector {
	return Vector{v.X * factor, v.Y * factor}
}

func (v Vector) Magnitude() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}

func (v Vector) Normalize() (Vector, error) {
	mag := v.Magnitude()
	if mag == 0 {
		return Vector{}, fmt.Errorf("cannot normalize zero vector")
	}
	return v.Scale(1 / mag), nil
}

// 4. Method on named type
type Temperature float64

func (t Temperature) Celsius() float64 {
	return float64(t)
}

func (t Temperature) Fahrenheit() float64 {
	return float64(t)*9/5 + 32
}

func (t Temperature) Kelvin() float64 {
	return float64(t) + 273.15
}

// 5. Method with interface satisfaction
type Stringer interface {
	String() string
}

type Point struct {
	X, Y int
}

func (p Point) String() string {
	return fmt.Sprintf("(%d, %d)", p.X, p.Y)
}

// 6. Embedded struct methods
type Animal struct {
	Name string
}

func (a Animal) Speak() string {
	return a.Name + " makes a sound"
}

type Dog struct {
	Animal
	Breed string
}

func (d Dog) Speak() string { // Method overriding
	return d.Name + " barks!"
}

// 7. Method with variadic parameters
type Logger struct {
	Prefix string
}

func (l Logger) Log(messages ...string) {
	for _, msg := range messages {
		fmt.Printf("[%s] %s\n", l.Prefix, msg)
	}
}

// 8. Method on slice type
type IntSlice []int

func (s IntSlice) Sum() int {
	total := 0
	for _, v := range s {
		total += v
	}
	return total
}

func (s IntSlice) Average() float64 {
	if len(s) == 0 {
		return 0
	}
	return float64(s.Sum()) / float64(len(s))
}

// 9. Method with error return
type Money struct {
	Amount   float64
	Currency string
}

func (m Money) Convert(rate float64, target string) (Money, error) {
	if rate <= 0 {
		return Money{}, fmt.Errorf("invalid rate: %f", rate)
	}
	return Money{
		Amount:   m.Amount * rate,
		Currency: target,
	}, nil
}

// 10. Method on map type
type WordCount map[string]int

func (wc WordCount) Add(word string) {
	wc[word]++
}

func (wc WordCount) MostCommon() (string, int) {
	maxCount := 0
	maxWord := ""
	for word, count := range wc {
		if count > maxCount {
			maxCount = count
			maxWord = word
		}
	}
	return maxWord, maxCount
}

func main() {
	// 1. Value vs pointer receiver
	fmt.Println("=== Value vs Pointer Receiver ===")
	c := Circle{Radius: 5}
	fmt.Printf("Original: Radius=%.1f, Area=%.2f\n", c.Radius, c.Area())
	c.Scale(2)
	fmt.Printf("After Scale: Radius=%.1f, Area=%.2f\n", c.Radius, c.Area())

	// 2. Method chaining
	fmt.Println("\n=== Method Chaining ===")
	output := NewBuilder().
		AddLine("Hello").
		AddLine("World").
		AddLine("Go is awesome").
		String()
	fmt.Printf("Chained output:\n%s\n", output)

	// 3. Vector operations
	fmt.Println("\n=== Vector Operations ===")
	v1 := Vector{X: 3, Y: 4}
	v2 := Vector{X: 1, Y: 2}
	v3 := v1.Add(v2)
	fmt.Printf("v1 + v2 = %v\n", v3)
	fmt.Printf("|v1| = %.2f\n", v1.Magnitude())
	if normalized, err := v1.Normalize(); err == nil {
		fmt.Printf("v1 normalized = %v\n", normalized)
	}

	// 4. Temperature conversion
	fmt.Println("\n=== Temperature Conversion ===")
	temp := Temperature(100)
	fmt.Printf("%.1f°C = %.1f°F = %.1fK\n",
		temp.Celsius(), temp.Fahrenheit(), temp.Kelvin())

	// 5. Interface satisfaction
	fmt.Println("\n=== Interface Satisfaction ===")
	p := Point{X: 3, Y: 4}
	var s Stringer = p
	fmt.Printf("Stringer: %s\n", s.String())

	// 6. Embedded struct methods
	fmt.Println("\n=== Embedded Struct Methods ===")
	dog := Dog{
		Animal: Animal{Name: "Rex"},
		Breed:  "German Shepherd",
	}
	fmt.Printf("Dog speaks: %s\n", dog.Speak())
	fmt.Printf("Animal speaks: %s\n", dog.Animal.Speak())

	// 7. Logger with variadic
	fmt.Println("\n=== Logger with Variadic ===")
	log := Logger{Prefix: "INFO"}
	log.Log("Server started", "Listening on :8080", "Ready")

	// 8. Slice methods
	fmt.Println("\n=== Slice Methods ===")
	nums := IntSlice{1, 2, 3, 4, 5}
	fmt.Printf("Sum: %d, Average: %.2f\n", nums.Sum(), nums.Average())

	// 9. Money conversion
	fmt.Println("\n=== Money Conversion ===")
	usd := Money{Amount: 100, Currency: "USD"}
	if eur, err := usd.Convert(0.85, "EUR"); err == nil {
		fmt.Printf("$%.2f = €%.2f\n", usd.Amount, eur.Amount)
	}

	// 10. Word count
	fmt.Println("\n=== Word Count ===")
	wc := make(WordCount)
	words := []string{"go", "is", "awesome", "go", "rocks", "go"}
	for _, word := range words {
		wc.Add(word)
	}
	word, count := wc.MostCommon()
	fmt.Printf("Most common: %q (%d times)\n", word, count)

	// 11. Method on nil pointer
	fmt.Println("\n=== Nil Pointer Methods ===")
	var ptr *Circle
	// This would panic: ptr.Area()
	// Safe pattern:
	if ptr != nil {
		fmt.Printf("Area: %.2f\n", ptr.Area())
	} else {
		fmt.Println("Pointer is nil, cannot call method")
	}
}
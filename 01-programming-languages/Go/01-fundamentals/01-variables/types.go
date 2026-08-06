package main

import (
	"fmt"
	"reflect"
)

// Go Type System - Comprehensive guide to all data types
// Go has fewer types than most languages, emphasizing simplicity

// Custom type definitions
type UserID int
type Email string
type Score float64

// Struct types
type Person struct {
	Name string
	Age  int
}

// Interface type (implicitly defined)
type Stringer interface {
	String() string
}

func main() {
	// 1. Boolean type
	var isReady bool = true
	var isDone bool // Zero value: false
	fmt.Printf("bool: %t, zero: %t\n", isReady, isDone)

	// 2. Integer types (signed and unsigned)
	var (
		i8   int8    = 127      // -128 to 127
		i16  int16   = 32767    // -32768 to 32767
		i32  int32   = 2147483647
		i64  int64   = 9223372036854775807
		ui8  uint8   = 255      // byte is alias for uint8
		ui16 uint16  = 65535
		ui32 uint32  = 4294967295
		ui64 uint64  = 18446744073709551615
		_i   int     = 42      // Platform dependent (32 or 64 bit)
		_u   uint    = 42      // Platform dependent
		_r   rune    = 'A'     // Alias for int32, represents Unicode code point
	)
	fmt.Printf("int8: %d, int16: %d, int32: %d, int64: %d\n", i8, i16, i32, i64)
	fmt.Printf("uint8: %d, uint16: %d, uint32: %d, uint64: %d\n", ui8, ui16, ui32, ui64)
	fmt.Printf("int: %d, uint: %d, rune: %d (%c)\n", _i, _u, _r, _r)

	// 3. Floating-point types
	var (
		f32 float32 = 3.14159
		f64 float64 = 3.141592653589793
	)
	fmt.Printf("float32: %f, float64: %f\n", f32, f64)

	// 4. Complex types (for scientific computing)
	var c64 complex64 = complex(3, 4)    // 3+4i
	var c128 complex128 = complex(5, 12) // 5+12i
	fmt.Printf("complex64: %v, complex128: %v\n", c64, c128)
	fmt.Printf("Real: %f, Imaginary: %f\n", real(c64), imag(c64))
	fmt.Printf("Absolute value: %f\n", cmplx.Abs(c128)) // Note: need cmplx import

	// 5. String type (immutable, UTF-8 encoded)
	var str1 string = "Hello, World!"
	str2 := "Go is awesome"
	str3 := `Raw string
with newlines`
	fmt.Printf("string1: %s\nstring2: %s\nstring3: %s\n", str1, str2, str3)
	fmt.Printf("Length: %d bytes\n", len(str2))

	// 6. Byte slice (string manipulation)
	bytes := []byte(str2)
	fmt.Printf("Bytes: %v\n", bytes)

	// 7. Rune slice (Unicode code points)
	runes := []rune("Hello, 世界")
	fmt.Printf("Runes: %v, Length: %d\n", runes, len(runes))

	// 8. Array type (fixed size)
	var arr1 [5]int = [5]int{1, 2, 3, 4, 5}
	arr2 := [3]string{"a", "b", "c"}
	arr3 := [...]int{10, 20, 30} // Compiler counts elements
	fmt.Printf("Array1: %v, Array2: %v, Array3: %v\n", arr1, arr2, arr3)

	// 9. Slice type (dynamic size, reference type)
	slice1 := []int{1, 2, 3, 4, 5}
	slice2 := make([]string, 3)      // Length 3, capacity 3
	slice3 := make([]int, 0, 10)     // Length 0, capacity 10
	fmt.Printf("Slice1: %v, Slice2: %v, Slice3: %v\n", slice1, slice2, slice3)

	// 10. Map type (key-value pairs)
	map1 := map[string]int{"one": 1, "two": 2}
	map2 := make(map[string]float64)
	map2["pi"] = 3.14159
	fmt.Printf("Map1: %v, Map2: %v\n", map1, map2)

	// 11. Struct type
	person := Person{Name: "Alice", Age: 30}
	fmt.Printf("Person: %+v\n", person)

	// 12. Pointer type
	x := 42
	ptr := &x
	fmt.Printf("Pointer: %p, Value: %d\n", ptr, *ptr)

	// 13. Function type
	add := func(a, b int) int { return a + b }
	fmt.Printf("Function result: %d\n", add(5, 3))

	// 14. Interface type (implicit implementation)
	var s Stringer = person // Person implements Stringer if it has String() method
	_ = s

	// 15. Channel type (covered in concurrency)
	ch := make(chan int, 1)
	ch <- 42
	val := <-ch
	fmt.Printf("Channel value: %d\n", val)

	// 16. Type reflection
	fmt.Printf("\nType information:\n")
	fmt.Printf("int: %s\n", reflect.TypeOf(42))
	fmt.Printf("string: %s\n", reflect.TypeOf("hello"))
	fmt.Printf("float64: %s\n", reflect.TypeOf(3.14))
	fmt.Printf("bool: %s\n", reflect.TypeOf(true))

	// 17. Custom type usage
	var userID UserID = 12345
	var email Email = "user@example.com"
	var score Score = 98.5
	fmt.Printf("UserID: %d, Email: %s, Score: %f\n", userID, email, score)

	// 18. Type assertions (for interfaces)
	var i interface{} = "hello"
	str, ok := i.(string)
	fmt.Printf("Type assertion: %s, ok: %t\n", str, ok)
}
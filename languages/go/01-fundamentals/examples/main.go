package main

import "fmt"

func main() {
	// Variables
	var name string = "Go"
	version := 1.21
	fmt.Printf("Language: %s, Version: %.2f\n", name, version)

	// Arrays and Slices
	numbers := []int{1, 2, 3, 4, 5}
	fmt.Println("Numbers:", numbers)

	// Maps
	languages := map[string]string{
		"go":     "Golang",
		"python": "Python",
		"java":   "Java",
	}
	fmt.Println("Languages:", languages)

	// Structs
	type Person struct {
		Name string
		Age  int
	}
	p := Person{Name: "Alice", Age: 30}
	fmt.Printf("Person: %+v\n", p)

	// Goroutines
	go func() {
		fmt.Println("Hello from goroutine!")
	}()

	// Channels
	ch := make(chan string)
	go func() {
		ch <- "Hello from channel!"
	}()
	msg := <-ch
	fmt.Println(msg)
}

package main

import "fmt"

// Basic struct
type Person struct {
    Name  string
    Age   int
    Email string
}

// Method with value receiver
func (p Person) Greet() string {
    return fmt.Sprintf("Hi, I'm %s (%d years old)", p.Name, p.Age)
}

// Method with pointer receiver
func (p *Person) SetAge(age int) {
    p.Age = age
}

// Struct with embedded structs
type Address struct {
    Street string
    City   string
    State  string
}

type Employee struct {
    Person   // Embedded
    Address  // Embedded
    Company  string
    Position string
}

func (e Employee) Details() string {
    return fmt.Sprintf("%s works as %s at %s in %s",
        e.Name, e.Position, e.Company, e.City)
}

// Struct with slice field
type Team struct {
    Name     string
    Members  []Person
}

func (t Team) AddMember(p Person) {
    t.Members = append(t.Members, p)
}

// Anonymous struct
func printPoint() {
    point := struct {
        X, Y int
    }{10, 20}
    fmt.Printf("Point: (%d, %d)\n", point.X, point.Y)
}

func main() {
    // 1. Struct creation
    fmt.Println("=== Struct Creation ===")

    p1 := Person{Name: "Alice", Age: 30, Email: "alice@example.com"}
    fmt.Printf("p1: %+v\n", p1)

    p2 := Person{"Bob", 25, "bob@example.com"}
    fmt.Printf("p2: %+v\n", p2)

    p3 := new(Person)
    p3.Name = "Charlie"
    fmt.Printf("p3: %+v\n", p3)

    // 2. Methods
    fmt.Println("\n=== Methods ===")
    fmt.Println(p1.Greet())
    p1.SetAge(31)
    fmt.Printf("Updated age: %d\n", p1.Age)

    // 3. Embedding
    fmt.Println("\n=== Embedding ===")
    emp := Employee{
        Person:  Person{Name: "Dave", Age: 35, Email: "dave@company.com"},
        Address: Address{Street: "123 Main St", City: "San Francisco", State: "CA"},
        Company: "TechCorp",
        Position: "Engineer",
    }
    fmt.Printf("Employee: %+v\n", emp)
    fmt.Println(emp.Details())

    // Accessing embedded fields directly
    fmt.Printf("Name: %s, City: %s\n", emp.Name, emp.City)

    // 4. Struct with slice
    fmt.Println("\n=== Struct with Slice ===")
    team := Team{Name: "Backend"}
    team.Members = append(team.Members, Person{Name: "Eve", Age: 28})
    team.Members = append(team.Members, Person{Name: "Frank", Age: 32})
    fmt.Printf("Team: %+v\n", team)

    // 5. Anonymous struct
    fmt.Println("\n=== Anonymous Struct ===")
    printPoint()

    // 6. Struct comparison
    fmt.Println("\n=== Struct Comparison ===")
    a := Person{Name: "Alice", Age: 30}
    b := Person{Name: "Alice", Age: 30}
    c := Person{Name: "Bob", Age: 25}
    fmt.Printf("a == b: %t\n", a == b)
    fmt.Printf("a == c: %t\n", a == c)
}

# Structs in Go

Structs are composite types that group named fields. Go uses structs instead of classes for OOP.

## Struct Definition

```go
type Person struct {
    Name string
    Age  int
    Email string
}
```

## Creating Instances

```go
// Named fields
p1 := Person{Name: "Alice", Age: 30}

// Positional
p2 := Person{"Bob", 25, "bob@example.com"}

// Pointer with new
p3 := new(Person)
```

## Methods

```go
// Value receiver
func (p Person) Greet() string {
    return "Hello, " + p.Name
}

// Pointer receiver (can modify)
func (p *Person) SetAge(age int) {
    p.Age = age
}
```

## Embedding (Composition)

```go
type Address struct {
    Street string
    City   string
}

type Employee struct {
    Person   // Embedded struct
    Address  // Embedded struct
    Company  string
}
```

## Key Points
- Structs are value types
- Use pointers for mutation
- Embedding provides composition, not inheritance
- Fields are exported if capitalized
- Anonymous structs supported

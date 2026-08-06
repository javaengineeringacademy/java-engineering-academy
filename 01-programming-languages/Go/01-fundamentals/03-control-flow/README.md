# Control Flow in Go

Go uses `if`, `switch`, and `for` for control flow. No `while` or `do-while` loops.

## If Statement

```go
if x > 0 {
    fmt.Println("positive")
} else if x < 0 {
    fmt.Println("negative")
} else {
    fmt.Println("zero")
}

// With initialization
if val, ok := m[key]; ok {
    fmt.Println(val)
}
```

## Switch Statement

```go
// Basic switch
switch day {
case "Monday":
    fmt.Println("Start of week")
case "Friday":
    fmt.Println("Almost weekend")
default:
    fmt.Println("Midweek")
}

// Expressionless switch (replaces if-else chains)
switch {
case score >= 90:
    grade = "A"
case score >= 80:
    grade = "B"
default:
    grade = "C"
}
```

## For Loop

```go
// Traditional for loop
for i := 0; i < 10; i++ {
    fmt.Println(i)
}

// While-style
for count > 0 {
    count--
}

// Infinite loop
for {
    // use break to exit
}
```

## Range

```go
for i, v := range slice {
    fmt.Printf("index: %d, value: %d\n", i, v)
}

for k, v := range myMap {
    fmt.Printf("key: %s, value: %v\n", k, v)
}
```

## Key Points
- `if` conditions don't need parentheses
- `switch` breaks automatically (no fallthrough by default)
- `for` is the only loop keyword
- Use `break` and `continue` for flow control

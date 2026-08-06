# Strings in Go

Strings in Go are immutable sequences of bytes. Use the `strings` and `fmt` packages for operations.

## String Basics

```go
s := "Hello, World!"
fmt.Println(len(s))        // 13 bytes
fmt.Println(s[0])          // 'H' (byte)
```

## Common Operations

```go
strings.Contains(s, "Hello")  // true
strings.HasPrefix(s, "Hello") // true
strings.HasSuffix(s, "!")     // true
strings.Index(s, "World")     // 7
strings.ToUpper(s)            // "HELLO, WORLD!"
strings.ToLower(s)            // "hello, world!"
strings.Split(s, ", ")        // ["Hello", "World!"]
strings.Join([]string{"a","b"}, "-") // "a-b"
```

## fmt Package

```go
fmt.Sprintf("Name: %s, Age: %d", name, age)
fmt.Fprintf(w, "Writing to writer")
fmt.Errorf("error: %s", msg)
```

## Key Points
- Strings are UTF-8 encoded
- Use `[]rune` for Unicode character manipulation
- Strings are immutable (creates new on modification)
- Use `strings.Builder` for concatenation
- Raw strings use backticks: `` `raw\nstring` ``

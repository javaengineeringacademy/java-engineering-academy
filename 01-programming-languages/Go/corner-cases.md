# Go Corner Cases

## Goroutine Leaks

Goroutines that block on a channel or mutex forever are leaked. They consume memory and are never garbage collected. A common cause is writing to a channel with no receiver or reading from a channel with no sender.

Always ensure goroutines can exit. Use `context.Context` for cancellation or select statements with a done channel.

```go
func leak() {
    ch := make(chan int)
    go func() {
        ch <- 1 // blocks forever, no receiver
    }()
}
```

## Channel Deadlocks

A deadlock occurs when all goroutines are blocked waiting on each other. A single goroutine sending on an unbuffered channel with no receiver deadlocks immediately.

Buffered channels deadlock when full and no receiver, or when empty and no sender. The Go runtime detects some deadlocks but not all, especially those involving multiple goroutines.

## Nil Slice vs Empty Slice

A nil slice has no underlying array. An empty slice has an allocated but zero-length array. They behave the same in most operations, but JSON marshaling differs: nil slices marshal to `null`, empty slices marshal to `[]`.

```go
var s []int         // nil
s2 := []int{}       // empty, not nil
s3 := make([]int, 0) // empty, not nil
```

Use `var s []T` when you want null in JSON. Use `s := []T{}` or `make([]T, 0)` when you want an empty array.

## Map Race Conditions

Concurrent map reads and writes cause a runtime panic. Go maps are not safe for concurrent use. Use `sync.RWMutex` or `sync.Map` for concurrent access.

The race detector (`-race` flag) catches these at runtime but not at compile time.

## Interface nil陷阱

An interface value is nil only if both its type and value are nil. A common mistake is returning a concrete nil pointer as an interface, which results in a non-nil interface:

```go
var p *MyError = nil
var err error = p // err is NOT nil!
```

The interface has type `*MyError` and value `nil`, so `err == nil` is false. Always return `nil` directly as the interface type.

## Range Over Strings

`range` over a string iterates over runes (Unicode code points), not bytes. The index may not match the byte offset for multi-byte characters. Use `for i := 0; i < len(s); i++` for byte-level iteration.

Range over a slice returns a copy of the element. Modifying the range variable does not affect the slice.

## String and Byte Slice Conversion

Converting a string to a byte slice allocates a new copy. This can be expensive in hot paths. Use `unsafe.Pointer` for zero-copy conversion when the string will not be mutated.

Converting a byte slice to a string also allocates unless used in a map lookup or string comparison.

## Deferred Function Arguments

Deferred function arguments are evaluated immediately, not when the defer executes. This is a common source of bugs when deferring in loops.

```go
for i := 0; i < 3; i++ {
    defer fmt.Println(i) // prints 2, 1, 0
}
```

Use a closure to capture the current value: `defer func(i int) { fmt.Println(i) }(i)`.

## Error Handling Patterns

Go does not have exceptions. Errors are values returned and checked explicitly. Ignoring errors with `_` can hide failures.

The `errors.Is` and `errors.As` functions should be used for error comparison instead of `==`, because errors can be wrapped.

`fmt.Errorf` with `%w` creates wrapped errors. Unwrapping with `errors.Unwrap` only goes one level. Use `errors.Is` or `errors.As` to traverse the chain.

## Switch Statement Fall-through

Go switch statements do not fall through by default. Each case is independent. Use `fallthrough` keyword to force fall-through to the next case, which is rarely needed.

Type switches cannot use `fallthrough`.

## Struct Embedding and Method Sets

Embedded types promote their methods, but the promoted method operates on the embedded value, not the outer struct. If the embedded field is a pointer, the method receives the pointer.

Interface satisfaction is checked at compile time. A struct implementing all methods of an interface satisfies it implicitly, even without declaring it.

## Zero Values

All types have zero values: `0` for integers, `""` for strings, `false` for booleans, `nil` for pointers, slices, maps, channels, and interfaces. Zero values are useful for initialization but can mask bugs when checking for "unset" states.

A nil pointer dereference causes a runtime panic, not a compile-time error.

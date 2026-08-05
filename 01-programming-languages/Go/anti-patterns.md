# Go Anti-Patterns

## 1. Ignoring Errors
**Description:** Discarding error returns with `_` without checking.

**Why it's bad:** Hides failures, makes debugging difficult, can cause silent data corruption or crashes.

**Example (bad code):**
```go
result, _ := someOperation()
data, _ := ioutil.ReadFile("file.txt")
```

**Better approach:** Always check and handle errors:
```go
result, err := someOperation()
if err != nil {
    log.Printf("operation failed: %v", err)
    return err
}
```

**Impact:** Prevents silent failures, makes code more reliable and debuggable.

---

## 2. Goroutine Leaks
**Description:** Starting goroutines without ensuring they can be stopped or will terminate.

**Why it's bad:** Leaked goroutines consume memory and can cause resource exhaustion.

**Example (bad code):**
```go
func leakyFunction() {
    go func() {
        for {
            // runs forever with no way to stop
        }
    }()
}
```

**Better approach:** Use context or channels for cancellation:
```go
func properFunction(ctx context.Context) {
    go func() {
        for {
            select {
            case <-ctx.Done():
                return
            default:
                // do work
            }
        }
    }()
}
```

**Impact:** Prevents resource leaks, enables graceful shutdown.

---

## 3. Interface{} Abuse
**Description:** Overusing empty interfaces to avoid proper typing.

**Why it's bad:** Loses type safety, requires type assertions, makes code harder to understand.

**Example (bad code):**
```go
func process(data interface{}) error {
    value := data.(string)
    // ...
}
```

**Better approach:** Use specific types or generics (Go 1.18+):
```go
func process(data string) error {
    // ...
}
```

**Impact:** Type safety, better documentation, compile-time error checking.

---

## 4. String Concatenation in Loops
**Description:** Using `+` operator to build strings in loops.

**Why it's bad:** Creates new string objects each iteration, O(n^2) performance.

**Example (bad code):**
```go
result := ""
for _, item := range items {
    result += item + ", "
}
```

**Better approach:** Use strings.Builder:
```go
var builder strings.Builder
for _, item := range items {
    builder.WriteString(item)
    builder.WriteString(", ")
}
```

**Impact:** O(n) performance, reduced memory allocation.

---

## 5. Not Using defer for Cleanup
**Description:** Forgetting to use defer for resource cleanup or cleanup in wrong order.

**Why it's bad:** Can cause resource leaks or cleanup in wrong order.

**Example (bad code):**
```go
func process() {
    f, _ := os.Open("file.txt")
    // use file
    f.Close()
    // if panic occurs, Close() never called
}
```

**Better approach:** Use defer immediately after resource acquisition:
```go
func process() error {
    f, err := os.Open("file.txt")
    if err != nil {
        return err
    }
    defer f.Close()
    // use file
}
```

**Impact:** Ensures cleanup even on panics, cleaner code structure.

---

## 6. Exporting Everything
**Description:** Making all types and functions exported (capitalized).

**Why it's bad:** Exposes internal implementation details, makes API surface larger than necessary.

**Example (bad code):**
```go
type InternalData struct {
    Field1 string
    Field2 int
}

func InternalHelper() {}
```

**Better approach:** Keep unexported what should be internal:
```go
type internalData struct {
    field1 string
    field2 int
}

func internalHelper() {}
```

**Impact:** Smaller API surface, better encapsulation, easier refactoring.

---

## 7. Not Handling context.Context Properly
**Description:** Not passing context through function chains or ignoring context cancellation.

**Why it's bad:** Prevents graceful shutdown, makes cancellation impossible.

**Example (bad code):**
```go
func longRunningTask() {
    for {
        // no way to cancel
    }
}
```

**Better approach:** Accept and respect context:
```go
func longRunningTask(ctx context.Context) error {
    for {
        select {
        case <-ctx.Done():
            return ctx.Err()
        default:
            // do work
        }
    }
}
```

**Impact:** Enables graceful shutdown, proper timeout handling.

---

## 8. Panic in Library Code
**Description:** Using panic for error handling in library code.

**Why it's bad:** Callers cannot recover from panics in their goroutines, makes code unstable.

**Example (bad code):**
```go
func libraryFunction() {
    if err != nil {
        panic(err)
    }
}
```

**Better approach:** Return errors:
```go
func libraryFunction() error {
    if err != nil {
        return fmt.Errorf("operation failed: %w", err)
    }
    return nil
}
```

**Impact:** Allows callers to handle errors appropriately, more stable code.

---

## 9. Not Using iota for Enums
**Description:** Manually defining enum constants instead of using iota.

**Why it's bad:** Error-prone, harder to maintain, can lead to incorrect values.

**Example (bad code):**
```go
const (
    StatusPending = 0
    StatusActive  = 1
    StatusDone    = 2
)
```

**Better approach:** Use iota:
```go
const (
    StatusPending = iota
    StatusActive
    StatusDone
)
```

**Impact:** Less error-prone, easier to maintain, auto-numbered constants.

---

## 10. Ignoring Slices Gotchas
**Description:** Not understanding slice behavior with underlying arrays.

**Why it's bad:** Can cause unexpected memory retention or data corruption.

**Example (bad code):**
```go
func getFirst(items []string) string {
    return items[0] // panics if empty
}
```

**Better approach:** Check length and handle properly:
```go
func getFirst(items []string) (string, bool) {
    if len(items) == 0 {
        return "", false
    }
    return items[0], true
}
```

**Impact:** Prevents panics, clearer function contracts.

---

## 11. Not Using gofmt/goimports
**Description:** Not using standard formatting tools.

**Why it's bad:** Inconsistent code style across team, merge conflicts from formatting differences.

**Example (bad code):**
```go
func myFunc( ) {
    if err!=nil{
        // inconsistent formatting
    }
}
```

**Better approach:** Use gofmt/goimports and pre-commit hooks.

**Impact:** Consistent style, fewer merge conflicts, better readability.

---

## 12. Channel Misuse
**Description:** Using channels incorrectly (buffered vs unbuffered, closing from wrong side).

**Why it's bad:** Can cause deadlocks, race conditions, or panics.

**Example (bad code):**
```go
ch := make(chan int)
go func() {
    ch <- 42
}()
close(ch) // closing before send completes
```

**Better approach:** Understand channel semantics:
```go
ch := make(chan int, 1) // buffered
go func() {
    ch <- 42
}()
value := <-ch
```

**Impact:** Prevents deadlocks and race conditions, correct synchronization.
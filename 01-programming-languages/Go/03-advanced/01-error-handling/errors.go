package main

import (
    "errors"
    "fmt"
    "os"
)

// Custom error types
type ValidationError struct {
    Field   string
    Message string
}

func (e *ValidationError) Error() string {
    return fmt.Sprintf("validation error: %s - %s", e.Field, e.Message)
}

type NotFoundError struct {
    Resource string
    ID       int
}

func (e *NotFoundError) Error() string {
    return fmt.Sprintf("%s with ID %d not found", e.Resource, e.ID)
}

// Sentinel errors
var (
    ErrUnauthorized = errors.New("unauthorized access")
    ErrForbidden    = errors.New("forbidden")
)

func main() {
    // 1. Basic error handling
    fmt.Println("=== Basic Error Handling ===")

    result, err := divide(10, 0)
    if err != nil {
        fmt.Println("Error:", err)
    } else {
        fmt.Printf("Result: %.2f\n", result)
    }

    result, err = divide(10, 3)
    if err != nil {
        fmt.Println("Error:", err)
    } else {
        fmt.Printf("Result: %.2f\n", result)
    }

    // 2. Custom errors
    fmt.Println("\n=== Custom Errors ===")

    err = validateAge(-5)
    if err != nil {
        fmt.Println("Error:", err)
    }

    err = validateAge(25)
    if err != nil {
        fmt.Println("Error:", err)
    } else {
        fmt.Println("Age valid")
    }

    // 3. Error wrapping
    fmt.Println("\n=== Error Wrapping ===")

    err = readFile("nonexistent.txt")
    if err != nil {
        fmt.Println("Wrapped error:", err)
    }

    // 4. errors.Is
    fmt.Println("\n=== errors.Is ===")

    err = checkAuth(false)
    if errors.Is(err, ErrUnauthorized) {
        fmt.Println("Got unauthorized error")
    }

    // 5. errors.As
    fmt.Println("\n=== errors.As ===")

    err = findUser(999)
    var notFound *NotFoundError
    if errors.As(err, &notFound) {
        fmt.Printf("Not found: %s ID %d\n", notFound.Resource, notFound.ID)
    }

    // 6. Multiple errors
    fmt.Println("\n=== Multiple Errors ===")

    errs := validateUser("", -1, "bad@email")
    for _, e := range errs {
        fmt.Println("Error:", e)
    }
}

func divide(a, b float64) (float64, error) {
    if b == 0 {
        return 0, errors.New("division by zero")
    }
    return a / b, nil
}

func validateAge(age int) error {
    if age < 0 {
        return &ValidationError{Field: "age", Message: "must be non-negative"}
    }
    if age > 150 {
        return &ValidationError{Field: "age", Message: "must be realistic"}
    }
    return nil
}

func readFile(path string) error {
    _, err := os.Open(path)
    if err != nil {
        return fmt.Errorf("readFile: %w", err)
    }
    return nil
}

func checkAuth(authorized bool) error {
    if !authorized {
        return ErrUnauthorized
    }
    return nil
}

func findUser(id int) error {
    return &NotFoundError{Resource: "user", ID: id}
}

func validateUser(name string, age int, email string) []error {
    var errs []error
    if name == "" {
        errs = append(errs, &ValidationError{Field: "name", Message: "required"})
    }
    if age < 0 {
        errs = append(errs, &ValidationError{Field: "age", Message: "must be positive"})
    }
    return errs
}

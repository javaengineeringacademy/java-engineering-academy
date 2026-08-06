package main

import (
    "fmt"
    "sync"
    "time"
)

func main() {
    // 1. Basic goroutine
    fmt.Println("=== Basic Goroutine ===")

    go func() {
        fmt.Println("Hello from goroutine!")
    }()

    time.Sleep(100 * time.Millisecond) // Wait for goroutine

    // 2. Multiple goroutines with WaitGroup
    fmt.Println("\n=== WaitGroup ===")

    var wg sync.WaitGroup
    for i := 0; i < 5; i++ {
        wg.Add(1)
        go func(id int) {
            defer wg.Done()
            fmt.Printf("Goroutine %d starting\n", id)
            time.Sleep(time.Duration(id) * 50 * time.Millisecond)
            fmt.Printf("Goroutine %d finished\n", id)
        }(i)
    }
    wg.Wait()
    fmt.Println("All goroutines completed")

    // 3. Shared state problem (without sync)
    fmt.Println("\n=== Race Condition Demo ===")

    counter := 0
    var wg2 sync.WaitGroup
    for i := 0; i < 1000; i++ {
        wg2.Add(1)
        go func() {
            defer wg2.Done()
            temp := counter
            temp++
            counter = temp // Race condition!
        }()
    }
    wg2.Wait()
    fmt.Printf("Counter (unsafe): %d (expected: 1000)\n", counter)

    // 4. Worker pool pattern
    fmt.Println("\n=== Worker Pool ===")

    jobs := make(chan int, 10)
    results := make(chan int, 10)

    // Start workers
    var wg3 sync.WaitGroup
    for w := 1; w <= 3; w++ {
        wg3.Add(1)
        go func(worker int) {
            defer wg3.Done()
            for job := range jobs {
                result := job * job
                fmt.Printf("Worker %d: %d -> %d\n", worker, job, result)
                results <- result
            }
        }(w)
    }

    // Send jobs
    for j := 1; j <= 5; j++ {
        jobs <- j
    }
    close(jobs)

    // Wait for workers and close results
    go func() {
        wg3.Wait()
        close(results)
    }()

    // Collect results
    total := 0
    for r := range results {
        total += r
    }
    fmt.Printf("Total: %d\n", total)

    // 5. Goroutine leak prevention
    fmt.Println("\n=== Done Channel ===")

    done := make(chan bool)
    go func() {
        fmt.Println("Working...")
        time.Sleep(50 * time.Millisecond)
        fmt.Println("Done working")
        done <- true
    }()

    <-done // Wait for signal
    fmt.Println("Main continues")
}

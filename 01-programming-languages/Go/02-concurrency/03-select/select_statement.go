package main

import (
    "fmt"
    "math/rand"
    "time"
)

func main() {
    // 1. Basic select
    fmt.Println("=== Basic Select ===")

    ch1 := make(chan string)
    ch2 := make(chan string)

    go func() {
        time.Sleep(100 * time.Millisecond)
        ch1 <- "Message from ch1"
    }()

    go func() {
        time.Sleep(50 * time.Millisecond)
        ch2 <- "Message from ch2"
    }()

    for i := 0; i < 2; i++ {
        select {
        case msg := <-ch1:
            fmt.Println(msg)
        case msg := <-ch2:
            fmt.Println(msg)
        }
    }

    // 2. Timeout pattern
    fmt.Println("\n=== Timeout Pattern ===")

    ch := make(chan string)
    go func() {
        time.Sleep(200 * time.Millisecond)
        ch <- "Slow result"
    }()

    select {
    case result := <-ch:
        fmt.Println("Got:", result)
    case <-time.After(100 * time.Millisecond):
        fmt.Println("Timed out!")
    }

    // 3. Non-blocking select with default
    fmt.Println("\n=== Non-blocking Select ===")

    msgs := make(chan string)
    select {
    case msg := <-msgs:
        fmt.Println(msg)
    default:
        fmt.Println("No message available")
    }

    // 4. Done channel pattern
    fmt.Println("\n=== Done Channel ===")

    done := make(chan bool)
    go func() {
        fmt.Println("Working...")
        time.Sleep(100 * time.Millisecond)
        done <- true
    }()

    select {
    case <-done:
        fmt.Println("Work completed")
    case <-time.After(time.Second):
        fmt.Println("Timed out waiting")
    }

    // 5. Multiple channels with select
    fmt.Println("\n=== Multiple Channels ===")

    chA := make(chan int, 1)
    chB := make(chan int, 1)
    chC := make(chan int, 1)

    go func() {
        time.Sleep(time.Duration(rand.Intn(100)) * time.Millisecond)
        chA <- 1
    }()
    go func() {
        time.Sleep(time.Duration(rand.Intn(100)) * time.Millisecond)
        chB <- 2
    }()
    go func() {
        time.Sleep(time.Duration(rand.Intn(100)) * time.Millisecond)
        chC <- 3
    }()

    for i := 0; i < 3; i++ {
        select {
        case v := <-chA:
            fmt.Printf("Received %d from chA\n", v)
        case v := <-chB:
            fmt.Printf("Received %d from chB\n", v)
        case v := <-chC:
            fmt.Printf("Received %d from chC\n", v)
        }
    }

    // 6. Infinite loop with select
    fmt.Println("\n=== Polling Pattern ===")

    ticker := time.NewTicker(50 * time.Millisecond)
    counter := 0

    for {
        select {
        case <-ticker.C:
            counter++
            fmt.Printf("Tick %d\n", counter)
            if counter >= 3 {
                ticker.Stop()
                return
            }
        }
    }
}

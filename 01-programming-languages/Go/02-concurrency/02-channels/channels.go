package main

import (
    "fmt"
    "time"
)

func main() {
    // 1. Unbuffered channel
    fmt.Println("=== Unbuffered Channel ===")

    ch := make(chan string)
    go func() {
        time.Sleep(100 * time.Millisecond)
        ch <- "Hello from goroutine"
    }()
    msg := <-ch
    fmt.Println(msg)

    // 2. Buffered channel
    fmt.Println("\n=== Buffered Channel ===")

    buffered := make(chan int, 3)
    buffered <- 1
    buffered <- 2
    buffered <- 3
    // buffered <- 4 // Would block (full)

    fmt.Printf("Received: %d\n", <-buffered)
    fmt.Printf("Received: %d\n", <-buffered)
    fmt.Printf("Received: %d\n", <-buffered)

    // 3. Channel direction
    fmt.Println("\n=== Channel Direction ===")

    bidir := make(chan int, 1)
    go producer(bidir)
    consumer(bidir)

    // 4. Range over channel
    fmt.Println("\n=== Range over Channel ===")

    nums := make(chan int, 5)
    go func() {
        for i := 0; i < 5; i++ {
            nums <- i * 10
        }
        close(nums)
    }()

    for v := range nums {
        fmt.Printf("%d ", v)
    }
    fmt.Println()

    // 5. Close channel
    fmt.Println("\n=== Close Channel ===")

    data := make(chan string, 2)
    data <- "first"
    data <- "second"
    close(data)

    // Receive from closed channel
    fmt.Println(<-data)
    fmt.Println(<-data)
    v, ok := <-data
    fmt.Printf("Value: %v, OK: %t\n", v, ok)

    // 6. Fan-out pattern
    fmt.Println("\n=== Fan-out Pattern ===")

    input := make(chan int, 5)
    for i := 0; i < 5; i++ {
        input <- i
    }
    close(input)

    // Fan out to multiple goroutines
    c1 := make(chan string, 5)
    c2 := make(chan string, 5)

    go fanWorker(input, c1, "Worker 1")
    go fanWorker(input, c2, "Worker 2")

    time.Sleep(100 * time.Millisecond)
    close(c1)
    close(c2)

    for v := range c1 {
        fmt.Println(v)
    }
    for v := range c2 {
        fmt.Println(v)
    }
}

func producer(ch chan<- int) {
    ch <- 42
}

func consumer(ch <-chan int) {
    val := <-ch
    fmt.Printf("Received: %d\n", val)
}

func fanWorker(input <-chan int, output chan<- string, name string) {
    for v := range input {
        output <- fmt.Sprintf("%s processed %d", name, v)
    }
}

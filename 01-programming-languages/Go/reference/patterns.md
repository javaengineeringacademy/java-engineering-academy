# Go Design Patterns

## Creational Patterns

### Singleton (using sync.Once)
```go
var (
    instance *Database
    once     sync.Once
)

func GetInstance() *Database {
    once.Do(func() {
        instance = &Database{}
    })
    return instance
}
```

### Factory
```go
func NewTransport(url string) (Transport, error) {
    if strings.HasPrefix(url, "https") {
        return &HTTPSTransport{}, nil
    }
    return &HTTPTransport{}, nil
}
```

## Structural Patterns

### Adapter
```go
type Logger interface {
    Log(msg string)
}

type StdLogger struct{}

func (l *StdLogger) Log(msg string) {
    fmt.Println(msg)
}
```

### Decorator
```go
func WithLogging(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        log.Println("Request:", r.URL.Path)
        next.ServeHTTP(w, r)
    })
}
```

## Behavioral Patterns

### Observer (using channels)
```go
type Event struct {
    Type string
    Data interface{}
}

func Subscribe(ch chan<- Event) {
    // Register subscriber
}
```

### Strategy
```go
type Sorter func([]int) []int

func Sort(data []int, strategy Sorter) []int {
    return strategy(data)
}
```

### Pipeline
```go
func pipeline(input <-chan int) <-chan int {
    out := make(chan int)
    go func() {
        for v := range input {
            out <- v * 2
        }
        close(out)
    }()
    return out
}
```

## Concurrency Patterns

### Fan-out/Fan-in
```go
func fanOut(input <-chan int, workers int) []<-chan int {
    channels := make([]<-chan int, workers)
    for i := 0; i < workers; i++ {
        channels[i] = worker(input)
    }
    return channels
}
```

### Worker Pool
```go
func workerPool(jobs <-chan Job, numWorkers int) <-chan Result {
    results := make(chan Result)
    var wg sync.WaitGroup
    for i := 0; i < numWorkers; i++ {
        wg.Add(1)
        go func() {
            defer wg.Done()
            for job := range jobs {
                results <- process(job)
            }
        }()
    }
    go func() {
        wg.Wait()
        close(results)
    }()
    return results
}
```

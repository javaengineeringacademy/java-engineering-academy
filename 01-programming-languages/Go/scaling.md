# Go Scaling

## Goroutine Pools

```go
type WorkerPool struct {
    tasks    chan func()
    workers  int
    wg       sync.WaitGroup
}

func NewWorkerPool(workers int) *WorkerPool {
    return &WorkerPool{
        tasks:   make(chan func(), 100),
        workers: workers,
    }
}

func (p *WorkerPool) Start() {
    for i := 0; i < p.workers; i++ {
        p.wg.Add(1)
        go func() {
            defer p.wg.Done()
            for task := range p.tasks {
                task()
            }
        }()
    }
}

func (p *WorkerPool) Submit(task func()) {
    p.tasks <- task
}

func (p *WorkerPool) Stop() {
    close(p.tasks)
    p.wg.Wait()
}
```

## Connection Pooling

```go
// HTTP client pool
transport := &http.Transport{
    MaxIdleConns:        100,
    MaxIdleConnsPerHost: 10,
    IdleConnTimeout:     90 * time.Second,
    TLSHandshakeTimeout: 10 * time.Second,
}

client := &http.Client{Transport: transport}

// Database pool
db.SetMaxOpenConns(25)
db.SetMaxIdleConns(10)
db.SetConnMaxLifetime(5 * time.Minute)
```

## Rate Limiting

```go
import "golang.org/x/time/rate"

// Global rate limiter
var globalLimiter = rate.NewLimiter(rate.Limit(1000), 2000)

// Per-IP rate limiter
type IPRateLimiter struct {
    mu       sync.RWMutex
    limiters map[string]*rate.Limiter
    rate     rate.Limit
    burst    int
}

func (rl *IPRateLimiter) GetLimiter(ip string) *rate.Limiter {
    rl.mu.RLock()
    limiter, exists := rl.limiters[ip]
    rl.mu.RUnlock()

    if !exists {
        rl.mu.Lock()
        limiter = rate.NewLimiter(rl.rate, rl.burst)
        rl.limiters[ip] = limiter
        rl.mu.Unlock()
    }

    return limiter
}
```

## Work Distribution

```go
func distributeWork(items []Item, workers int) [][]Item {
    chunkSize := (len(items) + workers - 1) / workers
    chunks := make([][]Item, 0, workers)

    for i := 0; i < len(items); i += chunkSize {
        end := i + chunkSize
        if end > len(items) {
            end = len(items)
        }
        chunks = append(chunks, items[i:end])
    }

    return chunks
}
```

## Caching

```go
type Cache struct {
    mu    sync.RWMutex
    items map[string]CacheEntry
}

type CacheEntry struct {
    Value     interface{}
    ExpiresAt time.Time
}

func (c *Cache) Get(key string) (interface{}, bool) {
    c.mu.RLock()
    defer c.mu.RUnlock()

    entry, ok := c.items[key]
    if !ok || time.Now().After(entry.ExpiresAt) {
        return nil, false
    }
    return entry.Value, true
}
```

## Circuit Breaker

```go
type CircuitBreaker struct {
    failures    int
    threshold   int
    resetTimer  time.Duration
    state       State
    lastFailure time.Time
}

func (cb *CircuitBreaker) Call(fn func() error) error {
    if cb.state == Open {
        if time.Since(cb.lastFailure) > cb.resetTimer {
            cb.state = HalfOpen
        } else {
            return errors.New("circuit breaker open")
        }
    }

    err := fn()
    if err != nil {
        cb.failures++
        cb.lastFailure = time.Now()
        if cb.failures >= cb.threshold {
            cb.state = Open
        }
        return err
    }

    cb.failures = 0
    cb.state = Closed
    return nil
}
```

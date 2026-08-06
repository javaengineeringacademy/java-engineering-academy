# Sync Package in Go

The `sync` package provides synchronization primitives for concurrent access.

## Mutex

```go
var mu sync.Mutex
var counter int

func increment() {
    mu.Lock()
    defer mu.Unlock()
    counter++
}
```

## RWMutex

```go
var rwMu sync.RWMutex

// Multiple readers allowed
func read() {
    rwMu.RLock()
    defer rwMu.RUnlock()
    // read data
}

// Only one writer
func write() {
    rwMu.Lock()
    defer rwMu.Unlock()
    // write data
}
```

## sync.Once

```go
var once sync.Once
var instance *Singleton

func GetInstance() *Singleton {
    once.Do(func() {
        instance = &Singleton{}
    })
    return instance
}
```

## Key Points
- Always unlock in defer or same function
- RWMutex: multiple readers, single writer
- Once guarantees single execution
- Mutex is NOT reentrant
- Use defer for safe unlocking

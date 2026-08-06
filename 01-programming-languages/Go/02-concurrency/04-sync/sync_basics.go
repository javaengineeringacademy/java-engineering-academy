package main

import (
    "fmt"
    "sync"
    "time"
)

// 1. Mutex - Safe counter
type SafeCounter struct {
    mu    sync.Mutex
    count int
}

func (c *SafeCounter) Increment() {
    c.mu.Lock()
    defer c.mu.Unlock()
    c.count++
}

func (c *SafeCounter) Get() int {
    c.mu.Lock()
    defer c.mu.Unlock()
    return c.count
}

// 2. RWMutex - Read-write lock
type Cache struct {
    mu   sync.RWMutex
    data map[string]string
}

func NewCache() *Cache {
    return &Cache{data: make(map[string]string)}
}

func (c *Cache) Get(key string) (string, bool) {
    c.mu.RLock()
    defer c.mu.RUnlock()
    val, ok := c.data[key]
    return val, ok
}

func (c *Cache) Set(key, value string) {
    c.mu.Lock()
    defer c.mu.Unlock()
    c.data[key] = value
}

// 3. sync.Once - Singleton
type Database struct {
    conn string
}

var (
    dbInstance *Database
    dbOnce     sync.Once
)

func GetDB() *Database {
    dbOnce.Do(func() {
        fmt.Println("Creating database connection...")
        time.Sleep(100 * time.Millisecond)
        dbInstance = &Database{conn: "postgres://localhost/mydb"}
    })
    return dbInstance
}

func main() {
    // 1. Mutex demo
    fmt.Println("=== Mutex ===")

    counter := &SafeCounter{}
    var wg sync.WaitGroup

    for i := 0; i < 1000; i++ {
        wg.Add(1)
        go func() {
            defer wg.Done()
            counter.Increment()
        }()
    }

    wg.Wait()
    fmt.Printf("Counter: %d (expected: 1000)\n", counter.Get())

    // 2. RWMutex demo
    fmt.Println("\n=== RWMutex ===")

    cache := NewCache()
    cache.Set("name", "Go")

    // Concurrent reads
    var wg2 sync.WaitGroup
    for i := 0; i < 5; i++ {
        wg2.Add(1)
        go func(id int) {
            defer wg2.Done()
            val, _ := cache.Get("name")
            fmt.Printf("Reader %d: %s\n", id, val)
        }(i)
    }

    // Concurrent write
    wg2.Add(1)
    go func() {
        defer wg2.Done()
        cache.Set("version", "1.21")
        fmt.Println("Writer: Set version")
    }()

    wg2.Wait()

    // 3. sync.Once demo
    fmt.Println("\n=== sync.Once ===")

    var wg3 sync.WaitGroup
    for i := 0; i < 5; i++ {
        wg3.Add(1)
        go func(id int) {
            defer wg3.Done()
            db := GetDB()
            fmt.Printf("Goroutine %d: %s\n", id, db.conn)
        }(i)
    }
    wg3.Wait()

    // 4. sync.Map (bonus)
    fmt.Println("\n=== sync.Map ===")

    var syncMap sync.Map
    syncMap.Store("key1", "value1")
    syncMap.Store("key2", "value2")

    if val, ok := syncMap.Load("key1"); ok {
        fmt.Printf("Loaded: %v\n", val)
    }

    syncMap.Range(func(key, value interface{}) bool {
        fmt.Printf("Key: %v, Value: %v\n", key, value)
        return true
    })
}

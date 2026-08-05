# Singleton Pattern

## Overview

Singleton ensures a class has only one instance and provides a global point of access. Go's `sync.Once` provides a thread-safe implementation.

## When to Use

- Database connection pools
- Configuration managers
- Logger and cache instances

## Go Implementation

```go
var (
    instance *Database
    once     sync.Once
)

func GetInstance(dsn string) *Database {
    once.Do(func() {
        instance = &Database{DSN: dsn}
    })
    return instance
}
```

## Go-Idiomatic Alternative

```go
var instance = &Database{DSN: "default"}

func GetInstance() *Database { return instance }
```

## Real-World Example

```go
func GetConfig() *Config {
    configOnce.Do(func() {
        config = &Config{DBHost: os.Getenv("DB_HOST"), DBPort: 5432}
    })
    return config
}
```

## Best Practices

- Use `sync.Once` over mutex-based initialization
- Prefer dependency injection for testability
- Avoid global state when possible

## Interview Questions

1. Why is `sync.Once` preferred over mutex for Singleton?
2. How would you make the Singleton testable?
3. Can you have a Singleton per goroutine? How?
4. What problems does the Singleton pattern introduce?
5. How does `sync.Once` handle concurrent initialization?

## References

- Go Blog: "sync/atomic, sync.Once"
- Go Dev: sync.Once documentation
- "Concurrency in Go" - Chapter 5

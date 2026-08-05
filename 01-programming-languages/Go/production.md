# Go Production

## Graceful Shutdown

```go
import (
    "context"
    "log"
    "net/http"
    "os"
    "os/signal"
    "syscall"
    "time"
)

func main() {
    srv := &http.Server{Addr: ":8080"}

    go func() {
        if err := srv.ListenAndServe(); err != http.ErrServerClosed {
            log.Fatalf("listen: %s\n", err)
        }
    }()

    quit := make(chan os.Signal, 1)
    signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
    <-quit

    ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
    defer cancel()

    if err := srv.Shutdown(ctx); err != nil {
        log.Fatalf("server shutdown: %s\n", err)
    }
}
```

## Health Checks

Liveness and readiness probes:

```go
func liveness(w http.ResponseWriter, r *http.Request) {
    w.WriteHeader(http.StatusOK)
}

func readiness(w http.ResponseWriter, r *http.Request) {
    if err := db.Ping(); err != nil {
        http.Error(w, "unhealthy", http.StatusServiceUnavailable)
        return
    }
    w.WriteHeader(http.StatusOK)
}
```

## Structured Logging

```go
import "log/slog"

logger := slog.New(slog.NewJSONHandler(os.Stdout, nil))

logger.Info("user created",
    "user_id", 123,
    "email", "user@example.com",
    "latency_ms", 45,
)
```

Output:

```json
{"time":"2024-01-01T00:00:00Z","level":"INFO","msg":"user created","user_id":123,"email":"user@example.com","latency_ms":45}
```

## Configuration Management

```go
import "os"

config := struct {
    Port    string
    DBURL   string
    LogLvl  string
}{
    Port:   getEnv("PORT", "8080"),
    DBURL:  getEnv("DATABASE_URL", ""),
    LogLvl: getEnv("LOG_LEVEL", "info"),
}

func getEnv(key, fallback string) string {
    if value := os.Getenv(key); value != "" {
        return value
    }
    return fallback
}
```

## Rate Limiting

```go
import "golang.org/x/time/rate"

var limiter = rate.NewLimiter(rate.Limit(100), 200)

func rateLimit(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        if !limiter.Allow() {
            http.Error(w, "rate limited", http.StatusTooManyRequests)
            return
        }
        next.ServeHTTP(w, r)
    })
}
```

## Connection Pooling

```go
import "database/sql"

db, _ := sql.Open("postgres", dsn)
db.SetMaxOpenConns(25)
db.SetMaxIdleConns(10)
db.SetConnMaxLifetime(5 * time.Minute)
db.SetConnMaxIdleTime(1 * time.Minute)
```

## Middleware Chain

```go
func Chain(h http.Handler, middlewares ...func(http.Handler) http.Handler) http.Handler {
    for i := len(middlewares) - 1; i >= 0; i-- {
        h = middlewares[i](h)
    }
    return h
}
```

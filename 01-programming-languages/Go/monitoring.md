# Go Monitoring

## Prometheus client_golang

```go
import (
    "github.com/prometheus/client_golang/prometheus"
    "github.com/prometheus/client_golang/prometheus/promhttp"
)

var (
    requestsTotal = prometheus.NewCounterVec(
        prometheus.CounterOpts{
            Name: "http_requests_total",
            Help: "Total HTTP requests",
        },
        []string{"method", "path", "status"},
    )
    requestDuration = prometheus.NewHistogramVec(
        prometheus.HistogramOpts{
            Name:    "http_request_duration_seconds",
            Help:    "Request duration",
            Buckets: prometheus.DefBuckets,
        },
        []string{"method", "path"},
    )
)

func init() {
    prometheus.MustRegister(requestsTotal)
    prometheus.MustRegister(requestDuration)
}

func metricsHandler() http.Handler {
    return promhttp.Handler()
}
```

## OpenTelemetry

```go
import (
    "go.opentelemetry.io/otel"
    "go.opentelemetry.io/otel/sdk/trace"
    "go.opentelemetry.io/otel/exporters/stdout"
)

func initTracer() (*trace.TracerProvider, error) {
    exporter, err := stdout.New(stdout.WithPrettyPrint())
    if err != nil {
        return nil, err
    }
    tp := trace.NewTracerProvider(
        trace.WithBatcher(exporter),
        trace.WithSampler(trace.AlwaysSample()),
    )
    otel.SetTracerProvider(tp)
    return tp, nil
}
```

## expvar

```go
import "expvar"

var (
    serverStats = expvar.NewMap("server")
    activeConns = expvar.NewInt("active_connections")
)

func init() {
    serverStats.Add("requests", 1)
    activeConns.Add(1)
}
```

Expose via HTTP:

```go
http.HandleFunc("/debug/vars", expvarHandler)
```

## Custom Metrics

```go
type Metrics struct {
    Latency *prometheus.HistogramVec
    Errors  *prometheus.CounterVec
}

func NewMetrics() *Metrics {
    return &Metrics{
        Latency: prometheus.NewHistogramVec(
            prometheus.HistogramOpts{
                Name:    "operation_duration_seconds",
                Buckets: prometheus.LinearBuckets(0.01, 0.01, 20),
            },
            []string{"operation"},
        ),
        Errors: prometheus.NewCounterVec(
            prometheus.CounterOpts{
                Name: "operation_errors_total",
            },
            []string{"operation", "type"},
        ),
    }
}
```

## Logging Integration

```go
import "log/slog"

slog.Info("request processed",
    "method", "GET",
    "path", "/api/users",
    "status", 200,
    "duration_ms", 45,
)
```

Structured logging with slog provides machine-readable output for monitoring systems.

## Health Checks

```go
func healthHandler(w http.ResponseWriter, r *http.Request) {
    if err := db.Ping(); err != nil {
        http.Error(w, "unhealthy", http.StatusServiceUnavailable)
        return
    }
    w.WriteHeader(http.StatusOK)
    w.Write([]byte("ok"))
}
```

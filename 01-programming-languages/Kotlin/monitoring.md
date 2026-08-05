# Kotlin Monitoring

## Logging

Use SLF4J or Kotlin logging for application monitoring.

```kotlin
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(MyClass::class.java)

fun processData() {
    logger.info("Processing started")
    try {
        val result = transform(input)
        logger.debug("Transform completed: $result")
    } catch (e: Exception) {
        logger.error("Processing failed", e)
        throw e
    }
}
```

## Structured Logging

```kotlin
data class LogEntry(
    val timestamp: String,
    val level: String,
    val message: String,
    val userId: String? = null,
    val duration: Long? = null
)

fun logRequest(entry: LogEntry) {
    println(Json.encodeToString(entry))
}
```

## Metrics Collection

Track application performance with Micrometer.

```kotlin
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer

class MetricsService(private val registry: MeterRegistry) {
    private val requestTimer = Timer.builder("http.requests")
        .description("HTTP request duration")
        .register(registry)

    fun <T> measureRequest(block: () -> T): T {
        val sample = Timer.start(registry)
        return try {
            block()
        } finally {
            sample.stop(requestTimer)
        }
    }
}
```

## Health Checks

Implement health endpoints for monitoring.

```kotlin
data class HealthStatus(
    val status: String,
    val checks: Map<String, String>
)

fun checkHealth(): HealthStatus {
    val checks = mutableMapOf<String, String>()

    checks["database"] = try {
        database.ping(); "UP"
    } catch (e: Exception) { "DOWN" }

    checks["redis"] = try {
        redis.ping(); "UP"
    } catch (e: Exception) { "DOWN" }

    val status = if (checks.all { it.value == "UP" }) "UP" else "DOWN"
    return HealthStatus(status, checks)
}
```

## Distributed Tracing

Track requests across services with OpenTelemetry.

```kotlin
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer

class TracingService(private val tracer: Tracer) {
    fun traceOperation(name: String, block: () -> Unit) {
        val span = tracer.spanBuilder(name).startSpan()
        val scope = span.makeCurrent()
        try {
            block()
        } finally {
            scope.close()
            span.end()
        }
    }
}
```

## Error Tracking

Capture and report errors to services like Sentry.

```kotlin
import io.sentry.Sentry

fun initSentry() {
    Sentry.init { options ->
        options.dsn = "your-sentry-dsn"
        options.tracesSampleRate = 1.0
    }
}

fun reportError(e: Throwable) {
    Sentry.captureException(e)
}
```

## Log Aggregation

- Use structured logging for ELK Stack
- Forward logs to Datadog or Splunk
- Set up alerts on error rate spikes
- Monitor application latency and throughput

# Production Quiz

## Question 1 (Scenario)
Your production service is returning HTTP 503 errors. JFR shows no GC pauses, CPU is at 10%, and memory is stable. Thread dumps show all threads waiting on a database connection pool. What is the root cause?

A) JVM needs more memory
B) Connection pool exhaustion — the pool size is too small for the workload
C) GC is paused — all threads blocked
D) The database is down

**Answer: B**
**Explanation:** Thread dumps showing threads waiting for database connections indicate the connection pool is exhausted. With 10% CPU and stable memory, the JVM itself is fine. The pool has no available connections, so new requests queue up. Solutions: increase pool size, optimize slow queries, or add connection timeout handling.

---

## Question 2 (Code Output)
What is the output of this circuit breaker code?

```java
CircuitBreaker cb = new CircuitBreaker(3, 2000);
for (int i = 0; i < 7; i++) {
    String result = cb.execute(
        () -> { throw new RuntimeException("fail"); },
        () -> "fallback"
    );
    System.out.println(i + ": " + result + " [" + cb.getState() + "]");
}
```

A) All "fallback [OPEN]"
B) 0-2: "fallback [CLOSED]", 3-6: "fallback [OPEN]"
C) 0-2: "fallback [CLOSED]", 3: "fallback [OPEN]", 4-6: "fallback [OPEN]"
D) Compilation error

**Answer: C**
**Explanation:** First 3 calls (0, 1, 2) fail but state stays CLOSED (failure count < 3). Call 3 triggers OPEN (failureCount reaches 3). Calls 4-6 are immediately rejected as OPEN without calling the supplier. After 2000ms timeout, state transitions to HALF_OPEN. But since calls happen immediately, they all see OPEN.

---

## Question 3 (Architecture)
Which production pattern is best for preventing cascading failures across microservices?

A) Retry with exponential backoff
B) Circuit breaker with fallback responses
C) Bulkhead isolation with timeout
D) All of the above, applied together

**Answer: D**
**Explanation:** Production resilience requires layered patterns: Circuit breakers stop calling failing services. Bulkheads isolate failures to prevent resource exhaustion. Retries with backoff handle transient failures. Fallbacks provide degraded functionality. Together, they create defense-in-depth against cascading failures.

---

## Question 4 (Code Output)
What does this health check system report?

```java
HealthCheckRegistry registry = new HealthCheckRegistry();
registry.register("db", () -> true);
registry.register("cache", () -> false);
registry.register("queue", () -> true);

Map<String, Object> report = registry.healthReport();
System.out.println(report.get("overall"));
System.out.println(report.size());
```

A) UP, 3
B) DOWN, 3
C) DOWN, 4
D) DEGRADED, 4

**Answer: C**
**Explanation:** The health report includes individual check results (db, cache, queue = 3) plus the "overall" status = 4 entries. Since "cache" is unhealthy, overall status is DOWN. The report has 4 entries: 3 service checks + 1 overall status.

---

## Question 5 (Scenario)
A production JVM experiences intermittent pauses of 50-100ms under load. GC logs show occasional "Allocation Failure" in the young generation. Which fix is most appropriate?

A) Increase `-Xmx` to give more heap
B) Increase young generation size with `-XX:NewRatio=3`
C) Reduce object allocation rate in the application
D) Switch to Serial GC

**Answer: C**
**Explanation:** "Allocation Failure" means the young generation is full and needs collection. Increasing `-Xmx` doesn't help young generation directly. Increasing NewRatio helps but doesn't address the root cause. Serial GC would be worse. The root cause is too many short-lived objects being allocated. Reducing allocation rate (object reuse, pooling, primitive types) is the permanent fix.

---

## Question 6 (Code Output)
What is the output of this rate limiter?

```java
TokenBucket limiter = new TokenBucket(3, 1.0); // 3 tokens, refill 1/sec
for (int i = 0; i < 6; i++) {
    boolean allowed = limiter.tryAcquire();
    System.out.print(allowed + " ");
    if (i == 2) Thread.sleep(2000); // let 2 tokens refill
}
```

A) true true false true true true
B) true true true false true true
C) true true true false false false
D) true true false false false

**Answer: B**
**Explanation:** First 3 calls succeed (3 tokens available). 4th call fails (0 tokens). After 2 seconds, ~2 tokens refill. 5th call succeeds (uses 1 token, ~1 left). 6th call succeeds (uses last token). Pattern: T T T F T T.

---

## Question 7 (Architecture)
When implementing a graceful shutdown in a Spring Boot application, what is the correct order?

A) Stop accepting new requests → drain in-flight requests → close connections → release resources
B) Kill the JVM immediately → let load balancer handle retries
C) Close database first → stop accepting requests → flush logs
D) Flush logs → close database → stop accepting requests

**Answer: A**
**Explanation:** Graceful shutdown order: (1) Stop accepting new requests (deregister from load balancer), (2) Wait for in-flight requests to complete (with timeout), (3) Close application resources (database connections, caches), (4) Release external resources (file handles, sockets). This prevents request drops and data corruption.

---

## Question 8 (Production)
What is the purpose of a bulkhead pattern in production?

A) To encrypt all inter-service communication
B) To isolate failures so one failing service doesn't exhaust resources for others
C) To load balance traffic across multiple instances
D) To cache responses for faster retrieval

**Answer: B**
**Explanation:** The bulkhead pattern (inspired by ship bulkheads) isolates components so failure in one doesn't sink the whole system. In software: separate thread pools per service, separate connection pools per database, separate executors per task type. If one service is slow, it only exhausts its own pool — others continue operating.

---

## Question 9 (Scenario)
Your service receives a traffic spike (10x normal). CPU spikes to 95%, response times increase 10x, and error rate climbs to 20%. Which immediate action prevents cascading failures?

A) Scale up instances immediately
B) Enable rate limiting to shed excess traffic
C) Restart all instances to clear state
D) Increase JVM heap size

**Answer: B**
**Explanation:** During a traffic spike, rate limiting is the immediate defense. It protects the service from being overwhelmed by rejecting excess requests early (returning 429 Too Many Requests). Scaling up takes minutes. Restarting doesn't help and may worsen. Heap increase doesn't address CPU. Rate limiting preserves capacity for requests the service can handle.

---

## Question 10 (Code Output)
What is the output of this distributed tracing code?

```java
Tracer tracer = new Tracer();
String traceId = tracer.startTrace("http-request");
String span1 = tracer.startSpan(traceId, null, "auth");
tracer.endSpan(span1);
String span2 = tracer.startSpan(traceId, null, "db-query");
String span3 = tracer.startSpan(traceId, span2, "sql-execute");
tracer.endSpan(span3);
tracer.endSpan(span2);

System.out.println(tracer.getTrace(traceId).size());
```

A) 2
B) 3
C) 4
D) 1

**Answer: C**
**Explanation:** Four spans are created: (1) "http-request" (root trace), (2) "auth" (child of root), (3) "db-query" (child of root), (4) "sql-execute" (child of db-query). The trace contains all 4 spans with parent-child relationships preserved.

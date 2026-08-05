# Redis Patterns

## 1. Cache-Aside Pattern

**Problem:** Applications must hit the database for every read, causing latency and load.

**Solution:** Application checks Redis first; on miss, reads from DB and populates cache with TTL.

**Implementation:**
```python
import redis
import json

r = redis.Redis()

def get_user(user_id):
    cache_key = f"user:{user_id}"
    cached = r.get(cache_key)
    if cached:
        return json.loads(cached)

    user = db.query("SELECT * FROM users WHERE id = %s", user_id)
    r.setex(cache_key, 3600, json.dumps(user))
    return user

def update_user(user_id, data):
    db.execute("UPDATE users SET ... WHERE id = %s", user_id)
    r.delete(f"user:{user_id}")
```

**When to Use:** Read-heavy workloads where stale data for seconds/minutes is acceptable.

**When NOT to Use:** When strong consistency is required for every read or when write frequency exceeds read frequency.

---

## 2. Read-Through / Write-Through Cache

**Problem:** Cache-aside requires duplicate logic in every caller for cache population.

**Solution:** Cache layer itself handles DB reads and writes, transparent to the application.

**Implementation:**
```python
class ReadThroughCache:
    def __init__(self, redis_client, db, ttl=3600):
        self.r = redis_client
        self.db = db
        self.ttl = ttl

    def get(self, key, loader):
        val = self.r.get(key)
        if val:
            return json.loads(val)
        val = loader()
        if val is not None:
            self.r.setex(key, self.ttl, json.dumps(val))
        return val

    def set(self, key, value, writer):
        writer(value)
        self.r.setex(key, self.ttl, json.dumps(value))

cache = ReadThroughCache(r, db)
user = cache.get("user:123", lambda: db.query("SELECT * FROM users WHERE id=123"))
```

**When to Use:** When you want a centralized caching abstraction that reduces application complexity.

**When NOT to Use:** When cache invalidation logic must differ from write logic or when custom eviction is needed.

---

## 3. Write-Behind (Write-Back) Cache

**Problem:** Synchronous DB writes on every update are slow and create bottlenecks.

**Solution:** Writes go to Redis immediately and are asynchronously flushed to the database.

**Implementation:**
```python
import threading
import time

class WriteBehindCache:
    def __init__(self, redis_client, db):
        self.r = redis_client
        self.db = db
        self.buffer_key = "write_buffer"

    def set(self, key, value):
        self.r.set(key, json.dumps(value))
        self.r.hset(self.buffer_key, key, json.dumps(value))

    def flush_loop(self):
        while True:
            entries = self.r.hgetall(self.buffer_key)
            for key, val in entries.items():
                data = json.loads(val)
                self.db.execute("UPSERT INTO users ...", data)
                self.r.hdel(self.buffer_key, key)
            time.sleep(5)

threading.Thread(target=WriteBehindCache(r, db).flush_loop, daemon=True).start()
```

**When to Use:** When write throughput matters more than durability and brief data loss is acceptable.

**When NOT to Use:** When every write must be immediately durable. Redis data loss means data loss.

---

## 4. Rate Limiter with Lua Script

**Problem:** API endpoints are vulnerable to abuse without request throttling.

**Solution:** Use Redis Lua scripts for atomic sliding-window rate limiting.

**Implementation:**
```lua
-- rate_limit.lua
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

redis.call('ZREMRANGEBYSCORE', key, 0, now - window)
local count = redis.call('ZCARD', key)

if count < limit then
    redis.call('ZADD', key, now, now .. math.random())
    redis.call('EXPIRE', key, window / 1000)
    return 1
else
    return 0
end
```

```python
with open("rate_limit.lua") as f:
    script = r.register_script(f.read())

def is_allowed(user_id, limit=100, window_ms=60000):
    return script(keys=[f"rate:{user_id}"], args=[limit, window_ms, int(time.time()*1000)])
```

**When to Use:** API gateways, login endpoints, and any operation where you need per-user or per-IP throttling.

**When NOT to Use:** When distributed rate limiting is not needed or when a dedicated API gateway handles throttling.

---

## 5. Session Store

**Problem:** Sticky sessions limit horizontal scaling. Sessions stored in app servers are lost on restart.

**Solution:** Store session data in Redis with TTL, accessible from any application instance.

**Implementation:**
```python
from flask import session
import uuid

@app.route("/login", methods=["POST"])
def login():
    user = authenticate(request.form)
    session_id = str(uuid.uuid4())
    r.setex(f"session:{session_id}", 1800, json.dumps({
        "user_id": user.id,
        "role": user.role,
        "login_time": time.time()
    }))
    return jsonify({"session_id": session_id})

@app.before_request
def load_session():
    session_id = request.headers.get("X-Session-Id")
    data = r.get(f"session:{session_id}")
    if data:
        g.user = json.loads(data)
    else:
        abort(401)
```

**When to Use:** Stateless application servers where any instance can serve any request.

**When NOT to Use:** When session data is large (use a database with a session ID reference).

---

## 6. Leaderboard with Sorted Sets

**Problem:** Ranking users by score requires expensive SQL ORDER BY queries.

**Solution:** Use Redis sorted sets where scores are automatically ranked.

**Implementation:**
```python
# Update score
r.zadd("leaderboard:daily", {"user:1001": 1500})
r.zadd("leaderboard:daily", {"user:1002": 2300})

# Get top 10
top = r.zrevrange("leaderboard:daily", 0, 9, withscores=True)

# Get user rank (0-indexed)
rank = r.zrevrank("leaderboard:daily", "user:1001")

# Get score
score = r.zscore("leaderboard:daily", "user:1001")

# Paginated leaderboard
def get_leaderboard(page=1, per_page=20):
    start = (page - 1) * per_page
    return r.zrevrange("leaderboard:daily", start, start + per_page - 1, withscores=True)
```

**When to Use:** Game leaderboards, trending content, and any ranking use case with frequent updates.

**When NOT to Use:** When rankings must be globally consistent across multiple Redis instances or when the dataset exceeds memory.

---

## 7. Distributed Lock with SET NX PX

**Problem:** Multiple application instances may process the same task concurrently.

**Solution:** Use Redis SET with NX (not exists) and PX (expiry) for mutual exclusion.

**Implementation:**
```python
import uuid

class RedisLock:
    def __init__(self, redis_client, key, ttl_ms=10000):
        self.r = redis_client
        self.key = f"lock:{key}"
        self.ttl_ms = ttl_ms
        self.token = str(uuid.uuid4())

    def acquire(self, timeout_ms=5000):
        end = time.time() + timeout_ms / 1000
        while time.time() < end:
            if self.r.set(self.key, self.token, nx=True, px=self.ttl_ms):
                return True
            time.sleep(0.05)
        return False

    def release(self):
        # Lua script for atomic check-and-delete
        script = """
        if redis.call("get", KEYS[1]) == ARGV[1] then
            return redis.call("del", KEYS[1])
        else
            return 0
        end
        """
        return self.r.eval(script, 1, self.key, self.token)

lock = RedisLock(r, "job:process-email")
if lock.acquire():
    try:
        process_emails()
    finally:
        lock.release()
```

**When to Use:** Coordinating singleton tasks across instances (cron jobs, batch processing).

**When NOT to Use:** When you need wait-free locking or when the lock holder might crash without releasing.

---

## 8. HyperLogLog Counters

**Problem:** Counting unique items (visitors, IPs) with SET requires memory proportional to cardinality.

**Solution:** HyperLogLog counts unique items using ~12KB per key regardless of cardinality.

**Implementation:**
```python
# Add unique visitors
r.pfadd("visitors:daily:2024-01-15", "user:1001", "user:1002", "user:1003")
r.pfadd("visitors:daily:2024-01-15", "user:1001")  # duplicate, no effect

# Get count
count = r.pfcount("visitors:daily:2024-01-15")

# Merge multiple days
r.pfmerge("visitors:weekly:1", "visitors:daily:2024-01-15", "visitors:daily:2024-01-16")
week_count = r.pfcount("visitors:weekly:1")
```

**When to Use:** Unique visitor counting, analytics, and any cardinality estimation where approximate counts (~0.8% error) are acceptable.

**When NOT to Use:** When exact counts are required or when you need to enumerate the unique items.

---

## Best Practices

- Always set TTLs on cache keys to prevent unbounded memory growth.
- Use pipeline batching to reduce round trips for bulk operations.
- Monitor Redis memory usage and evictions with `INFO memory`.
- Use namespaced key prefixes to avoid collisions.
- Choose the right data structure: hashes for objects, sorted sets for rankings, HyperLogLog for cardinality.
- Implement cache warming for critical paths after deployment.
- Use Redis Cluster for horizontal scaling and哨兵 for HA.

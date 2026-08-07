# Modern C++

## What it is
Features introduced in C++11, C++14, C++17, and C++20 that modernize the language.

## Why it exists
To improve safety, performance, and developer productivity.

## When to use it
Whenever writing new C++ code (C++11 and later).

## How it works

### Auto and Range-based For
```cpp
auto x = 42;
auto ptr = std::make_unique<int>(10);

for (const auto& elem : vec) {
    std::cout << elem << " ";
}
```

### Lambda Expressions
```cpp
auto add = [](int a, int b) { return a + b; };
std::sort(vec.begin(), vec.end(), [](int a, int b) { return a < b; });
```

### Move Semantics
```cpp
std::vector<int> vec1 = {1, 2, 3};
std::vector<int> vec2 = std::move(vec1);  // vec1 is now empty
```

### Structured Bindings (C++17)
```cpp
auto [name, age] = std::make_pair("John", 25);
```

### Concepts (C++20)
```cpp
template <typename T>
concept Numeric = std::is_arithmetic_v<T>;

template <Numeric T>
T add(T a, T b) { return a + b; }
```

## Production Incidents

### Incident 1: Lambda Capture Dangling Reference
**Problem**: An async task scheduler crashed sporadically in production, producing segfaults in lambda callbacks.

**Cause**: A lambda captured a local `std::string` by reference (`[&name]`) and was dispatched to a thread pool. The originating function returned before the lambda executed, destroying `name` on the stack. The lambda accessed a dangling reference.

**Impact**: ~5 crashes per day in the task scheduler. Corrupted task state caused 0.3% of user jobs to fail silently. Customer support received 12 escalation tickets in one week.

**Detection**: AddressSanitizer caught the use-after-free in a nightly stress test. ASan output clearly showed "stack-use-after-scope" with the lambda's capture list.

**Solution**: Changed capture from `[&name]` to `[name]` (capture by value) for all variables outliving the originating scope. For large objects, used `std::shared_ptr` to share ownership with the lambda.

**Prevention**: Enable ASan in CI. Clang-tidy rule: `-Wdangling- captured-reference` for lambdas. Code review checklist must verify capture mode vs. variable lifetime for every lambda dispatched to another thread.

---

### Incident 2: std::optional Misuse Causing Crash
**Problem**: A configuration service crashed on startup with `std::bad_optional_access` in production deployments on Kubernetes.

**Cause**: `std::optional<Config>` was used to lazily initialize a global config. The code accessed `.value()` before checking `.has_value()`. In production, the config file loaded slower than expected due to NFS latency, so the optional was still empty when accessed.

**Impact**: Service failed to start in 30% of pods. Kubernetes restarted loops burned cluster resources. Deployment rollback took 20 minutes, causing a partial outage.

**Detection**: Core dump analysis showed `std::bad_optional_access` thrown at the `.value()` call. `strace` on slow-starting pods confirmed NFS mount delays exceeding the initialization timeout.

**Solution**: Replaced `.value()` with `.value_or(default)` for non-critical config and added explicit `.has_value()` checks with logging for critical config. Added a startup readiness probe that blocks traffic until config is confirmed loaded.

**Prevention**: Lint rule: ban `.value()` calls — use `*opt` (unchecked, fast) or `.value_or()` (safe) instead. Add startup health checks to Kubernetes manifests. Use `std::optional` only when absence is a valid runtime state, not for deferred initialization.

---

## Production Checklist
- [ ] Use `auto` for type inference
- [ ] Use range-based for loops
- [ ] Use lambdas for short functions
- [ ] Use move semantics for efficiency
- [ ] Use structured bindings for clarity
- [ ] Use concepts for template constraints

## Maturity Levels
- **Beginner**: auto, range-based for, lambdas
- **Intermediate**: Move semantics, smart pointers
- **Advanced**: Concepts, coroutines, modules

## Common Myths
- ❌ "Modern C++ is incompatible with old code"
- ❌ "You should always use the latest standard"
- ❌ "Lambdas are always slower than functions"

## One-Minute Revision
| Feature | Description |
|---------|-------------|
| auto | Type inference |
| Lambda | Anonymous functions |
| Move | Transfer ownership |
| Structured Bindings | Unpack tuples/pairs |
| Concepts | Template constraints |

## Related Topics
- [Templates](../03-templates/)
- [STL](../04-stl/)
- [Best Practices](../14-best-practices/)
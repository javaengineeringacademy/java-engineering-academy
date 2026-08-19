# 09. JVM Diagnostics - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Diagnosing production issues (deadlocks, OOM) | **Must** |
| Capturing thread dumps and heap dumps | **Must** |
| Using jcmd, jmap, jstack, jstat | **Should** |
| Setting up automated diagnostics | **Should** |
| Debugging class loading issues | **Should** |
| Simple applications with no issues | **Nice to have** |

## When This Knowledge is Essential

- **Production debugging**: Thread dumps and heap dumps are essential for diagnosing issues
- **Deadlock detection**: jstack reveals deadlocks and thread contention
- **Memory leak diagnosis**: jmap and heap dump analysis identify leak sources
- **Performance monitoring**: jstat provides real-time GC and class loading stats
- **Incident response**: Quick diagnostic capture prevents extended outages

## Key Decision Points

| Decision | Diagnostic Knowledge Impact |
|----------|---------------------------|
| Thread dump vs heap dump vs flight recording | Different issues need different diagnostics |
| jcmd vs jmap vs jstack vs jhat | Different tools for different scenarios |
| Live analysis vs post-mortem | Real-time vs offline investigation |
| Automated vs manual capture | Continuous monitoring vs reactive debugging |

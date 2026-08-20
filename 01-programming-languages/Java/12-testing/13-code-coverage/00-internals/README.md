# Code Coverage - Internals

## JaCoCo Architecture

```
Compile Time:
Source → Bytecode → Instrumented Bytecode (with probes)

Runtime:
Tests → Execute instrumented code → Probes record data

Report Time:
Execution data → Analysis → Coverage report
```

## Probe Instrumentation

Each branch point gets a probe:

```java
// Before
if (a > b) { doX(); } else { doY(); }

// After
if (a > b) { probe[0]=true; doX(); }
else { probe[1]=true; doY(); }
```

## Data Collection

- Probes stored in boolean arrays
- Arrays attached to classloader
- Data collected on class unload
- Multiple test runs merged

## Report Generation

1. Load execution data
2. Load class files
3. Match probes to source
4. Calculate coverage ratios
5. Generate HTML/XML/CSV

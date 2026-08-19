# 06. GC Algorithms - Examples

## Example Files

| File | Description |
|------|-------------|
| *(See practices/ and solutions/ for runnable examples)* | GC algorithm comparison and tuning exercises |

## Running Examples

```bash
# Compare collectors with GC logging
java -XX:+UseG1GC -Xlog:gc* Exercise1
java -XX:+UseZGC -Xlog:gc* Exercise1
java -XX:+UseShenandoahGC -Xlog:gc* Exercise1

# Profile allocation patterns
java -XX:+UseG1GC -Xlog:gc* Exercise2
```

## Key Concepts Demonstrated

- G1 region-based collection
- ZGC colored pointers and concurrent collection
- Shenandoah Brooks pointers
- Algorithm selection for different workloads

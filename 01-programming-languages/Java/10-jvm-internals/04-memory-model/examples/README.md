# 04. Memory Model - Examples

## Example Files

| File | Description |
|------|-------------|
| `MemoryModel.java` | Demonstrates memory visibility issues without synchronization |
| `HappensBefore.java` | Shows all happens-before rules in action |
| `MemoryDeepDive.java` | Deep dive into memory barriers and volatile behavior |

## Running Examples

```bash
javac MemoryModel.java && java MemoryModel
javac HappensBefore.java && java HappensBefore
javac MemoryDeepDive.java && java MemoryDeepDive
```

## Key Concepts Demonstrated

- Data races and memory visibility problems
- volatile guarantees and limitations
- synchronized happens-before relationships
- Thread.start() and Thread.join() visibility
- Final field safe publication
- Memory barrier behavior

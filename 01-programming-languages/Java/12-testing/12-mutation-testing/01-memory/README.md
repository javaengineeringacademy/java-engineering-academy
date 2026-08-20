# Mutation Testing - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           JVM Heap                  │
│  - Original class bytecode          │
│  - Mutated class (one at a time)    │
│  - Test execution results           │
│  - Mutation report data             │
├─────────────────────────────────────┤
│         PIT Engine                  │
│  - Mutation generators              │
│  - Bytecode manipulator             │
│  - Class loader for mutants         │
└─────────────────────────────────────┘
```

## Memory Considerations

- Mutants are loaded one at a time
- ClassLoader manages mutant loading
- Original classes preserved for comparison
- Results accumulated across mutants

## Performance Tips

1. Limit mutation scope to changed code
2. Use efficient test ordering
3. Skip mutations in dead code
4. Use incremental analysis

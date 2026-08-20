# BDD Testing - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           Heap Memory               │
│  - Parsed feature files             │
│  - Step definition instances        │
│  - World object (shared state)      │
│  - Scenario context                │
├─────────────────────────────────────┤
│         Cucumber Engine             │
│  - Step matcher                     │
│  - Scenario executor                │
│  - Hook manager                     │
│  - Report generator                 │
└─────────────────────────────────────┘
```

## Memory Considerations

- Feature files parsed once at startup
- Step definitions instantiated per scenario
- World object per scenario (isolated)
- Reports generated after execution

## Performance Tips

1. Reuse step definitions
2. Minimize Background steps
3. Use tags for selective execution
4. Share expensive setup in BeforeAll hooks

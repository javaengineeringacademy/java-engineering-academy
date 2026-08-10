# While Loop Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Unknown iterations? → While loop
├── Complex exit condition? → While loop
├── Need to process until condition? → While loop
├── Known iterations? → For loop
├── Simple iteration? → For-each
└── Need index? → For loop
```

## Comparison Matrix

| Feature | While Loop | For Loop | Do-While | For-Each |
|---------|------------|----------|----------|----------|
| Unknown iterations | Yes | No | Yes | No |
| Complex conditions | Yes | Medium | Yes | No |
| Post-check | Optional | No | Yes | No |
| Index access | Manual | Yes | Manual | No |
| Readability | Medium | High | Medium | High |
| Risk of infinite loop | High | Low | High | None |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Unknown iterations | While loop | Condition-based |
| Complex exit condition | While loop | Flexible |
| Must execute at least once | Do-While | Post-check |
| Known iterations | For loop | Counter-based |
| Simple iteration | For-each | Cleanest |

## Production Recommendations

> **Use while loop for unknown iterations** — perfect for reading streams or queues.

> **Always ensure termination** — while loops easily become infinite.

> **Use do-while when body must execute** — at least one iteration guaranteed.

> **Prefer for loop when possible** — it's clearer and safer.

## Engineering Trade-offs

| Trade-off | While Loop | Alternative |
|-----------|------------|-------------|
| Flexibility vs Safety | Flexible | For loop: safer |
| Condition vs Counter | Condition-based | For loop: counter-based |
| Readability vs Power | Less readable | For loop: clearer |
| Risk vs Reward | Infinite loop risk | For-each: no risk |

## Common Code Review Comments

- "This while loop can be converted to a for loop for clarity."
- "Ensure this while loop has a termination condition."
- "Consider using do-while if body must execute at least once."
- "This while loop reads from a stream — consider try-with-resources."

## Common Production Mistakes

> Notice: While loop without proper termination causes infinite loop — always check exit condition.

> Notice: While loop reading from stream without try-with-resources leaks resources.

> Notice: Modifying loop condition variable in body can cause unexpected behavior.

> Notice: While loop with complex condition is hard to debug — keep conditions simple.

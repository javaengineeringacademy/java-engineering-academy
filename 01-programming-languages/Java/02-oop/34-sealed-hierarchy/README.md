# Sealed Hierarchy

## Why Sealed Classes?

| Criteria | Sealed | Interface | Abstract Class |
|----------|--------|-----------|----------------|
| Restricted subtypes | Yes | No | Yes |
| Pattern matching | Built-in | No | No |
| Implementation | Can have | No | Yes |
| Use when | Closed hierarchy | Open extension | Partial implementation |

### Decision Flowchart
Known set of types? → Yes → Need pattern matching? → Yes → Use Sealed

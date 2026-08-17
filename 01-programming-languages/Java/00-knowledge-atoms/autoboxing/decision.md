# Decision Guide: Autoboxing

## When to Use Autoboxing
- Use autoboxing when working with collections (`List<Integer>`, `Map<String, Boolean>`)
- Use wrapper types for nullability — primitives cannot represent `null`
- Use autoboxing in non-critical code paths for cleaner syntax

## When to Avoid Autoboxing
- Avoid in tight loops — each iteration creates a new wrapper object
- Avoid in performance-critical numeric processing — use primitive arrays or specialized collections
- Avoid relying on `==` for wrapper comparison outside cache range

## Trade-offs
| Aspect | Autoboxing | Primitives |
|--------|-----------|------------|
| Nullability | Supports null | No null support |
| Performance | Overhead per conversion | Direct value, no allocation |
| Collections | Required (pre-Java-9) | Use specialized collections (Eclipse, HPPC) |
| Memory | Higher (object headers) | Lower (raw values) |

## Expert Recommendation
Use primitives for local variables and loops. Reserve wrapper types for collections, API boundaries, and nullable fields. Profile before optimizing — JIT may eliminate some autoboxing overhead.

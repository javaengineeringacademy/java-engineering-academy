# Decision: Design Patterns

## When to Use Design Patterns

**Use patterns when:**
- You're solving a common, well-known problem
- You need maintainable, extensible code
- You're communicating with other developers
- You're preparing for interviews

**Don't force patterns when:**
- A simpler solution exists
- The pattern adds unnecessary complexity
- You're over-engineering for the current requirements

## Pattern Selection by Problem

| Problem | Pattern(s) |
|---------|------------|
| Create single instance | Singleton |
| Create complex objects | Builder, Factory |
| Adapt interfaces | Adapter, Facade |
| Add behavior dynamically | Decorator, Proxy |
| Notify multiple objects | Observer |
| Manage state transitions | State |
| Define algorithms | Strategy, Template Method |
| Traverse collections | Iterator |
| Handle requests in chain | Chain of Responsibility |
| Simplify complex subsystems | Facade |
| Share common objects | Flyweight |

## Anti-Patterns to Avoid

- **Singleton Abuse** — Don't use singleton for everything
- **Factory Method Overuse** — Not every object needs a factory
- **Decorator Stack** — Too many decorators reduce readability
- **Observer Hell** — Circular dependencies between observers

## Further Reading

- [Refactoring.Guru](https://refactoring.guru/design-patterns)
- [DoFactory Design Patterns](https://www.dofactory.com/)

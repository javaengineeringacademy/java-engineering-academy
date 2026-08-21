# Decision: Dynamic Proxy

## When to Use Dynamic Proxy

**Use when:**
- Implementing AOP (logging, transactions, security)
- Creating mock objects for testing
- Building remote procedure call (RPC) frameworks
- Implementing lazy loading proxies
- Adding cross-cutting concerns without modifying target code

**Avoid when:**
- You only have concrete classes (no interfaces) — use CGLIB instead
- Performance is critical — proxy adds overhead per call
- Simple delegation — inheritance is simpler

## Decision Matrix

| Need | Approach | Notes |
|------|----------|-------|
| Intercept interface methods | JDK Dynamic Proxy | Standard, no dependencies |
| Intercept class methods | CGLIB or ByteBuddy | Creates subclass |
| Mock in tests | Mockito (uses proxies) | Specialized tool |
| Transaction management | Spring AOP (uses proxies) | Framework support |
| Remote calls | Java RMI, gRPC | Specialized protocols |

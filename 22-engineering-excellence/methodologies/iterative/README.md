# Iterative Methodology

## Overview

The Iterative methodology develops software through repeated cycles (iterations), where each iteration adds functionality and refines the product. Unlike incremental development, iterations may revisit and improve existing features.

## Process

1. **Initial Planning** - Define overall scope and requirements
2. **Iteration 1** - Build core functionality
3. **Iteration 2** - Refine and add features
4. **Iteration 3** - Further enhancements
5. **Final Release** - Complete product

```mermaid
graph LR
    A[Planning] --> B[Iteration 1]
    B --> C[Iteration 2]
    C --> D[Iteration 3]
    D --> E[Release]
    B -.-> C
    C -.-> D
```

## When to Use

- Requirements are not fully known upfront
- Complex projects requiring evolutionary development
- Teams with experienced developers
- Projects where design improvements are expected
- Customer can provide ongoing feedback

## Pros

- Early delivery of usable functionality
- Adaptability to changing requirements
- Regular feedback from stakeholders
- Risk reduction through frequent reviews
- Continuous improvement of the product

## Cons

- Requires strong project management
- Can lead to scope creep without discipline
- May require extensive refactoring
- Not ideal for fixed-price contracts
- Documentation may lag behind development

## Real-World Example

**Microsoft Office** - Each major version of Microsoft Office represents an iteration, building on previous versions while adding new features and improvements based on user feedback.

## Interview Questions

1. How does iterative development differ from waterfall?
2. What are the key activities in each iteration?
3. How do you manage scope in an iterative project?
4. What are the risks of iterative development?
5. When would you choose iterative over incremental development?

## References

- Ivar Jacobson (1997). "The Unified Software Development Process"
- Craig Larman (2003). "Agile and Iterative Development: A Manager's Guide"
- Alistair Cockburn (2004). "Crystal Clear: A Human-Powered Methodology for Small Teams"

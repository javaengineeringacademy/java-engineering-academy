# Waterfall Methodology

## Overview

Waterfall is a linear, sequential software development methodology where each phase must be completed before the next begins. Named for the cascading flow from one phase to the next, it emphasizes thorough documentation and upfront planning.

## Phases

1. **Requirements** - Complete specification of what the system must do
2. **Design** - System architecture and detailed design documents
3. **Implementation** - Code writing based on design specifications
4. **Testing** - Verification against requirements
5. **Deployment** - Release to production environment
6. **Maintenance** - Bug fixes and enhancements

```mermaid
graph LR
    A[Requirements] --> B[Design]
    B --> C[Implementation]
    C --> D[Testing]
    D --> E[Deployment]
    E --> F[Maintenance]
```

## When to Use

- Requirements are well-understood and unlikely to change
- Regulated industries (healthcare, finance, aerospace)
- Projects with fixed scope, budget, and timeline
- Large teams requiring clear documentation
- Contract-based work with defined deliverables

## Pros

- Simple and easy to understand
- Clear milestones and deliverables
- Comprehensive documentation
- Easier project management and tracking
- Works well for distributed teams

## Cons

- Inflexible to changing requirements
- Late discovery of issues (testing comes late)
- Working software appears near the end
- High risk of scope creep
- Assumes perfect understanding upfront

## Real-World Example

**NASA Space Shuttle Software** - Requirements were extensively documented and validated before any code was written. The cost of changes post-implementation was extremely high, making Waterfall the appropriate choice.

## Interview Questions

1. What are the main phases of the Waterfall methodology?
2. When would you choose Waterfall over Agile approaches?
3. What are the risks of using Waterfall for projects with uncertain requirements?
4. How does Waterfall handle changes in requirements after a phase is complete?
5. What role does documentation play in Waterfall projects?

## References

- Winston W. Royce (1970). "Managing the Development of Large Software Systems"
- IEEE Standard 1058-1998 for Software Project Management Plans
- Project Management Institute (PMI) - PMBOK Guide

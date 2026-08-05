# Build vs. Buy Decision Framework

## Overview

The build vs. buy decision is one of the most consequential choices engineering teams face. It determines whether to develop a custom solution in-house or purchase an existing product or service. Neither option is universally correct; the right answer depends on the specific context, constraints, and strategic priorities of the organization.

## When to Buy

Buying is typically the better choice when:

- The problem is well-understood and has mature, proven solutions
- The solution is not a core differentiator for the business
- Time-to-market is critical and building would take too long
- The team lacks expertise in the problem domain
- The vendor ecosystem is active with regular updates and strong support
- The cost of building and maintaining exceeds the cost of purchasing over time
- The solution requires extensive third-party integrations that vendors already provide

Common examples include authentication providers, payment processing, monitoring platforms, and CRM systems.

## When to Build

Building is typically the better choice when:

- The solution is a core competitive advantage or differentiator
- Existing solutions do not meet specific requirements and customization would be excessive
- Long-term cost projections favor building over perpetual licensing
- The organization needs full control over the code, data, and roadmap
- Vendor lock-in poses unacceptable risk
- The team has domain expertise and capacity to maintain the solution
- Regulatory or compliance requirements demand full visibility into the implementation

## ROI Analysis

A practical ROI comparison requires estimating costs for both options over the same time horizon.

### Build ROI Formula

ROI = (Value Generated - Total Build Cost) / Total Build Cost

Build cost includes:
- Initial development labor
- Ongoing maintenance and support
- Infrastructure and tooling
- Opportunity cost of engineering time

### Buy ROI Formula

ROI = (Value Generated - Total Buy Cost) / Total Buy Cost

Buy cost includes:
- Licensing or subscription fees
- Implementation and customization labor
- Training and onboarding
- Integration development
- Ongoing subscription renewals

## Decision Matrix

| Factor | Favors Build | Favors Buy |
|--------|-------------|------------|
| Core differentiator | Yes | No |
| Time sensitivity | Low | High |
| Team expertise | High | Low |
| Vendor maturity | Low | High |
| Customization needs | High | Low |
| Long-term cost | Lower | Higher |
| Control requirements | High | Low |
| Maintenance capacity | High | Low |

## Hybrid Approach

Many organizations adopt a hybrid strategy. They buy commodity capabilities such as authentication, email, and monitoring while building custom solutions for their core domain. This approach balances speed and cost-effectiveness with differentiation and control.

## Best Practices

1. Quantify both options using the same cost model and time horizon
2. Include hidden costs such as vendor lock-in, integration, and opportunity cost
3. Revisit the decision periodically as the market and the organization evolve
4. Consider a proof-of-build spike before committing to a full build
5. Negotiate vendor contracts with exit clauses and data portability guarantees
6. Document the reasoning behind the decision for future reference
7. Avoid building for potential future needs that may never materialize

## Common Mistakes

- Choosing to build out of pride or novelty rather than strategic rationale
- Underestimating maintenance costs of custom solutions
- Ignoring the time cost of building when the market demands speed
- Buying a solution that requires so much customization it becomes a custom build anyway
- Failing to reassess the decision as requirements and market conditions change

## Further Reading

- "Build vs. Buy" by Martin Fowler
- "The Build vs. Buy Decision" by Forrester Research

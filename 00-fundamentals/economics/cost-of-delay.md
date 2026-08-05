# Cost of Delay

## Overview

Cost of Delay (CoD) quantifies the economic impact of delaying a feature, project, or decision. It answers a fundamental question: what is the cost of not doing this now? By making delay costs explicit, teams can make better prioritization decisions and avoid the trap of treating all work as equally urgent.

## Why Cost of Delay Matters

Without a framework for understanding delay costs, teams default to prioritizing based on intuition, politics, or the loudest voice in the room. Cost of delay introduces economic rigor into prioritization, enabling teams to focus on the work that delivers the most value per unit of time.

## Components of Cost of Delay

Cost of delay typically has three components:

### Value Erosion

Revenue or value that is lost for each unit of time the feature is delayed. A feature that generates $100,000 per month has a value erosion of approximately $3,333 per day.

### Time-Critical Value

Value that degrades or disappears if the feature is not delivered by a specific deadline. A feature tied to a regulatory deadline or a seasonal event may have zero value if delivered late.

### Risk Reduction and Opportunity Enablement

Some work reduces risk or unlocks future opportunities. Delaying this work increases the risk of a negative event or delays the ability to pursue related opportunities.

## Calculating Cost of Delay

A practical calculation:

1. Estimate the total value of the feature over its expected lifetime
2. Estimate the time period over which that value is realized
3. Calculate the daily or weekly value erosion
4. Identify any deadlines that create time-critical value cliffs
5. Sum the components to get total cost of delay

Example: A feature worth $600,000 over 12 months has a value erosion of approximately $50,000 per month. Each month of delay costs $50,000 in unrealized value.

## WSJF Prioritization

Weighted Shortest Job First (WSJF) is a prioritization framework that uses cost of delay to determine the optimal sequencing of work. It is used extensively in SAFe (Scaled Agile Framework).

WSJF = Cost of Delay / Job Size

The formula favors items with high cost of delay relative to their size. This ensures the team always works on the item that delivers the most value per unit of effort.

### WSJF Components

- **User-business value**: How much value does this deliver to users or the business
- **Time criticality**: How much does value degrade if this is delayed
- **Risk reduction or opportunity enablement**: How much does this reduce risk or unlock future work
- **Job size**: Relative estimate of the effort required (story points or t-shirt sizes)

### Applying WSJF

1. Estimate each component on a relative scale (1, 2, 3, 5, 8, 13)
2. Sum the three value components to get cost of delay
3. Divide by job size to get WSJF score
4. Rank items by WSJF score, highest first
5. Reassess estimates as new information emerges

## Best Practices

1. Estimate cost of delay in business terms, not technical terms
2. Use relative estimation when precise numbers are not available
3. Revisit estimates regularly as market conditions and understanding change
4. Communicate delay costs in stakeholder discussions to build urgency
5. Track actual vs. predicted delay costs to improve estimation accuracy
6. Combine cost of delay with other prioritization factors when necessary
7. Avoid analysis paralysis; rough estimates are better than no estimates

## Common Pitfalls

- Overestimating delay costs to make everything seem urgent
- Ignoring delay costs because they are difficult to quantify
- Focusing only on revenue and ignoring risk and opportunity dimensions
- Using cost of delay as the sole prioritization input without considering capacity and dependencies
- Not updating estimates as deadlines approach or pass

## Further Reading

- "Prioritizing Features with Cost of Delay" by Donald Reinertsen
- "SAFe Framework: WSJF" documentation
- "The Principles of Product Development Flow" by Donald Reinertsen

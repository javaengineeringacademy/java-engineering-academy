# Engineering Metrics

## Overview

Engineering metrics provide quantitative signals about the health, efficiency, and impact of software development processes. When used well, metrics reveal bottlenecks, validate improvements, and align engineering work with business outcomes. When misused, they become targets that distort behavior rather than inform decisions.

## Principles of Good Metrics

### Measure What Matters

Metrics should connect to outcomes that matter to the business and its customers. Deployment frequency is meaningful because it correlates with the ability to deliver value. Lines of code is not meaningful because it does not correlate with value.

### Trends Over Snapshots

A single data point is rarely useful. Metrics are most valuable when tracked over time to reveal trends, detect regressions, and measure the impact of process changes.

### Context Is Essential

A metric without context is meaningless. A deployment frequency of three times per week is excellent for a team building medical devices and poor for a team building a consumer web application. Always interpret metrics relative to the team's constraints and domain.

### Avoid Goodhart's Law

Goodhart's Law states that when a measure becomes a target, it ceases to be a good measure. If teams are incentivized to improve a metric, they may optimize for the metric rather than the underlying goal. Choose metrics that are difficult to game.

## Categories of Metrics

| Category | Examples |
|----------|----------|
| Delivery performance | DORA metrics, velocity, cycle time |
| Quality | Defect rate, change failure rate, code coverage |
| Reliability | Uptime, MTTR, error budgets |
| Developer experience | Satisfaction surveys, time to first commit, toil ratio |
| Business impact | Revenue per feature, customer satisfaction, adoption rate |

## DORA Metrics

The four DORA metrics are the gold standard for measuring software delivery performance:

- **Deployment Frequency**: How often code is deployed to production
- **Lead Time for Changes**: Time from commit to production deployment
- **Mean Time to Recovery (MTTR)**: Time to restore service after failure
- **Change Failure Rate**: Percentage of deployments that cause failures

## SPACE Framework

The SPACE framework provides a more holistic view by measuring five dimensions:

- **Satisfaction**: Developer satisfaction and well-being
- **Performance**: System and team performance
- **Activity**: Volume of work and contributions
- **Communication**: Collaboration and knowledge sharing
- **Efficiency**: Flow and throughput

## Best Practices

1. Use a small number of metrics rather than tracking everything
2. Review metrics as a team, not as individual performance measures
3. Combine quantitative metrics with qualitative feedback
4. Retire metrics that no longer provide actionable insight
5. Ensure metrics are collected automatically, not manually
6. Communicate why each metric is being tracked and how it will be used
7. Revisit metric selection quarterly as team goals evolve

## Common Mistakes

- Using metrics to evaluate individuals rather than systems
- Tracking too many metrics, leading to analysis paralysis
- Optimizing for the metric rather than the outcome
- Ignoring qualitative data that contradicts the numbers
- Not providing context when sharing metrics with leadership
- Using metrics as weapons in political discussions

## Further Reading

- "Accelerate" by Nicole Forsgren, Jez Humble, and Gene Kim
- "Measuring and Managing Performance in Organizations" by Steve McConnell
- "The Metrics Are the Enemy" by Nicole Forsgren

# DORA Metrics

## Overview

DORA (DevOps Research and Assessment) metrics are the four key measures of software delivery performance identified through extensive research by Nicole Forsgren, Jez Humble, and Gene Kim. These metrics are the strongest predictors of organizational performance in terms of profitability, productivity, and customer satisfaction.

## The Four Metrics

### 1. Deployment Frequency

Deployment frequency measures how often code is deployed to production. It reflects the team's ability to deliver small, incremental changes reliably.

- **Elite**: Multiple deployments per day
- **High**: Between once per day and once per week
- **Medium**: Between once per week and once per month
- **Low**: Fewer than once per month

Higher deployment frequency correlates with lower change failure rate and faster recovery, because smaller changes are easier to test, review, and roll back.

### 2. Lead Time for Changes

Lead time for changes measures the time from code commit to successful production deployment. It encompasses code review, testing, build, and deployment processes.

- **Elite**: Less than one hour
- **High**: One day to one week
- **Medium**: One week to one month
- **Low**: One month to six months

Shorter lead times indicate efficient pipelines, effective automation, and small batch sizes.

### 3. Mean Time to Recovery (MTTR)

MTTR measures how quickly a team can restore service after a production incident or failure. It starts when the incident is detected and ends when service is fully restored.

- **Elite**: Less than one hour
- **High**: Less than one day
- **Medium**: Between one day and one week
- **Low**: More than one week

Low MTTR indicates good monitoring, effective incident response processes, and system resilience.

### 4. Change Failure Rate

Change failure rate measures the percentage of deployments that result in degraded service or require a rollback, hotfix, or patch. A change failure is any change to production that causes an impairment to users.

- **Elite**: 0-15 percent
- **High**: 16-30 percent
- **Medium**: 16-30 percent
- **Low**: 16-30 percent

Lower change failure rates indicate better testing, review practices, and deployment processes.

## Performance Levels

The research identifies four performance tiers:

| Level | Deployment Freq | Lead Time | MTTR | Change Failure Rate |
|-------|----------------|-----------|------|-------------------|
| Elite | Multiple/day | < 1 hour | < 1 hour | 0-15% |
| High | Between day and week | 1 day - 1 week | < 1 day | 16-30% |
| Medium | Between week and month | 1 week - 1 month | 1 week - 1 month | 16-30% |
| Low | Less than month | 1-6 months | > 1 week | 16-30% |

## Key Findings from Research

- Elite performers are 208 times more likely to deploy multiple times per day
- Elite performers are 2,604 times more likely to recover from incidents in less than one hour
- Elite performers have a change failure rate 7 times lower than low performers
- Delivery performance is predictive of organizational performance (profitability, market share, productivity)
- Technical practices (trunk-based development, CI/CD, test automation) drive DORA metrics

## How to Improve DORA Metrics

### Improving Deployment Frequency

- Automate the build and deployment pipeline
- Use feature flags to decouple deployment from release
- Adopt trunk-based development with short-lived branches
- Reduce batch sizes to make deployments routine

### Improving Lead Time

- Implement continuous integration with automated testing
- Use CI/CD pipelines to automate build, test, and deploy
- Reduce manual approvals and handoffs
- Invest in test automation to reduce feedback loop time

### Improving MTTR

- Implement comprehensive monitoring and alerting
- Create runbooks for common failure scenarios
- Practice incident response through game days and chaos engineering
- Design systems for graceful degradation and fast rollback

### Reducing Change Failure Rate

- Increase test coverage, especially at the integration level
- Use code review and pair programming to catch defects early
- Deploy small changes frequently to reduce blast radius
- Implement feature flags to enable instant rollback

## Best Practices

1. Measure all four metrics together, not in isolation
2. Track trends over time rather than targeting specific numbers
3. Use metrics to identify bottlenecks in the delivery pipeline
4. Share metrics with the team to create collective ownership
5. Avoid using metrics for individual performance evaluation
6. Combine DORA metrics with qualitative developer feedback
7. Reassess baseline metrics quarterly as the team improves

## Further Reading

- "Accelerate" by Nicole Forsgren, Jez Humble, and Gene Kim
- DORA State of DevOps Reports
- "The DORA Metrics" by Nicole Forsgren

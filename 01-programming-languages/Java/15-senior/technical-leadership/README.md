# Technical Leadership

## How to Write an RFC

### Structure
```markdown
# RFC: [Title]

## Status
Draft | In Review | Accepted | Rejected | Superseded

## Summary
One paragraph explaining what this RFC proposes.

## Motivation
Why are we doing this? What problem does it solve?

## Detailed Design
Technical implementation details. Include:
- Architecture diagrams
- API contracts
- Data models
- Error handling
- Security considerations

## Alternatives Considered
What other approaches did we evaluate? Why were they rejected?

## Impact
- Affected services/systems
- Migration plan
- Rollback strategy
- Timeline

## Open Questions
What needs further discussion?

## Decision
Who approved this and when?
```

### Writing Tips
- **Be specific**: "Use Kafka for event streaming" not "Consider async messaging"
- **Include trade-offs**: Show you evaluated alternatives
- **Estimate effort**: Time, people, risk
- **Define success**: How will we know this worked?
- **Keep it scannable**: Use headers, bullet points, diagrams

### Example Opening
```markdown
# RFC: Migrate Payment Service to Event-Driven Architecture

## Summary
Migrate the payment service from synchronous REST calls to an event-driven 
architecture using Kafka. This will decouple payment processing from downstream 
services and improve reliability during provider outages.

## Motivation
The payment service currently has tight coupling with 5 downstream services.
During the October outage, a single slow service caused cascading failures
across all payment operations. An event-driven architecture will isolate 
failures and improve overall system resilience.
```

---

## How to Run a Design Review

### Preparation (1 week before)
- [ ] Share RFC with reviewers 48 hours in advance
- [ ] Identify key stakeholders and decision-makers
- [ ] Book 60-90 minute meeting room
- [ ] Prepare backup slides for complex diagrams
- [ ] Define what "done" looks like (approval, feedback, further investigation)

### During the Review (60-90 minutes)
1. **Opening** (5 min): State the goal and agenda
2. **Presentation** (20 min): Walk through RFC, focus on decisions
3. **Q&A** (30 min): Address questions, capture action items
4. **Discussion** (20 min): Resolve open questions
5. **Closing** (5 min): Summarize decisions, next steps

### Facilitation Rules
- **Timebox discussions**: "Let's take this offline if we can't resolve in 5 minutes"
- **Capture disagreements**: "We have two options here, let's document both"
- **Parking lot**: "Great question, but outside scope. Let's discuss after."
- **Silence means agreement**: "Unless someone objects, we'll proceed with this"

### Follow-up (24 hours after)
- [ ] Send meeting notes with decisions
- [ ] Update RFC with feedback
- [ ] Create tickets for action items
- [ ] Schedule follow-up if needed
- [ ] Thank participants

---

## How to Say "No" to Stakeholders

### Data-Driven Refusal
```markdown
Stakeholder: "Can we add this feature by Friday?"

Response: "Based on current velocity, this feature requires 3 developer-weeks 
of effort. Our current sprint is fully committed. I can offer:
1. Ship a minimal version (1 day) that covers 20% of the use case
2. Schedule full implementation for next sprint (2 weeks)
3. Descope other features to make room (which one should we defer?)
```

### Alternative Framing
```markdown
Stakeholder: "We need to rewrite the entire service."

Response: "A full rewrite would take 6 months and risk regressions. 
Instead, I propose:
1. Identify the top 3 pain points (let's list them)
2. Refactor those modules incrementally
3. Add comprehensive tests before each refactor
This achieves the same goal with lower risk and delivers value sooner."
```

### The "Yes, And" Technique
```markdown
Stakeholder: "We need to support 10x traffic by next month."

Response: "Yes, and to do that safely, we need to:
1. Load test current capacity (1 week)
2. Identify bottlenecks (1 week)
3. Scale horizontally and optimize queries (2 weeks)
This timeline ensures we don't sacrifice reliability for speed."
```

### When to Say Yes Instead
- The request aligns with team goals
- The risk is acceptable
- You have capacity
- The stakeholder has provided clear requirements

---

## Balancing Tech Debt vs. Features

### Allocation Model
```
Total Capacity: 100%
├── New Features:           60-70%
├── Tech Debt Reduction:    15-20%
├── Bug Fixes:              10-15%
└── Learning/Experiment:     5%
```

### The Debt Quadrant Strategy
```
Deliberate Prudent:   Pay immediately (quick wins)
Deliberate Reckless:  Track and schedule (planned debt)
Inadvertent Prudent:  Refactor when touching (opportunistic)
Inadvertent Reckless: Major refactor project (strategic)
```

### Communication Framework
```markdown
"We need to allocate 20% of this sprint to tech debt. Here's why:
- Payment service has no timeout handling (cascade failure risk)
- Test coverage dropped to 60% (regression risk)
- Build times increased to 15 minutes (developer productivity)

Impact: 2 fewer features this sprint
Benefit: Reduced incidents, faster development, fewer bugs

Do we agree on this trade-off?"
```

### Tracking Tech Debt
| Priority | Category | Effort | Impact | Sprint |
|----------|----------|--------|--------|--------|
| P0 | Security vulnerability | 1 day | High | Current |
| P1 | Performance bottleneck | 1 week | High | Next |
| P2 | Missing tests | 2 weeks | Medium | Q2 |
| P3 | Code cleanup | 1 month | Low | Q3 |

---

## How to Mentor Junior Developers

### Pairing Sessions
- **Frequency**: 2-3 times per week, 1 hour each
- **Format**: Driver/navigator, rotate roles
- **Focus**: Real work, not toy examples
- **Goal**: Build confidence, share patterns, teach debugging

```java
// During pairing, think aloud
public void processOrder(Order order) {
    // "First, let's validate the input"
    validateOrder(order);
    
    // "Now we need to check inventory"
    // "What happens if inventory check fails?"
    // Let the junior suggest the approach
    
    // "Good idea! Let's implement that"
    checkInventory(order);
}
```

### Code Review as Teaching
```java
// Instead of: "This is wrong, fix it"
// Say: "This works, but consider this approach for better maintainability"

// Junior code:
public List<Order> getOrders(Long userId) {
    List<Order> orders = new ArrayList<>();
    for (Order o : allOrders) {
        if (o.getUserId().equals(userId)) {
            orders.add(o);
        }
    }
    return orders;
}

// Review comment:
// "This works! For better readability and performance with large lists,
// consider using Streams:
// return allOrders.stream()
//     .filter(o -> o.getUserId().equals(userId))
//     .collect(Collectors.toList());
// This is more declarative and avoids manual iteration."
```

### Growth Plan
```markdown
## 30-60-90 Day Plan for Junior Developer

### First 30 Days
- [ ] Complete onboarding checklist
- [ ] Ship first small feature (bug fix or documentation)
- [ ] Understand codebase architecture
- [ ] Participate in code reviews (read-only first)

### Days 31-60
- [ ] Ship 2-3 features independently
- [ ] Lead a small technical discussion
- [ ] Mentor an intern or new hire
- [ ] Identify one improvement to team process

### Days 61-90
- [ ] Own a feature end-to-end
- [ ] Participate in on-call rotation
- [ ] Give a tech talk to the team
- [ ] Create a runbook for a service you own
```

---

## How to Build Consensus

### RFC Process for Decisions
1. **Draft RFC**: Write proposal with alternatives
2. **Async feedback**: Share in Slack/Teams, collect comments
3. **1:1 discussions**: Talk to key stakeholders individually
4. **Design review**: Final discussion in meeting
5. **Decision**: Document approval and rationale

### Async Feedback Template
```markdown
Hey team, I've drafted an RFC for [topic]. 

Key decision: [What are we deciding?]

Options:
1. Option A (pros/cons)
2. Option B (pros/cons)

I'd love your feedback by [date]. Please:
- React with 👍 if you agree
- Comment with concerns or suggestions
- DM me if you want to discuss privately
```

### Handling Disagreement
```
Step 1: Understand the objection
  "Help me understand your concern with this approach."

Step 2: Find common ground
  "We both want reliable, maintainable code. Where do we differ?"

Step 3: Present data
  "Let's look at the benchmarks/constraints/requirements."

Step 4: Compromise if possible
  "What if we try approach A for 2 weeks and evaluate?"

Step 5: Escalate if needed
  "Let's bring this to the architecture review board."
```

### The "Disagree and Commit" Principle
```markdown
After discussion, if consensus isn't reached:

1. Document the dissenting view
2. The decision-maker commits to the chosen path
3. The dissenter commits to supporting implementation
4. Schedule a follow-up to evaluate the decision

"This is a reversible decision. Let's proceed with Option A,
revisit in 30 days, and adjust if needed."
```

---

## Summary

| Skill | Key Practice |
|-------|--------------|
| RFC writing | Clear problem, alternatives, impact |
| Design review | Prep, facilitation, follow-up |
| Saying no | Data-driven, offer alternatives |
| Tech debt | Allocation model, tracking, communication |
| Mentoring | Pairing, code review as teaching, growth plans |
| Consensus | RFC process, async feedback, disagree and commit |

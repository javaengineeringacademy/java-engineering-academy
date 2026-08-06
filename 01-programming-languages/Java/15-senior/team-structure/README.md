# Java Team Structure

## Team Topologies

### Stream-Aligned Team
- Aligned to a single flow of delivery (feature, product, or service)
- Owns the full lifecycle: build, deploy, monitor, operate
- Typically 5-9 members
- Examples: Checkout team, Search team, Payments team

### Enabling Team
- Helps stream-aligned teams adopt new practices
- Provides expertise in specific areas (CI/CD, observability, security)
- Does not build features directly
- Examples: Platform engineering, DevOps, SRE

### Platform Team
- Provides internal services to reduce cognitive load
- Manages shared infrastructure (Kubernetes, databases, messaging)
- Self-service APIs for other teams
- Examples: Infrastructure team, Data platform team

### Complicated-Subsystem Team
- Owns a subsystem requiring deep specialized knowledge
- High coordination cost with other teams
- Examples: Video codec team, ML model training team, Billing engine team

### Choosing the Right Model
```
New product startup     → Stream-aligned (speed matters)
Growing organization    → Add Platform team (reduce duplication)
Complex domain          → Add Complicated-subsystem (specialization)
Multiple teams blocked  → Add Enabling team (unblock others)
```

---

## Code Ownership Models

### Collective Ownership
- Anyone can modify any code
- Encourages knowledge sharing
- Requires strong code review culture
- Best for small teams (<10 developers)

```java
// Collective ownership means everyone understands this code
public class OrderService {
    public Order createOrder(CreateOrderRequest request) {
        // Any team member can modify this
    }
}
```

### Module Ownership
- Team owns specific modules or services
- Clear responsibility boundaries
- Other teams can read but must request changes
- Best for medium organizations (10-50 developers)

### Team Ownership
- Entire team owns a service end-to-end
- Includes infrastructure, monitoring, on-call
- Full autonomy within bounded context
- Best for large organizations (50+ developers)

### Ownership Matrix
| Aspect | Collective | Module | Team |
|--------|------------|--------|------|
| Code changes | Anyone | Module owner | Owning team |
| Bug fixes | Anyone | Module owner | Owning team |
| On-call | Rotating | Module owner | Owning team |
| Architecture | Team decision | Module owner | Team decision |
| Knowledge silos | Low | Medium | High |

---

## Review Process

### PR Size Guidelines
```
< 100 lines:   Quick review (15 min)
100-300 lines: Standard review (30-60 min)
300-500 lines: Split PR or schedule review
500+ lines:    Must be split (refactor first)
```

### Response Time SLAs
- **Critical bugs**: Review within 2 hours
- **Features**: Review within 1 business day
- **Tech debt**: Review within 2 business days
- **Documentation**: Review within 3 business days

### Approval Requirements
- **Junior PRs**: 1 approval from mid+ developer
- **Senior PRs**: 1 approval from another senior
- **Architectural changes**: 2 approvals including architect
- **Critical path**: 2 approvals + tech lead sign-off

### Review Checklist
```markdown
- [ ] Code compiles and tests pass
- [ ] Follows project conventions
- [ ] No security vulnerabilities
- [ ] No performance regressions
- [ ] Error handling is appropriate
- [ ] Logging is sufficient
- [ ] Documentation updated if needed
- [ ] Breaking changes communicated
```

---

## On-Call Rotation

### Handoff Process
1. **Written summary**: Previous on-call posts status in Slack
2. **Active incidents**: Transfer ownership of open issues
3. **Escalation contacts**: Confirm escalation path
4. **Runbook review**: Check for new runbooks or changes
5. **Calendar check**: Note any scheduled deployments or maintenance

### Escalation Levels
```
Level 1 (On-call engineer):
  - Investigate and attempt resolution
  - Time limit: 30 minutes

Level 2 (Senior on-call):
  - Assist with complex issues
  - Time limit: 1 hour

Level 3 (Engineering manager + architect):
  - Major incidents requiring coordination
  - Decision authority for service degradation

Level 4 (VP Engineering + CTO):
  - Customer-facing outages
  - Communication with stakeholders
```

### Burnout Prevention
- **Maximum 1 week on-call per quarter**
- **Compensatory time off** after heavy on-call periods
- **No scheduled meetings** during on-call shifts
- **Blameless postmortems** (focus on systems, not individuals)
- **On-call stipend** or salary adjustment

---

## Technical Debt Management

### Debt Quadrant
```
                    Deliberate              Inadvertent
                ┌───────────────────┬───────────────────┐
  Prudent       │ "We must ship     │ "Now we know how   │
                │  now"             │  we should do it"  │
                ├───────────────────┼───────────────────┤
  Reckless      │ "We don't have    │ "What's layered    │
                │  time for design" │  architecture?"    │
                └───────────────────┴───────────────────┘
```

### Allocation Model
- **10-20% of sprint capacity** for tech debt
- **Dedicated tech debt sprints** quarterly
- **Pay-as-you-go** for small improvements
- **Major refactors** planned as separate projects

### Tracking System
```markdown
## Tech Debt Item: Payment Service Timeout Handling

**Category**: Reliability
**Impact**: High (cascade failure risk)
**Effort**: 2 weeks
**Quadrant**: Deliberate Prudent (shipped without timeouts)

### Description
Payment service has no timeout on external API calls.
Can cause thread starvation during provider outages.

### Acceptance Criteria
- [ ] All external calls have 5-second timeout
- [ ] Circuit breaker implemented
- [ ] Fallback behavior defined
- [ ] Monitoring alerts added

### Priority: P2 (Next quarter)
```

---

## Knowledge Sharing

### Tech Talks
- **Frequency**: Bi-weekly or monthly
- **Duration**: 30 minutes + 15 minutes Q&A
- **Topics**: Post-mortems, new technologies, architecture decisions
- **Recording**: All sessions recorded and indexed

### Pair Programming
- **New features**: Pair for first implementation
- **Bug fixes**: Pair when investigating complex issues
- **Knowledge transfer**: Pair junior with senior weekly
- **Duration**: 1-2 hours per session

### Documentation
- **Architecture Decision Records (ADRs)**: All major decisions
- **Runbooks**: Step-by-step operational procedures
- **Onboarding guide**: First 2 weeks checklist
- **Code walkthroughs**: Video recordings of complex systems

### Knowledge Sharing Matrix
| Format | Audience | Frequency | Owner |
|--------|----------|-----------|-------|
| Tech talks | All engineers | Bi-weekly | Rotating |
| Pair programming | Team | Weekly | Team leads |
| ADRs | All engineers | Per decision | Authors |
| Runbooks | On-call engineers | As needed | SRE team |
| Onboarding | New hires | Per hire | Buddy |

---

## Hiring Criteria

### Junior Developer (0-2 years)
- **Technical**: Basic Java knowledge, understands OOP, can write simple CRUD
- **Behavioral**: Eager to learn, asks questions, accepts feedback
- **Interview**: Coding challenge (2 hours), behavioral interview (1 hour)
- **Growth path**: Learn codebase, understand testing, handle simple bugs

### Mid-Level Developer (2-5 years)
- **Technical**: Strong Java skills, understands concurrency, can design simple systems
- **Behavioral**: Takes ownership, communicates proactively, mentors juniors
- **Interview**: System design (1 hour), coding (2 hours), cultural fit (1 hour)
- **Growth path**: Lead small features, participate in architecture decisions

### Senior Developer (5-8 years)
- **Technical**: Deep Java expertise, designs complex systems, performance optimization
- **Behavioral**: Drives technical decisions, influences without authority, handles ambiguity
- **Interview**: Architecture review (1 hour), technical leadership (1 hour), coding (1 hour)
- **Growth path**: Lead teams, define technical direction, mentor mid-levels

### Architect (8+ years)
- **Technical**: System-wide expertise, cross-cutting concerns, technology strategy
- **Behavioral**: Builds consensus, communicates with stakeholders, manages trade-offs
- **Interview**: Architecture deep-dive (2 hours), leadership scenario (1 hour), coding (1 hour)
- **Growth path**: Define architecture, guide multiple teams, drive technical vision

### Interview Scorecard
```
Candidate: _______________
Position: _______________

Technical Skills (0-5):
  - Java proficiency:       ___
  - System design:          ___
  - Code quality:           ___
  - Performance awareness:  ___

Behavioral Skills (0-5):
  - Communication:          ___
  - Ownership:              ___
  - Teamwork:               ___
  - Growth mindset:         ___

Overall: ___ / 40
Recommendation: Strong Hire / Hire / Lean No / Strong No
```

---

## Summary

| Aspect | Key Principle |
|--------|---------------|
| Team topologies | Match structure to delivery needs |
| Code ownership | Clear boundaries, shared responsibility |
| Reviews | Small PRs, fast feedback, consistent standards |
| On-call | Fair rotation, clear escalation, burnout prevention |
| Tech debt | Track it, allocate time, pay it down |
| Knowledge sharing | Multiple formats, consistent cadence |
| Hiring | Match criteria to level, assess both technical and behavioral |

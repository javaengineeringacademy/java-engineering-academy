# Agile Methodology

A comprehensive guide to Agile practices, including Scrum, Kanban, XP, SAFe, sprint planning, standups, retrospectives, and user stories.

---

## Table of Contents

1. [Overview](#overview)
2. [Agile Principles](#agile-principles)
3. [Scrum Framework](#scrum-framework)
4. [Kanban Method](#kanban-method)
5. [Extreme Programming (XP)](#extreme-programming-xp)
6. [SAFe (Scaled Agile Framework)](#safe-scaled-agile-framework)
7. [Sprint Planning](#sprint-planning)
8. [Daily Standups](#daily-standups)
9. [Sprint Retrospectives](#sprint-retrospectives)
10. [User Stories](#user-stories)
11. [Best Practices](#best-practices)
12. [Common Mistakes](#common-mistakes)
13. [Key Takeaways](#key-takeaways)

---

## Overview

Agile is an iterative approach to software development that focuses on delivering value quickly, adapting to change, and continuous improvement. It emphasizes collaboration, flexibility, and customer satisfaction.

### Why Agile?

- **Faster delivery**: Working software in weeks, not months
- **Better quality**: Continuous testing and feedback
- **Higher customer satisfaction**: Regular collaboration
- **Reduced risk**: Early detection of issues
- **Improved team morale**: Autonomy and ownership

### Agile vs. Waterfall

| Aspect | Agile | Waterfall |
|--------|-------|-----------|
| Approach | Iterative | Sequential |
| Requirements | Evolving | Fixed |
| Testing | Continuous | End phase |
| Delivery | Incremental | Final product |
| Change | Embraced | Resisted |
| Feedback | Regular | Late |

---

## Agile Principles

### The Agile Manifesto

**Values**
1. Individuals and interactions over processes and tools
2. Working software over comprehensive documentation
3. Customer collaboration over contract negotiation
4. Responding to change over following a plan

**Principles**
1. Our highest priority is to satisfy the customer through early and continuous delivery of valuable software.
2. Welcome changing requirements, even late in development.
3. Deliver working software frequently, from a couple of weeks to a couple of months.
4. Business people and developers must work together daily throughout the project.
5. Build projects around motivated individuals.
6. The most efficient and effective method of conveying information is face-to-face conversation.
7. Working software is the primary measure of progress.
8. Agile processes promote sustainable development.
9. Continuous attention to technical excellence and good design.
10. Simplicity—the art of maximizing the amount of work not done—is essential.
11. The best architectures, requirements, and designs emerge from self-organizing teams.
12. At regular intervals, the team reflects on how to become more effective, and adjusts its behavior accordingly.

---

## Scrum Framework

### Scrum Roles

**Product Owner**
- Defines product vision
- Manages product backlog
- Prioritizes work
- Accepts/rejects work
- Represents stakeholders

**Scrum Master**
- Facilitates Scrum events
- Removes impediments
- Coaches the team
- Protects the team
- Promotes continuous improvement

**Development Team**
- Self-organizing
- Cross-functional
- Delivers increments
- Estimates work
- Owns the process

### Scrum Events

**Sprint Planning**
- Duration: 2-4 hours for 2-week sprint
- Purpose: Plan the work for the sprint
- Activities:
  - Review product backlog
  - Select items for sprint
  - Create sprint backlog
  - Define sprint goal

**Daily Scrum (Standup)**
- Duration: 15 minutes
- Purpose: Daily synchronization
- Activities:
  - What did I do yesterday?
  - What will I do today?
  - Any impediments?

**Sprint Review**
- Duration: 1-2 hours
- Purpose: Demo the increment
- Activities:
  - Show working software
  - Get feedback
  - Update product backlog

**Sprint Retrospective**
- Duration: 1-1.5 hours
- Purpose: Improve the process
- Activities:
  - What went well?
  - What could improve?
  - Action items for next sprint

### Scrum Artifacts

**Product Backlog**
- Ordered list of everything needed
- Managed by Product Owner
- Never complete
- Evolves as learning occurs

**Sprint Backlog**
- Items selected for sprint
- Plan for delivering increment
- Owned by development team
- Updated daily

**Increment**
- Sum of all backlog items completed
- Meets Definition of Done
- Potentially releasable
- Adds value to product

### Scrum Example

```
Sprint 1 (Weeks 1-2)
├── Sprint Planning: Monday, Week 1
├── Daily Standups: Daily, 9:00 AM
├── Development: Week 1-2
├── Sprint Review: Friday, Week 2
└── Sprint Retrospective: Friday, Week 2

Sprint Goal: "Implement user authentication"
Sprint Backlog:
- User registration form
- Login functionality
- Password reset
- Session management
```

---

## Kanban Method

### Kanban Principles

1. **Visualize workflow**: See all work items
2. **Limit work in progress**: Focus on completion
3. **Manage flow**: Optimize throughput
4. **Make policies explicit**: Clear rules
5. **Implement feedback loops**: Continuous improvement
6. **Improve collaboratively**: Team optimization

### Kanban Board

**Columns**
```
| Backlog | To Do | In Progress | Review | Done |
|---------|-------|-------------|--------|------|
| Task A  | Task D| Task F      | Task H | Task J|
| Task B  | Task E| Task G      | Task I | Task K|
| Task C  |       |             |        |       |
```

**Work in Progress (WIP) Limits**
- Backlog: No limit
- To Do: 5 items
- In Progress: 3 items
- Review: 2 items
- Done: No limit

### Kanban vs. Scrum

| Aspect | Kanban | Scrum |
|--------|--------|-------|
| Cadence | Continuous | Sprints |
| Roles | No prescribed roles | PO, SM, Team |
| Changes | Any time | Between sprints |
| Metrics | Lead time, cycle time | Velocity |
| Board | Persistent | Reset each sprint |

### Kanban Example

```
Team Kanban Board

| Backlog (5) | To Do (3) | Dev (2) | Review (1) | Done |
|-------------|-----------|---------|------------|------|
| Feature A   | Bug 1     | Feature B| Feature C  | Task X|
| Feature D   | Bug 2     |         |            | Task Y|
| Feature E   | Task Z    |         |            |       |

WIP Limits: To Do=3, Dev=2, Review=1
```

---

## Extreme Programming (XP)

### XP Values

1. **Communication**: Share knowledge openly
2. **Simplicity**: Do what's necessary
3. **Feedback**: Learn and adapt
4. **Courage**: Make bold decisions
5. **Respect**: Value each team member

### XP Practices

**Pair Programming**
- Two developers, one computer
- Real-time code review
- Knowledge sharing
- Better quality code

**Test-Driven Development (TDD)**
- Write tests first
- Write minimal code to pass
- Refactor for clean code
- Continuous testing

**Continuous Integration**
- Integrate frequently
- Automated builds
- Automated tests
- Quick feedback

**Refactoring**
- Improve code structure
- No behavior changes
- Continuous improvement
- Technical debt reduction

**Simple Design**
- Do the simplest thing
- YAGNI principle
- Clean code
- Easy to understand

**Collective Code Ownership**
- Anyone can change any code
- Shared responsibility
- Knowledge spreading
- Code quality improvement

**Coding Standards**
- Consistent style
- Readable code
- Team agreement
- Automated enforcement

### XP Example

```
XP Development Cycle

1. Write a failing test
2. Run the test (should fail)
3. Write minimal code to pass
4. Run the test (should pass)
5. Refactor if needed
6. Repeat

Benefits:
- High test coverage
- Clean, simple code
- Continuous integration
- Knowledge sharing
```

---

## SAFe (Scaled Agile Framework)

### SAFe Levels

**Team Level**
- Scrum or Kanban
- Iteration planning
- Daily standups
- Iteration review and retrospective

**Program Level**
- Agile Release Train (ART)
- Program Increment (PI) planning
- System demo
- Inspect and adapt

**Large Solution Level**
- Solution train
- Solution management
- Value stream coordination

**Portfolio Level**
- Lean portfolio management
- Strategic themes
- Portfolio backlog
- Budgeting

### SAFe Principles

1. Take an economic view
2. Apply systems thinking
3. Assume variability; preserve options
4. Build incrementally with fast integrated learning cycles
5. Base milestones on objective evaluation of working systems
6. Visualize and limit WIP, reduce batch sizes, and manage queue lengths
7. Apply cadence, synchronize with cross-domain planning
8. Unlock the intrinsic motivation of knowledge workers
9. Decentralize decision-making
10. Organize around value

### SAFe Example

```
Program Increment (PI) Planning

Duration: 2 days
Participants: All teams on the ART

Day 1:
- Business context
- Architecture vision
- Development vision
- Team breakouts

Day 2:
- Team breakouts (continued)
- Program risks
- Confidence vote
- Planning retrospective

Output:
- PI objectives
- Team commitments
- Program board
- Risks and dependencies
```

---

## Sprint Planning

### Purpose

Define what can be delivered in the upcoming sprint and how that work will be achieved.

### Participants

- Product Owner
- Scrum Master
- Development Team

### Agenda

**Part 1: What (1-2 hours)**
1. Review product backlog
2. Discuss sprint goal
3. Select items for sprint
4. Confirm understanding

**Part 2: How (1-2 hours)**
1. Break down items into tasks
2. Estimate tasks
3. Identify dependencies
4. Create sprint backlog

### Best Practices

1. **Prepare backlog**: Groomed and prioritized
2. **Set realistic goal**: Achievable in sprint
3. **Involve the team**: They do the work
4. **Timebox**: Stick to time limits
5. **Commit as a team**: Shared ownership

### Sprint Planning Example

```
Sprint Planning Meeting

Duration: 4 hours (2-week sprint)
Attendees: PO, SM, 5 developers

Agenda:
09:00-09:30: Product Owner presents priorities
09:30-10:30: Team discusses and selects items
10:30-10:45: Break
10:45-12:00: Team breaks down items into tasks
12:00-12:30: Team estimates and commits

Sprint Goal: "Complete user authentication feature"

Selected Items:
- User registration (8 points)
- Login/logout (5 points)
- Password reset (3 points)
- Session management (5 points)

Total: 21 points
```

---

## Daily Standups

### Purpose

Daily synchronization to share progress, plans, and impediments.

### Duration

15 minutes maximum

### Format

Each team member answers:
1. What did I accomplish yesterday?
2. What will I work on today?
3. Are there any impediments?

### Best Practices

1. **Same time and place**: Consistency is key
2. **Stand up**: Keeps it short
3. **Everyone participates**: Equal voice
4. **Focus on commitment**: What will you do?
5. **Take impediments offline**: Don't solve in meeting

### Common Anti-Patterns

1. **Status reporting to manager**: It's for the team
2. **Problem-solving**: Take offline
3. **Going over time**: Respect the timebox
4. **Skipping days**: Consistency matters
5. **Multitasking**: Be present and engaged

### Daily Standup Example

```
Daily Standup

Time: 9:00 AM, 15 minutes
Location: Team area (virtual or physical)

Team Members:
- Alice: "Yesterday: Completed login API. Today: Write tests. No impediments."
- Bob: "Yesterday: Fixed registration bug. Today: Work on password reset. Impediment: Need access to test environment."
- Carol: "Yesterday: Code review. Today: Start session management. No impediments."
- Dave: "Yesterday: Database optimization. Today: Continue optimization. No impedements."
- Eve: "Yesterday: Documentation. Today: Continue documentation. No impediments."

Action Items:
- Scrum Master: Help Bob get test environment access
```

---

## Sprint Retrospectives

### Purpose

Reflect on the sprint to identify improvements and create action items.

### Participants

- Product Owner (optional)
- Scrum Master
- Development Team

### Duration

1-1.5 hours

### Format

**Start-Stop-Continue**
- What should we start doing?
- What should we stop doing?
- What should we continue doing?

**4Ls**
- Liked: What did we like?
- Learned: What did we learn?
- Lacked: What was missing?
- Longed for: What did we wish for?

**Sailboat**
- Wind: What propelled us forward?
- Anchor: What held us back?
- Rocks: What risks do we see?
- Island: What's our goal?

### Best Practices

1. **Safe environment**: Honest feedback
2. **Focus on process**: Not people
3. **Action items**: Specific and assigned
4. **Follow up**: Review previous actions
5. **Timebox**: Respect the time limit

### Retrospective Example

```
Sprint Retrospective

Duration: 1.5 hours
Format: Start-Stop-Continue

What went well (Continue):
- Daily standups kept us aligned
- Code reviews caught issues early
- Pair programming helped with complex tasks

What could improve (Start):
- More automated testing
- Better documentation
- Earlier communication about blockers

What to stop:
- Over-committing in sprint planning
- Late-stage changes to sprint goal
- Skipping refactoring time

Action Items:
1. Add 2 hours for automated testing (Owner: Alice, Due: Next sprint)
2. Create documentation template (Owner: Bob, Due: 2 weeks)
3. Review blockers in standup (Owner: Scrum Master, Due: Immediate)
```

---

## User Stories

### Format

**Standard Template**
```
As a [type of user],
I want [some goal],
So that [some reason].
```

### INVEST Criteria

- **Independent**: Can be developed separately
- **Negotiable**: Details can be discussed
- **Valuable**: Delivers value to users
- **Estimable**: Can be estimated
- **Small**: Can be completed in a sprint
- **Testable**: Can be verified

### User Story Example

```
User Story: User Registration

As a new user,
I want to register for an account,
So that I can access the application.

Acceptance Criteria:
1. User can enter email and password
2. User receives confirmation email
3. User can activate account via email link
4. User can log in after activation
5. Duplicate emails are rejected

Story Points: 8
Priority: High
Sprint: 1
```

### Story Mapping

```
User Journey: E-commerce Purchase

| Browsing | Selection | Checkout | Post-Purchase |
|----------|-----------|----------|---------------|
| Search   | Add to cart | Enter shipping | Order confirmation |
| Filter   | View cart | Enter payment | Shipping updates |
| Sort     | Remove items | Review order | Return items |
| View details | Apply coupon | Place order | Rate product |
```

### Best Practices

1. **Write from user perspective**: Focus on value
2. **Keep it simple**: One story, one value
3. **Include acceptance criteria**: Clear definition of done
4. **Estimate together**: Team consensus
5. **Prioritize value**: Most valuable first

---

## Best Practices

### General Agile Best Practices

1. **Embrace change**: Requirements will evolve
2. **Deliver value frequently**: Working software
3. **Collaborate continuously**: Stay connected
4. **Improve constantly**: Learn and adapt
5. **Keep it simple**: Avoid over-engineering

### Scrum Best Practices

1. **Respect the framework**: Follow Scrum rules
2. **Empower the team**: Self-organization
3. **Maintain transparency**: Open communication
4. **Focus on quality**: Definition of Done
5. **Continuous improvement**: Regular retrospectives

### Kanban Best Practices

1. **Visualize everything**: Make work visible
2. **Limit WIP**: Focus on completion
3. **Manage flow**: Optimize throughput
4. **Make policies explicit**: Clear rules
5. **Improve collaboratively**: Team optimization

### XP Best Practices

1. **Pair programming**: Knowledge sharing
2. **Test-driven development**: Quality first
3. **Continuous integration**: Frequent integration
4. **Refactoring**: Clean code
5. **Simple design**: Do the simplest thing

---

## Common Mistakes

### Planning Mistakes

1. **Over-committing**: Taking on too much
2. **Under-estimating**: Not enough time
3. **Skipping grooming**: Unprepared backlog
4. **Ignoring dependencies**: Not coordinating
5. **No sprint goal**: Lack of focus

### Execution Mistakes

1. **Scope creep**: Adding work mid-sprint
2. **Skipping standups**: Losing alignment
3. **No code reviews**: Quality suffers
4. **Ignoring impediments**: Blockers remain
5. **Poor communication**: Misalignment

### Retrospective Mistakes

1. **Blaming individuals**: Focus on process
2. **No action items**: Nothing changes
3. **Skipping follow-up**: Actions forgotten
4. **Not safe environment**: Honest feedback missing
5. **Same format always**: Becomes stale

### User Story Mistakes

1. **Not INVEST**: Poor quality stories
2. **Missing acceptance criteria**: Unclear requirements
3. **Too large**: Can't be completed
4. **Not estimated**: No planning
5. **Not prioritized**: Wrong order

---

## Key Takeaways

1. **Agile is a mindset**: Not just practices
2. **Scrum provides structure**: Roles, events, artifacts
3. **Kanban optimizes flow**: Visualize and limit WIP
4. **XP emphasizes quality**: Pair programming, TDD
5. **SAFe scales Agile**: For large organizations
6. **Planning is collaborative**: Team ownership
7. **Standups sync the team**: Daily alignment
8. **Retrospectives improve**: Continuous improvement
9. **User stories focus on value**: User perspective
10. **Practice makes perfect**: Learn by doing

---

## Additional Resources

- [SDLC](../sdlc/README.md) - Software Development Life Cycle
- [Engineering Principles](../engineering-principles/README.md) - Core principles
- [Clean Code](../clean-code/README.md) - Writing quality code
- [Books](../books/README.md) - Recommended reading
- [FAQs](../faqs/README.md) - Common questions

---

*Last Updated: August 2026*

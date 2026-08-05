# Mob Programming

## Overview

Mob programming is a software development approach where the whole team works together on the same thing, at the same time, in the same space. It takes pair programming to the next level by involving the entire team in real-time collaboration.

## Core Concept

```
┌─────────────────────────────────────────────────┐
│                  MOB SESSION                     │
│                                                  │
│  ┌───────────────────────────────────────────┐  │
│  │           Shared Screen/Driver            │  │
│  │                                           │  │
│  │  ┌─────────────────────────────────────┐  │  │
│  │  │                                     │  │  │
│  │  │         Code Being Written          │  │  │
│  │  │                                     │  │  │
│  │  └─────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────┘  │
│                                                  │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐       │
│  │ D1  │ │ D2  │ │ D3  │ │ D4  │ │ D5  │       │
│  │     │ │     │ │     │ │     │ │     │       │
│  │ Nav │ │ Nav │ │Nav★ │ │ Nav │ │ Nav │       │
│  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘       │
│                                                  │
│  ★ = Current Driver                              │
│  Everyone else = Navigator                       │
└─────────────────────────────────────────────────┘
```

## Benefits

### Collective Knowledge
- Everyone knows the entire codebase
- No single points of failure
- Shared context and understanding
- Faster onboarding for new members

### High Quality Code
- Multiple perspectives on every line
- Real-time code review
- Immediate feedback and correction
- Simpler, more thoughtful design

### Team Cohesion
- Stronger working relationships
- Improved communication skills
- Shared ownership and responsibility
- Reduced conflicts and silos

### Problem Solving
- Diverse viewpoints on challenges
- Creative solutions emerge
- Faster debugging
- Better architectural decisions

## Mob Programming Styles

### Strong Driver
One person drives, others navigate:

```
Duration: 10-15 minutes per driver
Process:
1. Driver types code
2. Team provides direction
3. Timer rings
4. Next person becomes driver
5. Previous driver returns to team
```

### Weak Driver
Driver has less autonomy:

```
Duration: 5-10 minutes per driver
Process:
1. Team decides what to type
2. Driver types exactly that
3. Quick rotations
4. More collaborative feel
```

### Consultant Driver
Driver consults team frequently:

```
Duration: 15-20 minutes per driver
Process:
1. Driver proposes approach
2. Team provides input
3. Driver adjusts
4. Continues until rotation
```

## Session Structure

### Setup
```markdown
## Pre-Session Checklist

- [ ] Define session goal
- [ ] Set up shared screen
- [ ] Prepare development environment
- [ ] Gather team (3-6 people ideal)
- [ ] Set rotation timer (10-15 min)
- [ ] Establish communication norms
```

### Session Flow
```markdown
## Mob Session Agenda (2 hours)

0:00 - 0:10  - Goal review and setup
0:10 - 0:25  - Driver 1
0:25 - 0:40  - Driver 2
0:40 - 0:55  - Driver 3
0:55 - 1:05  - Break
1:05 - 1:20  - Driver 4
1:20 - 1:35  - Driver 5
1:35 - 1:50  - Driver 6
1:50 - 2:00  - Wrap-up and next steps
```

### Rotation Mechanics
```yaml
rotation:
  timer: "10-15 minutes"
  signal: "Audible chime"
  process:
    - Current driver stops typing
    - Next driver takes keyboard
    - Brief transition (30 seconds)
    - Continue coding
  exceptions:
    - "In the middle of a thought"
    - "During critical debugging"
    - "When team agrees to extend"
```

## Communication Patterns

### Verbal Communication
```markdown
## Effective Verbal Patterns

### Navigator Suggestions
"Consider adding a null check here"
"What about handling the error case?"
"Could we extract this into a function?"

### Driver Explanations
"I'm creating a new class for this"
"This handles the edge case where..."
"I'm following the pattern from..."

### Team Discussion
"Let's discuss the approach before coding"
"Two options: A or B?"
"Everyone agree on this direction?"
```

### Non-Verbal Communication
```markdown
## Visual Aids

- Whiteboard for architecture diagrams
- Sticky notes for task breakdown
- Timer for rotations
- Shared notes for decisions
```

## Team Composition

### Ideal Team Size
```markdown
## Team Size Guidelines

| Size | Pros | Cons |
|------|------|------|
| 3 | Intimate, focused | Limited perspectives |
| 4-5 | Good balance | Can be noisy |
| 6 | Diverse input | Hard to coordinate |
| 7+ | Many perspectives | Difficult to manage |

Recommended: 4-5 people
```

### Role Distribution
```markdown
## Role Responsibilities

### Driver (1 person)
- Types the code
- Follows team direction
- Explains thinking
- Asks for clarification

### Navigators (Everyone else)
- Think strategically
- Review code in real-time
- Suggest improvements
- Research solutions
- Take notes
- Manage timer
```

## Tools and Setup

### Physical Space
```markdown
## Room Setup

### Essential Elements
- Large screen or projector
- Comfortable seating for all
- Whiteboard for diagrams
- Good lighting and ventilation
- Minimal distractions

### Layout Options
┌─────────────────────────────────┐
│         Presentation Screen     │
├─────────────────────────────────┤
│                                 │
│    ┌───┐ ┌───┐ ┌───┐ ┌───┐    │
│    │ 1 │ │ 2 │ │ 3 │ │ 4 │    │
│    └───┘ └───┘ └───┘ └───┘    │
│                                 │
│         ┌───┐ ┌───┐            │
│         │ 5 │ │ 6 │            │
│         └───┘ └───┘            │
│                                 │
└─────────────────────────────────┘
```

### Remote Setup
```yaml
remote_tools:
  video:
    - Zoom with screen sharing
    - Microsoft Teams
    - Google Meet
  code_sharing:
    - VS Code Live Share
    - GitHub Codespaces
    - Tuple
    - tmate
  collaboration:
    - Miro (whiteboarding)
    - Notion (notes)
    - Slack (communication)
```

## Common Challenges

### Dominant Personalities
```markdown
## Solutions

1. **Structured Rotation**
   - Strict timer enforcement
   - Everyone drives equally
   - Rotate discussion leaders

2. **Communication Norms**
   - One person speaks at a time
   - Use "round robin" for input
   - Anonymous idea generation

3. **Retrospectives**
   - Discuss team dynamics
   - Adjust processes
   - Get feedback
```

### Fatigue
```markdown
## Solutions

1. **Regular Breaks**
   - 5-minute break every hour
   - Stand and stretch
   - Hydrate and snack

2. **Session Length**
   - Keep sessions to 2 hours max
   - Shorter sessions for complex topics
   - Multiple sessions over days

3. **Energy Management**
   - High-energy tasks in morning
   - Creative work when fresh
   - Routine tasks when tired
```

### Remote Challenges
```markdown
## Solutions

1. **Connection Quality**
   - Use wired internet
   - Good headset/webcam
   - Low-latency tools

2. **Engagement**
   - Video on when possible
   - Active participation
   - Regular check-ins

3. **Time Zones**
   - Find overlapping hours
   - Async collaboration tools
   - Document everything
```

## When to Mob Program

### Ideal Scenarios
```markdown
## Good Mob Programming Tasks

- Complex problem solving
- Architecture decisions
- Critical bug fixes
- New team member onboarding
- Learning new technology
- Sprint planning
- Design reviews
- Prototype development
```

### Less Ideal Scenarios
```markdown
## Poor Mob Programming Tasks

- Simple, repetitive tasks
- Individual research
- Documentation writing
- Configuration changes
- Tasks requiring deep focus
- Time-sensitive deadlines
```

## Measuring Success

### Quantitative Metrics
```markdown
## Mob Metrics

| Metric | Target | Measurement |
|--------|--------|-------------|
| Knowledge sharing | 100% | Survey |
| Code quality | +30% | Defect rate |
| Onboarding time | -50% | Time to productivity |
| Team velocity | +20% | Story points |
```

### Qualitative Metrics
```markdown
## Team Feedback

- "Everyone understands the entire codebase"
- "We solve problems faster together"
- "Code quality is consistently high"
- "New members ramp up quickly"
```

## Best Practices

### Do's
```markdown
## Do

- Keep sessions focused (2 hours max)
- Rotate regularly (10-15 min)
- Take breaks
- Include everyone in discussion
- Document decisions
- Celebrate successes
- Retrospect regularly
```

### Don'ts
```markdown
## Don't

- Mob all day every day
- Let individuals dominate
- Skip breaks
- Ignore team dynamics
- Forget to rotate
- Avoid difficult topics
```

## Getting Started

### Week 1: Pilot
```markdown
## Pilot Session

1. Choose a small, well-defined task
2. Get 3-4 volunteers
3. Set up tools and environment
4. Run 1-hour session
5. Get feedback
```

### Week 2: Expand
```markdown
## Expansion

1. Include more team members
2. Try different task types
3. Refine rotation timing
4. Improve communication
```

### Week 3: Optimize
```markdown
## Optimization

1. Establish regular mob sessions
2. Create team norms
3. Measure and improve
4. Share learnings
```

## Related Topics

- [Pair Programming](../pair-programming/README.md)
- [Code Reviews](../code-reviews/README.md)
- [Engineering Culture](../engineering-culture/README.md)
- [Team Collaboration](../agile/README.md)

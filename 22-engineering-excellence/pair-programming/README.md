# Pair Programming

## Overview

Pair programming is a collaborative technique where two developers work together at one workstation. One writes code (driver) while the other reviews and guides (navigator). This practice produces higher-quality code, shares knowledge, and builds team cohesion.

## Benefits

### Code Quality
- Real-time code review catches bugs immediately
- Two perspectives on every decision
- Simpler, more readable code
- Fewer defects in production

### Knowledge Transfer
- Spread domain knowledge quickly
- Onboard new team members faster
- Reduce bus factor
- Cross-train on technologies

### Productivity
- Faster problem-solving
- Less time stuck on difficult problems
- Better design decisions
- Reduced context switching

### Team Building
- Build trust and rapport
- Improve communication skills
- Create shared ownership
- Reduce silos

## Pair Programming Styles

### Driver-Navigator
The classic approach:

```
Driver: Types the code
Navigator: Reviews, thinks strategically

Switch roles every 25-30 minutes
```

**When to use:**
- Complex algorithms
- Critical production code
- Onboarding new team members
- Learning new technologies

### Ping-Pong
TDD-focused pairing:

```
1. Developer A writes a failing test
2. Developer B writes the implementation
3. Developer B writes a failing test
4. Developer A writes the implementation
5. Repeat
```

**When to use:**
- Test-driven development
- Quick iterations
- Keeping both engaged
- Building test coverage

### Strong-Style
"An idea must pass through the navigator's hands before code":

```
Navigator: "Create a function called calculateTotal"
Driver: Types exactly that
Navigator: "Add parameters for items and taxRate"
Driver: Types exactly that
```

**When to use:**
- Teaching specific patterns
- Enforcing coding standards
- Critical security code
- Strict code quality requirements

### Unstructured
Casual collaboration:

```
Both developers discuss and code together
Less formal role switching
More like collaborative exploration
```

**When to use:**
- Brainstorming
- Debugging sessions
- Exploring new approaches
- Low-stakes code

## Setup and Environment

### Physical Setup
```
┌─────────────────────────────────────┐
│                                     │
│   ┌─────────┐     ┌─────────┐      │
│   │ Driver  │     │Navigator│      │
│   │         │     │         │      │
│   │  ┌───┐  │     │  ┌───┐  │      │
│   │  │ 🖥️ │  │     │  │ 🖥️ │  │      │
│   │  └───┘  │     │  └───┘  │      │
│   │         │     │         │      │
│   └─────────┘     └─────────┘      │
│                                     │
│      Shared screen/projector        │
└─────────────────────────────────────┘
```

### Remote Setup
```yaml
# Tools for remote pairing
communication:
  - Zoom/Teams with screen sharing
  - Tuple (dedicated pairing tool)
  - VS Code Live Share
  - JetBrains Code With Me

code_sharing:
  - VS Code Live Share
  - GitHub Codespaces
  - Gitpod
  - tmate (terminal sharing)

examples:
  # VS Code Live Share
  - Install Live Share extension
  - Click "Share" in top-right
  - Send link to partner
  - Both can edit simultaneously

  # Tuple
  - Download Tuple app
  - Create pairing session
  - Share screen and control
  - Low-latency connection
```

## Session Structure

### Before Pairing
```markdown
## Pre-Pairing Checklist

- [ ] Define session goal
- [ ] Gather relevant context
- [ ] Set up environment
- [ ] Agree on style/method
- [ ] Set timer for switches
```

### During Pairing
```markdown
## Pairing Session (2 hours)

0:00 - 0:05  - Goal review and setup
0:05 - 0:30 - Driver: Navigator pair 1
0:30 - 0:35 - Switch and stretch
0:35 - 1:00 - Driver: Navigator pair 2
1:00 - 1:10 - Break
1:10 - 1:35 - Driver: Navigator pair 3
1:35 - 1:40 - Switch and stretch
1:40 - 2:00 - Driver: Navigator pair 4
```

### After Pairing
```markdown
## Post-Pairing Review

- Did we achieve the goal?
- What worked well?
- What should we try differently?
- Any follow-up items?
```

## Communication Techniques

### Navigator Responsibilities
```markdown
## Navigator Tasks

1. **Think Strategically**
   - Consider edge cases
   - Plan next steps
   - Identify potential issues

2. **Review Code**
   - Check for errors
   - Suggest improvements
   - Ensure standards compliance

3. **Research**
   - Look up documentation
   - Find examples
   - Check Stack Overflow

4. **Take Notes**
   - Document decisions
   - Record action items
   - Track follow-ups
```

### Driver Responsibilities
```markdown
## Driver Tasks

1. **Focus on Implementation**
   - Write clean code
   - Follow agreed patterns
   - Keep code simple

2. **Explain Thinking**
   - Verbalize approach
   - Ask for input
   - Discuss trade-offs

3. **Stay Flexible**
   - Accept suggestions
   - Try alternatives
   - Don't get attached
```

## Common Challenges

### Personality Conflicts
```markdown
## Solutions

1. **Set Clear Expectations**
   - Agree on goals upfront
   - Define roles clearly
   - Establish communication norms

2. **Take Breaks**
   - Step away when frustrated
   - Resume with fresh perspective
   - Address issues offline

3. **Rotate Partners**
   - Pair with different people
   - Learn different styles
   - Build team cohesion
```

### Skill Gaps
```markdown
## Solutions

1. **Teach, Don't Take Over**
   - Navigator explains concepts
   - Driver implements
   - Both learn

2. **Start Simple**
   - Choose appropriate tasks
   - Build confidence gradually
   - Celebrate progress

3. **Be Patient**
   - Allow learning time
   - Provide encouragement
   - Focus on growth
```

### Remote Pairing Challenges
```markdown
## Solutions

1. **Improve Connection**
   - Use low-latency tools
   - Ensure good internet
   - Use headsets for audio

2. **Maintain Engagement**
   - Take regular breaks
   - Use video when possible
   - Keep energy high

3. **Handle Time Zones**
   - Find overlapping hours
   - Use async tools when needed
   - Document decisions
```

## Measuring Success

### Quantitative Metrics
```markdown
## Pairing Metrics

| Metric | Before | After |
|--------|--------|-------|
| Defect rate | 15% | 8% |
| Onboarding time | 4 weeks | 2 weeks |
| Code coverage | 60% | 85% |
| Knowledge silos | 3 | 0 |
```

### Qualitative Metrics
```markdown
## Team Feedback

- "I learn something new every pairing session"
- "We catch bugs much earlier now"
- "Code quality has improved significantly"
- "I feel more confident in the codebase"
```

## Best Practices

### Do's
```markdown
## Do

- Switch roles regularly (25-30 min)
- Take breaks to avoid fatigue
- Communicate constantly
- Focus on learning, not just coding
- Keep pairs small (2 people)
- Pair on difficult/important tasks
- Celebrate successes together
```

### Don'ts
```markdown
## Don't

- Pair all day every day
- Let one person dominate
- Skip breaks
- Pair on trivial tasks
- Force pairing on everyone
- Ignore personality conflicts
- Forget to have fun
```

## When to Pair

### Good Candidates
```markdown
## Good Pairing Tasks

- Complex algorithms
- Critical production code
- New team member onboarding
- Learning new technology
- Debugging difficult issues
- Architecture decisions
- Security-sensitive code
```

### Poor Candidates
```markdown
## Poor Pairing Tasks

- Simple bug fixes
- Documentation updates
- Configuration changes
- Routine maintenance
- Individual research
- Tasks requiring deep focus
```

## Related Topics

- [Code Reviews](../code-reviews/README.md)
- [Mob Programming](../mob-programming/README.md)
- [TDD as Craft](../../README.md)
- [Learning Culture](../engineering-culture/learning/README.md)

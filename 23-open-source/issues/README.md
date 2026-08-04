# Issue Management

## Table of Contents

- [Introduction](#introduction)
- [Understanding Issues](#understanding-issues)
- [Writing Good Bug Reports](#writing-good-bug-reports)
- [Feature Requests](#feature-requests)
- [Issue Triage](#issue-triage)
- [Labeling Systems](#labeling-systems)
- [Issue Templates](#issue-templates)
- [Linking Issues and PRs](#linking-issues-and-prs)
- [Closing Issues](#closing-issues)
- [Best Practices](#best-practices)

---

## Introduction

Issues are the backbone of open source project management. They provide a structured way to track bugs, feature requests, and other work items. This guide covers best practices for creating, managing, and triaging issues.

Effective issue management helps maintainers organize work, contributors find tasks, and users report problems.

---

## Understanding Issues

### Types of Issues

#### Bug Reports
- Report software defects
- Document unexpected behavior
- Include reproduction steps

#### Feature Requests
- Suggest new functionality
- Propose improvements
- Request enhancements

#### Questions
- Ask for help
- Seek clarification
- Discuss approaches

#### Tasks
- Track work items
- Manage project milestones
- Organize development efforts

### Issue Lifecycle

```
1. Issue Created
2. Triaged (labeled, assigned)
3. In Progress
4. Pull Request Created
5. Code Review
6. Merged
7. Issue Closed
```

---

## Writing Good Bug Reports

### Essential Components

#### 1. Clear Title

```markdown
# Good
"Application crashes when uploading files larger than 10MB"

# Bad
"Bug"
"Upload doesn't work"
"Help!"
```

#### 2. Description

```markdown
## Description
A clear and concise description of what the bug is.

## Steps to Reproduce
1. Go to '...'
2. Click on '...'
3. Upload a file larger than 10MB
4. See error

## Expected Behavior
What you expected to happen.

## Actual Behavior
What actually happened.

## Environment
- OS: [e.g., macOS 12.0]
- Browser: [e.g., Chrome 96]
- Version: [e.g., 2.1.0]
```

#### 3. Additional Context

```markdown
## Screenshots
If applicable, add screenshots to help explain your problem.

## Logs
```
[Paste relevant logs here]
```

## Additional Context
Add any other context about the problem here.
```

### Bug Report Template

```markdown
## Bug Description
A clear description of the bug.

## Reproduction Steps
1. Step one
2. Step two
3. Step three

## Expected Behavior
What should happen.

## Actual Behavior
What actually happens.

## Environment
- **OS**: [e.g., Windows 10, macOS 12]
- **Browser**: [e.g., Chrome 96, Firefox 95]
- **Version**: [e.g., 1.2.3]
- **Node.js**: [e.g., 16.13.0]

## Screenshots
If applicable, add screenshots.

## Additional Context
Any other relevant information.
```

---

## Feature Requests

### Writing Feature Requests

```markdown
## Feature Description
A clear description of the feature you'd like.

## Problem Statement
What problem does this feature solve?

## Proposed Solution
How do you envision this feature working?

## Alternatives Considered
Other solutions you've considered.

## Additional Context
Mockups, examples, or references.
```

### Feature Request Template

```markdown
## Is your feature request related to a problem?
A clear description of the problem. E.g., "I'm always frustrated when..."

## Describe the solution you'd like
A clear description of what you want to happen.

## Describe alternatives you've considered
Any alternative solutions or features you've considered.

## Additional context
Add any other context or screenshots about the feature request here.
```

### Evaluating Feature Requests

Consider these factors:

1. **Impact**: How many users will benefit?
2. **Effort**: How much work is required?
3. **Alignment**: Does it fit the project's goals?
4. **Feasibility**: Is it technically possible?
5. **Maintenance**: What's the long-term cost?

---

## Issue Triage

### What is Triage?

Triage is the process of reviewing, categorizing, and prioritizing issues. It helps maintainers focus on the most important work.

### Triage Process

#### 1. Initial Review

- **Duplicate Check**: Is this issue already reported?
- **Validity Check**: Is this a real bug or user error?
- **Completeness**: Does it have enough information?

#### 2. Labeling

Add appropriate labels:
- **Type**: bug, feature, documentation
- **Priority**: critical, high, medium, low
- **Status**: needs-triage, confirmed, in-progress
- **Difficulty**: good-first-issue, help-wanted

#### 3. Assignment

- **Assign to maintainer**: For core issues
- **Leave unassigned**: For community contributions
- **Mark for contribution**: For beginner-friendly issues

#### 4. Prioritization

Consider:
- **User Impact**: How many users affected?
- **Severity**: How bad is the issue?
- **Urgency**: How quickly does it need to be fixed?
- **Effort**: How much work is required?

### Triage Checklist

```markdown
## Issue Triage Checklist

### Initial Review
- [ ] Issue is not a duplicate
- [ ] Issue is valid and reproducible
- [ ] Issue has sufficient information
- [ ] Issue follows project guidelines

### Labeling
- [ ] Type label added (bug/feature/docs)
- [ ] Priority label added (critical/high/medium/low)
- [ ] Status label added (needs-triage/confirmed)
- [ ] Difficulty label added (if applicable)

### Assignment
- [ ] Assigned to appropriate maintainer (if needed)
- [ ] Marked for community contribution (if applicable)
- [ ] Milestone assigned (if applicable)

### Follow-up
- [ ] Requested information added
- [ ] Related issues linked
- [ ] Timeline communicated (if applicable)
```

---

## Labeling Systems

### Common Labels

#### Type Labels
- `bug`: Something isn't working
- `feature`: New feature request
- `enhancement`: Improvement to existing feature
- `documentation`: Documentation issue
- `question`: Further information requested

#### Priority Labels
- `critical`: Must be fixed immediately
- `high`: Important, should be fixed soon
- `medium`: Normal priority
- `low`: Nice to have, not urgent

#### Status Labels
- `needs-triage`: Needs initial review
- `confirmed`: Issue confirmed and validated
- `in-progress`: Currently being worked on
- `needs-review`: Needs code review
- `on-hold`: Blocked or waiting

#### Difficulty Labels
- `good-first-issue`: Great for newcomers
- `help-wanted`: Community contributions welcome
- `advanced`: Requires significant expertise
- `expert-only`: Only for experienced maintainers

### Creating a Labeling System

1. **Start Simple**: Begin with basic labels
2. **Be Consistent**: Use the same labels across issues
3. **Evolve**: Add labels as needed
4. **Document**: Maintain a label guide

### Label Management

```bash
# Using GitHub CLI to manage labels
gh label create "bug" --color "d73a4a" --description "Something isn't working"
gh label create "feature" --color "a2eeef" --description "New feature request"
gh label create "good-first-issue" --color "7057ff" --description "Good for newcomers"
```

---

## Issue Templates

### Bug Report Template

```markdown
---
name: Bug Report
about: Report a bug to help us improve
title: '[BUG] '
labels: bug
assignees: ''
---

## Bug Description
A clear description of the bug.

## Reproduction Steps
1. Go to '...'
2. Click on '...'
3. Scroll down to '...'
4. See error

## Expected Behavior
What you expected to happen.

## Actual Behavior
What actually happened.

## Screenshots
If applicable, add screenshots.

## Environment
- OS: 
- Browser: 
- Version: 

## Additional Context
Any other context about the problem.
```

### Feature Request Template

```markdown
---
name: Feature Request
about: Suggest an idea for this project
title: '[FEATURE] '
labels: enhancement
assignees: ''
---

## Is your feature request related to a problem?
A clear description of the problem. E.g., "I'm always frustrated when..."

## Describe the solution you'd like
A clear description of what you want to happen.

## Describe alternatives you've considered
Any alternative solutions or features you've considered.

## Additional context
Add any other context or screenshots about the feature request here.
```

### Creating Templates

1. **Create `.github/ISSUE_TEMPLATE/` directory**
2. **Add template files** (`.md` files)
3. **Configure template chooser** (optional)

---

## Linking Issues and PRs

### Linking Syntax

Reference issues in PRs or commits:

```markdown
# In commit messages
git commit -m "fix: resolve timeout issue

Fixes #123"

# In PR descriptions
Fixes #123
Closes #456
Resolves #789

# In comments
This PR addresses #123
See also: #456
```

### Link Types

| Syntax | Effect |
|--------|--------|
| `Fixes #123` | Closes issue when PR is merged |
| `Closes #123` | Closes issue when PR is merged |
| `Resolves #123` | Closes issue when PR is merged |
| `See #123` | Links but doesn't close |
| `Related to #123` | Links but doesn't close |

### Best Practices

1. **Always Link Issues**: Reference related issues in PRs
2. **Use Closing Keywords**: Use Fixes/Closes to auto-close issues
3. **Link in Both Directions**: Reference PRs in issues and vice versa
4. **Provide Context**: Explain how the PR relates to the issue

---

## Closing Issues

### When to Close

- **Fixed**: Issue is resolved by a merged PR
- **Duplicate**: Issue is already reported
- **Won't Fix**: Issue won't be addressed (with explanation)
- **Not a Bug**: Issue is not valid
- **Stale**: Issue is no longer relevant

### Closing Messages

```markdown
# Fixed
"Fixed in #123. Will be available in the next release."

# Duplicate
"Duplicate of #456. Closing this issue."

# Won't Fix
"We won't fix this because [reason]. Closing the issue."

# Not a Bug
"This is expected behavior. Closing the issue."
```

### Auto-Closing with PRs

Use closing keywords in PR descriptions:

```markdown
Fixes #123
Closes #456
Resolves #789
```

When the PR is merged, the linked issues will be automatically closed.

---

## Best Practices

### For Issue Creators

1. **Search First**: Check for existing issues
2. **Use Templates**: Follow issue templates
3. **Be Detailed**: Provide complete information
4. **Be Responsive**: Reply to follow-up questions
5. **One Issue Per Report**: Don't combine multiple issues

### For Maintainers

1. **Triage Regularly**: Review new issues frequently
2. **Be Responsive**: Acknowledge issues quickly
3. **Label Appropriately**: Use consistent labeling
4. **Communicate**: Keep issue authors informed
5. **Close When Done**: Don't leave resolved issues open

### For Contributors

1. **Claim Issues**: Ask to be assigned before starting work
2. **Provide Updates**: Comment on progress
3. **Ask Questions**: If unsure about something
4. **Follow Guidelines**: Adhere to project conventions
5. **Be Patient**: Maintainers are often busy

---

## Resources

### GitHub Documentation

- [About Issues](https://docs.github.com/en/issues/tracking-your-work-with-issues/about-issues)
- [Creating an Issue](https://docs.github.com/en/issues/tracking-your-work-with-issues/creating-an-issue)
- [Managing Issues](https://docs.github.com/en/issues/tracking-your-work-with-issues/managing-your-issues)

### Best Practices

- [How to Write a Great Issue Report](https://www.product-landing.com/great-issue-report/)
- [Issue Writing Best Practices](https://github.com/doorsontheriver/issue-writing-best-practices)

### Tools

- [GitHub CLI](https://cli.github.com/) - Manage issues from command line
- [GitHub Projects](https://docs.github.com/en/issues/organizing-your-work-with-project-boards) - Project boards

---

**Previous**: [Pull Requests](../pull-requests/README.md)
**Next**: [Code of Conduct](../code-of-conduct/README.md)

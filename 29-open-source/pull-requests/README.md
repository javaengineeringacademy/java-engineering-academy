# Pull Request Best Practices

## Table of Contents

- [Introduction](#introduction)
- [The Pull Request Workflow](#the-pull-request-workflow)
- [Creating a Pull Request](#creating-a-pull-request)
- [Writing Effective PR Descriptions](#writing-effective-pr-descriptions)
- [Code Review Best Practices](#code-review-best-practices)
- [Responding to Feedback](#responding-to-feedback)
- [Merging Strategies](#merging-strategies)
- [Common PR Mistakes](#common-pr-mistakes)
- [Advanced PR Techniques](#advanced-pr-techniques)

---

## Introduction

Pull requests are the heart of open source collaboration. They provide a structured way to propose changes, review code, and merge contributions. This guide covers best practices for creating, reviewing, and managing pull requests.

A well-crafted pull request makes it easy for maintainers to understand and review your changes, increasing the likelihood of acceptance.

---

## The Pull Request Workflow

### Overview

```
1. Fork repository
2. Clone locally
3. Create branch
4. Make changes
5. Commit changes
6. Push to fork
7. Create pull request
8. Address feedback
9. Merge
```

### Step-by-Step Process

#### 1. Keep Your Fork Updated

```bash
# Fetch upstream changes
git fetch upstream

# Switch to main branch
git checkout main

# Merge upstream changes
git merge upstream/main

# Push to your fork
git push origin main
```

#### 2. Create a Feature Branch

```bash
# Create and switch to a new branch
git checkout -b feature/add-login

# Or using GitHub CLI
gh issue develop <issue-number> --checkout
```

#### 3. Make Changes and Commit

```bash
# Make your changes
# ...

# Stage changes
git add .

# Commit with a descriptive message
git commit -m "feat: add user login functionality

- Implement login form component
- Add authentication API endpoint
- Add session management
- Include unit tests

Closes #123"
```

#### 4. Push and Create PR

```bash
# Push to your fork
git push origin feature/add-login

# Create pull request
gh pr create --title "Add user login functionality" --body "..."
```

---

## Creating a Pull Request

### PR Title

Write a clear, descriptive title:

```
# Good
fix: resolve timeout issue in API calls
feat: add user authentication
docs: update installation guide

# Bad
fixed stuff
update
WIP
```

### PR Template

Most projects provide a template. If not, use this structure:

```markdown
## Description

Brief description of what this PR does.

## Type of Change

- [ ] Bug fix (non-breaking change fixing an issue)
- [ ] New feature (non-breaking change adding functionality)
- [ ] Breaking change (fix or feature causing existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)

## Related Issues

Fixes #123
Closes #456

## How Has This Been Tested?

Describe the tests you ran to verify your changes.

## Checklist

- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review of my code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
```

### PR Description Best Practices

#### Start with Context

```markdown
## Problem

The application times out when making API calls to external services. 
This affects 20% of users and causes frustration.

## Solution

This PR adds retry logic with exponential backoff for failed API calls. 
It also increases the timeout threshold from 5s to 30s.
```

#### Explain Your Approach

```markdown
## Implementation Details

1. Created a `RetryClient` wrapper class
2. Implemented exponential backoff with jitter
3. Added configurable retry count and delay
4. Added comprehensive unit tests
```

#### Include Visual Changes

```markdown
## Screenshots

Before:
![Before](url-to-screenshot)

After:
![After](url-to-screenshot)
```

---

## Writing Effective PR Descriptions

### Structure

1. **Title**: Clear, concise summary
2. **Description**: What and why
3. **Changes**: List of specific changes
4. **Testing**: How you tested
5. **Related Issues**: Links to relevant issues
6. **Checklist**: Final verification

### Examples

#### Bug Fix PR

```markdown
fix: resolve memory leak in data processing

## Problem
The data processing module leaks memory when handling large datasets, 
causing the application to crash after extended use.

## Solution
Fixed the memory leak by properly closing file handles and 
implementing object pooling for frequently allocated objects.

## Changes
- Fixed file handle leak in `DataProcessor.process()`
- Implemented `ObjectPool` class for reuse
- Added memory monitoring in tests
- Updated documentation

## Testing
- Ran memory profiling tests showing 80% reduction in memory usage
- Added unit tests for memory management
- Tested with datasets up to 1GB

Fixes #234
```

#### Feature PR

```markdown
feat: add dark mode support

## Overview
Adds a dark mode toggle to the application, allowing users 
to switch between light and dark themes.

## Changes
- Added `ThemeContext` for theme management
- Created `ThemeToggle` component
- Updated all components to support both themes
- Added theme persistence in localStorage
- Updated documentation

## Screenshots
[Before/After screenshots]

## Testing
- Tested theme switching on Chrome, Firefox, Safari
- Verified accessibility in both themes
- Tested theme persistence across sessions

Closes #567
```

---

## Code Review Best Practices

### For Authors

1. **Self-Review First**
   - Read through all your changes
   - Check for typos and errors
   - Ensure code quality
   - Remove debug code

2. **Make Review Easy**
   - Keep PRs small and focused
   - Write clear descriptions
   - Add comments for complex logic
   - Link to relevant documentation

3. **Be Responsive**
   - Reply to feedback promptly
   - Ask for clarification when needed
   - Don't take feedback personally
   - Make requested changes

### For Reviewers

1. **Be Constructive**
   - Focus on the code, not the person
   - Explain why something should change
   - Suggest alternatives
   - Acknowledge good work

2. **Be Thorough**
   - Review all changes
   - Check for edge cases
   - Verify tests
   - Consider security implications

3. **Be Efficient**
   - Review in a timely manner
   - Prioritize critical issues
   - Use batch suggestions
   - Approve when ready

### Review Checklist

```markdown
## Code Quality
- [ ] Code is clean and readable
- [ ] Follows project style guidelines
- [ ] No code duplication
- [ ] Proper error handling
- [ ] Good variable/function names

## Functionality
- [ ] Changes work as expected
- [ ] Edge cases are handled
- [ ] No regression
- [ ] Performance is acceptable

## Testing
- [ ] Tests are added/updated
- [ ] Tests cover important cases
- [ ] All tests pass
- [ ] No test code in production

## Documentation
- [ ] Documentation is updated
- [ ] Comments explain complex logic
- [ ] README is updated (if needed)
- [ ] Changelog is updated (if needed)

## Security
- [ ] No security vulnerabilities
- [ ] Input validation
- [ ] Proper authentication/authorization
- [ ] No sensitive data exposed
```

---

## Responding to Feedback

### Positive Responses

```markdown
# Good
"Great catch! I've fixed that issue."

# Good
"You're right, that's a better approach. I've updated the code."

# Good
"Thanks for the suggestion! I've implemented it."
```

### When You Disagree

```markdown
# Good
"I understand your concern, but I think this approach is better because...

Let me know what you think."

# Good
"That's a valid point. I considered this approach, but chose this one because...

Happy to discuss further if you have concerns."
```

### Asking for Clarification

```markdown
# Good
"Could you clarify what you mean by...?"

# Good
"I'm not sure I understand the suggestion. Could you provide more context?"
```

---

## Merging Strategies

### Merge Commit

```bash
# Creates a merge commit, preserving all history
git merge --no-ff feature-branch
```

**Pros:**
- Preserves complete history
- Shows when features were merged
- Easy to revert entire features

**Cons:**
- Can create noisy history
- Many merge commits

### Squash and Merge

```bash
# Squashes all commits into one
git merge --squash feature-branch
git commit -m "feat: add user authentication"
```

**Pros:**
- Clean, linear history
- Easy to understand
- One commit per feature

**Cons:**
- Loses individual commit history
- Harder to revert specific changes

### Rebase and Merge

```bash
# Replays commits on top of main
git rebase main
git checkout main
git merge --ff-only feature-branch
```

**Pros:**
- Clean, linear history
- Preserves individual commits
- No merge commits

**Cons:**
- Can be confusing
- Rewrites history
- Can cause conflicts

### Choosing a Strategy

| Strategy | When to Use |
|----------|-------------|
| **Merge Commit** | Large features, preserving history |
| **Squash and Merge** | Small features, clean history |
| **Rebase and Merge** | Linear history, preserving commits |

---

## Common PR Mistakes

### Technical Mistakes

1. **Too Large**
   - PRs with hundreds of changes
   - Mix of unrelated changes
   - Hard to review

2. **No Tests**
   - Missing test coverage
   - Breaking existing tests
   - No integration tests

3. **Poor Code Quality**
   - Style violations
   - No error handling
   - Hardcoded values

### Communication Mistakes

1. **Unclear Description**
   - No explanation of changes
   - Missing context
   - No linked issues

2. **Not Responding**
   - Ignoring feedback
   - Slow responses
   - No updates

3. **Being Defensive**
   - Arguing with reviewers
   - Not accepting feedback
   - Taking criticism personally

### Process Mistakes

1. **Not Following Guidelines**
   - Ignoring CONTRIBUTING.md
   - Wrong branch naming
   - Missing commit message format

2. **Not Updating**
   - Outdated PR
   - Conflicts not resolved
   - Missing documentation updates

---

## Advanced PR Techniques

### Stacking PRs

For large features, stack multiple PRs:

```bash
# First PR
git checkout -b feat/add-auth-api
# Make changes, commit, push, create PR

# Second PR (based on first)
git checkout -b feat/add-auth-ui
# Make changes, commit, push, create PR with base: feat/add-auth-api
```

### Draft PRs

Use draft PRs for work in progress:

```bash
# Create draft PR
gh pr create --draft
```

**Benefits:**
- Get early feedback
- Signal work in progress
- Track progress

### Pull Request Reviews

Use GitHub's review features:

1. **Request Review**: Ask specific people to review
2. **Add Reviewers**: Assign reviewers automatically
3. **Review Comments**: Leave inline comments
4. **Suggest Changes**: Use GitHub's suggestion feature
5. **Approve/Request Changes**: Formal review status

---

## Resources

### Documentation

- [GitHub Pull Requests](https://docs.github.com/en/pull-requests)
- [Writing the Perfect Pull Request](https://www.freecodecamp.org/news/writing-the-perfect-pull-request/)
- [How to Write a Good Pull Request](https://blog.pragmaticengineer.com/good-pull-requests/)

### Tools

- [GitHub CLI](https://cli.github.com/) - Create PRs from command line
- [Pull Request Template](https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests/creating-a-pull-request-template-for-your-repository)

### Best Practices

- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
- [Git Flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)

---

**Previous**: [Contributing](../contributing/README.md)
**Next**: [Issues](../issues/README.md)

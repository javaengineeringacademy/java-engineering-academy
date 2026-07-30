# Contributing to Java Engineering Academy

Thank you for helping build Java Engineering Academy. This repository is intended to be a high-trust learning
resource, so contributions are reviewed for correctness, clarity, maintainability, and learner experience.

## Ways to Contribute

- Improve explanations, examples, and diagrams.
- Add exercises with tests and clear acceptance criteria.
- Expand interview preparation material.
- Fix build, tooling, or documentation issues.
- Review pull requests with constructive, evidence-based feedback.
- Report inaccuracies or unclear learning content.

## Before You Start

Open an issue before beginning large curriculum changes, new modules, architecture revisions, or project-wide
refactors. Small typo fixes, broken link fixes, and narrowly scoped improvements can go straight to a pull request.

## Local Development

Install:

- JDK 21
- Maven 3.8.6 or newer
- Git

Run the full verification suite before opening a pull request:

```bash
mvn clean verify
```

## Contribution Workflow

1. Fork the repository.
2. Create a branch from `main`.
3. Make a focused change.
4. Add or update tests when code behavior changes.
5. Run `mvn clean verify`.
6. Open a pull request with a clear description and testing notes.

## Content Standards

Learning content should be:

- Technically accurate for Java 21.
- Written in direct, inclusive language.
- Progressive: introduce concepts before relying on them.
- Practical: connect ideas to real engineering work.
- Reviewable: prefer small, coherent additions over large mixed changes.

Examples should:

- Compile without hidden dependencies.
- Use meaningful names.
- Avoid unnecessary cleverness.
- Prefer standard library features unless the lesson is about a third-party tool.
- Include tests when behavior matters.

## Code Standards

- Follow the Maven module structure.
- Keep packages under `academy.javaengineering`.
- Use JUnit 5 for tests.
- Keep examples deterministic and fast.
- Avoid committing generated build output.
- Let Checkstyle, SpotBugs, and JaCoCo guide quality.

## Commit Messages

Use concise, descriptive commit messages. Conventional Commit style is preferred:

```text
feat: add collections exercises
fix: correct generics example
docs: clarify learning path
test: add assertions for milestone validation
```

## Pull Request Checklist

- The change has a focused purpose.
- Documentation is updated when user-facing behavior or curriculum structure changes.
- Tests are added or updated where appropriate.
- `mvn clean verify` passes locally.
- The pull request explains what changed and how it was verified.

## Community Expectations

All contributors must follow the [Code of Conduct](CODE_OF_CONDUCT.md). Be respectful, specific, and helpful in
issues, reviews, and discussions.


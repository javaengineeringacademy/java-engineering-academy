# Contributing to Java Engineering Academy

Thank you for your interest in contributing! This guide will help you get started.

## Ways to Contribute

- **Content**: Create new topics, improve existing explanations
- **Code**: Add examples, fix bugs, improve implementations
- **Exercises**: Create practice problems with solutions
- **Interview**: Add interview questions by difficulty level
- **Documentation**: Improve README, fix typos, add diagrams
- **Testing**: Write tests for existing code
- **Review**: Review pull requests and provide feedback

## Local Development

### Prerequisites

- JDK 21
- Maven 3.8.6+
- Git

### Setup

```bash
# Fork the repository
git clone https://github.com/your-username/java-engineering-academy.git

# Navigate to project
cd java-engineering-academy

# Build and verify
mvn clean verify
```

## Topic Template

Every topic MUST follow this structure:

```
topic-name/
├── README.md              # The lesson
├── theory/                # Deep explanation
├── diagrams/              # Visual learning
├── examples/
│   ├── easy/              # Syntax & basics
│   ├── medium/            # Combined concepts
│   └── hard/              # Production-level
├── exercises/
│   ├── easy/
│   ├── medium/
│   └── hard/
├── assignments/           # Graded work
├── quiz/                  # Knowledge check
├── interview/             # Interview questions
├── pitfalls/              # Common mistakes
├── best-practices/        # Industry standards
├── real-world/            # Framework usage
├── references/            # External resources
└── solutions/             # Answer key
```

### README Template

Every topic README should include:

1. **Introduction** - What is this topic?
2. **Learning Objectives** - What will you learn?
3. **Prerequisites** - What do you need to know first?
4. **Why This Concept Exists** - The problem it solves
5. **Internal Working** - How it works technically
6. **Syntax** - Code syntax
7. **Easy Examples** - Basic usage
8. **Medium Examples** - Combined concepts
9. **Hard Examples** - Production-level
10. **Exercises** - Practice problems
11. **Interview Questions** - By difficulty level
12. **Common Pitfalls** - What to avoid
13. **Best Practices** - Industry standards
14. **Real World Usage** - Framework usage
15. **Summary** - Key takeaways

## Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat(scope): add new topic
fix(scope): correct explanation
docs(scope): improve documentation
test(scope): add unit tests
refactor(scope): improve code structure
```

Examples:

```
feat(oop): add sealed classes topic
fix(exercises): correct solution for exercise 3
docs(readme): improve learning path
test(encapsulation): add tests for Person class
```

## Pull Request Process

1. **Create a branch**: `git checkout -b feature/your-feature`
2. **Make changes**: Follow the topic template
3. **Run tests**: `mvn clean verify`
4. **Commit**: Use conventional commit messages
5. **Push**: `git push origin feature/your-feature`
6. **Open PR**: Fill out the PR template
7. **Address feedback**: Make requested changes
8. **Merge**: After approval

## Code Standards

### Java Code

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use Java 21 features where appropriate
- Write meaningful variable and method names
- Add Javadoc for public APIs
- Include unit tests

### Documentation

- Use clear, concise language
- Include code examples
- Add diagrams where helpful
- Cross-link related topics
- Keep formatting consistent

## Review Checklist

Before submitting a PR, verify:

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] Checkstyle passes
- [ ] PMD passes
- [ ] SpotBugs passes
- [ ] Documentation is clear
- [ ] Examples are correct
- [ ] No TODOs left in code
- [ ] Commit messages follow convention

## Getting Help

- **Issues**: [Report bugs or request features](https://github.com/javaengineeringacademy/java-engineering-academy/issues)
- **Discussions**: [Ask questions](https://github.com/javaengineeringacademy/java-engineering-academy/discussions)
- **Code of Conduct**: [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)

## Recognition

Contributors will be recognized in:

- README.md contributors section
- CHANGELOG.md for significant contributions
- Git history (always attributed)

Thank you for helping build the world's best Java Engineering curriculum!

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
- **Diagrams**: Create Mermaid/UML diagrams for visual learners

## Templates

We provide ready-to-use templates for all contribution types. **Always use these templates** to maintain consistency across the curriculum.

| Template | Purpose | Link |
|----------|---------|------|
| Topic Template | Full topic structure with README, examples, exercises | [templates/README.md](README.md) |
| Exercise Template | Practice problems with starter code and solutions | templates/exercise-template.md |
| Interview Template | Questions organized by difficulty level | templates/interview-template.md |
| Quiz Template | Multiple-choice knowledge checks | templates/quiz-template.md |

### How to Use Templates

1. Copy the directory structure from the [Topic Template](README.md)
2. Replace all `{{placeholders}}` with actual content
3. Follow the README.md template for consistent lesson format
4. Use the exercise template for practice materials
5. Use the interview template for interview prep
6. Use the quiz template for knowledge checks

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

See [templates/README.md](README.md) for the full template with all placeholders.

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

## Exercise Template

Every exercise should follow the exercise template:

- **Difficulty level**: Easy, Medium, or Hard
- **Estimated time**: How long it should take
- **Objective**: What students will practice
- **Requirements**: Clear, numbered steps
- **Starter code**: Java class with TODOs
- **Expected behavior**: Input/output examples
- **Evaluation criteria**: Checklist for self-assessment
- **Hints**: Progressive hints if stuck
- **Solution**: Link to the solution file

## Interview Template

Every interview question set should follow the interview template:

### Easy (0-2 years experience)
- Basic concept recall
- Simple code examples
- Definition questions

### Medium (2-5 years experience)
- Applied concepts
- Code walkthroughs
- Comparison questions

### Hard (5+ years experience)
- Architecture decisions
- Trade-off analysis
- Real-world scenarios

## Quality Standards

### Code Standards

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use Java 21 features where appropriate
- Write meaningful variable and method names
- Add Javadoc for public APIs
- Include unit tests

### Documentation Standards

- Use clear, concise language
- Include code examples
- Add Mermaid diagrams where helpful
- Cross-link related topics
- Keep formatting consistent

### Example Standards

- Every example must compile and run
- Include expected output
- Explain execution flow
- Note time/space complexity where relevant
- Show best practices

### Exercise Standards

- Clear problem statement
- Starter code provided
- Tests included
- Hints available
- Solution linked

## Review Process

### Before Submitting a PR

- [ ] Code compiles without errors: `mvn clean compile`
- [ ] All tests pass: `mvn test`
- [ ] Checkstyle passes: `mvn checkstyle:check`
- [ ] PMD passes: `mvn pmd:check`
- [ ] SpotBugs passes: `mvn spotbugs:check`
- [ ] Documentation is clear and complete
- [ ] Examples are correct and runnable
- [ ] No TODOs left in code
- [ ] Commit messages follow convention
- [ ] Topic follows the template structure
- [ ] Exercises have starter code and solutions
- [ ] Interview questions are organized by difficulty

### PR Review Checklist

Reviewers will check:

1. **Correctness**: Does the code compile and run?
2. **Quality**: Does it follow Google Java Style?
3. **Completeness**: Are all template sections filled?
4. **Clarity**: Is the documentation clear?
5. **Consistency**: Does it match existing topic style?
6. **Tests**: Are there adequate tests?
7. **Diagrams**: Are visual aids included where helpful?

### Review Timeline

- Initial review: Within 3 business days
- Follow-up review: Within 2 business days
- Merge: After approval and all checks pass

## Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat(scope): add new topic
fix(scope): correct explanation
docs(scope): improve documentation
test(scope): add unit tests
refactor(scope): improve code structure
diagrams(scope): add visual learning aids
```

Examples:

```
feat(oop): add sealed classes topic
fix(exercises): correct solution for exercise 3
docs(readme): improve learning path
test(encapsulation): add tests for Person class
diagrams(inheritance): add class hierarchy diagram
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

## Getting Help

- **Issues**: [Report bugs or request features](https://github.com/javaengineeringacademy/java-engineering-academy/issues)
- **Discussions**: [Ask questions](https://github.com/javaengineeringacademy/java-engineering-academy/discussions)
- **Templates**: [templates/README.md](README.md)
- **Code of Conduct**: [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)

## Recognition

Contributors will be recognized in:

- README.md contributors section
- CHANGELOG.md for significant contributions
- Git history (always attributed)

Thank you for helping build the world's best Java Engineering curriculum!

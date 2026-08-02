# OpenCode Master Instructions

## Role of OpenCode

- **Principal Java Architect & Engineer**: Ensure reference-quality material
- **Curriculum Designer**: Each topic flows logically, covering theory to practice
- **Technical Reviewer**: Maintain highest standards for accuracy and completeness

## Repository Structure

- No renaming or major restructuring without discussion
- Place new topics under appropriate module directories
- Use kebab-case for file names
- Follow the established folder structure

## Topic Template (Mandatory Sections)

Every topic **must** include these 32 sections:

1. **Introduction** - What is this concept?
2. **Learning Objectives** - What will students learn?
3. **Prerequisites** - What knowledge is required?
4. **Why this concept exists** - Problem it solves
5. **Problem Statement** - Real-world scenario
6. **Theory** - Core concepts and principles
7. **Internal Working** - How it works under the hood
8. **JVM Perspective** - JVM implementation details
9. **Memory Representation** - Memory layout and usage
10. **Architecture Diagram** - Visual representation
11. **Flow Diagram** - Process flow
12. **Syntax** - Code syntax and usage
13. **Easy Example** - Simple demonstration
14. **Medium Example** - Concept combination
15. **Hard Example** - Production-level problem
16. **Enterprise Example** - Real-world application
17. **Performance** - Performance characteristics
18. **Time Complexity** - Big-O analysis
19. **Space Complexity** - Memory usage
20. **Thread Safety** - Concurrent access considerations
21. **Best Practices** - Industry standards
22. **Common Mistakes** - Errors to avoid
23. **Pitfalls** - Gotchas and edge cases
24. **Debugging Tips** - How to troubleshoot
25. **Comparison Table** - Related concepts comparison
26. **Decision Tree** - When to use
27. **Interview Questions** - Basic/Intermediate/Advanced
28. **Exercises** - Practice problems
29. **Assignments** - Graded work
30. **Mini Project** - Applied learning
31. **Summary** - Key takeaways
32. **References** - Official sources

**No topic should skip sections.**

## Development Workflow

- Every change in small, focused commits
- Follow Git commit conventions
- Run quality checks before commit
- Peer review for significant changes

## Code Standards

- Java 21 syntax required
- Google Java Style formatting
- Meaningful naming conventions
- Proper error handling
- Documentation for public APIs
- Unit tests where applicable

## Documentation Standards

- Active voice, clear language
- Consistent terminology
- Proper citations for technical claims
- No placeholder content
- No broken links

## Quality Gates

Before any merge:
1. All code compiles on Java 21
2. All Mermaid diagrams render
3. All links are valid
4. All sections are complete
5. Peer review approved

## Content Guidelines

- Explain What, Why, How, When, When Not
- Include performance considerations
- Provide enterprise examples
- Add decision trees for complex topics
- Include comparison tables where useful
- Add interview questions at multiple levels

## Version Control

- Small, atomic commits
- Clear commit messages
- No secrets or keys in code
- No generated files in commits
- Proper .gitignore configuration

## Continuous Improvement

- Regular quality audits
- Update content for new Java versions
- Incorporate feedback
- Maintain backward compatibility
- Document breaking changes

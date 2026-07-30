## Pull Request Template

### Description
<!-- Provide a clear and concise description of the changes -->

### Type of Change
<!-- Mark the relevant option with an "x" -->
- [ ] Bug fix (non-breaking change that fixes an issue)
- [ ] New feature (non-breaking change that adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Code quality / Refactoring
- [ ] Test coverage improvement
- [ ] CI/CD configuration
- [ ] Sprint module content (theory, exercises, solutions)

### Related Issue
<!-- Link to the issue this PR addresses -->
Fixes #

### Sprint/Module
<!-- Which sprint/module does this change affect? -->
- [ ] Sprint 1: Java Fundamentals (`java-fundamentals/`)
- [ ] Sprint 2: OOP (`oop-fundamentals/`)
- [ ] Sprint 3: Collections & Generics
- [ ] Sprint 4: Java 8+ Functional Programming
- [ ] Sprint 5: Multithreading & Concurrency
- [ ] Sprint 6: JVM Internals
- [ ] Sprint 7: Design Patterns
- [ ] Sprint 8: Spring Framework
- [ ] Sprint 9: Spring Boot
- [ ] Sprint 10: Spring Security
- [ ] Sprint 11: Microservices
- [ ] Sprint 12: Cloud & DevOps
- [ ] Root / Cross-cutting

### Testing
<!-- Describe the tests you ran and how to reproduce -->
- [ ] Unit tests pass
- [ ] Integration tests pass (if applicable)
- [ ] Manual testing performed

**Test commands:**
```bash
mvn clean verify -pl <module-name>
```

### Quality Gates
<!-- Confirm all quality gates pass -->
- [ ] `mvn checkstyle:check` passes
- [ ] `mvn spotbugs:check` passes
- [ ] `mvn pmd:check` passes
- [ ] `mvn javadoc:javadoc` generates without warnings
- [ ] Test coverage maintained/improved

### Documentation
<!-- Has documentation been updated? -->
- [ ] README.md updated (if user-facing changes)
- [ ] CHANGELOG.md updated
- [ ] LEARNING_PATH.md updated (if curriculum changes)
- [ ] Module-level docs updated (theory, exercises, etc.)
- [ ] Javadoc added/updated for public APIs
- [ ] Mermaid diagrams render correctly

### Code Quality
- [ ] Follows Google Java Style (Checkstyle)
- [ ] No new SpotBugs warnings
- [ ] No new PMD violations
- [ ] No deprecated APIs used
- [ ] Meaningful variable/method/class names
- [ ] Proper Javadoc on public classes/methods
- [ ] Examples compile and run

### Breaking Changes
<!-- If this is a breaking change, describe the impact and migration path -->

### Screenshots / Diagrams
<!-- If applicable, add screenshots or updated Mermaid diagrams -->

### Checklist
- [ ] My code follows the style guidelines of this project
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] Any dependent changes have been merged and published

---

### For Reviewers
- [ ] Code compiles and tests pass
- [ ] Changes are minimal and focused
- [ ] Documentation is clear and accurate
- [ ] No security vulnerabilities introduced
- [ ] Performance impact considered
- [ ] Learning objectives still met (for content changes)
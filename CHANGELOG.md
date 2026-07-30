# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-07-30

### Added
- **Sprint 1: Java Fundamentals** - Complete module with 7 topics
  - Java Basics (HelloWorld, Comments, Variables, Program Structure)
  - Data Types (Primitives, Reference Types, Type Casting)
  - Operators (Arithmetic, Relational, Logical, Bitwise, Assignment)
  - Control Flow (If-Else, Switch, For, While/Do-While, Break/Continue)
  - Arrays (Basics, Multi-dimensional, Algorithms)
  - Strings (Basics, StringBuilder, Formatting, Algorithms)
  - Methods (Basics, Overloading, Varargs, Recursion)

- **Code Examples** - 27 production-quality Java files with Javadoc
- **Unit Tests** - 6 test classes with 100+ test methods
- **Documentation** - Theory, Exercises, Solutions, Quiz, Interview Questions
- **Assignment** - Temperature Converter CLI with full requirements
- **Quality Configuration** - Checkstyle (Google style), SpotBugs, PMD
- **Maven Multi-module** - Parent POM with dependency management
- **GitHub Actions CI** - Automated quality gates

### Quality Gates
- Checkstyle: Google Java Style (120 char lines, 2-space indent)
- SpotBugs: Max effort, Low threshold
- PMD: Custom ruleset (best practices, design, performance, security)
- JUnit 5: Minimum 80% code coverage target

---

## [Unreleased]

### Planned - Sprint 2: Object Oriented Programming
- Classes & Objects
- Constructors & Methods
- Encapsulation
- Inheritance & Polymorphism
- Abstraction & Interfaces
- Object Class (equals, hashCode, toString)
- Composition & Aggregation
- SOLID Principles Introduction
- Final Project: Bank Management System

---

## Release Checklist
- [ ] All tests pass
- [ ] Checkstyle passes
- [ ] SpotBugs passes
- [ ] PMD passes
- [ ] Javadoc generates without warnings
- [ ] CHANGELOG updated
- [ ] LEARNING_PATH updated
- [ ] Diagrams updated
- [ ] Version bumped
- [ ] Git tag created
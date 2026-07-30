# Java Engineering Academy

<p align="center">
  <img src="docs/images/java-engineering-academy-logo.svg" alt="Java Engineering Academy logo" width="160" />
</p>

<p align="center">
  <a href="https://github.com/javaengineeringacademy/java-engineering-academy/actions/workflows/build.yml">
    <img alt="Build" src="https://github.com/javaengineeringacademy/java-engineering-academy/actions/workflows/build.yml/badge.svg" />
  </a>
  <a href="https://github.com/javaengineeringacademy/java-engineering-academy/actions/workflows/test.yml">
    <img alt="Tests" src="https://github.com/javaengineeringacademy/java-engineering-academy/actions/workflows/test.yml/badge.svg" />
  </a>
  <a href="https://github.com/javaengineeringacademy/java-engineering-academy/actions/workflows/codeql.yml">
    <img alt="CodeQL" src="https://github.com/javaengineeringacademy/java-engineering-academy/actions/workflows/codeql.yml/badge.svg" />
  </a>
  <img alt="Java 21" src="https://img.shields.io/badge/Java-21-red" />
  <img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue" />
</p>

Java Engineering Academy is an open-source learning platform for engineers who want to master modern Java
through rigorous explanations, production-grade examples, testable exercises, and realistic interview preparation.

## Project Vision

The project exists to make Java education feel like professional engineering work. Every lesson, exercise, and
sample project should be clear enough for a beginner, deep enough for an experienced developer, and reliable
enough to run through the same quality gates used in production codebases.

Our goals are to:

- Build a complete Java 21 curriculum from fundamentals to distributed systems.
- Teach engineering habits alongside language features: testing, design, debugging, observability, and delivery.
- Provide realistic projects that can become portfolio-quality work.
- Maintain a welcoming, review-driven open-source community.
- Keep the repository structured, automated, and easy to contribute to from the first day.

## Learning Roadmap

| Stage | Focus | Outcomes |
| --- | --- | --- |
| 1 | Java fundamentals | Syntax, types, control flow, methods, records, packages, and exceptions |
| 2 | Object-oriented design | Encapsulation, inheritance, polymorphism, interfaces, SOLID, and composition |
| 3 | Collections and generics | Lists, maps, sets, iteration, generics, equality, ordering, and immutability |
| 4 | Testing and tooling | JUnit 5, Maven, debugging, static analysis, coverage, and CI |
| 5 | Concurrency | Threads, executors, virtual threads, synchronization, locks, and structured concurrency |
| 6 | Persistence and APIs | JDBC, transactions, HTTP APIs, validation, and integration testing |
| 7 | System design | Architecture, scalability, resilience, observability, and production readiness |
| 8 | Interview readiness | Data structures, algorithms, design questions, code reviews, and communication |

See [LEARNING_PATH.md](LEARNING_PATH.md) and [ROADMAP.md](ROADMAP.md) for the detailed curriculum plan.

## Repository Structure

```text
.
|-- .github/
|   |-- ISSUE_TEMPLATE/       Issue templates for community contributions
|   `-- workflows/            Build, test, and security automation
|-- config/
|   `-- checkstyle/           Static analysis configuration
|-- docs/                     Long-form documentation and reference material
|-- exercises/                Practice problems and guided exercises
|-- interview/                Interview preparation material
|-- modules/
|   `-- java-fundamentals/    First curriculum module
|-- projects/                 Portfolio-oriented Java projects
|-- resources/                Curated references and study resources
|-- pom.xml                   Maven parent build
`-- README.md
```

## Technology Stack

- Java 21
- Maven
- JUnit 5
- Maven Compiler Plugin
- Maven Surefire Plugin
- Maven Checkstyle Plugin
- SpotBugs Maven Plugin
- JaCoCo Maven Plugin
- GitHub Actions
- CodeQL

## Getting Started

Prerequisites:

- JDK 21
- Maven 3.8.6 or newer
- Git

Clone and verify the repository:

```bash
git clone https://github.com/javaengineeringacademy/java-engineering-academy.git
cd java-engineering-academy
mvn clean verify
```

## Contribution Guide

Contributions are welcome across lessons, exercises, examples, tests, documentation, and tooling. Start with
[CONTRIBUTING.md](CONTRIBUTING.md), follow the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md), and open an issue
before making large curriculum or architecture changes.

High-quality contributions should be:

- Accurate and aligned with Java 21.
- Small enough to review confidently.
- Covered by tests when code behavior changes.
- Written for learners, not only experts.
- Consistent with the repository structure and style.

## Roadmap

The first public milestones focus on the Java fundamentals module, core exercises, interview preparation, and
portfolio projects. See [ROADMAP.md](ROADMAP.md) for planned phases and contribution opportunities.

## Contact

- GitHub: [javaengineeringacademy](https://github.com/javaengineeringacademy)
- Issues: [Repository Issues](https://github.com/javaengineeringacademy/java-engineering-academy/issues)
- Discussions: [Repository Discussions](https://github.com/javaengineeringacademy/java-engineering-academy/discussions)

## License

This project is licensed under the [Apache License 2.0](LICENSE).


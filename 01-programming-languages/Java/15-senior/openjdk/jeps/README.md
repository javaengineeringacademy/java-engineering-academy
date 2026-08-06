# Java Enhancement Proposals (JEPs)

## What is a JEP?

A **Java Enhancement Proposal (JEP)** is a formal document proposing a new feature, change, or removal in Java. JEPs provide a structured way to propose, discuss, and implement changes to the Java platform.

### Purpose

- **Transparency**: All changes are publicly documented
- **Community input**: Anyone can comment and provide feedback
- **Traceability**: Every feature has a clear history and rationale
- **Planning**: Helps organize releases and prioritize work

### Structure of a JEP

```
Summary
Motivation
Description
Alternatives
Risks and Mitigations
Testing
References
```

## JEP Lifecycle

### Stages

```
Draft → Posted → Candidate → Final → Delivered
```

### 1. Draft

- **Status**: Initial proposal, not yet public
- **Purpose**: Author refines the idea
- **Duration**: 1-6 months
- **Outcome**: May be posted, withdrawn, or revised

### 2. Posted

- **Status**: Published for community review
- **Purpose**: Gather feedback and build consensus
- **Duration**: 1-3 months
- **Outcome**: May be moved to Candidate, withdrawn, or revised

### 3. Candidate

- **Status**: Accepted for inclusion in a release
- **Purpose**: Implementation begins
- **Duration**: 1-6 months
- **Outcome**: May be moved to Final, deferred, or withdrawn

### 4. Final

- **Status**: Implementation complete
- **Purpose**: Ready for release
- **Duration**: Until next release
- **Outcome**: Moved to Delivered

### 5. Delivered

- **Status**: Released in a specific Java version
- **Purpose**: Feature is part of Java
- **Duration**: Permanent
- **Outcome**: Available in JDK binaries

### Additional States

- **Deferred**: Postponed to a later release
- **Withdrawn**: Author or community decided not to proceed
- **Rejected**: Not accepted by the project

## How to Read a JEP

### Key Sections

1. **Summary**: One-sentence description
2. **Motivation**: Why this change is needed
3. **Description**: Technical details of the change
4. **Alternatives**: Other approaches considered
5. **Risks and Mitigations**: Potential issues and solutions
6. **Testing**: How the change will be tested
7. **References**: Related JEPs, bugs, and discussions

### Example JEP Structure

```markdown
# JEP 123: Variable Handles

## Summary
Define a standard set of variable handles...

## Motivation
Java lacks a standard way to access variables...

## Description
Introduce VarHandle class with methods for...

## Alternatives
Use Unsafe methods (not portable)
Use AtomicXxx classes (limited functionality)

## Risks and Mitigations
Risk: Compatibility with existing code
Mitigation: Provide migration guide

## Testing
Run existing tests and add new ones
```

## Key JEPs to Know

### Language Features

| JEP | Feature | Java Version |
|-----|---------|--------------|
| JEP 101 | Lambda expressions | Java 8 |
| JEP 154 | Remove Java EE and CORBA modules | Java 11 |
| JEP 286 | Local variable type inference (var) | Java 10 |
| JEP 305 | Pattern matching for instanceof | Java 16 |
| JEP 395 | Records and Sealed Classes | Java 16 |
| JEP 406 | Pattern matching for switch | Java 21 |

### JVM Improvements

| JEP | Feature | Java Version |
|-----|---------|--------------|
| JEP 158 | Unified JVM Logging | Java 9 |
| JEP 191 | Foreign Function Interface | Java 9 |
| JEP 277 | Enhanced Deprecation | Java 9 |
| JEP 333 | ZGC: A Scalable Low-Latency Garbage Collector | Java 11 |
| JEP 376: Shenandoah: A Low-Pause-Time Garbage Collector | Java 15 |
| JEP 451: Prepare to Disallow the Dynamic Loading of Agents | Java 21 |

### Library Additions

| JEP | Feature | Java Version |
|-----|---------|--------------|
| JEP 110 | HTTP/2 Client | Java 9 |
| JEP 255 | HTTP/2 Client updates | Java 11 |
| JEP 330 | Launch Single-File Source-Code Programs | Java 11 |
| JEP 360 | Sealed Classes (Preview) | Java 15 |
| JEP 412: Foreign Function & Memory API (Incubator) | Java 17 |
| JEP 454: Foreign Function & Memory API | Java 22 |

### Tooling and Runtime

| JEP | Feature | Java Version |
|-----|---------|--------------|
| JEP 223 | New Version-String Scheme | Java 9 |
| JEP 295 | Ahead-of-Time Compilation | Java 9 |
| JEP 361: Switch Expressions | Java 14 |
| JEP 394: Pattern Matching for instanceof | Java 16 |
| JEP 422: Linux/RISC-V Port | Java 21 |

## How to Propose a JEP

### Step 1: Research

- Check existing JEPs for similar ideas
- Review mailing list discussions
- Understand the technical background
- Identify potential impacts

### Step 2: Write the JEP

```markdown
# JEP XXX: Title

## Summary
One-sentence description.

## Motivation
Why this change is needed.

## Description
Technical details.

## Alternatives
Other approaches considered.

## Risks and Mitigations
Potential issues and solutions.

## Testing
How to test.

## References
Related work.
```

### Step 3: Discuss

- Post to the appropriate mailing list
- Get feedback from the community
- Iterate based on comments
- Find a sponsor (Oracle/Red Hat engineer)

### Step 4: Submit

- Submit to the JDK project
- Wait for review and decision
- Be prepared for multiple iterations
- Accept feedback gracefully

### Step 5: Implement

- Work with your sponsor
- Follow coding standards
- Write comprehensive tests
- Update documentation

## Mailing Lists for JEPs

| List | Purpose |
|------|---------|
| `jdk-dev` | General JDK development |
| `jdk-dev-discuss` | Early-stage proposals |
| `hotspot-dev` | JVM development |
| `compiler-dev` | Compiler (javac) |
| `core-libs-dev` | Core libraries |

## JEP Resources

- **OpenJDK JEPs**: [openjdk.org/jeps/](https://openjdk.org/jeps/)
- **JEP Index**: [openjdk.org/jeps/0](https://openjdk.org/jeps/0)
- **JEP Guide**: [openjdk.org/jeps/1](https://openjdk.org/jeps/1)
- **Bug Database**: [bugs.openjdk.org](https://bugs.openjdk.org)

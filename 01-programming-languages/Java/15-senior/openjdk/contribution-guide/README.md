# Contributing to OpenJDK

## How to Contribute

OpenJDK welcomes contributions from the community. Here's how to get started.

### 1. Set Up Development Environment

```bash
# Clone the repository
git clone https://github.com/openjdk/jdk.git
cd jdk

# Install required tools (macOS)
brew install autoconf bash coreutils wget

# Install required tools (Ubuntu)
sudo apt-get install build-essential autoconf
```

### 2. Understand the Codebase

- Start with `src/java.base/` for core classes
- Read existing code to understand conventions
- Review the OpenJDK Coding Style Guide
- Look at recent commits for patterns

### 3. Find Something to Work On

- **Bug Database**: [bugs.openjdk.org](https://bugs.openjdk.org)
- **Good First Issues**: Look for "good first issue" labels
- **Documentation**: Fix typos, improve examples
- **Tests**: Add missing test coverage

### 4. Create a Patch

```bash
# Create a new branch
git checkout -b my-feature

# Make changes
# ...

# Test your changes
make test TEST="relevant/test"

# Create a patch
git diff > my-feature.patch
```

### 5. Submit for Review

- **OpenJDK mailing lists**: Submit patches to the appropriate list
- **GitHub**: Use pull requests for minor fixes
- **Code review**: Be prepared for multiple review rounds

## JEP Process (Java Enhancement Proposals)

### What is a JEP?

A JEP is a proposal for a new feature or change to Java. JEPs provide a structured way to propose, discuss, and implement changes.

### JEP Lifecycle

```
Draft → Posted → Candidate → Final → Delivered
```

1. **Draft**: Initial proposal, not yet public
2. **Posted**: Published for community review
3. **Candidate**: Accepted for inclusion in a release
4. **Final**: Implementation complete
5. **Delivered**: Released in a specific Java version

### How to Propose a JEP

1. Write a JEP document using the template
2. Discuss on the appropriate mailing list
3. Get a sponsor (Oracle or Red Hat engineer)
4. Submit to the JDK project
5. Iterate based on feedback

## Code Review Process

### Review Standards

- **Correctness**: Does it work as intended?
- **Performance**: Any performance implications?
- **Security**: Any security concerns?
- **Compatibility**: Does it break existing code?
- **Style**: Follows OpenJDK coding conventions?
- **Tests**: Adequate test coverage?

### Review Tools

- **Code Review**: GitHub pull requests or mailing list patches
- **Static Analysis**: Run `make test` and check results
- **Performance**: Benchmark critical paths
- **Compatibility**: Run TCK tests if available

### Responding to Reviews

- Be respectful and professional
- Address all comments
- Explain your design decisions
- Make requested changes promptly
- Thank reviewers for their time

## Testing Requirements

### Test Types

| Test Type | Purpose | Command |
|-----------|---------|---------|
| Unit tests | Individual components | `make test TEST="jdk/...` |
| Regression tests | Bug fixes | `make test TEST="regression/..."` |
| Performance tests | Benchmarks | `make test TEST="micro/..."` |
| Stress tests | Stability | `make test TEST="stress/..."` |

### Writing Tests

```java
// Test naming convention: <ClassName>_<Method>_<Scenario>
@Test
public void testStringConcat_basic() {
    String result = "hello".concat(" world");
    assertEquals("hello world", result);
}
```

### Test Requirements

- Every bug fix should have a regression test
- New features should have comprehensive tests
- Performance changes should have benchmarks
- Tests should be deterministic (no flakiness)

## Mercurial → Git Migration

### History

OpenJDK originally used Mercurial (hg) for version control. In 2020, the project migrated to Git.

### Why the Migration?

- **Performance**: Git is faster for most operations
- **Tooling**: Better IDE support and ecosystem
- **GitHub**: Easier collaboration and code review
- **Community**: Lower barrier to contribution

### Impact on Contributors

- **New contributors**: Use Git and GitHub
- **Existing contributors**: Migrate workflows to Git
- **Old patches**: Can be converted from Mercurial to Git
- **Documentation**: Updated to reflect Git-based workflow

### Migration Tools

```bash
# Convert Mercurial repo to Git
hg clone https://hg.openjdk.java.net/jdk/jdk jdk-hg
cd jdk-hg
hg git-init
hg export --git > all-patches.txt

# Apply to Git repo
git am all-patches.txt
```

## Communication

### Mailing Lists

- **jdk-dev**: General JDK development discussions
- **jdk-dev-discuss**: Early-stage proposals
- **hotspot-dev**: HotSpot JVM development
- **compiler-dev**: Compiler (javac) development

### Best Practices

- Search archives before asking questions
- Provide minimal reproducible examples
- Be specific about your environment
- Follow up with solutions you find
- Be patient — responses may take time

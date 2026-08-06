# Code Quality

Measuring, maintaining, and improving code quality in Java projects.

## SonarQube Metrics

SonarQube provides comprehensive code quality analysis.

### Key Metrics
| Metric | Description | Target |
|--------|-------------|--------|
| **Reliability Rating** | A-E based on bugs | A |
| **Security Rating** | A-E based on vulnerabilities | A |
| **Maintainability Rating** | A-E based on code smells | A |
| **Coverage** | Unit test coverage | >80% |
| **Duplications** | Code duplication percentage | <3% |
| **Technical Debt** | Time to fix issues | <5% of dev time |

### SonarQube Rules
- **Bug**: Code error that will cause runtime failure
- **Vulnerability**: Security issue exploitable by attackers
- **Code Smell**: Maintainability issue
- **Duplication**: Repeated code blocks

### SonarQube Integration
```xml
<!-- Maven -->
<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>3.9.0.2184</version>
</plugin>

<!-- Gradle -->
plugins {
    id 'org.sonarqube' version '3.3'
}
```

```bash
# Run analysis
mvn sonar:sonar

# With properties
mvn sonar:sonar \
  -Dsonar.projectKey=my-project \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=token
```

## Code Smells

Code smells indicate maintainability issues.

### Common Code Smells
1. **Long Method**: Method >50 lines
2. **Large Class**: Class >500 lines
3. **Long Parameter List**: >4 parameters
4. **Duplicated Code**: Copy-pasted logic
5. **Dead Code**: Unused variables, methods, classes
6. **Complex Conditionals**: Nested if/else statements

### Detection Examples
```java
// Long Method
public void processOrder(Order order) { // >50 lines
    // Validation
    // Database operations
    // Business logic
    // Notifications
    // Logging
}

// Large Class
public class OrderService { // >500 lines
    // Too many responsibilities
}

// Long Parameter List
public void createUser(String name, String email, String phone,
                       String address, String city, String state) {
    // Should use a DTO or builder
}
```

### SonarQube Smell Detection
```xml
<!-- sonar-project.properties -->
sonar.issue.ignore.multicriteria=e1
sonar.issue.ignore.multicriteria.e1.ruleKey=squid:S1192
sonar.issue.ignore.multicriteria.e1.resourceKey=**/test/**
```

## Technical Debt Quantification

### Technical Debt Definition
The cost of additional rework caused by choosing an easy solution now instead of a better approach.

### Measurement Methods
1. **Remediation Effort**: Time to fix issues
2. **Debt Ratio**: Debt / Development time
3. **SQALE Rating**: A-E based on debt ratio

### Technical Debt Categories
- **Code Debt**: Poor code quality
- **Architecture Debt**: Poor design decisions
- **Documentation Debt**: Missing/outdated docs
- **Test Debt**: Insufficient testing
- **Infrastructure Debt**: Manual processes, missing automation

### Debt Reduction Strategy
1. **Identify**: Use SonarQube, code reviews
2. **Prioritize**: High-impact, low-effort first
3. **Schedule**: Allocate time each sprint
4. **Measure**: Track improvement over time
5. **Prevent**: Avoid new debt introduction

## Refactoring Strategies

### Refactoring Principles
1. **Boy Scout Rule**: Leave code cleaner than you found it
2. **Small Steps**: Make incremental changes
3. **Tests First**: Ensure tests pass before/after
4. **Version Control**: Commit frequently

### Refactoring Patterns
```java
// Extract Method
// Before
public void process() {
    // 50 lines of code
}

// After
public void process() {
    validate();
    calculate();
    save();
    notify();
}

// Extract Class
// Before
class Order {
    // Order fields
    // Payment fields
    // Shipping fields
}

// After
class Order { }
class Payment { }
class Shipping { }

// Replace Conditional with Polymorphism
// Before
class Shape {
    double area() {
        if (type == "circle") return PI * r * r;
        if (type == "rectangle") return w * h;
    }
}

// After
interface Shape { double area(); }
class Circle implements Shape { }
class Rectangle implements Shape { }
```

## Code Review Best Practices

### Code Review Checklist
- [ ] **Correctness**: Does it solve the problem?
- [ ] **Edge Cases**: Are边界条件 handled?
- [ ] **Error Handling**: Are errors handled gracefully?
- [ ] **Performance**: Is it efficient?
- [ ] **Security**: Are there vulnerabilities?
- [ ] **Readability**: Is it easy to understand?
- [ ] **Tests**: Are there adequate tests?
- [ ] **Documentation**: Is it documented?

### Review Guidelines
1. **Be Constructive**: Suggest improvements, don't criticize
2. **Ask Questions**: "Why did you choose this approach?"
3. **Focus on Code**: Not the person
4. **Prioritize**: Critical issues first
5. **Follow Up**: Ensure issues are addressed

### Automated Checks
```yaml
# GitHub Actions
name: Code Review
on: pull_request
jobs:
  review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: SonarQube Scan
        uses: sonarsource/sonarcloud-github-action@master
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

## Quality Gates

### Quality Gate Criteria
- **New Code Coverage**: >80%
- **Duplications**: <3%
- **Maintainability Rating**: A
- **Reliability Rating**: A
- **Security Rating**: A
- **Security Hotspots Reviewed**: >100%

### Enforcing Quality Gates
```bash
# With Maven
mvn sonar:sonar -Dsonar.qualitygate.wait=true

# In CI/CD
- name: Quality Gate Check
  run: |
    if ! mvn sonar:sonar -Dsonar.qualitygate.wait=true; then
      echo "Quality gate failed!"
      exit 1
    fi
```

## Static Analysis Tools

### Tool Comparison
| Tool | License | Features |
|------|---------|----------|
| SonarQube | Open Source | Comprehensive |
| Checkstyle | Open Source | Style checking |
| PMD | Open Source | Bug detection |
| SpotBugs | Open Source | Bug patterns |
| Error Prone | Open Source | Compiler checks |

### Integration
```xml
<!-- Checkstyle -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.2.0</version>
    <configuration>
        <configLocation>checkstyle.xml</configLocation>
    </configuration>
</plugin>

<!-- PMD -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>3.21.0</version>
</plugin>
```

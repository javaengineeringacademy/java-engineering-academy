# Internals: Code Quality Enforcement

## Checkstyle Rules for Logging

```xml
<!-- checkstyle.xml -->
<module name="RegexpSingleline">
    <property name="format" value="System\.out\.print"/>
    <property name="message" value="Use logger instead of System.out"/>
</module>

<module name="RegexpSingleline">
    <property name="format" value="System\.err\.print"/>
    <property name="message" value="Use logger instead of System.err"/>
</module>

<module name="AvoidStarImport"/>
```

## SonarQube Rules

| Rule | Description | Severity |
|------|-------------|----------|
| S2629 | Logs should be used instead of System.out | Major |
| S2139 | Exceptions should be logged | Major |
| S1148 | Exceptions should not be logged and re-thrown | Critical |
| S1166 | Exception handlers should preserve original exceptions | Major |
| S1142 | Methods should not have too many return statements | Minor |

## Custom Lint Rules

```java
// SpotBugs custom detector
public class LoggingBugDetector extends OpcodeStackDetector {
    
    @Override
    public void sawOpcode(int seen) {
        if (seen == INVOKEVIRTUAL) {
            String methodName = getNameConstantOperand();
            if (methodName.equals("println") || methodName.equals("print")) {
                if (getClassConstantOperand().equals("java/io/PrintStream")) {
                    reportMatch();
                }
            }
        }
    }
}
```

## IDE Inspections

### IntelliJ IDEA

- `Logger logger should be private static final`
- `Logger created with getClass()`
- `Log statement with string concatenation`
- `Exception logged without stack trace`

### Eclipse

- `System.out.println used`
- `Logger not static final`
- `String concatenation in log`

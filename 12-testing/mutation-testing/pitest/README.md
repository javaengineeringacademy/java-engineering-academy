# PITest (PIT)

## Overview
PITest is a mutation testing tool for Java that modifies code to create mutants, then tests if they are caught.

## Setup
### Maven
```xml
<plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>1.15.0</version>
    <configuration>
        <targetClasses>com.example.*</targetClasses>
        <targetTests>com.example.*</targetTests>
        <mutationThreshold>85</mutationThreshold>
    </configuration>
</plugin>
```

## Mutation Operators
- **Math**: `return a + b` -> `return 0`
- **Conditional**: `age >= 18` -> `age > 18`
- **Negate**: `x > 0` -> `!(x > 0)`
- **Void removal**: `foo()` -> removed

## Mutation Score
Score = Killed Mutants / Total Mutants * 100. Target: > 80%.

## Improving Score
```java
// Weak (low score)
assertNotNull(discount);
// Strong (high score)
assertEquals(10.00, discount, 0.01);
```

## Best Practices
1. Aim for 80%+ mutation score
2. Focus on survived mutants
3. Exclude getters/setters from mutations
4. Run in CI pipeline

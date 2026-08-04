# Checkstyle

## Overview

Checkstyle checks Java source code against coding standards and best practices.

## Configuration

### Maven Integration
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.0</version>
    <configuration>
        <configLocation>checkstyle.xml</configLocation>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
        <violationSeverity>warning</violationSeverity>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.puppycrawl.tools</groupId>
            <artifactId>checkstyle</artifactId>
            <version>10.12.5</version>
        </dependency>
    </dependencies>
</plugin>
```

### Gradle Integration
```groovy
plugins {
    id 'checkstyle'
}

checkstyle {
    toolVersion = '10.12.5'
    configFile = rootProject.file('config/checkstyle/checkstyle.xml')
    maxWarnings = 0
}
```

## Configuration File

```xml
<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
    "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
    "https://checkstyle.org/dtds/configuration_1_3.dtd">
<module name="Checker">
    <property name="charset" value="UTF-8"/>
    <property name="severity" value="error"/>
    
    <!-- File length -->
    <module name="FileLength">
        <property name="max" value="500"/>
    </module>
    
    <module name="TreeWalker">
        <!-- Naming conventions -->
        <module name="TypeName"/>
        <module name="ConstantName"/>
        <module name="LocalVariableName"/>
        <module name="MethodName"/>
        <module name="ParameterName"/>
        
        <!-- Coding -->
        <module name="EmptyStatement"/>
        <module name="EqualsHashCode"/>
        <module name="IllegalInstantiation"/>
        <module name="SimplifyBooleanExpression"/>
        <module name="SimplifyBooleanReturn"/>
        
        <!-- Design -->
        <module name="FinalClass"/>
        <module name="HideUtilityClassConstructor"/>
        <module name="InterfaceIsType"/>
        <module name="VisibilityModifier"/>
        
        <!-- Imports -->
        <module name="AvoidStarImport"/>
        <module name="IllegalImport"/>
        <module name="RedundantImport"/>
        <module name="UnusedImports"/>
        
        <!-- Metrics -->
        <module name="CyclomaticComplexity">
            <property name="max" value="10"/>
        </module>
        <module name="NPathComplexity">
            <property name="max" value="50"/>
        </module>
    </module>
</module>
```

## Checks

### Naming
| Check | Description |
|-------|-------------|
| TypeName | Class names |
| MethodName | Method names |
| ConstantName | Constants |
| LocalVariableName | Local variables |

### Coding
| Check | Description |
|-------|-------------|
| EmptyStatement | Empty statements |
| EqualsHashCode | equals() and hashCode() |
| SimplifyBoolean | Boolean simplification |

### Design
| Check | Description |
|-------|-------------|
| FinalClass | Final classes |
| HideUtilityClassConstructor | Utility class constructors |
| VisibilityModifier | Field visibility |

## Best Practices

1. Use consistent naming conventions
2. Keep methods short and focused
3. Avoid complex conditions
4. Remove unused imports
5. Follow naming standards
6. Use appropriate access modifiers
7. Keep classes focused
8. Run Checkstyle in CI/CD

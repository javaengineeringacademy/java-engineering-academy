# Maven - Solutions

## Multi-Module pom.xml

```xml
<!-- Parent pom.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>parent</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <modules>
        <module>core</module>
        <module>api</module>
    </modules>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>
</project>
```

## Fat JAR with Shade Plugin

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.1</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals><goal>shade</goal></goals>
            <configuration>
                <transformers>
                    <transformer implementation=
                        "org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.example.App</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Build Profile

```xml
<profiles>
    <profile>
        <id>dev</id>
        <properties>
            <app.env>development</app.env>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <app.env>production</app.env>
        </properties>
    </profile>
</profiles>
```

# Maven Internals

## How Maven Works

### POM Processing

Maven reads `pom.xml` and builds a model:

```
pom.xml → Maven Model → Dependency Resolution → Build Plan → Execution
```

### Repository Structure

```
~/.m2/repository/
├── groupId/artifactId/version/
│   ├── artifactId-version.jar
│   ├── artifactId-version.pom
│   └── _remote.repositories
```

### Dependency Resolution

1. Read pom.xml dependencies
2. Resolve transitive dependencies
3. Check local repository
4. Download from remote if needed
5. Build classpath

### Plugin Execution

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>21</source>
        <target>21</target>
    </configuration>
</plugin>
```

Maven calls plugin goals during lifecycle phases:
- `compile` → `compiler:compile`
- `test` → `surefire:test`
- `package` → `jar:jar`

### Build Lifecycle

```
validate → compile → test → package → verify → install → deploy
```

Each phase executes all plugin goals bound to that phase.

### Settings.xml

```xml
<settings>
    <localRepository>~/.m2/repository</localRepository>
    <profiles>
        <profile>
            <id>custom</id>
            <repositories>
                <repository>
                    <id>custom-repo</id>
                    <url>https://repo.example.com</url>
                </repository>
            </repositories>
        </profile>
    </profiles>
</settings>
```

### Parent POM Inheritance

```xml
<parent>
    <groupId>com.company</groupId>
    <artifactId>parent-pom</artifactId>
    <version>1.0.0</version>
</parent>

<!-- Inherits: properties, dependencies, plugins, profiles -->
```

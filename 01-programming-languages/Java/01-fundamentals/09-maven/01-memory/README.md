# Maven Memory Model

## Local Repository Memory

Maven caches dependencies locally:

```
~/.m2/repository/
├── org/apache/commons/commons-lang3/3.12.0/
│   ├── commons-lang3-3.12.0.jar    (~300KB)
│   ├── commons-lang3-3.12.0.pom    (~2KB)
│   └── _remote.repositories
```

### Repository Size

- Typical project: 50-200MB in local repo
- Large project: 500MB+ in local repo
- Each dependency: JAR + POM + metadata

### Build Memory Usage

```
Maven Build Memory:
├── JVM Heap: 256MB (default)
├── PermGen/Metaspace: 256MB (class metadata)
├── Stack: 1MB per thread
└── Native: OS resources
```

### Plugin Memory

Each plugin creates its own classloader:

```
Plugin ClassLoaders:
├── maven-compiler-plugin: Compiles Java sources
├── maven-surefire-plugin: Runs tests
├── maven-jar-plugin: Creates JAR
└── maven-resources-plugin: Copies resources
```

### Dependency Resolution Memory

```java
// Maven builds dependency tree in memory
// Each dependency has:
// - GAV coordinates
// - Scope
// - Exclusions
// - Transitive dependencies

// Memory: O(n) where n = total dependencies
```

### Build Output Memory

```
target/
├── classes/          # Compiled .class files
├── test-classes/     # Test .class files
├── surefire-reports/ # Test reports
├── *.jar             # Packaged artifact
└── *.war             # Web archive (if applicable)
```

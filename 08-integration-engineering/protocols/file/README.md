# Integration Protocols - File

## Overview

File integration enables reading from and writing to file systems, supporting batch processing and file-based workflows.

## Table of Contents

1. [File Integration Basics](#file-integration-basics)
2. [File Reading](#file-reading)
3. [File Writing](#file-writing)
4. [File Filtering](#file-filtering)
5. [File Polling](#file-polling)

## File Integration Basics

### File Operations

| Operation | Description |
|-----------|-------------|
| Read | Read file content |
| Write | Write file content |
| Copy | Copy files |
| Move | Move files |
| Delete | Delete files |
| List | List files |
| Poll | Watch for changes |

## File Reading

### Java NIO

```java
// Read entire file
String content = Files.readString(Path.of("/input/order.txt"));

// Read with encoding
String content = Files.readString(Path.of("/input/order.txt"), StandardCharsets.UTF_8);

// Read lines
List<String> lines = Files.readAllLines(Path.of("/input/order.txt"));
```

### Apache Commons IO

```java
// Read file to string
String content = FileUtils.readFileToString(new File("/input/order.txt"), "UTF-8");

// Read lines
List<String> lines = FileUtils.readLines(new File("/input/order.txt"), "UTF-8");
```

## File Writing

### Java NIO

```java
// Write string
Files.writeString(Path.of("/output/order.txt"), content);

// Write with options
Files.writeString(Path.of("/output/order.txt"), content, 
    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

// Write bytes
Files.write(Path.of("/output/order.dat"), data);
```

### Apache Commons IO

```java
// Write string
FileUtils.writeStringToFile(new File("/output/order.txt"), content, "UTF-8");

// Write lines
FileUtils.writeLines(new File("/output/order.txt"), lines);
```

## File Filtering

### Filename Filter

```java
// Filter by extension
File[] csvFiles = directory.listFiles((dir, name) -> name.endsWith(".csv"));

// Filter by pattern
FilenameFilter filter = new SimplePatternFileListFilter("*.txt");
File[] files = directory.listFiles();
```

### Content Filter

```java
// Read and filter
List<String> filteredLines = Files.lines(Path.of("/input/data.csv"))
    .filter(line -> line.contains("PROCESSED"))
    .collect(Collectors.toList());
```

## File Polling

### Scheduled Polling

```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
executor.scheduleAtFixedRate(() -> {
    File[] files = new File("/input").listFiles();
    for (File file : files) {
        processFile(file);
    }
}, 0, 5, TimeUnit.SECONDS);
```

### WatchService

```java
WatchService watchService = FileSystems.getDefault().newWatchService();
Path dir = Path.of("/input");
dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

while (true) {
    WatchKey key = watchService.take();
    for (WatchEvent<?> event : key.pollEvents()) {
        Path filename = (Path) event.context();
        processFile(dir.resolve(filename));
    }
    key.reset();
}
```

## Best Practices

1. **Use atomic operations**: Write to temp then rename
2. **Handle errors**: Handle file I/O exceptions
3. **File locking**: Use file locks for concurrent access
4. **Encoding**: Specify character encoding
5. **Cleanup**: Delete temporary files
6. **Monitoring**: Watch for file changes
7. **Backup**: Backup important files
8. **Security**: Validate file paths

## References

- [Java NIO Files](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html)
- [Apache Commons IO](https://commons.apache.org/proper/commons-io/)

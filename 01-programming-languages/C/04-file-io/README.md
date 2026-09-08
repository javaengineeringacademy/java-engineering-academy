# File I/O — C Language

## Why It Matters

When you're building any program that needs to persist data beyond execution — configuration files, logs, databases, saved state — you need file I/O. Without it, every program starts from scratch. C's `stdio.h` library works identically across Unix, Windows, and embedded systems, which is why C is used for configuration parsers, log processors, database engines, and data migration tools.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Text/binary file operations, cross-platform I/O | Memory-mapped files (`mmap`) for large files |
| When NOT to use | High-frequency small writes (buffering overhead) | Custom buffered I/O or `mmap` |
| Alternatives | `mmap` for random access, POSIX `pread`/`pwrite` | Better performance for specific patterns |
| Production Examples | SQLite file format, Redis AOF, Nginx logs | All use buffered stdio or raw syscalls |
| Common Mistakes | Not calling `fclose`, ignoring `fread` return, text vs binary mode | Always close, check returns, use `"rb"/"wb"` |

## What It Is

File I/O in C operates through `FILE *` streams — abstracted handles that represent open files. The standard library provides:

| Function Family | Purpose | Example |
|----------------|---------|---------|
| `fopen`/`fclose` | Open/close files | `FILE *fp = fopen("data.txt", "r")` |
| `fgets`/`fputs` | Read/write strings | `fgets(buf, n, fp)` |
| `fgetc`/`fputc` | Read/write characters | `fgetc(fp)` |
| `fprintf`/`fscanf` | Formatted I/O | `fprintf(fp, "%d\n", val)` |
| `fread`/`fwrite` | Binary I/O | `fread(buf, size, count, fp)` |
| `fseek`/`ftell` | File positioning | `fseek(fp, 0, SEEK_END)` |

## Why It Exists

C's file I/O is built on Unix philosophy: everything is a file. Regular files, devices, sockets, pipes — they all use the same `FILE *` abstraction. This design:

- Enables code reuse across file types
- Makes buffering automatic (the library manages read/write buffers)
- Provides portability across operating systems
- Supports both text and binary data

### Architecture: Buffered I/O

```
Application Code
    ↓ fprintf(fp, "data")
User-Space Buffer (stdio buffer)
    ↓ Buffer full or fflush()
Kernel Space Buffer (OS page cache)
    ↓ write() system call
Disk
```

Buffering reduces system calls: instead of one `write()` per byte, the library batches writes into larger chunks. This is why `fclose()` is critical — it flushes the buffer.

## Expanded Code Examples

### Complete File Operations

```c
#include <stdio.h>
#include <string.h>
#include <errno.h>

// Write a configuration file
int write_config(const char *filename) {
    FILE *fp = fopen(filename, "w");
    if (fp == NULL) {
        fprintf(stderr, "Failed to open %s: %s\n", filename, strerror(errno));
        return -1;
    }

    fprintf(fp, "# Configuration File\n");
    fprintf(fp, "server_port=8080\n");
    fprintf(fp, "max_connections=1024\n");
    fprintf(fp, "timeout=30\n");

    if (fclose(fp) != 0) {
        fprintf(stderr, "Failed to close %s: %s\n", filename, strerror(errno));
        return -1;
    }
    return 0;
}

// Read and parse a configuration file
typedef struct {
    int port;
    int max_conn;
    int timeout;
} Config;

int read_config(const char *filename, Config *cfg) {
    FILE *fp = fopen(filename, "r");
    if (fp == NULL) {
        fprintf(stderr, "Failed to open %s: %s\n", filename, strerror(errno));
        return -1;
    }

    char line[256];
    while (fgets(line, sizeof(line), fp) != NULL) {
        // Skip comments and empty lines
        if (line[0] == '#' || line[0] == '\n') continue;

        char key[64], value[64];
        if (sscanf(line, "%63[^=]=%63s", key, value) == 2) {
            if (strcmp(key, "server_port") == 0) cfg->port = atoi(value);
            else if (strcmp(key, "max_connections") == 0) cfg->max_conn = atoi(value);
            else if (strcmp(key, "timeout") == 0) cfg->timeout = atoi(value);
        }
    }

    fclose(fp);
    return 0;
}
```

### Binary File I/O — Reading Structured Data

```c
#include <stdio.h>
#include <string.h>

typedef struct {
    int id;
    char name[64];
    double score;
} Record;

// Write binary records
int write_records(const char *filename, const Record *records, int count) {
    FILE *fp = fopen(filename, "wb");
    if (!fp) return -1;

    // Write header
    fwrite("RECS", 1, 4, fp);           // Magic number
    fwrite(&count, sizeof(int), 1, fp);  // Record count

    // Write records
    for (int i = 0; i < count; i++) {
        fwrite(&records[i], sizeof(Record), 1, fp);
    }

    fclose(fp);
    return 0;
}

// Read binary records
int read_records(const char *filename, Record *records, int max_count) {
    FILE *fp = fopen(filename, "rb");
    if (!fp) return -1;

    // Verify magic number
    char magic[5] = {0};
    fread(magic, 1, 4, fp);
    if (strcmp(magic, "RECS") != 0) {
        fclose(fp);
        return -2;  // Invalid format
    }

    // Read count
    int count;
    fread(&count, sizeof(int), 1, fp);

    // Bounds check
    if (count > max_count) count = max_count;

    // Read records
    int read = (int)fread(records, sizeof(Record), count, fp);
    fclose(fp);
    return read;
}
```

### File Positioning — Random Access

```c
#include <stdio.h>

typedef struct {
    int id;
    long offset;   // File offset to record data
    int length;    // Length of record data
} IndexEntry;

// Build an index for fast lookup
int build_index(const char *datafile, const char *indexfile) {
    FILE *data = fopen(datafile, "rb");
    if (!data) return -1;

    FILE *idx = fopen(indexfile, "wb");
    if (!idx) { fclose(data); return -1; }

    int id = 0;
    while (1) {
        long offset = ftell(data);
        int length;
        if (fread(&length, sizeof(int), 1, data) != 1) break;

        // Skip record data
        fseek(data, length, SEEK_CUR);

        // Write index entry
        IndexEntry entry = {id, offset, length + sizeof(int)};
        fwrite(&entry, sizeof(IndexEntry), 1, idx);
        id++;
    }

    fclose(data);
    fclose(idx);
    return id;  // Number of records indexed
}

// Lookup record by ID using index
int lookup_record(const char *datafile, const char *indexfile,
                  int id, char *buffer, int bufsize) {
    FILE *idx = fopen(indexfile, "rb");
    if (!idx) return -1;

    fseek(idx, id * sizeof(IndexEntry), SEEK_SET);
    IndexEntry entry;
    if (fread(&entry, sizeof(IndexEntry), 1, idx) != 1) {
        fclose(idx);
        return -1;
    }
    fclose(idx);

    FILE *data = fopen(datafile, "rb");
    if (!data) return -1;

    fseek(data, entry.offset, SEEK_SET);
    int n = (int)fread(buffer, 1, bufsize - 1, data);
    buffer[n] = '\0';

    fclose(data);
    return n;
}
```

### Error Handling and Reliability

```c
#include <stdio.h>
#include <errno.h>
#include <string.h>

// Reliable file copy with error handling
int copy_file(const char *src, const char *dst) {
    FILE *in = fopen(src, "rb");
    if (!in) {
        fprintf(stderr, "Cannot open source '%s': %s\n", src, strerror(errno));
        return -1;
    }

    FILE *out = fopen(dst, "wb");
    if (!out) {
        fprintf(stderr, "Cannot create destination '%s': %s\n", dst, strerror(errno));
        fclose(in);
        return -1;
    }

    char buffer[8192];
    size_t bytes_read;
    while ((bytes_read = fread(buffer, 1, sizeof(buffer), in)) > 0) {
        size_t written = fwrite(buffer, 1, bytes_read, out);
        if (written != bytes_read) {
            fprintf(stderr, "Write error: %s\n", strerror(errno));
            fclose(in);
            fclose(out);
            return -1;
        }
    }

    if (ferror(in)) {
        fprintf(stderr, "Read error: %s\n", strerror(errno));
        fclose(in);
        fclose(out);
        return -1;
    }

    fclose(in);
    if (fclose(out) != 0) {
        fprintf(stderr, "Close error: %s\n", strerror(errno));
        return -1;
    }
    return 0;
}
```

## Production Incidents

### Incident 1: Missing fclose Causing Data Loss

**Problem**: A logging daemon writes entries but some are missing after a crash.

**Cause**: Data is in the stdio buffer but not flushed to disk:

```c
void log_event(const char *event) {
    FILE *fp = fopen("/var/log/app.log", "a");
    fprintf(fp, "[%s] %s\n", get_timestamp(), event);
    // No fclose — buffer may not be flushed before crash
}
```

**Solution**: Always close files (or explicitly flush):

```c
void log_event(const char *event) {
    FILE *fp = fopen("/var/log/app.log", "a");
    if (!fp) return;
    fprintf(fp, "[%s] %s\n", get_timestamp(), event);
    fclose(fp);  // Flushes buffer and releases resources
}

// Or for long-lived file handles:
void log_event(const char *event) {
    static FILE *log_fp = NULL;
    if (!log_fp) log_fp = fopen("/var/log/app.log", "a");
    if (!log_fp) return;
    fprintf(log_fp, "[%s] %s\n", get_timestamp(), event);
    fflush(log_fp);  // Flush without closing
}
```

### Incident 2: Text vs Binary Mode on Windows

**Problem**: A file transfer program corrupts binary files on Windows.

**Cause**: Text mode on Windows translates `\n` to `\r\n` on write and `\r\n` to `\n` on read, corrupting binary data:

```c
// On Windows, text mode corrupts binary data
FILE *fp = fopen("data.bin", "r");  // Text mode!
fread(buffer, 1, size, fp);         // \r\n → \n transformation
```

**Solution**: Use binary mode for all non-text files:

```c
FILE *fp = fopen("data.bin", "rb");  // Binary mode — no transformation
```

## Production Checklist

- [ ] Always check if file opened successfully (`fp == NULL`)
- [ ] Always close files when done (or explicitly `fflush`)
- [ ] Use binary mode (`"rb"`, `"wb"`) for non-text files
- [ ] Check return values of `fread`, `fwrite`, `fprintf`
- [ ] Handle partial reads/writes in loops
- [ ] Use `strerror(errno)` for meaningful error messages
- [ ] Set appropriate buffer sizes for large file operations
- [ ] Handle file locking for concurrent access
- [ ] Clean up temporary files on error paths
- [ ] Validate file format before reading structured data

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Reads/writes text files | Uses `fopen`, `fprintf`, `fgets` |
| **Intermediate** | Handles binary files and errors | Uses `fread`/`fwrite`, checks errors |
| **Advanced** | Uses memory-mapped files and buffering | Implements custom buffering, uses `mmap` |
| **Expert** | Designs file formats, handles concurrency | Implements journaling, file locking, compression |

## Common Myths Debunked

1. **Myth**: `fclose` is not important
   **Truth**: `fclose` flushes the stdio buffer to disk and releases the file handle. Without it, data may be lost on crash and file descriptors may leak.

2. **Myth**: Text and binary modes are the same
   **Truth**: On Windows, text mode translates line endings (`\n` ↔ `\r\n`), corrupting binary data. On Unix, they are identical — but always use binary mode for non-text files for portability.

3. **Myth**: `fread` always reads the requested amount
   **Truth**: `fread` may read fewer bytes than requested (end of file, interrupted by signal). Always check the return value.

4. **Myth**: You can't do random access with C file I/O
   **Truth**: `fseek` and `ftell` enable random access. For high-performance random access, use `mmap` or `pread`.

## One-Minute Revision

| Function | Purpose | Key Detail |
|----------|---------|------------|
| `fopen` | Open file | Returns `NULL` on failure |
| `fclose` | Close file | Flushes buffer, releases handle |
| `fgets` | Read line | Includes `\n` if buffer is large enough |
| `fputs` | Write string | Does not add newline |
| `fprintf` | Write formatted | Like `printf` but to file |
| `fread` | Read binary | May read fewer bytes than requested |
| `fwrite` | Write binary | Returns number of elements written |
| `fseek` | Move position | `SEEK_SET`, `SEEK_CUR`, `SEEK_END` |
| `ftell` | Get position | Returns current byte offset |
| `fflush` | Flush buffer | Forces write to disk |

## Related Topics

- [Best Practices](../15-best-practices/README.md) — Coding standards for file handling
- [Security](../11-security/README.md) — Preventing path traversal and file injection attacks
- [Memory Management](../08-memory-management/README.md) — Memory-mapped files and custom allocators

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Data loss after crash (unflushed buffers) | `strace` / DTrace | Trace `write()` syscalls to verify data reaches the kernel; check for missing `fclose`/`fflush` |
| Binary file corruption on Windows | Hex diff comparison | Compare file contents in hex editor; verify `"rb"/"wb"` modes are used for binary data |
| `fread` returning fewer bytes than expected | Check `feof()` and `ferror()` | After `fread`, check `feof(fp)` for end-of-file and `ferror(fp)` for errors |
| File descriptor leak in long-running process | `/proc/self/fd` or `lsof` | Count open file descriptors; use `lsof -p PID` to identify leaked descriptors |
| Incorrect file positioning with `fseek` | `ftell` debugging | Print `ftell(fp)` before and after `fseek` to verify correct position |

## Code Review Checklist

- [ ] `fopen` return value checked for `NULL` before use
- [ ] `fclose` called in all code paths (including error paths)
- [ ] Binary mode (`"rb"`, `"wb"`) used for non-text files
- [ ] `fread`/`fwrite` return values checked against expected counts
- [ ] `strerror(errno)` used for meaningful error messages after file operations
- [ ] Temporary files cleaned up on error paths
- [ ] File format validated before reading structured data

## Architecture Considerations

File I/O in C is built on the `FILE *` stream abstraction, which provides automatic buffering and portability across operating systems. For high-performance workloads, consider memory-mapped files (`mmap`) or direct I/O to bypass buffering overhead. The choice between buffered I/O and raw I/O depends on access patterns: buffered I/O excels for sequential writes, while `mmap` is superior for random access on large files.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Buffered stdio (`FILE *`) | General-purpose file operations | Portable, automatic buffering, but overhead for small writes |
| Memory-mapped I/O (`mmap`) | Random access on large files | Zero-copy, OS-managed paging, but not portable to Windows without `MapViewOfFile` |
| Direct I/O (`O_DIRECT`) | Database engines, bypassing page cache | Avoids double-buffering but requires aligned buffers |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Path traversal in file operations | Unauthorized file access | Validate and sanitize file paths; reject `..` components |
| Symlink race conditions (TOCTOU) | File replacement between check and open | Use `O_NOFOLLOW` flag; open files with `O_CLOEXEC` |
| Unchecked `fread`/`fwrite` return values | Silent data corruption | Always verify return counts match expected values |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `fseeko`/`ftello` (large file support), `tmpfile` improvements | Use `fseeko`/`ftello` for files > 2GB on 32-bit systems |
| C99 → C11 | Added `fgetws`/`fputws` (wide character I/O) | Use wide character functions for Unicode file content |
| C11 → C23 | Improved `fopen` mode handling, `_FILE_OFFSET_BITS` | Use `_FILE_OFFSET_BITS=64` for large file support on 32-bit platforms |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| `fopen`/`fclose`/`fread`/`fwrite` | C89 | Standard — core file I/O |
| `fseeko`/`ftello` (large file offset) | C99 (POSIX) | Widely available; use `_FILE_OFFSET_BITS=64` for portability |
| `fgetws`/`fputws` (wide character) | C99 | Standard — use for Unicode file content |
| `tmpfile` (temporary file) | C89 | Standard — file deleted on close |

## Interview Questions

1. **Why must you always check the return value of `fclose`?**: `fclose` flushes the stdio buffer to disk. On failure (disk full, I/O error), data may be lost. Always check `fclose` return value in production code, especially for logging or data persistence.
2. **What is the difference between text mode and binary mode on Windows?**: Text mode translates `\n` to `\r\n` on write and `\r\n` to `\n` on read. This corrupts binary data. On Unix, text and binary modes are identical. Always use `"rb"/"wb"` for non-text files.
3. **How do you implement safe file reading with `fread`?**: `fread` may return fewer bytes than requested (end of file, interrupted by signal). Always check the return value against the expected count, and use `feof(fp)` and `ferror(fp)` to determine the cause of short reads.
4. **When would you use `mmap` instead of `fread`/`fwrite`?**: `mmap` provides zero-copy random access to file contents by mapping the file directly into process memory. It is superior for large files with random access patterns (databases, memory-mapped data structures) but less portable than stdio.
5. **How do you handle file locking for concurrent access?**: Use `flock()` (BSD) or `fcntl()` (POSIX) for advisory locking. For mandatory locking, use `lockf()`. On Windows, use `LockFileEx()`. Always release locks in error paths to prevent deadlocks.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Secure Coding in C and CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)
- [Advanced Programming in the UNIX Environment (Stevens)](https://www.apuebook.com/)

## Overview

The File I/O module covers reading and writing files using C's `stdio.h` library. File operations work identically across Unix, Windows, and embedded systems, making C ideal for configuration parsers, log processors, database engines, and data migration tools.

## Learning Objectives

- Open and close files with `fopen` and `fclose`
- Read and write text files with `fgets`/`fputs` and `fprintf`/`fscanf`
- Perform binary I/O with `fread`/`fwrite`
- Use file positioning with `fseek`/`ftell`
- Handle file errors properly

## Prerequisites

- Completion of Module 03 (Preprocessor)
- Understanding of pointers and arrays
- Basic error handling concepts

## History

- **1972** — `stdio.h` included in original C
- **1978** — K&R C documented file I/O functions
- **1989** — ANSI C standardized `stdio.h` functions
- **1999** — C99 added `fgetpos`/`fsetpos` for large files
- **2011** — C11 added `fopen_s`, `fread_s` (Annex K)
- **2023** — C23 added improved error handling

## Production Notes

- **Where is it used?** Configuration files, logs, databases, saved state, data processing
- **Why is it useful?** Cross-platform, buffered I/O, standard library support
- **When should it be avoided?** High-frequency small writes (buffering overhead)
- **Alternative?** Memory-mapped files (`mmap`), POSIX `pread`/`pwrite`

## Core Concepts

### File I/O Functions

| Function Family | Purpose | Example |
|----------------|---------|---------|
| `fopen`/`fclose` | Open/close files | `FILE *fp = fopen("data.txt", "r")` |
| `fgets`/`fputs` | Read/write strings | `fgets(buf, n, fp)` |
| `fgetc`/`fputc` | Read/write characters | `fgetc(fp)` |
| `fprintf`/`fscanf` | Formatted I/O | `fprintf(fp, "%d\n", val)` |
| `fread`/`fwrite` | Binary I/O | `fread(buf, size, count, fp)` |
| `fseek`/`ftell` | File positioning | `fseek(fp, 0, SEEK_END)` |

### File Modes

| Mode | Description |
|------|-------------|
| `"r"` | Read (file must exist) |
| `"w"` | Write (creates/truncates) |
| `"a"` | Append (creates if needed) |
| `"r+"` | Read/write (file must exist) |
| `"w+"` | Read/write (creates/truncates) |
| `"a+"` | Read/append (creates if needed) |
| `"rb"` | Read binary |
| `"wb"` | Write binary |

## Internal Working

### Buffered I/O Architecture

```
User Code
    ↓
stdio Buffer (user-space)
    ↓
Kernel Buffer (system calls)
    ↓
File System (disk)
```

### Buffering Modes

| Mode | Description | Use Case |
|------|-------------|----------|
| Full buffering | 4KB-8KB buffer | Large file operations |
| Line buffering | Buffer flushed on newline | Terminal I/O |
| No buffering | Immediate I/O | stderr |

## Syntax

```c
#include <stdio.h>

// File opening
FILE *fp = fopen("data.txt", "r");
if (!fp) {
    perror("fopen failed");
    return 1;
}

// Reading text
char buffer[256];
while (fgets(buffer, sizeof(buffer), fp)) {
    printf("%s", buffer);
}

// Writing text
fprintf(fp, "Number: %d\n", 42);

// Binary I/O
int data[100];
size_t n = fread(data, sizeof(int), 100, fp);

// File positioning
fseek(fp, 0, SEEK_END);
long size = ftell(fp);
rewind(fp);

// Closing
fclose(fp);
```

## Examples

### Easy Example: Read File

```c
#include <stdio.h>

int main(void) {
    FILE *fp = fopen("data.txt", "r");
    if (!fp) return 1;
    
    char line[256];
    while (fgets(line, sizeof(line), fp)) {
        printf("%s", line);
    }
    
    fclose(fp);
    return 0;
}
```

### Medium Example: Write CSV

```c
#include <stdio.h>

int main(void) {
    FILE *fp = fopen("data.csv", "w");
    if (!fp) return 1;
    
    fprintf(fp, "Name,Age,City\n");
    fprintf(fp, "Alice,30,NYC\n");
    fprintf(fp, "Bob,25,LA\n");
    
    fclose(fp);
    return 0;
}
```

### Hard Example: Binary Record I/O

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int id;
    char name[50];
    double balance;
} Account;

int write_accounts(const char *filename, Account *accs, size_t count) {
    FILE *fp = fopen(filename, "wb");
    if (!fp) return -1;
    fwrite(accs, sizeof(Account), count, fp);
    fclose(fp);
    return 0;
}

Account *read_accounts(const char *filename, size_t *count) {
    FILE *fp = fopen(filename, "rb");
    if (!fp) return NULL;
    fseek(fp, 0, SEEK_END);
    *count = ftell(fp) / sizeof(Account);
    rewind(fp);
    Account *accs = malloc(*count * sizeof(Account));
    fread(accs, sizeof(Account), *count, fp);
    fclose(fp);
    return accs;
}
```

### Enterprise Example: Log Processor

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

typedef struct {
    time_t timestamp;
    char level[10];
    char message[256];
} LogEntry;

int process_log(const char *input, const char *output) {
    FILE *in = fopen(input, "r");
    FILE *out = fopen(output, "w");
    if (!in || !out) return -1;
    
    char line[512];
    while (fgets(line, sizeof(line), in)) {
        if (strstr(line, "ERROR")) {
            fputs(line, out);
        }
    }
    
    fclose(in);
    fclose(out);
    return 0;
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Buffering | stdio uses full buffering | Use `setvbuf` for custom buffering |
| Binary vs text | Text mode has newline translation | Use binary mode for performance |
| File positioning | `fseek`/`ftell` may flush buffer | Minimize positioning operations |
| Large files | 32-bit `ftell` limits | Use `fgetpos`/`fsetpos` for large files |

## Best Practices

- Do:
  - Always check return values from `fopen`, `fread`, etc.
  - Close files with `fclose` when done
  - Use binary mode for structured data
  - Handle errors with `perror` or `strerror`
  - Use `fgets` instead of `gets` for safe input
  
- Don't:
  - Ignore return values from file operations
  - Forget to close files (resource leak)
  - Use text mode for binary data
  - Assume file position after `fread`/`fwrite`
  - Use `scanf` for unbounded input

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Missing `fclose` | Resource leak, data loss | Always close files |
| Ignoring `fread` return | Incomplete data processing | Check return value |
| Text vs binary mode | Data corruption on Windows | Use `"rb"`/`"wb"` for binary |
| Unbounded `scanf` | Buffer overflow | Use `%ns` with width limit |
| Not checking `fopen` | NULL dereference | Always check for NULL |

## Interview Questions

### Q1: What is the difference between `fread` and `fgets`?
**Answer:** `fread` reads binary data (fixed-size blocks). `fgets` reads text (line-by-line, null-terminated).

### Q2: What is the difference between `feof` and `ferror`?
**Answer:** `feof` returns true when end-of-file is reached. `ferror` returns true when an error occurred.

### Q3: What is the difference between `fseek` and `fgetpos`?
**Answer:** `fseek` uses a long offset (may be 32-bit). `fgetpos` uses `fpos_t` (supports large files).

### Q4: What is the difference between text and binary mode?
**Answer:** Text mode translates newlines (Unix: `\n`, Windows: `\r\n`). Binary mode has no translation.

### Q5: What is `perror` used for?
**Answer:** Prints a human-readable error message for the current `errno` value.

### Q6: What is the difference between `fprintf` and `printf`?
**Answer:** `fprintf` writes to a file stream. `printf` writes to stdout.

### Q7: What is the difference between `fopen` and `open`?
**Answer:** `fopen` is C standard library (buffered). `open` is POSIX system call (unbuffered).

### Q8: What is the difference between `fread` return value and `feof`?
**Answer:** `fread` returns number of items read (may be less than requested). `feof` only true after failed read.

### Q9: What is `setvbuf` used for?
**Answer:** Sets buffering mode and buffer size for a file stream.

### Q10: What is the difference between `rewind` and `fseek(fp, 0, SEEK_SET)`?
**Answer:** `rewind` also clears the error indicator. `fseek` does not.

### Q11: What is `tmpfile` used for?
**Answer:** Creates a temporary file that is automatically deleted when closed.

### Q12: What is the difference between `snprintf` and `fprintf`?
**Answer:** `snprintf` writes to a string buffer. `fprintf` writes to a file stream.

### Q13: What is `ungetc` used for?
**Answer:** Pushes a character back onto the input stream for re-reading.

### Q14: What is the difference between `fopen` modes `"r+"` and `"w+"`?
**Answer:** `"r+"` requires file to exist. `"w+"` creates or truncates.

### Q15: What is `ferror` used for?
**Answer:** Checks if an error occurred on a file stream.

## Cross-References

- **Previous Module:** [03 - Preprocessor](../03-preprocessor/)
- **Next Module:** [05 - Pointers Advanced](../05-pointers-advanced/)
- **Related:** [08 - Memory Management](../08-memory-management/) — Buffer management
- **Related:** [10 - Networking](../10-networking/) — Socket I/O
- **External:** [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- **External:** [Advanced Programming in the UNIX Environment](https://www.apuebook.com/)

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

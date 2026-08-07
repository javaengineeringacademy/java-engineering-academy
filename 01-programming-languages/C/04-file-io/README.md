# File I/O — C Language

## What it is
File I/O provides functions to read from and write to files on disk.

## Why it exists
To persist data beyond program execution and process external data.

## When to use it
Whenever you need to read configuration, log data, or process files.

## How it works

### Opening Files

```c
FILE *fp = fopen("data.txt", "r");  // Read
FILE *fp = fopen("out.txt", "w");   // Write
FILE *fp = fopen("log.txt", "a");   // Append
FILE *fp = fopen("data.bin", "rb"); // Binary read
```

### Reading Files

```c
char buffer[100];
fgets(buffer, 100, fp);        // Read line
fgetc(fp);                     // Read character
fscanf(fp, "%d", &value);     // Read formatted
fread(buffer, size, count, fp); // Read binary
```

### Writing Files

```c
fprintf(fp, "Value: %d\n", value);
fputc('A', fp);
fputs("Hello\n", fp);
fwrite(buffer, size, count, fp);
```

### Error Handling

```c
if (fp == NULL) {
    perror("Error opening file");
    return 1;
}

if (fclose(fp) != 0) {
    perror("Error closing file");
}
```

### File Positioning

```c
fseek(fp, 0, SEEK_SET);  // Beginning
fseek(fp, 0, SEEK_END);  // End
ftell(fp);               // Current position
rewind(fp);              // Go to beginning
```

## Production Checklist

- [ ] Always check if file opened successfully
- [ ] Always close files when done
- [ ] Use fclose to flush buffers
- [ ] Handle binary files correctly
- [ ] Check return values of I/O functions

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Reads/writes text files |
| Intermediate | Handles binary files and errors |
| Advanced | Uses memory-mapped files and buffering |

## Common Myths

1. **Myth**: fclose is not important
   **Truth**: fclose flushes buffers and releases resources

2. **Myth**: Text and binary modes are the same
   **Truth**: Binary mode preserves exact byte content

## One-Minute Revision

| Function | Purpose |
|----------|---------|
| fopen | Open file |
| fclose | Close file |
| fgets | Read line |
| fputs | Write string |
| fprintf | Write formatted |
| fread | Read binary |
| fwrite | Write binary |
| fseek | Move position |
| ftell | Get position |

## Related Topics

- [Best Practices](../15-best-practices/README.md)
- [Security](../11-security/README.md)

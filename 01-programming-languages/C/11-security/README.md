# Security — C Language

## What it is
Security in C involves protecting programs from vulnerabilities and attacks.

## Why it exists
C's power comes with risks; security practices prevent exploitation.

## When to use it
Always. Security must be considered in every C program.

## How it works

### Buffer Overflow Prevention

```c
// Bad
char buffer[10];
gets(buffer);  // No bounds checking

// Good
char buffer[10];
fgets(buffer, sizeof(buffer), stdin);
```

### Integer Overflow

```c
// Bad
int size = n * sizeof(int);  // May overflow

// Good
if (n > SIZE_MAX / sizeof(int)) return ERROR;
int size = n * sizeof(int);
```

### Format String Vulnerabilities

```c
// Bad
printf(user_input);  // Format string attack

// Good
printf("%s", user_input);
```

### Null Pointer Checks

```c
void process(const char *data) {
    if (data == NULL) return;
    // process data
}
```

### Secure Random Numbers

```c
#include <stdlib.h>

// For cryptographic use
// Use platform-specific APIs (e.g., /dev/urandom on Linux)
```

### Bounds Checking

```c
void access_array(int *arr, int size, int index) {
    if (index < 0 || index >= size) return;
    int value = arr[index];
}
```

## Production Incidents

### Incident 1: Buffer Overflow Exploit

**Problem:** A network service is exploited to gain root access on the server.

**Cause:** A fixed-size stack buffer receives untrusted network data without bounds checking:

```c
void handle_packet(const char *packet) {
    char name[64];
    strcpy(name, packet);  // No bounds check
    process_name(name);
}
```

**Impact:** Attacker sends 256-byte packet, overwrites return address on stack, gains shell as root. Full server compromise, data breach, regulatory fines.

**Detection:** Network IDS alerts on unusual payload size. Server shows unexpected shell processes. Post-mortem reveals stack buffer overflow.

**Solution:** Use bounded copy and stack canary:

```c
void handle_packet(const char *packet) {
    char name[64];
    strncpy(name, packet, sizeof(name) - 1);
    name[sizeof(name) - 1] = '\0';
    process_name(name);
}
```

**Prevention:** Compile with `-fstack-protector-strong`, `-D_FORTIFY_SOURCE=2`, enable ASLR, use `gets()` removal (`-Werror=implicit-function-declaration`), deploy WAF, conduct regular penetration testing.

---

### Incident 2: Format String Vulnerability

**Problem:** A logging system allows attackers to read arbitrary memory and potentially execute code.

**Cause:** User-controlled input is passed directly as the format string to `printf`:

```c
void log_message(const char *user_msg) {
    printf(user_msg);  // Format string attack
    syslog(LOG_INFO, user_msg);
}
```

**Impact:** Attacker sends `%x%x%x%x%n` to read stack contents (info leak) or `%n` to write to arbitrary addresses (code execution). Full system compromise.

**Detection:** Code review identifies `printf(user_msg)` pattern. Exploitation leaves unusual stack traces in logs.

**Solution:** Use format specifier:

```c
void log_message(const char *user_msg) {
    printf("%s", user_msg);
    syslog(LOG_INFO, "%s", user_msg);
}
```

**Prevention:** Compile with `-Wformat-security`, `-Werror=format`, audit all `printf`/`syslog` calls, use linters to detect format string issues.

## Production Checklist

- [ ] Always validate input
- [ ] Use bounds-checked functions
- [ ] Check for integer overflow
- [ ] Don't use gets()
- [ ] Compile with security flags (-fstack-protector)

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Knows to check null pointers |
| Intermediate | Prevents buffer overflows |
| Advanced | Implements defense in depth |

## Common Myths

1. **Myth**: C is inherently insecure
   **Truth**: C is secure when written carefully with proper checks

2. **Myth**: Compiler flags fix all issues
   **Truth**: Security requires both code discipline and compiler protection

## One-Minute Revision

| Threat | Prevention |
|--------|------------|
| Buffer overflow | Bounds checking |
| Integer overflow | Range validation |
| Format string | Use %s format |
| Null dereference | Check before use |
| Use-after-free | Set pointer to NULL |
| Double free | Track allocation state |

## Related Topics

- [Best Practices](../15-best-practices/README.md)
- [Testing](../13-testing/README.md)

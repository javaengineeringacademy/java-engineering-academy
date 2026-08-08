# Security — C Language

## The Problem

C gives you direct access to memory and hardware — and direct responsibility for security. Buffer overflows, format string vulnerabilities, integer overflows, and use-after-free bugs are not theoretical — they are the most exploited vulnerability class in production systems. The Morris worm (1988), Heartbleed (2014), and countless zero-day exploits all trace back to memory safety issues in C code.

Security is not a feature you add later — it must be baked into every line of C code you write.

## What It Is

C security involves preventing:

| Vulnerability | Description | Impact |
|--------------|-------------|--------|
| Buffer overflow | Writing past array bounds | Remote code execution |
| Format string | User input as format string | Memory read/write |
| Integer overflow | Arithmetic wraps around | Buffer overflow, logic errors |
| Use-after-free | Using freed memory | Remote code execution |
| Double free | Freeing same memory twice | Heap corruption |
| Null dereference | Dereferencing NULL pointer | Crash, DoS |
| Race condition | Unsynchronized shared access | Data corruption |

## Why It Exists

C provides no runtime memory safety. The language trusts the programmer to:
- Check array bounds before access
- Validate pointer before dereference
- Check arithmetic for overflow
- Free memory correctly
- Synchronize concurrent access

This trust is both C's strength (performance, control) and its weakness (vulnerability to exploitation).

### Architecture: Defense in Depth

```
Layer 1: Secure Coding Practices
├── Input validation
├── Bounds checking
├── Proper error handling
└── Least privilege

Layer 2: Compiler Protections
├── Stack canaries (-fstack-protector-strong)
├── ASLR (-pie -fPIE)
├── DEP/NX stack (-Wl,-z,execstack)
└── FORTIFY_SOURCE (-D_FORTIFY_SOURCE=2)

Layer 3: Runtime Detection
├── AddressSanitizer (-fsanitize=address)
├── UndefinedBehaviorSanitizer (-fsanitize=undefined)
├── MemorySanitizer (-fsanitize=memory)
└── ThreadSanitizer (-fsanitize=thread)

Layer 4: OS Protections
├── ASLR
├── SECCOMP
├── SELinux / AppArmor
└── Capability dropping
```

## Expanded Code Examples

### Buffer Overflow Prevention

```c
#include <stdio.h>
#include <string.h>

// BAD: No bounds checking
void unsafe_copy(const char *src) {
    char buffer[64];
    strcpy(buffer, src);  // Buffer overflow if src > 64 chars
}

// GOOD: Bounded copy
void safe_copy(const char *src) {
    char buffer[64];
    strncpy(buffer, src, sizeof(buffer) - 1);
    buffer[sizeof(buffer) - 1] = '\0';
}

// BETTER: Use snprintf for formatted data
void safe_format(const char *name, int age) {
    char buffer[128];
    int written = snprintf(buffer, sizeof(buffer), "Name: %s, Age: %d", name, age);
    if (written >= (int)sizeof(buffer)) {
        // Truncation occurred
        fprintf(stderr, "Warning: output truncated\n");
    }
}

// BEST: Dynamic sizing
int safe_concat(const char *a, const char *b, char **result) {
    size_t len_a = strlen(a);
    size_t len_b = strlen(b);
    if (len_a > SIZE_MAX - len_b - 1) return -1;  // Overflow check

    *result = malloc(len_a + len_b + 1);
    if (!*result) return -2;

    memcpy(*result, a, len_a);
    memcpy(*result + len_a, b, len_b + 1);
    return 0;
}
```

### Integer Overflow Prevention

```c
#include <stdint.h>
#include <stddef.h>
#include <stdlib.h>

// BAD: Integer overflow in size calculation
void *unsafe_alloc(int count) {
    return malloc(count * sizeof(int));  // Overflow if count is large
}

// GOOD: Check before multiplication
void *safe_alloc(int count) {
    if (count < 0 || count > SIZE_MAX / sizeof(int)) {
        return NULL;
    }
    return malloc((size_t)count * sizeof(int));
}

// GOOD: Overflow-safe addition
int safe_add(int a, int b) {
    if ((b > 0 && a > INT_MAX - b) || (b < 0 && a < INT_MIN - b)) {
        return 0;  // Overflow
    }
    return a + b;
}

// GOOD: Safe multiplication for size calculations
size_t safe_mul(size_t a, size_t b) {
    if (a != 0 && b > SIZE_MAX / a) {
        return 0;  // Overflow
    }
    return a * b;
}
```

### Format String Vulnerability Prevention

```c
#include <stdio.h>

// BAD: User input as format string
void unsafe_log(const char *user_input) {
    printf(user_input);           // Format string attack
    fprintf(stderr, user_input);  // Same vulnerability
    syslog(LOG_INFO, user_input); // Same vulnerability
}

// GOOD: Always use format specifier
void safe_log(const char *user_input) {
    printf("%s", user_input);
    fprintf(stderr, "%s", user_input);
    syslog(LOG_INFO, "%s", user_input);
}

// SAFE: Compile with -Wformat-security -Werror=format
```

### Input Validation

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Validate and sanitize user input
typedef struct {
    char name[64];
    int age;
    char email[128];
} UserInput;

int validate_input(const char *name, int age, const char *email, UserInput *out) {
    // Validate name: non-empty, alphanumeric + spaces, max length
    if (!name || strlen(name) == 0 || strlen(name) > 63) return -1;
    for (size_t i = 0; name[i]; i++) {
        if (!isalnum(name[i]) && name[i] != ' ') return -2;
    }
    strncpy(out->name, name, sizeof(out->name) - 1);
    out->name[sizeof(out->name) - 1] = '\0';

    // Validate age: reasonable range
    if (age < 0 || age > 150) return -3;
    out->age = age;

    // Validate email: contains @ and . after @
    if (!email) return -4;
    const char *at = strchr(email, '@');
    if (!at || at == email) return -5;
    const char *dot = strrchr(at, '.');
    if (!dot || dot == at + 1) return -6;
    if (strlen(email) > 127) return -7;
    strncpy(out->email, email, sizeof(out->email) - 1);
    out->email[sizeof(out->email) - 1] = '\0';

    return 0;
}
```

### Secure String Handling

```c
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// Safe string duplication
char *safe_strdup(const char *s) {
    if (!s) return NULL;
    size_t len = strlen(s);
    char *dup = malloc(len + 1);
    if (!dup) return NULL;
    memcpy(dup, s, len + 1);
    return dup;
}

// Safe string concatenation
int safe_strcat(char *dest, size_t dest_size, const char *src) {
    size_t dest_len = strlen(dest);
    size_t src_len = strlen(src);

    if (dest_len + src_len >= dest_size) {
        // Truncate
        size_t copy_len = dest_size - dest_len - 1;
        memcpy(dest + dest_len, src, copy_len);
        dest[dest_len + copy_len] = '\0';
        return -1;  // Truncation occurred
    }

    memcpy(dest + dest_len, src, src_len + 1);
    return 0;
}

// Safe integer to string
int safe_itoa(int value, char *buf, size_t bufsize) {
    if (bufsize == 0) return -1;
    int written = snprintf(buf, bufsize, "%d", value);
    if (written < 0 || (size_t)written >= bufsize) return -1;
    return 0;
}
```

## Production Incidents

### Incident 1: Buffer Overflow Remote Code Execution

**Problem**: Network service exploited to gain root access.

**Cause**: Unbounded `strcpy` from network input:

```c
void handle_packet(const char *packet) {
    char name[64];
    strcpy(name, packet);  // No bounds check — attacker sends 256 bytes
}
```

**Impact**: Full server compromise. CVSS 9.8.

**Solution**: Use bounded copy and enable stack protection:

```c
void handle_packet(const char *packet) {
    char name[64];
    strncpy(name, packet, sizeof(name) - 1);
    name[sizeof(name) - 1] = '\0';
}
```

**Prevention**: Compile with `-fstack-protector-strong -D_FORTIFY_SOURCE=2 -Wformat-security`.

### Incident 2: Format String Attack

**Problem**: Logging system allows reading arbitrary memory.

**Cause**: User input passed as format string:

```c
void log_message(const char *user_msg) {
    printf(user_msg);  // Attacker sends "%x%x%x%x%n"
}
```

**Impact**: Memory disclosure, potential code execution.

**Solution**: Use `%s` format specifier:

```c
void log_message(const char *user_msg) {
    printf("%s", user_msg);
}
```

## Production Checklist

- [ ] Always validate input before processing
- [ ] Use bounds-checked functions (`strncpy`, `snprintf`, `strlcpy`)
- [ ] Check for integer overflow before arithmetic
- [ ] Never use `gets()`, `sprintf()`, `strcpy()` with untrusted input
- [ ] Compile with `-Wall -Wextra -Werror -Wformat-security`
- [ ] Enable stack protection: `-fstack-protector-strong`
- [ ] Enable FORTIFY_SOURCE: `-D_FORTIFY_SOURCE=2`
- [ ] Enable ASLR: `-pie -fPIE`
- [ ] Run with AddressSanitizer in testing
- [ ] Audit all `printf`/`syslog` calls for format string issues
- [ ] Use `const` for read-only parameters
- [ ] Check return values of all security-sensitive functions

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Knows to check null pointers | Uses null checks before dereference |
| **Intermediate** | Prevents buffer overflows | Uses bounded string functions |
| **Advanced** | Implements defense in depth | Compiler flags + code discipline + runtime detection |
| **Expert** | Designs secure systems, conducts code audits | Threat modeling, fuzzing, penetration testing |

## Common Myths Debunked

1. **Myth**: C is inherently insecure
   **Truth**: C is secure when written with proper checks, compiler flags, and runtime detection. Most C vulnerabilities are preventable with disciplined coding.

2. **Myth**: Compiler flags fix all issues
   **Security**: Flags like `-fstack-protector` help, but secure coding practices are essential. Flags are a safety net, not a replacement for secure code.

3. **Myth**: Using `strncpy` is always safe
   **Truth**: `strncpy` does not guarantee null termination. Always manually add `'\0'` after `strncpy`.

4. **Myth**: Security is someone else's job
   **Truth**: Every C programmer writes security-critical code. Buffer overflows affect the entire system.

## One-Minute Revision

| Threat | Prevention | Detection |
|--------|-----------|-----------|
| Buffer overflow | Bounds checking, `snprintf` | AddressSanitizer |
| Integer overflow | Range validation before arithmetic | UBSan |
| Format string | Always use `%s` format | Code review, `-Wformat-security` |
| Null dereference | Check before use | ASan, crashes |
| Use-after-free | Reference counting, NULL after free | AddressSanitizer |
| Double free | Track allocation state | Valgrind, ASan |
| Race condition | Mutex, atomics | ThreadSanitizer |

## Related Topics

- [Memory Management](../08-memory-management/README.md) — Preventing memory-based vulnerabilities
- [Testing](../13-testing/README.md) — Security testing tools
- [Best Practices](../15-best-practices/README.md) — Coding standards for secure code
- [Networking](../10-networking/README.md) — Secure network protocols

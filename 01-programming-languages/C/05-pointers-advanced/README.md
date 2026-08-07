# Advanced Pointers — C Language

## What it is
Advanced pointer concepts including pointer-to-pointer, function pointers, arrays of pointers, and complex declarations.

## Why it exists
To enable sophisticated memory management, callback mechanisms, and data structure implementations.

## When to use it
When building data structures, implementing callbacks, or managing complex memory layouts.

## How it works

### Pointer to Pointer

```c
int x = 10;
int *p = &x;
int **pp = &p;
printf("%d\n", **pp);  // 10
```

### Arrays of Pointers

```c
int a = 1, b = 2, c = 3;
int *arr[] = {&a, &b, &c};
printf("%d\n", *arr[1]);  // 2
```

### Pointer to Arrays

```c
int arr[5] = {1, 2, 3, 4, 5};
int (*ptr)[5] = &arr;
```

### Function Pointers

```c
int add(int a, int b) { return a + b; }
int (*func)(int, int) = add;
int result = func(5, 3);
```

### Callback Functions

```c
void apply(int *arr, int n, void (*callback)(int *)) {
    for (int i = 0; i < n; i++) {
        callback(&arr[i]);
    }
}
```

### Complex Declarations (Clockwise/Spiral Rule)

```c
int *p;           // p is a pointer to int
int **p;          // p is a pointer to pointer to int
int *arr[10];     // arr is array of 10 pointers to int
int (*arr)[10];   // arr is pointer to array of 10 ints
int (*func)(int); // func is pointer to function returning int
```

### Opaque Pointers

```c
// header.h
typedef struct Handle Handle;
Handle *create(void);
void destroy(Handle *h);

// implementation.c
struct Handle {
    int data;
    // private members
};
```

## Production Incidents

### Incident 1: Pointer Arithmetic Out of Bounds

**Problem:** A image processing library produces corrupted output and occasionally crashes on certain image sizes.

**Cause:** Pointer arithmetic advances past the end of an allocated buffer when processing edge pixels:

```c
void apply_filter(int *pixels, int width, int height) {
    int *end = pixels + width * height;
    int *p = pixels;
    while (p < end) {
        *(p + 1) = (*p + *(p + 1)) / 2;  // Off-by-one at row end
        p++;
    }
}
```

**Impact:** Reads/writes past allocated memory, corrupts adjacent allocations, causes intermittent crashes and data corruption.

**Detection:** AddressSanitizer reports heap-buffer-overflow. Valgrind shows invalid reads/writes.

**Solution:** Check row boundaries explicitly:

```c
void apply_filter(int *pixels, int width, int height) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width - 1; x++) {
            int idx = y * width + x;
            pixels[idx + 1] = (pixels[idx] + pixels[idx + 1]) / 2;
        }
    }
}
```

**Prevention:** Avoid raw pointer arithmetic for array traversal, use array indexing with bounds checks, enable AddressSanitizer in testing.

---

### Incident 2: Function Pointer Table Corruption

**Problem:** A plugin system crashes when calling plugin functions after a specific plugin is unloaded.

**Cause:** The function pointer table retains pointers to functions in the unloaded plugin's shared library:

```c
typedef struct {
    const char *name;
    void (*func)(void);
} PluginFunc;

PluginFunc plugins[100];

void load_plugin(const char *path) {
    void *handle = dlopen(path, RTLD_NOW);
    plugins[count].func = dlsym(handle, "plugin_init");
    plugins[count].handle = handle;
    count++;
}

void unload_plugin(int idx) {
    dlclose(plugins[idx].handle);
    // plugins[idx].func is now dangling!
}

void call_plugin(int idx) {
    plugins[idx].func();  // Calls into unloaded memory
}
```

**Impact:** Segfault or arbitrary code execution if attacker controls unloaded library's memory space.

**Detection:** Crashes in `call_plugin` after unload. `dladdr` confirms function is in unmapped memory.

**Solution:** Invalidate function pointers on unload:

```c
void unload_plugin(int idx) {
    dlclose(plugins[idx].handle);
    plugins[idx].func = NULL;
    plugins[idx].handle = NULL;
}

void call_plugin(int idx) {
    if (plugins[idx].func == NULL) {
        log_error("Plugin %d not loaded", idx);
        return;
    }
    plugins[idx].func();
}
```

**Prevention:** Null out function pointers after unload, use reference counting for plugin lifetime, validate pointers before calling.

## Production Checklist

- [ ] Validate all pointers before dereferencing
- [ ] Use const for read-only pointers
- [ ] Document complex pointer declarations
- [ ] Free all dynamically allocated memory
- [ ] Use function pointers for callbacks

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Understands basic pointers |
| Intermediate | Uses pointer-to-pointer and arrays |
| Advanced | Masters function pointers and opaque pointers |

## Common Myths

1. **Myth**: Function pointers are complicated
   **Truth**: They follow simple syntax rules once understood

2. **Myth**: Opaque pointers hide implementation
   **Truth**: They provide information hiding and ABI stability

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| **p | Pointer to pointer |
| *arr[] | Array of pointers |
| (*arr)[] | Pointer to array |
| (*func)() | Function pointer |
| Callback | Function passed as argument |
| Opaque | Hidden implementation |

## Related Topics

- [Pointers](../01-fundamentals/07-pointers/README.md)
- [Data Structures](../06-data-structures/README.md)

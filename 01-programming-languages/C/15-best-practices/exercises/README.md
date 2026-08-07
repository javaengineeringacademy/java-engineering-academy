# Best Practices Exercises

## Exercise 1: Code Review
Review and improve the following code:

```c
// Bad
int f(int *a, int n) {
    int s = 0;
    for (int i = 0; i <= n; i++)
        s += a[i];
    return s;
}

// Good
int sum_array(const int *array, int length) {
    int sum = 0;
    for (int i = 0; i < length; i++) {
        sum += array[i];
    }
    return sum;
}
```

## Exercise 2: Error Handling
Add proper error handling to a file processing function.

## Exercise 3: Documentation
Write documentation for a utility library.

## Exercise 4: Code Organization
Refactor a monolithic file into modules.

## Exercise 5: Memory Safety
Review and fix memory management issues.

# var Type Inference Quiz

## Questions

1. Can `var` be used for method parameters in Java 10?
2. Can `var` be used in a for loop's init statement?
3. Can `var` be assigned `null`?
4. What happens if you write `var x;`?
5. Can `var` be used for fields?
6. Can `var` be used for return types?
7. Can `var` be used with lambda expressions?
8. What is the type of `var x = 10;`?
9. Can `var` be used with diamond operator?
10. Can `var` be used for multiple declarations?

## Answers

1. **No, not until Java 11.** var was added to method parameters in Java 11.
2. **Yes.** `for (var i = 0; i < 10; i++)` is valid.
3. **Yes, but only as `var x = (Type) null;`** with explicit type or a definite assignment. `var x = null` is invalid.
4. **Compilation error.** var requires an initializer.
5. **No.** var can only be used for local variables.
6. **No.** Return types must be explicit.
7. **Yes,** in Java 11+. Before that, explicit type was required.
8. **int.** The compiler infers the type from the initializer.
9. **Yes.** `var set = new HashSet<String>();` is valid.
10. **No.** Each variable must be declared separately.

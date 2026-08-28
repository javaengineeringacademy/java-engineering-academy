# Modern Java Features - Quiz

## Questions

### Records

1. Can a Record implement an interface?
2. What methods does a Record automatically generate?
3. Can you have instance variables in a Record besides the components?
4. Can a Record be abstract?
5. Can Records extend classes?

### Sealed Classes

6. What modifier is used to restrict which classes can extend a sealed class?
7. Can a sealed class have non-final subclasses?
8. What happens if a subclass of a sealed class is in a different module?
9. What is the difference between `permits` and implicitly restricting to the same file?

### Pattern Matching

10. What does `case Shape s` do in a pattern match switch?
11. Can you use pattern variables in the switch body?
12. What is a guarded pattern?
13. Can you use null in a pattern match switch?

### Text Blocks

14. How do you start a text block?
15. What does `\s` do at the end of a line in a text block?
16. How do you prevent line terminators from being included?
17. What is the `"""` syntax called?

### Switch Expressions

18. What is the difference between `case` with `->` and `case` with `:`?
19. Do switch expressions need a `break` statement?
20. What must all cases in a switch expression have?

### var Type Inference

21. Can `var` be used for method parameters in Java 10?
22. Can `var` be used in a for loop's init statement?
23. Can `var` be assigned `null`?
24. What happens if you write `var x;`?

### Multi-catch

25. What syntax separates exception types in a multi-catch?
26. Can a multi-catch have an exception variable for each type?
27. Are the exception types in a multi-catch related by inheritance?

### instanceof Pattern Matching

28. What is a pattern variable?
29. Can you use `&&` with instanceof pattern variables?
30. What is the scope of a pattern variable?

---

## Answers

1. **Yes.** Records can implement interfaces: `record Point(int x, int y) implements Comparable<Point>`.
2. **equals(), hashCode(), toString(), and accessor methods** for each component. A canonical constructor is also generated.
3. **No.** All state is defined in the record header. You can declare static fields and methods.
4. **No.** Records are implicitly final.
5. **No.** Records implicitly extend `java.lang.Record`.

6. **`sealed`** keyword on the class declaration.
7. **Yes.** Subclasses can be `final`, `sealed`, or `non-sealed`.
8. **The subclass must explicitly opt-in** using `opens` in the module, or be in the same module.
9. **Same file:** subclasses must be top-level or nested classes in the same file. Different module requires explicit `opens`.

10. **Binds the matched object to the variable `s`** of type `Shape`.
11. **Yes.** The pattern variable is in scope for the case body.
12. **A pattern with a boolean condition:** `case String s && s.length() > 5`.
13. **Yes.** You can use `null` with `case null ->`. Or use `default` to catch null.
14. **Three double quotes:** `"""` followed by an optional newline.
15. **Trims trailing whitespace** by the number of leading spaces.
16. **Add `\` at the end of the line** (line terminator escape).
17. **Text block** literal.

18. **`->` is an expression form** (no fall-through, no break needed). **`:` is a statement form** (requires break, allows fall-through).
19. **No.** `->` form does not fall through and does not require break.
20. **Every case must either return a value, throw an exception, or be a statement case** (in expression form, all must be consistent).
21. **No, not until Java 11.** var was added to method parameters in Java 11.
22. **Yes.** `for (var i = 0; i < 10; i++)` is valid.
23. **Yes, but only as `var x = (Type) null;`** with explicit type or a definite assignment. `var x = null` is invalid.
24. **Compilation error.** var requires an initializer.

25. **Pipe `|`:** `catch (IOException | SQLException e)`.
26. **No.** Only one variable name for the multi-catch.
27. **No.** They should be unrelated types; related types use inheritance.

28. **A variable introduced by a pattern** that can be used in the case body.
29. **Yes,** using `instanceof` pattern: `case String s && s.isEmpty()`.
30. **Scoped to the case block** where the pattern is matched.

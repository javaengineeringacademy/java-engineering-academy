# Records Quiz

## Questions

1. What does `record Point(int x, int y) {}` generate?
2. Can a Record have a static field?
3. Can Records implement interfaces?
4. What is a compact constructor?
5. Can you override the accessor method?
6. What happens if two Records have the same component values?
7. Can a Record have a no-arg constructor?
8. How do you access component values?
9. Can Records have instance methods?
10. What is the parent class of all Records?

## Answers

1. **Generates:** `x()` and `y()` accessors, `equals()`, `hashCode()`, `toString()`, and a canonical constructor `Point(int x, int y)`.
2. **Yes.** Records can have static fields, static methods, and static blocks.
3. **Yes.** Records can implement any number of interfaces.
4. **A constructor without assignments** that validates or transforms data: `public Point { if (x < 0) throw ... }`.
5. **Yes,** but it's not recommended as it can break the contract.
6. **They are equal** according to equals() if all components are equal.
7. **Yes,** but only using the canonical constructor: `record Point() {}`.
8. **Using accessor methods:** `point.x()` not `point.getX()`.
9. **Yes.** Records can have any instance methods.
10. **`java.lang.Record`** (implicitly).

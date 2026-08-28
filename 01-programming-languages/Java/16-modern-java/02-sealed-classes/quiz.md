# Sealed Classes Quiz

## Questions

1. What keyword is used to restrict which classes can extend a sealed class?
2. Can a sealed class have non-final subclasses?
3. What is the difference between `sealed` and `non-sealed`?
4. Can a sealed class have no permitted subclasses?
5. What happens if a subclass of a sealed class is in a different module?
6. Can a sealed class extend another sealed class?
7. What is the purpose of the `permits` clause?
8. Can a sealed class be abstract?
9. Can a sealed class have static methods?
10. How does sealed class help with pattern matching?

## Answers

1. **`sealed`** keyword on the class declaration.
2. **Yes.** Subclasses can be `final`, `sealed`, or `non-sealed`.
3. **`sealed`** restricts further extension to permitted subclasses. **`non-sealed`** opens extension to any class.
4. **No.** A sealed class must have at least one permitted subclass.
5. **The subclass must explicitly opt-in** using `opens` in the module, or be in the same module.
6. **Yes,** as long as the parent permits it.
7. **Lists the classes/interfaces** that are allowed to extend/implement the sealed class.
8. **Yes.** Abstract classes can be sealed.
9. **Yes.** Sealed classes can have any static members.
10. **Exhaustive checking** - compiler knows all possible subtypes for pattern matching.

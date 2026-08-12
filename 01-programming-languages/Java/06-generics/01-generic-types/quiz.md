# Quiz: Generic Types

## Questions

### Q1: What is a generic type in Java?
**Answer:** A generic type is a class or interface that is parameterized over types. It allows you to write a single class that works with any type while catching type mismatches at compile time.

### Q2: What problem do generics solve?
**Answer:** Generics eliminate the need for explicit casting and provide compile-time type safety. Without generics, collections store Objects and require manual casting at retrieval, which can cause ClassCastException at runtime.

### Q3: What is the syntax for declaring a generic class?
**Answer:** `class ClassName<T> { ... }` where `T` is a type parameter. For example: `class Box<T> { private T value; }`

### Q4: Can a generic class have multiple type parameters?
**Answer:** Yes. For example: `class Pair<K, V> { ... }` or `class Triple<T, U, V> { ... }`. Multiple type parameters are separated by commas.

### Q5: What is a type parameter?
**Answer:** A type parameter is a placeholder for a type that is specified when the generic class is instantiated. For example, in `Box<String>`, `String` is the type argument for the type parameter `T`.

### Q6: What is the difference between a type parameter and a type argument?
**Answer:** A type parameter is declared in the generic class definition (e.g., `T` in `class Box<T>`). A type argument is the concrete type provided when instantiating (e.g., `String` in `Box<String>`).

### Q7: Can you use primitives as type arguments?
**Answer:** No. Java generics work only with reference types. When you use a primitive type (like `int`), Java automatically boxes it to its wrapper class (`Integer`). This is called autoboxing.

### Q8: What is the diamond operator (`<>`)?
**Answer:** The diamond operator, introduced in Java 7, allows the compiler to infer the type arguments from the context. Instead of `Box<String> box = new Box<String>()`, you can write `Box<String> box = new Box<>()`.

### Q9: Are generic types reified at runtime?
**Answer:** No. Due to type erasure, generic type information is removed at compile time. At runtime, `List<String>` and `List<Integer>` are both just `List`. This ensures backward compatibility with pre-generics code.

### Q10: What happens if you use raw types?
**Answer:** Raw types lose compile-time type safety. The compiler cannot catch type mismatches, and you must cast manually, which can cause ClassCastException at runtime. Raw types are only for backward compatibility with pre-generics code.

### Q11: Can a generic class extend another generic class?
**Answer:** Yes. For example: `class StringList extends ArrayList<String>` or `class GenericList<T> extends ArrayList<T>`. The type parameters can be passed through or fixed.

### Q12: What is a generic interface?
**Answer:** A generic interface is an interface with type parameters. For example: `interface Repository<T, ID> { T findById(ID id); }`. Classes implementing it can specify concrete types or remain generic.

### Q13: Can you declare a generic class with a bounded type parameter?
**Answer:** Yes. For example: `class SortedBox<T extends Comparable<T>> { ... }`. This constrains `T` to types that implement `Comparable`, allowing you to call `compareTo()` on instances of `T`.

### Q14: What is the benefit of using generics over Object-based approaches?
**Answer:** Generics provide compile-time type safety, eliminate explicit casting, enable better code documentation, and allow the compiler to perform type checking. They also enable more expressive APIs and reduce runtime ClassCastException errors.

### Q15: Can you create an instance of a generic type with `new T()`?
**Answer:** No. Due to type erasure, `T` becomes `Object` at runtime, so `new T()` is not valid. To create instances, pass a `Class<T>` parameter and use `clazz.getDeclaredConstructor().newInstance()`.

# Templates Quiz

## Questions

---

### MCQ 1: Template Instantiation
When is template code instantiated?

A) At runtime when the function is called
B) At compile time when the template is used with specific types
C) At link time
D) At program startup

---

### MCQ 2: Template Argument Deduction
What happens in this code?

```cpp
template <typename T>
T add(T a, T b) { return a + b; }

auto result = add(1, 2.5);
```

A) T is deduced as `int` — `2.5` is truncated
B) T is deduced as `double` — `1` is promoted
C) Compilation error — ambiguous deduction
D) T is deduced as `auto`

---

### MCQ 3: Full vs Partial Specialization
What is the difference between full and partial template specialization?

A) No difference
B) Full specialization fixes ALL template parameters; partial fixes only some
C) Partial specialization is for functions only
D) Full specialization is never needed

---

### MCQ 4: SFINAE
What does SFINAE stand for and what does it do?

A) Substitution Failure Is Not An Error — prevents hard errors when template substitution fails
B) Simple Function Is Not An Error — allows inline functions
C) Static Function Is Not An Error — enables static polymorphism
D) Single Type Is Not An Error — simplifies template syntax

---

### MCQ 5: Variadic Templates
What does `sizeof...` do in a variadic template?

A) Returns the size of the first argument
B) Returns the number of arguments in the parameter pack
C) Returns the total size of all arguments
D) Returns the size of the template parameter

---

### Code Output 1
What is the output?

```cpp
template <typename T>
void print(T val) {
    std::cout << "Generic: " << val << "\n";
}

template <>
void print<int>(int val) {
    std::cout << "Int: " << val << "\n";
}

int main() {
    print(42);
    print(3.14);
    print<std::string>("hello");
}
```

A)
```
Int: 42
Generic: 3.14
Generic: hello
```

B)
```
Generic: 42
Generic: 3.14
Int: hello
```

C)
```
Int: 42
Int: 3.14
Int: hello
```

D)
```
Generic: 42
Int: 3.14
Generic: hello
```

---

### Code Output 2
What is the output?

```cpp
template <typename T>
struct Wrapper {
    T value;
    void print() { std::cout << "Wrapper<default>: " << value << "\n"; }
};

template <typename T>
struct Wrapper<T*> {
    T* value;
    void print() { std::cout << "Wrapper<pointer>: " << *value << "\n"; }
};

int main() {
    Wrapper<int> w1{42};
    Wrapper<double*> w2{new double(3.14)};
    w1.print();
    w2.print();
    delete w2.value;
}
```

A)
```
Wrapper<default>: 42
Wrapper<pointer>: 3.14
```

B)
```
Wrapper<default>: 42
Wrapper<default>: 3.14
```

C)
```
Wrapper<pointer>: 42
Wrapper<pointer>: 3.14
```

D) Compilation error

---

### Bug Finding 1
Find the bug in this code:

```cpp
// template.cpp (separate file)
template <typename T>
T add(T a, T b) {
    return a + b;
}

// main.cpp
#include "template.cpp"

int main() {
    add(1, 2);   // Works
    add(1.0, 2.0); // Linker error!
}
```

Why does the second call fail?

---

### Bug Finding 2
Find the bug:

```cpp
template <typename T>
T* create() {
    return new T();
}

int main() {
    int* p = create();  // What's wrong?
    delete p;
}
```

---

### Scenario 1: Design Choice
You need a function that works for `int`, `double`, `std::string`, and custom numeric types. The function should only compile for types that support `+` and `<`. Which approach is best?

A) Write 4 overloads
B) Use `void*` with runtime type checking
C) Use C++20 concepts to constrain the template
D) Use macros

---

### Scenario 2: Performance
Your library has a heavy template class instantiated for 20 different types. Each TU that uses it generates a full copy. Build time is 45 minutes. What should you do?

A) Remove templates entirely
B) Use `extern template` declarations in headers and explicit instantiation in one .cpp file
C) Move all code to runtime polymorphism
D) Use `#include` guards more aggressively

---

## Answers

---

### MCQ 1: B
**At compile time when the template is used with specific types.** The compiler generates specialized code for each unique set of template arguments. This is why template implementations must be in headers — the compiler needs to see them to instantiate.

### MCQ 2: C
**Compilation error — ambiguous deduction.** `T` cannot be both `int` and `double` simultaneously. The compiler cannot deduce a single type. Fix: `add<double>(1, 2.5)` or `add(1.0, 2.5)`.

### MCQ 3: B
**Full specialization fixes ALL template parameters; partial fixes only some.** Full: `template <> class Foo<int> { }` — completely replaces the template. Partial: `template <typename T> class Foo<T*> { }` — still has one parameter, but specialized for pointer types.

### MCQ 4: A
**Substitution Failure Is Not An Error.** When the compiler tries to substitute a type into a template and fails, it doesn't produce an error — it simply removes that overload from consideration. This enables compile-time function selection based on type properties.

### MCQ 5: B
**Returns the number of arguments in the parameter pack.** `sizeof...(Args)` gives the count of types in the pack; `sizeof...(args)` gives the count of values. It's evaluated at compile time.

### Code Output 1: A
```
Int: 42
Generic: 3.14
Generic: hello
```
The explicit specialization `print<int>` is used for `int` arguments. `3.14` deduces `T=double` (no specialization). `"hello"` uses the explicit `std::string` template argument.

### Code Output 2: A
```
Wrapper<default>: 42
Wrapper<pointer>: 3.14
```
`Wrapper<int>` uses the generic template. `Wrapper<double*>` matches the partial specialization for `T*`, which dereferences the pointer before printing.

### Bug Finding 1
**The template definition is in a separate .cpp file.** When `main.cpp` includes `template.cpp`, it sees the template declaration but the compiler doesn't instantiate `add<double>` because the template definition isn't visible in the same translation unit at the point of call. Template implementations must be in header files (or explicitly instantiated with `template double add<double>(double, double);`).

### Bug Finding 2
**Cannot deduce `T` from no arguments.** `create<int>()` works if you specify the type explicitly, but `create()` with no arguments gives a deduction failure because there's nothing to deduce `T` from. Fix: `create<int>()` or add a default: `template <typename T = int>`.

### Scenario 1: C
**C++20 concepts.** They provide compile-time constraints with clear error messages. You can define a concept like `template <typename T> concept Addable = requires(T a, T b) { { a + b }; { a < b }; };` and use it as `template <Addable T>`.

### Scenario 2: B
**`extern template` with explicit instantiation.** Declare `extern template class HeavyClass<int>;` in the header to prevent each TU from instantiating independently. Put the actual template instantiation in a single .cpp file. This reduces duplicate compilation and can cut build time by 80%+.

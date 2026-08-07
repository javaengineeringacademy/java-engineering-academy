# Templates

## What it is
A feature that allows writing generic, type-independent code that works with any data type.

## Why it exists
To enable code reuse and type safety without runtime overhead.

## When to use it
When you need to write functions or classes that work with multiple data types.

## How it works

### Function Templates
```cpp
template <typename T>
T max(T a, T b) {
    return (a > b) ? a : b;
}

int main() {
    std::cout << max(5, 10) << std::endl;      // 10
    std::cout << max(3.14, 2.71) << std::endl; // 3.14
}
```

### Class Templates
```cpp
template <typename T>
class Stack {
private:
    std::vector<T> elements;
public:
    void push(T const&);
    T pop();
};
```

### Template Specialization
```cpp
template <>
class Stack<std::string> {
    // Specialized implementation for strings
};
```

## Production Incidents

### Incident 1: Template Bloat Increasing Binary Size
**Problem**: A financial analytics library grew from 12MB to 180MB after introducing template-heavy pricing models, breaking embedded deployment on a resource-constrained trading device.

**Cause**: A `PricingModel<T>` template was instantiated for 47 different types (float, double, 45 custom numeric types). Each instantiation generated a full copy of the template body — including ~2000 lines of validation, logging, and interpolation logic — resulting in massive binary duplication.

**Impact**: The library couldn't fit on the target trading device (200MB flash). Build times increased from 3 minutes to 25 minutes. Link times exceeded 10 minutes, blocking CI pipelines.

**Detection**: `bloaty` binary size analysis showed the template instantiation accounted for 85% of the binary. `nm --size-sort` revealed hundreds of near-identical function symbols differing only in type suffixes.

**Solution**: Factored type-independent logic into a non-template base class (`PricingEngineBase`). Reduced the template body to type-specific operations only, calling the base class for shared logic. Used `extern template` declarations to control instantiation across translation units.

**Prevention**: Monitor binary size in CI with a size budget. Use `bloaty` in code review for template-heavy changes. Prefer type erasure or runtime polymorphism when type-specific behavior is minimal. Limit template parameters to the narrowest necessary type.

---

### Incident 2: SFINAE Failure Causing Cryptic Error
**Problem**: A developer spent 6 hours debugging a template compilation error that produced a 200-line error message with no obvious cause.

**Cause**: A `serialize()` function template had a SFINAE constraint requiring `std::is_arithmetic_v<T>`. When called with `std::string`, the substitution failed silently, but the error was reported from deep within the template instantiation chain — pointing to a completely unrelated line in the STL internals.

**Impact**: Developer productivity loss of 6 hours. The bug delayed a feature release by one day. Two other team members hit the same class of error within the next month.

**Detection**: Compiler error output contained "candidate function template not viable" with a substitution failure buried in the output. No static analysis tool caught it because SFINAE failures are by design silent.

**Solution**: Replaced SFINAE with C++20 `requires` clauses: `template <typename T> requires std::is_arithmetic_v<T> void serialize(T value)`. This produces clear, readable error messages pointing directly at the constraint violation.

**Prevention**: Use C++20 concepts (`requires`) instead of SFINAE for all new code. For pre-C++20 codebases, create `static_assert` friendly wrappers that produce human-readable messages. Add a `CONTRIBUTING.md` note about the SFINAE-to-concepts migration path.

---

## Production Checklist
- [ ] Use templates for generic algorithms
- [ ] Consider template specialization for type-specific optimizations
- [ ] Use SFINAE for compile-time type checking
- [ ] Keep templates in header files
- [ ] Use `constexpr` for compile-time computation
- [ ] Prefer function overloading over template specialization

## Maturity Levels
- **Beginner**: Basic function and class templates
- **Intermediate**: Template specialization, SFINAE
- **Advanced**: Variadic templates, template metaprogramming

## Common Myths
- ❌ "Templates are slow because they generate code"
- ❌ "Templates are only for containers"
- ❌ "Template errors are impossible to understand"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Template | Generic code blueprint |
| Type Parameter | T in `template <typename T>` |
| Specialization | Custom implementation for specific types |
| SFINAE | Substitution failure is not an error |
| Variadic | Templates with variable number of arguments |

## Related Topics
- [STL](../04-stl/)
- [Modern C++](../08-modern-cpp/)
- [Performance](../11-performance/)
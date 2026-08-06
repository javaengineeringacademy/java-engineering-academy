# C++ Best Practices

## Memory Management
- Prefer smart pointers over raw pointers
- Use RAII for resource management
- Avoid `new`/`delete` directly

## Modern C++
- Use `auto` for type inference
- Use range-based for loops
- Use `nullptr` over `NULL`

## Performance
- Pass large objects by reference
- Use `std::move` for transferring ownership
- Prefer `std::array` over C arrays

## Safety
- Use `const` wherever possible
- Use `constexpr` for compile-time constants
- Avoid C-style casts

## Testing
- Use Google Test or Catch2
- Test edge cases
- Use sanitizers (ASan, UBSan)

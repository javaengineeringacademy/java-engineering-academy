# C++ Patterns

## RAII (Resource Acquisition Is Initialization)
```cpp
class FileHandler {
    FILE* file;
public:
    FileHandler(const char* path) : file(fopen(path, "r")) {}
    ~FileHandler() { if (file) fclose(file); }
};
```

## CRTP (Curiously Recurring Template Pattern)
```cpp
template <typename Derived>
class Base {
public:
    void interface() { static_cast<Derived*>(this)->implementation(); }
};
```

## Pimpl (Pointer to Implementation)
```cpp
class Widget {
    struct Impl;
    unique_ptr<Impl> pImpl;
public:
    Widget();
    ~Widget();
};
```

## Observer Pattern (via std::function)
```cpp
class Event {
    std::vector<std::function<void()>> handlers;
public:
    void subscribe(std::function<void()> h) { handlers.push_back(h); }
    void notify() { for (auto& h : handlers) h(); }
};
```

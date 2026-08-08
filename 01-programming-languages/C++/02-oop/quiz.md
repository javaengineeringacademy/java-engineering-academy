# Object-Oriented Programming (OOP) Quiz

## Questions

---

### MCQ 1: Virtual Destructors
What happens if you delete a derived class object through a base class pointer when the base class does NOT have a virtual destructor?

A) The derived destructor runs first, then the base destructor
B) Only the base class destructor runs — derived cleanup is skipped (UNDEFINED BEHAVIOR if derived has resources)
C) Both destructors run in random order
D) The program crashes immediately

---

### MCQ 2: Object Slicing
Which of the following prevents object slicing when storing polymorphic objects?

A) `std::vector<Animal>`
B) `std::vector<Animal*>`
C) `std::vector<std::unique_ptr<Animal>>`
D) Both B and C

---

### MCQ 3: Rule of Five
If a class manages a raw resource (e.g., `new int[]`), which special member functions should you define?

A) Only destructor
B) Destructor, copy constructor, copy assignment operator
C) All five: destructor, copy/move constructors, copy/move assignment operators
D) Only the constructor

---

### MCQ 4: Pure Virtual Functions
What happens if you try to instantiate a class that has at least one pure virtual function?

A) The pure virtual function uses a default implementation
B) Compilation error — the class is abstract and cannot be instantiated
C) Runtime error
D) The pure virtual function is skipped

---

### MCQ 5: Inheritance vs Composition
When should you prefer composition over inheritance?

A) When the derived class IS-A base class
B) When you need runtime polymorphism
C) When the relationship is "has-a" and you want explicit control over lifetime
D) Always — inheritance is never useful

---

### Code Output 1
What is the output of this code?

```cpp
class Base {
public:
    virtual void show() { std::cout << "Base "; }
    ~Base() { std::cout << "~Base "; }
};

class Derived : public Base {
public:
    void show() override { std::cout << "Derived "; }
    ~Derived() { std::cout << "~Derived "; }
};

int main() {
    Base* p = new Derived();
    p->show();
    delete p;
}
```

A) `Derived ~Derived ~Base`
B) `Derived ~Base`
C) `Base ~Base`
D) `Base ~Derived ~Base`

---

### Code Output 2
What is the output?

```cpp
class A {
public:
    A() { std::cout << "A "; }
    virtual ~A() { std::cout << "~A "; }
};

class B : public A {
public:
    B() { std::cout << "B "; }
    ~B() override { std::cout << "~B "; }
};

class C : public B {
public:
    C() { std::cout << "C "; }
    ~C() override { std::cout << "~C "; }
};

int main() {
    A* p = new C();
    delete p;
}
```

A) `A B C ~C ~B ~A`
B) `A B C ~A`
C) `C B A ~A ~B ~C`
D) `A B C ~C ~B ~A`

---

### Code Output 3
What is the output?

```cpp
class Base {
public:
    virtual void print() { std::cout << "Base "; }
};

class Derived : public Base {
public:
    void print() override { std::cout << "Derived "; }
};

void call(Base obj) {
    obj.print();
}

int main() {
    Derived d;
    call(d);
}
```

A) `Derived`
B) `Base`
C) Compilation error
D) Undefined behavior

---

### Bug Finding 1
Find the bug in this code:

```cpp
class Shape {
public:
    double area() { return 0; }
};

class Circle : public Shape {
    double radius_;
public:
    Circle(double r) : radius_(r) {}
    double area() { return 3.14 * radius_ * radius_; }
};

int main() {
    Shape* s = new Circle(5.0);
    std::cout << s->area() << "\n";
    delete s;
}
```

What is wrong? How would you fix it?

---

### Bug Finding 2
Find the bug in this code:

```cpp
class Base {
    int* data_;
public:
    Base(int val) : data_(new int(val)) {}
    ~Base() { delete data_; }
    int get() const { return *data_; }
};

class Derived : public Base {
    int* extra_;
public:
    Derived(int v1, int v2) : Base(v1), extra_(new int(v2)) {}
    ~Derived() { delete extra_; }
};

int main() {
    Base* p = new Derived(1, 2);
    delete p;  // What happens here?
}
```

---

### Scenario 1: Design Decision
You are building a game engine. You need to store 10,000 entities (Player, Enemy, NPC, Projectile) that all share a common interface (update, render, collide). Entities have different sizes and behaviors. Which approach is best?

A) `std::vector<Entity>` storing by value
B) `std::vector<Entity*>` with raw pointers and manual delete
C) `std::vector<std::unique_ptr<Entity>>`
D) `std::vector<void*>` for maximum flexibility

---

### Scenario 2: Architecture Choice
You're designing a notification system. You have EmailNotifier, SMSNotifier, and PushNotifier. Users can register multiple notifiers. When an event occurs, ALL registered notifiers should be triggered. Which pattern fits best?

A) Singleton — one notification manager
B) Observer — notifiers observe events
C) Factory — create notifiers dynamically
D) Strategy — select one notifier at runtime

---

## Answers

---

### MCQ 1: B
**Only the base class destructor runs — derived cleanup is skipped.** This is undefined behavior if the derived class holds resources (memory, file handles). The fix is adding `virtual ~Base() = default;` to any class with virtual functions.

### MCQ 2: C
**`std::vector<std::unique_ptr<Animal>>`** is the correct and safest approach. Option B (`Animal*`) works but requires manual memory management and risks leaks. Option A causes slicing. `unique_ptr` provides both polymorphism and automatic cleanup.

### MCQ 3: C
**All five.** When managing raw resources, the default-generated special members do shallow copies, leading to double-free bugs. You need: destructor (to free), copy ctor (deep copy), copy assignment (deep copy + self-assignment check), move ctor (steal ownership), move assignment (steal + cleanup).

### MCQ 4: B
**Compilation error.** A class with any pure virtual function is abstract and cannot be instantiated. It can only be used as a base class. The pure virtual function can have a body (defined out-of-line), but the class remains abstract.

### MCQ 5: C
**When the relationship is "has-a."** Composition gives you explicit control over lifetime, avoids the fragile base class problem, and makes ownership clear. Inheritance should model a true "is-a" behavioral contract. Option D is wrong — inheritance is valuable for polymorphism.

### Code Output 1: B
`Derived ~Base`. The virtual `show()` correctly calls `Derived::show()` (prints "Derived"). However, because `~Base()` is NOT virtual, `delete p` only calls `~Base()` — `~Derived()` is never called. The correct fix is `virtual ~Base() = default;`.

### Code Output 2: A
`A B C ~C ~B ~A`. Construction goes top-down (A → B → C). Destruction goes bottom-up (C → B → A) because `~A()` is virtual. This is the correct behavior when the destructor is virtual.

### Code Output 3: B
`Base`. The function `call()` takes `Base` **by value**, so when `Derived d` is passed, it is **sliced** to `Base`. The virtual dispatch is lost. The call `obj.print()` invokes `Base::print()`. This is why you should pass polymorphic objects by reference or pointer.

### Bug Finding 1
**Bug**: `area()` is not virtual in `Base`, and `~Base()` is not virtual. Two problems:
1. `s->area()` calls `Base::area()` (returns 0) — static dispatch because `area()` isn't virtual.
2. `delete s` on a `Derived*` cast to `Base*` with non-virtual destructor — undefined behavior, `~Derived()` never runs.

**Fix**: Add `virtual` to `area()` and `virtual ~Base() = default;`.

### Bug Finding 2
**Bug**: `~Base()` is NOT virtual, so `delete p` only calls `~Base()`. The `~Derived()` destructor never runs, leaking `extra_`. Additionally, `~Base()` calls `delete data_` on a single `int` allocated with `new int` — that's correct. But if `data_` were an array (`new int[n]`), it would need `delete[]`.

**Fix**: Add `virtual` before `~Base()`.

### Scenario 1: C
**`std::vector<std::unique_ptr<Entity>>`** is correct. It preserves polymorphism (no slicing), manages memory automatically (no leaks), and is the idiomatic modern C++ approach. Raw pointers (B) risk leaks. Value storage (A) slices. `void*` (D) loses type safety entirely.

### Scenario 2: B
**Observer pattern.** The event source (subject) maintains a list of observers (notifiers). When an event occurs, it notifies all registered observers. This matches the requirement of multiple notifiers reacting to the same event. Factory creates objects; Strategy selects one algorithm; Singleton limits to one instance.

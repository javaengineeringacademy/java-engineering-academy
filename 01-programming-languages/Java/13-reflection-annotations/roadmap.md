# Reflection & Annotations — Visual Learning Path

## The Journey

```
START
  │
  ▼
┌─────────────────────────────────────────────────────────┐
│  01. INTRODUCTION                                       │
│  What is reflection? Why does it exist?                 │
│  When should you use it vs avoid it?                    │
│  ─────────────────────────────────────────────────────  │
│  Time: 30-45 min  │  Difficulty: ★☆☆☆☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  02. CLASS INTROSPECTION                                │
│  getClass() • .class • Class.forName()                  │
│  getSimpleName() • getPackageName() • getModifiers()    │
│  ─────────────────────────────────────────────────────  │
│  Time: 45-60 min  │  Difficulty: ★★☆☆☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  03. FIELD ACCESS                                       │
│  getDeclaredFields() • set() • get()                    │
│  Accessibility • Modifiers • Type introspection         │
│  ─────────────────────────────────────────────────────  │
│  Time: 45-60 min  │  Difficulty: ★★☆☆☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  04. METHOD INVOCATION                                  │
│  getDeclaredMethods() • invoke()                        │
│  Parameter types • Return types • Varargs               │
│  ─────────────────────────────────────────────────────  │
│  Time: 45-60 min  │  Difficulty: ★★★☆☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  05. CONSTRUCTOR ACCESS                                 │
│  getDeclaredConstructors() • newInstance()              │
│  Parameter matching • Inner class instantiation         │
│  ─────────────────────────────────────────────────────  │
│  Time: 30-45 min  │  Difficulty: ★★☆☆☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  06. DYNAMIC PROXY                                      │
│  Proxy.newProxyInstance() • InvocationHandler           │
│  Real-world AOP • Logging • Transactions                │
│  ─────────────────────────────────────────────────────  │
│  Time: 60-90 min  │  Difficulty: ★★★★☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  07. CUSTOM ANNOTATIONS                                 │
│  @interface • Retention • Target                        │
│  Meta-annotations • Annotation elements                 │
│  ─────────────────────────────────────────────────────  │
│  Time: 45-60 min  │  Difficulty: ★★★☆☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  08. ANNOTATION PROCESSING                              │
│  AbstractProcessor • Compile-time processing            │
│  Code generation • Filer API • Messager                 │
│  ─────────────────────────────────────────────────────  │
│  Time: 60-90 min  │  Difficulty: ★★★★☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  09. REAL-WORLD USE CASES                               │
│  Spring @Autowired • JPA @Entity                        │
│  JUnit @Test • Jackson • Lombok                         │
│  ─────────────────────────────────────────────────────  │
│  Time: 45-60 min  │  Difficulty: ★★★☆☆                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
              ┌──────────────────┐
              │  EXERCISES (45)  │
              │  PROJECTS (3)    │
              │  INTERVIEW PREP  │
              └──────────────────┘
```

---

## Skill Checkpoints

After completing each section, you should be able to:

- [ ] **01:** Explain reflection to a junior developer
- [ ] **02:** Get any `Class` object three different ways
- [ ] **03:** Read and write private fields on any object
- [ ] **04:** Invoke any method dynamically, including with varargs
- [ ] **05:** Create objects without knowing their type at compile time
- [ ] **06:** Build a simple AOP proxy for method interception
- [ ] **07:** Define custom annotations with all meta-annotation options
- [ ] **08:** Write a compile-time annotation processor that generates code
- [ ] **09:** Identify reflection usage in Spring, JPA, and JUnit source code

---

## Difficulty Progression

```
★★☆☆☆ ──── 02, 03, 05
★★★☆☆ ──── 04, 07, 09
★★★★☆ ──── 06, 08
```

---

## Related Modules

| Module | Connection |
|--------|-----------|
| 04-collections | Reflection uses collections internally |
| 06-exception-handling | Reflection throws checked exceptions |
| 08-generics | Type erasure affects reflection on generics |
| 11-design-patterns | Proxy pattern, Factory pattern |
| 12-concurrency | Thread safety in reflective access |

---

*Follow the arrows. Master each box before moving to the next.*

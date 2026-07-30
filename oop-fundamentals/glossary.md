# OOP Glossary - Sprint 2

---

## A

| Term | Definition |
|------|------------|
| **Abstraction** | Hiding complex implementation details, exposing only essential features |
| **Abstract Class** | Class that cannot be instantiated, may contain abstract and concrete methods |
| **Abstract Method** | Method declared without implementation, must be overridden by subclass |
| **Aggregation** | Weak "has-a" relationship; part can exist independently of whole |
| **Annotation** | Metadata providing data about program elements (`@Override`, `@Deprecated`) |
| **Association** | General relationship where objects know about each other |

## B

| Term | Definition |
|------|------------|
| **Binding** | Connecting method call to method body (static = compile-time, dynamic = runtime) |
| **Builder Pattern** | Creational pattern for constructing complex objects step by step |
| **Bytecode** | Intermediate code executed by JVM |

## C

| Term | Definition |
|------|------------|
| **Class** | Blueprint/template defining structure and behavior of objects |
| **Class Variable** | Static field shared by all instances |
| **Composition** | Strong "has-a" relationship; part cannot exist without whole |
| **Constructor** | Special method initializing new objects, same name as class, no return type |
| **Constructor Chaining** | Calling one constructor from another using `this()` or `super()` |
| **Coupling** | Degree of interdependence between modules; low coupling = good design |
| **Coupling (Loose)** | Minimal dependencies between components |

## D

| Term | Definition |
|------|------------|
| **Default Method** | Interface method with implementation (Java 8+) |
| **Dependency Injection** | Providing dependencies from outside rather than creating internally |
| **Diamond Problem** | Ambiguity in multiple inheritance when class inherits from two classes with common ancestor |
| **Downcasting** | Casting superclass reference to subclass type (requires `instanceof` check) |

## E

| Term | Definition |
|------|------------|
| **Encapsulation** | Bundling data and methods, restricting direct access to internal state |
| **Enum** | Special class representing fixed set of constants |
| **Exception** | Event disrupting normal flow; checked (compile-time) or unchecked (runtime) |

## F

| Term | Definition |
|------|------------|
| **Factory Method** | Creational pattern delegating object creation to subclasses |
| **Field** | Variable declared in class (instance or static) |
| **Final** | Keyword preventing modification (class, method, field, variable) |
| **Functional Interface** | Interface with exactly one abstract method (lambda compatible) |

## G

| Term | Definition |
|------|------------|
| **Garbage Collection** | Automatic memory management reclaiming unused objects |
| **Generics** | Parameterized types enabling type-safe reusable code |
| **Getter** | Method returning field value (e.g., `getName()`) |

## H

| Term | Definition |
|------|------------|
| **HashCode** | Integer representing object identity; must be consistent with `equals()` |
| **Heap** | Memory area where objects are allocated |
| **Has-a** | Composition/aggregation relationship ("has-a" relationship) |

## I

| Term | Definition |
|------|------------|
| **Immutable** | Object whose state cannot change after creation |
| **Implements** | Keyword for class implementing interface |
| **Inheritance** | Mechanism deriving new class from existing one (`extends`) |
| **Instance** | Individual object created from class |
| **Instance Variable** | Non-static field unique to each object |
| **Interface** | Contract defining methods without implementation (pre-Java 8) |
| **Is-a** | Inheritance relationship ("is-a" relationship) |

## J

| Term | Definition |
|------|------------|
| **JVM** | Java Virtual Machine executing bytecode |
| **Javadoc** | Documentation generated from source code comments |

## K

| Term | Definition |
|------|------------|
| **Keyword** | Reserved word with special meaning (`class`, `extends`, `implements`) |

## L

| Term | Definition |
|------|------------|
| **Lambda Expression** | Anonymous function implementing functional interface |
| **Late Binding** | Method resolution at runtime (dynamic polymorphism) |
| **Liskov Substitution Principle (LSP)** | Subtype must be substitutable for base type without altering correctness |
| **Local Variable** | Variable declared inside method/block |

## M

| Term | Definition |
|------|------------|
| **Method** | Block of code performing specific task, associated with class |
| **Method Overloading** | Multiple methods with same name, different parameters (compile-time polymorphism) |
| **Method Overriding** | Subclass providing specific implementation of parent method (runtime polymorphism) |
| **Mutable** | Object whose state can change after creation |

## N

| Term | Definition |
|------|------------|
| **Nested Class** | Class defined within another class |
| **Null** | Reference pointing to no object |

## O

| Term | Definition |
|------|------------|
| **Object** | Instance of class with state, behavior, identity |
| **Object Class** | Root of class hierarchy; all classes implicitly extend `Object` |
| **Open/Closed Principle** | Open for extension, closed for modification |
| **Overloading** | Same method name, different parameters |
| **Overriding** | Subclass providing new implementation of parent method |

## P

| Term | Definition |
|------|------------|
| **Package** | Namespace organizing related classes |
| **Parameter** | Variable in method signature receiving argument |
| **Polymorphism** | "Many forms" - same interface, different implementations |
| **Primitive Types** | `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean` |

## Q

| Term | Definition |
|------|------------|
| **Qualifier** | Package/class name prefix for disambiguation |

## R

| Term | Definition |
|------|------------|
| **Record** | Immutable data carrier (Java 16+) with auto-generated methods |
| **Reference** | Variable holding memory address of object |
| **Reflection** | Inspecting/manipulating classes at runtime |
| **Reference Variable** | Variable holding object reference (memory address) |

## S

| Term | Definition |
|------|------------|
| **Setter** | Method modifying field value (e.g., `setName()`) |
| **Single Responsibility Principle (SRP)** | Class should have only one reason to change |
| **Solid** | Five design principles: SRP, OCP, LSP, ISP, DIP |
| **Stack** | Memory for method calls and local variables |
| **Static** | Belongs to class, not instance; shared across all instances |
| **Static Binding** | Method resolution at compile time (overloading) |
| **Static Method** | Method belonging to class, not instance |
| **Subclass** | Class inheriting from another class (`extends`) |
| **Superclass** | Class being inherited from (`extends`) |
| **Super** | Keyword referring to parent class (`super.method()`, `super()`) |

## T

| Term | Definition |
|------|------------|
| **This** | Reference to current object instance |
| **Type** | Classification defining allowed values and operations |

## U

| Term | Definition |
|------|------------|
| **Upcasting** | Casting subclass reference to superclass type (always safe) |
| **Unified Modeling Language (UML)** | Visual notation for system design |

## V

| Term | Definition |
|------|------------|
| **Variable** | Named storage location holding value/reference |
| **Virtual Method** | Method resolved at runtime (overridden method) |
| **Visibility** | Access level controlled by modifiers |

## W

| Term | Definition |
|------|------------|
| **Wrapper Class** | Object representation of primitive type (`Integer`, `Double`, etc.) |

## X-Y-Z

| Term | Definition |
|------|------------|
| **YAGNI** | "You Aren't Gonna Need It" - avoid adding functionality until needed |
| **Zero-Argument Constructor** | Constructor with no parameters (default if none defined) |

---

## Quick Reference: Symbols in UML Class Diagrams

| Symbol | Meaning |
|--------|---------|
| `+` | `public` |
| `-` | `private` |
| `#` | `protected` |
| `~` | package-private (default) |
| `<>>` | Interface |
| `{abstract}` | Abstract class/method |
| `*` | Multiplicity (0..*) |
| `1` | Multiplicity (exactly 1) |
| `1..*` | Multiplicity (one or more) |
| `0..1` | Multiplicity (optional) |
| `└─` | Generalization (inheritance) |
| `─◇` | Composition (filled diamond) |
| `─◇` | Aggregation (empty diamond) |
| `──` | Association |

---

## Common Acronyms

| Acronym | Full Form |
|---------|-----------|
| API | Application Programming Interface |
| DI | Dependency Injection |
| DTO | Data Transfer Object |
| DIP | Dependency Inversion Principle |
| DRY | Don't Repeat Yourself |
| IOC | Inversion of Control |
| LSP | Liskov Substitution Principle |
| OCP | Open/Closed Principle |
| OOP | Object-Oriented Programming |
| POJO | Plain Old Java Object |
| SRP | Single Responsibility Principle |
| UML | Unified Modeling Language |

---

*Use this glossary as a quick reference during study and interviews!*
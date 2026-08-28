# Modern Java Features - Decision Guide

## When to Use Each Feature

### Records vs Traditional Classes

**Use Records when:**
- Data is immutable and needs no validation
- You need automatic equals(), hashCode(), toString()
- DTOs, value objects, or transfer objects
- The class primarily holds data

**Use Traditional Classes when:**
- You need mutable state
- Complex business logic with state transitions
- Builder pattern with step-by-step construction
- Extending other classes is required

### Sealed Classes

**Use Sealed Classes when:**
- You want to restrict which classes can implement an interface
- Modeling algebraic data types
- Exhaustive switch/match on type hierarchy
- API design where only known subtypes are valid

**Avoid when:**
- You need an open extensible API
- The hierarchy is genuinely dynamic
- Third-party code needs to extend

### Pattern Matching for switch

**Use Pattern Matching when:**
- Replacing chains of instanceof checks
- Type-specific processing without casting
- Complex conditional logic based on types

**Avoid when:**
- Simple equality checks (use regular switch)
- Only one or two types to check

### Text Blocks

**Use Text Blocks when:**
- SQL queries, JSON templates, HTML/XML snippets
- Multi-line strings with natural formatting
- Template strings that would need escaping

**Avoid when:**
- Single-line strings (regular strings are clearer)
- Dynamic content that needs runtime interpolation (use String.format or templates)

### Switch Expressions

**Use Switch Expressions when:**
- Switch is used as an expression (assigning a value)
- All cases should return a value
- You want exhaustiveness checking with arrows

**Avoid when:**
- Fall-through behavior is intentionally needed
- Complex side effects in cases

### var Type Inference

**Use var when:**
- Type is obvious from context (constructors, factory methods)
- Complex generic types that clutter code
- Lambda expressions where type is clear
- Diamond operator with anonymous classes

**Avoid when:**
- Type is not obvious from context
- Public API signatures (prefer explicit types)
- Method parameters (inconsistent readability)

### Multi-catch

**Use Multi-catch when:**
- Multiple exceptions handled identically
- Reducing code duplication in catch blocks
- When exception types are unrelated but share handling

### instanceof Pattern Matching

**Use when:**
- Type checking followed by casting and using the object
- Replaces if(x instanceof Type) { Type t = (Type)x; ... }
- Combined with pattern variables in conditions

## Migration Strategy

1. Start with Records for data classes
2. Adopt var for complex local variable declarations
3. Replace instanceof chains with pattern matching
4. Use text blocks for string templates
5. Convert switch statements to expressions where appropriate
6. Restrict hierarchies with sealed classes

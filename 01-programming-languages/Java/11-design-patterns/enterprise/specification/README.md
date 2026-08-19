# Specification Pattern

## Intent
Encapsulate business rules as composable, reusable objects that can be combined using logical operators (AND, OR, NOT).

## Key Components
- **Specification Interface**: Defines the isSatisfiedBy contract
- **Composite Specifications**: AndSpecification, OrSpecification, NotSpecification
- **Concrete Specifications**: Domain-specific rules (price, category, etc.)

## When to Use
- Complex business rules that need to be combined dynamically
- Filtering domain objects by multiple criteria
- Rules that change frequently
- Query building without scattering conditionals

## Benefits
- Single Responsibility — each spec encapsulates one rule
- Open/Closed — add new specs without modifying existing code
- Composable — combine specs with AND, OR, NOT
- Testable — each spec is independently testable

## Example
```java
Specification<Product> expensive = new PriceSpecification(100);
Specification<Product> electronics = new CategorySpecification("Electronics");
Specification<Product> rule = new AndSpecification<>(expensive, electronics);
// rule.isSatisfiedBy(product) checks both conditions
```

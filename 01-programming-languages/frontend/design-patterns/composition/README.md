# Composition over Inheritance

## Overview

Composition over inheritance is a design principle where objects are composed with other objects to achieve code reuse and behavior extension, rather than using class inheritance. In frontend development, this means building components by combining smaller, focused pieces rather than creating deep inheritance hierarchies.

## When to Use

- Building reusable component libraries
- Creating flexible and maintainable code
- Avoiding deep inheritance hierarchies
- Needing runtime behavior changes
- Working with complex component interactions

## Implementation

### React
```javascript
// Composition with Children
function Card({ children, title }) {
  return (
    <div className="card">
      <h3>{title}</h3>
      <div className="card-content">{children}</div>
    </div>
  );
}

function CardWithImage({ children, image, title }) {
  return (
    <Card title={title}>
      <img src={image} alt={title} />
      {children}
    </Card>
  );
}

// Composition with Props
function Button({ children, variant, ...props }) {
  return (
    <button className={`btn btn-${variant}`} {...props}>
      {children}
    </button>
  );
}

function IconButton({ icon, children, ...props }) {
  return (
    <Button {...props}>
      <Icon name={icon} />
      <span>{children}</span>
    </Button>
  );
}
```

### Vue (Slots)
```html
<!-- Composition with Slots -->
<template>
  <div class="card">
    <h3><slot name="title"></slot></h3>
    <div class="card-content">
      <slot></slot>
    </div>
  </div>
</template>

<!-- Usage -->
<card>
  <template #title>Card Title</template>
  <p>Card content composed here</p>
</card>
```

### Angular (Content Projection)
```typescript
@Component({
  selector: 'app-card',
  template: `
    <div class="card">
      <h3><ng-content select="[card-title]"></ng-content></h3>
      <div class="card-content">
        <ng-content></ng-content>
      </div>
    </div>
  `
})
export class CardComponent {}

// Usage
<app-card>
  <div card-title>Title Here</div>
  <p>Content projected here</p>
</app-card>
```

## Best Practices

1. Favor small, focused components
2. Use composition to build complex components
3. Avoid deep inheritance chains
4. Create composable utilities and helpers
5. Use slots/projection for flexible layouts

## Interview Questions

1. What is the difference between composition and inheritance?
2. When should you use composition over inheritance?
3. How do slots enable composition in Vue?
4. What are the advantages of composition for testing?
5. How does composition improve code maintainability?

## References

- "Composition Over Inheritance" - Wikipedia
- "Gang of Four" Design Patterns
- React Documentation - Composition
- Vue.js Slots Documentation

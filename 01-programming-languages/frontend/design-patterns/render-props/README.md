# Render Props Pattern

## Overview

The Render Props pattern uses a component prop that is a function returning React elements. This function receives data from the component, allowing parent components to control what gets rendered while the child component handles the logic. It enables flexible component composition and logic sharing.

## When to Use

- Sharing component logic while allowing render customization
- Creating flexible component APIs
- Needing to pass data to children for rendering
- Building reusable behavior components
- Avoiding wrapper hell from HOCs

## Implementation

### React
```javascript
// Render Props Component
class MouseTracker extends React.Component {
  state = { x: 0, y: 0 };

  handleMouseMove = (e) => {
    this.setState({ x: e.clientX, y: e.clientY });
  };

  render() {
    return (
      <div onMouseMove={this.handleMouseMove}>
        {this.props.render(this.state)}
      </div>
    );
  }
}

// Usage
function App() {
  return (
    <MouseTracker
      render={({ x, y }) => (
        <div>Mouse position: {x}, {y}</div>
      )}
    />
  );
}

// Children as Function (variant)
class DataFetcher extends React.Component {
  state = { data: null, loading: true };

  componentDidMount() {
    fetch(this.props.url)
      .then(res => res.json())
      .then(data => this.setState({ data, loading: false }));
  }

  render() {
    return this.props.children(this.state);
  }
}

// Usage
<DataFetcher url="/api/data">
  {({ data, loading }) => (
    loading ? <Spinner /> : <DataList data={data} />
  )}
</DataFetcher>
```

### Vue (Scoped Slots)
```html
<!-- Parent Component -->
<mouse-tracker>
  <template v-slot="{ x, y }">
    <div>Mouse: {{ x }}, {{ y }}</div>
  </template>
</mouse-tracker>

<!-- MouseTracker Component -->
<template>
  <div @mousemove="updatePosition">
    <slot :x="x" :y="y"></slot>
  </div>
</template>

<script>
export default {
  data() { return { x: 0, y: 0 } },
  methods: {
    updatePosition(e) {
      this.x = e.clientX;
      this.y = e.clientY;
    }
  }
}
</script>
```

### Angular (Template Directive)
```typescript
// Directive
@Directive({ selector: '[appMouseTracker]' })
export class MouseTrackerDirective {
  x = 0;
  y = 0;

  @HostListener('mousemove', ['$event'])
  onMouseMove(e: MouseEvent) {
    this.x = e.clientX;
    this.y = e.clientY;
  }
}

// Usage
<div appMouseTracker #tracker="appMouseTracker">
  Mouse: {{ tracker.x }}, {{ tracker.y }}
</div>
```

## Best Practices

1. Use render props for flexible rendering
2. Keep the component responsible for logic only
3. Avoid deeply nested render props
4. Consider hooks as an alternative for simpler cases
5. Provide default rendering if needed

## Interview Questions

1. What is the Render Props pattern?
2. How does it differ from HOCs?
3. What are the benefits over HOCs?
4. How do React Hooks compare to render props?
5. When would you still use render props over hooks?

## References

- React Documentation - Render Props
- "Render Props" by Kent C. Dodds
- "Advanced React Patterns" Course
- React Apollo Client (render props usage)

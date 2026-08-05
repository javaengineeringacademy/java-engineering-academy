# Performance Patterns

## Overview

Performance patterns are techniques to optimize frontend applications for speed, responsiveness, and efficient resource usage. These patterns address rendering performance, memory management, and loading optimization to create smooth user experiences.

## When to Use

- Applications with large lists or datasets
- Complex UIs with frequent updates
- Slow initial page loads
- Mobile or low-bandwidth environments
- Applications with expensive computations

## Key Patterns

### Memoization
Cache expensive function results to avoid recalculation.

```javascript
// React - useMemo
function ExpensiveComponent({ data }) {
  const processed = useMemo(() => {
    return data.map(item => expensiveOperation(item));
  }, [data]);

  return <List items={processed} />;
}

// React - useCallback
const handleSubmit = useCallback((values) => {
  submitForm(values);
}, [dependencies]);
```

### Virtualization
Render only visible items in large lists.

```javascript
// React Virtualized
import { FixedSizeList } from 'react-window';

function VirtualList({ items }) {
  return (
    <FixedSizeList height={600} itemCount={items.length} itemSize={35}>
      {({ index, style }) => (
        <div style={style}>{items[index].name}</div>
      )}
    </FixedSizeList>
  );
}
```

### Lazy Loading
Load components or data only when needed.

```javascript
// React Lazy
const HeavyComponent = React.lazy(() => import('./HeavyComponent'));

function App() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <HeavyComponent />
    </Suspense>
  );
}

// Vue Async Component
const HeavyComponent = defineAsyncComponent(() => import('./HeavyComponent.vue'));
```

### Code Splitting
Split bundle into smaller chunks loaded on demand.

```javascript
// React - Route-based splitting
const Dashboard = React.lazy(() => import('./pages/Dashboard'));
const Settings = React.lazy(() => import('./pages/Settings'));

// Webpack dynamic import
const module = await import('./module');
```

### Debouncing and Throttling
Limit function execution frequency.

```javascript
// Debounce - Execute after delay
function debounce(fn, delay) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

// Throttle - Execute at most once per interval
function throttle(fn, limit) {
  let inThrottle;
  return (...args) => {
    if (!inThrottle) {
      fn(...args);
      inThrottle = true;
      setTimeout(() => inThrottle = false, limit);
    }
  };
}
```

### Image Optimization
Use lazy loading and modern formats.

```html
<img loading="lazy" src="image.webp" alt="Optimized" />
<picture>
  <source srcset="image.avif" type="image/avif" />
  <source srcset="image.webp" type="image/webp" />
  <img src="image.jpg" alt="Fallback" />
</picture>
```

## Implementation

### React Performance
```javascript
// React.memo for component memoization
const MemoizedComponent = React.memo(({ data }) => {
  return <div>{data.map(item => <Item key={item.id} {...item} />)}</div>;
});

// React.Profiler for measuring performance
<Profiler id="App" onRender={(id, phase, duration) => {
  console.log(`${id} ${phase}: ${duration}ms`);
}}>
  <App />
</Profiler>
```

### Vue Performance
```vue
<template>
  <!-- v-memo for memoization -->
  <div v-memo="[item.id]">{{ item.name }}</div>
</template>

<script>
export default {
  // Keep-alive for component caching
  components: { KeepAlive }
}
</script>
```

### Angular Performance
```typescript
// OnPush change detection
@Component({
  selector: 'app-performance',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PerformanceComponent {}

// Virtual scrolling
@Component({
  template: `<cdk-virtual-scroll-viewport itemSize="50">
    <div *cdkVirtualFor="let item of items">{{ item.name }}</div>
  </cdk-virtual-scroll-viewport>`
})
export class VirtualListComponent {}
```

## Best Practices

1. Measure before optimizing
2. Use React DevTools Profiler
3. Avoid premature optimization
4. Profile in production mode
5. Test on low-end devices

## Interview Questions

1. What is memoization and when should you use it?
2. How does virtualization improve performance?
3. What are the differences between debouncing and throttling?
4. How do you identify performance bottlenecks?
5. What is lazy loading and code splitting?

## References

- React Performance Documentation
- "Performance" by React Team
- Web Vitals Documentation
- "Patterns.dev" Performance Chapter
- Chrome DevTools Performance Panel

# JavaScript Performance

## Bundle Optimization

```javascript
// webpack.config.js
module.exports = {
    optimization: {
        splitChunks: {
            chunks: 'all',
            maxSize: 244000,
            cacheGroups: {
                vendor: {
                    test: /[\\/]node_modules[\\/]/,
                    name: 'vendors',
                    chunks: 'all'
                }
            }
        },
        minimizer: [
            new TerserPlugin({
                terserOptions: {
                    compress: {
                        drop_console: true
                    }
                }
            })
        ]
    }
};
```

## Code Splitting

```javascript
// Dynamic imports
const Dashboard = React.lazy(() => import('./Dashboard'));

// Route-based splitting
const About = React.lazy(() => import('./pages/About'));

// Conditional loading
if (needsFeature) {
    const feature = await import('./feature.js');
}
```

## Lazy Loading

```javascript
// Intersection Observer
const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.src = entry.target.dataset.src;
            observer.unobserve(entry.target);
        }
    });
});

document.querySelectorAll('img[data-src]').forEach(img => {
    observer.observe(img);
});

// Component lazy loading
const LazyComponent = React.lazy(() => import('./HeavyComponent'));
```

## Memoization

```javascript
// React.memo
const MemoizedComponent = React.memo(MyComponent, (prev, next) => {
    return prev.id === next.id;
});

// useMemo
const expensiveResult = useMemo(() => {
    return heavyComputation(data);
}, [data]);

// useCallback
const handleClick = useCallback(() => {
    doSomething(id);
}, [id]);

// General memoization
function memoize(fn) {
    const cache = new Map();
    return function(...args) {
        const key = JSON.stringify(args);
        if (cache.has(key)) {
            return cache.get(key);
        }
        const result = fn.apply(this, args);
        cache.set(key, result);
        return result;
    };
}
```

## Web Vitals

- **LCP**: Largest Contentful Paint (target < 2.5s)
- **FID**: First Input Delay (target < 100ms)
- **CLS**: Cumulative Layout Shift (target < 0.1)
- **TTFB**: Time to First Byte (target < 800ms)
- **INP**: Interaction to Next Paint (target < 200ms)

```javascript
import { onLCP, onFID, onCLS } from 'web-vitals';

onLCP(console.log);
onFID(console.log);
onCLS(console.log);
```

## Performance API

```javascript
// Measure execution time
performance.mark('start');
heavyOperation();
performance.mark('end');
performance.measure('operation', 'start', 'end');

// Navigation timing
const [navigation] = performance.getEntriesByType('navigation');
console.log(navigation.domContentLoadedEventEnd);

// Resource timing
const resources = performance.getEntriesByType('resource');
```

## Optimization Techniques

- Debounce event handlers
- Throttle scroll/resize events
- Use `requestAnimationFrame` for animations
- Avoid layout thrashing
- Use Web Workers for CPU-intensive tasks
- Implement virtual scrolling for large lists
- Use IndexedDB for client-side storage

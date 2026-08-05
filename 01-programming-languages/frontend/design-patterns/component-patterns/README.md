# Component Patterns

## Overview

Component patterns are reusable solutions for structuring UI components to achieve code reuse, separation of concerns, and maintainability. These patterns address common challenges like sharing behavior between components, managing component logic, and creating flexible component APIs.

## When to Use

- Building reusable component libraries
- Sharing logic across multiple components
- Creating flexible and composable APIs
- Reducing code duplication
- Improving component testability

## Main Patterns

### Container/Presentational
Separates data fetching (Container) from rendering (Presentational).

### Higher-Order Components (HOC)
Functions that wrap components to add shared behavior.

### Render Props
Components that use a function prop to render content.

### Custom Hooks
Functions that extract and reuse stateful logic.

### Compound Components
Components that work together with shared implicit state.

## Implementation

### React Examples
```javascript
// HOC Pattern
function withLoading(Component) {
  return function WithLoading({ isLoading, ...props }) {
    if (isLoading) return <div>Loading...</div>;
    return <Component {...props} />;
  };
}

// Render Props Pattern
class DataFetcher extends React.Component {
  state = { data: null };
  componentDidMount() {
    fetch(this.props.url)
      .then(res => res.json())
      .then(data => this.setState({ data }));
  }
  render() {
    return this.props.children(this.state.data);
  }
}

// Custom Hook Pattern
function useFetch(url) {
  const [data, setData] = useState(null);
  useEffect(() => {
    fetch(url).then(res => res.json()).then(setData);
  }, [url]);
  return data;
}
```

### Vue Examples
```javascript
// Composable (Custom Hook equivalent)
function useFetch(url) {
  const data = ref(null);
  onMounted(async () => {
    const res = await fetch(url);
    data.value = await res.json();
  });
  return { data };
}

// Mixin Pattern
const loadingMixin = {
  data() { return { loading: false } },
  methods: {
    async loadData(fn) {
      this.loading = true;
      await fn();
      this.loading = false;
    }
  }
}
```

### Angular Examples
```typescript
// Directive Pattern
@Directive({ selector: '[appLoading]' })
export class LoadingDirective {
  @Input() set appLoading(condition: boolean) {
    // Toggle loading state
  }
}

// Service Pattern
@Injectable()
export class DataService {
  fetchData(url: string): Observable<any> {
    return this.http.get(url);
  }
}
```

## Best Practices

1. Start with simple solutions first
2. Prefer composition over inheritance
3. Keep patterns focused on single responsibilities
4. Document component APIs clearly
5. Test patterns in isolation

## Interview Questions

1. Name three common component patterns
2. When would you use a HOC vs Render Props?
3. How do custom hooks improve code reuse?
4. What are compound components and when to use them?
5. How do component patterns differ across frameworks?

## References

- "Advanced React Patterns" by Kent C. Dodds
- "Patterns.dev" by Lydia Hallie
- React Documentation - Custom Hooks
- Vue.js Composables Guide

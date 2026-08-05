# Custom Hooks Patterns

## Overview

Custom Hooks are functions that allow you to extract component logic into reusable functions. They follow the "use" naming convention and can call other hooks. Custom Hooks enable logic sharing between components without changing the component hierarchy, replacing many use cases for HOCs and render props.

## When to Use

- Reusing stateful logic between components
- Abstracting complex operations (API calls, subscriptions)
- Creating composable behavior units
- Reducing component complexity
- Sharing logic across unrelated components

## Implementation

### React
```javascript
// useFetch - Data fetching hook
function useFetch(url) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    fetch(url)
      .then(res => res.json())
      .then(setData)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [url]);

  return { data, loading, error };
}

// useLocalStorage - Persistent state hook
function useLocalStorage(key, initialValue) {
  const [value, setValue] = useState(() => {
    const stored = localStorage.getItem(key);
    return stored ? JSON.parse(stored) : initialValue;
  });

  useEffect(() => {
    localStorage.setItem(key, JSON.stringify(value));
  }, [key, value]);

  return [value, setValue];
}

// useDebounce - Debounced value hook
function useDebounce(value, delay) {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedValue(value), delay);
    return () => clearTimeout(timer);
  }, [value, delay]);

  return debouncedValue;
}

// Usage
function UserSearch() {
  const [query, setQuery] = useState('');
  const debouncedQuery = useDebounce(query, 300);
  const { data, loading } = useFetch(`/api/search?q=${debouncedQuery}`);

  return <div>{loading ? 'Loading...' : data}</div>;
}
```

### Vue (Composables)
```javascript
// useFetch composable
function useFetch(url) {
  const data = ref(null);
  const loading = ref(true);
  const error = ref(null);

  onMounted(async () => {
    try {
      const res = await fetch(url);
      data.value = await res.json();
    } catch (e) {
      error.value = e;
    } finally {
      loading.value = false;
    }
  });

  return { data, loading, error };
}

// useDebounce composable
function useDebounce(value, delay) {
  const debouncedValue = ref(value.value);
  let timeout;

  watch(value, (newVal) => {
    clearTimeout(timeout);
    timeout = setTimeout(() => { debouncedValue.value = newVal; }, delay);
  });

  return debouncedValue;
}

// Usage
export default {
  setup() {
    const query = ref('');
    const { data, loading } = useFetch(computed(() => `/api/search?q=${query.value}`));
    return { data, loading, query };
  }
}
```

### Angular (Service/Custom Pipe)
```typescript
// Service-based hook pattern
@Injectable({ providedIn: 'root' })
export class UseFetchService {
  fetch<T>(url: string): Observable<{ data: T; loading: boolean; error: any }> {
    return of(null).pipe(
      switchMap(() => this.http.get<T>(url)),
      map(data => ({ data, loading: false, error: null })),
      catchError(error => of({ data: null, loading: false, error })),
      startWith({ data: null, loading: true, error: null })
    );
  }
}
```

## Best Practices

1. Name hooks starting with "use"
2. Keep hooks focused on single responsibility
3. Return consistent interfaces (object or array)
4. Handle cleanup in useEffect return
5. Test hooks with React Testing Library

## Interview Questions

1. What are Custom Hooks and why use them?
2. How do Custom Hooks differ from utility functions?
3. What rules apply to Custom Hooks?
4. When would you use a Custom Hook vs Context?
5. How do Custom Hooks handle side effects?

## References

- React Documentation - Building Custom Hooks
- "Complete Guide to useEffect" by Dan Abramov
- Vue.js Composables Guide
- Awesome React Hooks Repository

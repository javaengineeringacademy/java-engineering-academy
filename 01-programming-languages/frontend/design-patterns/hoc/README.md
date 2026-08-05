# Higher-Order Components (HOC)

## Overview

A Higher-Order Component is a function that takes a component and returns a new enhanced component. HOCs are used to share logic between components without modifying the original component. They follow the decorator pattern and enable cross-cutting concerns like authentication, logging, and data fetching.

## When to Use

- Adding shared behavior to multiple components
- Implementing cross-cutting concerns (auth, logging)
- Enhancing components with additional props or state
- Creating reusable component wrappers
- Abstracting away complex logic from presentational components

## Implementation

### React
```javascript
// Basic HOC
function withAuth(Component) {
  return function AuthenticatedComponent(props) {
    const isAuthenticated = useAuth();
    if (!isAuthenticated) return <Redirect to="/login" />;
    return <Component {...props} />;
  };
}

// HOC with Additional Props
function withUserData(Component) {
  return function WithUserData(props) {
    const [user, setUser] = useState(null);
    useEffect(() => {
      fetch('/api/user').then(res => res.json()).then(setUser);
    }, []);

    return <Component {...props} user={user} />;
  };
}

// Usage
const ProtectedDashboard = withAuth(Dashboard);
const DashboardWithUser = withUserData(Dashboard);
```

### Vue (Mixin Alternative)
```javascript
// Vue 3 - Composable (Modern HOC equivalent)
function useUserData() {
  const user = ref(null);
  onMounted(async () => {
    const res = await fetch('/api/user');
    user.value = await res.json();
  });
  return { user };
}

// Component Usage
export default {
  setup() {
    const { user } = useUserData();
    return { user };
  }
}
```

### Angular (Directive/Service)
```typescript
// Directive approach
@Directive({ selector: '[appWithAuth]' })
export class WithAuthDirective implements OnInit {
  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private authService: AuthService
  ) {}

  ngOnInit() {
    if (this.authService.isAuthenticated) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    }
  }
}

// Usage: <div *appWithAuth>Protected content</div>
```

## Best Practices

1. Don't mutate the original component
2. Pass unrelated props through to the wrapped component
3. Set a display name for debugging
4. Use composition instead of HOCs when possible
5. Document the props added by the HOC

## Interview Questions

1. What is a Higher-Order Component?
2. How do HOCs differ from regular component composition?
3. What are common use cases for HOCs?
4. What problems can HOCs introduce?
5. How have React Hooks changed the use of HOCs?

## References

- React Documentation - Higher-Order Components
- "Higher-Order Components" by Adam Robinson
- "Composing Higher-Order Components" by Kent C. Dodds
- React Higher-Order Components Library

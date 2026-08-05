# Provider Pattern (Context)

## Overview

The Provider Pattern uses React Context (or similar mechanisms) to make data available to a tree of components without explicit prop drilling. A Provider component wraps the component tree and provides data, while Consumer components access that data through the context. This is essential for global state, theme management, and dependency injection.

## When to Use

- Sharing global state (auth, theme, locale)
- Avoiding prop drilling through many levels
- Providing configuration to deep components
- Implementing dependency injection
- Managing application-wide settings

## Implementation

### React
```javascript
import { createContext, useContext, useState } from 'react';

// Create Context
const ThemeContext = createContext();

// Provider Component
function ThemeProvider({ children }) {
  const [theme, setTheme] = useState('light');

  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light');
  };

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

// Consumer Component
function ThemedButton() {
  const { theme, toggleTheme } = useContext(ThemeContext);
  return (
    <button className={`btn-${theme}`} onClick={toggleTheme}>
      Toggle Theme
    </button>
  );
}

// App
function App() {
  return (
    <ThemeProvider>
      <ThemedButton />
    </ThemeProvider>
  );
}
```

### Vue (Provide/Inject)
```javascript
// Provider Component
import { provide, ref } from 'vue';

export default {
  setup() {
    const theme = ref('light');
    const toggleTheme = () => {
      theme.value = theme.value === 'light' ? 'dark' : 'light';
    };

    provide('theme', { theme, toggleTheme });
  }
}

// Consumer Component
import { inject } from 'vue';

export default {
  setup() {
    const { theme, toggleTheme } = inject('theme');
    return { theme, toggleTheme };
  }
}
```

### Angular
```typescript
// Service
@Injectable({ providedIn: 'root' })
export class ThemeService {
  private theme = signal('light');
  theme$ = this.theme.asObservable();

  toggleTheme() {
    this.theme.update(t => t === 'light' ? 'dark' : 'light');
  }
}

// Provider
@Component({
  selector: 'app-root',
  template: `<router-outlet></router-outlet>`,
  providers: [ThemeService]
})
export class AppComponent {}

// Consumer
@Component({ template: `<button (click)="toggle()">Toggle</button>` })
export class ThemedButtonComponent {
  constructor(private themeService: ThemeService) {}
  toggle() { this.themeService.toggleTheme(); }
}
```

## Best Practices

1. Keep provider state minimal and focused
2. Use multiple providers for different concerns
3. Provide default values for contexts
4. Avoid re-renders by memoizing context values
5. Document context shape and usage

## Interview Questions

1. What problem does the Provider Pattern solve?
2. How does React Context differ from prop drilling?
3. What are performance implications of Context?
4. When should you not use Context?
5. How do you test components using Context?

## References

- React Context Documentation
- "A Complete Guide to useContext" by Kent C. Dodds
- Vue.js Provide/Inject Guide
- Angular Dependency Injection Guide

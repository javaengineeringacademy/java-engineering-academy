# Frontend Architecture

Frontend architecture encompasses the patterns, principles, and practices used to structure scalable, maintainable, and performant frontend applications.

## Table of Contents

- [Feature-Based Structure](#feature-based-structure)
- [Atomic Design](#atomic-design)
- [Component Patterns](#component-patterns)
- [Micro-Frontends](#micro-frontends)
- [Module Federation](#module-federation)
- [State Management Architecture](#state-management-architecture)
- [Error Boundaries](#error-boundaries)
- [Directory Structure](#directory-structure)
- [Coding Conventions](#coding-conventions)

---

## Feature-Based Structure

Organize code by features rather than technical concerns:

```
src/
  features/
    auth/
      components/
        LoginForm.tsx
        RegisterForm.tsx
      hooks/
        useAuth.ts
      services/
        authService.ts
      store/
        authSlice.ts
      types/
        auth.types.ts
      index.ts
    dashboard/
      components/
      hooks/
      services/
      index.ts
  shared/
    components/
      Button/
      Modal/
      Layout/
    hooks/
      useLocalStorage.ts
    utils/
      formatDate.ts
    types/
      common.types.ts
  app/
    providers/
      AuthProvider.tsx
    routes/
      index.tsx
    store.ts
  index.tsx
```

### Feature Module Pattern

```typescript
// features/auth/index.ts
export { LoginForm } from "./components/LoginForm";
export { RegisterForm } from "./components/RegisterForm";
export { useAuth } from "./hooks/useAuth";
export { authService } from "./services/authService";
export type { User, AuthState } from "./types/auth.types";

// Importing from features
import { LoginForm, useAuth } from "@/features/auth";

// Feature boundary enforcement
// eslint-plugin-boundaries configuration
module.exports = {
  settings: {
    "boundaries/element-types": [
      {
        type: "feature",
        pattern: "features/*",
        mode: "folder",
        allow: ["shared", "app"],
      },
      {
        type: "shared",
        pattern: "shared",
        allow: ["shared"],
      },
    ],
  },
};
```

---

## Atomic Design

Brad Frost's methodology for designing design systems:

```
src/
  components/
    atoms/
      Button/
      Input/
      Icon/
      Typography/
    molecules/
      SearchBar/
      FormField/
      Card/
    organisms/
      Header/
      Footer/
      Sidebar/
    templates/
      AuthLayout/
      DashboardLayout/
    pages/
      HomePage/
      LoginPage/
```

### Implementation

```typescript
// atoms/Button/Button.tsx
interface ButtonProps {
  variant?: "primary" | "secondary" | "ghost";
  size?: "sm" | "md" | "lg";
  children: React.ReactNode;
  onClick?: () => void;
}

export function Button({ variant = "primary", size = "md", children, onClick }: ButtonProps) {
  return (
    <button className={`btn btn-${variant} btn-${size}`} onClick={onClick}>
      {children}
    </button>
  );
}

// molecules/FormField/FormField.tsx
interface FormFieldProps {
  label: string;
  error?: string;
  children: React.ReactNode;
}

export function FormField({ label, error, children }: FormFieldProps) {
  return (
    <div className="form-field">
      <label>{label}</label>
      {children}
      {error && <span className="error">{error}</span>}
    </div>
  );
}

// organisms/Header/Header.tsx
import { Button } from "@/components/atoms/Button";
import { SearchBar } from "@/components/molecules/SearchBar";

export function Header() {
  return (
    <header className="header">
      <Logo />
      <Navigation />
      <SearchBar />
      <Button variant="primary">Sign In</Button>
    </header>
  );
}
```

---

## Component Patterns

### Compound Components

Share implicit state between related components:

```typescript
import { createContext, useContext, useState, ReactNode } from "react";

interface TabsContextType {
  activeTab: string;
  setActiveTab: (id: string) => void;
}

const TabsContext = createContext<TabsContextType | undefined>(undefined);

function useTabsContext() {
  const context = useContext(TabsContext);
  if (!context) {
    throw new Error("Tabs compound components must be used within Tabs");
  }
  return context;
}

function Tabs({ children, defaultTab }: { children: ReactNode; defaultTab: string }) {
  const [activeTab, setActiveTab] = useState(defaultTab);

  return (
    <TabsContext.Provider value={{ activeTab, setActiveTab }}>
      <div className="tabs">{children}</div>
    </TabsContext.Provider>
  );
}

function TabList({ children }: { children: ReactNode }) {
  return <div className="tab-list" role="tablist">{children}</div>;
}

function Tab({ id, children }: { id: string; children: ReactNode }) {
  const { activeTab, setActiveTab } = useTabsContext();

  return (
    <button
      role="tab"
      aria-selected={activeTab === id}
      onClick={() => setActiveTab(id)}
      className={`tab ${activeTab === id ? "active" : ""}`}
    >
      {children}
    </button>
  );
}

function TabPanel({ id, children }: { id: string; children: ReactNode }) {
  const { activeTab } = useTabsContext();
  if (activeTab !== id) return null;

  return (
    <div role="tabpanel" className="tab-panel">
      {children}
    </div>
  );
}
```

### Render Props

Share component logic through render functions:

```typescript
interface MouseTrackerProps {
  render: (position: { x: number; y: number }) => React.ReactNode;
}

function MouseTracker({ render }: MouseTrackerProps) {
  const [position, setPosition] = useState({ x: 0, y: 0 });

  const handleMouseMove = (e: React.MouseEvent) => {
    setPosition({ x: e.clientX, y: e.clientY });
  };

  return (
    <div onMouseMove={handleMouseMove}>
      {render(position)}
    </div>
  );
}

// Usage
<MouseTracker
  render={({ x, y }) => (
    <div>Mouse position: {x}, {y}</div>
  )}
/>
```

### Higher-Order Components (HOC)

Enhance components with additional functionality:

```typescript
interface WithAuthProps {
  isAuthenticated: boolean;
  user: User | null;
}

function withAuth<P extends WithAuthProps>(
  WrappedComponent: React.ComponentType<P>
) {
  return function WithAuthComponent(props: Omit<P, keyof WithAuthProps>) {
    const { isAuthenticated, user } = useAuth();

    if (!isAuthenticated) {
      return <Redirect to="/login" />;
    }

    return <WrappedComponent {...(props as P)} isAuthenticated={isAuthenticated} user={user} />;
  };
}

// HOC for logging
function withLogging<P extends object>(
  WrappedComponent: React.ComponentType<P>,
  componentName: string
) {
  return function WithLoggingComponent(props: P) {
    useEffect(() => {
      console.log(`${componentName} mounted`, props);
      return () => console.log(`${componentName} unmounted`);
    }, []);

    return <WrappedComponent {...props} />;
  };
}
```

---

## Micro-Frontends

Architecture for scaling frontend applications:

```typescript
// Module Federation configuration (Webpack 5)
// host/webpack.config.js
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");

module.exports = {
  plugins: [
    new ModuleFederationPlugin({
      name: "host",
      remotes: {
        remoteApp: "remoteApp@http://localhost:3001/remoteEntry.js",
      },
      shared: {
        react: { singleton: true },
        "react-dom": { singleton: true },
      },
    }),
  ],
};

// remote/webpack.config.js
module.exports = {
  plugins: [
    new ModuleFederationPlugin({
      name: "remoteApp",
      filename: "remoteEntry.js",
      exposes: {
        "./Button": "./src/components/Button",
        "./Header": "./src/components/Header",
      },
      shared: {
        react: { singleton: true },
        "react-dom": { singleton: true },
      },
    }),
  ],
};
```

---

## Module Federation

Runtime loading of modules from other applications:

```typescript
// Dynamic remote loading
const loadRemote = async (scope: string, module: string) => {
  await __webpack_init_sharing__("default");
  const container = window[scope];
  await container.init(__webpack_share_scopes__.default);
  const factory = await container.get(module);
  return factory();
};

// React lazy with Module Federation
const RemoteButton = React.lazy(() =>
  loadRemote("remoteApp", "./Button").then((m) => ({ default: m.Button }))
);

function App() {
  return (
    <Suspense fallback="Loading remote component...">
      <RemoteButton />
    </Suspense>
  );
}
```

---

## State Management Architecture

Choose the right state management approach:

```typescript
// State management decision tree
// 1. Local component state -> useState/useReducer
// 2. Shared state in tree -> Context API
// 3. Complex client state -> Redux/Zustand
// 4. Server state -> React Query/SWR
// 5. URL state -> URL params/search params

// Example: Layered state architecture
// - Form state: React Hook Form
// - UI state: Zustand
// - Server state: React Query
// - URL state: Next.js router

// Zustand store for UI state
import { create } from "zustand";

interface UIState {
  sidebarOpen: boolean;
  theme: "light" | "dark";
  toggleSidebar: () => void;
  setTheme: (theme: "light" | "dark") => void;
}

const useUIStore = create<UIState>((set) => ({
  sidebarOpen: false,
  theme: "light",
  toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),
  setTheme: (theme) => set({ theme }),
}));
```

---

## Error Boundaries

Catch and handle errors gracefully:

```typescript
import { Component, ReactNode } from "react";

interface Props {
  children: ReactNode;
  fallback?: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
}

class ErrorBoundary extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error("Error caught by boundary:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        this.props.fallback || (
          <div className="error-boundary">
            <h2>Something went wrong</h2>
            <p>{this.state.error?.message}</p>
            <button onClick={() => this.setState({ hasError: false })}>
              Try again
            </button>
          </div>
        )
      );
    }

    return this.props.children;
  }
}

// Usage
<ErrorBoundary fallback={<ErrorPage />}>
  <App />
</ErrorBoundary>
```

---

## Coding Conventions

```markdown
### Naming Conventions
- Components: PascalCase (UserCard.tsx)
- Hooks: camelCase with 'use' prefix (useAuth.ts)
- Utils: camelCase (formatDate.ts)
- Types: PascalCase with .types.ts suffix (user.types.ts)
- Constants: UPPER_SNAKE_CASE (API_ENDPOINTS.ts)
- CSS Modules: camelCase (UserCard.module.css)

### File Organization
- One component per file
- Co-locate related files (test, styles, types)
- Use index.ts for public API exports
- Keep files under 200 lines

### Component Guidelines
- Props interface above component
- Destructure props in function signature
- Early returns for error/loading states
- Extract complex logic to custom hooks
- Keep components focused and small
```

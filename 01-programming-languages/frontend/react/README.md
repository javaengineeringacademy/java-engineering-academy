# React Complete Guide

## Table of Contents

1. [Core Concepts](#core-concepts)
2. [Components](#components)
3. [Props and State](#props-and-state)
4. [Hooks](#hooks)
5. [Context API](#context-api)
6. [Event Handling](#event-handling)
7. [Conditional Rendering](#conditional-rendering)
8. [Lists and Keys](#lists-and-keys)
9. [Forms](#forms)
10. [React Router](#react-router)
11. [Performance Optimization](#performance-optimization)
12. [Error Handling](#error-handling)

---

## Core Concepts

### JSX Syntax

```jsx
// JSX expressions
const element = <h1>Hello, {user.name}!</h1>;
const element = <h1>{isLoggedIn ? 'Welcome' : 'Please login'}</h1>;

// Self-closing tags required
const element = <img src="photo.jpg" alt="Photo" />;
const element = <input type="text" />;

// Fragments
const element = (
    <>
        <h1>Title</h1>
        <p>Content</p>
    </>
);

// Nested fragments with key
const element = (
    <Fragment key="section">
        <h1>Title</h1>
        <p>Content</p>
    </Fragment>
);

// Style objects (camelCase, values as strings)
const style = {
    backgroundColor: '#333',
    fontSize: '16px',
    padding: '10px 20px'
};

const element = <div style={style}>Styled content</div>;

// className instead of class
const element = <div className="container active">Content</div>;

// dangerouslySetInnerHTML
const element = <div dangerouslySetInnerHTML={{ __html: rawHTML }} />;
```

### Project Structure

```
src/
├── components/
│   ├── common/
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   └── Modal.tsx
│   ├── features/
│   │   ├── auth/
│   │   │   ├── LoginForm.tsx
│   │   │   └── RegisterForm.tsx
│   │   └── dashboard/
│   │       ├── Dashboard.tsx
│   │       └── Stats.tsx
│   └── layout/
│       ├── Header.tsx
│       ├── Sidebar.tsx
│       └── Footer.tsx
├── hooks/
│   ├── useAuth.ts
│   ├── useDebounce.ts
│   └── useLocalStorage.ts
├── context/
│   ├── AuthContext.tsx
│   └── ThemeContext.tsx
├── services/
│   ├── api.ts
│   └── auth.ts
├── types/
│   └── index.ts
├── utils/
│   └── helpers.ts
├── App.tsx
└── main.tsx
```

---

## Components

### Functional Components

```jsx
// Basic component
function Greeting({ name }) {
    return <h1>Hello, {name}!</h1>;
}

// Arrow function
const Greeting = ({ name }) => {
    return <h1>Hello, {name}!</h1>;
};

// With default props
function Greeting({ name = 'World' }) {
    return <h1>Hello, {name}!</h1>;
}

// With children
function Card({ title, children }) {
    return (
        <div className="card">
            <h2>{title}</h2>
            <div className="card-content">
                {children}
            </div>
        </div>
    );
}

// Usage
<Card title="My Title">
    <p>This is card content</p>
    <button>Click me</button>
</Card>
```

### Component Composition

```jsx
// Props spreading
function Button({ variant, ...props }) {
    return (
        <button className={`btn btn-${variant}`} {...props}>
            {props.children}
        </button>
    );
}

// Render props
function DataFetcher({ url, children }) {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(url)
            .then(res => res.json())
            .then(data => {
                setData(data);
                setLoading(false);
            });
    }, [url]);

    return children({ data, loading });
}

// Usage
<DataFetcher url="/api/users">
    {({ data, loading }) => (
        loading ? <Spinner /> : <UserList users={data} />
    )}
</DataFetcher>

// Compound components
function Tabs({ children }) {
    const [activeTab, setActiveTab] = useState(0);

    return (
        <TabsContext.Provider value={{ activeTab, setActiveTab }}>
            <div className="tabs">{children}</div>
        </TabsContext.Provider>
    );
}

Tabs.Panel = function TabPanel({ label, children }) {
    const { activeTab, setActiveTab } = useContext(TabsContext);
    const index = Tabs.Panel.index++;

    return (
        <>
            <button
                className={activeTab === index ? 'active' : ''}
                onClick={() => setActiveTab(index)}
            >
                {label}
            </button>
            {activeTab === index && children}
        </>
    );
};
```

---

## Props and State

```jsx
import { useState } from 'react';

function Counter() {
    const [count, setCount] = useState(0);

    const increment = () => {
        setCount(prev => prev + 1);  // Use functional update
    };

    const decrement = () => {
        setCount(prev => prev - 1);
    };

    return (
        <div>
            <p>Count: {count}</p>
            <button onClick={increment}>+</button>
            <button onClick={decrement}>-</button>
        </div>
    );
}

// Complex state
function Form() {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        preferences: {
            newsletter: false,
            theme: 'light'
        }
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;

        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleNestedChange = (e) => {
        const { name, checked } = e.target;

        setFormData(prev => ({
            ...prev,
            preferences: {
                ...prev.preferences,
                [name]: checked
            }
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(formData);
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                name="name"
                value={formData.name}
                onChange={handleChange}
            />
            <input
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
            />
            <label>
                <input
                    type="checkbox"
                    name="newsletter"
                    checked={formData.preferences.newsletter}
                    onChange={handleNestedChange}
                />
                Subscribe to newsletter
            </label>
            <button type="submit">Submit</button>
        </form>
    );
}
```

---

## Hooks

### useState

```jsx
import { useState } from 'react';

// Basic state
const [value, setValue] = useState(initialValue);

// Lazy initialization
const [state, setState] = useState(() => {
    return JSON.parse(localStorage.getItem('data'));
});

// Object state with spread
const [user, setUser] = useState({ name: '', email: '' });

setUser(prev => ({ ...prev, name: 'John' }));

// Array state
const [items, setItems] = useState([]);

// Add item
setItems(prev => [...prev, newItem]);

// Remove item
setItems(prev => prev.filter(item => item.id !== id));

// Update item
setItems(prev => prev.map(item =>
    item.id === id ? { ...item, ...updates } : item
));
```

### useEffect

```jsx
import { useEffect } from 'react';

// Run after every render
useEffect(() => {
    console.log('Component rendered');
});

// Run only once on mount
useEffect(() => {
    console.log('Component mounted');
    return () => console.log('Component unmounted');
}, []);

// Run when dependency changes
useEffect(() => {
    console.log('Count changed:', count);
}, [count]);

// Cleanup function
useEffect(() => {
    const interval = setInterval(() => {
        console.log('tick');
    }, 1000);

    return () => clearInterval(interval);
}, []);

// Data fetching
useEffect(() => {
    let cancelled = false;

    async function fetchData() {
        try {
            const response = await fetch('/api/data');
            const data = await response.json();
            if (!cancelled) {
                setData(data);
            }
        } catch (error) {
            if (!cancelled) {
                setError(error);
            }
        }
    }

    fetchData();

    return () => { cancelled = true; };
}, []);
```

### useRef

```jsx
import { useRef, useEffect } from 'react';

function TextInput() {
    const inputRef = useRef(null);

    useEffect(() => {
        inputRef.current.focus();
    }, []);

    return <input ref={inputRef} type="text" />;
}

// Store previous value
function usePrevious(value) {
    const ref = useRef();

    useEffect(() => {
        ref.current = value;
    }, [value]);

    return ref.current;
}

// DOM measurement
function MeasureExample() {
    const [dimensions, setDimensions] = useState({ width: 0, height: 0 });
    const textRef = useRef(null);

    useEffect(() => {
        if (textRef.current) {
            setDimensions({
                width: textRef.current.offsetWidth,
                height: textRef.current.offsetHeight
            });
        }
    }, []);

    return (
        <div>
            <p ref={textRef}>Measure me</p>
            <p>Width: {dimensions.width}px</p>
            <p>Height: {dimensions.height}px</p>
        </div>
    );
}
```

### useMemo and useCallback

```jsx
import { useMemo, useCallback } from 'react';

function ExpensiveComponent({ items, filter }) {
    // Memoize expensive computation
    const filteredItems = useMemo(() => {
        return items.filter(item =>
            item.category === filter
        ).sort((a, b) => a.name.localeCompare(b.name));
    }, [items, filter]);

    // Memoize callback
    const handleClick = useCallback((id) => {
        console.log('Clicked:', id);
    }, []);

    return (
        <ul>
            {filteredItems.map(item => (
                <li key={item.id} onClick={() => handleClick(item.id)}>
                    {item.name}
                </li>
            ))}
        </ul>
    );
}

// Memoize component
const MemoizedChild = React.memo(function Child({ data, onClick }) {
    console.log('Child rendered');
    return <div onClick={onClick}>{data}</div>;
});

// Parent
function Parent() {
    const [count, setCount] = useState(0);

    const handleClick = useCallback(() => {
        console.log('clicked');
    }, []);

    return (
        <div>
            <MemoizedChild data="hello" onClick={handleClick} />
            <button onClick={() => setCount(c => c + 1)}>
                Count: {count}
            </button>
        </div>
    );
}
```

### useReducer

```jsx
import { useReducer } from 'react';

const initialState = { count: 0 };

function reducer(state, action) {
    switch (action.type) {
        case 'increment':
            return { count: state.count + 1 };
        case 'decrement':
            return { count: state.count - 1 };
        case 'reset':
            return initialState;
        default:
            throw new Error(`Unknown action: ${action.type}`);
    }
}

function Counter() {
    const [state, dispatch] = useReducer(reducer, initialState);

    return (
        <div>
            <p>Count: {state.count}</p>
            <button onClick={() => dispatch({ type: 'increment' })}>+</button>
            <button onClick={() => dispatch({ type: 'decrement' })}>-</button>
            <button onClick={() => dispatch({ type: 'reset' })}>Reset</button>
        </div>
    );
}

// With lazy initialization
function init(initialCount) {
    return { count: initialCount };
}

function Counter({ initialCount }) {
    const [state, dispatch] = useReducer(reducer, initialCount, init);
    // ...
}
```

### Custom Hooks

```jsx
// useLocalStorage
function useLocalStorage(key, initialValue) {
    const [storedValue, setStoredValue] = useState(() => {
        try {
            const item = window.localStorage.getItem(key);
            return item ? JSON.parse(item) : initialValue;
        } catch (error) {
            return initialValue;
        }
    });

    const setValue = (value) => {
        try {
            const valueToStore = value instanceof Function
                ? value(storedValue)
                : value;
            setStoredValue(valueToStore);
            window.localStorage.setItem(key, JSON.stringify(valueToStore));
        } catch (error) {
            console.error(error);
        }
    };

    return [storedValue, setValue];
}

// useDebounce
function useDebounce(value, delay) {
    const [debouncedValue, setDebouncedValue] = useState(value);

    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedValue(value);
        }, delay);

        return () => clearTimeout(handler);
    }, [value, delay]);

    return debouncedValue;
}

// useFetch
function useFetch(url) {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const abortController = new AbortController();

        async function fetchData() {
            try {
                setLoading(true);
                const response = await fetch(url, {
                    signal: abortController.signal
                });
                const json = await response.json();
                setData(json);
                setError(null);
            } catch (err) {
                if (err.name !== 'AbortError') {
                    setError(err.message);
                }
            } finally {
                setLoading(false);
            }
        }

        fetchData();

        return () => abortController.abort();
    }, [url]);

    return { data, loading, error };
}

// useToggle
function useToggle(initial = false) {
    const [value, setValue] = useState(initial);
    const toggle = useCallback(() => setValue(v => !v), []);
    return [value, toggle];
}
```

---

## Context API

```jsx
import { createContext, useContext, useState } from 'react';

// Create context
const ThemeContext = createContext(null);

// Provider component
function ThemeProvider({ children }) {
    const [theme, setTheme] = useState('light');

    const toggleTheme = () => {
        setTheme(prev => prev === 'light' ? 'dark' : 'light');
    };

    const value = { theme, toggleTheme };

    return (
        <ThemeContext.Provider value={value}>
            {children}
        </ThemeContext.Provider>
    );
}

// Custom hook
function useTheme() {
    const context = useContext(ThemeContext);
    if (context === undefined) {
        throw new Error('useTheme must be used within ThemeProvider');
    }
    return context;
}

// Usage
function App() {
    return (
        <ThemeProvider>
            <Header />
            <Main />
        </ThemeProvider>
    );
}

function Header() {
    const { theme, toggleTheme } = useTheme();

    return (
        <header className={theme}>
            <button onClick={toggleTheme}>Toggle Theme</button>
        </header>
    );
}

// Multi-context pattern
const AuthContext = createContext(null);
const NotificationContext = createContext(null);

function AppProviders({ children }) {
    return (
        <AuthProvider>
            <NotificationProvider>
                {children}
            </NotificationProvider>
        </AuthProvider>
    );
}
```

---

## Event Handling

```jsx
function EventDemo() {
    // Synthetic events
    const handleClick = (e) => {
        e.preventDefault();
        console.log('Target:', e.target);
        console.log('Current target:', e.currentTarget);
    };

    // Passing arguments
    const handleItemAction = (id, action) => {
        console.log(`${action} item ${id}`);
    };

    return (
        <div>
            <button onClick={handleClick}>Click me</button>

            {/* Arrow function for arguments */}
            <button onClick={() => handleItemAction(1, 'delete')}>
                Delete
            </button>

            {/* Form handling */}
            <form onSubmit={handleSubmit}>
                <input type="text" onChange={handleChange} />
                <button type="submit">Submit</button>
            </form>

            {/* Event delegation */}
            <ul onClick={(e) => {
                if (e.target.tagName === 'LI') {
                    console.log('Clicked:', e.target.textContent);
                }
            }}>
                <li>Item 1</li>
                <li>Item 2</li>
                <li>Item 3</li>
            </ul>

            {/* Keyboard events */}
            <input
                onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                        console.log('Enter pressed');
                    }
                    if (e.key === 'Escape') {
                        console.log('Escape pressed');
                    }
                }}
            />

            {/* Focus events */}
            <input
                onFocus={() => console.log('focused')}
                onBlur={() => console.log('blurred')}
            />
        </div>
    );
}
```

---

## Conditional Rendering

```jsx
function Dashboard({ user, items, isLoading, error }) {
    // If/else
    if (isLoading) return <Spinner />;
    if (error) return <ErrorMessage message={error} />;
    if (!user) return <Redirect to="/login" />;

    return (
        <div>
            {/* Ternary operator */}
            <h1>{user.role === 'admin' ? 'Admin Dashboard' : 'User Dashboard'}</h1>

            {/* Short circuit */}
            {user.isAdmin && <AdminPanel />}

            {/* Short circuit with fallback */}
            {user.avatar ? (
                <img src={user.avatar} alt={user.name} />
            ) : (
                <DefaultAvatar name={user.name} />
            )}

            {/* Logical AND */}
            {items.length > 0 && (
                <ul>
                    {items.map(item => (
                        <li key={item.id}>{item.name}</li>
                    ))}
                </ul>
            )}

            {/* Immediately invoked */}
            {(() => {
                if (items.length === 0) return <EmptyState />;
                if (items.length > 10) return <PaginatedList items={items} />;
                return <SimpleList items={items} />;
            })()}

            {/* Null operator */}
            {user?.address?.city ?? 'No city'}
        </div>
    );
}
```

---

## Lists and Keys

```jsx
function UserList({ users }) {
    return (
        <ul>
            {users.map(user => (
                <li key={user.id}>
                    <img src={user.avatar} alt={user.name} />
                    <span>{user.name}</span>
                </li>
            ))}
        </ul>
    );
}

// Grouped list
function GroupedList({ items }) {
    const grouped = items.reduce((acc, item) => {
        const key = item.category;
        if (!acc[key]) acc[key] = [];
        acc[key].push(item);
        return acc;
    }, {});

    return (
        <div>
            {Object.entries(grouped).map(([category, categoryItems]) => (
                <div key={category}>
                    <h3>{category}</h3>
                    <ul>
                        {categoryItems.map(item => (
                            <li key={item.id}>{item.name}</li>
                        ))}
                    </ul>
                </div>
            ))}
        </div>
    );
}

// Nested lists
function NestedList({ data }) {
    return (
        <ul>
            {data.map(parent => (
                <li key={parent.id}>
                    {parent.name}
                    {parent.children && (
                        <ul>
                            {parent.children.map(child => (
                                <li key={child.id}>{child.name}</li>
                            ))}
                        </ul>
                    )}
                </li>
            ))}
        </ul>
    );
}
```

---

## Forms

```jsx
import { useState, useCallback } from 'react';

function SignupForm() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: 'user',
        agreeTerms: false
    });

    const [errors, setErrors] = useState({});

    const validate = useCallback(() => {
        const newErrors = {};

        if (!formData.username) {
            newErrors.username = 'Username is required';
        } else if (formData.username.length < 3) {
            newErrors.username = 'Username must be at least 3 characters';
        }

        if (!formData.email) {
            newErrors.email = 'Email is required';
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = 'Email is invalid';
        }

        if (!formData.password) {
            newErrors.password = 'Password is required';
        } else if (formData.password.length < 8) {
            newErrors.password = 'Password must be at least 8 characters';
        }

        if (formData.password !== formData.confirmPassword) {
            newErrors.confirmPassword = 'Passwords do not match';
        }

        if (!formData.agreeTerms) {
            newErrors.agreeTerms = 'You must agree to the terms';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }, [formData]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
        // Clear error on change
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (validate()) {
            console.log('Form submitted:', formData);
        }
    };

    return (
        <form onSubmit={handleSubmit} noValidate>
            <div className="form-group">
                <label htmlFor="username">Username</label>
                <input
                    type="text"
                    id="username"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    className={errors.username ? 'error' : ''}
                />
                {errors.username && (
                    <span className="error-message">{errors.username}</span>
                )}
            </div>

            <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    className={errors.email ? 'error' : ''}
                />
                {errors.email && (
                    <span className="error-message">{errors.email}</span>
                )}
            </div>

            <div className="form-group">
                <label htmlFor="password">Password</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                />
            </div>

            <div className="form-group">
                <label htmlFor="confirmPassword">Confirm Password</label>
                <input
                    type="password"
                    id="confirmPassword"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                />
            </div>

            <div className="form-group">
                <label htmlFor="role">Role</label>
                <select
                    id="role"
                    name="role"
                    value={formData.role}
                    onChange={handleChange}
                >
                    <option value="user">User</option>
                    <option value="admin">Admin</option>
                </select>
            </div>

            <div className="form-group">
                <label>
                    <input
                        type="checkbox"
                        name="agreeTerms"
                        checked={formData.agreeTerms}
                        onChange={handleChange}
                    />
                    I agree to the terms and conditions
                </label>
            </div>

            <button type="submit">Sign Up</button>
        </form>
    );
}
```

---

## React Router

```jsx
import {
    BrowserRouter,
    Routes,
    Route,
    Link,
    NavLink,
    Navigate,
    useParams,
    useNavigate,
    useLocation,
    Outlet
} from 'react-router-dom';

// App Router
function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<Home />} />
                    <Route path="about" element={<About />} />
                    <Route path="users" element={<UsersLayout />}>
                        <Route index element={<UserList />} />
                        <Route path=":userId" element={<UserDetail />} />
                    </Route>
                    <Route path="login" element={<Login />} />
                    <Route path="dashboard" element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    } />
                    <Route path="*" element={<NotFound />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

// Layout with Outlet
function Layout() {
    return (
        <div>
            <nav>
                <NavLink to="/" className={({ isActive }) => isActive ? 'active' : ''}>
                    Home
                </NavLink>
                <NavLink to="/about">About</NavLink>
                <NavLink to="/users">Users</NavLink>
            </nav>
            <main>
                <Outlet />
            </main>
        </div>
    );
}

// Protected Route
function ProtectedRoute({ children }) {
    const { user } = useAuth();
    const location = useLocation();

    if (!user) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return children;
}

// Route params
function UserDetail() {
    const { userId } = useParams();
    const navigate = useNavigate();
    const location = useLocation();

    return (
        <div>
            <h2>User {userId}</h2>
            <button onClick={() => navigate(-1)}>Go Back</button>
            <button onClick={() => navigate('/users')}>
                Go to Users
            </button>
        </div>
    );
}

// Programmatic navigation
function Login() {
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogin = async () => {
        await login();
        const from = location.state?.from?.pathname || '/';
        navigate(from, { replace: true });
    };

    return <button onClick={handleLogin}>Login</button>;
}
```

---

## Performance Optimization

```jsx
import { memo, useMemo, useCallback, lazy, Suspense } from 'react';

// Code splitting with lazy loading
const HeavyComponent = lazy(() => import('./HeavyComponent'));

function App() {
    return (
        <Suspense fallback={<Loading />}>
            <HeavyComponent />
        </Suspense>
    );
}

// Memoize expensive computations
function ProductList({ products, filter }) {
    const filteredProducts = useMemo(() => {
        return products.filter(p =>
            p.category === filter
        ).sort((a, b) => a.price - b.price);
    }, [products, filter]);

    return (
        <ul>
            {filteredProducts.map(product => (
                <ProductItem key={product.id} product={product} />
            ))}
        </ul>
    );
}

// Memoize callbacks to prevent unnecessary re-renders
function TodoList({ todos, onToggle, onDelete }) {
    const handleToggle = useCallback((id) => {
        onToggle(id);
    }, [onToggle]);

    const handleDelete = useCallback((id) => {
        onDelete(id);
    }, [onDelete]);

    return (
        <ul>
            {todos.map(todo => (
                <TodoItem
                    key={todo.id}
                    todo={todo}
                    onToggle={handleToggle}
                    onDelete={handleDelete}
                />
            ))}
        </ul>
    );
}

const TodoItem = memo(function TodoItem({ todo, onToggle, onDelete }) {
    return (
        <li>
            <span
                style={{ textDecoration: todo.done ? 'line-through' : 'none' }}
                onClick={() => onToggle(todo.id)}
            >
                {todo.text}
            </span>
            <button onClick={() => onDelete(todo.id)}>Delete</button>
        </li>
    );
});

// Virtualized list for large datasets
import { FixedSizeList } from 'react-window';

function VirtualList({ items }) {
    return (
        <FixedSizeList
            height={600}
            itemCount={items.length}
            itemSize={50}
            width="100%"
        >
            {({ index, style }) => (
                <div style={style}>
                    {items[index].name}
                </div>
            )}
        </FixedSizeList>
    );
}
```

---

## Error Handling

```jsx
import { Component, ErrorBoundary } from 'react';

// Class-based Error Boundary
class ErrorBoundary extends Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false, error: null };
    }

    static getDerivedStateFromError(error) {
        return { hasError: true, error };
    }

    componentDidCatch(error, errorInfo) {
        console.error('Error caught:', error, errorInfo);
    }

    render() {
        if (this.state.hasError) {
            return (
                <div className="error-fallback">
                    <h2>Something went wrong</h2>
                    <p>{this.state.error?.message}</p>
                    <button onClick={() => this.setState({ hasError: false })}>
                        Try again
                    </button>
                </div>
            );
        }
        return this.props.children;
    }
}

// Usage
function App() {
    return (
        <ErrorBoundary>
            <Header />
            <ErrorBoundary>
                <Main />
            </ErrorBoundary>
            <Footer />
        </ErrorBoundary>
    );
}

// Async error handling with Suspense
function UserProfile({ userId }) {
    const user = use(fetchUser(userId));

    return <div>{user.name}</div>;
}

function App() {
    return (
        <Suspense fallback={<Loading />}>
            <ErrorBoundary>
                <UserProfile userId={1} />
            </ErrorBoundary>
        </Suspense>
    );
}
```

---

## React Best Practices

1. Keep components small and focused on a single responsibility
2. Extract reusable logic into custom hooks
3. Use TypeScript for better type safety and developer experience
4. Memoize expensive computations and callbacks
5. Use `React.memo` for pure components that receive stable props
6. Implement code splitting with `React.lazy` and `Suspense`
7. Handle loading, error, and empty states explicitly
8. Avoid inline function definitions in JSX for frequently rendered components
9. Use the Context API sparingly — prefer prop drilling for simple cases
10. Follow the rules of hooks (no conditions, no loops)

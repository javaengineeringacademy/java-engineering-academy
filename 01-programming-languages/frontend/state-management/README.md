# State Management

State management is the process of managing and maintaining the state of an application across components and time.

## Table of Contents

- [Local vs Global State](#local-vs-global-state)
- [Context API](#context-api)
- [Redux](#redux)
- [Zustand](#zustand)
- [Jotai](#jotai)
- [Recoil](#recoil)
- [MobX](#mobx)
- [Signals](#signals)
- [Server State](#server-state)
- [URL State](#url-state)
- [Form State](#form-state)

---

## Local vs Global State

### Local State

State that belongs to a single component:

```typescript
import { useState } from "react";

function Counter() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}
```

### Global State

State shared across multiple components:

```typescript
// Using Context
const ThemeContext = createContext("light");

function App() {
  return (
    <ThemeContext.Provider value="dark">
      <Header />
      <Main />
      <Footer />
    </ThemeContext.Provider>
  );
}

function Header() {
  const theme = useContext(ThemeContext);
  return <header className={theme}>Header</header>;
}
```

---

## Context API

Built-in React state sharing mechanism:

```typescript
import { createContext, useContext, useState, ReactNode } from "react";

// Define context type
interface AuthContextType {
  user: User | null;
  login: (credentials: Credentials) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
}

// Create context
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Provider component
function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  const login = async (credentials: Credentials) => {
    const response = await api.login(credentials);
    setUser(response.user);
  };

  const logout = () => {
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

// Custom hook for consuming context
function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}

// Usage
function LoginForm() {
  const { login, isAuthenticated } = useAuth();

  if (isAuthenticated) {
    return <Redirect to="/dashboard" />;
  }

  return (
    <form onSubmit={(e) => { e.preventDefault(); login(credentials); }}>
      {/* form fields */}
    </form>
  );
}
```

---

## Redux

Predictable state container for JavaScript apps:

```typescript
// store.ts
import { configureStore, createSlice, createAsyncThunk } from "@reduxjs/toolkit";

// Async thunk
export const fetchUsers = createAsyncThunk(
  "users/fetchUsers",
  async () => {
    const response = await fetch("/api/users");
    return response.json();
  }
);

// Slice
const usersSlice = createSlice({
  name: "users",
  initialState: {
    items: [],
    loading: false,
    error: null,
  },
  reducers: {
    addUser: (state, action) => {
      state.items.push(action.payload);
    },
    removeUser: (state, action) => {
      state.items = state.items.filter((user) => user.id !== action.payload);
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUsers.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });
  },
});

export const { addUser, removeUser } = usersSlice.actions;

// Store
export const store = configureStore({
  reducer: {
    users: usersSlice.reducer,
  },
});

// Types
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
```

---

## Zustand

Small, fast, and scalable state management:

```typescript
import { create } from "zustand";
import { devtools, persist } from "zustand/middleware";

interface BearState {
  bears: number;
  increase: () => void;
  decrease: () => void;
  reset: () => void;
}

// Basic store
const useBearStore = create<BearState>((set) => ({
  bears: 0,
  increase: () => set((state) => ({ bears: state.bears + 1 })),
  decrease: () => set((state) => ({ bears: state.bears - 1 })),
  reset: () => set({ bears: 0 }),
}));

// With middleware
interface AuthState {
  user: User | null;
  token: string | null;
  login: (user: User, token: string) => void;
  logout: () => void;
}

const useAuthStore = create<AuthState>()(
  devtools(
    persist(
      (set) => ({
        user: null,
        token: null,
        login: (user, token) => set({ user, token }),
        logout: () => set({ user: null, token: null }),
      }),
      { name: "auth-storage" }
    )
  )
);

// Selectors
const bears = useBearStore((state) => state.bears);
const increase = useBearStore((state) => state.increase);

// Usage
function BearCounter() {
  const bears = useBearStore((state) => state.bears);
  const increase = useBearStore((state) => state.increase);

  return (
    <div>
      <span>{bears} bears</span>
      <button onClick={increase}>Add Bear</button>
    </div>
  );
}
```

---

## Jotai

Primitive and flexible state management:

```typescript
import { atom, useAtom, atomWithStorage } from "jotai";

// Basic atoms
const countAtom = atom(0);
const doubleCountAtom = atom((get) => get(countAtom) * 2);

// Derived atoms
const derivedAtom = atom((get) => {
  const count = get(countAtom);
  return `Count is ${count}`;
});

// Writable derived atoms
const readOnlyAtom = atom((get) => get(countAtom) * 2);
const writeOnlyAtom = atom(null, (get, set, value) => {
  set(countAtom, value / 2);
});

// Async atoms
const userAtom = atom(async () => {
  const response = await fetch("/api/user");
  return response.json();
});

// Atom families (parameterized)
const todoAtomFamily = atomFamily((id: number) =>
  atom({ id, text: "", completed: false })
);

// Persistent atoms
const themeAtom = atomWithStorage("theme", "light");

// Usage
function Counter() {
  const [count, setCount] = useAtom(countAtom);
  const [doubleCount] = useAtom(doubleCountAtom);

  return (
    <div>
      <p>Count: {count}</p>
      <p>Double: {doubleCount}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}
```

---

## Recoil

State management library from Facebook:

```typescript
import { atom, selector, useRecoilState, useRecoilValue } from "recoil";

// Atoms
const todoListState = atom({
  key: "todoListState",
  default: [],
});

const todoListFilterState = atom({
  key: "todoListFilterState",
  default: "Show All",
});

// Selectors
const filteredTodoListState = selector({
  key: "filteredTodoListState",
  get: ({ get }) => {
    const filter = get(todoListFilterState);
    const list = get(todoListState);

    switch (filter) {
      case "Show Completed":
        return list.filter((item) => item.isComplete);
      case "Show Uncompleted":
        return list.filter((item) => !item.isComplete);
      default:
        return list;
    }
  },
});

const todoListStatsState = selector({
  key: "todoListStatsState",
  get: ({ get }) => {
    const todoList = get(todoListState);
    const totalNum = todoList.length;
    const totalCompletedNum = todoList.filter((item) => item.isComplete).length;
    const totalUncompletedNum = totalNum - totalCompletedNum;
    const percentCompleted = totalNum === 0 ? 0 : totalCompletedNum / totalNum;

    return {
      totalNum,
      totalCompletedNum,
      totalUncompletedNum,
      percentCompleted,
    };
  },
});

// Usage
function TodoList() {
  const todoList = useRecoilValue(filteredTodoListState);

  return (
    <ul>
      {todoList.map((todo) => (
        <TodoItem key={todo.id} item={todo} />
      ))}
    </ul>
  );
}

function TodoItem({ item }: { item: Todo }) {
  const [todoList, setTodoList] = useRecoilState(todoListState);
  const index = todoList.findIndex((listItem) => listItem === item);

  const toggleItemCompletion = () => {
    setTodoList((oldTodoList) => [
      ...oldTodoList.slice(0, index),
      { ...item, isComplete: !item.isComplete },
      ...oldTodoList.slice(index + 1),
    ]);
  };

  return (
    <li>
      <input type="checkbox" checked={item.isComplete} onChange={toggleItemCompletion} />
      {item.text}
    </li>
  );
}
```

---

## MobX

Simple, scalable state management:

```typescript
import { makeAutoObservable, runInAction } from "mobx";
import { observer } from "mobx-react-lite";

// Observable store
class TodoStore {
  todos = [];
  filter = "all";

  constructor() {
    makeAutoObservable(this);
  }

  addTodo(text) {
    this.todos.push({
      id: Date.now(),
      text,
      completed: false,
    });
  }

  toggleTodo(id) {
    const todo = this.todos.find((todo) => todo.id === id);
    if (todo) {
      todo.completed = !todo.completed;
    }
  }

  get filteredTodos() {
    switch (this.filter) {
      case "completed":
        return this.todos.filter((t) => t.completed);
      case "active":
        return this.todos.filter((t) => !t.completed);
      default:
        return this.todos;
    }
  }

  get stats() {
    const total = this.todos.length;
    const completed = this.todos.filter((t) => t.completed).length;
    return { total, completed, active: total - completed };
  }
}

const store = new TodoStore();

// Async actions
class UserStore {
  users = [];
  loading = false;

  constructor() {
    makeAutoObservable(this);
  }

  async fetchUsers() {
    this.loading = true;
    try {
      const response = await fetch("/api/users");
      const users = await response.json();
      runInAction(() => {
        this.users = users;
        this.loading = false;
      });
    } catch (error) {
      runInAction(() => {
        this.loading = false;
      });
    }
  }
}

// Usage with observer
const TodoList = observer(() => {
  return (
    <div>
      {store.filteredTodos.map((todo) => (
        <div key={todo.id} onClick={() => store.toggleTodo(todo.id)}>
          {todo.text}
        </div>
      ))}
    </div>
  );
});
```

---

## Signals

Fine-grained reactive state (framework-agnostic):

```typescript
// Preact Signals
import { signal, computed, effect } from "@preact/signals";

// Basic signal
const count = signal(0);

// Computed signal
const doubleCount = computed(() => count.value * 2);

// Effect
effect(() => {
  console.log(`Count changed: ${count.value}`);
});

// Usage in component
function Counter() {
  return (
    <div>
      <p>Count: {count.value}</p>
      <p>Double: {doubleCount.value}</p>
      <button onClick={() => count.value++}>Increment</button>
    </div>
  );
}

// SolidJS signals
import { createSignal, createMemo, createEffect } from "solid-js";

function Counter() {
  const [count, setCount] = createSignal(0);
  const doubleCount = createMemo(() => count() * 2);

  createEffect(() => {
    console.log(`Count is: ${count()}`);
  });

  return (
    <div>
      <p>Count: {count()}</p>
      <p>Double: {doubleCount()}</p>
      <button onClick={() => setCount(count() + 1)}>Increment</button>
    </div>
  );
}
```

---

## Server State

Managing data fetched from APIs:

### React Query (TanStack Query)

```typescript
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

// Fetch data
function useUsers() {
  return useQuery({
    queryKey: ["users"],
    queryFn: async () => {
      const response = await fetch("/api/users");
      return response.json();
    },
  });
}

// Fetch with parameters
function useUser(id: number) {
  return useQuery({
    queryKey: ["users", id],
    queryFn: async () => {
      const response = await fetch(`/api/users/${id}`);
      return response.json();
    },
    enabled: !!id,
  });
}

// Mutations
function useCreateUser() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (newUser) => {
      const response = await fetch("/api/users", {
        method: "POST",
        body: JSON.stringify(newUser),
      });
      return response.json();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["users"] });
    },
  });
}

// Usage
function UsersList() {
  const { data: users, isLoading, error } = useUsers();
  const createUser = useCreateUser();

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <ul>
      {users.map((user) => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}
```

### SWR

```typescript
import useSWR, { useSWRConfig } from "swr";

const fetcher = (url) => fetch(url).then((res) => res.json());

// Basic usage
function Profile() {
  const { data, error, isLoading } = useSWR("/api/user", fetcher);

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;

  return <div>Hello, {data.name}!</div>;
}

// With conditional fetching
function Dashboard() {
  const { data: user } = useSWR("/api/user");
  const { data: projects } = useSWR(
    user ? `/api/projects?userId=${user.id}` : null,
    fetcher
  );

  return <div>{projects?.map((p) => <div key={p.id}>{p.name}</div>)}</div>;
}

// Optimistic updates
function TodoApp() {
  const { mutate } = useSWRConfig();

  const addTodo = async (newTodo) => {
    mutate(
      "/api/todos",
      async (currentTodos) => [...currentTodos, newTodo],
      { revalidate: false }
    );

    await fetch("/api/todos", {
      method: "POST",
      body: JSON.stringify(newTodo),
    });

    mutate("/api/todos");
  };
}
```

---

## URL State

Managing state through URL parameters:

```typescript
import { useSearchParams, useRouter } from "next/navigation";

// Basic URL state
function SearchFilter() {
  const [searchParams, setSearchParams] = useSearchParams();

  const filter = searchParams.get("filter") || "all";
  const page = parseInt(searchParams.get("page") || "1");

  const setFilter = (newFilter) => {
    const params = new URLSearchParams(searchParams);
    params.set("filter", newFilter);
    setSearchParams(params);
  };

  return (
    <div>
      <select value={filter} onChange={(e) => setFilter(e.target.value)}>
        <option value="all">All</option>
        <option value="active">Active</option>
        <option value="completed">Completed</option>
      </select>
      <span>Page: {page}</span>
    </div>
  );
}

// Using nuqs for type-safe URL state
import { useQueryState, parseAsInteger, parseAsString } from "nuqs";

function Filters() {
  const [page, setPage] = useQueryState("page", parseAsInteger.withDefault(1));
  const [search, setSearch] = useQueryState("search", parseAsString.withDefault(""));

  return (
    <div>
      <input value={search} onChange={(e) => setSearch(e.target.value)} />
      <button onClick={() => setPage(page - 1)}>Previous</button>
      <button onClick={() => setPage(page + 1)}>Next</button>
    </div>
  );
}
```

---

## Form State

Managing form data and validation:

### React Hook Form

```typescript
import { useForm, FormProvider } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

const schema = z.object({
  name: z.string().min(1, "Name is required"),
  email: z.string().email("Invalid email"),
  age: z.number().min(18, "Must be 18 or older"),
});

type FormData = z.infer<typeof schema>;

function RegistrationForm() {
  const methods = useForm<FormData>({
    resolver: zodResolver(schema),
  });

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = methods;

  const onSubmit = async (data: FormData) => {
    await fetch("/api/register", {
      method: "POST",
      body: JSON.stringify(data),
    });
  };

  return (
    <FormProvider {...methods}>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          <label htmlFor="name">Name</label>
          <input {...register("name")} />
          {errors.name && <span>{errors.name.message}</span>}
        </div>

        <div>
          <label htmlFor="email">Email</label>
          <input {...register("email")} type="email" />
          {errors.email && <span>{errors.email.message}</span>}
        </div>

        <div>
          <label htmlFor="age">Age</label>
          <input {...register("age", { valueAsNumber: true })} type="number" />
          {errors.age && <span>{errors.age.message}</span>}
        </div>

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Submitting..." : "Register"}
        </button>
      </form>
    </FormProvider>
  );
}
```

### Formik

```typescript
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

const validationSchema = Yup.object({
  name: Yup.string().required("Name is required"),
  email: Yup.string().email("Invalid email").required("Email is required"),
  password: Yup.string().min(8, "Password must be at least 8 characters"),
});

function RegistrationForm() {
  return (
    <Formik
      initialValues={{ name: "", email: "", password: "" }}
      validationSchema={validationSchema}
      onSubmit={async (values, { setSubmitting }) => {
        await fetch("/api/register", {
          method: "POST",
          body: JSON.stringify(values),
        });
        setSubmitting(false);
      }}
    >
      {({ isSubmitting }) => (
        <Form>
          <div>
            <Field name="name" placeholder="Name" />
            <ErrorMessage name="name" component="div" />
          </div>

          <div>
            <Field name="email" type="email" placeholder="Email" />
            <ErrorMessage name="email" component="div" />
          </div>

          <div>
            <Field name="password" type="password" placeholder="Password" />
            <ErrorMessage name="password" component="div" />
          </div>

          <button type="submit" disabled={isSubmitting}>
            Register
          </button>
        </Form>
      )}
    </Formik>
  );
}
```

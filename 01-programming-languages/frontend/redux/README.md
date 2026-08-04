# Redux

Redux is a predictable state container for JavaScript applications. It helps you write applications with consistent behavior across client, server, and production environments.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Actions](#actions)
- [Reducers](#reducers)
- [Store](#store)
- [Middleware](#middleware)
- [Redux Toolkit](#redux-toolkit)
- [RTK Query](#rtk-query)
- [Selector Patterns](#selector-patterns)
- [Normalized State](#normalized-state)
- [Redux DevTools](#redux-devtools)
- [Testing](#testing)

---

## Core Concepts

Redux follows three fundamental principles:

1. **Single source of truth** - The global state is stored in a single object tree
2. **State is read-only** - The only way to change state is by dispatching actions
3. **Changes are made with pure functions** - Reducers specify how state transitions occur

```
Action → Reducer → New State
  ↑                    |
  └────────────────────┘
```

---

## Actions

Actions are plain JavaScript objects describing what happened:

```typescript
// Action types (constants)
const ADD_TODO = "ADD_TODO";
const TOGGLE_TODO = "TOGGLE_TODO";
const REMOVE_TODO = "REMOVE_TODO";
const SET_FILTER = "SET_FILTER";

// Action creators
interface AddTodoAction {
  type: typeof ADD_TODO;
  payload: {
    id: number;
    text: string;
  };
}

interface ToggleTodoAction {
  type: typeof TOGGLE_TODO;
  payload: {
    id: number;
  };
}

interface RemoveTodoAction {
  type: typeof REMOVE_TODO;
  payload: {
    id: number;
  };
}

interface SetFilterAction {
  type: typeof SET_FILTER;
  payload: {
    filter: "all" | "active" | "completed";
  };
}

type TodoAction =
  | AddTodoAction
  | ToggleTodoAction
  | RemoveTodoAction
  | SetFilterAction;

// Action creators with types
const addTodo = (text: string): AddTodoAction => ({
  type: ADD_TODO,
  payload: {
    id: Date.now(),
    text,
  },
});

const toggleTodo = (id: number): ToggleTodoAction => ({
  type: TOGGLE_TODO,
  payload: { id },
});

const removeTodo = (id: number): RemoveTodoAction => ({
  type: REMOVE_TODO,
  payload: { id },
});

const setFilter = (filter: "all" | "active" | "completed"): SetFilterAction => ({
  type: SET_FILTER,
  payload: { filter },
});
```

---

## Reducers

Pure functions that specify how the state changes:

```typescript
interface Todo {
  id: number;
  text: string;
  completed: boolean;
}

interface TodoState {
  items: Todo[];
  filter: "all" | "active" | "completed";
}

const initialState: TodoState = {
  items: [],
  filter: "all",
};

// Reducer with switch statement
function todoReducer(state = initialState, action: TodoAction): TodoState {
  switch (action.type) {
    case ADD_TODO:
      return {
        ...state,
        items: [
          ...state.items,
          {
            id: action.payload.id,
            text: action.payload.text,
            completed: false,
          },
        ],
      };

    case TOGGLE_TODO:
      return {
        ...state,
        items: state.items.map((todo) =>
          todo.id === action.payload.id
            ? { ...todo, completed: !todo.completed }
            : todo
        ),
      };

    case REMOVE_TODO:
      return {
        ...state,
        items: state.items.filter((todo) => todo.id !== action.payload.id),
      };

    case SET_FILTER:
      return {
        ...state,
        filter: action.payload.filter,
      };

    default:
      return state;
  }
}

// Combine reducers
import { combineReducers } from "@reduxjs/toolkit";

const rootReducer = combineReducers({
  todos: todoReducer,
  user: userReducer,
  ui: uiReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
```

---

## Store

The single source of truth:

```typescript
import { createStore, applyMiddleware, compose } from "redux";
import { configureStore } from "@reduxjs/toolkit";
import thunk from "redux-thunk";

// With Redux Toolkit (recommended)
const store = configureStore({
  reducer: {
    todos: todoReducer,
    user: userReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
      immutableCheck: false,
    }),
  devTools: process.env.NODE_ENV !== "production",
  preloadedState: {},
  enhancers: [],
});

// Store actions
const state = store.getState(); // Get current state
store.dispatch(addTodo("Learn Redux")); // Dispatch action
const unsubscribe = store.subscribe(() => {
  console.log("State changed:", store.getState());
}); // Subscribe to changes

// Store types
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

// Typed hooks
import { useDispatch, useSelector, TypedUseSelectorHook } from "react-redux";

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
```

---

## Middleware

Functions that intercept actions before they reach reducers:

### Thunk Middleware

Handle asynchronous operations:

```typescript
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

// Async thunk
export const fetchUsers = createAsyncThunk(
  "users/fetchUsers",
  async (_, { rejectWithValue }) => {
    try {
      const response = await fetch("/api/users");
      if (!response.ok) {
        throw new Error("Failed to fetch users");
      }
      return await response.json();
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

// With parameters
export const updateUser = createAsyncThunk(
  "users/updateUser",
  async (userData: Partial<User>, { getState, rejectWithValue }) => {
    try {
      const state = getState() as RootState;
      const response = await fetch(`/api/users/${userData.id}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${state.auth.token}`,
        },
        body: JSON.stringify(userData),
      });
      return await response.json();
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

// Slice with async thunks
const usersSlice = createSlice({
  name: "users",
  initialState: {
    items: [] as User[],
    loading: false,
    error: null as string | null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchUsers.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});
```

### Saga Middleware

Handle complex async flows with generator functions:

```typescript
import { call, put, takeEvery, takeLatest, delay } from "redux-saga/effects";
import { fetchUsersSuccess, fetchUsersFailure } from "./actions";

// Worker saga
function* fetchUsersSaga(): Generator<any, void, any> {
  try {
    const response = yield call(fetch, "/api/users");
    const data = yield call([response, "json"]);
    yield put(fetchUsersSuccess(data));
  } catch (error) {
    yield put(fetchUsersFailure(error.message));
  }
}

// Watcher saga
function* watchFetchUsers() {
  yield takeLatest("users/fetchUsers", fetchUsersSaga);
}

// Complex flow saga
function* handleAddTodoSaga(action: any): Generator<any, void, any> {
  try {
    // Optimistic update
    yield put(addTodoOptimistic(action.payload));

    // API call
    const response = yield call(fetch, "/api/todos", {
      method: "POST",
      body: JSON.stringify(action.payload),
    });

    const savedTodo = yield call([response, "json"]);

    // Update with server response
    yield put(addTodoSuccess(savedTodo));
  } catch (error) {
    // Rollback on failure
    yield put(addTodoFailure(error.message));
    yield put(removeTodoOptimistic(action.payload.id));
  }
}

// Debounced saga
function* handleSearchSaga(action: any): Generator<any, void, any> {
  yield delay(300); // Debounce
  try {
    const response = yield call(fetch, `/api/search?q=${action.payload}`);
    const data = yield call([response, "json"]);
    yield put(searchSuccess(data));
  } catch (error) {
    yield put(searchFailure(error.message));
  }
}

function* searchSaga() {
  yield takeLatest("search/request", handleSearchSaga);
}

// Root saga
export default function* rootSaga() {
  yield all([
    watchFetchUsers(),
    watchAddTodo(),
    searchSaga(),
  ]);
}
```

---

## Redux Toolkit

Official, opinionated toolset for Redux:

```typescript
import {
  createSlice,
  createAsyncThunk,
  createEntityAdapter,
  createSelector,
  PayloadAction,
} from "@reduxjs/toolkit";

// Entity adapter for normalized state
const usersAdapter = createEntityAdapter<User>({
  selectId: (user) => user.id,
  sortComparer: (a, b) => a.name.localeCompare(b.name),
});

// Async thunks
export const fetchUsers = createAsyncThunk("users/fetchUsers", async () => {
  const response = await fetch("/api/users");
  return response.json();
});

export const addUser = createAsyncThunk(
  "users/addUser",
  async (userData: Omit<User, "id">) => {
    const response = await fetch("/api/users", {
      method: "POST",
      body: JSON.stringify(userData),
    });
    return response.json();
  }
);

// Slice with entity adapter
const usersSlice = createSlice({
  name: "users",
  initialState: usersAdapter.getInitialState({
    loading: false,
    error: null as string | null,
  }),
  reducers: {
    userUpdated: usersAdapter.updateOne,
    userRemoved: usersAdapter.removeOne,
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUsers.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.loading = false;
        usersAdapter.setAll(state, action.payload);
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || "Failed to fetch users";
      })
      .addCase(addUser.fulfilled, usersAdapter.addOne);
  },
});

// Actions
export const { userUpdated, userRemoved } = usersSlice.actions;

// Selectors
export const {
  selectAll: selectAllUsers,
  selectById: selectUserById,
  selectIds: selectUserIds,
} = usersAdapter.getSelectors((state: RootState) => state.users);

// Memoized selectors
export const selectActiveUsers = createSelector(
  [selectAllUsers],
  (users) => users.filter((user) => user.isActive)
);

export const selectUserCount = createSelector(
  [selectAllUsers],
  (users) => users.length
);
```

---

## RTK Query

Data fetching and caching for Redux:

```typescript
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

// Create API slice
export const apiSlice = createApi({
  reducerPath: "api",
  baseQuery: fetchBaseQuery({
    baseUrl: "/api",
    prepareHeaders: (headers, { getState }) => {
      const token = (getState() as RootState).auth.token;
      if (token) {
        headers.set("authorization", `Bearer ${token}`);
      }
      return headers;
    },
  }),
  tagTypes: ["User", "Post", "Comment"],
  endpoints: (builder) => ({
    // Queries
    getUsers: builder.query<User[], void>({
      query: () => "/users",
      providesTags: (result) =>
        result
          ? [...result.map(({ id }) => ({ type: "User" as const, id })), "User"]
          : ["User"],
    }),

    getUserById: builder.query<User, number>({
      query: (id) => `/users/${id}`,
      providesTags: (result, error, id) => [{ type: "User", id }],
    }),

    // Mutations
    addUser: builder.mutation<User, Omit<User, "id">>({
      query: (body) => ({
        url: "/users",
        method: "POST",
        body,
      }),
      invalidatesTags: ["User"],
    }),

    updateUser: builder.mutation<User, Partial<User> & Pick<User, "id">>({
      query: ({ id, ...patch }) => ({
        url: `/users/${id}`,
        method: "PATCH",
        body: patch,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: "User", id }],
      // Optimistic update
      async onQueryStarted({ id, ...patch }, { dispatch, queryFulfilled }) {
        const patchResult = dispatch(
          apiSlice.util.updateQueryData("getUsers", undefined, (draft) => {
            const user = draft.find((u) => u.id === id);
            if (user) Object.assign(user, patch);
          })
        );
        try {
          await queryFulfilled;
        } catch {
          patchResult.undo();
        }
      },
    }),

    deleteUser: builder.mutation<void, number>({
      query: (id) => ({
        url: `/users/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: (result, error, id) => [{ type: "User", id }],
    }),
  }),
});

// Auto-generated hooks
export const {
  useGetUsersQuery,
  useGetUserByIdQuery,
  useAddUserMutation,
  useUpdateUserMutation,
  useDeleteUserMutation,
} = apiSlice;

// Usage
function UsersList() {
  const { data: users, isLoading, error } = useGetUsersQuery();
  const [addUser] = useAddUserMutation();

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;

  return (
    <div>
      <button onClick={() => addUser({ name: "New User", email: "new@example.com" })}>
        Add User
      </button>
      {users?.map((user) => (
        <div key={user.id}>{user.name}</div>
      ))}
    </div>
  );
}
```

---

## Selector Patterns

Efficient data selection from state:

```typescript
import { createSelector } from "@reduxjs/toolkit";

// Basic selectors
const selectTodos = (state: RootState) => state.todos.items;
const selectFilter = (state: RootState) => state.todos.filter;

// Memoized selectors
export const selectFilteredTodos = createSelector(
  [selectTodos, selectFilter],
  (todos, filter) => {
    switch (filter) {
      case "completed":
        return todos.filter((todo) => todo.completed);
      case "active":
        return todos.filter((todo) => !todo.completed);
      default:
        return todos;
    }
  }
);

// Derived selectors
export const selectTodoStats = createSelector(
  [selectTodos],
  (todos) => ({
    total: todos.length,
    completed: todos.filter((t) => t.completed).length,
    active: todos.filter((t) => !t.completed).length,
  })
);

// Complex selectors with parameters
export const makeSelectTodoById = () =>
  createSelector(
    [selectTodos, (_, id: number) => id],
    (todos, id) => todos.find((todo) => todo.id === id)
  );

// Usage in component
const TodoItem = ({ id }: { id: number }) => {
  const selectTodoById = useMemo(makeSelectTodoById, []);
  const todo = useAppSelector((state) => selectTodoById(state, id));

  return <div>{todo?.text}</div>;
};
```

---

## Normalized State

Flatten nested data for efficient updates:

```typescript
// Before normalization
const state = {
  posts: [
    {
      id: 1,
      title: "Post 1",
      author: { id: 1, name: "Alice" },
      comments: [
        { id: 1, text: "Comment 1", author: { id: 2, name: "Bob" } },
      ],
    },
  ],
};

// After normalization
const normalizedState = {
  posts: {
    byId: {
      1: { id: 1, title: "Post 1", authorId: 1, commentIds: [1] },
    },
    allIds: [1],
  },
  users: {
    byId: {
      1: { id: 1, name: "Alice" },
      2: { id: 2, name: "Bob" },
    },
    allIds: [1, 2],
  },
  comments: {
    byId: {
      1: { id: 1, text: "Comment 1", authorId: 2 },
    },
    allIds: [1],
  },
};

// Using normalizr
import { normalize, schema } from "normalizr";

const userSchema = new schema.Entity("users");
const commentSchema = new schema.Entity("comments", { author: userSchema });
const postSchema = new schema.Entity("posts", {
  author: userSchema,
  comments: [commentSchema],
});

const data = { posts: [postData] };
const normalizedData = normalize(data, { posts: [postSchema] });

// Redux Toolkit Entity Adapter
import { createEntityAdapter } from "@reduxjs/toolkit";

const postsAdapter = createEntityAdapter({
  selectId: (post) => post.id,
  sortComparer: (a, b) => a.title.localeCompare(b.title),
});

const postsSlice = createSlice({
  name: "posts",
  initialState: postsAdapter.getInitialState(),
  reducers: {
    postAdded: postsAdapter.addOne,
    postUpdated: postsAdapter.updateOne,
    postRemoved: postsAdapter.removeOne,
    postsReceived: postsAdapter.setAll,
  },
});
```

---

## Redux DevTools

Debug and inspect Redux state:

```typescript
// Setup
import { configureStore } from "@reduxjs/toolkit";

const store = configureStore({
  reducer: rootReducer,
  devTools: process.env.NODE_ENV !== "production",
});

// With options
const store = configureStore({
  reducer: rootReducer,
  devTools: {
    name: "My App",
    trace: true,
    traceLimit: 25,
  },
});

// Custom devTools options
const store = configureStore({
  reducer: rootReducer,
  enhancers: (getDefaultEnhancers) => {
    if (process.env.NODE_ENV !== "production") {
      return getDefaultEnhancers().concat(
        devToolsEnhancer({ name: "My App", trace: true })
      );
    }
    return getDefaultEnhancers();
  },
});
```

### DevTools Features

- **State Inspector**: View current and previous states
- **Action Log**: See dispatched actions and their payloads
- **Time Travel**: Navigate through state history
- **Action Replay**: Re-dispatch previous actions
- **State Diff**: Compare state changes
- **Export/Import**: Save and restore state

---

## Testing

Test Redux logic in isolation:

```typescript
// Testing reducers
describe("todoReducer", () => {
  const initialState = { items: [], filter: "all" };

  it("should handle ADD_TODO", () => {
    const action = addTodo("Learn Redux");
    const result = todoReducer(initialState, action);

    expect(result.items).toHaveLength(1);
    expect(result.items[0].text).toBe("Learn Redux");
    expect(result.items[0].completed).toBe(false);
  });

  it("should handle TOGGLE_TODO", () => {
    const stateWithTodo = {
      items: [{ id: 1, text: "Test", completed: false }],
      filter: "all",
    };

    const action = toggleTodo(1);
    const result = todoReducer(stateWithTodo, action);

    expect(result.items[0].completed).toBe(true);
  });
});

// Testing selectors
describe("selectFilteredTodos", () => {
  const state = {
    todos: {
      items: [
        { id: 1, text: "Active", completed: false },
        { id: 2, text: "Completed", completed: true },
      ],
      filter: "completed",
    },
  };

  it("should filter todos by status", () => {
    const result = selectFilteredTodos(state);
    expect(result).toHaveLength(1);
    expect(result[0].text).toBe("Completed");
  });
});

// Testing async thunks
describe("fetchUsers", () => {
  it("should fetch users successfully", async () => {
    const mockUsers = [{ id: 1, name: "Alice" }];
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: () => Promise.resolve(mockUsers),
    });

    const dispatch = jest.fn();
    const getState = jest.fn();

    await fetchUsers()(dispatch, getState, undefined);

    expect(dispatch).toHaveBeenCalledWith(fetchUsers.pending());
    expect(dispatch).toHaveBeenCalledWith(fetchUsers.fulfilled(mockUsers));
  });

  it("should handle fetch errors", async () => {
    global.fetch = jest.fn().mockRejectedValue(new Error("Network error"));

    const dispatch = jest.fn();
    const getState = jest.fn();

    await fetchUsers()(dispatch, getState, undefined);

    expect(dispatch).toHaveBeenCalledWith(fetchUsers.rejected());
  });
});

// Testing with Redux Toolkit
import { renderHook } from "@testing-library/react";
import { Provider } from "react-redux";
import { createMockStore } from "@reduxjs/toolkit";

const createWrapper = (preloadedState) => {
  const store = createMockStore({
    reducer: rootReducer,
    preloadedState,
  });

  return ({ children }) => <Provider store={store}>{children}</Provider>;
};

it("should use useAppSelector", () => {
  const preloadedState = {
    todos: { items: [{ id: 1, text: "Test", completed: false }], filter: "all" },
  };

  const { result } = renderHook(() => useAppSelector(selectFilteredTodos), {
    wrapper: createWrapper(preloadedState),
  });

  expect(result.current).toHaveLength(1);
});
```

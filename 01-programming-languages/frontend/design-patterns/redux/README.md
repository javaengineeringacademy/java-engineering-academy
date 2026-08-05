# Redux (Predictable State Container)

## Overview

Redux is a predictable state container for JavaScript applications, inspired by Flux but with stricter rules. It uses a single store with pure reducer functions, action creators, and middleware. Redux enforces immutability, making state changes predictable and debuggable through tools like time-travel debugging.

## When to Use

- Applications with complex state management needs
- Needing time-travel debugging capabilities
- Working with large teams requiring consistent patterns
- Building applications requiring undo/redo functionality
- Managing state across deeply nested components

## Implementation

### React + Redux
```javascript
// Actions
const ADD_USER = 'ADD_USER';
const addUser = (name) => ({ type: ADD_USER, payload: { name } });

// Reducer
const userReducer = (state = [], action) => {
  switch (action.type) {
    case ADD_USER:
      return [...state, action.payload];
    default:
      return state;
  }
};

// Store
import { createStore } from 'redux';
const store = createStore(userReducer);

// Component
function UserList() {
  const users = useSelector(state => state);
  const dispatch = useDispatch();

  return (
    <div>
      {users.map((u, i) => <div key={i}>{u.name}</div>)}
      <button onClick={() => dispatch(addUser('New User'))}>Add</button>
    </div>
  );
}

// Provider
function App() {
  return (
    <Provider store={store}>
      <UserList />
    </Provider>
  );
}
```

### Vue (with Vuex)
```javascript
// Store
const store = new Vuex.Store({
  state: { users: [] },
  mutations: {
    ADD_USER(state, user) { state.users.push(user); }
  },
  actions: {
    addUser({ commit }, name) {
      commit('ADD_USER', { name });
    }
  }
});

// Component
export default {
  computed: { ...mapState(['users']) },
  methods: { ...mapActions(['addUser']) }
}
```

### Angular (NgRx)
```typescript
// Reducer
export const userReducer = createReducer(
  initialState,
  on(addUser, (state, { user }) => [...state, user])
);

// Component
@Component({ template: `
  <div *ngFor="let u of users$ | async">{{ u.name }}</div>
  <button (click)="addUser()">Add</button>
`})
export class UserListComponent {
  users$ = this.store.select(selectUsers);
  constructor(private store: Store) {}
  addUser() { this.store.dispatch(addUser({ name: 'New' })); }
}
```

## Best Practices

1. Keep state normalized and flat
2. Use selectors for derived data
3. Write pure reducer functions
4. Use middleware for side effects
5. Avoid storing derived data in state

## Interview Questions

1. What are the three Redux principles?
2. How does Redux differ from Flux?
3. What are reducers and why must they be pure?
4. How does middleware work in Redux?
5. What is the purpose of Redux DevTools?

## References

- Redux Official Documentation
- "Redux - Taming JavaScript's State Container" by Mark Erikson
- Redux Toolkit Documentation
- NgRx Documentation

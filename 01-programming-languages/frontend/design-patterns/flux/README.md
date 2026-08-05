# Flux (Unidirectional Data Flow)

## Overview

Flux is an application architecture created by Facebook for building client-side web applications. It enforces unidirectional data flow through four main components: Actions (events), Dispatcher (central hub), Stores (data/logic), and Views (UI components). This ensures predictable data flow and easier debugging.

## When to Use

- Building complex applications with dynamic data
- Needing predictable state management
- Working with multiple components sharing state
- Debugging data flow issues
- Developing applications requiring undo/redo functionality

## Implementation

### React (Flux Architecture)
```javascript
// Dispatcher
import { Dispatcher } from 'flux';
const dispatcher = new Dispatcher();

// Action Creator
function addUser(name) {
  dispatcher.dispatch({ type: 'ADD_USER', payload: { name } });
}

// Store
class UserStore {
  constructor() {
    this.users = [];
    dispatcher.register(this.handleAction.bind(this));
  }

  handleAction(action) {
    switch (action.type) {
      case 'ADD_USER':
        this.users.push(action.payload);
        this.emit('change');
        break;
    }
  }

  getUsers() { return [...this.users]; }
}

// View (Component)
function UserList() {
  const [users, setUsers] = useState([]);
  const store = new UserStore();

  useEffect(() => {
    store.on('change', () => setUsers(store.getUsers()));
  }, []);

  return (
    <div>
      {users.map((u, i) => <div key={i}>{u.name}</div>)}
      <button onClick={() => addUser('New User')}>Add</button>
    </div>
  );
}
```

### Vue
```javascript
// Simple Flux implementation
const store = Vue.observable({ users: [] });

const actions = {
  addUser(name) { store.users.push({ name }); }
};

// Component
export default {
  computed: { users: () => store.users },
  methods: { addUser: () => actions.addUser('New') }
}
```

### Angular
```typescript
// Store
@Injectable()
export class UserStore {
  private users: User[] = [];
  private subject = new Subject<User[]>();

  dispatch(action: Action) {
    switch (action.type) {
      case 'ADD_USER':
        this.users = [...this.users, action.payload];
        this.subject.next(this.users);
    }
  }

  select() { return this.subject.asObservable(); }
}

// Component
@Component({ template: `<div *ngFor="let u of users">{{u.name}}</div>` })
export class UserListComponent {
  users: User[] = [];
  constructor(private store: UserStore) {
    this.store.select().subscribe(users => this.users = users);
  }
}
```

## Best Practices

1. All data flows in one direction
2. Stores contain business logic and state
3. Components should be thin - dispatch actions only
4. Use action constants for type definitions
5. Keep the dispatcher synchronous

## Interview Questions

1. What are the four main Flux components?
2. How does Flux achieve unidirectional data flow?
3. What are the benefits over two-way data binding?
4. How does Redux extend Flux?
5. When is Flux overkill for an application?

## References

- Facebook Flux Documentation
- "Flux In Depth" by Bill Fisher
- Facebook Flux Examples
- Redux - A Flux Implementation

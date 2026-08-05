# MVP (Model-View-Presenter)

## Overview

MVP separates the application into Model, View, and Presenter. The Presenter acts as the middleman that retrieves data from the Model, formats it, and passes it to the View. Unlike MVC, the View is passive and contains no logic - all presentation logic lives in the Presenter.

## When to Use

- Building applications requiring high testability
- Needing to separate UI from business logic completely
- Working on large teams with designers and developers
- Developing applications with multiple views
- Creating platform-independent business logic

## Implementation

### React (MVP Pattern)
```javascript
// Model
class UserModel {
  constructor() { this.users = []; }
  fetchUsers() { return Promise.resolve(this.users); }
  saveUser(user) { this.users.push(user); }
}

// Presenter
class UserPresenter {
  constructor(model, view) {
    this.model = model;
    this.view = view;
  }
  async loadUsers() {
    const users = await this.model.fetchUsers();
    this.view.displayUsers(users);
  }
  addUser(name) {
    this.model.saveUser({ name, id: Date.now() });
    this.loadUsers();
  }
}

// View (Passive)
function UserView({ presenter }) {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    presenter.view = {
      displayUsers: setUsers
    };
    presenter.loadUsers();
  }, []);

  return (
    <div>
      {users.map(u => <div key={u.id}>{u.name}</div>)}
      <button onClick={() => presenter.addUser('New User')}>Add</button>
    </div>
  );
}
```

### Vue
```javascript
// Presenter
class UserPresenter {
  constructor(model, view) {
    this.model = model;
    this.view = view;
  }
  loadUsers() {
    const users = this.model.getUsers();
    this.view.users = users;
  }
}

// Component (View)
export default {
  data() { return { users: [] } },
  created() {
    this.presenter = new UserPresenter(new UserModel(), this);
    this.presenter.loadUsers();
  }
}
```

### Angular
```typescript
// Presenter
@Injectable()
export class UserPresenter {
  constructor(private model: UserModel, private view: UserViewInterface) {}

  loadUsers() {
    this.model.getUsers().subscribe(users => {
      this.view.displayUsers(users);
    });
  }
}

// Component (View)
@Component({
  selector: 'app-users',
  template: `
    <div *ngFor="let user of users">{{ user.name }}</div>
  `
})
export class UsersComponent implements UserViewInterface {
  users: User[] = [];

  constructor(private presenter: UserPresenter) {
    this.presenter.view = this;
    this.presenter.loadUsers();
  }

  displayUsers(users: User[]) { this.users = users; }
}
```

## Best Practices

1. Keep Views passive - no business logic
2. Presenters should be framework-agnostic
3. Use interfaces for View contracts
4. Test Presenters with mock Views
5. Keep Presenter methods focused and small

## Interview Questions

1. How does MVP differ from MVC?
2. What makes the View "passive" in MVP?
3. How does MVP improve testability?
4. What are common MVP implementations in JavaScript?
5. When would you choose MVP over MVVM?

## References

- Model-View-Presenter Wikipedia
- Martin Fowler - "GUI Architectures"
- Google Android MVP Samples
- Presentation Model Pattern

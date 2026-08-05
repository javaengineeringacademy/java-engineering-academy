# Container/Presentational Pattern

## Overview

The Container/Presentational pattern separates components into two types: Containers handle data fetching, state management, and logic; Presentational components handle rendering UI. This separation improves testability, reusability, and keeps components focused on single responsibilities.

## When to Use

- Separating data concerns from UI rendering
- Creating reusable presentational components
- Improving component testability
- Working with designers who focus on UI
- Building component libraries

## Implementation

### React
```javascript
// Presentational Component (UserList)
function UserList({ users, onUserClick, loading }) {
  if (loading) return <div>Loading...</div>;

  return (
    <ul>
      {users.map(user => (
        <li key={user.id} onClick={() => onUserClick(user)}>
          {user.name}
        </li>
      ))}
    </ul>
  );
}

// Container Component (UserListContainer)
function UserListContainer() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/api/users')
      .then(res => res.json())
      .then(data => {
        setUsers(data);
        setLoading(false);
      });
  }, []);

  const handleUserClick = (user) => {
    console.log('Clicked:', user);
  };

  return (
    <UserList
      users={users}
      onUserClick={handleUserClick}
      loading={loading}
    />
  );
}
```

### Vue
```vue
<!-- Presentational (UserList.vue) -->
<template>
  <div v-if="loading">Loading...</div>
  <ul v-else>
    <li v-for="user in users" :key="user.id" @click="$emit('click', user)">
      {{ user.name }}
    </li>
  </ul>
</template>

<script>
export default {
  props: ['users', 'loading'],
  emits: ['click']
}
</script>

<!-- Container (UserListContainer.vue) -->
<template>
  <UserList :users="users" :loading="loading" @click="handleClick" />
</template>

<script>
import UserList from './UserList.vue';

export default {
  components: { UserList },
  data() { return { users: [], loading: true } },
  mounted() {
    fetch('/api/users')
      .then(res => res.json())
      .then(data => { this.users = data; this.loading = false; });
  },
  methods: {
    handleClick(user) { console.log('Clicked:', user); }
  }
}
</script>
```

### Angular
```typescript
// Presentational
@Component({
  selector: 'app-user-list',
  template: `
    <div *ngIf="loading">Loading...</div>
    <ul *ngIf="!loading">
      <li *ngFor="let user of users" (click)="click.emit(user)">
        {{ user.name }}
      </li>
    </ul>
  `
})
export class UserListComponent {
  @Input() users: User[] = [];
  @Input() loading = false;
  @Output() click = new EventEmitter<User>();
}

// Container
@Component({
  selector: 'app-user-container',
  template: `<app-user-list [users]="users" [loading]="loading" (click)="onUserClick($event)"></app-user-list>`
})
export class UserContainerComponent implements OnInit {
  users: User[] = [];
  loading = true;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
      this.loading = false;
    });
  }

  onUserClick(user: User) { console.log('Clicked:', user); }
}
```

## Best Practices

1. Keep presentational components pure and declarative
2. Containers should handle all side effects
3. Use TypeScript interfaces for props
4. Presentational components should be reusable
5. Test presentational components with mock data

## Interview Questions

1. What is the difference between container and presentational components?
2. When should you separate a component into container/presentational?
3. How does this pattern improve testability?
4. What are the drawbacks of this pattern?
5. How do hooks change this pattern?

## References

- "Presentational and Container Components" by Dan Abramov
- React Documentation - Thinking in React
- "Patterns for Managing State" by Addy Osmani
- Component Patterns Course by Kent C. Dodds

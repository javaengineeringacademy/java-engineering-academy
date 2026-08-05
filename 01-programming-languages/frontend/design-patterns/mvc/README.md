# MVC (Model-View-Controller)

## Overview

MVC is an architectural pattern that separates an application into three interconnected components: Model (data and business logic), View (user interface), and Controller (handles input and updates). This separation allows for organized code, parallel development, and easier testing.

## When to Use

- Building applications with complex data manipulation
- Needing clear separation between UI and business logic
- Working on large-scale enterprise applications
- Requiring multiple views for the same data
- Developing applications with multiple developers

## Implementation

### React (Manual MVC)
```javascript
// Model
class UserModel {
  constructor() {
    this.users = [];
  }
  addUser(user) { this.users.push(user); }
  getUsers() { return [...this.users]; }
}

// Controller
class UserController {
  constructor(model, view) {
    this.model = model;
    this.view = view;
  }
  handleAddUser(name) {
    this.model.addUser({ name });
    this.view.render(this.model.getUsers());
  }
}

// View
class UserView {
  render(users) {
    console.log('Users:', users);
  }
}
```

### Vue (Template-based MVC)
```html
<template>
  <div>
    <input v-model="newUser" />
    <button @click="addUser">Add</button>
    <ul>
      <li v-for="user in users">{{ user.name }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  data() { return { users: [], newUser: '' } },
  methods: {
    addUser() {
      this.users.push({ name: this.newUser });
      this.newUser = '';
    }
  }
}
</script>
```

### Angular
```typescript
// Component (Controller + View)
@Component({
  selector: 'app-users',
  template: `
    <input [(ngModel)]="newUser" />
    <button (click)="addUser()">Add</button>
    <li *ngFor="let user of users">{{ user.name }}</li>
  `
})
export class UsersComponent {
  users: User[] = [];
  newUser = '';
  addUser() {
    this.users.push({ name: this.newUser });
    this.newUser = '';
  }
}
```

## Best Practices

1. Keep models independent of views
2. Controllers should be thin - delegate to services
3. Views should only display data, not contain business logic
4. Use data binding to connect views to models
5. Test each component separately

## Interview Questions

1. What are the responsibilities of each MVC component?
2. How does MVC differ from MVVM?
3. What problems does MVC solve?
4. What are the drawbacks of MVC in frontend development?
5. How do frameworks implement MVC differently?

## References

- "Architecture Patterns with Python" by Harry Percival
- Martin Fowler - "Patterns of Enterprise Application Architecture"
- Angular Documentation
- Backbone.js Documentation

# MVVM (Model-View-ViewModel)

## Overview

MVVM separates the user interface (View) from the business logic (ViewModel) and data (Model). The ViewModel acts as an intermediary that exposes data and commands the View can bind to, enabling two-way data binding and automatic UI updates when data changes.

## When to Use

- Building data-driven applications with forms
- Needing automatic synchronization between data and UI
- Working with complex form validation
- Developing applications requiring rich user interaction
- Using frameworks with built-in data binding

## Implementation

### Vue (Native MVVM)
```html
<template>
  <div>
    <input v-model="fullName" />
    <p>{{ fullName }}</p>
    <button @click="save">Save</button>
  </div>
</template>

<script>
export default {
  data() {
    return { firstName: '', lastName: '' }
  },
  computed: {
    fullName: {
      get() { return `${this.firstName} ${this.lastName}` },
      set(value) {
        const [first, last] = value.split(' ');
        this.firstName = first;
        this.lastName = last || '';
      }
    }
  },
  methods: {
    save() { console.log('Saving:', this.fullName); }
  }
}
</script>
```

### React (with Hooks)
```javascript
import { useState } from 'react';

function UserViewModel() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');

  const fullName = `${firstName} ${lastName}`;
  const updateFullName = (value) => {
    const [first, last] = value.split(' ');
    setFirstName(first);
    setLastName(last || '');
  };

  return { firstName, lastName, fullName, updateFullName };
}

function UserView() {
  const vm = UserViewModel();
  return (
    <div>
      <input value={vm.fullName}
             onChange={(e) => vm.updateFullName(e.target.value)} />
      <p>{vm.fullName}</p>
    </div>
  );
}
```

### Angular
```typescript
// ViewModel (Component)
@Component({
  selector: 'app-user',
  template: `
    <input [(ngModel)]="fullName" />
    <p>{{ fullName }}</p>
    <button (click)="save()">Save</button>
  `
})
export class UserComponent {
  firstName = '';
  lastName = '';

  get fullName() { return `${this.firstName} ${this.lastName}`; }
  set fullName(value) {
    const [first, last] = value.split(' ');
    this.firstName = first;
    this.lastName = last || '';
  }

  save() { console.log('Saving:', this.fullName); }
}
```

## Best Practices

1. Keep Views declarative - avoid logic in templates
2. ViewModels should expose only what the View needs
3. Use computed properties for derived state
4. Implement proper data validation in the ViewModel
5. Test ViewModels independently from Views

## Interview Questions

1. How does two-way data binding work in MVVM?
2. What is the difference between MVVM and MVC?
3. How do computed properties relate to MVVM?
4. What are the benefits of using MVVM in frontend development?
5. Which frameworks natively support MVVM?

## References

- John Gossman - "Introduction to Model/View/ViewModel pattern"
- Vue.js Official Documentation
- Knockout.js Documentation
- Angular Documentation

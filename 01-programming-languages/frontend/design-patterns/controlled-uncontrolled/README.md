# Controlled vs Uncontrolled Components

## Overview

Controlled components have their state managed by the parent component through props, while uncontrolled components manage their own state internally, often using refs. Controlled components offer predictability and validation, while uncontrolled components are simpler for forms with minimal interaction.

## When to Use

### Controlled
- Form validation is required
- Form data needs to be submitted programmatically
- Multiple inputs need to synchronize
- Dynamic form behavior based on other fields
- Needing real-time validation feedback

### Uncontrolled
- Simple forms without complex validation
- Integrating with non-React code
- Quick prototypes and simple implementations
- File inputs (always uncontrolled in React)
- Minimal form state management

## Implementation

### React
```javascript
// Controlled Component
function ControlledForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log({ name, email });
  };

  return (
    <form onSubmit={handleSubmit}>
      <input value={name} onChange={(e) => setName(e.target.value)} />
      <input value={email} onChange={(e) => setEmail(e.target.value)} />
      <button type="submit">Submit</button>
    </form>
  );
}

// Uncontrolled Component
function UncontrolledForm() {
  const nameRef = useRef();
  const emailRef = useRef();

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log({
      name: nameRef.current.value,
      email: emailRef.current.value
    });
  };

  return (
    <form onSubmit={handleSubmit}>
      <input ref={nameRef} defaultValue="" />
      <input ref={emailRef} defaultValue="" />
      <button type="submit">Submit</button>
    </form>
  );
}

// File Input (Always Uncontrolled)
function FileUpload() {
  const fileRef = useRef();

  const handleSubmit = (e) => {
    e.preventDefault();
    const file = fileRef.current.files[0];
    console.log(file);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input type="file" ref={fileRef} />
      <button type="submit">Upload</button>
    </form>
  );
}
```

### Vue
```vue
<!-- Controlled (v-model) -->
<template>
  <form @submit.prevent="handleSubmit">
    <input v-model="name" />
    <input v-model="email" />
    <button type="submit">Submit</button>
  </form>
</template>

<script>
export default {
  data() { return { name: '', email: '' } },
  methods: {
    handleSubmit() { console.log({ name: this.name, email: this.email }); }
  }
}
</script>

<!-- Uncontrolled ($refs) -->
<template>
  <form @submit.prevent="handleSubmit">
    <input ref="name" />
    <input ref="email" />
    <button type="submit">Submit</button>
  </form>
</template>

<script>
export default {
  methods: {
    handleSubmit() {
      console.log({
        name: this.$refs.name.value,
        email: this.$refs.email.value
      });
    }
  }
}
</script>
```

### Angular
```typescript
// Reactive Forms (Controlled)
@Component({
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <input formControlName="name" />
      <input formControlName="email" />
      <button type="submit">Submit</button>
    </form>
  `
})
export class ControlledFormComponent {
  form = new FormGroup({
    name: new FormControl(''),
    email: new FormControl('')
  });
  onSubmit() { console.log(this.form.value); }
}

// Template-driven (More Uncontrolled)
@Component({
  template: `
    <form #form="ngForm" (ngSubmit)="onSubmit(form)">
      <input name="name" ngModel />
      <input name="email" ngModel />
      <button type="submit">Submit</button>
    </form>
  `
})
export class UncontrolledFormComponent {
  onSubmit(form: NgForm) { console.log(form.value); }
}
```

## Best Practices

1. Use controlled components for form validation
2. Default to controlled components in React
3. Use uncontrolled for file inputs
4. Consider performance for large forms
5. Use refs sparingly for uncontrolled inputs

## Interview Questions

1. What is the difference between controlled and uncontrolled components?
2. When would you use an uncontrolled component?
3. How do you handle file uploads in React?
4. What are the performance implications of each approach?
5. How do Angular Reactive Forms compare?

## References

- React Documentation - Controlled vs Uncontrolled
- "Forms in React" by React Team
- Angular Reactive Forms Guide
- Vue.js Form Handling Guide

# Lifting State Up

## Overview

Lifting State Up is a pattern where shared state is moved to the nearest common ancestor of components that need it. Instead of duplicating state in multiple components or passing it deeply through props, the state lives in a parent component that passes it down as props. This ensures a single source of truth and keeps components in sync.

## When to Use

- Multiple components need to share the same data
- Child components need to synchronize their state
- Avoiding duplicate state across sibling components
- Building forms with interdependent fields
- Creating coordinated UI elements

## Implementation

### React
```javascript
// Before: Duplicated state
function TemperatureInput({ scale }) {
  const [temperature, setTemperature] = useState('');
  // State is local and not shared
}

// After: Lifting state up
function BoilingVerdict({ celsius }) {
  if (celsius >= 100) return <p>The water would boil.</p>;
  return <p>The water would not boil.</p>;
}

function TemperatureInput({ scale, temperature, onTemperatureChange }) {
  const names = { c: 'Celsius', f: 'Fahrenheit' };
  return (
    <fieldset>
      <legend>Enter temperature in {names[scale]}:</legend>
      <input value={temperature}
             onChange={(e) => onTemperatureChange(e.target.value)} />
    </fieldset>
  );
}

function Calculator() {
  const [temperature, setTemperature] = useState('');
  const [scale, setScale] = useState('c');

  const handleCelsiusChange = (temp) => {
    setTemperature(temp);
    setScale('c');
  };

  const handleFahrenheitChange = (temp) => {
    setTemperature(temp);
    setScale('f');
  };

  const celsius = scale === 'f' ? (parseFloat(temperature) - 32) * 5/9 : temperature;

  return (
    <div>
      <TemperatureInput scale="c" temperature={temperature}
                        onTemperatureChange={handleCelsiusChange} />
      <TemperatureInput scale="f" temperature={temperature}
                        onTemperatureChange={handleFahrenheitChange} />
      <BoilingVerdict celsius={parseFloat(celsius)} />
    </div>
  );
}
```

### Vue
```vue
<!-- Parent Component -->
<template>
  <temperature-input
    scale="c"
    :temperature="temperature"
    @update="handleCelsius"
  />
  <temperature-input
    scale="f"
    :temperature="temperature"
    @update="handleFahrenheit"
  />
  <boiling-verdict :celsius="celsius" />
</template>

<script>
export default {
  data() { return { temperature: '', scale: 'c' } },
  computed: {
    celsius() {
      if (this.scale === 'f') {
        return (parseFloat(this.temperature) - 32) * 5/9;
      }
      return this.temperature;
    }
  },
  methods: {
    handleCelsius(temp) { this.temperature = temp; this.scale = 'c'; },
    handleFahrenheit(temp) { this.temperature = temp; this.scale = 'f'; }
  }
}
</script>
```

### Angular
```typescript
@Component({
  selector: 'app-calculator',
  template: `
    <app-temperature-input scale="c" [temperature]="temperature"
      (update)="handleCelsius($event)"></app-temperature-input>
    <app-temperature-input scale="f" [temperature]="temperature"
      (update)="handleFahrenheit($event)"></app-temperature-input>
    <app-boiling-verdict [celsius]="celsius"></app-boiling-verdict>
  `
})
export class CalculatorComponent {
  temperature = '';
  scale = 'c';

  get celsius() {
    if (this.scale === 'f') {
      return (parseFloat(this.temperature) - 32) * 5/9;
    }
    return this.temperature;
  }

  handleCelsius(temp: string) { this.temperature = temp; this.scale = 'c'; }
  handleFahrenheit(temp: string) { this.temperature = temp; this.scale = 'f'; }
}
```

## Best Practices

1. Lift state only as high as needed
2. Keep state minimal and derive values when possible
3. Use callback props to allow child updates
4. Consider Context for deeply nested state
5. Avoid lifting too much state to parent components

## Interview Questions

1. What does "lifting state up" mean?
2. When should you lift state versus using local state?
3. How do you handle state shared by distant components?
4. What are the alternatives to lifting state?
5. How does lifting state affect component re-rendering?

## References

- React Documentation - Lifting State Up
- "Thinking in React" by React Team
- "Managing State" by Kent C. Dodds
- Vue.js Reactivity Guide

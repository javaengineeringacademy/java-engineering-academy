# Compound Components

## Overview

Compound Components are a pattern where a set of related components work together with shared implicit state. The parent component manages state and provides it to children through context, creating a cohesive API where children automatically coordinate with each other without explicit prop passing.

## When to Use

- Building complex UI components with multiple parts
- Creating cohesive component APIs (tabs, accordions, menus)
- Needing implicit state sharing between related components
- Designing developer-friendly component libraries
- Reducing prop drilling in component hierarchies

## Implementation

### React (Context-based)
```javascript
import { createContext, useContext, useState } from 'react';

const TabsContext = createContext();

function Tabs({ children, defaultTab }) {
  const [activeTab, setActiveTab] = useState(defaultTab);

  return (
    <TabsContext.Provider value={{ activeTab, setActiveTab }}>
      <div className="tabs">{children}</div>
    </TabsContext.Provider>
  );
}

function TabList({ children }) {
  return <div className="tab-list">{children}</div>;
}

function Tab({ children, id }) {
  const { activeTab, setActiveTab } = useContext(TabsContext);
  return (
    <button
      className={`tab ${activeTab === id ? 'active' : ''}`}
      onClick={() => setActiveTab(id)}
    >
      {children}
    </button>
  );
}

function TabPanel({ children, id }) {
  const { activeTab } = useContext(TabsContext);
  if (activeTab !== id) return null;
  return <div className="tab-panel">{children}</div>;
}

// Usage
<Tabs defaultTab="tab1">
  <TabList>
    <Tab id="tab1">Tab 1</Tab>
    <Tab id="tab2">Tab 2</Tab>
  </TabList>
  <TabPanel id="tab1">Content 1</TabPanel>
  <TabPanel id="tab2">Content 2</TabPanel>
</Tabs>
```

### Vue (Provide/Inject)
```vue
<!-- Parent Component -->
<script>
export default {
  provide() {
    return {
      tabsState: reactive({
        activeTab: this.defaultTab,
        setActiveTab: (id) => { this.tabsState.activeTab = id; }
      })
    };
  }
}
</script>

<!-- Child Component (Tab) -->
<script>
export default {
  inject: ['tabsState'],
  props: ['id'],
  computed: {
    isActive() { return this.tabsState.activeTab === this.id; }
  }
}
</script>
```

### Angular (Service-based)
```typescript
// Service
@Injectable()
export class TabsService {
  activeTab = signal('');
  setActiveTab(id: string) { this.activeTab.set(id); }
}

// Parent Component
@Component({
  providers: [TabsService],
  template: `<ng-content></ng-content>`
})
export class TabsComponent {
  constructor(public tabsService: TabsService) {}
}

// Child Component
@Component({
  selector: 'app-tab',
  template: `
    <button [class.active]="isActive()" (click)="select()">
      <ng-content></ng-content>
    </button>
  `
})
export class TabComponent {
  @Input() id = '';
  constructor(private tabsService: TabsService) {}
  isActive() { return this.tabsService.activeTab() === this.id; }
  select() { this.tabsService.setActiveTab(this.id); }
}
```

## Best Practices

1. Use context for implicit state sharing
2. Keep compound components in the same module
3. Provide sensible defaults for state
4. Allow both controlled and uncontrolled usage
5. Document the component API clearly

## Interview Questions

1. What makes components "compound"?
2. How does implicit state sharing work?
3. When should you use compound components?
4. What are examples of compound components in libraries?
5. How do you test compound components?

## References

- "Advanced React Patterns" by Kent C. Dodds
- "Compound Components" by Kent C. Dodds Blog
- Downshift Library (compound component example)
- React Aria (Adobe compound components)

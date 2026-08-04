# Web Components

Web Components are a set of web platform APIs that allow you to create reusable custom elements with encapsulated functionality. They work across any framework or without one.

## Table of Contents

- [Custom Elements (v1)](#custom-elements-v1)
- [Shadow DOM (v1)](#shadow-dom-v1)
- [HTML Templates](#html-templates)
- [Lifecycle Callbacks](#lifecycle-callbacks)
- [Slots](#slots)
- [CSS Encapsulation](#css-encapsulation)
- [Framework Integration](#framework-integration)
- [Best Practices](#best-practices)

---

## Custom Elements (v1)

Define new HTML elements with custom behavior:

```javascript
// Basic custom element
class MyCounter extends HTMLElement {
  constructor() {
    super();
    this.count = 0;
  }

  connectedCallback() {
    this.render();
  }

  render() {
    this.innerHTML = `
      <div class="counter">
        <p>Count: <span id="count">${this.count}</span></p>
        <button id="increment">+</button>
        <button id="decrement">-</button>
      </div>
    `;

    this.querySelector("#increment").addEventListener("click", () => {
      this.count++;
      this.querySelector("#count").textContent = this.count;
    });

    this.querySelector("#decrement").addEventListener("click", () => {
      this.count--;
      this.querySelector("#count").textContent = this.count;
    });
  }
}

// Register the custom element
customElements.define("my-counter", MyCounter);

// Use in HTML
// <my-counter></my-counter>
```

### Custom Element with Attributes

```javascript
class UserProfile extends HTMLElement {
  static get observedAttributes() {
    return ["username", "avatar", "role"];
  }

  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }

  connectedCallback() {
    this.render();
  }

  attributeChangedCallback(name, oldValue, newValue) {
    if (oldValue !== newValue) {
      this.render();
    }
  }

  get username() {
    return this.getAttribute("username") || "Anonymous";
  }

  get avatar() {
    return this.getAttribute("avatar") || "/default-avatar.png";
  }

  get role() {
    return this.getAttribute("role") || "user";
  }

  render() {
    this.shadowRoot.innerHTML = `
      <style>
        .profile {
          display: flex;
          align-items: center;
          gap: 1rem;
          padding: 1rem;
          border: 1px solid #ccc;
          border-radius: 8px;
        }
        .avatar {
          width: 48px;
          height: 48px;
          border-radius: 50%;
        }
        .role {
          font-size: 0.75rem;
          color: #666;
          text-transform: uppercase;
        }
      </style>
      <div class="profile">
        <img class="avatar" src="${this.avatar}" alt="${this.username}">
        <div>
          <div class="name">${this.username}</div>
          <div class="role">${this.role}</div>
        </div>
      </div>
    `;
  }
}

customElements.define("user-profile", UserProfile);

// Usage
// <user-profile username="Alice" avatar="/alice.jpg" role="admin"></user-profile>
```

---

## Shadow DOM (v1)

Encapsulate DOM and styles within custom elements:

```javascript
class Card extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }

  connectedCallback() {
    const title = this.getAttribute("title") || "Card Title";
    const content = this.getAttribute("content") || "Card content";

    this.shadowRoot.innerHTML = `
      <style>
        :host {
          display: block;
          border: 1px solid #e0e0e0;
          border-radius: 8px;
          padding: 16px;
          margin: 8px;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        ::slotted(h2) {
          margin: 0 0 8px 0;
          color: #333;
        }

        ::slotted(p) {
          margin: 0;
          color: #666;
        }

        .card-header {
          border-bottom: 1px solid #e0e0e0;
          padding-bottom: 8px;
          margin-bottom: 8px;
        }
      </style>
      <div class="card-header">
        <slot name="header">
          <h2>${title}</h2>
        </slot>
      </div>
      <div class="card-content">
        <slot>
          <p>${content}</p>
        </slot>
      </div>
      <div class="card-footer">
        <slot name="footer"></slot>
      </div>
    `;
  }
}

customElements.define("my-card", Card);

// Usage
// <my-card title="My Card">
//   <p slot="header">Custom Header</p>
//   <p>Custom content</p>
//   <p slot="footer">Custom Footer</p>
// </my-card>
```

### Shadow DOM Modes

```javascript
// Open mode - accessible from outside
this.attachShadow({ mode: "open" });
// element.shadowRoot returns the shadow root

// Closed mode - not accessible from outside
this.attachShadow({ mode: "closed" });
// element.shadowRoot returns null

// Accessing shadow DOM
const card = document.querySelector("my-card");
console.log(card.shadowRoot); // Open mode: ShadowRoot, Closed mode: null
```

---

## HTML Templates

Define reusable HTML structures:

```html
<!-- Template element -->
<template id="user-template">
  <style>
    .user {
      display: flex;
      align-items: center;
      padding: 8px;
      border-bottom: 1px solid #eee;
    }
    .avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin-right: 12px;
    }
    .info {
      flex: 1;
    }
    .name {
      font-weight: bold;
    }
    .email {
      color: #666;
      font-size: 0.875rem;
    }
  </style>
  <div class="user">
    <img class="avatar" src="" alt="">
    <div class="info">
      <div class="name"></div>
      <div class="email"></div>
    </div>
  </div>
</template>

<!-- Slot template -->
<template id="card-template">
  <div class="card">
    <div class="card-header">
      <slot name="header">Default Header</slot>
    </div>
    <div class="card-body">
      <slot>Default Content</slot>
    </div>
    <div class="card-footer">
      <slot name="footer">Default Footer</slot>
    </div>
  </div>
</template>

<script>
  // Using templates
  class UserList extends HTMLElement {
    connectedCallback() {
      const template = document.getElementById("user-template");
      const users = [
        { name: "Alice", email: "alice@example.com", avatar: "/alice.jpg" },
        { name: "Bob", email: "bob@example.com", avatar: "/bob.jpg" },
      ];

      users.forEach((user) => {
        const clone = template.content.cloneNode(true);
        clone.querySelector(".name").textContent = user.name;
        clone.querySelector(".email").textContent = user.email;
        clone.querySelector(".avatar").src = user.avatar;
        this.appendChild(clone);
      });
    }
  }

  customElements.define("user-list", UserList);
</script>
```

---

## Lifecycle Callbacks

React to element lifecycle events:

```javascript
class LifecycleLogger extends HTMLElement {
  constructor() {
    super();
    console.log("constructor called");
  }

  connectedCallback() {
    console.log("Element added to DOM");
    console.log("Attributes:", this.attributes);

    // Dispatch custom event
    this.dispatchEvent(
      new CustomEvent("component-ready", {
        bubbles: true,
        detail: { element: this },
      })
    );
  }

  disconnectedCallback() {
    console.log("Element removed from DOM");

    // Clean up event listeners
    this.removeEventListener("click", this.handleClick);
  }

  adoptedCallback() {
    console.log("Element moved to new document");
  }

  attributeChangedCallback(name, oldValue, newValue) {
    console.log(`Attribute ${name} changed from ${oldValue} to ${newValue}`);
  }

  static get observedAttributes() {
    return ["data-id", "data-type"];
  }
}

customElements.define("lifecycle-logger", LifecycleLogger);
```

### Lifecycle Diagram

```
constructor()
    |
    v
connectedCallback()  <--- Element added to DOM
    |
    v
attributeChangedCallback()  <--- Attributes change
    |
    v
disconnectedCallback()  <--- Element removed from DOM
    |
    v
adoptedCallback()  <--- Element moved to new document
```

---

## Slots

Project content into custom element templates:

```javascript
class Modal extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }

  connectedCallback() {
    this.shadowRoot.innerHTML = `
      <style>
        :host {
          display: none;
          position: fixed;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          background: rgba(0, 0, 0, 0.5);
          z-index: 1000;
        }

        :host([open]) {
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .modal {
          background: white;
          border-radius: 8px;
          max-width: 500px;
          width: 90%;
          box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
        }

        .modal-header {
          padding: 16px;
          border-bottom: 1px solid #e0e0e0;
          display: flex;
          justify-content: space-between;
          align-items: center;
        }

        .modal-body {
          padding: 16px;
        }

        .modal-footer {
          padding: 16px;
          border-top: 1px solid #e0e0e0;
          display: flex;
          justify-content: flex-end;
          gap: 8px;
        }

        .close-btn {
          background: none;
          border: none;
          font-size: 24px;
          cursor: pointer;
        }
      </style>
      <div class="modal">
        <div class="modal-header">
          <slot name="header">
            <h2>Modal Title</h2>
          </slot>
          <button class="close-btn" id="close">&times;</button>
        </div>
        <div class="modal-body">
          <slot>Default modal content</slot>
        </div>
        <div class="modal-footer">
          <slot name="footer">
            <button id="close-footer">Close</button>
          </slot>
        </div>
      </div>
    `;

    this.shadowRoot.getElementById("close").addEventListener("click", () => {
      this.close();
    });

    this.shadowRoot.getElementById("close-footer").addEventListener("click", () => {
      this.close();
    });
  }

  open() {
    this.setAttribute("open", "");
  }

  close() {
    this.removeAttribute("open");
    this.dispatchEvent(new CustomEvent("close", { bubbles: true }));
  }
}

customElements.define("my-modal", Modal);

// Usage
// <my-modal id="myModal">
//   <h2 slot="header">Custom Title</h2>
//   <p>This is custom content</p>
//   <button slot="footer">Confirm</button>
// </my-modal>
```

### Default Slot Content

```html
<!-- Default slot content -->
<my-card>
  <!-- Uses default content if no slot provided -->
</my-card>

<!-- Named slot content -->
<my-card>
  <h2 slot="header">Custom Header</h2>
  <p>Custom body content</p>
  <button slot="footer">Action</button>
</my-card>
```

---

## CSS Encapsulation

Styles scoped to Shadow DOM:

```javascript
class StyledButton extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }

  connectedCallback() {
    const variant = this.getAttribute("variant") || "primary";
    const size = this.getAttribute("size") || "medium";

    this.shadowRoot.innerHTML = `
      <style>
        :host {
          display: inline-block;
        }

        button {
          border: none;
          border-radius: 4px;
          cursor: pointer;
          font-weight: 500;
          transition: all 0.2s;
        }

        :host([variant="primary"]) button {
          background: #007bff;
          color: white;
        }

        :host([variant="secondary"]) button {
          background: #6c757d;
          color: white;
        }

        :host([variant="danger"]) button {
          background: #dc3545;
          color: white;
        }

        :host([size="small"]) button {
          padding: 4px 8px;
          font-size: 12px;
        }

        :host([size="medium"]) button {
          padding: 8px 16px;
          font-size: 14px;
        }

        :host([size="large"]) button {
          padding: 12px 24px;
          font-size: 16px;
        }

        button:hover {
          opacity: 0.9;
          transform: translateY(-1px);
        }

        button:active {
          transform: translateY(0);
        }

        button:focus {
          outline: 2px solid #005fcc;
          outline-offset: 2px;
        }
      </style>
      <button>
        <slot></slot>
      </button>
    `;
  }
}

customElements.define("styled-button", StyledButton);

// Usage
// <styled-button variant="primary" size="large">Click Me</styled-button>
```

### CSS Parts

```javascript
class VideoPlayer extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }

  connectedCallback() {
    this.shadowRoot.innerHTML = `
      <style>
        .player {
          background: #000;
          border-radius: 8px;
          overflow: hidden;
        }

        .controls {
          background: rgba(0, 0, 0, 0.7);
          padding: 8px;
          display: flex;
          gap: 8px;
        }
      </style>
      <div class="player">
        <video part="video"></video>
        <div class="controls" part="controls">
          <button part="play-button">Play</button>
          <input type="range" part="progress-bar" min="0" max="100" value="0">
        </div>
      </div>
    `;
  }
}

customElements.define("video-player", VideoPlayer);

/* Styling from outside using ::part */
video-player::part(video) {
  width: 100%;
  max-height: 400px;
}

video-player::part(controls) {
  background: rgba(0, 0, 0, 0.9);
}

video-player::part(play-button) {
  background: #007bff;
  color: white;
}
```

---

## Framework Integration

### React Integration

```jsx
// React wrapper for Web Component
import { useRef, useEffect } from "react";

function WebComponentWrapper({ component, ...props }) {
  const ref = useRef();

  useEffect(() => {
    const element = ref.current;
    if (!element) return;

    // Set attributes
    Object.entries(props).forEach(([key, value]) => {
      if (key.startsWith("on")) {
        const eventName = key.slice(2).toLowerCase();
        element.addEventListener(eventName, value);
      } else {
        element.setAttribute(key, value);
      }
    });

    return () => {
      // Clean up event listeners
      Object.entries(props).forEach(([key, value]) => {
        if (key.startsWith("on")) {
          const eventName = key.slice(2).toLowerCase();
          element.removeEventListener(eventName, value);
        }
      });
    };
  }, [props]);

  return <component ref={ref} {...props} />;
}

// Usage
<WebComponentWrapper
  component="my-button"
  variant="primary"
  onClick={handleClick}
>
  Click me
</WebComponentWrapper>
```

### Vue Integration

```vue
<!-- Vue component wrapping Web Component -->
<template>
  <my-button
    :variant="variant"
    :size="size"
    @click="handleClick"
  >
    <slot></slot>
  </my-button>
</template>

<script>
export default {
  name: "MyButton",
  props: {
    variant: { type: String, default: "primary" },
    size: { type: String, default: "medium" },
  },
  methods: {
    handleClick(event) {
      this.$emit("click", event);
    },
  },
};
</script>
```

### Angular Integration

```typescript
// Angular component wrapping Web Component
import { Component, Input, Output, EventEmitter } from "@angular/core";

@Component({
  selector: "app-button",
  template: `
    <my-button
      [attr.variant]="variant"
      [attr.size]="size"
      (click)="handleClick($event)"
    >
      <ng-content></ng-content>
    </my-button>
  `,
})
export class ButtonComponent {
  @Input() variant = "primary";
  @Input() size = "medium";
  @Output() clicked = new EventEmitter<Event>();

  handleClick(event: Event) {
    this.clicked.emit(event);
  }
}
```

---

## Best Practices

```markdown
### Naming
- Use kebab-case for custom element names
- Prefix with your namespace (e.g., my-app-button)
- Avoid naming conflicts with existing elements

### Performance
- Use Shadow DOM for encapsulation
- Avoid heavy DOM manipulation in callbacks
- Use requestAnimationFrame for animations
- Clean up event listeners in disconnectedCallback

### Accessibility
- Add ARIA attributes to custom elements
- Support keyboard navigation
- Provide proper focus management
- Use semantic HTML within shadow DOM

### Testing
- Test custom elements in isolation
- Mock shadow DOM for unit tests
- Test attribute changes and events
- Verify accessibility compliance

### Documentation
- Document observed attributes
- List available slots
- Describe custom events
- Provide usage examples
```

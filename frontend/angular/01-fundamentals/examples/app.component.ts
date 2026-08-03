import { Component, OnInit } from '@angular/core';

// Interface
interface User {
  id: number;
  name: string;
  email: string;
}

@Component({
  selector: 'app-root',
  template: `
    <div class="app">
      <h1>Angular Fundamentals</h1>
      
      <!-- Counter -->
      <div>
        <p>Count: {{ count }}</p>
        <button (click)="increment()">Increment</button>
        <button (click)="decrement()">Decrement</button>
      </div>

      <!-- List rendering -->
      <h2>Users</h2>
      <ul>
        <li *ngFor="let user of users">
          {{ user.name }} - {{ user.email }}
        </li>
      </ul>

      <!-- Conditional rendering -->
      <p *ngIf="users.length === 0">No users found</p>
      <p *ngIf="users.length > 0">{{ users.length }} users loaded</p>

      <!-- Two-way binding -->
      <div>
        <input [(ngModel)]="name" placeholder="Enter name">
        <p>Hello, {{ name }}!</p>
      </div>
    </div>
  `,
})
export class AppComponent implements OnInit {
  count: number = 0;
  users: User[] = [];
  name: string = '';

  ngOnInit() {
    // Simulate API call
    this.users = [
      { id: 1, name: 'Alice', email: 'alice@example.com' },
      { id: 2, name: 'Bob', email: 'bob@example.com' },
    ];
  }

  increment() {
    this.count++;
  }

  decrement() {
    this.count--;
  }
}

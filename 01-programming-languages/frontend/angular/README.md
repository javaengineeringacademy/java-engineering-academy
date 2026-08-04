# Angular Complete Guide

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Components](#components)
3. [Modules](#modules)
4. [Services and Dependency Injection](#services-and-dependency-injection)
5. [Routing](#routing)
6. [Forms](#forms)
7. [RxJS and Observables](#rxjs-and-observables)
8. [HTTP Client](#http-client)
9. [Directives](#directives)
10. [Pipes](#pipes)
11. [Lifecycle Hooks](#lifecycle-hooks)
12. [Change Detection](#change-detection)
13. [Testing](#testing)

---

## Architecture Overview

Angular is a platform and framework for building single-page client applications using TypeScript.

### Core Concepts

```
Module          → Organizes related components, services, and directives
Component       → Controls a portion of the screen (template + class)
Service         → Shared business logic and data
Directive       → Extends HTML behavior
Pipe            → Transforms displayed values
Dependency Injection → Provides services to components
RxJS            → Reactive programming with Observables
```

### Project Structure

```
src/
├── app/
│   ├── core/
│   │   ├── guards/
│   │   │   └── auth.guard.ts
│   │   ├── interceptors/
│   │   │   └── auth.interceptor.ts
│   │   ├── services/
│   │   │   └── auth.service.ts
│   │   └── core.module.ts
│   ├── shared/
│   │   ├── components/
│   │   │   ├── button/
│   │   │   └── modal/
│   │   ├── directives/
│   │   ├── pipes/
│   │   └── shared.module.ts
│   ├── features/
│   │   ├── auth/
│   │   │   ├── auth.module.ts
│   │   │   ├── login/
│   │   │   └── register/
│   │   ├── dashboard/
│   │   │   ├── dashboard.module.ts
│   │   │   ├── dashboard.component.ts
│   │   │   ├── dashboard.component.html
│   │   │   └── dashboard.component.spec.ts
│   │   └── users/
│   ├── app.component.ts
│   ├── app.component.html
│   ├── app.module.ts
│   └── app.routes.ts
├── environments/
│   ├── environment.ts
│   └── environment.prod.ts
├── assets/
├── styles.scss
└── main.ts
```

---

## Components

### Basic Component

```typescript
// user-profile.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-user-profile',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent {
    @Input() user: any;
    @Input() editable = false;
    @Output() userUpdated = new EventEmitter<any>();

    onSave() {
        this.userUpdated.emit(this.user);
    }
}
```

```html
<!-- user-profile.component.html -->
<div class="profile-card">
    <img [src]="user.avatar" [alt]="user.name">
    <h2>{{ user.name }}</h2>
    <p>{{ user.email }}</p>

    @if (editable) {
        <button (click)="onSave()">Save Changes</button>
    }
</div>
```

### Component with Template Syntax

```typescript
@Component({
    selector: 'app-demo',
    standalone: true,
    template: `
        <!-- Property binding -->
        <img [src]="imageUrl" [alt]="imageName">
        <input [disabled]="isDisabled" [value]="name">

        <!-- Event binding -->
        <button (click)="handleClick()">Click me</button>
        <input (input)="onInput($event)" (keyup.enter)="onSubmit()">
        <form (submit)="onSubmit()">

        <!-- Two-way binding -->
        <input [(ngModel)]="name">

        <!-- Binding shorthand -->
        <div [class.active]="isActive">Class binding</div>
        <div [style.color]="textColor">Style binding</div>

        <!-- Template references -->
        <input #emailInput>
        <button (click)="logEmail(emailInput.value)">Log</button>

        <!-- Template variables -->
        @for (item of items; track item.id) {
            <div>{{ item.name }}</div>
        }

        <!-- Conditional display -->
        @if (isLoggedIn) {
            <p>Welcome back!</p>
        } @else {
            <p>Please log in</p>
        }

        @switch (role) {
            @case ('admin') { <p>Admin panel</p> }
            @case ('user') { <p>User dashboard</p> }
            @default { <p>Unknown role</p> }
        }
    `
})
export class DemoComponent {
    name = 'Angular';
    isDisabled = false;
    isActive = true;
    textColor = 'blue';
    isLoggedIn = true;
    role = 'admin';
    items = [
        { id: 1, name: 'Item 1' },
        { id: 2, name: 'Item 2' }
    ];

    handleClick() {
        console.log('Clicked!');
    }

    onInput(event: Event) {
        const target = event.target as HTMLInputElement;
        console.log(target.value);
    }

    onSubmit() {
        console.log('Form submitted');
    }

    logEmail(email: string) {
        console.log(email);
    }
}
```

### Content Projection

```typescript
@Component({
    selector: 'app-card',
    standalone: true,
    template: `
        <div class="card">
            <div class="card-header">
                <ng-content select="[card-header]"></ng-content>
            </div>
            <div class="card-body">
                <ng-content></ng-content>
            </div>
            <div class="card-footer">
                <ng-content select="[card-footer]"></ng-content>
            </div>
        </div>
    `
})
export class CardComponent {}

// Usage
// <app-card>
//     <h2 card-header>Title</h2>
//     <p>Body content</p>
//     <button card-footer>Action</button>
// </app-card>
```

---

## Modules

### Feature Module

```typescript
// dashboard.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { StatsComponent } from './stats.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
    declarations: [
        DashboardComponent,
        StatsComponent
    ],
    imports: [
        CommonModule,
        DashboardRoutingModule,
        SharedModule
    ]
})
export class DashboardModule {}
```

### Shared Module

```typescript
// shared.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ButtonComponent } from './components/button/button.component';
import { ModalComponent } from './components/modal/modal.component';
import { HighlightDirective } from './directives/highlight.directive';
import { TruncatePipe } from './pipes/truncate.pipe';

@NgModule({
    declarations: [
        ButtonComponent,
        ModalComponent,
        HighlightDirective,
        TruncatePipe
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule
    ],
    exports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ButtonComponent,
        ModalComponent,
        HighlightDirective,
        TruncatePipe
    ]
})
export class SharedModule {}
```

### Core Module (Singleton Services)

```typescript
// core.module.ts
import { NgModule, Optional, SkipSelf } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthService } from './services/auth.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';

@NgModule({
    providers: [
        AuthService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        }
    ]
})
export class CoreModule {
    constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
        if (parentModule) {
            throw new Error('CoreModule is already loaded. Import it in the AppModule only.');
        }
    }
}
```

---

## Services and Dependency Injection

### Service

```typescript
// auth.service.ts
import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject, tap } from 'rxjs';

interface User {
    id: number;
    name: string;
    email: string;
    role: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = '/api/auth';
    private currentUserSubject = new BehaviorSubject<User | null>(null);

    currentUser$ = this.currentUserSubject.asObservable();

    // Signals (Angular 16+)
    isLoggedIn = signal(false);
    user = signal<User | null>(null);

    displayName = computed(() => this.user()?.name ?? 'Guest');

    constructor(
        private http: HttpClient,
        private router: Router
    ) {
        this.loadUser();
    }

    private loadUser(): void {
        const userData = localStorage.getItem('user');
        if (userData) {
            const user = JSON.parse(userData);
            this.currentUserSubject.next(user);
            this.user.set(user);
            this.isLoggedIn.set(true);
        }
    }

    login(email: string, password: string): Observable<User> {
        return this.http.post<User>(`${this.apiUrl}/login`, { email, password })
            .pipe(
                tap(user => {
                    localStorage.setItem('user', JSON.stringify(user));
                    this.currentUserSubject.next(user);
                    this.user.set(user);
                    this.isLoggedIn.set(true);
                })
            );
    }

    logout(): void {
        localStorage.removeItem('user');
        this.currentUserSubject.next(null);
        this.user.set(null);
        this.isLoggedIn.set(false);
        this.router.navigate(['/login']);
    }

    isAuthenticated(): boolean {
        return this.isLoggedIn();
    }

    hasRole(role: string): boolean {
        return this.user()?.role === role;
    }
}
```

### Multi-Provider Pattern

```typescript
// Logger service with multiple implementations
export abstract class Logger {
    abstract log(message: string): void;
    abstract error(message: string): void;
}

@Injectable()
export class ConsoleLogger extends Logger {
    log(message: string) {
        console.log(`[LOG] ${message}`);
    }
    error(message: string) {
        console.error(`[ERROR] ${message}`);
    }
}

@Injectable()
export class RemoteLogger extends Logger {
    constructor(private http: HttpClient) { super(); }

    log(message: string) {
        this.http.post('/api/logs', { level: 'info', message }).subscribe();
    }
    error(message: string) {
        this.http.post('/api/logs', { level: 'error', message }).subscribe();
    }
}

// Provider setup
providers: [
    {
        provide: Logger,
        useClass: environment.production ? RemoteLogger : ConsoleLogger
    }
]

// Factory provider
providers: [
    {
        provide: API_URL,
        useFactory: () => {
            return environment.production
                ? 'https://api.production.com'
                : 'http://localhost:3000';
        }
    }
]
```

---

## Routing

### Route Configuration

```typescript
// app.routes.ts
import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
    },
    {
        path: 'login',
        loadComponent: () => import('./features/auth/login/login.component')
            .then(m => m.LoginComponent)
    },
    {
        path: 'dashboard',
        canActivate: [authGuard],
        loadComponent: () => import('./features/dashboard/dashboard.component')
            .then(m => m.DashboardComponent)
    },
    {
        path: 'admin',
        canActivate: [authGuard, roleGuard],
        data: { role: 'admin' },
        children: [
            {
                path: '',
                loadComponent: () => import('./features/admin/admin.component')
                    .then(m => m.AdminComponent)
            },
            {
                path: 'users',
                loadComponent: () => import('./features/admin/users/users.component')
                    .then(m => m.UsersComponent)
            },
            {
                path: 'settings',
                loadComponent: () => import('./features/admin/settings/settings.component')
                    .then(m => m.SettingsComponent)
            }
        ]
    },
    {
        path: 'users/:id',
        canActivate: [authGuard],
        loadComponent: () => import('./features/users/user-detail/user-detail.component')
            .then(m => m.UserDetailComponent)
    },
    {
        path: '**',
        loadComponent: () => import('./shared/components/not-found/not-found.component')
            .then(m => m.NotFoundComponent)
    }
];
```

### Route Guards

```typescript
// auth.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.isAuthenticated()) {
        return true;
    }

    router.navigate(['/login'], {
        queryParams: { returnUrl: state.url }
    });
    return false;
};

// role.guard.ts
export const roleGuard: CanActivateFn = (route) => {
    const authService = inject(AuthService);
    const requiredRole = route.data['role'];

    return authService.hasRole(requiredRole);
};

// can-deactivate guard
export const canDeactivateGuard: CanDeactivateFn<any> = (component) => {
    if (component.hasUnsavedChanges) {
        return confirm('You have unsaved changes. Do you want to leave?');
    }
    return true;
};
```

### Navigation in Components

```typescript
@Component({
    selector: 'app-nav',
    template: `
        <nav>
            <a routerLink="/dashboard"
               routerLinkActive="active"
               [routerLinkActiveOptions]="{ exact: true }">
                Dashboard
            </a>
            <a routerLink="/users"
               routerLinkActive="active">
                Users
            </a>
            <a [routerLink]="['/users', userId]"
               [queryParams]="{ tab: 'profile' }">
                My Profile
            </a>
        </nav>
    `
})
export class NavComponent {
    userId = 1;

    constructor(private router: Router, private route: ActivatedRoute) {}

    navigateToUser(id: number) {
        this.router.navigate(['/users', id], {
            queryParams: { tab: 'settings' },
            fragment: 'section2'
        });
    }

    getCurrentRoute() {
        this.route.url.subscribe(url => console.log(url));
        this.route.paramMap.subscribe(params => {
            const id = params.get('id');
        });
        this.route.queryParamMap.subscribe(params => {
            const tab = params.get('tab');
        });
    }
}
```

---

## Forms

### Template-Driven Forms

```typescript
@Component({
    selector: 'app-login',
    template: `
        <form #loginForm="ngForm" (ngSubmit)="onSubmit(loginForm)">
            <div>
                <label for="email">Email</label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    [(ngModel)]="credentials.email"
                    required
                    email
                    #email="ngModel"
                >
                @if (email.invalid && email.touched) {
                    <span class="error">
                        @if (email.errors?.['required']) {
                            Email is required
                        } @else if (email.errors?.['email']) {
                            Invalid email format
                        }
                    </span>
                }
            </div>

            <div>
                <label for="password">Password</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    [(ngModel)]="credentials.password"
                    required
                    minlength="8"
                    #password="ngModel"
                >
                @if (password.invalid && password.touched) {
                    <span class="error">Password must be at least 8 characters</span>
                }
            </div>

            <button type="submit" [disabled]="loginForm.invalid">Login</button>
        </form>
    `
})
export class LoginComponent {
    credentials = { email: '', password: '' };

    onSubmit(form: NgForm) {
        if (form.valid) {
            console.log(this.credentials);
        }
    }
}
```

### Reactive Forms

```typescript
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    template: `
        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
            <div formGroupName="personalInfo">
                <div>
                    <label>First Name</label>
                    <input formControlName="firstName">
                    @if (registerForm.get('personalInfo.firstName')?.invalid &&
                         registerForm.get('personalInfo.firstName')?.touched) {
                        <span class="error">First name is required</span>
                    }
                </div>

                <div>
                    <label>Last Name</label>
                    <input formControlName="lastName">
                </div>
            </div>

            <div>
                <label>Email</label>
                <input formControlName="email" type="email">
                @if (registerForm.get('email')?.errors?.['required'] &&
                     registerForm.get('email')?.touched) {
                    <span class="error">Email is required</span>
                } @else if (registerForm.get('email')?.errors?.['email'] &&
                           registerForm.get('email')?.touched) {
                    <span class="error">Invalid email</span>
                }
            </div>

            <div formArrayName="skills">
                <h3>Skills</h3>
                @for (skill of skills.controls; track $index; let i = $index) {
                    <div>
                        <input [formControlName]="i" placeholder="Skill {{ i + 1 }}">
                        <button type="button" (click)="removeSkill(i)">Remove</button>
                    </div>
                }
                <button type="button" (click)="addSkill()">Add Skill</button>
            </div>

            <div>
                <label>
                    <input type="checkbox" formControlName="agreeTerms">
                    I agree to the terms
                </label>
            </div>

            <button type="submit" [disabled]="registerForm.invalid">Register</button>

            <pre>{{ registerForm.value | json }}</pre>
        </form>
    `
})
export class RegisterComponent implements OnInit {
    registerForm!: FormGroup;

    constructor(private fb: FormBuilder) {}

    ngOnInit() {
        this.registerForm = this.fb.group({
            personalInfo: this.fb.group({
                firstName: ['', Validators.required],
                lastName: ['']
            }),
            email: ['', [Validators.required, Validators.email]],
            skills: this.fb.array([
                this.fb.control('', Validators.required)
            ]),
            agreeTerms: [false, Validators.requiredTrue]
        });
    }

    get skills() {
        return this.registerForm.get('skills') as FormArray;
    }

    addSkill() {
        this.skills.push(this.fb.control('', Validators.required));
    }

    removeSkill(index: number) {
        this.skills.removeAt(index);
    }

    onSubmit() {
        if (this.registerForm.valid) {
            console.log(this.registerForm.value);
        } else {
            this.registerForm.markAllAsTouched();
        }
    }
}
```

### Custom Validators

```typescript
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// Simple validator
export function noWhitespaceValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const isWhitespace = (control.value || '').trim().length === 0;
        return isWhitespace ? { whitespace: true } : null;
    };
}

// Async validator
export function uniqueEmailValidator(authService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) => {
        return authService.checkEmailUnique(control.value).pipe(
            map(isUnique => isUnique ? null : { emailTaken: true }),
            catchError(() => of(null))
        );
    };
}

// Cross-field validator
export function passwordMatchValidator(): ValidatorFn {
    return (group: AbstractControl): ValidationErrors | null => {
        const password = group.get('password')?.value;
        const confirmPassword = group.get('confirmPassword')?.value;
        return password === confirmPassword ? null : { passwordMismatch: true };
    };
}

// Usage
this.registerForm = this.fb.group({
    email: ['', [Validators.required, Validators.email], [uniqueEmailValidator(this.authService)]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    confirmPassword: ['']
}, { validators: passwordMatchValidator() });
```

---

## RxJS and Observables

### Common Operators

```typescript
import { Observable, of, from, BehaviorSubject, Subject } from 'rxjs';
import {
    map, filter, switchMap, mergeMap, concatMap,
    debounceTime, distinctUntilChanged, tap,
    catchError, retry, takeUntil, take,
    combineLatest, forkJoin, zip,
    startWith, withLatestFrom
} from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class SearchService {
    private searchSubject = new Subject<string>();

    // Debounced search
    search$ = this.searchSubject.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(term => term.length >= 2),
        switchMap(term => this.http.get(`/api/search?q=${term}`)),
        catchError(error => {
            console.error('Search error:', error);
            return of([]);
        })
    );

    search(term: string) {
        this.searchSubject.next(term);
    }
}

// Component usage
@Component({
    selector: 'app-search',
    template: `
        <input #searchInput (input)="onSearch(searchInput.value)">
        @if (loading) {
            <spinner></spinner>
        }
        <ul>
            @for (result of results$ | async; track result.id) {
                <li>{{ result.name }}</li>
            }
        </ul>
    `
})
export class SearchComponent implements OnInit, OnDestroy {
    results$?: Observable<any[]>;
    loading = false;
    private destroy$ = new Subject<void>();

    constructor(private searchService: SearchService) {}

    ngOnInit() {
        this.results$ = this.searchService.search$.pipe(
            tap(() => this.loading = true),
            switchMap(results => {
                this.loading = false;
                return of(results);
            })
        );
    }

    onSearch(term: string) {
        this.searchService.search(term);
    }

    ngOnDestroy() {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
```

### Combining Streams

```typescript
// forkJoin - waits for all to complete
const combined$ = forkJoin({
    users: this.http.get('/api/users'),
    posts: this.http.get('/api/posts'),
    comments: this.http.get('/api/comments')
});

combined$.subscribe(({ users, posts, comments }) => {
    console.log(users, posts, comments);
});

// combineLatest - emits when any source emits
const form$ = combineLatest([
    this.nameInput.valueChanges,
    this.emailInput.valueChanges,
    this.ageInput.valueChanges
]).pipe(
    map(([name, email, age]) => ({ name, email, age }))
);

// withLatestFrom
this.save$.pipe(
    withLatestFrom(this.formData$),
    switchMap(([_, formData]) => this.http.post('/api/save', formData))
).subscribe();
```

---

## HTTP Client

```typescript
import { HttpClient, HttpHeaders, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

interface User {
    id: number;
    name: string;
    email: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
    private apiUrl = '/api/users';

    constructor(private http: HttpClient) {}

    // GET with params
    getUsers(page = 1, limit = 10): Observable<User[]> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('limit', limit.toString());

        return this.http.get<User[]>(this.apiUrl, { params }).pipe(
            retry(2),
            catchError(this.handleError)
        );
    }

    // GET single resource
    getUser(id: number): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/${id}`).pipe(
            catchError(this.handleError)
        );
    }

    // POST
    createUser(user: Partial<User>): Observable<User> {
        return this.http.post<User>(this.apiUrl, user).pipe(
            catchError(this.handleError)
        );
    }

    // PUT
    updateUser(id: number, user: Partial<User>): Observable<User> {
        return this.http.put<User>(`${this.apiUrl}/${id}`, user).pipe(
            catchError(this.handleError)
        );
    }

    // PATCH
    patchUser(id: number, changes: Partial<User>): Observable<User> {
        return this.http.patch<User>(`${this.apiUrl}/${id}`, changes).pipe(
            catchError(this.handleError)
        );
    }

    // DELETE
    deleteUser(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
            catchError(this.handleError)
        );
    }

    // File upload
    uploadFile(file: File): Observable<{ url: string }> {
        const formData = new FormData();
        formData.append('file', file);

        return this.http.post<{ url: string }>('/api/upload', formData, {
            reportProgress: true,
            observe: 'body'
        });
    }

    private handleError(error: HttpErrorResponse) {
        let errorMessage = 'An error occurred';

        if (error.error instanceof ErrorEvent) {
            errorMessage = error.error.message;
        } else {
            errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }

        console.error(errorMessage);
        return throwError(() => new Error(errorMessage));
    }
}
```

---

## Directives

### Structural Directives

```typescript
@Directive({
    selector: '[appRepeat]',
    standalone: true
})
export class RepeatDirective {
    @Input() appRepeat = 0;

    constructor(
        templateRef: TemplateRef<any>,
        viewContainerRef: ViewContainerRef
    ) {
        for (let i = 0; i < this.appRepeat; i++) {
            viewContainerRef.createEmbeddedView(templateRef, {
                $implicit: i,
                index: i,
                first: i === 0,
                last: i === this.appRepeat - 1,
                even: i % 2 === 0,
                odd: i % 2 !== 0
            });
        }
    }
}

// Usage: <div *appRepeat="5; let i">Item {{ i }}</div>
```

### Attribute Directives

```typescript
@Directive({
    selector: '[appHighlight]',
    standalone: true
})
export class HighlightDirective {
    @Input() appHighlight = 'yellow';
    @Input() highlightTextColor = 'black';

    constructor(private el: ElementRef, private renderer: Renderer2) {}

    @HostListener('mouseenter') onMouseEnter() {
        this.highlight(this.appHighlight, this.highlightTextColor);
    }

    @HostListener('mouseleave') onMouseLeave() {
        this.highlight('', '');
    }

    private highlight(bgColor: string, textColor: string) {
        this.renderer.setStyle(this.el.nativeElement, 'background-color', bgColor);
        this.renderer.setStyle(this.el.nativeElement, 'color', textColor);
    }
}

// Usage: <p [appHighlight]="'lightblue'" [highlightTextColor]="'darkblue'">Hover me</p>
```

---

## Pipes

```typescript
@Pipe({
    name: 'truncate',
    standalone: true
})
export class TruncatePipe implements PipeTransform {
    transform(value: string, limit = 50, trail = '...'): string {
        if (!value) return '';
        return value.length > limit
            ? value.substring(0, limit) + trail
            : value;
    }
}

@Pipe({
    name: 'timeAgo',
    standalone: true
})
export class TimeAgoPipe implements PipeTransform {
    transform(value: Date | string): string {
        const date = new Date(value);
        const now = new Date();
        const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

        const intervals: [number, string][] = [
            [31536000, 'year'],
            [2592000, 'month'],
            [604800, 'week'],
            [86400, 'day'],
            [3600, 'hour'],
            [60, 'minute'],
            [1, 'second']
        ];

        for (const [secondsInUnit, unit] of intervals) {
            const interval = Math.floor(seconds / secondsInUnit);
            if (interval >= 1) {
                return `${interval} ${unit}${interval > 1 ? 's' : ''} ago`;
            }
        }

        return 'just now';
    }
}

@Pipe({
    name: 'filter',
    standalone: true
})
export class FilterPipe implements PipeTransform {
    transform(items: any[], field: string, value: any): any[] {
        if (!items || !field || value === undefined) return items;
        return items.filter(item => item[field] === value);
    }
}

// Usage
// {{ text | truncate:30 }}
// {{ date | timeAgo }}
// {{ users | filter:'role':'admin' | json }}
```

---

## Lifecycle Hooks

```typescript
@Component({
    selector: 'app-lifecycle',
    template: `<p>{{ message }}</p>`
})
export class LifecycleComponent implements OnInit, OnChanges,
    AfterViewInit, AfterContentInit, OnDestroy {

    @Input() name = '';

    message = '';
    private destroy$ = new Subject<void>();

    constructor() {
        console.log('constructor');
    }

    ngOnChanges(changes: SimpleChanges) {
        console.log('ngOnChanges', changes);
        if (changes['name']) {
            this.message = `Name changed to ${changes['name'].currentValue}`;
        }
    }

    ngOnInit() {
        console.log('ngOnInit');
        // Initialize component, fetch data
        this.loadData().pipe(
            takeUntil(this.destroy$)
        ).subscribe(data => {
            this.message = data;
        });
    }

    ngAfterViewInit() {
        console.log('ngAfterViewInit');
        // Access child view elements
    }

    ngAfterContentInit() {
        console.log('ngAfterContentInit');
        // Access projected content
    }

    ngOnDestroy() {
        console.log('ngOnDestroy');
        this.destroy$.next();
        this.destroy$.complete();
    }
}
```

---

## Change Detection

```typescript
import { Component, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';

@Component({
    selector: 'app-optimized',
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: `
        <div>{{ data | json }}</div>
        <button (click)="update()">Update</button>
    `
})
export class OptimizedComponent {
    data = { items: [1, 2, 3] };

    constructor(private cdr: ChangeDetectorRef) {}

    update() {
        // OnPush requires manual detection or immutable updates
        this.data = { ...this.data, items: [...this.data.items, 4] };
        // or
        this.cdr.markForCheck();
    }

    // Async pipe handles change detection automatically
    // data$ = this.http.get('/api/data');
}
```

---

## Angular Best Practices

1. Use standalone components for new Angular projects (v15+)
2. Use signals for reactive state management (Angular 16+)
3. Prefer `OnPush` change detection strategy for performance
4. Lazy load feature modules with `loadComponent` or `loadChildren`
5. Use the `async` pipe to handle subscriptions and prevent memory leaks
6. Implement proper error handling with interceptors
7. Use `trackBy` in `@for` loops for better performance
8. Keep components small — extract logic into services
9. Use TypeScript interfaces for type safety
10. Follow the Angular style guide conventions

# Vue.js Complete Guide

## Table of Contents

1. [Core Concepts](#core-concepts)
2. [Components](#components)
3. [Reactivity System](#reactivity-system)
4. [Computed Properties and Watchers](#computed-properties-and-watchers)
5. [Lifecycle Hooks](#lifecycle-hooks)
6. [Template Syntax](#template-syntax)
7. [Events and Handling User Input](#events-and-handling-user-input)
8. [Forms](#forms)
9. [Vue Router](#vue-router)
10. [State Management (Pinia)](#state-management-pinia)
11. [Composables](#composables)
12. [Performance Optimization](#performance-optimization)

---

## Core Concepts

### Single File Components (SFC)

```vue
<script setup>
import { ref, computed } from 'vue'

const message = ref('Hello Vue!')
const count = ref(0)

const doubleCount = computed(() => count.value * 2)

function increment() {
    count.value++
}
</script>

<template>
    <div class="counter">
        <h1>{{ message }}</h1>
        <p>Count: {{ count }}</p>
        <p>Double: {{ doubleCount }}</p>
        <button @click="increment">+1</button>
    </div>
</template>

<style scoped>
.counter {
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
}

button {
    background: #42b883;
    color: white;
    border: none;
    padding: 8px 16px;
    border-radius: 4px;
    cursor: pointer;
}
</style>
```

### Project Structure

```
src/
  assets/
    images/
    styles/
  components/
    common/
      AppButton.vue
      AppInput.vue
      AppModal.vue
    features/
      auth/
        LoginForm.vue
        RegisterForm.vue
      dashboard/
        DashboardView.vue
        StatsCard.vue
  composables/
    useAuth.ts
    useDebounce.ts
    useFetch.ts
  stores/
    auth.ts
    cart.ts
    ui.ts
  router/
    index.ts
  types/
    index.ts
  App.vue
  main.ts
```

### App Entry

```typescript
// main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.config.errorHandler = (err, instance, info) => {
    console.error('Global error:', err, info)
}

app.mount('#app')
```

---

## Components

### Basic Component with Props and Emits

```vue
<!-- Greeting.vue -->
<script setup>
import { computed } from 'vue'

const props = defineProps({
    name: {
        type: String,
        required: true
    },
    count: {
        type: Number,
        default: 0
    },
    user: {
        type: Object,
        default: () => ({})
    }
})

const emit = defineEmits(['update', 'delete'])

const displayName = computed(() => {
    return props.name || 'Guest'
})

function handleUpdate() {
    emit('update', { name: displayName.value, timestamp: Date.now() })
}

function handleDelete() {
    emit('delete')
}
</script>

<template>
    <div class="greeting">
        <h2>Hello, {{ displayName }}!</h2>
        <p>Count: {{ count }}</p>
        <slot></slot>
        <div class="actions">
            <button @click="handleUpdate">Update</button>
            <button @click="handleDelete">Delete</button>
        </div>
    </div>
</template>
```

### Component Communication

```vue
<!-- Parent.vue -->
<script setup>
import { ref } from 'vue'
import ChildComponent from './ChildComponent.vue'

const parentMessage = ref('Hello from parent')
const receivedData = ref(null)

function handleChildEvent(data) {
    receivedData.value = data
}
</script>

<template>
    <div>
        <ChildComponent
            :message="parentMessage"
            :items="[1, 2, 3]"
            @child-event="handleChildEvent"
        >
            <template #header>
                <h3>Custom Header</h3>
            </template>

            <template #default>
                <p>Default slot content</p>
            </template>

            <template #footer>
                <footer>Custom Footer</footer>
            </template>
        </ChildComponent>

        <p v-if="receivedData">Received: {{ receivedData }}</p>
    </div>
</template>
```

### Provide and Inject

```vue
<!-- Parent.vue -->
<script setup>
import { provide, ref } from 'vue'

const theme = ref('light')
const toggleTheme = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light'
}

provide('theme', theme)
provide('toggleTheme', toggleTheme)
provide('appName', 'My Application')
</script>

<!-- DeepChild.vue -->
<script setup>
import { inject } from 'vue'

const theme = inject('theme')
const toggleTheme = inject('toggleTheme')
const appName = inject('appName', 'Default App')
</script>

<template>
    <div :class="theme">
        <p>{{ appName }}</p>
        <button @click="toggleTheme">
            Current: {{ theme }}
        </button>
    </div>
</template>
```

---

## Reactivity System

### Refs

```vue
<script setup>
import { ref, shallowRef, triggerRef } from 'vue'

// Deep reactive reference
const count = ref(0)
const user = ref({ name: 'John', scores: [10, 20, 30] })

// Mutate
count.value++
user.value.name = 'Jane'
user.value.scores.push(40)

// Shallow reactive (only top-level)
const shallow = shallowRef({ nested: { value: 1 } })

// Force trigger on shallow ref
function updateShallow() {
    shallow.value.nested.value++
    triggerRef(shallow)
}
</script>
```

### Reactive

```vue
<script setup>
import { reactive, toRefs, toRef } from 'vue'

const state = reactive({
    count: 0,
    user: {
        name: 'John',
        email: 'john@example.com'
    },
    items: []
})

// Mutate directly
state.count++
state.user.name = 'Jane'
state.items.push({ id: 1, text: 'New item' })

// Destructure while maintaining reactivity
const { count, user } = toRefs(state)

// Single property ref
const userName = toRef(state, 'userName')
</script>

<template>
    <p>{{ state.count }}</p>
    <p>{{ count }}</p>
</template>
```

### Computed Properties

```vue
<script setup>
import { ref, computed } from 'vue'

const firstName = ref('John')
const lastName = ref('Doe')
const items = ref([
    { id: 1, name: 'Item 1', price: 10, quantity: 2 },
    { id: 2, name: 'Item 2', price: 20, quantity: 1 }
])

// Read-only computed
const fullName = computed(() => `${firstName.value} ${lastName.value}`)

// Writable computed
const fullNameWritable = computed({
    get: () => `${firstName.value} ${lastName.value}`,
    set: (newValue) => {
        const [first, last] = newValue.split(' ')
        firstName.value = first
        lastName.value = last
    }
})

// Computed with complex logic
const total = computed(() => {
    return items.value.reduce((sum, item) => {
        return sum + (item.price * item.quantity)
    }, 0)
})

const sortedItems = computed(() => {
    return [...items.value].sort((a, b) => a.price - b.price)
})
</script>

<template>
    <input v-model="firstName" placeholder="First name">
    <input v-model="lastName" placeholder="Last name">
    <p>Full name: {{ fullName }}</p>
    <input v-model="fullNameWritable" placeholder="Full name">
    <p>Total: ${{ total }}</p>
</template>
```

### Watchers

```vue
<script setup>
import { ref, watch, watchEffect } from 'vue'

const searchQuery = ref('')
const selectedCategory = ref('all')
const deepObject = ref({ nested: { value: '' } })

// Basic watcher
watch(searchQuery, (newVal, oldVal) => {
    console.log(`Search changed from "${oldVal}" to "${newVal}"`)
})

// Watch multiple sources
watch(
    [searchQuery, selectedCategory],
    ([newQuery, newCategory], [oldQuery, oldCategory]) => {
        console.log('Query:', newQuery, 'Category:', newCategory)
        fetchResults()
    }
)

// Deep watcher
watch(
    deepObject,
    (newVal) => {
        console.log('Object changed:', JSON.stringify(newVal))
    },
    { deep: true, immediate: false }
)

// watchEffect - auto tracks dependencies
watchEffect(() => {
    console.log(`Searching: ${searchQuery.value} in ${selectedCategory.value}`)
})

// One-time watcher
const stopWatch = watch(searchQuery, (newVal) => {
    console.log('First change:', newVal)
    stopWatch()
})
</script>
```

---

## Lifecycle Hooks

```vue
<script setup>
import {
    ref,
    onBeforeMount,
    onMounted,
    onBeforeUpdate,
    onUpdated,
    onBeforeUnmount,
    onUnmounted,
    onErrorCaptured
} from 'vue'

const data = ref(null)

onBeforeMount(() => {
    console.log('Component is about to mount')
})

onMounted(async () => {
    console.log('Component is mounted')
    try {
        const response = await fetch('/api/data')
        data.value = await response.json()
    } catch (error) {
        console.error('Failed to fetch:', error)
    }
    const interval = setInterval(() => {}, 1000)
    onUnmounted(() => {
        clearInterval(interval)
    })
})

onBeforeUpdate(() => {
    console.log('State is about to update')
})

onUpdated(() => {
    console.log('DOM has been updated')
})

onBeforeUnmount(() => {
    console.log('Component is about to unmount')
})

onUnmounted(() => {
    console.log('Component has been unmounted')
})

onErrorCaptured((err, instance, info) => {
    console.error('Error captured:', err)
    return false
})
</script>
```

---

## Template Syntax

```vue
<template>
    <!-- Text interpolation -->
    <p>{{ message }}</p>
    <p>{{ count + 1 }}</p>
    <p>{{ ok ? 'Yes' : 'No' }}</p>

    <!-- Raw HTML -->
    <p v-html="rawHtml"></p>

    <!-- Attribute binding -->
    <div :id="dynamicId" :class="activeClass" :style="dynamicStyle"></div>

    <!-- Boolean attributes -->
    <button :disabled="isDisabled">Submit</button>

    <!-- Dynamic arguments -->
    <a :[attributeName]="url">Link</a>
    <button @[eventName]="handler">Click</button>
</template>
```

### Conditional Rendering

```vue
<template>
    <div v-if="isLoggedIn">
        <p>Welcome back, {{ user.name }}!</p>
    </div>

    <div v-else-if="isGuest">
        <p>Welcome, guest user!</p>
    </div>

    <div v-else>
        <p>Please log in</p>
    </div>

    <template v-if="showSection">
        <h2>Section Title</h2>
        <p>Content</p>
    </template>

    <div v-show="isVisible">Always in DOM, toggled with CSS</div>
</template>
```

### List Rendering

```vue
<template>
    <ul>
        <li v-for="item in items" :key="item.id">
            {{ item.name }}
        </li>
    </ul>

    <ul>
        <li v-for="(item, index) in items" :key="item.id">
            {{ index + 1 }}. {{ item.name }}
        </li>
    </ul>

    <div v-for="(value, key, index) in user" :key="key">
        {{ index }}. {{ key }}: {{ value }}
    </div>

    <span v-for="n in 10" :key="n">{{ n }}</span>

    <div v-for="category in categories" :key="category.id">
        <h3>{{ category.name }}</h3>
        <ul>
            <li v-for="item in category.items" :key="item.id">
                {{ item.name }}
            </li>
        </ul>
    </div>
</template>
```

---

## Events and Handling User Input

```vue
<template>
    <button @click="count++">Click me</button>
    <button @click="handleClick">Click me</button>
    <button @click="handleClick($event, 'extra')">Click</button>

    <form @submit.prevent="onSubmit">
        <input @keyup.enter="submit">
        <div @click.self="handleClick">Only direct clicks</div>
        <a @click.prevent="handleLink">Link</a>
        <div @click.once="handleOnce">Only once</div>
        <input @keyup.ctrl.enter="submit">
    </form>

    <input @keyup.enter="submit">
    <input @keyup.esc="cancel">
    <input @keyup.delete="clear">
    <input @keyup.ctrl.s="save">

    <div @scroll.passive="handleScroll">
    <div @click.capture="handleCapture">
</template>
```

---

## Forms

```vue
<template>
    <form @submit.prevent="handleSubmit">
        <div class="form-group">
            <label for="name">Name</label>
            <input id="name" v-model="form.name" type="text" required>
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input id="email" v-model="form.email" type="email">
        </div>

        <div class="form-group">
            <label for="bio">Bio</label>
            <textarea id="bio" v-model="form.bio" rows="4"></textarea>
        </div>

        <div class="form-group">
            <label for="role">Role</label>
            <select id="role" v-model="form.role">
                <option value="" disabled>Select a role</option>
                <option value="user">User</option>
                <option value="admin">Admin</option>
            </select>
        </div>

        <div class="form-group">
            <label>
                <input type="checkbox" v-model="form.agreeTerms">
                I agree to the terms
            </label>
        </div>

        <div class="form-group">
            <label>Interests</label>
            <label>
                <input type="checkbox" v-model="form.interests" value="tech">
                Technology
            </label>
            <label>
                <input type="checkbox" v-model="form.interests" value="sports">
                Sports
            </label>
        </div>

        <div class="form-group">
            <label>Gender</label>
            <label>
                <input type="radio" v-model="form.gender" value="male">
                Male
            </label>
            <label>
                <input type="radio" v-model="form.gender" value="female">
                Female
            </label>
        </div>

        <div class="form-group">
            <label for="age">Age</label>
            <input id="age" v-model.number="form.age" type="number">
        </div>

        <div class="form-group">
            <label for="username">Username (trimmed)</label>
            <input id="username" v-model.trim="form.username" type="text">
        </div>

        <div class="form-group">
            <label for="lazy-input">Lazy input</label>
            <input id="lazy-input" v-model.lazy="form.lazyValue" type="text">
        </div>

        <pre>{{ form }}</pre>
        <button type="submit" :disabled="!isValid">Submit</button>
    </form>
</template>

<script setup>
import { reactive, computed } from 'vue'

const form = reactive({
    name: '',
    email: '',
    bio: '',
    role: '',
    agreeTerms: false,
    interests: [],
    gender: '',
    age: null,
    username: '',
    lazyValue: ''
})

const isValid = computed(() => {
    return form.name && form.email && form.agreeTerms
})

function handleSubmit() {
    if (isValid.value) {
        console.log('Form submitted:', { ...form })
    }
}
</script>
```

---

## Vue Router

```typescript
// router/index.ts
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/HomeView.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/LoginView.vue'),
        meta: { guest: true }
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { requiresAuth: true },
        children: [
            {
                path: '',
                name: 'DashboardHome',
                component: () => import('@/views/dashboard/HomeView.vue')
            },
            {
                path: 'settings',
                name: 'Settings',
                component: () => import('@/views/dashboard/SettingsView.vue')
            },
            {
                path: 'users/:id',
                name: 'UserDetail',
                component: () => import('@/views/dashboard/UserDetailView.vue'),
                props: true
            }
        ]
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('@/views/NotFoundView.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return { top: 0 }
        }
    }
})

router.beforeEach((to, from, next) => {
    const isAuthenticated = !!localStorage.getItem('token')

    if (to.meta.requiresAuth && !isAuthenticated) {
        next({ name: 'Login', query: { redirect: to.fullPath } })
    } else if (to.meta.guest && isAuthenticated) {
        next({ name: 'Dashboard' })
    } else {
        next()
    }
})

export default router
```

### Navigation in Components

```vue
<script setup>
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

console.log(route.params.id)
console.log(route.query.tab)

function navigateToUser(id) {
    router.push({
        name: 'UserDetail',
        params: { id },
        query: { tab: 'profile' }
    })
}

function goBack() {
    router.back()
}
</script>

<template>
    <nav>
        <router-link to="/" exact-active-class="active">Home</router-link>
        <router-link :to="{ name: 'Dashboard' }">Dashboard</router-link>
        <router-link :to="`/users/${userId}`">User</router-link>
    </nav>

    <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
            <component :is="Component" />
        </transition>
    </router-view>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
```

---

## State Management (Pinia)

```typescript
// stores/auth.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

interface User {
    id: number
    name: string
    email: string
    role: string
}

export const useAuthStore = defineStore('auth', () => {
    // State
    const user = ref<User | null>(null)
    const token = ref<string | null>(localStorage.getItem('token'))
    const loading = ref(false)
    const error = ref<string | null>(null)

    // Getters
    const isAuthenticated = computed(() => !!token.value && !!user.value)
    const displayName = computed(() => user.value?.name ?? 'Guest')
    const isAdmin = computed(() => user.value?.role === 'admin')

    // Actions
    async function login(email: string, password: string) {
        loading.value = true
        error.value = null

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            })

            if (!response.ok) {
                throw new Error('Login failed')
            }

            const data = await response.json()
            token.value = data.token
            user.value = data.user
            localStorage.setItem('token', data.token)
        } catch (err) {
            error.value = err.message
            throw err
        } finally {
            loading.value = false
        }
    }

    function logout() {
        token.value = null
        user.value = null
        localStorage.removeItem('token')
    }

    async function fetchUser() {
        if (!token.value) return

        loading.value = true
        try {
            const response = await fetch('/api/auth/me', {
                headers: { Authorization: `Bearer ${token.value}` }
            })
            user.value = await response.json()
        } catch {
            logout()
        } finally {
            loading.value = false
        }
    }

    return {
        user,
        token,
        loading,
        error,
        isAuthenticated,
        displayName,
        isAdmin,
        login,
        logout,
        fetchUser
    }
})
```

### Cart Store

```typescript
// stores/cart.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

interface CartItem {
    id: number
    name: string
    price: number
    quantity: number
}

export const useCartStore = defineStore('cart', () => {
    const items = ref<CartItem[]>([])

    const totalItems = computed(() =>
        items.value.reduce((sum, item) => sum + item.quantity, 0)
    )

    const totalPrice = computed(() =>
        items.value.reduce((sum, item) => sum + (item.price * item.quantity), 0)
    )

    function addItem(product: Omit<CartItem, 'quantity'>) {
        const existing = items.value.find(item => item.id === product.id)

        if (existing) {
            existing.quantity++
        } else {
            items.value.push({ ...product, quantity: 1 })
        }
    }

    function removeItem(productId: number) {
        const index = items.value.findIndex(item => item.id === productId)
        if (index > -1) {
            items.value.splice(index, 1)
        }
    }

    function updateQuantity(productId: number, quantity: number) {
        const item = items.value.find(item => item.id === productId)
        if (item) {
            item.quantity = quantity
        }
    }

    function clearCart() {
        items.value = []
    }

    return {
        items,
        totalItems,
        totalPrice,
        addItem,
        removeItem,
        updateQuantity,
        clearCart
    }
})
```

### Using Stores in Components

```vue
<script setup>
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'
import { storeToRefs } from 'pinia'

const authStore = useAuthStore()
const cartStore = useCartStore()

// Use storeToRefs for reactive state
const { user, isAuthenticated, displayName } = storeToRefs(authStore)

// Actions can be destructured directly
const { login, logout } = authStore

// Cart
const { items, totalItems, totalPrice } = storeToRefs(cartStore)
const { addItem, removeItem, clearCart } = cartStore
</script>

<template>
    <div v-if="isAuthenticated">
        <p>Welcome, {{ displayName }}</p>
        <button @click="logout">Logout</button>
    </div>
    <div v-else>
        <button @click="login('user@example.com', 'password')">
            Login
        </button>
    </div>

    <div>
        <p>Cart: {{ totalItems }} items - ${{ totalPrice }}</p>
        <ul>
            <li v-for="item in items" :key="item.id">
                {{ item.name }} x{{ item.quantity }}
                <button @click="removeItem(item.id)">Remove</button>
            </li>
        </ul>
    </div>
</template>
```

---

## Composables

```typescript
// composables/useFetch.ts
import { ref, watchEffect, toValue, type Ref } from 'vue'

interface UseFetchReturn<T> {
    data: Ref<T | null>
    error: Ref<string | null>
    loading: Ref<boolean>
    execute: () => Promise<void>
}

export function useFetch<T>(url: string | Ref<string>): UseFetchReturn<T> {
    const data = ref<T | null>(null) as Ref<T | null>
    const error = ref<string | null>(null)
    const loading = ref(false)

    async function execute() {
        loading.value = true
        error.value = null

        try {
            const response = await fetch(toValue(url))
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`)
            }
            data.value = await response.json()
        } catch (err) {
            error.value = err.message
        } finally {
            loading.value = false
        }
    }

    watchEffect(() => {
        toValue(url)  // track dependency
        execute()
    })

    return { data, error, loading, execute }
}

// composables/useDebounce.ts
import { ref, watch } from 'vue'

export function useDebounce<T>(value: Ref<T>, delay = 300): Ref<T> {
    const debouncedValue = ref(value.value) as Ref<T>
    let timeout: ReturnType<typeof setTimeout>

    watch(value, (newVal) => {
        clearTimeout(timeout)
        timeout = setTimeout(() => {
            debouncedValue.value = newVal
        }, delay)
    })

    return debouncedValue
}

// composables/useLocalStorage.ts
import { ref, watch } from 'vue'

export function useLocalStorage<T>(key: string, defaultValue: T) {
    const stored = localStorage.getItem(key)
    const data = ref<T>(stored ? JSON.parse(stored) : defaultValue) as Ref<T>

    watch(data, (newVal) => {
        localStorage.setItem(key, JSON.stringify(newVal))
    }, { deep: true })

    return data
}

// composables/useAuth.ts
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

export function useAuth() {
    const store = useAuthStore()

    const user = computed(() => store.user)
    const isAuthenticated = computed(() => store.isAuthenticated)
    const isAdmin = computed(() => store.isAdmin)

    async function login(email: string, password: string) {
        await store.login(email, password)
    }

    function logout() {
        store.logout()
    }

    return { user, isAuthenticated, isAdmin, login, logout }
}
```

### Using Composables

```vue
<script setup>
import { ref } from 'vue'
import { useFetch } from '@/composables/useFetch'
import { useDebounce } from '@/composables/useDebounce'
import { useLocalStorage } from '@/composables/useLocalStorage'

const searchQuery = ref('')
const debouncedQuery = useDebounce(searchQuery, 500)

const savedTheme = useLocalStorage('theme', 'light')

const { data: users, loading, error, execute } = useFetch('/api/users')
</script>

<template>
    <input v-model="searchQuery" placeholder="Search...">

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">Error: {{ error }}</div>
    <ul v-else>
        <li v-for="user in users" :key="user.id">{{ user.name }}</li>
    </ul>

    <button @click="execute">Refresh</button>
</template>
```

---

## Performance Optimization

```vue
<script setup>
import { ref, computed, shallowRef, triggerRef } from 'vue'

// Use shallowRef for large objects that don't need deep reactivity
const largeList = shallowRef([])

async function loadLargeList() {
    const response = await fetch('/api/large-dataset')
    largeList.value = await response.json()
    triggerRef(largeList)
}

// v-memo for conditional rendering
const sortBy = ref('name')
const filterQuery = ref('')

const processedItems = computed(() => {
    return items.value
        .filter(item => item.name.includes(filterQuery.value))
        .sort((a, b) => a[sortBy.value] > b[sortBy.value] ? 1 : -1)
})
</script>

<template>
    <!-- v-memo caches the rendered output -->
    <div v-for="item in processedItems" :key="item.id"
         v-memo="[item.id === selectedId]">
        <p :class="{ selected: item.id === selectedId }">
            {{ item.name }}
        </p>
    </div>

    <!-- Lazy loading images -->
    <img v-for="image in images" :key="image.id"
         :src="image.thumbnail"
         loading="lazy"
         :alt="image.alt">

    <!-- Keep-alive for cached components -->
    <keep-alive :include="['Dashboard', 'Settings']">
        <component :is="currentView" />
    </keep-alive>
</template>
```

### Async Components

```vue
<script setup>
import { defineAsyncComponent } from 'vue'

const HeavyChart = defineAsyncComponent(() =>
    import('./components/HeavyChart.vue')
)

const AdminPanel = defineAsyncComponent({
    loader: () => import('./components/AdminPanel.vue'),
    loadingComponent: LoadingSpinner,
    errorComponent: ErrorDisplay,
    delay: 200,
    timeout: 10000
})
</script>

<template>
    <HeavyChart :data="chartData" />
</template>
```

---

## Vue Best Practices

1. Use `<script setup>` for cleaner component syntax
2. Prefer `ref` for primitives, `reactive` for objects
3. Use `computed` for derived state, `watch` for side effects
4. Extract reusable logic into composables
5. Use Pinia for global state management
6. Lazy-load routes and heavy components
7. Use `v-memo` for expensive list rendering
8. Always provide `key` in `v-for` loops
9. Use `shallowRef` for large datasets
10. Prefer `v-show` for frequently toggled elements, `v-if` for conditionals

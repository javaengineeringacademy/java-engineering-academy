<template>
  <div class="app">
    <h1>{{ message }}</h1>
    
    <!-- Counter -->
    <div>
      <p>Count: {{ count }}</p>
      <button @click="increment">Increment</button>
      <button @click="decrement">Decrement</button>
    </div>

    <!-- List rendering -->
    <h2>Users</h2>
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.name }} - {{ user.email }}
      </li>
    </ul>

    <!-- Conditional rendering -->
    <p v-if="users.length === 0">No users found</p>
    <p v-else>{{ users.length }} users loaded</p>

    <!-- Computed property -->
    <p>Double count: {{ doubleCount }}</p>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const message = ref('Hello Vue!')
const count = ref(0)
const users = ref([
  { id: 1, name: 'Alice', email: 'alice@example.com' },
  { id: 2, name: 'Bob', email: 'bob@example.com' }
])

const doubleCount = computed(() => count.value * 2)

const increment = () => count.value++
const decrement = () => count.value--
</script>

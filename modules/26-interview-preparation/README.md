# Module 26: Interview Preparation

## Overview
Comprehensive guide for Java developer interviews covering technical questions, system design, coding challenges, and behavioral questions.

## Learning Objectives
- Prepare for technical interviews
- Practice coding challenges
- Understand system design questions
- Handle behavioral questions
- Build confidence

## Prerequisites
- Java fundamentals
- Data structures
- System design basics

## Why This Concept Exists
Interviews require:
- Technical knowledge
- Problem-solving skills
- Communication
- Confidence

This module provides:
- Common questions
- Practice problems
- Tips and strategies
- Mock interviews

## Problem Statement
How do you prepare effectively for Java developer interviews?

## Theory

### Interview Types

| Type | Focus |
|------|-------|
| Technical | Java, algorithms |
| System Design | Architecture |
| Behavioral | Soft skills |
| Coding | Problem solving |

### Technical Topics

| Topic | Importance |
|-------|------------|
| Java Core | High |
| Collections | High |
| Multithreading | High |
| Spring | High |
| Database | Medium |
| Design Patterns | Medium |

## Common Questions

### Java Core

| Question | Answer |
|----------|--------|
| What is JVM? | Java Virtual Machine - executes bytecode |
| What is the difference between == and .equals()? | == checks reference, .equals() checks value |
| What is garbage collection? | Automatic memory management |
| What is the difference between abstract class and interface? | Abstract can have state, interface cannot |
| What are generics? | Type parameterization |

### Collections

| Question | Answer |
|----------|--------|
| What is the difference between ArrayList and LinkedList? | ArrayList is array-based, LinkedList is node-based |
| What is the difference between HashMap and TreeMap? | HashMap is unordered, TreeMap is sorted |
| What is ConcurrentHashMap? | Thread-safe HashMap |
| What is the difference between Iterator and ListIterator? | Iterator for single direction, ListIterator for bidirectional |

### Multithreading

| Question | Answer |
|----------|--------|
| What is the difference between Thread and Runnable? | Thread is class, Runnable is interface |
| What is synchronization? | Mechanism to control thread access |
| What is a deadlock? | Two threads waiting for each other |
| What is the difference between wait and sleep? | wait releases lock, sleep doesn't |

### Spring

| Question | Answer |
|----------|--------|
| What is IoC? | Inversion of Control - framework manages objects |
| What is Dependency Injection? | Providing dependencies from outside |
| What is the difference between @Component and @Service? | @Service is specialized @Component |
| What is AOP? | Aspect-Oriented Programming for cross-cutting concerns |

## Coding Challenges

### Easy
1. Reverse a string
2. Find maximum in array
3. Check palindrome
4. Fibonacci sequence

### Medium
1. Two sum problem
2. Merge sorted arrays
3. Binary search
4. Valid parentheses

### Hard
1. LRU cache
2. Top K frequent elements
3. Alien dictionary
4. Word ladder

## System Design Questions

| Question | Key Points |
|----------|------------|
| Design URL shortener | Hashing, database, caching |
| Design chat system | WebSocket, messaging, storage |
| Design news feed | Push/pull, ranking, caching |
| Design payment system | Security, transactions, reliability |

## Behavioral Questions

### STAR Method

| Component | Description |
|-----------|-------------|
| Situation | Context |
| Task | Your responsibility |
| Action | What you did |
| Result | Outcome |

### Common Questions

| Question | Focus |
|----------|-------|
| Tell me about yourself | Background, skills |
| Why this company? | Research, fit |
| Describe a challenge | Problem-solving |
| How do you handle conflict? | Communication |

## Enterprise Example

```java
// Common coding challenge: LRU Cache
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }
    
    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) return null;
        
        moveToHead(node);
        return node.value;
    }
    
    public void put(K key, V value) {
        Node<K, V> node = map.get(key);
        
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            Node<K, V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            addToHead(newNode);
            
            if (map.size() > capacity) {
                Node<K, V> removed = removeTail();
                map.remove(removed.key);
            }
        }
    }
    
    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }
    
    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    private Node<K, V> removeTail() {
        Node<K, V> node = tail.prev;
        removeNode(node);
        return node;
    }
    
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
```

## Performance Considerations
- Practice regularly
- Focus on fundamentals
- Understand trade-offs
- Communicate clearly

## Best Practices
1. Study fundamentals daily
2. Practice coding problems
3. Do mock interviews
4. Review past projects
5. Prepare questions

## Common Mistakes
1. Not practicing
2. Rushing through problems
3. Not communicating
4. Giving up too easily

## Comparison Table

| Preparation | Time | Effectiveness |
|-------------|------|---------------|
| Passive reading | Low | Low |
| Active coding | High | High |
| Mock interviews | High | Very High |
| System design | Medium | High |

## Interview Tips

### Technical
1. Think out loud
2. Ask clarifying questions
3. Start with brute force
4. Optimize step by step
5. Test your solution

### Behavioral
1. Use STAR method
2. Be specific
3. Show growth
4. Be honest
5. Ask questions

## Summary
Interview preparation requires consistent practice and focus on fundamentals.

## References
- Cracking the Coding Interview
- Java Interview Guide
- System Design Interview

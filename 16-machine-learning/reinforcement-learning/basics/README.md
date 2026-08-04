# Reinforcement Learning Basics

## Overview

Reinforcement learning (RL) trains agents to make sequential decisions by interacting with an environment to maximize cumulative reward.

## Markov Decision Process (MDP)

### Components
- **States (S)**: All possible states
- **Actions (A)**: All possible actions
- **Transitions (P)**: P(s'|s, a)
- **Rewards (R)**: R(s, a, s')
- **Discount factor (γ)**: 0 ≤ γ ≤ 1

### Value Functions

#### State Value Function
```
V(s) = E[Gt | St = s] = E[Σ γ^k * Rt+k+1 | St = s]
```

#### Action Value Function (Q-function)
```
Q(s, a) = E[Gt | St = s, At = a]
```

### Bellman Equation
```
V(s) = Σ π(a|s) * Σ P(s'|s,a) * [R(s,a,s') + γ * V(s')]
```

---

## Q-Learning

### Theory

Model-free algorithm that learns Q-values:

```
Q(s, a) ← Q(s, a) + α * [r + γ * max Q(s', a') - Q(s, a)]
```

### Implementation

```python
import numpy as np
import gymnasium as gym

class QLearningAgent:
    def __init__(self, n_states, n_actions, lr=0.1, gamma=0.99, epsilon=1.0):
        self.q_table = np.zeros((n_states, n_actions))
        self.lr = lr
        self.gamma = gamma
        self.epsilon = epsilon
        self.epsilon_decay = 0.995
        self.epsilon_min = 0.01
    
    def choose_action(self, state):
        if np.random.random() < self.epsilon:
            return np.random.randint(self.q_table.shape[1])
        return np.argmax(self.q_table[state])
    
    def update(self, state, action, reward, next_state, done):
        if done:
            target = reward
        else:
            target = reward + self.gamma * np.max(self.q_table[next_state])
        
        self.q_table[state, action] += self.lr * (target - self.q_table[state, action])
    
    def decay_epsilon(self):
        self.epsilon = max(self.epsilon_min, self.epsilon * self.epsilon_decay)

# Train Q-learning agent
env = gym.make('CartPole-v1')
agent = QLearningAgent(
    n_states=env.observation_space.n,
    n_actions=env.action_space.n
)

episodes = 1000
rewards_history = []

for episode in range(episodes):
    state, _ = env.reset()
    total_reward = 0
    done = False
    
    while not done:
        action = agent.choose_action(state)
        next_state, reward, terminated, truncated, _ = env.step(action)
        done = terminated or truncated
        
        agent.update(state, action, reward, next_state, done)
        state = next_state
        total_reward += reward
    
    agent.decay_epsilon()
    rewards_history.append(total_reward)
    
    if (episode + 1) % 100 == 0:
        avg_reward = np.mean(rewards_history[-100:])
        print(f"Episode {episode+1}: Avg Reward = {avg_reward:.2f}")
```

---

## Exploration vs Exploitation

### ε-Greedy Strategy
```python
def epsilon_greedy(state, q_table, epsilon, n_actions):
    if np.random.random() < epsilon:
        return np.random.randint(n_actions)  # Explore
    return np.argmax(q_table[state])  # Exploit
```

### Upper Confidence Bound (UCB)
```python
def ucb(state, q_table, visit_counts, n_actions, c=1.0):
    ucb_values = q_table[state] + c * np.sqrt(np.log(visit_counts.sum() + 1) / (visit_counts + 1))
    return np.argmax(ucb_values)
```

### Boltzmann Exploration
```python
def boltzmann(state, q_table, temperature=1.0):
    q_values = q_table[state]
    exp_q = np.exp(q_values / temperature)
    probabilities = exp_q / exp_q.sum()
    return np.random.choice(len(q_values), p=probabilities)
```

---

## SARSA

### Theory

On-policy learning:
```
Q(s, a) ← Q(s, a) + α * [r + γ * Q(s', a') - Q(s, a)]
```

### Implementation

```python
class SARSAAgent:
    def __init__(self, n_states, n_actions, lr=0.1, gamma=0.99, epsilon=1.0):
        self.q_table = np.zeros((n_states, n_actions))
        self.lr = lr
        self.gamma = gamma
        self.epsilon = epsilon
    
    def choose_action(self, state):
        if np.random.random() < self.epsilon:
            return np.random.randint(self.q_table.shape[1])
        return np.argmax(self.q_table[state])
    
    def update(self, state, action, reward, next_state, next_action, done):
        if done:
            target = reward
        else:
            target = reward + self.gamma * self.q_table[next_state, next_action]
        
        self.q_table[state, action] += self.lr * (target - self.q_table[state, action])
```

---

## Dynamic Programming

### Policy Evaluation
```python
def policy_evaluation(env, policy, gamma=0.99, theta=1e-8):
    V = np.zeros(env.observation_space.n)
    while True:
        delta = 0
        for s in range(env.observation_space.n):
            v = V[s]
            V[s] = sum(p * (r + gamma * V[s_]) 
                       for p, s_, r, _ in env.P[s][policy[s]])
            delta = max(delta, abs(v - V[s]))
        if delta < theta:
            break
    return V
```

### Policy Iteration
```python
def policy_iteration(env, gamma=0.99):
    policy = np.zeros(env.observation_space.n, dtype=int)
    
    while True:
        V = policy_evaluation(env, policy, gamma)
        
        policy_stable = True
        for s in range(env.observation_space.n):
            old_action = policy[s]
            policy[s] = np.argmax([sum(p * (r + gamma * V[s_]) 
                                       for p, s_, r, _ in env.P[s][a])
                                   for a in range(env.action_space.n)])
            if old_action != policy[s]:
                policy_stable = False
        
        if policy_stable:
            return policy, V
```

---

## Monte Carlo Methods

### First-Visit Monte Carlo
```python
def monte_carlo(env, episodes=1000, gamma=0.99, epsilon=0.1):
    q_table = np.zeros((env.observation_space.n, env.action_space.n))
    returns = {(s, a): [] for s in range(env.observation_space.n) 
               for a in range(env.action_space.n)}
    
    for _ in range(episodes):
        episode = []
        state, _ = env.reset()
        done = False
        
        while not done:
            action = epsilon_greedy(state, q_table, epsilon, env.action_space.n)
            next_state, reward, terminated, truncated, _ = env.step(action)
            done = terminated or truncated
            episode.append((state, action, reward))
            state = next_state
        
        G = 0
        visited = set()
        for state, action, reward in reversed(episode):
            G = gamma * G + reward
            if (state, action) not in visited:
                returns[(state, action)].append(G)
                q_table[state, action] = np.mean(returns[(state, action)])
                visited.add((state, action))
    
    return q_table
```

---

## Key Concepts

### Discount Factor (γ)
- γ = 0: Myopic (only immediate reward)
- γ = 1: Far-sighted (equal weight to all future rewards)
- Typical: 0.9 to 0.99

### Learning Rate (α)
- High α: Fast learning, high variance
- Low α: Slow learning, stable
- Typical: 0.01 to 0.1

### Reward Design
- Dense rewards: Frequent feedback
- Sparse rewards: Only at episode end
- Shaped rewards: Intermediate rewards to guide learning

## Further Reading

- "Reinforcement Learning: An Introduction" by Sutton and Barto
- OpenAI Spinning Up
- Stable Baselines3 documentation

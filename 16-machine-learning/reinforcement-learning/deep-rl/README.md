# Deep Reinforcement Learning

## Overview

Deep RL combines deep neural networks with reinforcement learning, enabling agents to learn directly from high-dimensional sensory inputs.

## Deep Q-Network (DQN)

### Theory

Uses neural network to approximate Q-values:
- Experience replay
- Target network

### Implementation

```python
import torch
import torch.nn as nn
import torch.optim as optim
import numpy as np
import random
from collections import deque
import gymnasium as gym

class DQN(nn.Module):
    def __init__(self, state_dim, action_dim, hidden_dim=128):
        super().__init__()
        self.network = nn.Sequential(
            nn.Linear(state_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, action_dim)
        )
    
    def forward(self, x):
        return self.network(x)

class ReplayBuffer:
    def __init__(self, capacity=100000):
        self.buffer = deque(maxlen=capacity)
    
    def push(self, state, action, reward, next_state, done):
        self.buffer.append((state, action, reward, next_state, done))
    
    def sample(self, batch_size):
        batch = random.sample(self.buffer, batch_size)
        states, actions, rewards, next_states, dones = zip(*batch)
        return (
            torch.FloatTensor(np.array(states)),
            torch.LongTensor(actions),
            torch.FloatTensor(rewards),
            torch.FloatTensor(np.array(next_states)),
            torch.FloatTensor(dones)
        )
    
    def __len__(self):
        return len(self.buffer)

class DQNAgent:
    def __init__(self, state_dim, action_dim, lr=0.001, gamma=0.99,
                 epsilon=1.0, epsilon_min=0.01, epsilon_decay=0.995):
        self.q_network = DQN(state_dim, action_dim)
        self.target_network = DQN(state_dim, action_dim)
        self.target_network.load_state_dict(self.q_network.state_dict())
        self.optimizer = optim.Adam(self.q_network.parameters(), lr=lr)
        self.gamma = gamma
        self.epsilon = epsilon
        self.epsilon_min = epsilon_min
        self.epsilon_decay = epsilon_decay
        self.buffer = ReplayBuffer()
        self.batch_size = 64
    
    def choose_action(self, state):
        if random.random() < self.epsilon:
            return random.randint(0, self.q_network.network[-1].out_features - 1)
        
        with torch.no_grad():
            state_tensor = torch.FloatTensor(state).unsqueeze(0)
            q_values = self.q_network(state_tensor)
            return q_values.argmax().item()
    
    def update(self):
        if len(self.buffer) < self.batch_size:
            return
        
        states, actions, rewards, next_states, dones = self.buffer.sample(self.batch_size)
        
        # Current Q values
        current_q = self.q_network(states).gather(1, actions.unsqueeze(1))
        
        # Target Q values
        with torch.no_grad():
            next_q = self.target_network(next_states).max(1)[0]
            target_q = rewards + self.gamma * next_q * (1 - dones)
        
        loss = nn.MSELoss()(current_q.squeeze(), target_q)
        
        self.optimizer.zero_grad()
        loss.backward()
        self.optimizer.step()
        
        # Update target network
        self.epsilon = max(self.epsilon_min, self.epsilon * self.epsilon_decay)
        
        return loss.item()
    
    def update_target(self):
        self.target_network.load_state_dict(self.q_network.state_dict())

# Train DQN
env = gym.make('CartPole-v1')
agent = DQNAgent(
    state_dim=env.observation_space.shape[0],
    action_dim=env.action_space.n
)

episodes = 500
rewards_history = []

for episode in range(episodes):
    state, _ = env.reset()
    total_reward = 0
    done = False
    
    while not done:
        action = agent.choose_action(state)
        next_state, reward, terminated, truncated, _ = env.step(action)
        done = terminated or truncated
        
        agent.buffer.push(state, action, reward, next_state, done)
        agent.update()
        
        state = next_state
        total_reward += reward
    
    if episode % 10 == 0:
        agent.update_target()
    
    rewards_history.append(total_reward)
    
    if (episode + 1) % 50 == 0:
        avg_reward = np.mean(rewards_history[-50:])
        print(f"Episode {episode+1}: Avg Reward = {avg_reward:.2f}")
```

---

## Double DQN

### Theory

Reduces overestimation by using online network for action selection and target network for evaluation:

```
target = r + γ * Q_target(s', argmax_a Q_online(s', a))
```

### Implementation

```python
class DoubleDQNAgent(DQNAgent):
    def update(self):
        if len(self.buffer) < self.batch_size:
            return
        
        states, actions, rewards, next_states, dones = self.buffer.sample(self.batch_size)
        
        current_q = self.q_network(states).gather(1, actions.unsqueeze(1))
        
        with torch.no_grad():
            # Double DQN: use online network for action selection
            next_actions = self.q_network(next_states).argmax(1)
            next_q = self.target_network(next_states).gather(1, next_actions.unsqueeze(1)).squeeze()
            target_q = rewards + self.gamma * next_q * (1 - dones)
        
        loss = nn.MSELoss()(current_q.squeeze(), target_q)
        
        self.optimizer.zero_grad()
        loss.backward()
        self.optimizer.step()
        
        self.epsilon = max(self.epsilon_min, self.epsilon * self.epsilon_decay)
        
        return loss.item()
```

---

## Dueling DQN

### Architecture

Separates value and advantage streams:
```
Q(s, a) = V(s) + A(s, a) - mean(A(s, a))
```

### Implementation

```python
class DuelingDQN(nn.Module):
    def __init__(self, state_dim, action_dim, hidden_dim=128):
        super().__init__()
        self.feature = nn.Sequential(
            nn.Linear(state_dim, hidden_dim),
            nn.ReLU()
        )
        self.value = nn.Sequential(
            nn.Linear(hidden_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, 1)
        )
        self.advantage = nn.Sequential(
            nn.Linear(hidden_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, action_dim)
        )
    
    def forward(self, x):
        features = self.feature(x)
        value = self.value(features)
        advantage = self.advantage(features)
        q_values = value + advantage - advantage.mean(dim=1, keepdim=True)
        return q_values
```

---

## Rainbow DQN

Combines multiple improvements:
1. Double DQN
2. Dueling DQN
3. Prioritized Experience Replay
4. Multi-step Learning
5. Distributional RL
6. Noisy Nets

### Prioritized Experience Replay

```python
class PrioritizedReplayBuffer:
    def __init__(self, capacity=100000, alpha=0.6):
        self.buffer = []
        self.priorities = []
        self.capacity = capacity
        self.alpha = alpha
        self.position = 0
    
    def push(self, state, action, reward, next_state, done):
        max_priority = max(self.priorities) if self.priorities else 1.0
        
        if len(self.buffer) < self.capacity:
            self.buffer.append((state, action, reward, next_state, done))
            self.priorities.append(max_priority)
        else:
            self.buffer[self.position] = (state, action, reward, next_state, done)
            self.priorities[self.position] = max_priority
        
        self.position = (self.position + 1) % self.capacity
    
    def sample(self, batch_size, beta=0.4):
        priorities = np.array(self.priorities[:len(self.buffer)])
        probabilities = priorities ** self.alpha
        probabilities /= probabilities.sum()
        
        indices = np.random.choice(len(self.buffer), batch_size, p=probabilities)
        samples = [self.buffer[i] for i in indices]
        
        # Importance sampling weights
        weights = (len(self.buffer) * probabilities[indices]) ** (-beta)
        weights /= weights.max()
        
        states, actions, rewards, next_states, dones = zip(*samples)
        return (
            torch.FloatTensor(np.array(states)),
            torch.LongTensor(actions),
            torch.FloatTensor(rewards),
            torch.FloatTensor(np.array(next_states)),
            torch.FloatTensor(dones),
            torch.FloatTensor(weights),
            indices
        )
    
    def update_priorities(self, indices, priorities):
        for idx, priority in zip(indices, priorities):
            self.priorities[idx] = priority + 1e-6
```

---

## Actor-Critic Methods

### A3C (Asynchronous Advantage Actor-Critic)

```python
import torch.multiprocessing as mp

class A3CWorker(mp.Process):
    def __init__(self, global_model, optimizer, env_name, worker_id):
        super().__init__()
        self.global_model = global_model
        self.optimizer = optimizer
        self.env_name = env_name
        self.worker_id = worker_id
    
    def run(self):
        env = gym.make(self.env_name)
        local_model = ActorCritic(env.observation_space.shape[0], env.action_space.n)
        
        while True:
            # Sync with global model
            local_model.load_state_dict(self.global_model.state_dict())
            
            # Collect experience
            states, actions, rewards = [], [], []
            state, _ = env.reset()
            done = False
            
            while not done:
                probs, value = local_model(torch.FloatTensor(state).unsqueeze(0))
                dist = Categorical(probs)
                action = dist.sample()
                
                next_state, reward, terminated, truncated, _ = env.step(action.item())
                done = terminated or truncated
                
                states.append(state)
                actions.append(action)
                rewards.append(reward)
                
                state = next_state
            
            # Compute returns and advantages
            returns = []
            R = 0
            for r in reversed(rewards):
                R = r + 0.99 * R
                returns.insert(0, R)
            
            returns = torch.FloatTensor(returns)
            states = torch.FloatTensor(np.array(states))
            actions = torch.LongTensor(actions)
            
            # Update
            probs, values = local_model(states)
            dist = Categorical(probs)
            log_probs = dist.log_prob(actions)
            
            advantages = returns - values.squeeze()
            actor_loss = -(log_probs * advantages.detach()).mean()
            critic_loss = nn.MSELoss()(values.squeeze(), returns)
            loss = actor_loss + 0.5 * critic_loss
            
            self.optimizer.zero_grad()
            loss.backward()
            nn.utils.clip_grad_norm_(local_model.parameters(), 0.5)
            
            # Update global model
            for global_param, local_param in zip(self.global_model.parameters(), 
                                                  local_model.parameters()):
                global_param.grad = local_param.grad
            self.optimizer.step()
```

---

## Model-Based RL

### World Models

```python
class WorldModel(nn.Module):
    def __init__(self, state_dim, action_dim, hidden_dim=128):
        super().__init__()
        self.dynamics = nn.Sequential(
            nn.Linear(state_dim + action_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, state_dim)
        )
        self.reward_predictor = nn.Sequential(
            nn.Linear(state_dim + action_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, 1)
        )
    
    def forward(self, state, action):
        sa = torch.cat([state, action], dim=-1)
        next_state = self.dynamics(sa)
        reward = self.reward_predictor(sa)
        return next_state, reward
```

---

## Comparison

| Method | Type | Stability | Sample Efficiency | Complexity |
|--------|------|-----------|-------------------|------------|
| DQN | Value-based | Medium | Low | Low |
| Double DQN | Value-based | High | Low | Low |
| Dueling DQN | Value-based | High | Low | Medium |
| Rainbow | Value-based | High | Medium | High |
| A3C | Actor-Critic | Medium | Medium | Medium |
| PPO | Policy-based | High | Medium | Medium |

## Best Practices

1. **Start with DQN**: Good baseline for discrete actions
2. **Use replay buffer**: Stabilize training
3. **Target network**: Reduce overestimation
4. **Gradient clipping**: Prevent exploding gradients
5. **Reward shaping**: Guide learning with intermediate rewards

## Further Reading

- "Playing Atari with Deep Reinforcement Learning" (DQN paper)
- "Rainbow" by Hessel et al.
- OpenAI Spinning Up in Deep RL
- Stable Baselines3 documentation

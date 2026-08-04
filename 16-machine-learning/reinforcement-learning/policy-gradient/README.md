# Policy Gradient Methods

## Overview

Policy gradient methods directly optimize the policy by gradient ascent on expected return. They can handle continuous action spaces and stochastic policies.

## REINFORCE

### Theory

Monte Carlo policy gradient:
```
∇J(θ) = E[Σ ∇log π(aₜ|sₜ; θ) * Gₜ]
```

### Implementation

```python
import torch
import torch.nn as nn
import torch.optim as optim
from torch.distributions import Categorical
import gymnasium as gym
import numpy as np

class PolicyNetwork(nn.Module):
    def __init__(self, state_dim, action_dim, hidden_dim=128):
        super().__init__()
        self.network = nn.Sequential(
            nn.Linear(state_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, action_dim),
            nn.Softmax(dim=-1)
        )
    
    def forward(self, state):
        return self.network(state)

class REINFORCEAgent:
    def __init__(self, state_dim, action_dim, lr=0.001, gamma=0.99):
        self.policy = PolicyNetwork(state_dim, action_dim)
        self.optimizer = optim.Adam(self.policy.parameters(), lr=lr)
        self.gamma = gamma
        self.log_probs = []
        self.rewards = []
    
    def choose_action(self, state):
        state_tensor = torch.FloatTensor(state).unsqueeze(0)
        probs = self.policy(state_tensor)
        dist = Categorical(probs)
        action = dist.sample()
        self.log_probs.append(dist.log_prob(action))
        return action.item()
    
    def store_reward(self, reward):
        self.rewards.append(reward)
    
    def update(self):
        # Calculate discounted returns
        returns = []
        G = 0
        for r in reversed(self.rewards):
            G = r + self.gamma * G
            returns.insert(0, G)
        returns = torch.FloatTensor(returns)
        returns = (returns - returns.mean()) / (returns.std() + 1e-8)
        
        # Policy gradient
        policy_loss = []
        for log_prob, G in zip(self.log_probs, returns):
            policy_loss.append(-log_prob * G)
        
        self.optimizer.zero_grad()
        loss = torch.stack(policy_loss).sum()
        loss.backward()
        self.optimizer.step()
        
        # Clear buffers
        self.log_probs = []
        self.rewards = []
        
        return loss.item()

# Train REINFORCE
env = gym.make('CartPole-v1')
agent = REINFORCEAgent(
    state_dim=env.observation_space.shape[0],
    action_dim=env.action_space.n
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
        
        agent.store_reward(reward)
        state = next_state
        total_reward += reward
    
    loss = agent.update()
    rewards_history.append(total_reward)
    
    if (episode + 1) % 100 == 0:
        avg_reward = np.mean(rewards_history[-100:])
        print(f"Episode {episode+1}: Avg Reward = {avg_reward:.2f}")
```

---

## Actor-Critic

### Theory

Combines policy gradient (actor) with value function (critic):

```
Actor: π(a|s; θ) - policy
Critic: V(s; w) or Q(s, a; w) - value function

Advantage: A(s, a) = Q(s, a) - V(s) or A(s, a) = r + γV(s') - V(s)
```

### Implementation

```python
class ActorCritic(nn.Module):
    def __init__(self, state_dim, action_dim, hidden_dim=128):
        super().__init__()
        self.actor = nn.Sequential(
            nn.Linear(state_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, action_dim),
            nn.Softmax(dim=-1)
        )
        self.critic = nn.Sequential(
            nn.Linear(state_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, 1)
        )
    
    def forward(self, state):
        action_probs = self.actor(state)
        state_value = self.critic(state)
        return action_probs, state_value

class A2CAgent:
    def __init__(self, state_dim, action_dim, lr=0.001, gamma=0.99):
        self.model = ActorCritic(state_dim, action_dim)
        self.optimizer = optim.Adam(self.model.parameters(), lr=lr)
        self.gamma = gamma
    
    def choose_action(self, state):
        state_tensor = torch.FloatTensor(state).unsqueeze(0)
        probs, value = self.model(state_tensor)
        dist = Categorical(probs)
        action = dist.sample()
        return action.item(), dist.log_prob(action), value
    
    def update(self, log_probs, values, rewards, dones):
        returns = []
        G = 0
        for r, done in zip(reversed(rewards), reversed(dones)):
            if done:
                G = r
            else:
                G = r + self.gamma * G
            returns.insert(0, G)
        
        returns = torch.FloatTensor(returns)
        values = torch.cat(values)
        log_probs = torch.stack(log_probs)
        
        advantages = returns - values.detach()
        advantages = (advantages - advantages.mean()) / (advantages.std() + 1e-8)
        
        actor_loss = -(log_probs * advantages).mean()
        critic_loss = nn.MSELoss()(values, returns)
        
        loss = actor_loss + 0.5 * critic_loss
        
        self.optimizer.zero_grad()
        loss.backward()
        self.optimizer.step()
        
        return loss.item()
```

---

## A2C (Advantage Actor-Critic)

```python
import gymnasium as gym
from gymnasium.vector import AsyncVectorEnv

def make_env():
    return gym.make('CartPole-v1')

class A2C:
    def __init__(self, state_dim, action_dim, n_envs=4, lr=0.001, gamma=0.99):
        self.model = ActorCritic(state_dim, action_dim)
        self.optimizer = optim.Adam(self.model.parameters(), lr=lr)
        self.gamma = gamma
        self.n_envs = n_envs
    
    def train(self, envs, n_steps=5):
        states = torch.FloatTensor(envs.reset()[0])
        done = np.zeros(self.n_envs)
        
        all_log_probs = []
        all_values = []
        all_rewards = []
        all_dones = []
        
        for _ in range(n_steps):
            probs, values = self.model(states)
            dist = Categorical(probs)
            actions = dist.sample()
            
            next_states, rewards, terminateds, truncateds, _ = envs.step(actions.numpy())
            dones = np.logical_or(terminateds, truncateds)
            
            all_log_probs.append(dist.log_prob(actions))
            all_values.append(values.squeeze())
            all_rewards.append(torch.FloatTensor(rewards))
            all_dones.append(torch.FloatTensor(dones))
            
            states = torch.FloatTensor(next_states)
        
        # Compute returns
        with torch.no_grad():
            _, next_value = self.model(states)
        
        returns = []
        R = next_value.squeeze()
        for r, done in zip(reversed(all_rewards), reversed(all_dones)):
            R = r + self.gamma * R * (1 - done)
            returns.insert(0, R)
        
        returns = torch.stack(returns)
        values = torch.stack(all_values)
        log_probs = torch.stack(all_log_probs)
        
        advantages = returns - values.detach()
        advantages = (advantages - advantages.mean()) / (advantages.std() + 1e-8)
        
        actor_loss = -(log_probs * advantages).mean()
        critic_loss = nn.MSELoss()(values, returns)
        entropy_loss = -0.01 * (probs * probs.log()).sum(dim=-1).mean()
        
        loss = actor_loss + 0.5 * critic_loss - entropy_loss
        
        self.optimizer.zero_grad()
        loss.backward()
        self.optimizer.step()
        
        return loss.item()
```

---

## PPO (Proximal Policy Optimization)

### Theory

Clips policy updates to prevent large changes:
```
L^CLIP = E[min(rₜ(θ)Âₜ, clip(rₜ(θ), 1-ε, 1+ε)Âₜ)]
rₜ(θ) = π(aₜ|sₜ; θ) / π(aₜ|sₜ; θ_old)
```

### Implementation

```python
class PPOAgent:
    def __init__(self, state_dim, action_dim, lr=3e-4, gamma=0.99, clip_epsilon=0.2):
        self.model = ActorCritic(state_dim, action_dim)
        self.optimizer = optim.Adam(self.model.parameters(), lr=lr)
        self.gamma = gamma
        self.clip_epsilon = clip_epsilon
    
    def update(self, states, actions, old_log_probs, returns, advantages, epochs=10):
        for _ in range(epochs):
            probs, values = self.model(states)
            dist = Categorical(probs)
            new_log_probs = dist.log_prob(actions)
            entropy = dist.entropy().mean()
            
            ratio = torch.exp(new_log_probs - old_log_probs)
            surr1 = ratio * advantages
            surr2 = torch.clamp(ratio, 1 - self.clip_epsilon, 1 + self.clip_epsilon) * advantages
            
            actor_loss = -torch.min(surr1, surr2).mean()
            critic_loss = nn.MSELoss()(values.squeeze(), returns)
            loss = actor_loss + 0.5 * critic_loss - 0.01 * entropy
            
            self.optimizer.zero_grad()
            loss.backward()
            nn.utils.clip_grad_norm_(self.model.parameters(), 0.5)
            self.optimizer.step()
        
        return loss.item()
```

---

## Comparison

| Method | On/Off Policy | Stability | Sample Efficiency |
|--------|---------------|-----------|-------------------|
| REINFORCE | On | Low | Low |
| A2C | On | Medium | Medium |
| PPO | On | High | Medium |
| SAC | Off | High | High |

## Best Practices

1. **Use baselines**: Reduce variance
2. **Normalize rewards**: Improve stability
3. **Entropy bonus**: Encourage exploration
4. **Gradient clipping**: Prevent exploding gradients
5. **GAE**: Generalized Advantage Estimation

## Further Reading

- "Policy Gradient Methods" by Sutton et al.
- "Proximal Policy Optimization" by Schulman et al.
- Stable Baselines3 PPO documentation

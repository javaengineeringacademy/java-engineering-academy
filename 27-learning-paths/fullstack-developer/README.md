# Fullstack Developer Learning Path

Comprehensive roadmap for becoming a proficient fullstack developer.

## Overview

This learning path combines frontend and backend skills with DevOps knowledge to create well-rounded fullstack developers.

## Prerequisites

- Basic programming concepts
- HTML/CSS fundamentals
- Any programming language basics
- Git basics

## Learning Path

### Phase 1: Frontend Mastery (6-8 weeks)

#### Week 1-4: HTML, CSS, JavaScript
- [ ] HTML5 semantic elements
- [ ] CSS Flexbox and Grid
- [ ] JavaScript ES6+ features
- [ ] DOM manipulation
- [ ] Responsive design

**Resources:**
- MDN Web Docs
- freeCodeCamp
- "Eloquent JavaScript" by Marijn Haverbeke

**Practice:**
```html
<!-- Responsive navbar -->
<nav class="navbar">
    <div class="container">
        <a href="/" class="logo">MyApp</a>
        <ul class="nav-links">
            <li><a href="/home">Home</a></li>
            <li><a href="/about">About</a></li>
            <li><a href="/contact">Contact</a></li>
        </ul>
        <button class="mobile-menu-btn">☰</button>
    </div>
</nav>
```

```css
/* CSS Grid layout */
.dashboard {
    display: grid;
    grid-template-columns: 250px 1fr;
    grid-template-rows: auto 1fr auto;
    min-height: 100vh;
}

.sidebar {
    grid-row: 1 / -1;
    background: #2c3e50;
}

.main-content {
    grid-column: 2;
    padding: 2rem;
}

@media (max-width: 768px) {
    .dashboard {
        grid-template-columns: 1fr;
    }
    
    .sidebar {
        display: none;
    }
}
```

#### Week 5-8: React Fundamentals
- [ ] Components and JSX
- [ ] Props and state
- [ ] Hooks (useState, useEffect)
- [ ] React Router
- [ ] Context API

**Practice:**
```jsx
// React component with hooks
function UserProfile({ userId }) {
    const [user, setUser] = React.useState(null);
    const [loading, setLoading] = React.useState(true);
    
    React.useEffect(() => {
        fetch(`/api/users/${userId}`)
            .then(res => res.json())
            .then(data => {
                setUser(data);
                setLoading(false);
            });
    }, [userId]);
    
    if (loading) return <div>Loading...</div>;
    
    return (
        <div className="profile">
            <h1>{user.name}</h1>
            <p>{user.email}</p>
        </div>
    );
}

// React Router setup
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

function App() {
    return (
        <Router>
            <Switch>
                <Route exact path="/" component={Home} />
                <Route path="/about" component={About} />
                <Route path="/users/:id" component={UserProfile} />
            </Switch>
        </Router>
    );
}
```

### Phase 2: Backend Development (6-8 weeks)

#### Week 9-12: Node.js and Express
- [ ] Node.js fundamentals
- [ ] Express.js framework
- [ ] RESTful API design
- [ ] Middleware patterns

**Practice:**
```javascript
// Express.js API
const express = require('express');
const app = express();

app.use(express.json());

// Routes
app.get('/api/users', async (req, res) => {
    try {
        const users = await User.find();
        res.json(users);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

app.post('/api/users', async (req, res) => {
    try {
        const user = new User(req.body);
        await user.save();
        res.status(201).json(user);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Middleware
const auth = async (req, res, next) => {
    try {
        const token = req.header('Authorization').replace('Bearer ', '');
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        const user = await User.findById(decoded.userId);
        
        if (!user) {
            throw new Error();
        }
        
        req.user = user;
        next();
    } catch (error) {
        res.status(401).json({ error: 'Please authenticate' });
    }
};

app.get('/api/users/me', auth, async (req, res) => {
    res.json(req.user);
});

app.listen(3000, () => {
    console.log('Server running on port 3000');
});
```

#### Week 13-16: Database Integration
- [ ] MongoDB with Mongoose
- [ ] PostgreSQL with Sequelize
- [ ] Database design patterns
- [ ] Migrations and seeding

**Practice:**
```javascript
// Mongoose model
const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        trim: true
    },
    email: {
        type: String,
        required: true,
        unique: true,
        lowercase: true
    },
    password: {
        type: String,
        required: true,
        minlength: 7
    }
}, {
    timestamps: true
});

// Hash password before saving
userSchema.pre('save', async function(next) {
    if (this.isModified('password')) {
        this.password = await bcrypt.hash(this.password, 8);
    }
    next();
});

// Instance method
userSchema.methods.toJSON = function() {
    const user = this.toObject();
    delete user.password;
    return user;
};

const User = mongoose.model('User', userSchema);

// Sequelize model (PostgreSQL)
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const User = sequelize.define('User', {
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    name: {
        type: DataTypes.STRING,
        allowNull: false
    },
    email: {
        type: DataTypes.STRING,
        allowNull: false,
        unique: true,
        validate: {
            isEmail: true
        }
    }
}, {
    timestamps: true
});
```

### Phase 3: Authentication & Security (3-4 weeks)

#### Week 17-18: Authentication
- [ ] JWT implementation
- [ ] Session management
- [ ] OAuth 2.0
- [ ] Password reset flow

**Practice:**
```javascript
// JWT implementation
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');

const generateToken = (userId) => {
    return jwt.sign({ userId }, process.env.JWT_SECRET, {
        expiresIn: '7d'
    });
};

const auth = async (req, res, next) => {
    try {
        const token = req.header('Authorization').replace('Bearer ', '');
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        const user = await User.findById(decoded.userId);
        
        if (!user) {
            throw new Error();
        }
        
        req.token = token;
        req.user = user;
        next();
    } catch (error) {
        res.status(401).json({ error: 'Not authenticated' });
    }
};

// Login endpoint
app.post('/api/login', async (req, res) => {
    try {
        const { email, password } = req.body;
        const user = await User.findByCredentials(email, password);
        const token = generateToken(user._id);
        
        res.json({ user, token });
    } catch (error) {
        res.status(400).json({ error: 'Invalid credentials' });
    }
});
```

#### Week 19-20: Security Best Practices
- [ ] Input validation
- [ ] Rate limiting
- [ ] CORS configuration
- [ ] Helmet.js

**Practice:**
```javascript
// Security middleware
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const cors = require('cors');

// Helmet
app.use(helmet());

// Rate limiting
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100 // limit each IP to 100 requests per windowMs
});

app.use('/api/', limiter);

// CORS
app.use(cors({
    origin: process.env.CLIENT_URL,
    credentials: true
}));

// Input validation
const { body, validationResult } = require('express-validator');

app.post('/api/users', [
    body('email').isEmail().normalizeEmail(),
    body('name').trim().escape(),
    body('password').isLength({ min: 7 })
], async (req, res) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        return res.status(400).json({ errors: errors.array() });
    }
    
    // Create user...
});
```

### Phase 4: DevOps Basics (3-4 weeks)

#### Week 21-22: Git and Deployment
- [ ] Git workflow
- [ ] GitHub/GitLab
- [ ] CI/CD basics
- [ ] Deployment strategies

**Practice:**
```bash
# Git workflow
git checkout -b feature/user-auth
git add .
git commit -m "feat: implement user authentication"
git push origin feature/user-auth

# Create pull request
# Merge to main

# CI/CD with GitHub Actions
name: CI/CD

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Use Node.js
      uses: actions/setup-node@v2
      with:
        node-version: '14'
    - run: npm ci
    - run: npm test
    
  deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
    - uses: actions/checkout@v2
    - name: Deploy to Heroku
      uses: akhileshns/heroku-deploy@master
      with:
        heroku_api_key: ${{secrets.HEROKU_API_KEY}}
        heroku_app_name: "your-app-name"
        heroku_email: "your-email@example.com"
```

#### Week 23-24: Docker Basics
- [ ] Dockerfile
- [ ] Docker Compose
- [ ] Containerization concepts
- [ ] Development environment

**Practice:**
```dockerfile
# Dockerfile
FROM node:14-alpine

WORKDIR /app

COPY package*.json ./
RUN npm ci --only=production

COPY . .

EXPOSE 3000

CMD ["node", "server.js"]
```

```yaml
# docker-compose.yml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "3000:3000"
    environment:
      - NODE_ENV=development
      - MONGODB_URI=mongodb://mongo:27017/myapp
    depends_on:
      - mongo
    volumes:
      - .:/app
      - /app/node_modules
    
  mongo:
    image: mongo:4.4
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db

volumes:
  mongo-data:
```

### Phase 5: Fullstack Project (4-6 weeks)

#### Project: Task Management Application

**Features:**
- User authentication
- CRUD operations
- Real-time updates
- Responsive design

**Tech Stack:**
- Frontend: React + TypeScript
- Backend: Node.js + Express
- Database: MongoDB
- Authentication: JWT
- Deployment: Docker + Heroku

**Project Structure:**
```
task-manager/
├── client/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── context/
│   │   └── utils/
│   └── package.json
├── server/
│   ├── models/
│   ├── routes/
│   ├── middleware/
│   └── package.json
├── docker-compose.yml
└── README.md
```

## Certification Path

### Recommended Certifications
- **AWS Certified Developer**
- **MongoDB Certified Developer**
- **Meta Frontend Developer Certificate**

## Career Progression

### Junior Fullstack Developer (0-2 years)
- Build complete features
- Understand both frontend and backend
- Write clean code
- Basic deployment skills

### Mid-Level Fullstack Developer (2-5 years)
- Lead feature development
- Architect simple systems
- Mentor junior developers
- Optimize performance

### Senior Fullstack Developer (5+ years)
- Architect complex systems
- Make technology choices
- Drive technical strategy
- Lead teams

## Resources

### Books
- "Fullstack JavaScript" by Philip Kluss
- "Node.js Design Patterns" by Mario Casciaro
- "React in Practice" by Charles Freeman

### Online
- The Odin Project
- freeCodeCamp
- Codecademy

### Practice
- Build personal projects
- Contribute to open source
- Solve fullstack challenges

## Next Steps

After completing this path:
- [19-case-studies](../19-case-studies/) - Learn from real-world examples
- [20-interview-preparation](../20-interview-preparation/) - Prepare for interviews
- [24-certifications](../24-certifications/) - Pursue certifications
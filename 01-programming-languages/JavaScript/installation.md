# JavaScript Installation

## Node.js

```bash
# Download from nodejs.org
# LTS version recommended

# Verify installation
node --version
npm --version
```

## nvm - Node Version Manager

```bash
# Install nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Install Node.js
nvm install 20
nvm use 20
nvm alias default 20

# List installed versions
nvm ls

# Install specific version
nvm install 18.17.0
```

## npm

```bash
# Initialize project
npm init -y

# Install dependencies
npm install express
npm install --save-dev jest eslint

# Install globally
npm install -g typescript nodemon

# Update dependencies
npm update

# Audit for vulnerabilities
npm audit
npm audit fix
```

## yarn

```bash
# Install yarn
npm install -g yarn

# Initialize project
yarn init -y

# Add dependencies
yarn add express
yarn add --dev jest

# Install all dependencies
yarn install

# Update dependencies
yarn upgrade
```

## pnpm

```bash
# Install pnpm
npm install -g pnpm

# Initialize project
pnpm init

# Add dependencies
pnpm add express
pnpm add -D jest

# Install all dependencies
pnpm install
```

## CDN

```html
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/lodash@4/lodash.min.js"></script>
```

## Uninstall

```bash
# Remove node_modules
rm -rf node_modules package-lock.json yarn.lock

# Uninstall nvm
nvm deactivate
rm -rf ~/.nvm
```

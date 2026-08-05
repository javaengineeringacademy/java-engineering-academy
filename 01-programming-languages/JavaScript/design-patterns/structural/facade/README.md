# Facade Pattern (JavaScript)

## Overview

The Facade pattern provides a unified interface to a set of interfaces in a subsystem.
JavaScript classes and modules can wrap complex subsystems to provide simpler APIs.

## When to Use

- Simplifying complex library usage
- Providing layered architecture
- Decoupling subsystems from clients
- Creating service layers

## JavaScript Implementation

### Basic Facade

```javascript
class VideoConverter {
  convert(filename, format) {
    console.log(`Converting ${filename} to ${format}`);
    return { filename: `${filename}.${format}`, format };
  }
}

class AudioConverter {
  extractAudio(filename) {
    console.log(`Extracting audio from ${filename}`);
    return { audio: `${filename}.mp3` };
  }
}

class Facade {
  constructor() {
    this.videoConverter = new VideoConverter();
    this.audioConverter = new AudioConverter();
  }

  convertToMP4(filename) {
    return this.videoConverter.convert(filename, 'mp4');
  }

  extractAudio(filename) {
    return this.audioConverter.extractAudio(filename);
  }
}

const facade = new Facade();
facade.convertToMP4('video');
```

### With Async Operations

```javascript
class APIFacade {
  constructor(config) {
    this.config = config;
  }

  async getUser(id) {
    const response = await fetch(`${this.config.baseURL}/users/${id}`);
    return response.json();
  }

  async getPosts(userId) {
    const response = await fetch(`${this.config.baseURL}/users/${userId}/posts`);
    return response.json();
  }

  async getUserWithPosts(id) {
    const user = await this.getUser(id);
    const posts = await this.getPosts(id);
    return { user, posts };
  }
}
```

## Best Practices

- Keep facade focused and minimal
- Don't add business logic to facade
- Use facade as thin layer only
- Document subsystem dependencies
- Consider using dependency injection

## Interview Questions

1. What is the difference between Facade and Adapter?
2. Does Facade add new functionality?
3. When should you use Facade vs direct subsystem access?
4. Can Facade be combined with other patterns?
5. How do you test code using Facade?

## References

- MDN: Facade Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Clean Architecture" by Robert C. Martin

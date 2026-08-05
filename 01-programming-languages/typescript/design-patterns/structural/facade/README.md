# Facade Pattern (TypeScript)

## Overview

The Facade pattern provides a unified interface to a set of interfaces in a subsystem.
TypeScript's interfaces enable type-safe facade implementations with compile-time
checking.

## When to Use

- Simplifying complex library usage
- Providing layered architecture
- Decoupling subsystems from clients
- Creating service layers

## TypeScript Implementation

### Typed Facade

```typescript
interface VideoConverter {
  convert(filename: string, format: string): { filename: string; format: string };
}

interface AudioConverter {
  extractAudio(filename: string): { audio: string };
}

class Facade {
  constructor(
    private videoConverter: VideoConverter,
    private audioConverter: AudioConverter
  ) {}

  convertToMP4(filename: string): { filename: string; format: string } {
    return this.videoConverter.convert(filename, 'mp4');
  }

  extractAudio(filename: string): { audio: string } {
    return this.audioConverter.extractAudio(filename);
  }
}
```

### Generic Facade

```typescript
class GenericFacade<T> {
  private services: Map<string, T> = new Map();

  register(name: string, service: T): void {
    this.services.set(name, service);
  }

  get(name: string): T | undefined {
    return this.services.get(name);
  }
}
```

### Service Facade

```typescript
interface UserService {
  getUser(id: string): Promise<User>;
}

interface PostService {
  getPosts(userId: string): Promise<Post[]>;
}

class APIFacade {
  constructor(
    private userService: UserService,
    private postService: PostService
  ) {}

  async getUserWithPosts(id: string): Promise<{ user: User; posts: Post[] }> {
    const user = await this.userService.getUser(id);
    const posts = await this.postService.getPosts(id);
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

- TypeScript Handbook: Interfaces
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Clean Architecture" by Robert C. Martin

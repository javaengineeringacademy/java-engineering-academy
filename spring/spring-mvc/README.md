# Module 75: Spring MVC

## Overview
Master Spring MVC framework for building web applications and REST APIs. This module covers controllers, request mapping, form handling, validation, view resolvers, and RESTful web services.

## Learning Objectives
- Understand Spring MVC architecture
- Create controllers with @Controller and @RestController
- Handle HTTP requests with @RequestMapping
- Implement form validation
- Build RESTful APIs
- Configure view resolvers
- Handle exceptions globally

## Prerequisites
- Module 33: Spring Core
- Module 14: Spring Framework
- Java fundamentals
- HTTP protocol basics

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | MVC Architecture | Model-View-Controller pattern in Spring |
| 02 | DispatcherServlet | Front controller pattern |
| 03 | Controllers | @Controller and @RestController |
| 04 | Request Mapping | @RequestMapping, @GetMapping, @PostMapping |
| 05 | Request Parameters | @RequestParam, @PathVariable, @RequestBody |
| 06 | Form Handling | Form binding, validation |
| 07 | View Resolvers | JSP, Thymeleaf configuration |
| 08 | Exception Handling | @ExceptionHandler, @ControllerAdvice |
| 09 | REST API | RESTful web services |
| 10 | Interceptors | HandlerInterceptor |
| 11 | File Upload | Multipart file handling |
| 12 | CORS Configuration | Cross-Origin Resource Sharing |
| 13 | Content Negotiation | JSON, XML responses |
| 14 | HATEOAS | Hypermedia links |
| 15 | Mini Project | Complete REST API with CRUD operations |

## Key Concepts

### MVC Architecture
```
Client → DispatcherServlet → Controller → Service → Repository → Database
                              ↓
                           Model + View → Response
```

### Controller Example
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User saved = userService.save(user);
        URI location = URI.create("/api/users/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }
}
```

## Module Structure
```
75-spring-mvc/
├── README.md
├── pom.xml
├── src/main/java/academy/javaengineering/springmvc/
│   ├── controller/
│   ├── model/
│   ├── service/
│   ├── exception/
│   ├── config/
│   └── interceptor/
└── src/test/java/academy/javaengineering/springmvc/
```

## References
- [Spring MVC Documentation](https://docs.spring.io/spring-framework/reference/web.html)
- [Spring MVC Tutorial](https://www.baeldung.com/spring-mvc)

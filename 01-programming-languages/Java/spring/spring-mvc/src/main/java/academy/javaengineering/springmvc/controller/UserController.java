package academy.javaengineering.springmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import academy.javaengineering.springmvc.model.User;
import academy.javaengineering.springmvc.service.UserService;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * REST Controller for User operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, 
                                           @RequestBody @Valid User user) {
        return userService.findById(id)
            .map(existing -> {
                user.setId(id);
                return ResponseEntity.ok(userService.save(user));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(existing -> {
                userService.delete(id);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        List<User> filtered = userService.findAll().stream()
            .filter(u -> u.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();
        return ResponseEntity.ok(filtered);
    }
}

package com.nahap.todo_web_app.controller;

import com.nahap.todo_web_app.entity.Task;
import com.nahap.todo_web_app.entity.User;
import com.nahap.todo_web_app.service.TaskService;
import com.nahap.todo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/notes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> getUserNotes(@PathVariable int id) {
        return taskService.getTasksByUserId(id);
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleUserBlock(@PathVariable int id, @RequestBody BlockUserRequest request) {
        try {
            User user = userService.getUser(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            user.setEnabled(!request.isBlocked()); // Инвертируем значение
            userService.saveUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // DTO для запроса блокировки
    public static class BlockUserRequest {
        private boolean blocked;

        public boolean isBlocked() {
            return blocked;
        }

        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }
    }
}
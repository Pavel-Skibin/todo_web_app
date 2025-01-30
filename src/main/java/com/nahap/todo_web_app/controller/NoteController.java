package com.nahap.todo_web_app.controller;

import com.nahap.todo_web_app.aspect.NoteAccessCheck;
import com.nahap.todo_web_app.entity.Task;
import com.nahap.todo_web_app.entity.User;
import com.nahap.todo_web_app.service.TaskService;
import com.nahap.todo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> showAllNotes() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    @NoteAccessCheck
    public Task showNote(@PathVariable int id) {
        return taskService.getTask(id);
    }

    @GetMapping("/user/{userId}")
    public List<Task> showUserNotes(@PathVariable int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByUsername(authentication.getName());

        // Проверяем, что пользователь запрашивает свои заметки или это админ
        if (currentUser.getId() == userId || authentication.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return taskService.getTasksByUserId(userId);
        }
        throw new AccessDeniedException("Access denied to notes");
    }

    @PostMapping
    public Task saveNote(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByUsername(authentication.getName());
        task.setUser(currentUser);
        taskService.saveTask(task);
        return task;
    }


    // остальные методы для работы с заметками


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable int id,
            @RequestBody TaskUpdateRequest request,
            Authentication authentication) {

        //  User currentUser = userService.getUserByUsername(authentication.getName());
        Task existingTask = taskService.getTask(id);

        existingTask.setText(request.getText());
        existingTask.setCompleted(request.isCompleted());
//        existingTask.setUser(currentUser);

        taskService.saveTask(existingTask);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    // DTO для обновления
    public static class TaskUpdateRequest {
        private String text;
        private boolean completed;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }


}
package com.nahap.todo_web_app.controller;


import com.nahap.todo_web_app.entity.Task;
import com.nahap.todo_web_app.entity.User;
import com.nahap.todo_web_app.service.TaskService;
import com.nahap.todo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControllerAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/users")
    public List<User>showAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User showUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "User removed !! " + id;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @GetMapping("/notes")
    public List<Task> showAllNotes() {
        return taskService.getTasks();
    }

    @GetMapping("/notes/{id}")
    public Task showNote(@PathVariable int id) {
        return taskService.getTask(id);
    }

    @PostMapping("/notes")
    public Task saveNote(@RequestBody Task task) {
        taskService.saveTask(task);
        return task;
    }

    @PutMapping("/notes")
    public Task updateNote(@RequestBody Task note) {
        taskService.saveTask(note);
        return note;
    }

    @DeleteMapping("/notes/{id}")
    public String deleteNote(@PathVariable int id) {
        taskService.deleteTask(id);
        return "Note removed !! " + id;
    }



    @GetMapping("/users/{id}/notes")
    public List<Task> showUserNotes(@PathVariable int id) {
        return taskService.getTasksByUserId(id);
    }






}

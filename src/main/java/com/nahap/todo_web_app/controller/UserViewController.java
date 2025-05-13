package com.nahap.todo_web_app.controller;

import com.nahap.todo_web_app.entity.Task;
import com.nahap.todo_web_app.entity.User;
import com.nahap.todo_web_app.service.TaskService;
import com.nahap.todo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-management";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUserDetails(@PathVariable int id, Model model) {
        User user = userService.getUser(id);
        List<Task> tasks = taskService.getTasksByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        return "user-notes";
    }
}
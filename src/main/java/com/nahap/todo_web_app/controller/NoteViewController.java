package com.nahap.todo_web_app.controller;


import com.nahap.todo_web_app.entity.Task;

import com.nahap.todo_web_app.service.TaskService;
import com.nahap.todo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteViewController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showNotes(Authentication authentication, Model model) {
        User securityUser = (User) authentication.getPrincipal();
        boolean isAdmin = securityUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Task> tasks;
        if (isAdmin) {
            tasks = taskService.getTasks(); // для админа все задачи
        } else {
            com.nahap.todo_web_app.entity.User user = userService.getUserByUsername(securityUser.getUsername());
            tasks = taskService.getTasksByUserId(user.getId()); // для пользователя только его задачи
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("isAdmin", isAdmin);

        return "notes";
    }
}
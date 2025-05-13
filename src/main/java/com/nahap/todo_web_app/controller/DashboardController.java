package com.nahap.todo_web_app.controller;

import com.nahap.todo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User securityUser = (User) authentication.getPrincipal();
        String username = securityUser.getUsername();

        com.nahap.todo_web_app.entity.User user = userService.getUserByUsername(username);

        String role = securityUser.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))
                .findFirst()
                .orElse("USER");

        if ("ADMIN".equals(role)) {
            model.addAttribute("message", "Welcome Administrator " + username);
            model.addAttribute("notesUrl", "/notes");
        } else {
            model.addAttribute("message", "Welcome " + username);
            model.addAttribute("notesUrl", "/notes");
        }

        model.addAttribute("isAdmin", "ADMIN".equals(role));

        return "dashboard";
    }
}
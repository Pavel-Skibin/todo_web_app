package com.nahap.todo_web_app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        String role = user.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))  // Убираем префикс "ROLE_"
                .findFirst()
                .orElse("USER");

        if ("ADMIN".equals(role)) {
            model.addAttribute("message", "Welcome Administrator " + username);
        } else {
            model.addAttribute("message", "Welcome " + username);
        }

        return "dashboard";  // Имя шаблона
    }
}

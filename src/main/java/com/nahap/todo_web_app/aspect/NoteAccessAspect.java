package com.nahap.todo_web_app.aspect;

import com.nahap.todo_web_app.entity.Task;
import com.nahap.todo_web_app.entity.User;
import com.nahap.todo_web_app.service.TaskService;
import com.nahap.todo_web_app.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(10)
public class NoteAccessAspect {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Around("@annotation(NoteAccessCheck) && args(taskId,..)")
    public Object checkAccess(ProceedingJoinPoint joinPoint, Integer taskId) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByUsername(authentication.getName());

        // Если админ - разрешаем доступ
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return joinPoint.proceed();
        }

        // Проверяем принадлежность заметки пользователю
        Task task = taskService.getTask(taskId);
        if (task.getUser().getId() == currentUser.getId()) {
            return joinPoint.proceed();
        }

        throw new AccessDeniedException("Access denied to note: " + taskId);
    }
}
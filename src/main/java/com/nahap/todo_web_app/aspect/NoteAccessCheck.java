package com.nahap.todo_web_app.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Создаем аннотацию для проверки прав
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoteAccessCheck {
    // можно добавить параметры аннотации если потребуется
}
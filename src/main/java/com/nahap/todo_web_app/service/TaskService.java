package com.nahap.todo_web_app.service;

import com.nahap.todo_web_app.entity.Task;

import java.util.List;

public interface TaskService {

     List<Task> getTasks();

     Task getTask(int id);

     void saveTask(Task task);

     void deleteTask(int id);

     List<Task> getTasksByUserId(int id);

}

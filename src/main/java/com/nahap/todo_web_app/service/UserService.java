package com.nahap.todo_web_app.service;

import com.nahap.todo_web_app.entity.User;

import java.util.List;

public interface UserService {

    User getUser(int id);

    List<User> getAllUsers();

    void saveUser(User user);

    void deleteUser(int id);

    User getUserByUsername(String username);



}

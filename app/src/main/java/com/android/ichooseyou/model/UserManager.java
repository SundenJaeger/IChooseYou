package com.android.ichooseyou.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public enum UserManager {
    INSTANCE;

    private List<User> userList;

    UserManager() {
        userList = new ArrayList<>();
    }

    public void updateUser(User oldUser, User newUser) {
        int index = userList.indexOf(oldUser);
        if (index != -1) {
            userList.set(index, newUser);
        }
    }

    public boolean isUserDuplicate(String username) {
        return userList.stream().anyMatch(user -> user.getName().equals(username));
    }

    public boolean isUserExists(String username) {
        return userList.stream().anyMatch(user -> user.getName().equals(username));
    }

    public boolean isValidLogin(String username, String password) {
        return userList.stream().anyMatch(user -> user.getName().equals(username) && user.getPassword().equals(password));
    }

    public List<User> getUserList() {
        return userList;
    }
}

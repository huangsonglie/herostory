package com.zl.model;

import com.zl.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UserManager {

    private static final Map<Integer, User> _userMap = new ConcurrentHashMap<>();

    private UserManager() {

    }

    public static void addUser(User user) {
        _userMap.putIfAbsent(user.userId, user);
    }

    public static void remove(Integer userId) {
        _userMap.remove(userId);
    }

    public static User getUser(int userId) {
        return _userMap.get(userId);
    }

    public static void updateUser(User user) {
        _userMap.put(user.userId, user);
    }

    public static Collection<User> getAllUser() {
        return _userMap.values();
    }
}

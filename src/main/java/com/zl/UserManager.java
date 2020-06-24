package com.zl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class UserManager {

    private static final Map<Integer, User> _userMap = new HashMap<>();

    private UserManager() {

    }

    public static void addUser(User user) {
        _userMap.putIfAbsent(user.userId, user);
    }

    public static void remove(Integer userId) {
        _userMap.remove(userId);
    }

    public static Collection<User> getAllUser() {
        return _userMap.values();
    }
}

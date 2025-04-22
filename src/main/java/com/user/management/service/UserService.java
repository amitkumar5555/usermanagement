package com.user.management.service;

import com.user.management.model.User;

public interface UserService {
    Object createUser(User user);
    Object getUserDetails(String userId);
    Object deleteUser(String userId);
    Object updateUser(User user);
}

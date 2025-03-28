package com.hart.employee_management.services.user;

import com.hart.employee_management.model.User;

public interface IUserService {
    User createUser(User user);
    User updateUser(User user);
    User findUserByEmail(String email);

}
